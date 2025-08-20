#!/usr/bin/env python3
import os
import sys
import subprocess
import difflib
import time
import signal
import multiprocessing
from concurrent.futures import ProcessPoolExecutor, as_completed

def compile_assembly(asm_file):
    """编译.s文件，生成可执行文件"""
    # 使用基于文件名的唯一可执行文件名，避免并行冲突
    basename = os.path.splitext(os.path.basename(asm_file))[0]
    executable = f"{basename}.out"
    try:
        subprocess.run(
            ["clang", "-march=rv64gc", "-mabi=lp64d", "-static", asm_file, "sylib.c", "-o", executable],
            check=True,
            stderr=subprocess.PIPE
        )
        return executable
    except subprocess.CalledProcessError as e:
        return None

def run_test(executable, input_file=None, timeout=5):
    """运行可执行文件，返回标准输出和返回码"""
    try:
        stdin = None
        if input_file and os.path.exists(input_file):
            with open(input_file, 'r') as f:
                stdin = f.read()
                
        process = subprocess.Popen(
            ["qemu-riscv64", executable],
            stdin=subprocess.PIPE if stdin else None,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )
        
        stdout, stderr = process.communicate(input=stdin, timeout=timeout)
        return_code = process.returncode
        
        # 只返回实际输出和返回码，不再合并
        return stdout, return_code
    except subprocess.TimeoutExpired:
        process.kill()
        return "超时", -1
    except Exception as e:
        return f"错误: {str(e)}", -1

def read_expected_file(expected_file):
    """从预期输出文件中分别读取预期输出内容和返回码"""
    if not os.path.exists(expected_file):
        return None, None
        
    with open(expected_file, 'r') as f:
        content = f.read()
    
    # 从末尾开始寻找最后一个数字（返回码）
    lines = content.splitlines()
    if not lines:
        return "", 0
    
    # 最后一行应该是返回码
    expected_return_code_str = lines[-1].strip()
    try:
        expected_return_code = int(expected_return_code_str)
        # 合并除了最后一行（返回码）之外的所有行作为预期输出
        expected_output = '\n'.join(lines[:-1])
        if expected_output:
            expected_output += '\n'  # 如果有内容，添加一个换行符
    except ValueError:
        # 如果最后一行不是一个整数，则假设整个内容都是输出，没有返回码
        expected_output = content
        expected_return_code = 0
    
    # 去除首尾空格、换行符和其他不可见字符
    expected_output = expected_output.strip()
    
    return expected_output, expected_return_code

def compare_outputs(actual_output, actual_return_code, expected_file):
    """比较实际输出和预期输出，返回是否通过以及差异信息"""
    expected_output, expected_return_code = read_expected_file(expected_file)
    
    if expected_output is None:
        return False, "没有预期输出文件"
    
    # 去除首尾空格、换行符和其他不可见字符
    actual_output = actual_output.strip()
    
    # 分别比较输出内容和返回码
    output_match = actual_output == expected_output
    return_code_match = actual_return_code == expected_return_code
    
    if output_match and return_code_match:
        return True, "测试通过"
    
    # 生成差异信息
    diff_info = []
    if not output_match:
        diff_info.append(f"输出不匹配:\n预期: '{expected_output}'\n实际: '{actual_output}'")
    if not return_code_match:
        diff_info.append(f"返回码不匹配: 预期 {expected_return_code}, 实际 {actual_return_code}")
    
    return False, "\n".join(diff_info)

def test_assembly_file(asm_file, directory):
    """测试单个汇编文件，返回测试结果和详细信息"""
    basename = os.path.splitext(os.path.basename(asm_file))[0]
    input_file = os.path.join(directory, f"{basename[:-3]}.in")
    output_file = os.path.join(directory, f"{basename[:-3]}.out")
    
    results = {
        "file": basename,
        "passed": False,
        "message": "",
        "actual_output": "",
        "actual_return_code": 0,
        "expected_output": "",
        "expected_return_code": 0
    }
    
    # 编译
    executable = compile_assembly(asm_file)
    if not executable:
        results["message"] = f"编译失败: {asm_file}"
        return results
        
    # 运行测试
    actual_output, actual_return_code = run_test(
        executable, 
        input_file if os.path.exists(input_file) else None
    )
    
    results["actual_output"] = actual_output
    results["actual_return_code"] = actual_return_code
    
    # 比较输出
    if os.path.exists(output_file):
        expected_output, expected_return_code = read_expected_file(output_file)
        results["expected_output"] = expected_output
        results["expected_return_code"] = expected_return_code
        
        passed, message = compare_outputs(actual_output, actual_return_code, output_file)
        results["passed"] = passed
        results["message"] = f"{'测试通过' if passed else '测试失败'}: {basename}"
        results["diff_message"] = message if not passed else ""
    else:
        results["message"] = f"警告: 没有找到输出文件 {output_file}，无法验证结果"
    
    # 清理可执行文件，避免冲突
    try:
        if executable and os.path.exists(executable):
            os.remove(executable)
    except:
        pass
        
    return results

def worker(args):
    """工作进程函数，用于并行执行测试"""
    asm_file, directory = args
    return test_assembly_file(asm_file, directory)

def main():
    """主函数"""
    if len(sys.argv) < 2:
        print("用法: python script.py <目录路径> [并行进程数]")
        sys.exit(1)
        
    directory = sys.argv[1]
    if not os.path.isdir(directory):
        print(f"错误: {directory} 不是一个有效的目录")
        sys.exit(1)
        
    # 获取目录下所有的.s文件
    assembly_files = [os.path.join(directory, f) for f in os.listdir(directory) if f.endswith('.s')]
    
    if not assembly_files:
        print(f"在 {directory} 中没有找到.s文件")
        sys.exit(0)
    
    # 确定并行进程数，如果用户没有指定，默认使用CPU核心数
    num_processes = int(sys.argv[2]) if len(sys.argv) > 2 else None
    if not num_processes:
        num_processes = multiprocessing.cpu_count()
    
    print(f"开始测试 {len(assembly_files)} 个文件，使用 {num_processes} 个并行进程...")
    start_time = time.time()
    
    # 测试统计
    total = len(assembly_files)
    passed = 0
    failed = 0
    
    # 准备参数
    args_list = [(asm_file, directory) for asm_file in assembly_files]
    
    # 使用进程池并行执行测试
    with ProcessPoolExecutor(max_workers=num_processes) as executor:
        future_to_file = {executor.submit(worker, args): args[0] for args in args_list}
        
        for future in as_completed(future_to_file):
            result = future.result()
            print(f"\n{result['message']}")
            
            if result["passed"]:
                passed += 1
            else:
                failed += 1
                if "diff_message" in result and result["diff_message"]:
                    print(result["diff_message"][:1000])
                print(f"实际输出: '{result['actual_output'][:1000]}', 返回码: {result['actual_return_code']}")
                print(f"预期输出: '{result['expected_output'][:1000]}', 返回码: {result['expected_return_code']}")
    
    end_time = time.time()
    elapsed_time = end_time - start_time
    
    # 打印测试结果摘要
    print("\n测试摘要:")
    print(f"总计: {total}")
    print(f"通过: {passed}")
    print(f"失败: {failed}")
    print(f"总耗时: {elapsed_time:.2f} 秒")

if __name__ == "__main__":
    main()
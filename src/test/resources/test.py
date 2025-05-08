#!/usr/bin/env python3
import os
import sys
import subprocess
import difflib
import time
import signal

def compile_assembly(asm_file):
    """编译.s文件，生成可执行文件"""
    executable = "a.out"
    try:
        subprocess.run(
            ["riscv64-linux-gnu-gcc", "-march=rv64gc", "-mabi=lp64d", "-static", asm_file, "sylib.c", "-o", executable],
            check=True,
            stderr=subprocess.PIPE
        )
        return executable
    except subprocess.CalledProcessError as e:
        print(f"编译失败 {asm_file}: {e.stderr.decode('utf-8')}")
        return None

def run_test(executable, input_file=None, timeout=100):
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
        
        # 标准输出 + 换行 + 返回码
        return stdout, return_code
    except subprocess.TimeoutExpired:
        process.kill()
        return "超时", -1
    except Exception as e:
        return f"错误: {str(e)}", -1

def compare_outputs(actual, expected_file):
    """比较实际输出和预期输出"""
    if not os.path.exists(expected_file):
        print(f"找不到预期输出文件 {expected_file}")
        return False
    
    with open(expected_file, 'r') as f:
        expected = f.read()
    
    if actual == expected:
        return True
    else:
        print("输出不匹配，差异如下:")
        print("预期输出：")
        print(expected)
        print("实际输出：")
        print(actual)
        return False

def test_assembly_file(asm_file, directory):
    """测试单个汇编文件"""
    print(f"\n测试文件: {asm_file}")
    
    basename = os.path.splitext(os.path.basename(asm_file))[0]
    input_file = os.path.join(directory, f"{basename[:-3]}.in")
    output_file = os.path.join(directory, f"{basename[:-3]}.out")
    
    # 编译
    executable = compile_assembly(asm_file)
    if not executable:
        return False
    
    # 运行测试
    actual_output, return_code = run_test(
        executable, 
        input_file if os.path.exists(input_file) else None
    )

    if actual_output == "":
        actual_output = str(return_code) + "\n"
    else:
        actual_output = actual_output + ("" if actual_output.endswith("\n") else "\n") + str(return_code) + "\n"
    
    # 比较输出
    if os.path.exists(output_file):
        if compare_outputs(actual_output, output_file):
            print(f"测试通过: {basename}")
            return True
        else:
            print(f"测试失败: {basename}")
            return False
    else:
        print(f"警告: 没有找到输出文件 {output_file}，无法验证结果")
        print(f"实际输出:\n{actual_output}")
        return False

def main():
    """主函数"""
    if len(sys.argv) < 2:
        print("用法: python script.py <目录路径>")
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
    
    # 测试统计
    total = len(assembly_files)
    passed = 0
    failed = 0
    
    # 执行测试
    for asm_file in assembly_files:
        if test_assembly_file(asm_file, directory):
            passed += 1
        else:
            failed += 1
    
    # 打印测试结果摘要
    print("\n测试摘要:")
    print(f"总计: {total}")
    print(f"通过: {passed}")
    print(f"失败: {failed}")

if __name__ == "__main__":
    main()

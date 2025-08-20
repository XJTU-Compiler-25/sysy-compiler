  .globl $n
  .data
  .align 2
  .type $n, @object
  .size $n, 4
$n:
  .zero 4
  .align 1
  .globl main
  .text
  .type main, @function
main:
bb212:
  sd fp, -8(sp)
  sd ra, -16(sp)
  mv fp, sp
  addi sp, sp, -72
  mv t5, s2
  sw t5, -64(fp)
  mv t5, s1
  sw t5, -60(fp)
  la t5, $n
  li t6, 10
  sw t6, 0(t5)
  addi t5, fp, -56
  mv a0, t5
  li t6, 4
  sw t6, 0(a0)
  addi t5, fp, -56
  addi t5, t5, 4
  mv a1, t5
  li t6, 3
  sw t6, 0(a1)
  addi t5, fp, -56
  addi t5, t5, 8
  mv a1, t5
  li t6, 9
  sw t6, 0(a1)
  addi t5, fp, -56
  addi t5, t5, 12
  mv a1, t5
  li t6, 2
  sw t6, 0(a1)
  addi t5, fp, -56
  addi t5, t5, 16
  mv a1, t5
  sw zero, 0(a1)
  addi t5, fp, -56
  addi t5, t5, 20
  mv a1, t5
  li t6, 1
  sw t6, 0(a1)
  addi t5, fp, -56
  addi t5, t5, 24
  mv a1, t5
  li t6, 6
  sw t6, 0(a1)
  addi t5, fp, -56
  addi t5, t5, 28
  mv a1, t5
  li t6, 5
  sw t6, 0(a1)
  addi t5, fp, -56
  addi t5, t5, 32
  mv a1, t5
  li t6, 7
  sw t6, 0(a1)
  addi t5, fp, -56
  addi t5, t5, 36
  mv a1, t5
  li t6, 8
  sw t6, 0(a1)
  mv a1, zero
  li t5, 1
  mv a2, t5
  call swap
  la t5, $n
  lw a0, 0(t5)
  slt a0, zero, a0
  bnez a0, bb583
bb585:
  j bb266
bb266:
  mv a0, zero
  j bb213
bb583:
  mv a0, zero
  j bb264
bb264:
  addi t5, fp, -56
  li t4, 4
  mul t4, a0, t4
  add t5, t5, t4
  mv a1, t5
  lw a1, 0(a1)
  mv t5, a0
  sw t5, -68(fp)
  mv a0, a1
  call putint
  lw t5, -68(fp)
  mv a0, t5
  mv t5, a0
  sw t5, -72(fp)
  li t5, 10
  mv a0, t5
  call putch
  lw t5, -72(fp)
  mv a0, t5
  j bb265
bb265:
  la t5, $n
  lw a1, 0(t5)
  li t6, 1
  addw a0, a0, t6
  slt a1, a0, a1
  bnez a1, bb587
bb589:
  j bb266
bb587:
  j bb264
bb213:
  ld ra, -16(fp)
  ld fp, -8(fp)
  addi sp, sp, 72
  lw t5, -60(fp)
  mv s1, t5
  lw t5, -64(fp)
  mv s2, t5
  ret
  .size main, .-main
  .align 1
  .globl heap_ajust
  .text
  .type heap_ajust, @function
heap_ajust:
bb41:
  sd fp, -8(sp)
  sd ra, -16(sp)
  mv fp, sp
  addi sp, sp, -52
  mv t5, s4
  sw t5, -32(fp)
  mv t5, s3
  sw t5, -28(fp)
  mv t5, s2
  sw t5, -24(fp)
  mv t5, s1
  sw t5, -20(fp)
  li t6, 2
  mulw a3, a1, t6
  li t6, 1
  addw a3, a3, t6
  li t6, 1
  addw a4, a2, t6
  slt a5, a3, a4
  bnez a5, bb365
bb575:
  j bb62
bb62:
  mv a0, zero
  j bb42
bb365:
  mv t3, a3
  mv a3, a1
  mv a1, t3
  j bb60
bb60:
  slt a5, a1, a2
  bnez a5, bb72
bb577:
  j bb71
bb71:
  mv t5, a0
  li t4, 4
  mul t4, a3, t4
  add t5, t5, t4
  mv a5, t5
  lw a5, 0(a5)
  mv t5, a0
  li t4, 4
  mul t4, a1, t4
  add t5, t5, t4
  mv a6, t5
  lw a6, 0(a6)
  slt a5, a6, a5
  bnez a5, bb92
bb93:
  mv t5, a0
  sd t5, -40(fp)
  mv a0, a0
  mv t5, a1
  sw t5, -44(fp)
  mv a1, a3
  mv t5, a2
  sw t5, -48(fp)
  mv a2, a1
  mv t5, a4
  sw t5, -52(fp)
  call swap
  lw t5, -44(fp)
  mv a1, t5
  lw t5, -52(fp)
  mv a4, t5
  lw t5, -48(fp)
  mv a2, t5
  ld t5, -40(fp)
  mv a0, t5
  j bb61
bb61:
  li t6, 2
  mulw a3, a1, t6
  li t6, 1
  addw a3, a3, t6
  slt a5, a3, a4
  bnez a5, bb579
bb581:
  j bb62
bb579:
  mv t3, a3
  mv a3, a1
  mv a1, t3
  j bb60
bb92:
  mv a0, zero
  j bb42
bb72:
  mv t5, a0
  li t4, 4
  mul t4, a1, t4
  add t5, t5, t4
  mv a5, t5
  lw a5, 0(a5)
  li t6, 1
  addw a6, a1, t6
  mv t5, a0
  li t4, 4
  mul t4, a6, t4
  add t5, t5, t4
  mv a7, t5
  lw a7, 0(a7)
  slt a5, a5, a7
  bnez a5, bb571
bb573:
  j bb71
bb571:
  mv a1, a6
  j bb71
bb42:
  ld ra, -16(fp)
  ld fp, -8(fp)
  addi sp, sp, 52
  lw t5, -20(fp)
  mv s1, t5
  lw t5, -32(fp)
  mv s4, t5
  lw t5, -28(fp)
  mv s3, t5
  lw t5, -24(fp)
  mv s2, t5
  ret
  .size heap_ajust, .-heap_ajust
  .align 1
  .globl swap
  .text
  .type swap, @function
swap:
bb11:
  sd fp, -8(sp)
  sd ra, -16(sp)
  mv fp, sp
  addi sp, sp, -16
  mv t5, a0
  li t4, 4
  mul t4, a1, t4
  add t5, t5, t4
  mv a1, t5
  lw a3, 0(a1)
  mv t5, a0
  li t4, 4
  mul t4, a2, t4
  add t5, t5, t4
  mv a0, t5
  lw a2, 0(a0)
  sw a2, 0(a1)
  sw a3, 0(a0)
  mv a0, zero
  j bb12
bb12:
  ld ra, -16(fp)
  ld fp, -8(fp)
  addi sp, sp, 16
  ret
  .size swap, .-swap

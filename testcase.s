  .globl $n
  .bss
  .align 2
  .type $n, @object
  .size $n, 4
$n:
  .zero 4
  .align 1
  .globl bubblesort
  .text
  .type bubblesort, @function
bubblesort:
  addi sp, sp, -48
  sd fp, 40(sp)
  addi fp, sp, 48
  li a0, 0
  sw a0, -16(fp)
.L1:
  lw a0, -16(fp)
  slt a0, t0, a0
  la a0, $n
  lw a0, 0(a0)
  subw a0, t0, a0
  li a0, 1
  beqz a0, .L2
  li a0, 0
  sw a0, -20(fp)
.L3:
  lw a0, -20(fp)
  slt a0, t0, a0
  la a0, $n
  lw a0, 0(a0)
  subw a0, t0, a0
  lw a0, -16(fp)
  subw a0, t0, a0
  li a0, 1
  beqz a0, .L4
  lw a0, -20(fp)
  slli a0, a0, 2
  ld t0, 0(fp)
  add t0, t0, a0
  lw a0, 0(t0)
  slt a0, a0, t0
  lw a0, -20(fp)
  addw a0, t0, a0
  li a0, 1
  slli a0, a0, 2
  ld t0, 0(fp)
  add t0, t0, a0
  lw a0, 0(t0)
  beqz a0, .L6
  lw a0, -20(fp)
  addw a0, t0, a0
  li a0, 1
  slli a0, a0, 2
  ld t0, 0(fp)
  add t0, t0, a0
  lw a0, 0(t0)
  sw a0, -24(fp)
  lw a0, -20(fp)
  slli a0, a0, 2
  ld t0, 0(fp)
  add t0, t0, a0
  lw a0, 0(t0)
  sw a0, -28(fp)
  lw a0, -20(fp)
  addw a0, t0, a0
  li a0, 1
  slli a0, a0, 2
  ld t0, 0(fp)
  add a0, t0, a0
  lw t0, -28(fp)
  sw t0, 0(a0)
  lw a0, -24(fp)
  sw a0, -28(fp)
  lw a0, -20(fp)
  slli a0, a0, 2
  ld t0, 0(fp)
  add a0, t0, a0
  lw t0, -28(fp)
  sw t0, 0(a0)
  j .L5
.L6:
.L5:
  lw a0, -20(fp)
  addw a0, t0, a0
  li a0, 1
  sw a0, -20(fp)
  j .L3
.L4:
  lw a0, -16(fp)
  addw a0, t0, a0
  li a0, 1
  sw a0, -16(fp)
  j .L1
.L2:
  li a0, 0
  j .L0
  mv a0, zero
.L0:
  ld fp, 40(sp)
  addi sp, sp, 48
  ret
  .size bubblesort, .-bubblesort
  .align 1
  .globl main
  .text
  .type main, @function
main:
  addi sp, sp, -112
  sd ra, 104(sp)
  sd fp, 96(sp)
  addi fp, sp, 112
  li a0, 10
  sw a0, $n, t0
  li a0, 4
  sw a0, -64(fp)
  li a0, 0
  slli a0, a0, 2
  addi t0, fp, -60
  add a0, t0, a0
  lw t0, -64(fp)
  sw t0, 0(a0)
  li a0, 3
  sw a0, -64(fp)
  li a0, 1
  slli a0, a0, 2
  addi t0, fp, -60
  add a0, t0, a0
  lw t0, -64(fp)
  sw t0, 0(a0)
  li a0, 9
  sw a0, -64(fp)
  li a0, 2
  slli a0, a0, 2
  addi t0, fp, -60
  add a0, t0, a0
  lw t0, -64(fp)
  sw t0, 0(a0)
  li a0, 2
  sw a0, -64(fp)
  li a0, 3
  slli a0, a0, 2
  addi t0, fp, -60
  add a0, t0, a0
  lw t0, -64(fp)
  sw t0, 0(a0)
  li a0, 0
  sw a0, -64(fp)
  li a0, 4
  slli a0, a0, 2
  addi t0, fp, -60
  add a0, t0, a0
  lw t0, -64(fp)
  sw t0, 0(a0)
  li a0, 1
  sw a0, -64(fp)
  li a0, 5
  slli a0, a0, 2
  addi t0, fp, -60
  add a0, t0, a0
  lw t0, -64(fp)
  sw t0, 0(a0)
  li a0, 6
  sw a0, -64(fp)
  li a0, 6
  slli a0, a0, 2
  addi t0, fp, -60
  add a0, t0, a0
  lw t0, -64(fp)
  sw t0, 0(a0)
  li a0, 5
  sw a0, -64(fp)
  li a0, 7
  slli a0, a0, 2
  addi t0, fp, -60
  add a0, t0, a0
  lw t0, -64(fp)
  sw t0, 0(a0)
  li a0, 7
  sw a0, -64(fp)
  li a0, 8
  slli a0, a0, 2
  addi t0, fp, -60
  add a0, t0, a0
  lw t0, -64(fp)
  sw t0, 0(a0)
  li a0, 8
  sw a0, -64(fp)
  li a0, 9
  slli a0, a0, 2
  addi t0, fp, -60
  add a0, t0, a0
  lw t0, -64(fp)
  sw t0, 0(a0)
  addi a0, fp, -60
  sd a0, -72(fp)
  ld a0, -72(fp)
  sd a0, 0(sp)
  call bubblesort
  sw a0, -64(fp)
.L8:
  lw a0, -64(fp)
  slt a0, t0, a0
  la a0, $n
  lw a0, 0(a0)
  beqz a0, .L9
  lw a0, -64(fp)
  slli a0, a0, 2
  addi t0, fp, -60
  add t0, t0, a0
  lw a0, 0(t0)
  sw a0, -68(fp)
  lw a0, -68(fp)
  sw a0, -72(fp)
  lw a0, -72(fp)
  sw a0, 4(sp)
  call putint
  li a0, 10
  sw a0, -68(fp)
  lw a0, -68(fp)
  sw a0, -72(fp)
  lw a0, -72(fp)
  sw a0, 4(sp)
  call putch
  lw a0, -64(fp)
  addw a0, t0, a0
  li a0, 1
  sw a0, -64(fp)
  j .L8
.L9:
  li a0, 0
  j .L7
  mv a0, zero
.L7:
  ld ra, 104(sp)
  ld fp, 96(sp)
  addi sp, sp, 112
  ret
  .size main, .-main

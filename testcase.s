  .align 1
  .globl main
  .text
  .type main, @function
main:
  addi sp, sp, -48
  sd fp, 40(sp)
  addi fp, sp, 48
  li a0, 0
  sw a0, -16(fp)
  li a0, 0
  sw a0, -20(fp)
  li a0, 0
  sw a0, -24(fp)
  lw a0, -16(fp)
  sw a0, -28(fp)
  li a0, 1
  lw t0, -28(fp)
  addw a0, t0, a0
  sw a0, -20(fp)
  lw a0, -16(fp)
  sw a0, -28(fp)
  li a0, 1
  lw t0, -28(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -16(fp)
  sw a0, -28(fp)
  li a0, 1
  lw t0, -28(fp)
  addw a0, t0, a0
  sw a0, -16(fp)
  lw a0, -16(fp)
  sw a0, -28(fp)
  li a0, 1
  lw t0, -28(fp)
  addw a0, t0, a0
  sw a0, -20(fp)
  lw a0, -16(fp)
  sw a0, -28(fp)
  li a0, 1
  lw t0, -28(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -16(fp)
  j .L0
  mv a0, zero
.L0:
  ld fp, 40(sp)
  addi sp, sp, 48
  ret
  .size main, .-main

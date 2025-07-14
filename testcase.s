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
  sw a0, -20(fp)
  lw a0, -16(fp)
  sw a0, -28(fp)
  li a0, 1
  lw t0, -28(fp)
  addw a0, t0, a0
  sw a0, -20(fp)
  lw a0, -20(fp)
  sw a0, -28(fp)
  lw a0, -16(fp)
  lw t0, -28(fp)
  xor a0, t0, a0
  seqz a0, a0
  beqz a0, .L2
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
  j .L1
.L2:
  lw a0, -16(fp)
  sw a0, -28(fp)
  li a0, 2
  lw t0, -28(fp)
  addw a0, t0, a0
  sw a0, -20(fp)
.L1:
  lw a0, -16(fp)
  sw a0, -28(fp)
  li a0, 1
  lw t0, -28(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -16(fp)
  sw a0, -28(fp)
  li a0, 2
  lw t0, -28(fp)
  addw a0, t0, a0
  sw a0, -20(fp)
  li a0, 0
  j .L0
  mv a0, zero
.L0:
  ld fp, 40(sp)
  addi sp, sp, 48
  ret
  .size main, .-main

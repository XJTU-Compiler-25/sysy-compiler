  .align 1
  .globl main
  .text
  .type main, @function
main:
  addi sp, sp, -208
  sd fp, 200(sp)
  addi fp, sp, 208
  li a0, 1
  sw a0, -44(fp)
  li a0, 2
  sw a0, -40(fp)
  li a0, 3
  sw a0, -36(fp)
  li a0, 4
  sw a0, -32(fp)
  sw zero, -24(fp)
  sw zero, -28(fp)
  li a0, 7
  sw a0, -20(fp)
  sw zero, -16(fp)
  li a0, 3
  sw a0, -48(fp)
  sw zero, -52(fp)
  sw zero, -56(fp)
  sw zero, -60(fp)
  sw zero, -64(fp)
  sw zero, -68(fp)
  sw zero, -72(fp)
  sw zero, -76(fp)
  sw zero, -80(fp)
  li a0, 1
  sw a0, -112(fp)
  li a0, 2
  sw a0, -108(fp)
  li a0, 3
  sw a0, -104(fp)
  li a0, 4
  sw a0, -100(fp)
  li a0, 5
  sw a0, -96(fp)
  li a0, 6
  sw a0, -92(fp)
  li a0, 7
  sw a0, -88(fp)
  li a0, 8
  sw a0, -84(fp)
  li a0, 1
  sw a0, -144(fp)
  li a0, 2
  sw a0, -140(fp)
  li a0, 3
  sw a0, -136(fp)
  sw zero, -132(fp)
  li a0, 5
  sw a0, -128(fp)
  sw zero, -124(fp)
  li a0, 3
  li t0, 2
  mul a0, t0, a0
  sd a0, -152(fp)
  li a0, 0
  ld t0, -152(fp)
  add a0, t0, a0
  slli a0, a0, 2
  addi t0, fp, -44
  add t0, t0, a0
  lw a0, 0(t0)
  sw a0, -120(fp)
  li a0, 8
  sw a0, -116(fp)
  li a0, 2
  li t0, 2
  mul a0, t0, a0
  sd a0, -184(fp)
  li a0, 1
  ld t0, -184(fp)
  add a0, t0, a0
  slli a0, a0, 2
  addi t0, fp, -144
  add t0, t0, a0
  lw a0, 0(t0)
  sw a0, -176(fp)
  li a0, 2
  li t0, 2
  mul a0, t0, a0
  sd a0, -184(fp)
  li a0, 1
  ld t0, -184(fp)
  add a0, t0, a0
  slli a0, a0, 2
  addi t0, fp, -112
  add t0, t0, a0
  lw a0, 0(t0)
  sw a0, -172(fp)
  li a0, 3
  sw a0, -168(fp)
  li a0, 4
  sw a0, -164(fp)
  li a0, 5
  sw a0, -160(fp)
  li a0, 6
  sw a0, -156(fp)
  li a0, 7
  sw a0, -152(fp)
  li a0, 8
  sw a0, -148(fp)
  li a0, 3
  li t0, 2
  mul a0, t0, a0
  sd a0, -184(fp)
  li a0, 1
  ld t0, -184(fp)
  add a0, t0, a0
  li t0, 1
  mul a0, t0, a0
  sd a0, -184(fp)
  li a0, 0
  ld t0, -184(fp)
  add a0, t0, a0
  slli a0, a0, 2
  addi t0, fp, -176
  add t0, t0, a0
  lw a0, 0(t0)
  addw a0, t0, a0
  li a0, 0
  li t0, 2
  mul a0, t0, a0
  sd a0, -192(fp)
  li a0, 0
  ld t0, -192(fp)
  add a0, t0, a0
  li t0, 1
  mul a0, t0, a0
  sd a0, -192(fp)
  li a0, 0
  ld t0, -192(fp)
  add a0, t0, a0
  slli a0, a0, 2
  addi t0, fp, -176
  add t0, t0, a0
  lw a0, 0(t0)
  addw a0, t0, a0
  li a0, 0
  li t0, 2
  mul a0, t0, a0
  sd a0, -192(fp)
  li a0, 1
  ld t0, -192(fp)
  add a0, t0, a0
  li t0, 1
  mul a0, t0, a0
  sd a0, -192(fp)
  li a0, 0
  ld t0, -192(fp)
  add a0, t0, a0
  slli a0, a0, 2
  addi t0, fp, -176
  add t0, t0, a0
  lw a0, 0(t0)
  addw a0, t0, a0
  li a0, 3
  li t0, 2
  mul a0, t0, a0
  sd a0, -192(fp)
  li a0, 0
  ld t0, -192(fp)
  add a0, t0, a0
  slli a0, a0, 2
  addi t0, fp, -144
  add t0, t0, a0
  lw a0, 0(t0)
  j .L0
  mv a0, zero
.L0:
  ld fp, 200(sp)
  addi sp, sp, 208
  ret
  .size main, .-main

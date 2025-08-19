  .align 1
  .globl main
  .text
  .type main, @function
main:
bb4:
  sd fp, -8(sp)
  sd ra, -16(sp)
  mv fp, sp
  addi sp, sp, -16
  mv a0, zero
  mv a1, zero
  j bb17
bb17:
  li t6, 50
  xor t4, a1, t6
  seqz a2, t4
  bnez a2, bb24
bb26:
  li t6, 1
  addw a0, a1, t6
  addw a1, a0, a1
  mv a0, a1
  mv a1, a0
  j bb18
bb18:
  li t6, 100
  slt a2, a1, t6
  bnez a2, bb107
bb19:
  j bb5
bb107:
  j bb17
bb24:
  li t6, 1
  addw a0, a1, t6
  mv a1, a0
  j bb18
bb5:
  ld ra, -16(fp)
  ld fp, -8(fp)
  addi sp, sp, 16
  ret
  .size main, .-main

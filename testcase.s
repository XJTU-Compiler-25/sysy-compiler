  .align 1
  .globl fib
  .text
  .type fib, @function
fib:
  addi sp, sp, -336
  sd ra, 328(sp)
  sd fp, 320(sp)
  addi fp, sp, 336
  lw a0, 4(fp)
  sw a0, -24(fp)
  li a0, 2
  lw t0, -24(fp)
  slt a0, a0, t0
  xori a0, a0, 1
  beqz a0, .L2
  li a0, 1
  j .L0
  j .L1
.L2:
.L1:
  li a0, 1
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L4
  lw a0, -36(fp)
  negw a0, a0
  sw a0, -36(fp)
  j .L3
.L4:
.L3:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L6
  lw a0, -40(fp)
  negw a0, a0
  sw a0, -40(fp)
  j .L5
.L6:
.L5:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -44(fp)
  lw a0, -44(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L8
  lw a0, -44(fp)
  negw a0, a0
  sw a0, -44(fp)
  j .L7
.L8:
.L7:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -48(fp)
  lw a0, -48(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L10
  lw a0, -48(fp)
  negw a0, a0
  sw a0, -48(fp)
  j .L9
.L10:
.L9:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -52(fp)
  lw a0, -52(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L12
  lw a0, -52(fp)
  negw a0, a0
  sw a0, -52(fp)
  j .L11
.L12:
.L11:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -56(fp)
  lw a0, -56(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L14
  lw a0, -56(fp)
  negw a0, a0
  sw a0, -56(fp)
  j .L13
.L14:
.L13:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -60(fp)
  lw a0, -60(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L16
  lw a0, -60(fp)
  negw a0, a0
  sw a0, -60(fp)
  j .L15
.L16:
.L15:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -64(fp)
  lw a0, -64(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L18
  lw a0, -64(fp)
  negw a0, a0
  sw a0, -64(fp)
  j .L17
.L18:
.L17:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -68(fp)
  lw a0, -68(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L20
  lw a0, -68(fp)
  negw a0, a0
  sw a0, -68(fp)
  j .L19
.L20:
.L19:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -72(fp)
  lw a0, -72(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L22
  lw a0, -72(fp)
  negw a0, a0
  sw a0, -72(fp)
  j .L21
.L22:
.L21:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -76(fp)
  lw a0, -76(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L24
  lw a0, -76(fp)
  negw a0, a0
  sw a0, -76(fp)
  j .L23
.L24:
.L23:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -80(fp)
  lw a0, -80(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L26
  lw a0, -80(fp)
  negw a0, a0
  sw a0, -80(fp)
  j .L25
.L26:
.L25:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -84(fp)
  lw a0, -84(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L28
  lw a0, -84(fp)
  negw a0, a0
  sw a0, -84(fp)
  j .L27
.L28:
.L27:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -88(fp)
  lw a0, -88(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L30
  lw a0, -88(fp)
  negw a0, a0
  sw a0, -88(fp)
  j .L29
.L30:
.L29:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -92(fp)
  lw a0, -92(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L32
  lw a0, -92(fp)
  negw a0, a0
  sw a0, -92(fp)
  j .L31
.L32:
.L31:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -96(fp)
  lw a0, -96(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L34
  lw a0, -96(fp)
  negw a0, a0
  sw a0, -96(fp)
  j .L33
.L34:
.L33:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -36(fp)
  seqz a0, a0
  beqz a0, .L36
  li a0, 1
  sw a0, -100(fp)
  j .L35
.L36:
  li a0, 0
  sw a0, -100(fp)
.L35:
  lw a0, -40(fp)
  seqz a0, a0
  beqz a0, .L38
  li a0, 1
  sw a0, -104(fp)
  j .L37
.L38:
  li a0, 0
  sw a0, -104(fp)
.L37:
  lw a0, -44(fp)
  seqz a0, a0
  beqz a0, .L40
  li a0, 1
  sw a0, -108(fp)
  j .L39
.L40:
  li a0, 0
  sw a0, -108(fp)
.L39:
  lw a0, -48(fp)
  seqz a0, a0
  beqz a0, .L42
  li a0, 1
  sw a0, -112(fp)
  j .L41
.L42:
  li a0, 0
  sw a0, -112(fp)
.L41:
  lw a0, -52(fp)
  seqz a0, a0
  beqz a0, .L44
  li a0, 1
  sw a0, -116(fp)
  j .L43
.L44:
  li a0, 0
  sw a0, -116(fp)
.L43:
  lw a0, -56(fp)
  seqz a0, a0
  beqz a0, .L46
  li a0, 1
  sw a0, -120(fp)
  j .L45
.L46:
  li a0, 0
  sw a0, -120(fp)
.L45:
  lw a0, -60(fp)
  seqz a0, a0
  beqz a0, .L48
  li a0, 1
  sw a0, -124(fp)
  j .L47
.L48:
  li a0, 0
  sw a0, -124(fp)
.L47:
  lw a0, -64(fp)
  seqz a0, a0
  beqz a0, .L50
  li a0, 1
  sw a0, -128(fp)
  j .L49
.L50:
  li a0, 0
  sw a0, -128(fp)
.L49:
  lw a0, -68(fp)
  seqz a0, a0
  beqz a0, .L52
  li a0, 1
  sw a0, -132(fp)
  j .L51
.L52:
  li a0, 0
  sw a0, -132(fp)
.L51:
  lw a0, -72(fp)
  seqz a0, a0
  beqz a0, .L54
  li a0, 1
  sw a0, -136(fp)
  j .L53
.L54:
  li a0, 0
  sw a0, -136(fp)
.L53:
  lw a0, -76(fp)
  seqz a0, a0
  beqz a0, .L56
  li a0, 1
  sw a0, -140(fp)
  j .L55
.L56:
  li a0, 0
  sw a0, -140(fp)
.L55:
  lw a0, -80(fp)
  seqz a0, a0
  beqz a0, .L58
  li a0, 1
  sw a0, -144(fp)
  j .L57
.L58:
  li a0, 0
  sw a0, -144(fp)
.L57:
  lw a0, -84(fp)
  seqz a0, a0
  beqz a0, .L60
  li a0, 1
  sw a0, -148(fp)
  j .L59
.L60:
  li a0, 0
  sw a0, -148(fp)
.L59:
  lw a0, -88(fp)
  seqz a0, a0
  beqz a0, .L62
  li a0, 1
  sw a0, -152(fp)
  j .L61
.L62:
  li a0, 0
  sw a0, -152(fp)
.L61:
  lw a0, -92(fp)
  seqz a0, a0
  beqz a0, .L64
  li a0, 1
  sw a0, -156(fp)
  j .L63
.L64:
  li a0, 0
  sw a0, -156(fp)
.L63:
  lw a0, -96(fp)
  seqz a0, a0
  beqz a0, .L66
  li a0, 1
  sw a0, -160(fp)
  j .L65
.L66:
  li a0, 0
  sw a0, -160(fp)
.L65:
  li a0, 0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -164(fp)
  li a0, 2
  lw t0, -164(fp)
  mulw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -160(fp)
  lw t0, -164(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -164(fp)
  li a0, 2
  lw t0, -164(fp)
  mulw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -156(fp)
  lw t0, -164(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -164(fp)
  li a0, 2
  lw t0, -164(fp)
  mulw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -152(fp)
  lw t0, -164(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -164(fp)
  li a0, 2
  lw t0, -164(fp)
  mulw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -148(fp)
  lw t0, -164(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -164(fp)
  li a0, 2
  lw t0, -164(fp)
  mulw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -144(fp)
  lw t0, -164(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -164(fp)
  li a0, 2
  lw t0, -164(fp)
  mulw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -140(fp)
  lw t0, -164(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -164(fp)
  li a0, 2
  lw t0, -164(fp)
  mulw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -136(fp)
  lw t0, -164(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -164(fp)
  li a0, 2
  lw t0, -164(fp)
  mulw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -132(fp)
  lw t0, -164(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -164(fp)
  li a0, 2
  lw t0, -164(fp)
  mulw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -128(fp)
  lw t0, -164(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -164(fp)
  li a0, 2
  lw t0, -164(fp)
  mulw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -124(fp)
  lw t0, -164(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -164(fp)
  li a0, 2
  lw t0, -164(fp)
  mulw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -120(fp)
  lw t0, -164(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -164(fp)
  li a0, 2
  lw t0, -164(fp)
  mulw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -116(fp)
  lw t0, -164(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -164(fp)
  li a0, 2
  lw t0, -164(fp)
  mulw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -112(fp)
  lw t0, -164(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -164(fp)
  li a0, 2
  lw t0, -164(fp)
  mulw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -108(fp)
  lw t0, -164(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -164(fp)
  li a0, 2
  lw t0, -164(fp)
  mulw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -104(fp)
  lw t0, -164(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -164(fp)
  li a0, 2
  lw t0, -164(fp)
  mulw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -100(fp)
  lw t0, -164(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  remw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -108(fp)
  li a0, 0
  lw t0, -108(fp)
  slt a0, t0, a0
  beqz a0, .L68
  lw a0, -40(fp)
  negw a0, a0
  sw a0, -40(fp)
  j .L67
.L68:
.L67:
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  divw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  remw a0, t0, a0
  sw a0, -44(fp)
  lw a0, -44(fp)
  sw a0, -108(fp)
  li a0, 0
  lw t0, -108(fp)
  slt a0, t0, a0
  beqz a0, .L70
  lw a0, -44(fp)
  negw a0, a0
  sw a0, -44(fp)
  j .L69
.L70:
.L69:
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  divw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  remw a0, t0, a0
  sw a0, -48(fp)
  lw a0, -48(fp)
  sw a0, -108(fp)
  li a0, 0
  lw t0, -108(fp)
  slt a0, t0, a0
  beqz a0, .L72
  lw a0, -48(fp)
  negw a0, a0
  sw a0, -48(fp)
  j .L71
.L72:
.L71:
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  divw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  remw a0, t0, a0
  sw a0, -52(fp)
  lw a0, -52(fp)
  sw a0, -108(fp)
  li a0, 0
  lw t0, -108(fp)
  slt a0, t0, a0
  beqz a0, .L74
  lw a0, -52(fp)
  negw a0, a0
  sw a0, -52(fp)
  j .L73
.L74:
.L73:
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  divw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  remw a0, t0, a0
  sw a0, -56(fp)
  lw a0, -56(fp)
  sw a0, -108(fp)
  li a0, 0
  lw t0, -108(fp)
  slt a0, t0, a0
  beqz a0, .L76
  lw a0, -56(fp)
  negw a0, a0
  sw a0, -56(fp)
  j .L75
.L76:
.L75:
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  divw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  remw a0, t0, a0
  sw a0, -60(fp)
  lw a0, -60(fp)
  sw a0, -108(fp)
  li a0, 0
  lw t0, -108(fp)
  slt a0, t0, a0
  beqz a0, .L78
  lw a0, -60(fp)
  negw a0, a0
  sw a0, -60(fp)
  j .L77
.L78:
.L77:
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  divw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  remw a0, t0, a0
  sw a0, -64(fp)
  lw a0, -64(fp)
  sw a0, -108(fp)
  li a0, 0
  lw t0, -108(fp)
  slt a0, t0, a0
  beqz a0, .L80
  lw a0, -64(fp)
  negw a0, a0
  sw a0, -64(fp)
  j .L79
.L80:
.L79:
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  divw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  remw a0, t0, a0
  sw a0, -68(fp)
  lw a0, -68(fp)
  sw a0, -108(fp)
  li a0, 0
  lw t0, -108(fp)
  slt a0, t0, a0
  beqz a0, .L82
  lw a0, -68(fp)
  negw a0, a0
  sw a0, -68(fp)
  j .L81
.L82:
.L81:
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  divw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  remw a0, t0, a0
  sw a0, -72(fp)
  lw a0, -72(fp)
  sw a0, -108(fp)
  li a0, 0
  lw t0, -108(fp)
  slt a0, t0, a0
  beqz a0, .L84
  lw a0, -72(fp)
  negw a0, a0
  sw a0, -72(fp)
  j .L83
.L84:
.L83:
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  divw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  remw a0, t0, a0
  sw a0, -76(fp)
  lw a0, -76(fp)
  sw a0, -108(fp)
  li a0, 0
  lw t0, -108(fp)
  slt a0, t0, a0
  beqz a0, .L86
  lw a0, -76(fp)
  negw a0, a0
  sw a0, -76(fp)
  j .L85
.L86:
.L85:
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  divw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  remw a0, t0, a0
  sw a0, -80(fp)
  lw a0, -80(fp)
  sw a0, -108(fp)
  li a0, 0
  lw t0, -108(fp)
  slt a0, t0, a0
  beqz a0, .L88
  lw a0, -80(fp)
  negw a0, a0
  sw a0, -80(fp)
  j .L87
.L88:
.L87:
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  divw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  remw a0, t0, a0
  sw a0, -84(fp)
  lw a0, -84(fp)
  sw a0, -108(fp)
  li a0, 0
  lw t0, -108(fp)
  slt a0, t0, a0
  beqz a0, .L90
  lw a0, -84(fp)
  negw a0, a0
  sw a0, -84(fp)
  j .L89
.L90:
.L89:
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  divw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  remw a0, t0, a0
  sw a0, -88(fp)
  lw a0, -88(fp)
  sw a0, -108(fp)
  li a0, 0
  lw t0, -108(fp)
  slt a0, t0, a0
  beqz a0, .L92
  lw a0, -88(fp)
  negw a0, a0
  sw a0, -88(fp)
  j .L91
.L92:
.L91:
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  divw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  remw a0, t0, a0
  sw a0, -92(fp)
  lw a0, -92(fp)
  sw a0, -108(fp)
  li a0, 0
  lw t0, -108(fp)
  slt a0, t0, a0
  beqz a0, .L94
  lw a0, -92(fp)
  negw a0, a0
  sw a0, -92(fp)
  j .L93
.L94:
.L93:
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  divw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  remw a0, t0, a0
  sw a0, -96(fp)
  lw a0, -96(fp)
  sw a0, -108(fp)
  li a0, 0
  lw t0, -108(fp)
  slt a0, t0, a0
  beqz a0, .L96
  lw a0, -96(fp)
  negw a0, a0
  sw a0, -96(fp)
  j .L95
.L96:
.L95:
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  divw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  remw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -108(fp)
  li a0, 0
  lw t0, -108(fp)
  slt a0, t0, a0
  beqz a0, .L98
  lw a0, -100(fp)
  negw a0, a0
  sw a0, -100(fp)
  j .L97
.L98:
.L97:
  lw a0, -104(fp)
  sw a0, -108(fp)
  li a0, 2
  lw t0, -108(fp)
  divw a0, t0, a0
  sw a0, -104(fp)
  li a0, 1
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  remw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -172(fp)
  li a0, 0
  lw t0, -172(fp)
  slt a0, t0, a0
  beqz a0, .L100
  lw a0, -104(fp)
  negw a0, a0
  sw a0, -104(fp)
  j .L99
.L100:
.L99:
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  divw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  remw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -172(fp)
  li a0, 0
  lw t0, -172(fp)
  slt a0, t0, a0
  beqz a0, .L102
  lw a0, -108(fp)
  negw a0, a0
  sw a0, -108(fp)
  j .L101
.L102:
.L101:
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  divw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  remw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -172(fp)
  li a0, 0
  lw t0, -172(fp)
  slt a0, t0, a0
  beqz a0, .L104
  lw a0, -112(fp)
  negw a0, a0
  sw a0, -112(fp)
  j .L103
.L104:
.L103:
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  divw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  remw a0, t0, a0
  sw a0, -116(fp)
  lw a0, -116(fp)
  sw a0, -172(fp)
  li a0, 0
  lw t0, -172(fp)
  slt a0, t0, a0
  beqz a0, .L106
  lw a0, -116(fp)
  negw a0, a0
  sw a0, -116(fp)
  j .L105
.L106:
.L105:
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  divw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  remw a0, t0, a0
  sw a0, -120(fp)
  lw a0, -120(fp)
  sw a0, -172(fp)
  li a0, 0
  lw t0, -172(fp)
  slt a0, t0, a0
  beqz a0, .L108
  lw a0, -120(fp)
  negw a0, a0
  sw a0, -120(fp)
  j .L107
.L108:
.L107:
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  divw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  remw a0, t0, a0
  sw a0, -124(fp)
  lw a0, -124(fp)
  sw a0, -172(fp)
  li a0, 0
  lw t0, -172(fp)
  slt a0, t0, a0
  beqz a0, .L110
  lw a0, -124(fp)
  negw a0, a0
  sw a0, -124(fp)
  j .L109
.L110:
.L109:
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  divw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  remw a0, t0, a0
  sw a0, -128(fp)
  lw a0, -128(fp)
  sw a0, -172(fp)
  li a0, 0
  lw t0, -172(fp)
  slt a0, t0, a0
  beqz a0, .L112
  lw a0, -128(fp)
  negw a0, a0
  sw a0, -128(fp)
  j .L111
.L112:
.L111:
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  divw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  remw a0, t0, a0
  sw a0, -132(fp)
  lw a0, -132(fp)
  sw a0, -172(fp)
  li a0, 0
  lw t0, -172(fp)
  slt a0, t0, a0
  beqz a0, .L114
  lw a0, -132(fp)
  negw a0, a0
  sw a0, -132(fp)
  j .L113
.L114:
.L113:
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  divw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  remw a0, t0, a0
  sw a0, -136(fp)
  lw a0, -136(fp)
  sw a0, -172(fp)
  li a0, 0
  lw t0, -172(fp)
  slt a0, t0, a0
  beqz a0, .L116
  lw a0, -136(fp)
  negw a0, a0
  sw a0, -136(fp)
  j .L115
.L116:
.L115:
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  divw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  remw a0, t0, a0
  sw a0, -140(fp)
  lw a0, -140(fp)
  sw a0, -172(fp)
  li a0, 0
  lw t0, -172(fp)
  slt a0, t0, a0
  beqz a0, .L118
  lw a0, -140(fp)
  negw a0, a0
  sw a0, -140(fp)
  j .L117
.L118:
.L117:
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  divw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  remw a0, t0, a0
  sw a0, -144(fp)
  lw a0, -144(fp)
  sw a0, -172(fp)
  li a0, 0
  lw t0, -172(fp)
  slt a0, t0, a0
  beqz a0, .L120
  lw a0, -144(fp)
  negw a0, a0
  sw a0, -144(fp)
  j .L119
.L120:
.L119:
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  divw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  remw a0, t0, a0
  sw a0, -148(fp)
  lw a0, -148(fp)
  sw a0, -172(fp)
  li a0, 0
  lw t0, -172(fp)
  slt a0, t0, a0
  beqz a0, .L122
  lw a0, -148(fp)
  negw a0, a0
  sw a0, -148(fp)
  j .L121
.L122:
.L121:
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  divw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  remw a0, t0, a0
  sw a0, -152(fp)
  lw a0, -152(fp)
  sw a0, -172(fp)
  li a0, 0
  lw t0, -172(fp)
  slt a0, t0, a0
  beqz a0, .L124
  lw a0, -152(fp)
  negw a0, a0
  sw a0, -152(fp)
  j .L123
.L124:
.L123:
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  divw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  remw a0, t0, a0
  sw a0, -156(fp)
  lw a0, -156(fp)
  sw a0, -172(fp)
  li a0, 0
  lw t0, -172(fp)
  slt a0, t0, a0
  beqz a0, .L126
  lw a0, -156(fp)
  negw a0, a0
  sw a0, -156(fp)
  j .L125
.L126:
.L125:
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  divw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  remw a0, t0, a0
  sw a0, -160(fp)
  lw a0, -160(fp)
  sw a0, -172(fp)
  li a0, 0
  lw t0, -172(fp)
  slt a0, t0, a0
  beqz a0, .L128
  lw a0, -160(fp)
  negw a0, a0
  sw a0, -160(fp)
  j .L127
.L128:
.L127:
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  divw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  remw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -172(fp)
  li a0, 0
  lw t0, -172(fp)
  slt a0, t0, a0
  beqz a0, .L130
  lw a0, -164(fp)
  negw a0, a0
  sw a0, -164(fp)
  j .L129
.L130:
.L129:
  lw a0, -168(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  divw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -40(fp)
  snez a0, a0
  bnez a0, .L133
  sw a0, -300(fp)
  lw a0, -104(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L133:
  beqz a0, .L132
  li a0, 1
  sw a0, -296(fp)
  j .L131
.L132:
  li a0, 0
  sw a0, -296(fp)
.L131:
  lw a0, -40(fp)
  snez a0, a0
  beqz a0, .L136
  sw a0, -304(fp)
  lw a0, -104(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L136:
  beqz a0, .L135
  li a0, 1
  sw a0, -300(fp)
  j .L134
.L135:
  li a0, 0
  sw a0, -300(fp)
.L134:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L138
  li a0, 1
  sw a0, -304(fp)
  j .L137
.L138:
  li a0, 0
  sw a0, -304(fp)
.L137:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L141
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L141:
  beqz a0, .L140
  li a0, 1
  sw a0, -292(fp)
  j .L139
.L140:
  li a0, 0
  sw a0, -292(fp)
.L139:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L144
  sw a0, -300(fp)
  li a0, 0
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L144:
  beqz a0, .L143
  li a0, 1
  sw a0, -296(fp)
  j .L142
.L143:
  li a0, 0
  sw a0, -296(fp)
.L142:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L147
  sw a0, -304(fp)
  li a0, 0
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L147:
  beqz a0, .L146
  li a0, 1
  sw a0, -300(fp)
  j .L145
.L146:
  li a0, 0
  sw a0, -300(fp)
.L145:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L149
  li a0, 1
  sw a0, -304(fp)
  j .L148
.L149:
  li a0, 0
  sw a0, -304(fp)
.L148:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L152
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L152:
  beqz a0, .L151
  li a0, 1
  sw a0, -228(fp)
  j .L150
.L151:
  li a0, 0
  sw a0, -228(fp)
.L150:
  lw a0, -40(fp)
  snez a0, a0
  beqz a0, .L155
  sw a0, -300(fp)
  lw a0, -104(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L155:
  beqz a0, .L154
  li a0, 1
  sw a0, -296(fp)
  j .L153
.L154:
  li a0, 0
  sw a0, -296(fp)
.L153:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L158
  sw a0, -304(fp)
  li a0, 0
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L158:
  beqz a0, .L157
  li a0, 1
  sw a0, -300(fp)
  j .L156
.L157:
  li a0, 0
  sw a0, -300(fp)
.L156:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L161
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L161:
  beqz a0, .L160
  li a0, 1
  sw a0, -168(fp)
  j .L159
.L160:
  li a0, 0
  sw a0, -168(fp)
.L159:
  lw a0, -44(fp)
  snez a0, a0
  bnez a0, .L164
  sw a0, -300(fp)
  lw a0, -108(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L164:
  beqz a0, .L163
  li a0, 1
  sw a0, -296(fp)
  j .L162
.L163:
  li a0, 0
  sw a0, -296(fp)
.L162:
  lw a0, -44(fp)
  snez a0, a0
  beqz a0, .L167
  sw a0, -304(fp)
  lw a0, -108(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L167:
  beqz a0, .L166
  li a0, 1
  sw a0, -300(fp)
  j .L165
.L166:
  li a0, 0
  sw a0, -300(fp)
.L165:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L169
  li a0, 1
  sw a0, -304(fp)
  j .L168
.L169:
  li a0, 0
  sw a0, -304(fp)
.L168:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L172
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L172:
  beqz a0, .L171
  li a0, 1
  sw a0, -292(fp)
  j .L170
.L171:
  li a0, 0
  sw a0, -292(fp)
.L170:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L175
  sw a0, -300(fp)
  lw a0, -168(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L175:
  beqz a0, .L174
  li a0, 1
  sw a0, -296(fp)
  j .L173
.L174:
  li a0, 0
  sw a0, -296(fp)
.L173:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L178
  sw a0, -304(fp)
  lw a0, -168(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L178:
  beqz a0, .L177
  li a0, 1
  sw a0, -300(fp)
  j .L176
.L177:
  li a0, 0
  sw a0, -300(fp)
.L176:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L180
  li a0, 1
  sw a0, -304(fp)
  j .L179
.L180:
  li a0, 0
  sw a0, -304(fp)
.L179:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L183
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L183:
  beqz a0, .L182
  li a0, 1
  sw a0, -232(fp)
  j .L181
.L182:
  li a0, 0
  sw a0, -232(fp)
.L181:
  lw a0, -44(fp)
  snez a0, a0
  beqz a0, .L186
  sw a0, -300(fp)
  lw a0, -108(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L186:
  beqz a0, .L185
  li a0, 1
  sw a0, -296(fp)
  j .L184
.L185:
  li a0, 0
  sw a0, -296(fp)
.L184:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L189
  sw a0, -304(fp)
  lw a0, -168(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L189:
  beqz a0, .L188
  li a0, 1
  sw a0, -300(fp)
  j .L187
.L188:
  li a0, 0
  sw a0, -300(fp)
.L187:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L192
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L192:
  beqz a0, .L191
  li a0, 1
  sw a0, -172(fp)
  j .L190
.L191:
  li a0, 0
  sw a0, -172(fp)
.L190:
  lw a0, -48(fp)
  snez a0, a0
  bnez a0, .L195
  sw a0, -300(fp)
  lw a0, -112(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L195:
  beqz a0, .L194
  li a0, 1
  sw a0, -296(fp)
  j .L193
.L194:
  li a0, 0
  sw a0, -296(fp)
.L193:
  lw a0, -48(fp)
  snez a0, a0
  beqz a0, .L198
  sw a0, -304(fp)
  lw a0, -112(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L198:
  beqz a0, .L197
  li a0, 1
  sw a0, -300(fp)
  j .L196
.L197:
  li a0, 0
  sw a0, -300(fp)
.L196:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L200
  li a0, 1
  sw a0, -304(fp)
  j .L199
.L200:
  li a0, 0
  sw a0, -304(fp)
.L199:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L203
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L203:
  beqz a0, .L202
  li a0, 1
  sw a0, -292(fp)
  j .L201
.L202:
  li a0, 0
  sw a0, -292(fp)
.L201:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L206
  sw a0, -300(fp)
  lw a0, -172(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L206:
  beqz a0, .L205
  li a0, 1
  sw a0, -296(fp)
  j .L204
.L205:
  li a0, 0
  sw a0, -296(fp)
.L204:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L209
  sw a0, -304(fp)
  lw a0, -172(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L209:
  beqz a0, .L208
  li a0, 1
  sw a0, -300(fp)
  j .L207
.L208:
  li a0, 0
  sw a0, -300(fp)
.L207:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L211
  li a0, 1
  sw a0, -304(fp)
  j .L210
.L211:
  li a0, 0
  sw a0, -304(fp)
.L210:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L214
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L214:
  beqz a0, .L213
  li a0, 1
  sw a0, -236(fp)
  j .L212
.L213:
  li a0, 0
  sw a0, -236(fp)
.L212:
  lw a0, -48(fp)
  snez a0, a0
  beqz a0, .L217
  sw a0, -300(fp)
  lw a0, -112(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L217:
  beqz a0, .L216
  li a0, 1
  sw a0, -296(fp)
  j .L215
.L216:
  li a0, 0
  sw a0, -296(fp)
.L215:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L220
  sw a0, -304(fp)
  lw a0, -172(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L220:
  beqz a0, .L219
  li a0, 1
  sw a0, -300(fp)
  j .L218
.L219:
  li a0, 0
  sw a0, -300(fp)
.L218:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L223
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L223:
  beqz a0, .L222
  li a0, 1
  sw a0, -176(fp)
  j .L221
.L222:
  li a0, 0
  sw a0, -176(fp)
.L221:
  lw a0, -52(fp)
  snez a0, a0
  bnez a0, .L226
  sw a0, -300(fp)
  lw a0, -116(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L226:
  beqz a0, .L225
  li a0, 1
  sw a0, -296(fp)
  j .L224
.L225:
  li a0, 0
  sw a0, -296(fp)
.L224:
  lw a0, -52(fp)
  snez a0, a0
  beqz a0, .L229
  sw a0, -304(fp)
  lw a0, -116(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L229:
  beqz a0, .L228
  li a0, 1
  sw a0, -300(fp)
  j .L227
.L228:
  li a0, 0
  sw a0, -300(fp)
.L227:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L231
  li a0, 1
  sw a0, -304(fp)
  j .L230
.L231:
  li a0, 0
  sw a0, -304(fp)
.L230:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L234
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L234:
  beqz a0, .L233
  li a0, 1
  sw a0, -292(fp)
  j .L232
.L233:
  li a0, 0
  sw a0, -292(fp)
.L232:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L237
  sw a0, -300(fp)
  lw a0, -176(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L237:
  beqz a0, .L236
  li a0, 1
  sw a0, -296(fp)
  j .L235
.L236:
  li a0, 0
  sw a0, -296(fp)
.L235:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L240
  sw a0, -304(fp)
  lw a0, -176(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L240:
  beqz a0, .L239
  li a0, 1
  sw a0, -300(fp)
  j .L238
.L239:
  li a0, 0
  sw a0, -300(fp)
.L238:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L242
  li a0, 1
  sw a0, -304(fp)
  j .L241
.L242:
  li a0, 0
  sw a0, -304(fp)
.L241:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L245
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L245:
  beqz a0, .L244
  li a0, 1
  sw a0, -240(fp)
  j .L243
.L244:
  li a0, 0
  sw a0, -240(fp)
.L243:
  lw a0, -52(fp)
  snez a0, a0
  beqz a0, .L248
  sw a0, -300(fp)
  lw a0, -116(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L248:
  beqz a0, .L247
  li a0, 1
  sw a0, -296(fp)
  j .L246
.L247:
  li a0, 0
  sw a0, -296(fp)
.L246:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L251
  sw a0, -304(fp)
  lw a0, -176(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L251:
  beqz a0, .L250
  li a0, 1
  sw a0, -300(fp)
  j .L249
.L250:
  li a0, 0
  sw a0, -300(fp)
.L249:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L254
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L254:
  beqz a0, .L253
  li a0, 1
  sw a0, -180(fp)
  j .L252
.L253:
  li a0, 0
  sw a0, -180(fp)
.L252:
  lw a0, -56(fp)
  snez a0, a0
  bnez a0, .L257
  sw a0, -300(fp)
  lw a0, -120(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L257:
  beqz a0, .L256
  li a0, 1
  sw a0, -296(fp)
  j .L255
.L256:
  li a0, 0
  sw a0, -296(fp)
.L255:
  lw a0, -56(fp)
  snez a0, a0
  beqz a0, .L260
  sw a0, -304(fp)
  lw a0, -120(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L260:
  beqz a0, .L259
  li a0, 1
  sw a0, -300(fp)
  j .L258
.L259:
  li a0, 0
  sw a0, -300(fp)
.L258:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L262
  li a0, 1
  sw a0, -304(fp)
  j .L261
.L262:
  li a0, 0
  sw a0, -304(fp)
.L261:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L265
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L265:
  beqz a0, .L264
  li a0, 1
  sw a0, -292(fp)
  j .L263
.L264:
  li a0, 0
  sw a0, -292(fp)
.L263:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L268
  sw a0, -300(fp)
  lw a0, -180(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L268:
  beqz a0, .L267
  li a0, 1
  sw a0, -296(fp)
  j .L266
.L267:
  li a0, 0
  sw a0, -296(fp)
.L266:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L271
  sw a0, -304(fp)
  lw a0, -180(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L271:
  beqz a0, .L270
  li a0, 1
  sw a0, -300(fp)
  j .L269
.L270:
  li a0, 0
  sw a0, -300(fp)
.L269:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L273
  li a0, 1
  sw a0, -304(fp)
  j .L272
.L273:
  li a0, 0
  sw a0, -304(fp)
.L272:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L276
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L276:
  beqz a0, .L275
  li a0, 1
  sw a0, -244(fp)
  j .L274
.L275:
  li a0, 0
  sw a0, -244(fp)
.L274:
  lw a0, -56(fp)
  snez a0, a0
  beqz a0, .L279
  sw a0, -300(fp)
  lw a0, -120(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L279:
  beqz a0, .L278
  li a0, 1
  sw a0, -296(fp)
  j .L277
.L278:
  li a0, 0
  sw a0, -296(fp)
.L277:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L282
  sw a0, -304(fp)
  lw a0, -180(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L282:
  beqz a0, .L281
  li a0, 1
  sw a0, -300(fp)
  j .L280
.L281:
  li a0, 0
  sw a0, -300(fp)
.L280:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L285
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L285:
  beqz a0, .L284
  li a0, 1
  sw a0, -184(fp)
  j .L283
.L284:
  li a0, 0
  sw a0, -184(fp)
.L283:
  lw a0, -60(fp)
  snez a0, a0
  bnez a0, .L288
  sw a0, -300(fp)
  lw a0, -124(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L288:
  beqz a0, .L287
  li a0, 1
  sw a0, -296(fp)
  j .L286
.L287:
  li a0, 0
  sw a0, -296(fp)
.L286:
  lw a0, -60(fp)
  snez a0, a0
  beqz a0, .L291
  sw a0, -304(fp)
  lw a0, -124(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L291:
  beqz a0, .L290
  li a0, 1
  sw a0, -300(fp)
  j .L289
.L290:
  li a0, 0
  sw a0, -300(fp)
.L289:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L293
  li a0, 1
  sw a0, -304(fp)
  j .L292
.L293:
  li a0, 0
  sw a0, -304(fp)
.L292:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L296
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L296:
  beqz a0, .L295
  li a0, 1
  sw a0, -292(fp)
  j .L294
.L295:
  li a0, 0
  sw a0, -292(fp)
.L294:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L299
  sw a0, -300(fp)
  lw a0, -184(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L299:
  beqz a0, .L298
  li a0, 1
  sw a0, -296(fp)
  j .L297
.L298:
  li a0, 0
  sw a0, -296(fp)
.L297:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L302
  sw a0, -304(fp)
  lw a0, -184(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L302:
  beqz a0, .L301
  li a0, 1
  sw a0, -300(fp)
  j .L300
.L301:
  li a0, 0
  sw a0, -300(fp)
.L300:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L304
  li a0, 1
  sw a0, -304(fp)
  j .L303
.L304:
  li a0, 0
  sw a0, -304(fp)
.L303:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L307
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L307:
  beqz a0, .L306
  li a0, 1
  sw a0, -248(fp)
  j .L305
.L306:
  li a0, 0
  sw a0, -248(fp)
.L305:
  lw a0, -60(fp)
  snez a0, a0
  beqz a0, .L310
  sw a0, -300(fp)
  lw a0, -124(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L310:
  beqz a0, .L309
  li a0, 1
  sw a0, -296(fp)
  j .L308
.L309:
  li a0, 0
  sw a0, -296(fp)
.L308:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L313
  sw a0, -304(fp)
  lw a0, -184(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L313:
  beqz a0, .L312
  li a0, 1
  sw a0, -300(fp)
  j .L311
.L312:
  li a0, 0
  sw a0, -300(fp)
.L311:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L316
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L316:
  beqz a0, .L315
  li a0, 1
  sw a0, -188(fp)
  j .L314
.L315:
  li a0, 0
  sw a0, -188(fp)
.L314:
  lw a0, -64(fp)
  snez a0, a0
  bnez a0, .L319
  sw a0, -300(fp)
  lw a0, -128(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L319:
  beqz a0, .L318
  li a0, 1
  sw a0, -296(fp)
  j .L317
.L318:
  li a0, 0
  sw a0, -296(fp)
.L317:
  lw a0, -64(fp)
  snez a0, a0
  beqz a0, .L322
  sw a0, -304(fp)
  lw a0, -128(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L322:
  beqz a0, .L321
  li a0, 1
  sw a0, -300(fp)
  j .L320
.L321:
  li a0, 0
  sw a0, -300(fp)
.L320:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L324
  li a0, 1
  sw a0, -304(fp)
  j .L323
.L324:
  li a0, 0
  sw a0, -304(fp)
.L323:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L327
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L327:
  beqz a0, .L326
  li a0, 1
  sw a0, -292(fp)
  j .L325
.L326:
  li a0, 0
  sw a0, -292(fp)
.L325:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L330
  sw a0, -300(fp)
  lw a0, -188(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L330:
  beqz a0, .L329
  li a0, 1
  sw a0, -296(fp)
  j .L328
.L329:
  li a0, 0
  sw a0, -296(fp)
.L328:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L333
  sw a0, -304(fp)
  lw a0, -188(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L333:
  beqz a0, .L332
  li a0, 1
  sw a0, -300(fp)
  j .L331
.L332:
  li a0, 0
  sw a0, -300(fp)
.L331:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L335
  li a0, 1
  sw a0, -304(fp)
  j .L334
.L335:
  li a0, 0
  sw a0, -304(fp)
.L334:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L338
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L338:
  beqz a0, .L337
  li a0, 1
  sw a0, -252(fp)
  j .L336
.L337:
  li a0, 0
  sw a0, -252(fp)
.L336:
  lw a0, -64(fp)
  snez a0, a0
  beqz a0, .L341
  sw a0, -300(fp)
  lw a0, -128(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L341:
  beqz a0, .L340
  li a0, 1
  sw a0, -296(fp)
  j .L339
.L340:
  li a0, 0
  sw a0, -296(fp)
.L339:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L344
  sw a0, -304(fp)
  lw a0, -188(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L344:
  beqz a0, .L343
  li a0, 1
  sw a0, -300(fp)
  j .L342
.L343:
  li a0, 0
  sw a0, -300(fp)
.L342:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L347
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L347:
  beqz a0, .L346
  li a0, 1
  sw a0, -192(fp)
  j .L345
.L346:
  li a0, 0
  sw a0, -192(fp)
.L345:
  lw a0, -68(fp)
  snez a0, a0
  bnez a0, .L350
  sw a0, -300(fp)
  lw a0, -132(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L350:
  beqz a0, .L349
  li a0, 1
  sw a0, -296(fp)
  j .L348
.L349:
  li a0, 0
  sw a0, -296(fp)
.L348:
  lw a0, -68(fp)
  snez a0, a0
  beqz a0, .L353
  sw a0, -304(fp)
  lw a0, -132(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L353:
  beqz a0, .L352
  li a0, 1
  sw a0, -300(fp)
  j .L351
.L352:
  li a0, 0
  sw a0, -300(fp)
.L351:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L355
  li a0, 1
  sw a0, -304(fp)
  j .L354
.L355:
  li a0, 0
  sw a0, -304(fp)
.L354:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L358
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L358:
  beqz a0, .L357
  li a0, 1
  sw a0, -292(fp)
  j .L356
.L357:
  li a0, 0
  sw a0, -292(fp)
.L356:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L361
  sw a0, -300(fp)
  lw a0, -192(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L361:
  beqz a0, .L360
  li a0, 1
  sw a0, -296(fp)
  j .L359
.L360:
  li a0, 0
  sw a0, -296(fp)
.L359:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L364
  sw a0, -304(fp)
  lw a0, -192(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L364:
  beqz a0, .L363
  li a0, 1
  sw a0, -300(fp)
  j .L362
.L363:
  li a0, 0
  sw a0, -300(fp)
.L362:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L366
  li a0, 1
  sw a0, -304(fp)
  j .L365
.L366:
  li a0, 0
  sw a0, -304(fp)
.L365:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L369
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L369:
  beqz a0, .L368
  li a0, 1
  sw a0, -256(fp)
  j .L367
.L368:
  li a0, 0
  sw a0, -256(fp)
.L367:
  lw a0, -68(fp)
  snez a0, a0
  beqz a0, .L372
  sw a0, -300(fp)
  lw a0, -132(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L372:
  beqz a0, .L371
  li a0, 1
  sw a0, -296(fp)
  j .L370
.L371:
  li a0, 0
  sw a0, -296(fp)
.L370:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L375
  sw a0, -304(fp)
  lw a0, -192(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L375:
  beqz a0, .L374
  li a0, 1
  sw a0, -300(fp)
  j .L373
.L374:
  li a0, 0
  sw a0, -300(fp)
.L373:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L378
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L378:
  beqz a0, .L377
  li a0, 1
  sw a0, -196(fp)
  j .L376
.L377:
  li a0, 0
  sw a0, -196(fp)
.L376:
  lw a0, -72(fp)
  snez a0, a0
  bnez a0, .L381
  sw a0, -300(fp)
  lw a0, -136(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L381:
  beqz a0, .L380
  li a0, 1
  sw a0, -296(fp)
  j .L379
.L380:
  li a0, 0
  sw a0, -296(fp)
.L379:
  lw a0, -72(fp)
  snez a0, a0
  beqz a0, .L384
  sw a0, -304(fp)
  lw a0, -136(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L384:
  beqz a0, .L383
  li a0, 1
  sw a0, -300(fp)
  j .L382
.L383:
  li a0, 0
  sw a0, -300(fp)
.L382:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L386
  li a0, 1
  sw a0, -304(fp)
  j .L385
.L386:
  li a0, 0
  sw a0, -304(fp)
.L385:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L389
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L389:
  beqz a0, .L388
  li a0, 1
  sw a0, -292(fp)
  j .L387
.L388:
  li a0, 0
  sw a0, -292(fp)
.L387:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L392
  sw a0, -300(fp)
  lw a0, -196(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L392:
  beqz a0, .L391
  li a0, 1
  sw a0, -296(fp)
  j .L390
.L391:
  li a0, 0
  sw a0, -296(fp)
.L390:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L395
  sw a0, -304(fp)
  lw a0, -196(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L395:
  beqz a0, .L394
  li a0, 1
  sw a0, -300(fp)
  j .L393
.L394:
  li a0, 0
  sw a0, -300(fp)
.L393:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L397
  li a0, 1
  sw a0, -304(fp)
  j .L396
.L397:
  li a0, 0
  sw a0, -304(fp)
.L396:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L400
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L400:
  beqz a0, .L399
  li a0, 1
  sw a0, -260(fp)
  j .L398
.L399:
  li a0, 0
  sw a0, -260(fp)
.L398:
  lw a0, -72(fp)
  snez a0, a0
  beqz a0, .L403
  sw a0, -300(fp)
  lw a0, -136(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L403:
  beqz a0, .L402
  li a0, 1
  sw a0, -296(fp)
  j .L401
.L402:
  li a0, 0
  sw a0, -296(fp)
.L401:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L406
  sw a0, -304(fp)
  lw a0, -196(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L406:
  beqz a0, .L405
  li a0, 1
  sw a0, -300(fp)
  j .L404
.L405:
  li a0, 0
  sw a0, -300(fp)
.L404:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L409
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L409:
  beqz a0, .L408
  li a0, 1
  sw a0, -200(fp)
  j .L407
.L408:
  li a0, 0
  sw a0, -200(fp)
.L407:
  lw a0, -76(fp)
  snez a0, a0
  bnez a0, .L412
  sw a0, -300(fp)
  lw a0, -140(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L412:
  beqz a0, .L411
  li a0, 1
  sw a0, -296(fp)
  j .L410
.L411:
  li a0, 0
  sw a0, -296(fp)
.L410:
  lw a0, -76(fp)
  snez a0, a0
  beqz a0, .L415
  sw a0, -304(fp)
  lw a0, -140(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L415:
  beqz a0, .L414
  li a0, 1
  sw a0, -300(fp)
  j .L413
.L414:
  li a0, 0
  sw a0, -300(fp)
.L413:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L417
  li a0, 1
  sw a0, -304(fp)
  j .L416
.L417:
  li a0, 0
  sw a0, -304(fp)
.L416:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L420
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L420:
  beqz a0, .L419
  li a0, 1
  sw a0, -292(fp)
  j .L418
.L419:
  li a0, 0
  sw a0, -292(fp)
.L418:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L423
  sw a0, -300(fp)
  lw a0, -200(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L423:
  beqz a0, .L422
  li a0, 1
  sw a0, -296(fp)
  j .L421
.L422:
  li a0, 0
  sw a0, -296(fp)
.L421:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L426
  sw a0, -304(fp)
  lw a0, -200(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L426:
  beqz a0, .L425
  li a0, 1
  sw a0, -300(fp)
  j .L424
.L425:
  li a0, 0
  sw a0, -300(fp)
.L424:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L428
  li a0, 1
  sw a0, -304(fp)
  j .L427
.L428:
  li a0, 0
  sw a0, -304(fp)
.L427:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L431
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L431:
  beqz a0, .L430
  li a0, 1
  sw a0, -264(fp)
  j .L429
.L430:
  li a0, 0
  sw a0, -264(fp)
.L429:
  lw a0, -76(fp)
  snez a0, a0
  beqz a0, .L434
  sw a0, -300(fp)
  lw a0, -140(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L434:
  beqz a0, .L433
  li a0, 1
  sw a0, -296(fp)
  j .L432
.L433:
  li a0, 0
  sw a0, -296(fp)
.L432:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L437
  sw a0, -304(fp)
  lw a0, -200(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L437:
  beqz a0, .L436
  li a0, 1
  sw a0, -300(fp)
  j .L435
.L436:
  li a0, 0
  sw a0, -300(fp)
.L435:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L440
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L440:
  beqz a0, .L439
  li a0, 1
  sw a0, -204(fp)
  j .L438
.L439:
  li a0, 0
  sw a0, -204(fp)
.L438:
  lw a0, -80(fp)
  snez a0, a0
  bnez a0, .L443
  sw a0, -300(fp)
  lw a0, -144(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L443:
  beqz a0, .L442
  li a0, 1
  sw a0, -296(fp)
  j .L441
.L442:
  li a0, 0
  sw a0, -296(fp)
.L441:
  lw a0, -80(fp)
  snez a0, a0
  beqz a0, .L446
  sw a0, -304(fp)
  lw a0, -144(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L446:
  beqz a0, .L445
  li a0, 1
  sw a0, -300(fp)
  j .L444
.L445:
  li a0, 0
  sw a0, -300(fp)
.L444:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L448
  li a0, 1
  sw a0, -304(fp)
  j .L447
.L448:
  li a0, 0
  sw a0, -304(fp)
.L447:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L451
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L451:
  beqz a0, .L450
  li a0, 1
  sw a0, -292(fp)
  j .L449
.L450:
  li a0, 0
  sw a0, -292(fp)
.L449:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L454
  sw a0, -300(fp)
  lw a0, -204(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L454:
  beqz a0, .L453
  li a0, 1
  sw a0, -296(fp)
  j .L452
.L453:
  li a0, 0
  sw a0, -296(fp)
.L452:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L457
  sw a0, -304(fp)
  lw a0, -204(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L457:
  beqz a0, .L456
  li a0, 1
  sw a0, -300(fp)
  j .L455
.L456:
  li a0, 0
  sw a0, -300(fp)
.L455:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L459
  li a0, 1
  sw a0, -304(fp)
  j .L458
.L459:
  li a0, 0
  sw a0, -304(fp)
.L458:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L462
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L462:
  beqz a0, .L461
  li a0, 1
  sw a0, -268(fp)
  j .L460
.L461:
  li a0, 0
  sw a0, -268(fp)
.L460:
  lw a0, -80(fp)
  snez a0, a0
  beqz a0, .L465
  sw a0, -300(fp)
  lw a0, -144(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L465:
  beqz a0, .L464
  li a0, 1
  sw a0, -296(fp)
  j .L463
.L464:
  li a0, 0
  sw a0, -296(fp)
.L463:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L468
  sw a0, -304(fp)
  lw a0, -204(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L468:
  beqz a0, .L467
  li a0, 1
  sw a0, -300(fp)
  j .L466
.L467:
  li a0, 0
  sw a0, -300(fp)
.L466:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L471
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L471:
  beqz a0, .L470
  li a0, 1
  sw a0, -208(fp)
  j .L469
.L470:
  li a0, 0
  sw a0, -208(fp)
.L469:
  lw a0, -84(fp)
  snez a0, a0
  bnez a0, .L474
  sw a0, -300(fp)
  lw a0, -148(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L474:
  beqz a0, .L473
  li a0, 1
  sw a0, -296(fp)
  j .L472
.L473:
  li a0, 0
  sw a0, -296(fp)
.L472:
  lw a0, -84(fp)
  snez a0, a0
  beqz a0, .L477
  sw a0, -304(fp)
  lw a0, -148(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L477:
  beqz a0, .L476
  li a0, 1
  sw a0, -300(fp)
  j .L475
.L476:
  li a0, 0
  sw a0, -300(fp)
.L475:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L479
  li a0, 1
  sw a0, -304(fp)
  j .L478
.L479:
  li a0, 0
  sw a0, -304(fp)
.L478:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L482
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L482:
  beqz a0, .L481
  li a0, 1
  sw a0, -292(fp)
  j .L480
.L481:
  li a0, 0
  sw a0, -292(fp)
.L480:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L485
  sw a0, -300(fp)
  lw a0, -208(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L485:
  beqz a0, .L484
  li a0, 1
  sw a0, -296(fp)
  j .L483
.L484:
  li a0, 0
  sw a0, -296(fp)
.L483:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L488
  sw a0, -304(fp)
  lw a0, -208(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L488:
  beqz a0, .L487
  li a0, 1
  sw a0, -300(fp)
  j .L486
.L487:
  li a0, 0
  sw a0, -300(fp)
.L486:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L490
  li a0, 1
  sw a0, -304(fp)
  j .L489
.L490:
  li a0, 0
  sw a0, -304(fp)
.L489:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L493
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L493:
  beqz a0, .L492
  li a0, 1
  sw a0, -272(fp)
  j .L491
.L492:
  li a0, 0
  sw a0, -272(fp)
.L491:
  lw a0, -84(fp)
  snez a0, a0
  beqz a0, .L496
  sw a0, -300(fp)
  lw a0, -148(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L496:
  beqz a0, .L495
  li a0, 1
  sw a0, -296(fp)
  j .L494
.L495:
  li a0, 0
  sw a0, -296(fp)
.L494:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L499
  sw a0, -304(fp)
  lw a0, -208(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L499:
  beqz a0, .L498
  li a0, 1
  sw a0, -300(fp)
  j .L497
.L498:
  li a0, 0
  sw a0, -300(fp)
.L497:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L502
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L502:
  beqz a0, .L501
  li a0, 1
  sw a0, -212(fp)
  j .L500
.L501:
  li a0, 0
  sw a0, -212(fp)
.L500:
  lw a0, -88(fp)
  snez a0, a0
  bnez a0, .L505
  sw a0, -300(fp)
  lw a0, -152(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L505:
  beqz a0, .L504
  li a0, 1
  sw a0, -296(fp)
  j .L503
.L504:
  li a0, 0
  sw a0, -296(fp)
.L503:
  lw a0, -88(fp)
  snez a0, a0
  beqz a0, .L508
  sw a0, -304(fp)
  lw a0, -152(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L508:
  beqz a0, .L507
  li a0, 1
  sw a0, -300(fp)
  j .L506
.L507:
  li a0, 0
  sw a0, -300(fp)
.L506:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L510
  li a0, 1
  sw a0, -304(fp)
  j .L509
.L510:
  li a0, 0
  sw a0, -304(fp)
.L509:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L513
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L513:
  beqz a0, .L512
  li a0, 1
  sw a0, -292(fp)
  j .L511
.L512:
  li a0, 0
  sw a0, -292(fp)
.L511:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L516
  sw a0, -300(fp)
  lw a0, -212(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L516:
  beqz a0, .L515
  li a0, 1
  sw a0, -296(fp)
  j .L514
.L515:
  li a0, 0
  sw a0, -296(fp)
.L514:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L519
  sw a0, -304(fp)
  lw a0, -212(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L519:
  beqz a0, .L518
  li a0, 1
  sw a0, -300(fp)
  j .L517
.L518:
  li a0, 0
  sw a0, -300(fp)
.L517:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L521
  li a0, 1
  sw a0, -304(fp)
  j .L520
.L521:
  li a0, 0
  sw a0, -304(fp)
.L520:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L524
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L524:
  beqz a0, .L523
  li a0, 1
  sw a0, -276(fp)
  j .L522
.L523:
  li a0, 0
  sw a0, -276(fp)
.L522:
  lw a0, -88(fp)
  snez a0, a0
  beqz a0, .L527
  sw a0, -300(fp)
  lw a0, -152(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L527:
  beqz a0, .L526
  li a0, 1
  sw a0, -296(fp)
  j .L525
.L526:
  li a0, 0
  sw a0, -296(fp)
.L525:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L530
  sw a0, -304(fp)
  lw a0, -212(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L530:
  beqz a0, .L529
  li a0, 1
  sw a0, -300(fp)
  j .L528
.L529:
  li a0, 0
  sw a0, -300(fp)
.L528:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L533
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L533:
  beqz a0, .L532
  li a0, 1
  sw a0, -216(fp)
  j .L531
.L532:
  li a0, 0
  sw a0, -216(fp)
.L531:
  lw a0, -92(fp)
  snez a0, a0
  bnez a0, .L536
  sw a0, -300(fp)
  lw a0, -156(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L536:
  beqz a0, .L535
  li a0, 1
  sw a0, -296(fp)
  j .L534
.L535:
  li a0, 0
  sw a0, -296(fp)
.L534:
  lw a0, -92(fp)
  snez a0, a0
  beqz a0, .L539
  sw a0, -304(fp)
  lw a0, -156(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L539:
  beqz a0, .L538
  li a0, 1
  sw a0, -300(fp)
  j .L537
.L538:
  li a0, 0
  sw a0, -300(fp)
.L537:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L541
  li a0, 1
  sw a0, -304(fp)
  j .L540
.L541:
  li a0, 0
  sw a0, -304(fp)
.L540:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L544
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L544:
  beqz a0, .L543
  li a0, 1
  sw a0, -292(fp)
  j .L542
.L543:
  li a0, 0
  sw a0, -292(fp)
.L542:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L547
  sw a0, -300(fp)
  lw a0, -216(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L547:
  beqz a0, .L546
  li a0, 1
  sw a0, -296(fp)
  j .L545
.L546:
  li a0, 0
  sw a0, -296(fp)
.L545:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L550
  sw a0, -304(fp)
  lw a0, -216(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L550:
  beqz a0, .L549
  li a0, 1
  sw a0, -300(fp)
  j .L548
.L549:
  li a0, 0
  sw a0, -300(fp)
.L548:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L552
  li a0, 1
  sw a0, -304(fp)
  j .L551
.L552:
  li a0, 0
  sw a0, -304(fp)
.L551:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L555
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L555:
  beqz a0, .L554
  li a0, 1
  sw a0, -280(fp)
  j .L553
.L554:
  li a0, 0
  sw a0, -280(fp)
.L553:
  lw a0, -92(fp)
  snez a0, a0
  beqz a0, .L558
  sw a0, -300(fp)
  lw a0, -156(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L558:
  beqz a0, .L557
  li a0, 1
  sw a0, -296(fp)
  j .L556
.L557:
  li a0, 0
  sw a0, -296(fp)
.L556:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L561
  sw a0, -304(fp)
  lw a0, -216(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L561:
  beqz a0, .L560
  li a0, 1
  sw a0, -300(fp)
  j .L559
.L560:
  li a0, 0
  sw a0, -300(fp)
.L559:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L564
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L564:
  beqz a0, .L563
  li a0, 1
  sw a0, -220(fp)
  j .L562
.L563:
  li a0, 0
  sw a0, -220(fp)
.L562:
  lw a0, -96(fp)
  snez a0, a0
  bnez a0, .L567
  sw a0, -300(fp)
  lw a0, -160(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L567:
  beqz a0, .L566
  li a0, 1
  sw a0, -296(fp)
  j .L565
.L566:
  li a0, 0
  sw a0, -296(fp)
.L565:
  lw a0, -96(fp)
  snez a0, a0
  beqz a0, .L570
  sw a0, -304(fp)
  lw a0, -160(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L570:
  beqz a0, .L569
  li a0, 1
  sw a0, -300(fp)
  j .L568
.L569:
  li a0, 0
  sw a0, -300(fp)
.L568:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L572
  li a0, 1
  sw a0, -304(fp)
  j .L571
.L572:
  li a0, 0
  sw a0, -304(fp)
.L571:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L575
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L575:
  beqz a0, .L574
  li a0, 1
  sw a0, -292(fp)
  j .L573
.L574:
  li a0, 0
  sw a0, -292(fp)
.L573:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L578
  sw a0, -300(fp)
  lw a0, -220(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L578:
  beqz a0, .L577
  li a0, 1
  sw a0, -296(fp)
  j .L576
.L577:
  li a0, 0
  sw a0, -296(fp)
.L576:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L581
  sw a0, -304(fp)
  lw a0, -220(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L581:
  beqz a0, .L580
  li a0, 1
  sw a0, -300(fp)
  j .L579
.L580:
  li a0, 0
  sw a0, -300(fp)
.L579:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L583
  li a0, 1
  sw a0, -304(fp)
  j .L582
.L583:
  li a0, 0
  sw a0, -304(fp)
.L582:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L586
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L586:
  beqz a0, .L585
  li a0, 1
  sw a0, -284(fp)
  j .L584
.L585:
  li a0, 0
  sw a0, -284(fp)
.L584:
  lw a0, -96(fp)
  snez a0, a0
  beqz a0, .L589
  sw a0, -300(fp)
  lw a0, -160(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L589:
  beqz a0, .L588
  li a0, 1
  sw a0, -296(fp)
  j .L587
.L588:
  li a0, 0
  sw a0, -296(fp)
.L587:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L592
  sw a0, -304(fp)
  lw a0, -220(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L592:
  beqz a0, .L591
  li a0, 1
  sw a0, -300(fp)
  j .L590
.L591:
  li a0, 0
  sw a0, -300(fp)
.L590:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L595
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L595:
  beqz a0, .L594
  li a0, 1
  sw a0, -224(fp)
  j .L593
.L594:
  li a0, 0
  sw a0, -224(fp)
.L593:
  lw a0, -100(fp)
  snez a0, a0
  bnez a0, .L598
  sw a0, -300(fp)
  lw a0, -164(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L598:
  beqz a0, .L597
  li a0, 1
  sw a0, -296(fp)
  j .L596
.L597:
  li a0, 0
  sw a0, -296(fp)
.L596:
  lw a0, -100(fp)
  snez a0, a0
  beqz a0, .L601
  sw a0, -304(fp)
  lw a0, -164(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L601:
  beqz a0, .L600
  li a0, 1
  sw a0, -300(fp)
  j .L599
.L600:
  li a0, 0
  sw a0, -300(fp)
.L599:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L603
  li a0, 1
  sw a0, -304(fp)
  j .L602
.L603:
  li a0, 0
  sw a0, -304(fp)
.L602:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L606
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L606:
  beqz a0, .L605
  li a0, 1
  sw a0, -292(fp)
  j .L604
.L605:
  li a0, 0
  sw a0, -292(fp)
.L604:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L609
  sw a0, -300(fp)
  lw a0, -224(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L609:
  beqz a0, .L608
  li a0, 1
  sw a0, -296(fp)
  j .L607
.L608:
  li a0, 0
  sw a0, -296(fp)
.L607:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L612
  sw a0, -304(fp)
  lw a0, -224(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L612:
  beqz a0, .L611
  li a0, 1
  sw a0, -300(fp)
  j .L610
.L611:
  li a0, 0
  sw a0, -300(fp)
.L610:
  lw a0, -300(fp)
  seqz a0, a0
  beqz a0, .L614
  li a0, 1
  sw a0, -304(fp)
  j .L613
.L614:
  li a0, 0
  sw a0, -304(fp)
.L613:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L617
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L617:
  beqz a0, .L616
  li a0, 1
  sw a0, -288(fp)
  j .L615
.L616:
  li a0, 0
  sw a0, -288(fp)
.L615:
  lw a0, -100(fp)
  snez a0, a0
  beqz a0, .L620
  sw a0, -300(fp)
  lw a0, -164(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L620:
  beqz a0, .L619
  li a0, 1
  sw a0, -296(fp)
  j .L618
.L619:
  li a0, 0
  sw a0, -296(fp)
.L618:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L623
  sw a0, -304(fp)
  lw a0, -224(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L623:
  beqz a0, .L622
  li a0, 1
  sw a0, -300(fp)
  j .L621
.L622:
  li a0, 0
  sw a0, -300(fp)
.L621:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L626
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L626:
  beqz a0, .L625
  li a0, 1
  sw a0, -36(fp)
  j .L624
.L625:
  li a0, 0
  sw a0, -36(fp)
.L624:
  li a0, 0
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, -292(fp)
  li a0, 2
  lw t0, -292(fp)
  mulw a0, t0, a0
  sw a0, -292(fp)
  lw a0, -288(fp)
  lw t0, -292(fp)
  addw a0, t0, a0
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, -292(fp)
  li a0, 2
  lw t0, -292(fp)
  mulw a0, t0, a0
  sw a0, -292(fp)
  lw a0, -284(fp)
  lw t0, -292(fp)
  addw a0, t0, a0
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, -292(fp)
  li a0, 2
  lw t0, -292(fp)
  mulw a0, t0, a0
  sw a0, -292(fp)
  lw a0, -280(fp)
  lw t0, -292(fp)
  addw a0, t0, a0
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, -292(fp)
  li a0, 2
  lw t0, -292(fp)
  mulw a0, t0, a0
  sw a0, -292(fp)
  lw a0, -276(fp)
  lw t0, -292(fp)
  addw a0, t0, a0
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, -292(fp)
  li a0, 2
  lw t0, -292(fp)
  mulw a0, t0, a0
  sw a0, -292(fp)
  lw a0, -272(fp)
  lw t0, -292(fp)
  addw a0, t0, a0
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, -292(fp)
  li a0, 2
  lw t0, -292(fp)
  mulw a0, t0, a0
  sw a0, -292(fp)
  lw a0, -268(fp)
  lw t0, -292(fp)
  addw a0, t0, a0
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, -292(fp)
  li a0, 2
  lw t0, -292(fp)
  mulw a0, t0, a0
  sw a0, -292(fp)
  lw a0, -264(fp)
  lw t0, -292(fp)
  addw a0, t0, a0
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, -292(fp)
  li a0, 2
  lw t0, -292(fp)
  mulw a0, t0, a0
  sw a0, -292(fp)
  lw a0, -260(fp)
  lw t0, -292(fp)
  addw a0, t0, a0
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, -292(fp)
  li a0, 2
  lw t0, -292(fp)
  mulw a0, t0, a0
  sw a0, -292(fp)
  lw a0, -256(fp)
  lw t0, -292(fp)
  addw a0, t0, a0
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, -292(fp)
  li a0, 2
  lw t0, -292(fp)
  mulw a0, t0, a0
  sw a0, -292(fp)
  lw a0, -252(fp)
  lw t0, -292(fp)
  addw a0, t0, a0
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, -292(fp)
  li a0, 2
  lw t0, -292(fp)
  mulw a0, t0, a0
  sw a0, -292(fp)
  lw a0, -248(fp)
  lw t0, -292(fp)
  addw a0, t0, a0
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, -292(fp)
  li a0, 2
  lw t0, -292(fp)
  mulw a0, t0, a0
  sw a0, -292(fp)
  lw a0, -244(fp)
  lw t0, -292(fp)
  addw a0, t0, a0
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, -292(fp)
  li a0, 2
  lw t0, -292(fp)
  mulw a0, t0, a0
  sw a0, -292(fp)
  lw a0, -240(fp)
  lw t0, -292(fp)
  addw a0, t0, a0
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, -292(fp)
  li a0, 2
  lw t0, -292(fp)
  mulw a0, t0, a0
  sw a0, -292(fp)
  lw a0, -236(fp)
  lw t0, -292(fp)
  addw a0, t0, a0
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, -292(fp)
  li a0, 2
  lw t0, -292(fp)
  mulw a0, t0, a0
  sw a0, -292(fp)
  lw a0, -232(fp)
  lw t0, -292(fp)
  addw a0, t0, a0
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, -292(fp)
  li a0, 2
  lw t0, -292(fp)
  mulw a0, t0, a0
  sw a0, -292(fp)
  lw a0, -228(fp)
  lw t0, -292(fp)
  addw a0, t0, a0
  sw a0, -28(fp)
  lw a0, 4(fp)
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L628
  lw a0, -36(fp)
  negw a0, a0
  sw a0, -36(fp)
  j .L627
.L628:
.L627:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L630
  lw a0, -40(fp)
  negw a0, a0
  sw a0, -40(fp)
  j .L629
.L630:
.L629:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -44(fp)
  lw a0, -44(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L632
  lw a0, -44(fp)
  negw a0, a0
  sw a0, -44(fp)
  j .L631
.L632:
.L631:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -48(fp)
  lw a0, -48(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L634
  lw a0, -48(fp)
  negw a0, a0
  sw a0, -48(fp)
  j .L633
.L634:
.L633:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -52(fp)
  lw a0, -52(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L636
  lw a0, -52(fp)
  negw a0, a0
  sw a0, -52(fp)
  j .L635
.L636:
.L635:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -56(fp)
  lw a0, -56(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L638
  lw a0, -56(fp)
  negw a0, a0
  sw a0, -56(fp)
  j .L637
.L638:
.L637:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -60(fp)
  lw a0, -60(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L640
  lw a0, -60(fp)
  negw a0, a0
  sw a0, -60(fp)
  j .L639
.L640:
.L639:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -64(fp)
  lw a0, -64(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L642
  lw a0, -64(fp)
  negw a0, a0
  sw a0, -64(fp)
  j .L641
.L642:
.L641:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -68(fp)
  lw a0, -68(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L644
  lw a0, -68(fp)
  negw a0, a0
  sw a0, -68(fp)
  j .L643
.L644:
.L643:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -72(fp)
  lw a0, -72(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L646
  lw a0, -72(fp)
  negw a0, a0
  sw a0, -72(fp)
  j .L645
.L646:
.L645:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -76(fp)
  lw a0, -76(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L648
  lw a0, -76(fp)
  negw a0, a0
  sw a0, -76(fp)
  j .L647
.L648:
.L647:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -80(fp)
  lw a0, -80(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L650
  lw a0, -80(fp)
  negw a0, a0
  sw a0, -80(fp)
  j .L649
.L650:
.L649:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -84(fp)
  lw a0, -84(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L652
  lw a0, -84(fp)
  negw a0, a0
  sw a0, -84(fp)
  j .L651
.L652:
.L651:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -88(fp)
  lw a0, -88(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L654
  lw a0, -88(fp)
  negw a0, a0
  sw a0, -88(fp)
  j .L653
.L654:
.L653:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -92(fp)
  lw a0, -92(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L656
  lw a0, -92(fp)
  negw a0, a0
  sw a0, -92(fp)
  j .L655
.L656:
.L655:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  remw a0, t0, a0
  sw a0, -96(fp)
  lw a0, -96(fp)
  sw a0, -104(fp)
  li a0, 0
  lw t0, -104(fp)
  slt a0, t0, a0
  beqz a0, .L658
  lw a0, -96(fp)
  negw a0, a0
  sw a0, -96(fp)
  j .L657
.L658:
.L657:
  lw a0, -100(fp)
  sw a0, -104(fp)
  li a0, 2
  lw t0, -104(fp)
  divw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -28(fp)
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  remw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -168(fp)
  li a0, 0
  lw t0, -168(fp)
  slt a0, t0, a0
  beqz a0, .L660
  lw a0, -100(fp)
  negw a0, a0
  sw a0, -100(fp)
  j .L659
.L660:
.L659:
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  divw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  remw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -168(fp)
  li a0, 0
  lw t0, -168(fp)
  slt a0, t0, a0
  beqz a0, .L662
  lw a0, -104(fp)
  negw a0, a0
  sw a0, -104(fp)
  j .L661
.L662:
.L661:
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  divw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  remw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -168(fp)
  li a0, 0
  lw t0, -168(fp)
  slt a0, t0, a0
  beqz a0, .L664
  lw a0, -108(fp)
  negw a0, a0
  sw a0, -108(fp)
  j .L663
.L664:
.L663:
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  divw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  remw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -168(fp)
  li a0, 0
  lw t0, -168(fp)
  slt a0, t0, a0
  beqz a0, .L666
  lw a0, -112(fp)
  negw a0, a0
  sw a0, -112(fp)
  j .L665
.L666:
.L665:
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  divw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  remw a0, t0, a0
  sw a0, -116(fp)
  lw a0, -116(fp)
  sw a0, -168(fp)
  li a0, 0
  lw t0, -168(fp)
  slt a0, t0, a0
  beqz a0, .L668
  lw a0, -116(fp)
  negw a0, a0
  sw a0, -116(fp)
  j .L667
.L668:
.L667:
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  divw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  remw a0, t0, a0
  sw a0, -120(fp)
  lw a0, -120(fp)
  sw a0, -168(fp)
  li a0, 0
  lw t0, -168(fp)
  slt a0, t0, a0
  beqz a0, .L670
  lw a0, -120(fp)
  negw a0, a0
  sw a0, -120(fp)
  j .L669
.L670:
.L669:
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  divw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  remw a0, t0, a0
  sw a0, -124(fp)
  lw a0, -124(fp)
  sw a0, -168(fp)
  li a0, 0
  lw t0, -168(fp)
  slt a0, t0, a0
  beqz a0, .L672
  lw a0, -124(fp)
  negw a0, a0
  sw a0, -124(fp)
  j .L671
.L672:
.L671:
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  divw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  remw a0, t0, a0
  sw a0, -128(fp)
  lw a0, -128(fp)
  sw a0, -168(fp)
  li a0, 0
  lw t0, -168(fp)
  slt a0, t0, a0
  beqz a0, .L674
  lw a0, -128(fp)
  negw a0, a0
  sw a0, -128(fp)
  j .L673
.L674:
.L673:
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  divw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  remw a0, t0, a0
  sw a0, -132(fp)
  lw a0, -132(fp)
  sw a0, -168(fp)
  li a0, 0
  lw t0, -168(fp)
  slt a0, t0, a0
  beqz a0, .L676
  lw a0, -132(fp)
  negw a0, a0
  sw a0, -132(fp)
  j .L675
.L676:
.L675:
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  divw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  remw a0, t0, a0
  sw a0, -136(fp)
  lw a0, -136(fp)
  sw a0, -168(fp)
  li a0, 0
  lw t0, -168(fp)
  slt a0, t0, a0
  beqz a0, .L678
  lw a0, -136(fp)
  negw a0, a0
  sw a0, -136(fp)
  j .L677
.L678:
.L677:
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  divw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  remw a0, t0, a0
  sw a0, -140(fp)
  lw a0, -140(fp)
  sw a0, -168(fp)
  li a0, 0
  lw t0, -168(fp)
  slt a0, t0, a0
  beqz a0, .L680
  lw a0, -140(fp)
  negw a0, a0
  sw a0, -140(fp)
  j .L679
.L680:
.L679:
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  divw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  remw a0, t0, a0
  sw a0, -144(fp)
  lw a0, -144(fp)
  sw a0, -168(fp)
  li a0, 0
  lw t0, -168(fp)
  slt a0, t0, a0
  beqz a0, .L682
  lw a0, -144(fp)
  negw a0, a0
  sw a0, -144(fp)
  j .L681
.L682:
.L681:
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  divw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  remw a0, t0, a0
  sw a0, -148(fp)
  lw a0, -148(fp)
  sw a0, -168(fp)
  li a0, 0
  lw t0, -168(fp)
  slt a0, t0, a0
  beqz a0, .L684
  lw a0, -148(fp)
  negw a0, a0
  sw a0, -148(fp)
  j .L683
.L684:
.L683:
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  divw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  remw a0, t0, a0
  sw a0, -152(fp)
  lw a0, -152(fp)
  sw a0, -168(fp)
  li a0, 0
  lw t0, -168(fp)
  slt a0, t0, a0
  beqz a0, .L686
  lw a0, -152(fp)
  negw a0, a0
  sw a0, -152(fp)
  j .L685
.L686:
.L685:
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  divw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  remw a0, t0, a0
  sw a0, -156(fp)
  lw a0, -156(fp)
  sw a0, -168(fp)
  li a0, 0
  lw t0, -168(fp)
  slt a0, t0, a0
  beqz a0, .L688
  lw a0, -156(fp)
  negw a0, a0
  sw a0, -156(fp)
  j .L687
.L688:
.L687:
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  divw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  remw a0, t0, a0
  sw a0, -160(fp)
  lw a0, -160(fp)
  sw a0, -168(fp)
  li a0, 0
  lw t0, -168(fp)
  slt a0, t0, a0
  beqz a0, .L690
  lw a0, -160(fp)
  negw a0, a0
  sw a0, -160(fp)
  j .L689
.L690:
.L689:
  lw a0, -164(fp)
  sw a0, -168(fp)
  li a0, 2
  lw t0, -168(fp)
  divw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -36(fp)
  snez a0, a0
  bnez a0, .L693
  sw a0, -296(fp)
  lw a0, -100(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L693:
  beqz a0, .L692
  li a0, 1
  sw a0, -292(fp)
  j .L691
.L692:
  li a0, 0
  sw a0, -292(fp)
.L691:
  lw a0, -36(fp)
  snez a0, a0
  beqz a0, .L696
  sw a0, -300(fp)
  lw a0, -100(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L696:
  beqz a0, .L695
  li a0, 1
  sw a0, -296(fp)
  j .L694
.L695:
  li a0, 0
  sw a0, -296(fp)
.L694:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L698
  li a0, 1
  sw a0, -300(fp)
  j .L697
.L698:
  li a0, 0
  sw a0, -300(fp)
.L697:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L701
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L701:
  beqz a0, .L700
  li a0, 1
  sw a0, -288(fp)
  j .L699
.L700:
  li a0, 0
  sw a0, -288(fp)
.L699:
  lw a0, -288(fp)
  snez a0, a0
  bnez a0, .L704
  sw a0, -296(fp)
  li a0, 0
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L704:
  beqz a0, .L703
  li a0, 1
  sw a0, -292(fp)
  j .L702
.L703:
  li a0, 0
  sw a0, -292(fp)
.L702:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L707
  sw a0, -300(fp)
  li a0, 0
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L707:
  beqz a0, .L706
  li a0, 1
  sw a0, -296(fp)
  j .L705
.L706:
  li a0, 0
  sw a0, -296(fp)
.L705:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L709
  li a0, 1
  sw a0, -300(fp)
  j .L708
.L709:
  li a0, 0
  sw a0, -300(fp)
.L708:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L712
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L712:
  beqz a0, .L711
  li a0, 1
  sw a0, -224(fp)
  j .L710
.L711:
  li a0, 0
  sw a0, -224(fp)
.L710:
  lw a0, -36(fp)
  snez a0, a0
  beqz a0, .L715
  sw a0, -296(fp)
  lw a0, -100(fp)
  snez a0, a0
  lw t0, -296(fp)
  and a0, t0, a0
.L715:
  beqz a0, .L714
  li a0, 1
  sw a0, -292(fp)
  j .L713
.L714:
  li a0, 0
  sw a0, -292(fp)
.L713:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L718
  sw a0, -300(fp)
  li a0, 0
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L718:
  beqz a0, .L717
  li a0, 1
  sw a0, -296(fp)
  j .L716
.L717:
  li a0, 0
  sw a0, -296(fp)
.L716:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L721
  sw a0, -300(fp)
  lw a0, -296(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L721:
  beqz a0, .L720
  li a0, 1
  sw a0, -164(fp)
  j .L719
.L720:
  li a0, 0
  sw a0, -164(fp)
.L719:
  lw a0, -40(fp)
  snez a0, a0
  bnez a0, .L724
  sw a0, -296(fp)
  lw a0, -104(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L724:
  beqz a0, .L723
  li a0, 1
  sw a0, -292(fp)
  j .L722
.L723:
  li a0, 0
  sw a0, -292(fp)
.L722:
  lw a0, -40(fp)
  snez a0, a0
  beqz a0, .L727
  sw a0, -300(fp)
  lw a0, -104(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L727:
  beqz a0, .L726
  li a0, 1
  sw a0, -296(fp)
  j .L725
.L726:
  li a0, 0
  sw a0, -296(fp)
.L725:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L729
  li a0, 1
  sw a0, -300(fp)
  j .L728
.L729:
  li a0, 0
  sw a0, -300(fp)
.L728:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L732
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L732:
  beqz a0, .L731
  li a0, 1
  sw a0, -288(fp)
  j .L730
.L731:
  li a0, 0
  sw a0, -288(fp)
.L730:
  lw a0, -288(fp)
  snez a0, a0
  bnez a0, .L735
  sw a0, -296(fp)
  lw a0, -164(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L735:
  beqz a0, .L734
  li a0, 1
  sw a0, -292(fp)
  j .L733
.L734:
  li a0, 0
  sw a0, -292(fp)
.L733:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L738
  sw a0, -300(fp)
  lw a0, -164(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L738:
  beqz a0, .L737
  li a0, 1
  sw a0, -296(fp)
  j .L736
.L737:
  li a0, 0
  sw a0, -296(fp)
.L736:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L740
  li a0, 1
  sw a0, -300(fp)
  j .L739
.L740:
  li a0, 0
  sw a0, -300(fp)
.L739:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L743
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L743:
  beqz a0, .L742
  li a0, 1
  sw a0, -228(fp)
  j .L741
.L742:
  li a0, 0
  sw a0, -228(fp)
.L741:
  lw a0, -40(fp)
  snez a0, a0
  beqz a0, .L746
  sw a0, -296(fp)
  lw a0, -104(fp)
  snez a0, a0
  lw t0, -296(fp)
  and a0, t0, a0
.L746:
  beqz a0, .L745
  li a0, 1
  sw a0, -292(fp)
  j .L744
.L745:
  li a0, 0
  sw a0, -292(fp)
.L744:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L749
  sw a0, -300(fp)
  lw a0, -164(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L749:
  beqz a0, .L748
  li a0, 1
  sw a0, -296(fp)
  j .L747
.L748:
  li a0, 0
  sw a0, -296(fp)
.L747:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L752
  sw a0, -300(fp)
  lw a0, -296(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L752:
  beqz a0, .L751
  li a0, 1
  sw a0, -168(fp)
  j .L750
.L751:
  li a0, 0
  sw a0, -168(fp)
.L750:
  lw a0, -44(fp)
  snez a0, a0
  bnez a0, .L755
  sw a0, -296(fp)
  lw a0, -108(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L755:
  beqz a0, .L754
  li a0, 1
  sw a0, -292(fp)
  j .L753
.L754:
  li a0, 0
  sw a0, -292(fp)
.L753:
  lw a0, -44(fp)
  snez a0, a0
  beqz a0, .L758
  sw a0, -300(fp)
  lw a0, -108(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L758:
  beqz a0, .L757
  li a0, 1
  sw a0, -296(fp)
  j .L756
.L757:
  li a0, 0
  sw a0, -296(fp)
.L756:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L760
  li a0, 1
  sw a0, -300(fp)
  j .L759
.L760:
  li a0, 0
  sw a0, -300(fp)
.L759:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L763
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L763:
  beqz a0, .L762
  li a0, 1
  sw a0, -288(fp)
  j .L761
.L762:
  li a0, 0
  sw a0, -288(fp)
.L761:
  lw a0, -288(fp)
  snez a0, a0
  bnez a0, .L766
  sw a0, -296(fp)
  lw a0, -168(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L766:
  beqz a0, .L765
  li a0, 1
  sw a0, -292(fp)
  j .L764
.L765:
  li a0, 0
  sw a0, -292(fp)
.L764:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L769
  sw a0, -300(fp)
  lw a0, -168(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L769:
  beqz a0, .L768
  li a0, 1
  sw a0, -296(fp)
  j .L767
.L768:
  li a0, 0
  sw a0, -296(fp)
.L767:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L771
  li a0, 1
  sw a0, -300(fp)
  j .L770
.L771:
  li a0, 0
  sw a0, -300(fp)
.L770:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L774
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L774:
  beqz a0, .L773
  li a0, 1
  sw a0, -232(fp)
  j .L772
.L773:
  li a0, 0
  sw a0, -232(fp)
.L772:
  lw a0, -44(fp)
  snez a0, a0
  beqz a0, .L777
  sw a0, -296(fp)
  lw a0, -108(fp)
  snez a0, a0
  lw t0, -296(fp)
  and a0, t0, a0
.L777:
  beqz a0, .L776
  li a0, 1
  sw a0, -292(fp)
  j .L775
.L776:
  li a0, 0
  sw a0, -292(fp)
.L775:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L780
  sw a0, -300(fp)
  lw a0, -168(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L780:
  beqz a0, .L779
  li a0, 1
  sw a0, -296(fp)
  j .L778
.L779:
  li a0, 0
  sw a0, -296(fp)
.L778:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L783
  sw a0, -300(fp)
  lw a0, -296(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L783:
  beqz a0, .L782
  li a0, 1
  sw a0, -172(fp)
  j .L781
.L782:
  li a0, 0
  sw a0, -172(fp)
.L781:
  lw a0, -48(fp)
  snez a0, a0
  bnez a0, .L786
  sw a0, -296(fp)
  lw a0, -112(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L786:
  beqz a0, .L785
  li a0, 1
  sw a0, -292(fp)
  j .L784
.L785:
  li a0, 0
  sw a0, -292(fp)
.L784:
  lw a0, -48(fp)
  snez a0, a0
  beqz a0, .L789
  sw a0, -300(fp)
  lw a0, -112(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L789:
  beqz a0, .L788
  li a0, 1
  sw a0, -296(fp)
  j .L787
.L788:
  li a0, 0
  sw a0, -296(fp)
.L787:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L791
  li a0, 1
  sw a0, -300(fp)
  j .L790
.L791:
  li a0, 0
  sw a0, -300(fp)
.L790:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L794
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L794:
  beqz a0, .L793
  li a0, 1
  sw a0, -288(fp)
  j .L792
.L793:
  li a0, 0
  sw a0, -288(fp)
.L792:
  lw a0, -288(fp)
  snez a0, a0
  bnez a0, .L797
  sw a0, -296(fp)
  lw a0, -172(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L797:
  beqz a0, .L796
  li a0, 1
  sw a0, -292(fp)
  j .L795
.L796:
  li a0, 0
  sw a0, -292(fp)
.L795:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L800
  sw a0, -300(fp)
  lw a0, -172(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L800:
  beqz a0, .L799
  li a0, 1
  sw a0, -296(fp)
  j .L798
.L799:
  li a0, 0
  sw a0, -296(fp)
.L798:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L802
  li a0, 1
  sw a0, -300(fp)
  j .L801
.L802:
  li a0, 0
  sw a0, -300(fp)
.L801:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L805
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L805:
  beqz a0, .L804
  li a0, 1
  sw a0, -236(fp)
  j .L803
.L804:
  li a0, 0
  sw a0, -236(fp)
.L803:
  lw a0, -48(fp)
  snez a0, a0
  beqz a0, .L808
  sw a0, -296(fp)
  lw a0, -112(fp)
  snez a0, a0
  lw t0, -296(fp)
  and a0, t0, a0
.L808:
  beqz a0, .L807
  li a0, 1
  sw a0, -292(fp)
  j .L806
.L807:
  li a0, 0
  sw a0, -292(fp)
.L806:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L811
  sw a0, -300(fp)
  lw a0, -172(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L811:
  beqz a0, .L810
  li a0, 1
  sw a0, -296(fp)
  j .L809
.L810:
  li a0, 0
  sw a0, -296(fp)
.L809:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L814
  sw a0, -300(fp)
  lw a0, -296(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L814:
  beqz a0, .L813
  li a0, 1
  sw a0, -176(fp)
  j .L812
.L813:
  li a0, 0
  sw a0, -176(fp)
.L812:
  lw a0, -52(fp)
  snez a0, a0
  bnez a0, .L817
  sw a0, -296(fp)
  lw a0, -116(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L817:
  beqz a0, .L816
  li a0, 1
  sw a0, -292(fp)
  j .L815
.L816:
  li a0, 0
  sw a0, -292(fp)
.L815:
  lw a0, -52(fp)
  snez a0, a0
  beqz a0, .L820
  sw a0, -300(fp)
  lw a0, -116(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L820:
  beqz a0, .L819
  li a0, 1
  sw a0, -296(fp)
  j .L818
.L819:
  li a0, 0
  sw a0, -296(fp)
.L818:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L822
  li a0, 1
  sw a0, -300(fp)
  j .L821
.L822:
  li a0, 0
  sw a0, -300(fp)
.L821:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L825
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L825:
  beqz a0, .L824
  li a0, 1
  sw a0, -288(fp)
  j .L823
.L824:
  li a0, 0
  sw a0, -288(fp)
.L823:
  lw a0, -288(fp)
  snez a0, a0
  bnez a0, .L828
  sw a0, -296(fp)
  lw a0, -176(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L828:
  beqz a0, .L827
  li a0, 1
  sw a0, -292(fp)
  j .L826
.L827:
  li a0, 0
  sw a0, -292(fp)
.L826:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L831
  sw a0, -300(fp)
  lw a0, -176(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L831:
  beqz a0, .L830
  li a0, 1
  sw a0, -296(fp)
  j .L829
.L830:
  li a0, 0
  sw a0, -296(fp)
.L829:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L833
  li a0, 1
  sw a0, -300(fp)
  j .L832
.L833:
  li a0, 0
  sw a0, -300(fp)
.L832:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L836
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L836:
  beqz a0, .L835
  li a0, 1
  sw a0, -240(fp)
  j .L834
.L835:
  li a0, 0
  sw a0, -240(fp)
.L834:
  lw a0, -52(fp)
  snez a0, a0
  beqz a0, .L839
  sw a0, -296(fp)
  lw a0, -116(fp)
  snez a0, a0
  lw t0, -296(fp)
  and a0, t0, a0
.L839:
  beqz a0, .L838
  li a0, 1
  sw a0, -292(fp)
  j .L837
.L838:
  li a0, 0
  sw a0, -292(fp)
.L837:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L842
  sw a0, -300(fp)
  lw a0, -176(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L842:
  beqz a0, .L841
  li a0, 1
  sw a0, -296(fp)
  j .L840
.L841:
  li a0, 0
  sw a0, -296(fp)
.L840:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L845
  sw a0, -300(fp)
  lw a0, -296(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L845:
  beqz a0, .L844
  li a0, 1
  sw a0, -180(fp)
  j .L843
.L844:
  li a0, 0
  sw a0, -180(fp)
.L843:
  lw a0, -56(fp)
  snez a0, a0
  bnez a0, .L848
  sw a0, -296(fp)
  lw a0, -120(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L848:
  beqz a0, .L847
  li a0, 1
  sw a0, -292(fp)
  j .L846
.L847:
  li a0, 0
  sw a0, -292(fp)
.L846:
  lw a0, -56(fp)
  snez a0, a0
  beqz a0, .L851
  sw a0, -300(fp)
  lw a0, -120(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L851:
  beqz a0, .L850
  li a0, 1
  sw a0, -296(fp)
  j .L849
.L850:
  li a0, 0
  sw a0, -296(fp)
.L849:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L853
  li a0, 1
  sw a0, -300(fp)
  j .L852
.L853:
  li a0, 0
  sw a0, -300(fp)
.L852:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L856
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L856:
  beqz a0, .L855
  li a0, 1
  sw a0, -288(fp)
  j .L854
.L855:
  li a0, 0
  sw a0, -288(fp)
.L854:
  lw a0, -288(fp)
  snez a0, a0
  bnez a0, .L859
  sw a0, -296(fp)
  lw a0, -180(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L859:
  beqz a0, .L858
  li a0, 1
  sw a0, -292(fp)
  j .L857
.L858:
  li a0, 0
  sw a0, -292(fp)
.L857:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L862
  sw a0, -300(fp)
  lw a0, -180(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L862:
  beqz a0, .L861
  li a0, 1
  sw a0, -296(fp)
  j .L860
.L861:
  li a0, 0
  sw a0, -296(fp)
.L860:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L864
  li a0, 1
  sw a0, -300(fp)
  j .L863
.L864:
  li a0, 0
  sw a0, -300(fp)
.L863:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L867
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L867:
  beqz a0, .L866
  li a0, 1
  sw a0, -244(fp)
  j .L865
.L866:
  li a0, 0
  sw a0, -244(fp)
.L865:
  lw a0, -56(fp)
  snez a0, a0
  beqz a0, .L870
  sw a0, -296(fp)
  lw a0, -120(fp)
  snez a0, a0
  lw t0, -296(fp)
  and a0, t0, a0
.L870:
  beqz a0, .L869
  li a0, 1
  sw a0, -292(fp)
  j .L868
.L869:
  li a0, 0
  sw a0, -292(fp)
.L868:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L873
  sw a0, -300(fp)
  lw a0, -180(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L873:
  beqz a0, .L872
  li a0, 1
  sw a0, -296(fp)
  j .L871
.L872:
  li a0, 0
  sw a0, -296(fp)
.L871:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L876
  sw a0, -300(fp)
  lw a0, -296(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L876:
  beqz a0, .L875
  li a0, 1
  sw a0, -184(fp)
  j .L874
.L875:
  li a0, 0
  sw a0, -184(fp)
.L874:
  lw a0, -60(fp)
  snez a0, a0
  bnez a0, .L879
  sw a0, -296(fp)
  lw a0, -124(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L879:
  beqz a0, .L878
  li a0, 1
  sw a0, -292(fp)
  j .L877
.L878:
  li a0, 0
  sw a0, -292(fp)
.L877:
  lw a0, -60(fp)
  snez a0, a0
  beqz a0, .L882
  sw a0, -300(fp)
  lw a0, -124(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L882:
  beqz a0, .L881
  li a0, 1
  sw a0, -296(fp)
  j .L880
.L881:
  li a0, 0
  sw a0, -296(fp)
.L880:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L884
  li a0, 1
  sw a0, -300(fp)
  j .L883
.L884:
  li a0, 0
  sw a0, -300(fp)
.L883:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L887
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L887:
  beqz a0, .L886
  li a0, 1
  sw a0, -288(fp)
  j .L885
.L886:
  li a0, 0
  sw a0, -288(fp)
.L885:
  lw a0, -288(fp)
  snez a0, a0
  bnez a0, .L890
  sw a0, -296(fp)
  lw a0, -184(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L890:
  beqz a0, .L889
  li a0, 1
  sw a0, -292(fp)
  j .L888
.L889:
  li a0, 0
  sw a0, -292(fp)
.L888:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L893
  sw a0, -300(fp)
  lw a0, -184(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L893:
  beqz a0, .L892
  li a0, 1
  sw a0, -296(fp)
  j .L891
.L892:
  li a0, 0
  sw a0, -296(fp)
.L891:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L895
  li a0, 1
  sw a0, -300(fp)
  j .L894
.L895:
  li a0, 0
  sw a0, -300(fp)
.L894:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L898
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L898:
  beqz a0, .L897
  li a0, 1
  sw a0, -248(fp)
  j .L896
.L897:
  li a0, 0
  sw a0, -248(fp)
.L896:
  lw a0, -60(fp)
  snez a0, a0
  beqz a0, .L901
  sw a0, -296(fp)
  lw a0, -124(fp)
  snez a0, a0
  lw t0, -296(fp)
  and a0, t0, a0
.L901:
  beqz a0, .L900
  li a0, 1
  sw a0, -292(fp)
  j .L899
.L900:
  li a0, 0
  sw a0, -292(fp)
.L899:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L904
  sw a0, -300(fp)
  lw a0, -184(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L904:
  beqz a0, .L903
  li a0, 1
  sw a0, -296(fp)
  j .L902
.L903:
  li a0, 0
  sw a0, -296(fp)
.L902:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L907
  sw a0, -300(fp)
  lw a0, -296(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L907:
  beqz a0, .L906
  li a0, 1
  sw a0, -188(fp)
  j .L905
.L906:
  li a0, 0
  sw a0, -188(fp)
.L905:
  lw a0, -64(fp)
  snez a0, a0
  bnez a0, .L910
  sw a0, -296(fp)
  lw a0, -128(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L910:
  beqz a0, .L909
  li a0, 1
  sw a0, -292(fp)
  j .L908
.L909:
  li a0, 0
  sw a0, -292(fp)
.L908:
  lw a0, -64(fp)
  snez a0, a0
  beqz a0, .L913
  sw a0, -300(fp)
  lw a0, -128(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L913:
  beqz a0, .L912
  li a0, 1
  sw a0, -296(fp)
  j .L911
.L912:
  li a0, 0
  sw a0, -296(fp)
.L911:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L915
  li a0, 1
  sw a0, -300(fp)
  j .L914
.L915:
  li a0, 0
  sw a0, -300(fp)
.L914:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L918
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L918:
  beqz a0, .L917
  li a0, 1
  sw a0, -288(fp)
  j .L916
.L917:
  li a0, 0
  sw a0, -288(fp)
.L916:
  lw a0, -288(fp)
  snez a0, a0
  bnez a0, .L921
  sw a0, -296(fp)
  lw a0, -188(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L921:
  beqz a0, .L920
  li a0, 1
  sw a0, -292(fp)
  j .L919
.L920:
  li a0, 0
  sw a0, -292(fp)
.L919:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L924
  sw a0, -300(fp)
  lw a0, -188(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L924:
  beqz a0, .L923
  li a0, 1
  sw a0, -296(fp)
  j .L922
.L923:
  li a0, 0
  sw a0, -296(fp)
.L922:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L926
  li a0, 1
  sw a0, -300(fp)
  j .L925
.L926:
  li a0, 0
  sw a0, -300(fp)
.L925:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L929
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L929:
  beqz a0, .L928
  li a0, 1
  sw a0, -252(fp)
  j .L927
.L928:
  li a0, 0
  sw a0, -252(fp)
.L927:
  lw a0, -64(fp)
  snez a0, a0
  beqz a0, .L932
  sw a0, -296(fp)
  lw a0, -128(fp)
  snez a0, a0
  lw t0, -296(fp)
  and a0, t0, a0
.L932:
  beqz a0, .L931
  li a0, 1
  sw a0, -292(fp)
  j .L930
.L931:
  li a0, 0
  sw a0, -292(fp)
.L930:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L935
  sw a0, -300(fp)
  lw a0, -188(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L935:
  beqz a0, .L934
  li a0, 1
  sw a0, -296(fp)
  j .L933
.L934:
  li a0, 0
  sw a0, -296(fp)
.L933:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L938
  sw a0, -300(fp)
  lw a0, -296(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L938:
  beqz a0, .L937
  li a0, 1
  sw a0, -192(fp)
  j .L936
.L937:
  li a0, 0
  sw a0, -192(fp)
.L936:
  lw a0, -68(fp)
  snez a0, a0
  bnez a0, .L941
  sw a0, -296(fp)
  lw a0, -132(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L941:
  beqz a0, .L940
  li a0, 1
  sw a0, -292(fp)
  j .L939
.L940:
  li a0, 0
  sw a0, -292(fp)
.L939:
  lw a0, -68(fp)
  snez a0, a0
  beqz a0, .L944
  sw a0, -300(fp)
  lw a0, -132(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L944:
  beqz a0, .L943
  li a0, 1
  sw a0, -296(fp)
  j .L942
.L943:
  li a0, 0
  sw a0, -296(fp)
.L942:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L946
  li a0, 1
  sw a0, -300(fp)
  j .L945
.L946:
  li a0, 0
  sw a0, -300(fp)
.L945:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L949
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L949:
  beqz a0, .L948
  li a0, 1
  sw a0, -288(fp)
  j .L947
.L948:
  li a0, 0
  sw a0, -288(fp)
.L947:
  lw a0, -288(fp)
  snez a0, a0
  bnez a0, .L952
  sw a0, -296(fp)
  lw a0, -192(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L952:
  beqz a0, .L951
  li a0, 1
  sw a0, -292(fp)
  j .L950
.L951:
  li a0, 0
  sw a0, -292(fp)
.L950:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L955
  sw a0, -300(fp)
  lw a0, -192(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L955:
  beqz a0, .L954
  li a0, 1
  sw a0, -296(fp)
  j .L953
.L954:
  li a0, 0
  sw a0, -296(fp)
.L953:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L957
  li a0, 1
  sw a0, -300(fp)
  j .L956
.L957:
  li a0, 0
  sw a0, -300(fp)
.L956:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L960
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L960:
  beqz a0, .L959
  li a0, 1
  sw a0, -256(fp)
  j .L958
.L959:
  li a0, 0
  sw a0, -256(fp)
.L958:
  lw a0, -68(fp)
  snez a0, a0
  beqz a0, .L963
  sw a0, -296(fp)
  lw a0, -132(fp)
  snez a0, a0
  lw t0, -296(fp)
  and a0, t0, a0
.L963:
  beqz a0, .L962
  li a0, 1
  sw a0, -292(fp)
  j .L961
.L962:
  li a0, 0
  sw a0, -292(fp)
.L961:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L966
  sw a0, -300(fp)
  lw a0, -192(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L966:
  beqz a0, .L965
  li a0, 1
  sw a0, -296(fp)
  j .L964
.L965:
  li a0, 0
  sw a0, -296(fp)
.L964:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L969
  sw a0, -300(fp)
  lw a0, -296(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L969:
  beqz a0, .L968
  li a0, 1
  sw a0, -196(fp)
  j .L967
.L968:
  li a0, 0
  sw a0, -196(fp)
.L967:
  lw a0, -72(fp)
  snez a0, a0
  bnez a0, .L972
  sw a0, -296(fp)
  lw a0, -136(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L972:
  beqz a0, .L971
  li a0, 1
  sw a0, -292(fp)
  j .L970
.L971:
  li a0, 0
  sw a0, -292(fp)
.L970:
  lw a0, -72(fp)
  snez a0, a0
  beqz a0, .L975
  sw a0, -300(fp)
  lw a0, -136(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L975:
  beqz a0, .L974
  li a0, 1
  sw a0, -296(fp)
  j .L973
.L974:
  li a0, 0
  sw a0, -296(fp)
.L973:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L977
  li a0, 1
  sw a0, -300(fp)
  j .L976
.L977:
  li a0, 0
  sw a0, -300(fp)
.L976:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L980
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L980:
  beqz a0, .L979
  li a0, 1
  sw a0, -288(fp)
  j .L978
.L979:
  li a0, 0
  sw a0, -288(fp)
.L978:
  lw a0, -288(fp)
  snez a0, a0
  bnez a0, .L983
  sw a0, -296(fp)
  lw a0, -196(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L983:
  beqz a0, .L982
  li a0, 1
  sw a0, -292(fp)
  j .L981
.L982:
  li a0, 0
  sw a0, -292(fp)
.L981:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L986
  sw a0, -300(fp)
  lw a0, -196(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L986:
  beqz a0, .L985
  li a0, 1
  sw a0, -296(fp)
  j .L984
.L985:
  li a0, 0
  sw a0, -296(fp)
.L984:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L988
  li a0, 1
  sw a0, -300(fp)
  j .L987
.L988:
  li a0, 0
  sw a0, -300(fp)
.L987:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L991
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L991:
  beqz a0, .L990
  li a0, 1
  sw a0, -260(fp)
  j .L989
.L990:
  li a0, 0
  sw a0, -260(fp)
.L989:
  lw a0, -72(fp)
  snez a0, a0
  beqz a0, .L994
  sw a0, -296(fp)
  lw a0, -136(fp)
  snez a0, a0
  lw t0, -296(fp)
  and a0, t0, a0
.L994:
  beqz a0, .L993
  li a0, 1
  sw a0, -292(fp)
  j .L992
.L993:
  li a0, 0
  sw a0, -292(fp)
.L992:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L997
  sw a0, -300(fp)
  lw a0, -196(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L997:
  beqz a0, .L996
  li a0, 1
  sw a0, -296(fp)
  j .L995
.L996:
  li a0, 0
  sw a0, -296(fp)
.L995:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L1000
  sw a0, -300(fp)
  lw a0, -296(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L1000:
  beqz a0, .L999
  li a0, 1
  sw a0, -200(fp)
  j .L998
.L999:
  li a0, 0
  sw a0, -200(fp)
.L998:
  lw a0, -76(fp)
  snez a0, a0
  bnez a0, .L1003
  sw a0, -296(fp)
  lw a0, -140(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L1003:
  beqz a0, .L1002
  li a0, 1
  sw a0, -292(fp)
  j .L1001
.L1002:
  li a0, 0
  sw a0, -292(fp)
.L1001:
  lw a0, -76(fp)
  snez a0, a0
  beqz a0, .L1006
  sw a0, -300(fp)
  lw a0, -140(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L1006:
  beqz a0, .L1005
  li a0, 1
  sw a0, -296(fp)
  j .L1004
.L1005:
  li a0, 0
  sw a0, -296(fp)
.L1004:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L1008
  li a0, 1
  sw a0, -300(fp)
  j .L1007
.L1008:
  li a0, 0
  sw a0, -300(fp)
.L1007:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L1011
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L1011:
  beqz a0, .L1010
  li a0, 1
  sw a0, -288(fp)
  j .L1009
.L1010:
  li a0, 0
  sw a0, -288(fp)
.L1009:
  lw a0, -288(fp)
  snez a0, a0
  bnez a0, .L1014
  sw a0, -296(fp)
  lw a0, -200(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L1014:
  beqz a0, .L1013
  li a0, 1
  sw a0, -292(fp)
  j .L1012
.L1013:
  li a0, 0
  sw a0, -292(fp)
.L1012:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L1017
  sw a0, -300(fp)
  lw a0, -200(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L1017:
  beqz a0, .L1016
  li a0, 1
  sw a0, -296(fp)
  j .L1015
.L1016:
  li a0, 0
  sw a0, -296(fp)
.L1015:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L1019
  li a0, 1
  sw a0, -300(fp)
  j .L1018
.L1019:
  li a0, 0
  sw a0, -300(fp)
.L1018:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L1022
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L1022:
  beqz a0, .L1021
  li a0, 1
  sw a0, -264(fp)
  j .L1020
.L1021:
  li a0, 0
  sw a0, -264(fp)
.L1020:
  lw a0, -76(fp)
  snez a0, a0
  beqz a0, .L1025
  sw a0, -296(fp)
  lw a0, -140(fp)
  snez a0, a0
  lw t0, -296(fp)
  and a0, t0, a0
.L1025:
  beqz a0, .L1024
  li a0, 1
  sw a0, -292(fp)
  j .L1023
.L1024:
  li a0, 0
  sw a0, -292(fp)
.L1023:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L1028
  sw a0, -300(fp)
  lw a0, -200(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L1028:
  beqz a0, .L1027
  li a0, 1
  sw a0, -296(fp)
  j .L1026
.L1027:
  li a0, 0
  sw a0, -296(fp)
.L1026:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L1031
  sw a0, -300(fp)
  lw a0, -296(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L1031:
  beqz a0, .L1030
  li a0, 1
  sw a0, -204(fp)
  j .L1029
.L1030:
  li a0, 0
  sw a0, -204(fp)
.L1029:
  lw a0, -80(fp)
  snez a0, a0
  bnez a0, .L1034
  sw a0, -296(fp)
  lw a0, -144(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L1034:
  beqz a0, .L1033
  li a0, 1
  sw a0, -292(fp)
  j .L1032
.L1033:
  li a0, 0
  sw a0, -292(fp)
.L1032:
  lw a0, -80(fp)
  snez a0, a0
  beqz a0, .L1037
  sw a0, -300(fp)
  lw a0, -144(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L1037:
  beqz a0, .L1036
  li a0, 1
  sw a0, -296(fp)
  j .L1035
.L1036:
  li a0, 0
  sw a0, -296(fp)
.L1035:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L1039
  li a0, 1
  sw a0, -300(fp)
  j .L1038
.L1039:
  li a0, 0
  sw a0, -300(fp)
.L1038:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L1042
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L1042:
  beqz a0, .L1041
  li a0, 1
  sw a0, -288(fp)
  j .L1040
.L1041:
  li a0, 0
  sw a0, -288(fp)
.L1040:
  lw a0, -288(fp)
  snez a0, a0
  bnez a0, .L1045
  sw a0, -296(fp)
  lw a0, -204(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L1045:
  beqz a0, .L1044
  li a0, 1
  sw a0, -292(fp)
  j .L1043
.L1044:
  li a0, 0
  sw a0, -292(fp)
.L1043:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L1048
  sw a0, -300(fp)
  lw a0, -204(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L1048:
  beqz a0, .L1047
  li a0, 1
  sw a0, -296(fp)
  j .L1046
.L1047:
  li a0, 0
  sw a0, -296(fp)
.L1046:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L1050
  li a0, 1
  sw a0, -300(fp)
  j .L1049
.L1050:
  li a0, 0
  sw a0, -300(fp)
.L1049:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L1053
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L1053:
  beqz a0, .L1052
  li a0, 1
  sw a0, -268(fp)
  j .L1051
.L1052:
  li a0, 0
  sw a0, -268(fp)
.L1051:
  lw a0, -80(fp)
  snez a0, a0
  beqz a0, .L1056
  sw a0, -296(fp)
  lw a0, -144(fp)
  snez a0, a0
  lw t0, -296(fp)
  and a0, t0, a0
.L1056:
  beqz a0, .L1055
  li a0, 1
  sw a0, -292(fp)
  j .L1054
.L1055:
  li a0, 0
  sw a0, -292(fp)
.L1054:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L1059
  sw a0, -300(fp)
  lw a0, -204(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L1059:
  beqz a0, .L1058
  li a0, 1
  sw a0, -296(fp)
  j .L1057
.L1058:
  li a0, 0
  sw a0, -296(fp)
.L1057:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L1062
  sw a0, -300(fp)
  lw a0, -296(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L1062:
  beqz a0, .L1061
  li a0, 1
  sw a0, -208(fp)
  j .L1060
.L1061:
  li a0, 0
  sw a0, -208(fp)
.L1060:
  lw a0, -84(fp)
  snez a0, a0
  bnez a0, .L1065
  sw a0, -296(fp)
  lw a0, -148(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L1065:
  beqz a0, .L1064
  li a0, 1
  sw a0, -292(fp)
  j .L1063
.L1064:
  li a0, 0
  sw a0, -292(fp)
.L1063:
  lw a0, -84(fp)
  snez a0, a0
  beqz a0, .L1068
  sw a0, -300(fp)
  lw a0, -148(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L1068:
  beqz a0, .L1067
  li a0, 1
  sw a0, -296(fp)
  j .L1066
.L1067:
  li a0, 0
  sw a0, -296(fp)
.L1066:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L1070
  li a0, 1
  sw a0, -300(fp)
  j .L1069
.L1070:
  li a0, 0
  sw a0, -300(fp)
.L1069:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L1073
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L1073:
  beqz a0, .L1072
  li a0, 1
  sw a0, -288(fp)
  j .L1071
.L1072:
  li a0, 0
  sw a0, -288(fp)
.L1071:
  lw a0, -288(fp)
  snez a0, a0
  bnez a0, .L1076
  sw a0, -296(fp)
  lw a0, -208(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L1076:
  beqz a0, .L1075
  li a0, 1
  sw a0, -292(fp)
  j .L1074
.L1075:
  li a0, 0
  sw a0, -292(fp)
.L1074:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L1079
  sw a0, -300(fp)
  lw a0, -208(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L1079:
  beqz a0, .L1078
  li a0, 1
  sw a0, -296(fp)
  j .L1077
.L1078:
  li a0, 0
  sw a0, -296(fp)
.L1077:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L1081
  li a0, 1
  sw a0, -300(fp)
  j .L1080
.L1081:
  li a0, 0
  sw a0, -300(fp)
.L1080:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L1084
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L1084:
  beqz a0, .L1083
  li a0, 1
  sw a0, -272(fp)
  j .L1082
.L1083:
  li a0, 0
  sw a0, -272(fp)
.L1082:
  lw a0, -84(fp)
  snez a0, a0
  beqz a0, .L1087
  sw a0, -296(fp)
  lw a0, -148(fp)
  snez a0, a0
  lw t0, -296(fp)
  and a0, t0, a0
.L1087:
  beqz a0, .L1086
  li a0, 1
  sw a0, -292(fp)
  j .L1085
.L1086:
  li a0, 0
  sw a0, -292(fp)
.L1085:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L1090
  sw a0, -300(fp)
  lw a0, -208(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L1090:
  beqz a0, .L1089
  li a0, 1
  sw a0, -296(fp)
  j .L1088
.L1089:
  li a0, 0
  sw a0, -296(fp)
.L1088:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L1093
  sw a0, -300(fp)
  lw a0, -296(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L1093:
  beqz a0, .L1092
  li a0, 1
  sw a0, -212(fp)
  j .L1091
.L1092:
  li a0, 0
  sw a0, -212(fp)
.L1091:
  lw a0, -88(fp)
  snez a0, a0
  bnez a0, .L1096
  sw a0, -296(fp)
  lw a0, -152(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L1096:
  beqz a0, .L1095
  li a0, 1
  sw a0, -292(fp)
  j .L1094
.L1095:
  li a0, 0
  sw a0, -292(fp)
.L1094:
  lw a0, -88(fp)
  snez a0, a0
  beqz a0, .L1099
  sw a0, -300(fp)
  lw a0, -152(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L1099:
  beqz a0, .L1098
  li a0, 1
  sw a0, -296(fp)
  j .L1097
.L1098:
  li a0, 0
  sw a0, -296(fp)
.L1097:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L1101
  li a0, 1
  sw a0, -300(fp)
  j .L1100
.L1101:
  li a0, 0
  sw a0, -300(fp)
.L1100:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L1104
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L1104:
  beqz a0, .L1103
  li a0, 1
  sw a0, -288(fp)
  j .L1102
.L1103:
  li a0, 0
  sw a0, -288(fp)
.L1102:
  lw a0, -288(fp)
  snez a0, a0
  bnez a0, .L1107
  sw a0, -296(fp)
  lw a0, -212(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L1107:
  beqz a0, .L1106
  li a0, 1
  sw a0, -292(fp)
  j .L1105
.L1106:
  li a0, 0
  sw a0, -292(fp)
.L1105:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L1110
  sw a0, -300(fp)
  lw a0, -212(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L1110:
  beqz a0, .L1109
  li a0, 1
  sw a0, -296(fp)
  j .L1108
.L1109:
  li a0, 0
  sw a0, -296(fp)
.L1108:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L1112
  li a0, 1
  sw a0, -300(fp)
  j .L1111
.L1112:
  li a0, 0
  sw a0, -300(fp)
.L1111:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L1115
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L1115:
  beqz a0, .L1114
  li a0, 1
  sw a0, -276(fp)
  j .L1113
.L1114:
  li a0, 0
  sw a0, -276(fp)
.L1113:
  lw a0, -88(fp)
  snez a0, a0
  beqz a0, .L1118
  sw a0, -296(fp)
  lw a0, -152(fp)
  snez a0, a0
  lw t0, -296(fp)
  and a0, t0, a0
.L1118:
  beqz a0, .L1117
  li a0, 1
  sw a0, -292(fp)
  j .L1116
.L1117:
  li a0, 0
  sw a0, -292(fp)
.L1116:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L1121
  sw a0, -300(fp)
  lw a0, -212(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L1121:
  beqz a0, .L1120
  li a0, 1
  sw a0, -296(fp)
  j .L1119
.L1120:
  li a0, 0
  sw a0, -296(fp)
.L1119:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L1124
  sw a0, -300(fp)
  lw a0, -296(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L1124:
  beqz a0, .L1123
  li a0, 1
  sw a0, -216(fp)
  j .L1122
.L1123:
  li a0, 0
  sw a0, -216(fp)
.L1122:
  lw a0, -92(fp)
  snez a0, a0
  bnez a0, .L1127
  sw a0, -296(fp)
  lw a0, -156(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L1127:
  beqz a0, .L1126
  li a0, 1
  sw a0, -292(fp)
  j .L1125
.L1126:
  li a0, 0
  sw a0, -292(fp)
.L1125:
  lw a0, -92(fp)
  snez a0, a0
  beqz a0, .L1130
  sw a0, -300(fp)
  lw a0, -156(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L1130:
  beqz a0, .L1129
  li a0, 1
  sw a0, -296(fp)
  j .L1128
.L1129:
  li a0, 0
  sw a0, -296(fp)
.L1128:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L1132
  li a0, 1
  sw a0, -300(fp)
  j .L1131
.L1132:
  li a0, 0
  sw a0, -300(fp)
.L1131:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L1135
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L1135:
  beqz a0, .L1134
  li a0, 1
  sw a0, -288(fp)
  j .L1133
.L1134:
  li a0, 0
  sw a0, -288(fp)
.L1133:
  lw a0, -288(fp)
  snez a0, a0
  bnez a0, .L1138
  sw a0, -296(fp)
  lw a0, -216(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L1138:
  beqz a0, .L1137
  li a0, 1
  sw a0, -292(fp)
  j .L1136
.L1137:
  li a0, 0
  sw a0, -292(fp)
.L1136:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L1141
  sw a0, -300(fp)
  lw a0, -216(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L1141:
  beqz a0, .L1140
  li a0, 1
  sw a0, -296(fp)
  j .L1139
.L1140:
  li a0, 0
  sw a0, -296(fp)
.L1139:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L1143
  li a0, 1
  sw a0, -300(fp)
  j .L1142
.L1143:
  li a0, 0
  sw a0, -300(fp)
.L1142:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L1146
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L1146:
  beqz a0, .L1145
  li a0, 1
  sw a0, -280(fp)
  j .L1144
.L1145:
  li a0, 0
  sw a0, -280(fp)
.L1144:
  lw a0, -92(fp)
  snez a0, a0
  beqz a0, .L1149
  sw a0, -296(fp)
  lw a0, -156(fp)
  snez a0, a0
  lw t0, -296(fp)
  and a0, t0, a0
.L1149:
  beqz a0, .L1148
  li a0, 1
  sw a0, -292(fp)
  j .L1147
.L1148:
  li a0, 0
  sw a0, -292(fp)
.L1147:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L1152
  sw a0, -300(fp)
  lw a0, -216(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L1152:
  beqz a0, .L1151
  li a0, 1
  sw a0, -296(fp)
  j .L1150
.L1151:
  li a0, 0
  sw a0, -296(fp)
.L1150:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L1155
  sw a0, -300(fp)
  lw a0, -296(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L1155:
  beqz a0, .L1154
  li a0, 1
  sw a0, -220(fp)
  j .L1153
.L1154:
  li a0, 0
  sw a0, -220(fp)
.L1153:
  lw a0, -96(fp)
  snez a0, a0
  bnez a0, .L1158
  sw a0, -296(fp)
  lw a0, -160(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L1158:
  beqz a0, .L1157
  li a0, 1
  sw a0, -292(fp)
  j .L1156
.L1157:
  li a0, 0
  sw a0, -292(fp)
.L1156:
  lw a0, -96(fp)
  snez a0, a0
  beqz a0, .L1161
  sw a0, -300(fp)
  lw a0, -160(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L1161:
  beqz a0, .L1160
  li a0, 1
  sw a0, -296(fp)
  j .L1159
.L1160:
  li a0, 0
  sw a0, -296(fp)
.L1159:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L1163
  li a0, 1
  sw a0, -300(fp)
  j .L1162
.L1163:
  li a0, 0
  sw a0, -300(fp)
.L1162:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L1166
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L1166:
  beqz a0, .L1165
  li a0, 1
  sw a0, -288(fp)
  j .L1164
.L1165:
  li a0, 0
  sw a0, -288(fp)
.L1164:
  lw a0, -288(fp)
  snez a0, a0
  bnez a0, .L1169
  sw a0, -296(fp)
  lw a0, -220(fp)
  snez a0, a0
  lw t0, -296(fp)
  or a0, t0, a0
.L1169:
  beqz a0, .L1168
  li a0, 1
  sw a0, -292(fp)
  j .L1167
.L1168:
  li a0, 0
  sw a0, -292(fp)
.L1167:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L1172
  sw a0, -300(fp)
  lw a0, -220(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L1172:
  beqz a0, .L1171
  li a0, 1
  sw a0, -296(fp)
  j .L1170
.L1171:
  li a0, 0
  sw a0, -296(fp)
.L1170:
  lw a0, -296(fp)
  seqz a0, a0
  beqz a0, .L1174
  li a0, 1
  sw a0, -300(fp)
  j .L1173
.L1174:
  li a0, 0
  sw a0, -300(fp)
.L1173:
  lw a0, -292(fp)
  snez a0, a0
  beqz a0, .L1177
  sw a0, -304(fp)
  lw a0, -300(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L1177:
  beqz a0, .L1176
  li a0, 1
  sw a0, -284(fp)
  j .L1175
.L1176:
  li a0, 0
  sw a0, -284(fp)
.L1175:
  lw a0, -96(fp)
  snez a0, a0
  beqz a0, .L1180
  sw a0, -296(fp)
  lw a0, -160(fp)
  snez a0, a0
  lw t0, -296(fp)
  and a0, t0, a0
.L1180:
  beqz a0, .L1179
  li a0, 1
  sw a0, -292(fp)
  j .L1178
.L1179:
  li a0, 0
  sw a0, -292(fp)
.L1178:
  lw a0, -288(fp)
  snez a0, a0
  beqz a0, .L1183
  sw a0, -300(fp)
  lw a0, -220(fp)
  snez a0, a0
  lw t0, -300(fp)
  and a0, t0, a0
.L1183:
  beqz a0, .L1182
  li a0, 1
  sw a0, -296(fp)
  j .L1181
.L1182:
  li a0, 0
  sw a0, -296(fp)
.L1181:
  lw a0, -292(fp)
  snez a0, a0
  bnez a0, .L1186
  sw a0, -300(fp)
  lw a0, -296(fp)
  snez a0, a0
  lw t0, -300(fp)
  or a0, t0, a0
.L1186:
  beqz a0, .L1185
  li a0, 1
  sw a0, -32(fp)
  j .L1184
.L1185:
  li a0, 0
  sw a0, -32(fp)
.L1184:
  li a0, 0
  sw a0, -24(fp)
  lw a0, -24(fp)
  sw a0, -288(fp)
  li a0, 2
  lw t0, -288(fp)
  mulw a0, t0, a0
  sw a0, -288(fp)
  lw a0, -284(fp)
  lw t0, -288(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -24(fp)
  sw a0, -288(fp)
  li a0, 2
  lw t0, -288(fp)
  mulw a0, t0, a0
  sw a0, -288(fp)
  lw a0, -280(fp)
  lw t0, -288(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -24(fp)
  sw a0, -288(fp)
  li a0, 2
  lw t0, -288(fp)
  mulw a0, t0, a0
  sw a0, -288(fp)
  lw a0, -276(fp)
  lw t0, -288(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -24(fp)
  sw a0, -288(fp)
  li a0, 2
  lw t0, -288(fp)
  mulw a0, t0, a0
  sw a0, -288(fp)
  lw a0, -272(fp)
  lw t0, -288(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -24(fp)
  sw a0, -288(fp)
  li a0, 2
  lw t0, -288(fp)
  mulw a0, t0, a0
  sw a0, -288(fp)
  lw a0, -268(fp)
  lw t0, -288(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -24(fp)
  sw a0, -288(fp)
  li a0, 2
  lw t0, -288(fp)
  mulw a0, t0, a0
  sw a0, -288(fp)
  lw a0, -264(fp)
  lw t0, -288(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -24(fp)
  sw a0, -288(fp)
  li a0, 2
  lw t0, -288(fp)
  mulw a0, t0, a0
  sw a0, -288(fp)
  lw a0, -260(fp)
  lw t0, -288(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -24(fp)
  sw a0, -288(fp)
  li a0, 2
  lw t0, -288(fp)
  mulw a0, t0, a0
  sw a0, -288(fp)
  lw a0, -256(fp)
  lw t0, -288(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -24(fp)
  sw a0, -288(fp)
  li a0, 2
  lw t0, -288(fp)
  mulw a0, t0, a0
  sw a0, -288(fp)
  lw a0, -252(fp)
  lw t0, -288(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -24(fp)
  sw a0, -288(fp)
  li a0, 2
  lw t0, -288(fp)
  mulw a0, t0, a0
  sw a0, -288(fp)
  lw a0, -248(fp)
  lw t0, -288(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -24(fp)
  sw a0, -288(fp)
  li a0, 2
  lw t0, -288(fp)
  mulw a0, t0, a0
  sw a0, -288(fp)
  lw a0, -244(fp)
  lw t0, -288(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -24(fp)
  sw a0, -288(fp)
  li a0, 2
  lw t0, -288(fp)
  mulw a0, t0, a0
  sw a0, -288(fp)
  lw a0, -240(fp)
  lw t0, -288(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -24(fp)
  sw a0, -288(fp)
  li a0, 2
  lw t0, -288(fp)
  mulw a0, t0, a0
  sw a0, -288(fp)
  lw a0, -236(fp)
  lw t0, -288(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -24(fp)
  sw a0, -288(fp)
  li a0, 2
  lw t0, -288(fp)
  mulw a0, t0, a0
  sw a0, -288(fp)
  lw a0, -232(fp)
  lw t0, -288(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -24(fp)
  sw a0, -288(fp)
  li a0, 2
  lw t0, -288(fp)
  mulw a0, t0, a0
  sw a0, -288(fp)
  lw a0, -228(fp)
  lw t0, -288(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -24(fp)
  sw a0, -288(fp)
  li a0, 2
  lw t0, -288(fp)
  mulw a0, t0, a0
  sw a0, -288(fp)
  lw a0, -224(fp)
  lw t0, -288(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  lw a0, -24(fp)
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, 4(sp)
  call fib
  sw a0, -28(fp)
  li a0, 2
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -44(fp)
  lw a0, -44(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1188
  lw a0, -44(fp)
  negw a0, a0
  sw a0, -44(fp)
  j .L1187
.L1188:
.L1187:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -48(fp)
  lw a0, -48(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1190
  lw a0, -48(fp)
  negw a0, a0
  sw a0, -48(fp)
  j .L1189
.L1190:
.L1189:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -52(fp)
  lw a0, -52(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1192
  lw a0, -52(fp)
  negw a0, a0
  sw a0, -52(fp)
  j .L1191
.L1192:
.L1191:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -56(fp)
  lw a0, -56(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1194
  lw a0, -56(fp)
  negw a0, a0
  sw a0, -56(fp)
  j .L1193
.L1194:
.L1193:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -60(fp)
  lw a0, -60(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1196
  lw a0, -60(fp)
  negw a0, a0
  sw a0, -60(fp)
  j .L1195
.L1196:
.L1195:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -64(fp)
  lw a0, -64(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1198
  lw a0, -64(fp)
  negw a0, a0
  sw a0, -64(fp)
  j .L1197
.L1198:
.L1197:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -68(fp)
  lw a0, -68(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1200
  lw a0, -68(fp)
  negw a0, a0
  sw a0, -68(fp)
  j .L1199
.L1200:
.L1199:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -72(fp)
  lw a0, -72(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1202
  lw a0, -72(fp)
  negw a0, a0
  sw a0, -72(fp)
  j .L1201
.L1202:
.L1201:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -76(fp)
  lw a0, -76(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1204
  lw a0, -76(fp)
  negw a0, a0
  sw a0, -76(fp)
  j .L1203
.L1204:
.L1203:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -80(fp)
  lw a0, -80(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1206
  lw a0, -80(fp)
  negw a0, a0
  sw a0, -80(fp)
  j .L1205
.L1206:
.L1205:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -84(fp)
  lw a0, -84(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1208
  lw a0, -84(fp)
  negw a0, a0
  sw a0, -84(fp)
  j .L1207
.L1208:
.L1207:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -88(fp)
  lw a0, -88(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1210
  lw a0, -88(fp)
  negw a0, a0
  sw a0, -88(fp)
  j .L1209
.L1210:
.L1209:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -92(fp)
  lw a0, -92(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1212
  lw a0, -92(fp)
  negw a0, a0
  sw a0, -92(fp)
  j .L1211
.L1212:
.L1211:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -96(fp)
  lw a0, -96(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1214
  lw a0, -96(fp)
  negw a0, a0
  sw a0, -96(fp)
  j .L1213
.L1214:
.L1213:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1216
  lw a0, -100(fp)
  negw a0, a0
  sw a0, -100(fp)
  j .L1215
.L1216:
.L1215:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1218
  lw a0, -104(fp)
  negw a0, a0
  sw a0, -104(fp)
  j .L1217
.L1218:
.L1217:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -44(fp)
  seqz a0, a0
  beqz a0, .L1220
  li a0, 1
  sw a0, -108(fp)
  j .L1219
.L1220:
  li a0, 0
  sw a0, -108(fp)
.L1219:
  lw a0, -48(fp)
  seqz a0, a0
  beqz a0, .L1222
  li a0, 1
  sw a0, -112(fp)
  j .L1221
.L1222:
  li a0, 0
  sw a0, -112(fp)
.L1221:
  lw a0, -52(fp)
  seqz a0, a0
  beqz a0, .L1224
  li a0, 1
  sw a0, -116(fp)
  j .L1223
.L1224:
  li a0, 0
  sw a0, -116(fp)
.L1223:
  lw a0, -56(fp)
  seqz a0, a0
  beqz a0, .L1226
  li a0, 1
  sw a0, -120(fp)
  j .L1225
.L1226:
  li a0, 0
  sw a0, -120(fp)
.L1225:
  lw a0, -60(fp)
  seqz a0, a0
  beqz a0, .L1228
  li a0, 1
  sw a0, -124(fp)
  j .L1227
.L1228:
  li a0, 0
  sw a0, -124(fp)
.L1227:
  lw a0, -64(fp)
  seqz a0, a0
  beqz a0, .L1230
  li a0, 1
  sw a0, -128(fp)
  j .L1229
.L1230:
  li a0, 0
  sw a0, -128(fp)
.L1229:
  lw a0, -68(fp)
  seqz a0, a0
  beqz a0, .L1232
  li a0, 1
  sw a0, -132(fp)
  j .L1231
.L1232:
  li a0, 0
  sw a0, -132(fp)
.L1231:
  lw a0, -72(fp)
  seqz a0, a0
  beqz a0, .L1234
  li a0, 1
  sw a0, -136(fp)
  j .L1233
.L1234:
  li a0, 0
  sw a0, -136(fp)
.L1233:
  lw a0, -76(fp)
  seqz a0, a0
  beqz a0, .L1236
  li a0, 1
  sw a0, -140(fp)
  j .L1235
.L1236:
  li a0, 0
  sw a0, -140(fp)
.L1235:
  lw a0, -80(fp)
  seqz a0, a0
  beqz a0, .L1238
  li a0, 1
  sw a0, -144(fp)
  j .L1237
.L1238:
  li a0, 0
  sw a0, -144(fp)
.L1237:
  lw a0, -84(fp)
  seqz a0, a0
  beqz a0, .L1240
  li a0, 1
  sw a0, -148(fp)
  j .L1239
.L1240:
  li a0, 0
  sw a0, -148(fp)
.L1239:
  lw a0, -88(fp)
  seqz a0, a0
  beqz a0, .L1242
  li a0, 1
  sw a0, -152(fp)
  j .L1241
.L1242:
  li a0, 0
  sw a0, -152(fp)
.L1241:
  lw a0, -92(fp)
  seqz a0, a0
  beqz a0, .L1244
  li a0, 1
  sw a0, -156(fp)
  j .L1243
.L1244:
  li a0, 0
  sw a0, -156(fp)
.L1243:
  lw a0, -96(fp)
  seqz a0, a0
  beqz a0, .L1246
  li a0, 1
  sw a0, -160(fp)
  j .L1245
.L1246:
  li a0, 0
  sw a0, -160(fp)
.L1245:
  lw a0, -100(fp)
  seqz a0, a0
  beqz a0, .L1248
  li a0, 1
  sw a0, -164(fp)
  j .L1247
.L1248:
  li a0, 0
  sw a0, -164(fp)
.L1247:
  lw a0, -104(fp)
  seqz a0, a0
  beqz a0, .L1250
  li a0, 1
  sw a0, -168(fp)
  j .L1249
.L1250:
  li a0, 0
  sw a0, -168(fp)
.L1249:
  li a0, 0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  mulw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -168(fp)
  lw t0, -172(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  mulw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -164(fp)
  lw t0, -172(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  mulw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -160(fp)
  lw t0, -172(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  mulw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -156(fp)
  lw t0, -172(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  mulw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -152(fp)
  lw t0, -172(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  mulw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -148(fp)
  lw t0, -172(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  mulw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -144(fp)
  lw t0, -172(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  mulw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -140(fp)
  lw t0, -172(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  mulw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -136(fp)
  lw t0, -172(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  mulw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -132(fp)
  lw t0, -172(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  mulw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -128(fp)
  lw t0, -172(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  mulw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -124(fp)
  lw t0, -172(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  mulw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -120(fp)
  lw t0, -172(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  mulw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -116(fp)
  lw t0, -172(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  mulw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -112(fp)
  lw t0, -172(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -172(fp)
  li a0, 2
  lw t0, -172(fp)
  mulw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -108(fp)
  lw t0, -172(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -48(fp)
  lw a0, -48(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L1252
  lw a0, -48(fp)
  negw a0, a0
  sw a0, -48(fp)
  j .L1251
.L1252:
.L1251:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -52(fp)
  lw a0, -52(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L1254
  lw a0, -52(fp)
  negw a0, a0
  sw a0, -52(fp)
  j .L1253
.L1254:
.L1253:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -56(fp)
  lw a0, -56(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L1256
  lw a0, -56(fp)
  negw a0, a0
  sw a0, -56(fp)
  j .L1255
.L1256:
.L1255:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -60(fp)
  lw a0, -60(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L1258
  lw a0, -60(fp)
  negw a0, a0
  sw a0, -60(fp)
  j .L1257
.L1258:
.L1257:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -64(fp)
  lw a0, -64(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L1260
  lw a0, -64(fp)
  negw a0, a0
  sw a0, -64(fp)
  j .L1259
.L1260:
.L1259:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -68(fp)
  lw a0, -68(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L1262
  lw a0, -68(fp)
  negw a0, a0
  sw a0, -68(fp)
  j .L1261
.L1262:
.L1261:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -72(fp)
  lw a0, -72(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L1264
  lw a0, -72(fp)
  negw a0, a0
  sw a0, -72(fp)
  j .L1263
.L1264:
.L1263:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -76(fp)
  lw a0, -76(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L1266
  lw a0, -76(fp)
  negw a0, a0
  sw a0, -76(fp)
  j .L1265
.L1266:
.L1265:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -80(fp)
  lw a0, -80(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L1268
  lw a0, -80(fp)
  negw a0, a0
  sw a0, -80(fp)
  j .L1267
.L1268:
.L1267:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -84(fp)
  lw a0, -84(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L1270
  lw a0, -84(fp)
  negw a0, a0
  sw a0, -84(fp)
  j .L1269
.L1270:
.L1269:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -88(fp)
  lw a0, -88(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L1272
  lw a0, -88(fp)
  negw a0, a0
  sw a0, -88(fp)
  j .L1271
.L1272:
.L1271:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -92(fp)
  lw a0, -92(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L1274
  lw a0, -92(fp)
  negw a0, a0
  sw a0, -92(fp)
  j .L1273
.L1274:
.L1273:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -96(fp)
  lw a0, -96(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L1276
  lw a0, -96(fp)
  negw a0, a0
  sw a0, -96(fp)
  j .L1275
.L1276:
.L1275:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L1278
  lw a0, -100(fp)
  negw a0, a0
  sw a0, -100(fp)
  j .L1277
.L1278:
.L1277:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L1280
  lw a0, -104(fp)
  negw a0, a0
  sw a0, -104(fp)
  j .L1279
.L1280:
.L1279:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L1282
  lw a0, -108(fp)
  negw a0, a0
  sw a0, -108(fp)
  j .L1281
.L1282:
.L1281:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  li a0, 1
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L1284
  lw a0, -112(fp)
  negw a0, a0
  sw a0, -112(fp)
  j .L1283
.L1284:
.L1283:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -116(fp)
  lw a0, -116(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L1286
  lw a0, -116(fp)
  negw a0, a0
  sw a0, -116(fp)
  j .L1285
.L1286:
.L1285:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -120(fp)
  lw a0, -120(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L1288
  lw a0, -120(fp)
  negw a0, a0
  sw a0, -120(fp)
  j .L1287
.L1288:
.L1287:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -124(fp)
  lw a0, -124(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L1290
  lw a0, -124(fp)
  negw a0, a0
  sw a0, -124(fp)
  j .L1289
.L1290:
.L1289:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -128(fp)
  lw a0, -128(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L1292
  lw a0, -128(fp)
  negw a0, a0
  sw a0, -128(fp)
  j .L1291
.L1292:
.L1291:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -132(fp)
  lw a0, -132(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L1294
  lw a0, -132(fp)
  negw a0, a0
  sw a0, -132(fp)
  j .L1293
.L1294:
.L1293:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -136(fp)
  lw a0, -136(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L1296
  lw a0, -136(fp)
  negw a0, a0
  sw a0, -136(fp)
  j .L1295
.L1296:
.L1295:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -140(fp)
  lw a0, -140(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L1298
  lw a0, -140(fp)
  negw a0, a0
  sw a0, -140(fp)
  j .L1297
.L1298:
.L1297:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -144(fp)
  lw a0, -144(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L1300
  lw a0, -144(fp)
  negw a0, a0
  sw a0, -144(fp)
  j .L1299
.L1300:
.L1299:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -148(fp)
  lw a0, -148(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L1302
  lw a0, -148(fp)
  negw a0, a0
  sw a0, -148(fp)
  j .L1301
.L1302:
.L1301:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -152(fp)
  lw a0, -152(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L1304
  lw a0, -152(fp)
  negw a0, a0
  sw a0, -152(fp)
  j .L1303
.L1304:
.L1303:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -156(fp)
  lw a0, -156(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L1306
  lw a0, -156(fp)
  negw a0, a0
  sw a0, -156(fp)
  j .L1305
.L1306:
.L1305:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -160(fp)
  lw a0, -160(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L1308
  lw a0, -160(fp)
  negw a0, a0
  sw a0, -160(fp)
  j .L1307
.L1308:
.L1307:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L1310
  lw a0, -164(fp)
  negw a0, a0
  sw a0, -164(fp)
  j .L1309
.L1310:
.L1309:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L1312
  lw a0, -168(fp)
  negw a0, a0
  sw a0, -168(fp)
  j .L1311
.L1312:
.L1311:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -172(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L1314
  lw a0, -172(fp)
  negw a0, a0
  sw a0, -172(fp)
  j .L1313
.L1314:
.L1313:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -48(fp)
  snez a0, a0
  bnez a0, .L1317
  sw a0, -308(fp)
  lw a0, -112(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1317:
  beqz a0, .L1316
  li a0, 1
  sw a0, -304(fp)
  j .L1315
.L1316:
  li a0, 0
  sw a0, -304(fp)
.L1315:
  lw a0, -48(fp)
  snez a0, a0
  beqz a0, .L1320
  sw a0, -312(fp)
  lw a0, -112(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1320:
  beqz a0, .L1319
  li a0, 1
  sw a0, -308(fp)
  j .L1318
.L1319:
  li a0, 0
  sw a0, -308(fp)
.L1318:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1322
  li a0, 1
  sw a0, -312(fp)
  j .L1321
.L1322:
  li a0, 0
  sw a0, -312(fp)
.L1321:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1325
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1325:
  beqz a0, .L1324
  li a0, 1
  sw a0, -300(fp)
  j .L1323
.L1324:
  li a0, 0
  sw a0, -300(fp)
.L1323:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1328
  sw a0, -308(fp)
  li a0, 0
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1328:
  beqz a0, .L1327
  li a0, 1
  sw a0, -304(fp)
  j .L1326
.L1327:
  li a0, 0
  sw a0, -304(fp)
.L1326:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1331
  sw a0, -312(fp)
  li a0, 0
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1331:
  beqz a0, .L1330
  li a0, 1
  sw a0, -308(fp)
  j .L1329
.L1330:
  li a0, 0
  sw a0, -308(fp)
.L1329:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1333
  li a0, 1
  sw a0, -312(fp)
  j .L1332
.L1333:
  li a0, 0
  sw a0, -312(fp)
.L1332:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1336
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1336:
  beqz a0, .L1335
  li a0, 1
  sw a0, -236(fp)
  j .L1334
.L1335:
  li a0, 0
  sw a0, -236(fp)
.L1334:
  lw a0, -48(fp)
  snez a0, a0
  beqz a0, .L1339
  sw a0, -308(fp)
  lw a0, -112(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1339:
  beqz a0, .L1338
  li a0, 1
  sw a0, -304(fp)
  j .L1337
.L1338:
  li a0, 0
  sw a0, -304(fp)
.L1337:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1342
  sw a0, -312(fp)
  li a0, 0
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1342:
  beqz a0, .L1341
  li a0, 1
  sw a0, -308(fp)
  j .L1340
.L1341:
  li a0, 0
  sw a0, -308(fp)
.L1340:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L1345
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L1345:
  beqz a0, .L1344
  li a0, 1
  sw a0, -176(fp)
  j .L1343
.L1344:
  li a0, 0
  sw a0, -176(fp)
.L1343:
  lw a0, -52(fp)
  snez a0, a0
  bnez a0, .L1348
  sw a0, -308(fp)
  lw a0, -116(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1348:
  beqz a0, .L1347
  li a0, 1
  sw a0, -304(fp)
  j .L1346
.L1347:
  li a0, 0
  sw a0, -304(fp)
.L1346:
  lw a0, -52(fp)
  snez a0, a0
  beqz a0, .L1351
  sw a0, -312(fp)
  lw a0, -116(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1351:
  beqz a0, .L1350
  li a0, 1
  sw a0, -308(fp)
  j .L1349
.L1350:
  li a0, 0
  sw a0, -308(fp)
.L1349:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1353
  li a0, 1
  sw a0, -312(fp)
  j .L1352
.L1353:
  li a0, 0
  sw a0, -312(fp)
.L1352:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1356
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1356:
  beqz a0, .L1355
  li a0, 1
  sw a0, -300(fp)
  j .L1354
.L1355:
  li a0, 0
  sw a0, -300(fp)
.L1354:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1359
  sw a0, -308(fp)
  lw a0, -176(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1359:
  beqz a0, .L1358
  li a0, 1
  sw a0, -304(fp)
  j .L1357
.L1358:
  li a0, 0
  sw a0, -304(fp)
.L1357:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1362
  sw a0, -312(fp)
  lw a0, -176(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1362:
  beqz a0, .L1361
  li a0, 1
  sw a0, -308(fp)
  j .L1360
.L1361:
  li a0, 0
  sw a0, -308(fp)
.L1360:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1364
  li a0, 1
  sw a0, -312(fp)
  j .L1363
.L1364:
  li a0, 0
  sw a0, -312(fp)
.L1363:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1367
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1367:
  beqz a0, .L1366
  li a0, 1
  sw a0, -240(fp)
  j .L1365
.L1366:
  li a0, 0
  sw a0, -240(fp)
.L1365:
  lw a0, -52(fp)
  snez a0, a0
  beqz a0, .L1370
  sw a0, -308(fp)
  lw a0, -116(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1370:
  beqz a0, .L1369
  li a0, 1
  sw a0, -304(fp)
  j .L1368
.L1369:
  li a0, 0
  sw a0, -304(fp)
.L1368:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1373
  sw a0, -312(fp)
  lw a0, -176(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1373:
  beqz a0, .L1372
  li a0, 1
  sw a0, -308(fp)
  j .L1371
.L1372:
  li a0, 0
  sw a0, -308(fp)
.L1371:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L1376
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L1376:
  beqz a0, .L1375
  li a0, 1
  sw a0, -180(fp)
  j .L1374
.L1375:
  li a0, 0
  sw a0, -180(fp)
.L1374:
  lw a0, -56(fp)
  snez a0, a0
  bnez a0, .L1379
  sw a0, -308(fp)
  lw a0, -120(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1379:
  beqz a0, .L1378
  li a0, 1
  sw a0, -304(fp)
  j .L1377
.L1378:
  li a0, 0
  sw a0, -304(fp)
.L1377:
  lw a0, -56(fp)
  snez a0, a0
  beqz a0, .L1382
  sw a0, -312(fp)
  lw a0, -120(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1382:
  beqz a0, .L1381
  li a0, 1
  sw a0, -308(fp)
  j .L1380
.L1381:
  li a0, 0
  sw a0, -308(fp)
.L1380:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1384
  li a0, 1
  sw a0, -312(fp)
  j .L1383
.L1384:
  li a0, 0
  sw a0, -312(fp)
.L1383:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1387
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1387:
  beqz a0, .L1386
  li a0, 1
  sw a0, -300(fp)
  j .L1385
.L1386:
  li a0, 0
  sw a0, -300(fp)
.L1385:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1390
  sw a0, -308(fp)
  lw a0, -180(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1390:
  beqz a0, .L1389
  li a0, 1
  sw a0, -304(fp)
  j .L1388
.L1389:
  li a0, 0
  sw a0, -304(fp)
.L1388:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1393
  sw a0, -312(fp)
  lw a0, -180(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1393:
  beqz a0, .L1392
  li a0, 1
  sw a0, -308(fp)
  j .L1391
.L1392:
  li a0, 0
  sw a0, -308(fp)
.L1391:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1395
  li a0, 1
  sw a0, -312(fp)
  j .L1394
.L1395:
  li a0, 0
  sw a0, -312(fp)
.L1394:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1398
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1398:
  beqz a0, .L1397
  li a0, 1
  sw a0, -244(fp)
  j .L1396
.L1397:
  li a0, 0
  sw a0, -244(fp)
.L1396:
  lw a0, -56(fp)
  snez a0, a0
  beqz a0, .L1401
  sw a0, -308(fp)
  lw a0, -120(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1401:
  beqz a0, .L1400
  li a0, 1
  sw a0, -304(fp)
  j .L1399
.L1400:
  li a0, 0
  sw a0, -304(fp)
.L1399:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1404
  sw a0, -312(fp)
  lw a0, -180(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1404:
  beqz a0, .L1403
  li a0, 1
  sw a0, -308(fp)
  j .L1402
.L1403:
  li a0, 0
  sw a0, -308(fp)
.L1402:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L1407
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L1407:
  beqz a0, .L1406
  li a0, 1
  sw a0, -184(fp)
  j .L1405
.L1406:
  li a0, 0
  sw a0, -184(fp)
.L1405:
  lw a0, -60(fp)
  snez a0, a0
  bnez a0, .L1410
  sw a0, -308(fp)
  lw a0, -124(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1410:
  beqz a0, .L1409
  li a0, 1
  sw a0, -304(fp)
  j .L1408
.L1409:
  li a0, 0
  sw a0, -304(fp)
.L1408:
  lw a0, -60(fp)
  snez a0, a0
  beqz a0, .L1413
  sw a0, -312(fp)
  lw a0, -124(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1413:
  beqz a0, .L1412
  li a0, 1
  sw a0, -308(fp)
  j .L1411
.L1412:
  li a0, 0
  sw a0, -308(fp)
.L1411:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1415
  li a0, 1
  sw a0, -312(fp)
  j .L1414
.L1415:
  li a0, 0
  sw a0, -312(fp)
.L1414:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1418
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1418:
  beqz a0, .L1417
  li a0, 1
  sw a0, -300(fp)
  j .L1416
.L1417:
  li a0, 0
  sw a0, -300(fp)
.L1416:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1421
  sw a0, -308(fp)
  lw a0, -184(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1421:
  beqz a0, .L1420
  li a0, 1
  sw a0, -304(fp)
  j .L1419
.L1420:
  li a0, 0
  sw a0, -304(fp)
.L1419:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1424
  sw a0, -312(fp)
  lw a0, -184(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1424:
  beqz a0, .L1423
  li a0, 1
  sw a0, -308(fp)
  j .L1422
.L1423:
  li a0, 0
  sw a0, -308(fp)
.L1422:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1426
  li a0, 1
  sw a0, -312(fp)
  j .L1425
.L1426:
  li a0, 0
  sw a0, -312(fp)
.L1425:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1429
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1429:
  beqz a0, .L1428
  li a0, 1
  sw a0, -248(fp)
  j .L1427
.L1428:
  li a0, 0
  sw a0, -248(fp)
.L1427:
  lw a0, -60(fp)
  snez a0, a0
  beqz a0, .L1432
  sw a0, -308(fp)
  lw a0, -124(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1432:
  beqz a0, .L1431
  li a0, 1
  sw a0, -304(fp)
  j .L1430
.L1431:
  li a0, 0
  sw a0, -304(fp)
.L1430:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1435
  sw a0, -312(fp)
  lw a0, -184(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1435:
  beqz a0, .L1434
  li a0, 1
  sw a0, -308(fp)
  j .L1433
.L1434:
  li a0, 0
  sw a0, -308(fp)
.L1433:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L1438
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L1438:
  beqz a0, .L1437
  li a0, 1
  sw a0, -188(fp)
  j .L1436
.L1437:
  li a0, 0
  sw a0, -188(fp)
.L1436:
  lw a0, -64(fp)
  snez a0, a0
  bnez a0, .L1441
  sw a0, -308(fp)
  lw a0, -128(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1441:
  beqz a0, .L1440
  li a0, 1
  sw a0, -304(fp)
  j .L1439
.L1440:
  li a0, 0
  sw a0, -304(fp)
.L1439:
  lw a0, -64(fp)
  snez a0, a0
  beqz a0, .L1444
  sw a0, -312(fp)
  lw a0, -128(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1444:
  beqz a0, .L1443
  li a0, 1
  sw a0, -308(fp)
  j .L1442
.L1443:
  li a0, 0
  sw a0, -308(fp)
.L1442:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1446
  li a0, 1
  sw a0, -312(fp)
  j .L1445
.L1446:
  li a0, 0
  sw a0, -312(fp)
.L1445:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1449
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1449:
  beqz a0, .L1448
  li a0, 1
  sw a0, -300(fp)
  j .L1447
.L1448:
  li a0, 0
  sw a0, -300(fp)
.L1447:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1452
  sw a0, -308(fp)
  lw a0, -188(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1452:
  beqz a0, .L1451
  li a0, 1
  sw a0, -304(fp)
  j .L1450
.L1451:
  li a0, 0
  sw a0, -304(fp)
.L1450:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1455
  sw a0, -312(fp)
  lw a0, -188(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1455:
  beqz a0, .L1454
  li a0, 1
  sw a0, -308(fp)
  j .L1453
.L1454:
  li a0, 0
  sw a0, -308(fp)
.L1453:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1457
  li a0, 1
  sw a0, -312(fp)
  j .L1456
.L1457:
  li a0, 0
  sw a0, -312(fp)
.L1456:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1460
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1460:
  beqz a0, .L1459
  li a0, 1
  sw a0, -252(fp)
  j .L1458
.L1459:
  li a0, 0
  sw a0, -252(fp)
.L1458:
  lw a0, -64(fp)
  snez a0, a0
  beqz a0, .L1463
  sw a0, -308(fp)
  lw a0, -128(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1463:
  beqz a0, .L1462
  li a0, 1
  sw a0, -304(fp)
  j .L1461
.L1462:
  li a0, 0
  sw a0, -304(fp)
.L1461:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1466
  sw a0, -312(fp)
  lw a0, -188(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1466:
  beqz a0, .L1465
  li a0, 1
  sw a0, -308(fp)
  j .L1464
.L1465:
  li a0, 0
  sw a0, -308(fp)
.L1464:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L1469
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L1469:
  beqz a0, .L1468
  li a0, 1
  sw a0, -192(fp)
  j .L1467
.L1468:
  li a0, 0
  sw a0, -192(fp)
.L1467:
  lw a0, -68(fp)
  snez a0, a0
  bnez a0, .L1472
  sw a0, -308(fp)
  lw a0, -132(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1472:
  beqz a0, .L1471
  li a0, 1
  sw a0, -304(fp)
  j .L1470
.L1471:
  li a0, 0
  sw a0, -304(fp)
.L1470:
  lw a0, -68(fp)
  snez a0, a0
  beqz a0, .L1475
  sw a0, -312(fp)
  lw a0, -132(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1475:
  beqz a0, .L1474
  li a0, 1
  sw a0, -308(fp)
  j .L1473
.L1474:
  li a0, 0
  sw a0, -308(fp)
.L1473:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1477
  li a0, 1
  sw a0, -312(fp)
  j .L1476
.L1477:
  li a0, 0
  sw a0, -312(fp)
.L1476:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1480
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1480:
  beqz a0, .L1479
  li a0, 1
  sw a0, -300(fp)
  j .L1478
.L1479:
  li a0, 0
  sw a0, -300(fp)
.L1478:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1483
  sw a0, -308(fp)
  lw a0, -192(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1483:
  beqz a0, .L1482
  li a0, 1
  sw a0, -304(fp)
  j .L1481
.L1482:
  li a0, 0
  sw a0, -304(fp)
.L1481:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1486
  sw a0, -312(fp)
  lw a0, -192(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1486:
  beqz a0, .L1485
  li a0, 1
  sw a0, -308(fp)
  j .L1484
.L1485:
  li a0, 0
  sw a0, -308(fp)
.L1484:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1488
  li a0, 1
  sw a0, -312(fp)
  j .L1487
.L1488:
  li a0, 0
  sw a0, -312(fp)
.L1487:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1491
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1491:
  beqz a0, .L1490
  li a0, 1
  sw a0, -256(fp)
  j .L1489
.L1490:
  li a0, 0
  sw a0, -256(fp)
.L1489:
  lw a0, -68(fp)
  snez a0, a0
  beqz a0, .L1494
  sw a0, -308(fp)
  lw a0, -132(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1494:
  beqz a0, .L1493
  li a0, 1
  sw a0, -304(fp)
  j .L1492
.L1493:
  li a0, 0
  sw a0, -304(fp)
.L1492:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1497
  sw a0, -312(fp)
  lw a0, -192(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1497:
  beqz a0, .L1496
  li a0, 1
  sw a0, -308(fp)
  j .L1495
.L1496:
  li a0, 0
  sw a0, -308(fp)
.L1495:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L1500
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L1500:
  beqz a0, .L1499
  li a0, 1
  sw a0, -196(fp)
  j .L1498
.L1499:
  li a0, 0
  sw a0, -196(fp)
.L1498:
  lw a0, -72(fp)
  snez a0, a0
  bnez a0, .L1503
  sw a0, -308(fp)
  lw a0, -136(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1503:
  beqz a0, .L1502
  li a0, 1
  sw a0, -304(fp)
  j .L1501
.L1502:
  li a0, 0
  sw a0, -304(fp)
.L1501:
  lw a0, -72(fp)
  snez a0, a0
  beqz a0, .L1506
  sw a0, -312(fp)
  lw a0, -136(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1506:
  beqz a0, .L1505
  li a0, 1
  sw a0, -308(fp)
  j .L1504
.L1505:
  li a0, 0
  sw a0, -308(fp)
.L1504:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1508
  li a0, 1
  sw a0, -312(fp)
  j .L1507
.L1508:
  li a0, 0
  sw a0, -312(fp)
.L1507:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1511
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1511:
  beqz a0, .L1510
  li a0, 1
  sw a0, -300(fp)
  j .L1509
.L1510:
  li a0, 0
  sw a0, -300(fp)
.L1509:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1514
  sw a0, -308(fp)
  lw a0, -196(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1514:
  beqz a0, .L1513
  li a0, 1
  sw a0, -304(fp)
  j .L1512
.L1513:
  li a0, 0
  sw a0, -304(fp)
.L1512:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1517
  sw a0, -312(fp)
  lw a0, -196(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1517:
  beqz a0, .L1516
  li a0, 1
  sw a0, -308(fp)
  j .L1515
.L1516:
  li a0, 0
  sw a0, -308(fp)
.L1515:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1519
  li a0, 1
  sw a0, -312(fp)
  j .L1518
.L1519:
  li a0, 0
  sw a0, -312(fp)
.L1518:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1522
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1522:
  beqz a0, .L1521
  li a0, 1
  sw a0, -260(fp)
  j .L1520
.L1521:
  li a0, 0
  sw a0, -260(fp)
.L1520:
  lw a0, -72(fp)
  snez a0, a0
  beqz a0, .L1525
  sw a0, -308(fp)
  lw a0, -136(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1525:
  beqz a0, .L1524
  li a0, 1
  sw a0, -304(fp)
  j .L1523
.L1524:
  li a0, 0
  sw a0, -304(fp)
.L1523:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1528
  sw a0, -312(fp)
  lw a0, -196(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1528:
  beqz a0, .L1527
  li a0, 1
  sw a0, -308(fp)
  j .L1526
.L1527:
  li a0, 0
  sw a0, -308(fp)
.L1526:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L1531
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L1531:
  beqz a0, .L1530
  li a0, 1
  sw a0, -200(fp)
  j .L1529
.L1530:
  li a0, 0
  sw a0, -200(fp)
.L1529:
  lw a0, -76(fp)
  snez a0, a0
  bnez a0, .L1534
  sw a0, -308(fp)
  lw a0, -140(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1534:
  beqz a0, .L1533
  li a0, 1
  sw a0, -304(fp)
  j .L1532
.L1533:
  li a0, 0
  sw a0, -304(fp)
.L1532:
  lw a0, -76(fp)
  snez a0, a0
  beqz a0, .L1537
  sw a0, -312(fp)
  lw a0, -140(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1537:
  beqz a0, .L1536
  li a0, 1
  sw a0, -308(fp)
  j .L1535
.L1536:
  li a0, 0
  sw a0, -308(fp)
.L1535:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1539
  li a0, 1
  sw a0, -312(fp)
  j .L1538
.L1539:
  li a0, 0
  sw a0, -312(fp)
.L1538:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1542
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1542:
  beqz a0, .L1541
  li a0, 1
  sw a0, -300(fp)
  j .L1540
.L1541:
  li a0, 0
  sw a0, -300(fp)
.L1540:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1545
  sw a0, -308(fp)
  lw a0, -200(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1545:
  beqz a0, .L1544
  li a0, 1
  sw a0, -304(fp)
  j .L1543
.L1544:
  li a0, 0
  sw a0, -304(fp)
.L1543:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1548
  sw a0, -312(fp)
  lw a0, -200(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1548:
  beqz a0, .L1547
  li a0, 1
  sw a0, -308(fp)
  j .L1546
.L1547:
  li a0, 0
  sw a0, -308(fp)
.L1546:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1550
  li a0, 1
  sw a0, -312(fp)
  j .L1549
.L1550:
  li a0, 0
  sw a0, -312(fp)
.L1549:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1553
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1553:
  beqz a0, .L1552
  li a0, 1
  sw a0, -264(fp)
  j .L1551
.L1552:
  li a0, 0
  sw a0, -264(fp)
.L1551:
  lw a0, -76(fp)
  snez a0, a0
  beqz a0, .L1556
  sw a0, -308(fp)
  lw a0, -140(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1556:
  beqz a0, .L1555
  li a0, 1
  sw a0, -304(fp)
  j .L1554
.L1555:
  li a0, 0
  sw a0, -304(fp)
.L1554:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1559
  sw a0, -312(fp)
  lw a0, -200(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1559:
  beqz a0, .L1558
  li a0, 1
  sw a0, -308(fp)
  j .L1557
.L1558:
  li a0, 0
  sw a0, -308(fp)
.L1557:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L1562
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L1562:
  beqz a0, .L1561
  li a0, 1
  sw a0, -204(fp)
  j .L1560
.L1561:
  li a0, 0
  sw a0, -204(fp)
.L1560:
  lw a0, -80(fp)
  snez a0, a0
  bnez a0, .L1565
  sw a0, -308(fp)
  lw a0, -144(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1565:
  beqz a0, .L1564
  li a0, 1
  sw a0, -304(fp)
  j .L1563
.L1564:
  li a0, 0
  sw a0, -304(fp)
.L1563:
  lw a0, -80(fp)
  snez a0, a0
  beqz a0, .L1568
  sw a0, -312(fp)
  lw a0, -144(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1568:
  beqz a0, .L1567
  li a0, 1
  sw a0, -308(fp)
  j .L1566
.L1567:
  li a0, 0
  sw a0, -308(fp)
.L1566:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1570
  li a0, 1
  sw a0, -312(fp)
  j .L1569
.L1570:
  li a0, 0
  sw a0, -312(fp)
.L1569:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1573
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1573:
  beqz a0, .L1572
  li a0, 1
  sw a0, -300(fp)
  j .L1571
.L1572:
  li a0, 0
  sw a0, -300(fp)
.L1571:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1576
  sw a0, -308(fp)
  lw a0, -204(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1576:
  beqz a0, .L1575
  li a0, 1
  sw a0, -304(fp)
  j .L1574
.L1575:
  li a0, 0
  sw a0, -304(fp)
.L1574:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1579
  sw a0, -312(fp)
  lw a0, -204(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1579:
  beqz a0, .L1578
  li a0, 1
  sw a0, -308(fp)
  j .L1577
.L1578:
  li a0, 0
  sw a0, -308(fp)
.L1577:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1581
  li a0, 1
  sw a0, -312(fp)
  j .L1580
.L1581:
  li a0, 0
  sw a0, -312(fp)
.L1580:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1584
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1584:
  beqz a0, .L1583
  li a0, 1
  sw a0, -268(fp)
  j .L1582
.L1583:
  li a0, 0
  sw a0, -268(fp)
.L1582:
  lw a0, -80(fp)
  snez a0, a0
  beqz a0, .L1587
  sw a0, -308(fp)
  lw a0, -144(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1587:
  beqz a0, .L1586
  li a0, 1
  sw a0, -304(fp)
  j .L1585
.L1586:
  li a0, 0
  sw a0, -304(fp)
.L1585:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1590
  sw a0, -312(fp)
  lw a0, -204(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1590:
  beqz a0, .L1589
  li a0, 1
  sw a0, -308(fp)
  j .L1588
.L1589:
  li a0, 0
  sw a0, -308(fp)
.L1588:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L1593
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L1593:
  beqz a0, .L1592
  li a0, 1
  sw a0, -208(fp)
  j .L1591
.L1592:
  li a0, 0
  sw a0, -208(fp)
.L1591:
  lw a0, -84(fp)
  snez a0, a0
  bnez a0, .L1596
  sw a0, -308(fp)
  lw a0, -148(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1596:
  beqz a0, .L1595
  li a0, 1
  sw a0, -304(fp)
  j .L1594
.L1595:
  li a0, 0
  sw a0, -304(fp)
.L1594:
  lw a0, -84(fp)
  snez a0, a0
  beqz a0, .L1599
  sw a0, -312(fp)
  lw a0, -148(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1599:
  beqz a0, .L1598
  li a0, 1
  sw a0, -308(fp)
  j .L1597
.L1598:
  li a0, 0
  sw a0, -308(fp)
.L1597:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1601
  li a0, 1
  sw a0, -312(fp)
  j .L1600
.L1601:
  li a0, 0
  sw a0, -312(fp)
.L1600:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1604
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1604:
  beqz a0, .L1603
  li a0, 1
  sw a0, -300(fp)
  j .L1602
.L1603:
  li a0, 0
  sw a0, -300(fp)
.L1602:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1607
  sw a0, -308(fp)
  lw a0, -208(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1607:
  beqz a0, .L1606
  li a0, 1
  sw a0, -304(fp)
  j .L1605
.L1606:
  li a0, 0
  sw a0, -304(fp)
.L1605:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1610
  sw a0, -312(fp)
  lw a0, -208(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1610:
  beqz a0, .L1609
  li a0, 1
  sw a0, -308(fp)
  j .L1608
.L1609:
  li a0, 0
  sw a0, -308(fp)
.L1608:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1612
  li a0, 1
  sw a0, -312(fp)
  j .L1611
.L1612:
  li a0, 0
  sw a0, -312(fp)
.L1611:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1615
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1615:
  beqz a0, .L1614
  li a0, 1
  sw a0, -272(fp)
  j .L1613
.L1614:
  li a0, 0
  sw a0, -272(fp)
.L1613:
  lw a0, -84(fp)
  snez a0, a0
  beqz a0, .L1618
  sw a0, -308(fp)
  lw a0, -148(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1618:
  beqz a0, .L1617
  li a0, 1
  sw a0, -304(fp)
  j .L1616
.L1617:
  li a0, 0
  sw a0, -304(fp)
.L1616:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1621
  sw a0, -312(fp)
  lw a0, -208(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1621:
  beqz a0, .L1620
  li a0, 1
  sw a0, -308(fp)
  j .L1619
.L1620:
  li a0, 0
  sw a0, -308(fp)
.L1619:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L1624
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L1624:
  beqz a0, .L1623
  li a0, 1
  sw a0, -212(fp)
  j .L1622
.L1623:
  li a0, 0
  sw a0, -212(fp)
.L1622:
  lw a0, -88(fp)
  snez a0, a0
  bnez a0, .L1627
  sw a0, -308(fp)
  lw a0, -152(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1627:
  beqz a0, .L1626
  li a0, 1
  sw a0, -304(fp)
  j .L1625
.L1626:
  li a0, 0
  sw a0, -304(fp)
.L1625:
  lw a0, -88(fp)
  snez a0, a0
  beqz a0, .L1630
  sw a0, -312(fp)
  lw a0, -152(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1630:
  beqz a0, .L1629
  li a0, 1
  sw a0, -308(fp)
  j .L1628
.L1629:
  li a0, 0
  sw a0, -308(fp)
.L1628:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1632
  li a0, 1
  sw a0, -312(fp)
  j .L1631
.L1632:
  li a0, 0
  sw a0, -312(fp)
.L1631:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1635
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1635:
  beqz a0, .L1634
  li a0, 1
  sw a0, -300(fp)
  j .L1633
.L1634:
  li a0, 0
  sw a0, -300(fp)
.L1633:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1638
  sw a0, -308(fp)
  lw a0, -212(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1638:
  beqz a0, .L1637
  li a0, 1
  sw a0, -304(fp)
  j .L1636
.L1637:
  li a0, 0
  sw a0, -304(fp)
.L1636:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1641
  sw a0, -312(fp)
  lw a0, -212(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1641:
  beqz a0, .L1640
  li a0, 1
  sw a0, -308(fp)
  j .L1639
.L1640:
  li a0, 0
  sw a0, -308(fp)
.L1639:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1643
  li a0, 1
  sw a0, -312(fp)
  j .L1642
.L1643:
  li a0, 0
  sw a0, -312(fp)
.L1642:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1646
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1646:
  beqz a0, .L1645
  li a0, 1
  sw a0, -276(fp)
  j .L1644
.L1645:
  li a0, 0
  sw a0, -276(fp)
.L1644:
  lw a0, -88(fp)
  snez a0, a0
  beqz a0, .L1649
  sw a0, -308(fp)
  lw a0, -152(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1649:
  beqz a0, .L1648
  li a0, 1
  sw a0, -304(fp)
  j .L1647
.L1648:
  li a0, 0
  sw a0, -304(fp)
.L1647:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1652
  sw a0, -312(fp)
  lw a0, -212(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1652:
  beqz a0, .L1651
  li a0, 1
  sw a0, -308(fp)
  j .L1650
.L1651:
  li a0, 0
  sw a0, -308(fp)
.L1650:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L1655
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L1655:
  beqz a0, .L1654
  li a0, 1
  sw a0, -216(fp)
  j .L1653
.L1654:
  li a0, 0
  sw a0, -216(fp)
.L1653:
  lw a0, -92(fp)
  snez a0, a0
  bnez a0, .L1658
  sw a0, -308(fp)
  lw a0, -156(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1658:
  beqz a0, .L1657
  li a0, 1
  sw a0, -304(fp)
  j .L1656
.L1657:
  li a0, 0
  sw a0, -304(fp)
.L1656:
  lw a0, -92(fp)
  snez a0, a0
  beqz a0, .L1661
  sw a0, -312(fp)
  lw a0, -156(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1661:
  beqz a0, .L1660
  li a0, 1
  sw a0, -308(fp)
  j .L1659
.L1660:
  li a0, 0
  sw a0, -308(fp)
.L1659:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1663
  li a0, 1
  sw a0, -312(fp)
  j .L1662
.L1663:
  li a0, 0
  sw a0, -312(fp)
.L1662:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1666
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1666:
  beqz a0, .L1665
  li a0, 1
  sw a0, -300(fp)
  j .L1664
.L1665:
  li a0, 0
  sw a0, -300(fp)
.L1664:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1669
  sw a0, -308(fp)
  lw a0, -216(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1669:
  beqz a0, .L1668
  li a0, 1
  sw a0, -304(fp)
  j .L1667
.L1668:
  li a0, 0
  sw a0, -304(fp)
.L1667:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1672
  sw a0, -312(fp)
  lw a0, -216(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1672:
  beqz a0, .L1671
  li a0, 1
  sw a0, -308(fp)
  j .L1670
.L1671:
  li a0, 0
  sw a0, -308(fp)
.L1670:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1674
  li a0, 1
  sw a0, -312(fp)
  j .L1673
.L1674:
  li a0, 0
  sw a0, -312(fp)
.L1673:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1677
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1677:
  beqz a0, .L1676
  li a0, 1
  sw a0, -280(fp)
  j .L1675
.L1676:
  li a0, 0
  sw a0, -280(fp)
.L1675:
  lw a0, -92(fp)
  snez a0, a0
  beqz a0, .L1680
  sw a0, -308(fp)
  lw a0, -156(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1680:
  beqz a0, .L1679
  li a0, 1
  sw a0, -304(fp)
  j .L1678
.L1679:
  li a0, 0
  sw a0, -304(fp)
.L1678:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1683
  sw a0, -312(fp)
  lw a0, -216(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1683:
  beqz a0, .L1682
  li a0, 1
  sw a0, -308(fp)
  j .L1681
.L1682:
  li a0, 0
  sw a0, -308(fp)
.L1681:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L1686
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L1686:
  beqz a0, .L1685
  li a0, 1
  sw a0, -220(fp)
  j .L1684
.L1685:
  li a0, 0
  sw a0, -220(fp)
.L1684:
  lw a0, -96(fp)
  snez a0, a0
  bnez a0, .L1689
  sw a0, -308(fp)
  lw a0, -160(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1689:
  beqz a0, .L1688
  li a0, 1
  sw a0, -304(fp)
  j .L1687
.L1688:
  li a0, 0
  sw a0, -304(fp)
.L1687:
  lw a0, -96(fp)
  snez a0, a0
  beqz a0, .L1692
  sw a0, -312(fp)
  lw a0, -160(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1692:
  beqz a0, .L1691
  li a0, 1
  sw a0, -308(fp)
  j .L1690
.L1691:
  li a0, 0
  sw a0, -308(fp)
.L1690:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1694
  li a0, 1
  sw a0, -312(fp)
  j .L1693
.L1694:
  li a0, 0
  sw a0, -312(fp)
.L1693:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1697
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1697:
  beqz a0, .L1696
  li a0, 1
  sw a0, -300(fp)
  j .L1695
.L1696:
  li a0, 0
  sw a0, -300(fp)
.L1695:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1700
  sw a0, -308(fp)
  lw a0, -220(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1700:
  beqz a0, .L1699
  li a0, 1
  sw a0, -304(fp)
  j .L1698
.L1699:
  li a0, 0
  sw a0, -304(fp)
.L1698:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1703
  sw a0, -312(fp)
  lw a0, -220(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1703:
  beqz a0, .L1702
  li a0, 1
  sw a0, -308(fp)
  j .L1701
.L1702:
  li a0, 0
  sw a0, -308(fp)
.L1701:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1705
  li a0, 1
  sw a0, -312(fp)
  j .L1704
.L1705:
  li a0, 0
  sw a0, -312(fp)
.L1704:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1708
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1708:
  beqz a0, .L1707
  li a0, 1
  sw a0, -284(fp)
  j .L1706
.L1707:
  li a0, 0
  sw a0, -284(fp)
.L1706:
  lw a0, -96(fp)
  snez a0, a0
  beqz a0, .L1711
  sw a0, -308(fp)
  lw a0, -160(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1711:
  beqz a0, .L1710
  li a0, 1
  sw a0, -304(fp)
  j .L1709
.L1710:
  li a0, 0
  sw a0, -304(fp)
.L1709:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1714
  sw a0, -312(fp)
  lw a0, -220(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1714:
  beqz a0, .L1713
  li a0, 1
  sw a0, -308(fp)
  j .L1712
.L1713:
  li a0, 0
  sw a0, -308(fp)
.L1712:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L1717
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L1717:
  beqz a0, .L1716
  li a0, 1
  sw a0, -224(fp)
  j .L1715
.L1716:
  li a0, 0
  sw a0, -224(fp)
.L1715:
  lw a0, -100(fp)
  snez a0, a0
  bnez a0, .L1720
  sw a0, -308(fp)
  lw a0, -164(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1720:
  beqz a0, .L1719
  li a0, 1
  sw a0, -304(fp)
  j .L1718
.L1719:
  li a0, 0
  sw a0, -304(fp)
.L1718:
  lw a0, -100(fp)
  snez a0, a0
  beqz a0, .L1723
  sw a0, -312(fp)
  lw a0, -164(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1723:
  beqz a0, .L1722
  li a0, 1
  sw a0, -308(fp)
  j .L1721
.L1722:
  li a0, 0
  sw a0, -308(fp)
.L1721:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1725
  li a0, 1
  sw a0, -312(fp)
  j .L1724
.L1725:
  li a0, 0
  sw a0, -312(fp)
.L1724:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1728
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1728:
  beqz a0, .L1727
  li a0, 1
  sw a0, -300(fp)
  j .L1726
.L1727:
  li a0, 0
  sw a0, -300(fp)
.L1726:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1731
  sw a0, -308(fp)
  lw a0, -224(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1731:
  beqz a0, .L1730
  li a0, 1
  sw a0, -304(fp)
  j .L1729
.L1730:
  li a0, 0
  sw a0, -304(fp)
.L1729:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1734
  sw a0, -312(fp)
  lw a0, -224(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1734:
  beqz a0, .L1733
  li a0, 1
  sw a0, -308(fp)
  j .L1732
.L1733:
  li a0, 0
  sw a0, -308(fp)
.L1732:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1736
  li a0, 1
  sw a0, -312(fp)
  j .L1735
.L1736:
  li a0, 0
  sw a0, -312(fp)
.L1735:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1739
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1739:
  beqz a0, .L1738
  li a0, 1
  sw a0, -288(fp)
  j .L1737
.L1738:
  li a0, 0
  sw a0, -288(fp)
.L1737:
  lw a0, -100(fp)
  snez a0, a0
  beqz a0, .L1742
  sw a0, -308(fp)
  lw a0, -164(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1742:
  beqz a0, .L1741
  li a0, 1
  sw a0, -304(fp)
  j .L1740
.L1741:
  li a0, 0
  sw a0, -304(fp)
.L1740:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1745
  sw a0, -312(fp)
  lw a0, -224(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1745:
  beqz a0, .L1744
  li a0, 1
  sw a0, -308(fp)
  j .L1743
.L1744:
  li a0, 0
  sw a0, -308(fp)
.L1743:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L1748
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L1748:
  beqz a0, .L1747
  li a0, 1
  sw a0, -228(fp)
  j .L1746
.L1747:
  li a0, 0
  sw a0, -228(fp)
.L1746:
  lw a0, -104(fp)
  snez a0, a0
  bnez a0, .L1751
  sw a0, -308(fp)
  lw a0, -168(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1751:
  beqz a0, .L1750
  li a0, 1
  sw a0, -304(fp)
  j .L1749
.L1750:
  li a0, 0
  sw a0, -304(fp)
.L1749:
  lw a0, -104(fp)
  snez a0, a0
  beqz a0, .L1754
  sw a0, -312(fp)
  lw a0, -168(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1754:
  beqz a0, .L1753
  li a0, 1
  sw a0, -308(fp)
  j .L1752
.L1753:
  li a0, 0
  sw a0, -308(fp)
.L1752:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1756
  li a0, 1
  sw a0, -312(fp)
  j .L1755
.L1756:
  li a0, 0
  sw a0, -312(fp)
.L1755:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1759
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1759:
  beqz a0, .L1758
  li a0, 1
  sw a0, -300(fp)
  j .L1757
.L1758:
  li a0, 0
  sw a0, -300(fp)
.L1757:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1762
  sw a0, -308(fp)
  lw a0, -228(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1762:
  beqz a0, .L1761
  li a0, 1
  sw a0, -304(fp)
  j .L1760
.L1761:
  li a0, 0
  sw a0, -304(fp)
.L1760:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1765
  sw a0, -312(fp)
  lw a0, -228(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1765:
  beqz a0, .L1764
  li a0, 1
  sw a0, -308(fp)
  j .L1763
.L1764:
  li a0, 0
  sw a0, -308(fp)
.L1763:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1767
  li a0, 1
  sw a0, -312(fp)
  j .L1766
.L1767:
  li a0, 0
  sw a0, -312(fp)
.L1766:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1770
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1770:
  beqz a0, .L1769
  li a0, 1
  sw a0, -292(fp)
  j .L1768
.L1769:
  li a0, 0
  sw a0, -292(fp)
.L1768:
  lw a0, -104(fp)
  snez a0, a0
  beqz a0, .L1773
  sw a0, -308(fp)
  lw a0, -168(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1773:
  beqz a0, .L1772
  li a0, 1
  sw a0, -304(fp)
  j .L1771
.L1772:
  li a0, 0
  sw a0, -304(fp)
.L1771:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1776
  sw a0, -312(fp)
  lw a0, -228(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1776:
  beqz a0, .L1775
  li a0, 1
  sw a0, -308(fp)
  j .L1774
.L1775:
  li a0, 0
  sw a0, -308(fp)
.L1774:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L1779
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L1779:
  beqz a0, .L1778
  li a0, 1
  sw a0, -232(fp)
  j .L1777
.L1778:
  li a0, 0
  sw a0, -232(fp)
.L1777:
  lw a0, -108(fp)
  snez a0, a0
  bnez a0, .L1782
  sw a0, -308(fp)
  lw a0, -172(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1782:
  beqz a0, .L1781
  li a0, 1
  sw a0, -304(fp)
  j .L1780
.L1781:
  li a0, 0
  sw a0, -304(fp)
.L1780:
  lw a0, -108(fp)
  snez a0, a0
  beqz a0, .L1785
  sw a0, -312(fp)
  lw a0, -172(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1785:
  beqz a0, .L1784
  li a0, 1
  sw a0, -308(fp)
  j .L1783
.L1784:
  li a0, 0
  sw a0, -308(fp)
.L1783:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1787
  li a0, 1
  sw a0, -312(fp)
  j .L1786
.L1787:
  li a0, 0
  sw a0, -312(fp)
.L1786:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1790
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1790:
  beqz a0, .L1789
  li a0, 1
  sw a0, -300(fp)
  j .L1788
.L1789:
  li a0, 0
  sw a0, -300(fp)
.L1788:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1793
  sw a0, -308(fp)
  lw a0, -232(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1793:
  beqz a0, .L1792
  li a0, 1
  sw a0, -304(fp)
  j .L1791
.L1792:
  li a0, 0
  sw a0, -304(fp)
.L1791:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1796
  sw a0, -312(fp)
  lw a0, -232(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1796:
  beqz a0, .L1795
  li a0, 1
  sw a0, -308(fp)
  j .L1794
.L1795:
  li a0, 0
  sw a0, -308(fp)
.L1794:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L1798
  li a0, 1
  sw a0, -312(fp)
  j .L1797
.L1798:
  li a0, 0
  sw a0, -312(fp)
.L1797:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L1801
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L1801:
  beqz a0, .L1800
  li a0, 1
  sw a0, -296(fp)
  j .L1799
.L1800:
  li a0, 0
  sw a0, -296(fp)
.L1799:
  lw a0, -108(fp)
  snez a0, a0
  beqz a0, .L1804
  sw a0, -308(fp)
  lw a0, -172(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1804:
  beqz a0, .L1803
  li a0, 1
  sw a0, -304(fp)
  j .L1802
.L1803:
  li a0, 0
  sw a0, -304(fp)
.L1802:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1807
  sw a0, -312(fp)
  lw a0, -232(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1807:
  beqz a0, .L1806
  li a0, 1
  sw a0, -308(fp)
  j .L1805
.L1806:
  li a0, 0
  sw a0, -308(fp)
.L1805:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L1810
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L1810:
  beqz a0, .L1809
  li a0, 1
  sw a0, -44(fp)
  j .L1808
.L1809:
  li a0, 0
  sw a0, -44(fp)
.L1808:
  li a0, 0
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -296(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -292(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -288(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -284(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -280(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -276(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -272(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -268(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -264(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -260(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -256(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -252(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -248(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -244(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -240(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -236(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -36(fp)
  lw a0, 4(fp)
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -44(fp)
  lw a0, -44(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1812
  lw a0, -44(fp)
  negw a0, a0
  sw a0, -44(fp)
  j .L1811
.L1812:
.L1811:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -48(fp)
  lw a0, -48(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1814
  lw a0, -48(fp)
  negw a0, a0
  sw a0, -48(fp)
  j .L1813
.L1814:
.L1813:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -52(fp)
  lw a0, -52(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1816
  lw a0, -52(fp)
  negw a0, a0
  sw a0, -52(fp)
  j .L1815
.L1816:
.L1815:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -56(fp)
  lw a0, -56(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1818
  lw a0, -56(fp)
  negw a0, a0
  sw a0, -56(fp)
  j .L1817
.L1818:
.L1817:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -60(fp)
  lw a0, -60(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1820
  lw a0, -60(fp)
  negw a0, a0
  sw a0, -60(fp)
  j .L1819
.L1820:
.L1819:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -64(fp)
  lw a0, -64(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1822
  lw a0, -64(fp)
  negw a0, a0
  sw a0, -64(fp)
  j .L1821
.L1822:
.L1821:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -68(fp)
  lw a0, -68(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1824
  lw a0, -68(fp)
  negw a0, a0
  sw a0, -68(fp)
  j .L1823
.L1824:
.L1823:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -72(fp)
  lw a0, -72(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1826
  lw a0, -72(fp)
  negw a0, a0
  sw a0, -72(fp)
  j .L1825
.L1826:
.L1825:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -76(fp)
  lw a0, -76(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1828
  lw a0, -76(fp)
  negw a0, a0
  sw a0, -76(fp)
  j .L1827
.L1828:
.L1827:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -80(fp)
  lw a0, -80(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1830
  lw a0, -80(fp)
  negw a0, a0
  sw a0, -80(fp)
  j .L1829
.L1830:
.L1829:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -84(fp)
  lw a0, -84(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1832
  lw a0, -84(fp)
  negw a0, a0
  sw a0, -84(fp)
  j .L1831
.L1832:
.L1831:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -88(fp)
  lw a0, -88(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1834
  lw a0, -88(fp)
  negw a0, a0
  sw a0, -88(fp)
  j .L1833
.L1834:
.L1833:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -92(fp)
  lw a0, -92(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1836
  lw a0, -92(fp)
  negw a0, a0
  sw a0, -92(fp)
  j .L1835
.L1836:
.L1835:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -96(fp)
  lw a0, -96(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1838
  lw a0, -96(fp)
  negw a0, a0
  sw a0, -96(fp)
  j .L1837
.L1838:
.L1837:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1840
  lw a0, -100(fp)
  negw a0, a0
  sw a0, -100(fp)
  j .L1839
.L1840:
.L1839:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  remw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -112(fp)
  li a0, 0
  lw t0, -112(fp)
  slt a0, t0, a0
  beqz a0, .L1842
  lw a0, -104(fp)
  negw a0, a0
  sw a0, -104(fp)
  j .L1841
.L1842:
.L1841:
  lw a0, -108(fp)
  sw a0, -112(fp)
  li a0, 2
  lw t0, -112(fp)
  divw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -36(fp)
  sw a0, -172(fp)
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  remw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -176(fp)
  li a0, 0
  lw t0, -176(fp)
  slt a0, t0, a0
  beqz a0, .L1844
  lw a0, -108(fp)
  negw a0, a0
  sw a0, -108(fp)
  j .L1843
.L1844:
.L1843:
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  divw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  remw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -176(fp)
  li a0, 0
  lw t0, -176(fp)
  slt a0, t0, a0
  beqz a0, .L1846
  lw a0, -112(fp)
  negw a0, a0
  sw a0, -112(fp)
  j .L1845
.L1846:
.L1845:
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  divw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  remw a0, t0, a0
  sw a0, -116(fp)
  lw a0, -116(fp)
  sw a0, -176(fp)
  li a0, 0
  lw t0, -176(fp)
  slt a0, t0, a0
  beqz a0, .L1848
  lw a0, -116(fp)
  negw a0, a0
  sw a0, -116(fp)
  j .L1847
.L1848:
.L1847:
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  divw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  remw a0, t0, a0
  sw a0, -120(fp)
  lw a0, -120(fp)
  sw a0, -176(fp)
  li a0, 0
  lw t0, -176(fp)
  slt a0, t0, a0
  beqz a0, .L1850
  lw a0, -120(fp)
  negw a0, a0
  sw a0, -120(fp)
  j .L1849
.L1850:
.L1849:
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  divw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  remw a0, t0, a0
  sw a0, -124(fp)
  lw a0, -124(fp)
  sw a0, -176(fp)
  li a0, 0
  lw t0, -176(fp)
  slt a0, t0, a0
  beqz a0, .L1852
  lw a0, -124(fp)
  negw a0, a0
  sw a0, -124(fp)
  j .L1851
.L1852:
.L1851:
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  divw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  remw a0, t0, a0
  sw a0, -128(fp)
  lw a0, -128(fp)
  sw a0, -176(fp)
  li a0, 0
  lw t0, -176(fp)
  slt a0, t0, a0
  beqz a0, .L1854
  lw a0, -128(fp)
  negw a0, a0
  sw a0, -128(fp)
  j .L1853
.L1854:
.L1853:
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  divw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  remw a0, t0, a0
  sw a0, -132(fp)
  lw a0, -132(fp)
  sw a0, -176(fp)
  li a0, 0
  lw t0, -176(fp)
  slt a0, t0, a0
  beqz a0, .L1856
  lw a0, -132(fp)
  negw a0, a0
  sw a0, -132(fp)
  j .L1855
.L1856:
.L1855:
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  divw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  remw a0, t0, a0
  sw a0, -136(fp)
  lw a0, -136(fp)
  sw a0, -176(fp)
  li a0, 0
  lw t0, -176(fp)
  slt a0, t0, a0
  beqz a0, .L1858
  lw a0, -136(fp)
  negw a0, a0
  sw a0, -136(fp)
  j .L1857
.L1858:
.L1857:
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  divw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  remw a0, t0, a0
  sw a0, -140(fp)
  lw a0, -140(fp)
  sw a0, -176(fp)
  li a0, 0
  lw t0, -176(fp)
  slt a0, t0, a0
  beqz a0, .L1860
  lw a0, -140(fp)
  negw a0, a0
  sw a0, -140(fp)
  j .L1859
.L1860:
.L1859:
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  divw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  remw a0, t0, a0
  sw a0, -144(fp)
  lw a0, -144(fp)
  sw a0, -176(fp)
  li a0, 0
  lw t0, -176(fp)
  slt a0, t0, a0
  beqz a0, .L1862
  lw a0, -144(fp)
  negw a0, a0
  sw a0, -144(fp)
  j .L1861
.L1862:
.L1861:
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  divw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  remw a0, t0, a0
  sw a0, -148(fp)
  lw a0, -148(fp)
  sw a0, -176(fp)
  li a0, 0
  lw t0, -176(fp)
  slt a0, t0, a0
  beqz a0, .L1864
  lw a0, -148(fp)
  negw a0, a0
  sw a0, -148(fp)
  j .L1863
.L1864:
.L1863:
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  divw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  remw a0, t0, a0
  sw a0, -152(fp)
  lw a0, -152(fp)
  sw a0, -176(fp)
  li a0, 0
  lw t0, -176(fp)
  slt a0, t0, a0
  beqz a0, .L1866
  lw a0, -152(fp)
  negw a0, a0
  sw a0, -152(fp)
  j .L1865
.L1866:
.L1865:
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  divw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  remw a0, t0, a0
  sw a0, -156(fp)
  lw a0, -156(fp)
  sw a0, -176(fp)
  li a0, 0
  lw t0, -176(fp)
  slt a0, t0, a0
  beqz a0, .L1868
  lw a0, -156(fp)
  negw a0, a0
  sw a0, -156(fp)
  j .L1867
.L1868:
.L1867:
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  divw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  remw a0, t0, a0
  sw a0, -160(fp)
  lw a0, -160(fp)
  sw a0, -176(fp)
  li a0, 0
  lw t0, -176(fp)
  slt a0, t0, a0
  beqz a0, .L1870
  lw a0, -160(fp)
  negw a0, a0
  sw a0, -160(fp)
  j .L1869
.L1870:
.L1869:
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  divw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  remw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -176(fp)
  li a0, 0
  lw t0, -176(fp)
  slt a0, t0, a0
  beqz a0, .L1872
  lw a0, -164(fp)
  negw a0, a0
  sw a0, -164(fp)
  j .L1871
.L1872:
.L1871:
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  divw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  remw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -176(fp)
  li a0, 0
  lw t0, -176(fp)
  slt a0, t0, a0
  beqz a0, .L1874
  lw a0, -168(fp)
  negw a0, a0
  sw a0, -168(fp)
  j .L1873
.L1874:
.L1873:
  lw a0, -172(fp)
  sw a0, -176(fp)
  li a0, 2
  lw t0, -176(fp)
  divw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -44(fp)
  snez a0, a0
  bnez a0, .L1877
  sw a0, -304(fp)
  lw a0, -108(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L1877:
  beqz a0, .L1876
  li a0, 1
  sw a0, -300(fp)
  j .L1875
.L1876:
  li a0, 0
  sw a0, -300(fp)
.L1875:
  lw a0, -44(fp)
  snez a0, a0
  beqz a0, .L1880
  sw a0, -308(fp)
  lw a0, -108(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1880:
  beqz a0, .L1879
  li a0, 1
  sw a0, -304(fp)
  j .L1878
.L1879:
  li a0, 0
  sw a0, -304(fp)
.L1878:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L1882
  li a0, 1
  sw a0, -308(fp)
  j .L1881
.L1882:
  li a0, 0
  sw a0, -308(fp)
.L1881:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1885
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1885:
  beqz a0, .L1884
  li a0, 1
  sw a0, -296(fp)
  j .L1883
.L1884:
  li a0, 0
  sw a0, -296(fp)
.L1883:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L1888
  sw a0, -304(fp)
  li a0, 0
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L1888:
  beqz a0, .L1887
  li a0, 1
  sw a0, -300(fp)
  j .L1886
.L1887:
  li a0, 0
  sw a0, -300(fp)
.L1886:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L1891
  sw a0, -308(fp)
  li a0, 0
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1891:
  beqz a0, .L1890
  li a0, 1
  sw a0, -304(fp)
  j .L1889
.L1890:
  li a0, 0
  sw a0, -304(fp)
.L1889:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L1893
  li a0, 1
  sw a0, -308(fp)
  j .L1892
.L1893:
  li a0, 0
  sw a0, -308(fp)
.L1892:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1896
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1896:
  beqz a0, .L1895
  li a0, 1
  sw a0, -232(fp)
  j .L1894
.L1895:
  li a0, 0
  sw a0, -232(fp)
.L1894:
  lw a0, -44(fp)
  snez a0, a0
  beqz a0, .L1899
  sw a0, -304(fp)
  lw a0, -108(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L1899:
  beqz a0, .L1898
  li a0, 1
  sw a0, -300(fp)
  j .L1897
.L1898:
  li a0, 0
  sw a0, -300(fp)
.L1897:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L1902
  sw a0, -308(fp)
  li a0, 0
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1902:
  beqz a0, .L1901
  li a0, 1
  sw a0, -304(fp)
  j .L1900
.L1901:
  li a0, 0
  sw a0, -304(fp)
.L1900:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1905
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1905:
  beqz a0, .L1904
  li a0, 1
  sw a0, -172(fp)
  j .L1903
.L1904:
  li a0, 0
  sw a0, -172(fp)
.L1903:
  lw a0, -48(fp)
  snez a0, a0
  bnez a0, .L1908
  sw a0, -304(fp)
  lw a0, -112(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L1908:
  beqz a0, .L1907
  li a0, 1
  sw a0, -300(fp)
  j .L1906
.L1907:
  li a0, 0
  sw a0, -300(fp)
.L1906:
  lw a0, -48(fp)
  snez a0, a0
  beqz a0, .L1911
  sw a0, -308(fp)
  lw a0, -112(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1911:
  beqz a0, .L1910
  li a0, 1
  sw a0, -304(fp)
  j .L1909
.L1910:
  li a0, 0
  sw a0, -304(fp)
.L1909:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L1913
  li a0, 1
  sw a0, -308(fp)
  j .L1912
.L1913:
  li a0, 0
  sw a0, -308(fp)
.L1912:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1916
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1916:
  beqz a0, .L1915
  li a0, 1
  sw a0, -296(fp)
  j .L1914
.L1915:
  li a0, 0
  sw a0, -296(fp)
.L1914:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L1919
  sw a0, -304(fp)
  lw a0, -172(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L1919:
  beqz a0, .L1918
  li a0, 1
  sw a0, -300(fp)
  j .L1917
.L1918:
  li a0, 0
  sw a0, -300(fp)
.L1917:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L1922
  sw a0, -308(fp)
  lw a0, -172(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1922:
  beqz a0, .L1921
  li a0, 1
  sw a0, -304(fp)
  j .L1920
.L1921:
  li a0, 0
  sw a0, -304(fp)
.L1920:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L1924
  li a0, 1
  sw a0, -308(fp)
  j .L1923
.L1924:
  li a0, 0
  sw a0, -308(fp)
.L1923:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1927
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1927:
  beqz a0, .L1926
  li a0, 1
  sw a0, -236(fp)
  j .L1925
.L1926:
  li a0, 0
  sw a0, -236(fp)
.L1925:
  lw a0, -48(fp)
  snez a0, a0
  beqz a0, .L1930
  sw a0, -304(fp)
  lw a0, -112(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L1930:
  beqz a0, .L1929
  li a0, 1
  sw a0, -300(fp)
  j .L1928
.L1929:
  li a0, 0
  sw a0, -300(fp)
.L1928:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L1933
  sw a0, -308(fp)
  lw a0, -172(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1933:
  beqz a0, .L1932
  li a0, 1
  sw a0, -304(fp)
  j .L1931
.L1932:
  li a0, 0
  sw a0, -304(fp)
.L1931:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1936
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1936:
  beqz a0, .L1935
  li a0, 1
  sw a0, -176(fp)
  j .L1934
.L1935:
  li a0, 0
  sw a0, -176(fp)
.L1934:
  lw a0, -52(fp)
  snez a0, a0
  bnez a0, .L1939
  sw a0, -304(fp)
  lw a0, -116(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L1939:
  beqz a0, .L1938
  li a0, 1
  sw a0, -300(fp)
  j .L1937
.L1938:
  li a0, 0
  sw a0, -300(fp)
.L1937:
  lw a0, -52(fp)
  snez a0, a0
  beqz a0, .L1942
  sw a0, -308(fp)
  lw a0, -116(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1942:
  beqz a0, .L1941
  li a0, 1
  sw a0, -304(fp)
  j .L1940
.L1941:
  li a0, 0
  sw a0, -304(fp)
.L1940:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L1944
  li a0, 1
  sw a0, -308(fp)
  j .L1943
.L1944:
  li a0, 0
  sw a0, -308(fp)
.L1943:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1947
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1947:
  beqz a0, .L1946
  li a0, 1
  sw a0, -296(fp)
  j .L1945
.L1946:
  li a0, 0
  sw a0, -296(fp)
.L1945:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L1950
  sw a0, -304(fp)
  lw a0, -176(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L1950:
  beqz a0, .L1949
  li a0, 1
  sw a0, -300(fp)
  j .L1948
.L1949:
  li a0, 0
  sw a0, -300(fp)
.L1948:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L1953
  sw a0, -308(fp)
  lw a0, -176(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1953:
  beqz a0, .L1952
  li a0, 1
  sw a0, -304(fp)
  j .L1951
.L1952:
  li a0, 0
  sw a0, -304(fp)
.L1951:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L1955
  li a0, 1
  sw a0, -308(fp)
  j .L1954
.L1955:
  li a0, 0
  sw a0, -308(fp)
.L1954:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1958
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1958:
  beqz a0, .L1957
  li a0, 1
  sw a0, -240(fp)
  j .L1956
.L1957:
  li a0, 0
  sw a0, -240(fp)
.L1956:
  lw a0, -52(fp)
  snez a0, a0
  beqz a0, .L1961
  sw a0, -304(fp)
  lw a0, -116(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L1961:
  beqz a0, .L1960
  li a0, 1
  sw a0, -300(fp)
  j .L1959
.L1960:
  li a0, 0
  sw a0, -300(fp)
.L1959:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L1964
  sw a0, -308(fp)
  lw a0, -176(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1964:
  beqz a0, .L1963
  li a0, 1
  sw a0, -304(fp)
  j .L1962
.L1963:
  li a0, 0
  sw a0, -304(fp)
.L1962:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1967
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1967:
  beqz a0, .L1966
  li a0, 1
  sw a0, -180(fp)
  j .L1965
.L1966:
  li a0, 0
  sw a0, -180(fp)
.L1965:
  lw a0, -56(fp)
  snez a0, a0
  bnez a0, .L1970
  sw a0, -304(fp)
  lw a0, -120(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L1970:
  beqz a0, .L1969
  li a0, 1
  sw a0, -300(fp)
  j .L1968
.L1969:
  li a0, 0
  sw a0, -300(fp)
.L1968:
  lw a0, -56(fp)
  snez a0, a0
  beqz a0, .L1973
  sw a0, -308(fp)
  lw a0, -120(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1973:
  beqz a0, .L1972
  li a0, 1
  sw a0, -304(fp)
  j .L1971
.L1972:
  li a0, 0
  sw a0, -304(fp)
.L1971:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L1975
  li a0, 1
  sw a0, -308(fp)
  j .L1974
.L1975:
  li a0, 0
  sw a0, -308(fp)
.L1974:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1978
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1978:
  beqz a0, .L1977
  li a0, 1
  sw a0, -296(fp)
  j .L1976
.L1977:
  li a0, 0
  sw a0, -296(fp)
.L1976:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L1981
  sw a0, -304(fp)
  lw a0, -180(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L1981:
  beqz a0, .L1980
  li a0, 1
  sw a0, -300(fp)
  j .L1979
.L1980:
  li a0, 0
  sw a0, -300(fp)
.L1979:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L1984
  sw a0, -308(fp)
  lw a0, -180(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1984:
  beqz a0, .L1983
  li a0, 1
  sw a0, -304(fp)
  j .L1982
.L1983:
  li a0, 0
  sw a0, -304(fp)
.L1982:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L1986
  li a0, 1
  sw a0, -308(fp)
  j .L1985
.L1986:
  li a0, 0
  sw a0, -308(fp)
.L1985:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L1989
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L1989:
  beqz a0, .L1988
  li a0, 1
  sw a0, -244(fp)
  j .L1987
.L1988:
  li a0, 0
  sw a0, -244(fp)
.L1987:
  lw a0, -56(fp)
  snez a0, a0
  beqz a0, .L1992
  sw a0, -304(fp)
  lw a0, -120(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L1992:
  beqz a0, .L1991
  li a0, 1
  sw a0, -300(fp)
  j .L1990
.L1991:
  li a0, 0
  sw a0, -300(fp)
.L1990:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L1995
  sw a0, -308(fp)
  lw a0, -180(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L1995:
  beqz a0, .L1994
  li a0, 1
  sw a0, -304(fp)
  j .L1993
.L1994:
  li a0, 0
  sw a0, -304(fp)
.L1993:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L1998
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L1998:
  beqz a0, .L1997
  li a0, 1
  sw a0, -184(fp)
  j .L1996
.L1997:
  li a0, 0
  sw a0, -184(fp)
.L1996:
  lw a0, -60(fp)
  snez a0, a0
  bnez a0, .L2001
  sw a0, -304(fp)
  lw a0, -124(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2001:
  beqz a0, .L2000
  li a0, 1
  sw a0, -300(fp)
  j .L1999
.L2000:
  li a0, 0
  sw a0, -300(fp)
.L1999:
  lw a0, -60(fp)
  snez a0, a0
  beqz a0, .L2004
  sw a0, -308(fp)
  lw a0, -124(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2004:
  beqz a0, .L2003
  li a0, 1
  sw a0, -304(fp)
  j .L2002
.L2003:
  li a0, 0
  sw a0, -304(fp)
.L2002:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2006
  li a0, 1
  sw a0, -308(fp)
  j .L2005
.L2006:
  li a0, 0
  sw a0, -308(fp)
.L2005:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2009
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2009:
  beqz a0, .L2008
  li a0, 1
  sw a0, -296(fp)
  j .L2007
.L2008:
  li a0, 0
  sw a0, -296(fp)
.L2007:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L2012
  sw a0, -304(fp)
  lw a0, -184(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2012:
  beqz a0, .L2011
  li a0, 1
  sw a0, -300(fp)
  j .L2010
.L2011:
  li a0, 0
  sw a0, -300(fp)
.L2010:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2015
  sw a0, -308(fp)
  lw a0, -184(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2015:
  beqz a0, .L2014
  li a0, 1
  sw a0, -304(fp)
  j .L2013
.L2014:
  li a0, 0
  sw a0, -304(fp)
.L2013:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2017
  li a0, 1
  sw a0, -308(fp)
  j .L2016
.L2017:
  li a0, 0
  sw a0, -308(fp)
.L2016:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2020
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2020:
  beqz a0, .L2019
  li a0, 1
  sw a0, -248(fp)
  j .L2018
.L2019:
  li a0, 0
  sw a0, -248(fp)
.L2018:
  lw a0, -60(fp)
  snez a0, a0
  beqz a0, .L2023
  sw a0, -304(fp)
  lw a0, -124(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L2023:
  beqz a0, .L2022
  li a0, 1
  sw a0, -300(fp)
  j .L2021
.L2022:
  li a0, 0
  sw a0, -300(fp)
.L2021:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2026
  sw a0, -308(fp)
  lw a0, -184(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2026:
  beqz a0, .L2025
  li a0, 1
  sw a0, -304(fp)
  j .L2024
.L2025:
  li a0, 0
  sw a0, -304(fp)
.L2024:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2029
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2029:
  beqz a0, .L2028
  li a0, 1
  sw a0, -188(fp)
  j .L2027
.L2028:
  li a0, 0
  sw a0, -188(fp)
.L2027:
  lw a0, -64(fp)
  snez a0, a0
  bnez a0, .L2032
  sw a0, -304(fp)
  lw a0, -128(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2032:
  beqz a0, .L2031
  li a0, 1
  sw a0, -300(fp)
  j .L2030
.L2031:
  li a0, 0
  sw a0, -300(fp)
.L2030:
  lw a0, -64(fp)
  snez a0, a0
  beqz a0, .L2035
  sw a0, -308(fp)
  lw a0, -128(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2035:
  beqz a0, .L2034
  li a0, 1
  sw a0, -304(fp)
  j .L2033
.L2034:
  li a0, 0
  sw a0, -304(fp)
.L2033:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2037
  li a0, 1
  sw a0, -308(fp)
  j .L2036
.L2037:
  li a0, 0
  sw a0, -308(fp)
.L2036:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2040
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2040:
  beqz a0, .L2039
  li a0, 1
  sw a0, -296(fp)
  j .L2038
.L2039:
  li a0, 0
  sw a0, -296(fp)
.L2038:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L2043
  sw a0, -304(fp)
  lw a0, -188(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2043:
  beqz a0, .L2042
  li a0, 1
  sw a0, -300(fp)
  j .L2041
.L2042:
  li a0, 0
  sw a0, -300(fp)
.L2041:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2046
  sw a0, -308(fp)
  lw a0, -188(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2046:
  beqz a0, .L2045
  li a0, 1
  sw a0, -304(fp)
  j .L2044
.L2045:
  li a0, 0
  sw a0, -304(fp)
.L2044:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2048
  li a0, 1
  sw a0, -308(fp)
  j .L2047
.L2048:
  li a0, 0
  sw a0, -308(fp)
.L2047:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2051
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2051:
  beqz a0, .L2050
  li a0, 1
  sw a0, -252(fp)
  j .L2049
.L2050:
  li a0, 0
  sw a0, -252(fp)
.L2049:
  lw a0, -64(fp)
  snez a0, a0
  beqz a0, .L2054
  sw a0, -304(fp)
  lw a0, -128(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L2054:
  beqz a0, .L2053
  li a0, 1
  sw a0, -300(fp)
  j .L2052
.L2053:
  li a0, 0
  sw a0, -300(fp)
.L2052:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2057
  sw a0, -308(fp)
  lw a0, -188(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2057:
  beqz a0, .L2056
  li a0, 1
  sw a0, -304(fp)
  j .L2055
.L2056:
  li a0, 0
  sw a0, -304(fp)
.L2055:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2060
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2060:
  beqz a0, .L2059
  li a0, 1
  sw a0, -192(fp)
  j .L2058
.L2059:
  li a0, 0
  sw a0, -192(fp)
.L2058:
  lw a0, -68(fp)
  snez a0, a0
  bnez a0, .L2063
  sw a0, -304(fp)
  lw a0, -132(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2063:
  beqz a0, .L2062
  li a0, 1
  sw a0, -300(fp)
  j .L2061
.L2062:
  li a0, 0
  sw a0, -300(fp)
.L2061:
  lw a0, -68(fp)
  snez a0, a0
  beqz a0, .L2066
  sw a0, -308(fp)
  lw a0, -132(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2066:
  beqz a0, .L2065
  li a0, 1
  sw a0, -304(fp)
  j .L2064
.L2065:
  li a0, 0
  sw a0, -304(fp)
.L2064:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2068
  li a0, 1
  sw a0, -308(fp)
  j .L2067
.L2068:
  li a0, 0
  sw a0, -308(fp)
.L2067:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2071
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2071:
  beqz a0, .L2070
  li a0, 1
  sw a0, -296(fp)
  j .L2069
.L2070:
  li a0, 0
  sw a0, -296(fp)
.L2069:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L2074
  sw a0, -304(fp)
  lw a0, -192(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2074:
  beqz a0, .L2073
  li a0, 1
  sw a0, -300(fp)
  j .L2072
.L2073:
  li a0, 0
  sw a0, -300(fp)
.L2072:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2077
  sw a0, -308(fp)
  lw a0, -192(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2077:
  beqz a0, .L2076
  li a0, 1
  sw a0, -304(fp)
  j .L2075
.L2076:
  li a0, 0
  sw a0, -304(fp)
.L2075:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2079
  li a0, 1
  sw a0, -308(fp)
  j .L2078
.L2079:
  li a0, 0
  sw a0, -308(fp)
.L2078:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2082
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2082:
  beqz a0, .L2081
  li a0, 1
  sw a0, -256(fp)
  j .L2080
.L2081:
  li a0, 0
  sw a0, -256(fp)
.L2080:
  lw a0, -68(fp)
  snez a0, a0
  beqz a0, .L2085
  sw a0, -304(fp)
  lw a0, -132(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L2085:
  beqz a0, .L2084
  li a0, 1
  sw a0, -300(fp)
  j .L2083
.L2084:
  li a0, 0
  sw a0, -300(fp)
.L2083:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2088
  sw a0, -308(fp)
  lw a0, -192(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2088:
  beqz a0, .L2087
  li a0, 1
  sw a0, -304(fp)
  j .L2086
.L2087:
  li a0, 0
  sw a0, -304(fp)
.L2086:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2091
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2091:
  beqz a0, .L2090
  li a0, 1
  sw a0, -196(fp)
  j .L2089
.L2090:
  li a0, 0
  sw a0, -196(fp)
.L2089:
  lw a0, -72(fp)
  snez a0, a0
  bnez a0, .L2094
  sw a0, -304(fp)
  lw a0, -136(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2094:
  beqz a0, .L2093
  li a0, 1
  sw a0, -300(fp)
  j .L2092
.L2093:
  li a0, 0
  sw a0, -300(fp)
.L2092:
  lw a0, -72(fp)
  snez a0, a0
  beqz a0, .L2097
  sw a0, -308(fp)
  lw a0, -136(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2097:
  beqz a0, .L2096
  li a0, 1
  sw a0, -304(fp)
  j .L2095
.L2096:
  li a0, 0
  sw a0, -304(fp)
.L2095:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2099
  li a0, 1
  sw a0, -308(fp)
  j .L2098
.L2099:
  li a0, 0
  sw a0, -308(fp)
.L2098:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2102
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2102:
  beqz a0, .L2101
  li a0, 1
  sw a0, -296(fp)
  j .L2100
.L2101:
  li a0, 0
  sw a0, -296(fp)
.L2100:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L2105
  sw a0, -304(fp)
  lw a0, -196(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2105:
  beqz a0, .L2104
  li a0, 1
  sw a0, -300(fp)
  j .L2103
.L2104:
  li a0, 0
  sw a0, -300(fp)
.L2103:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2108
  sw a0, -308(fp)
  lw a0, -196(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2108:
  beqz a0, .L2107
  li a0, 1
  sw a0, -304(fp)
  j .L2106
.L2107:
  li a0, 0
  sw a0, -304(fp)
.L2106:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2110
  li a0, 1
  sw a0, -308(fp)
  j .L2109
.L2110:
  li a0, 0
  sw a0, -308(fp)
.L2109:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2113
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2113:
  beqz a0, .L2112
  li a0, 1
  sw a0, -260(fp)
  j .L2111
.L2112:
  li a0, 0
  sw a0, -260(fp)
.L2111:
  lw a0, -72(fp)
  snez a0, a0
  beqz a0, .L2116
  sw a0, -304(fp)
  lw a0, -136(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L2116:
  beqz a0, .L2115
  li a0, 1
  sw a0, -300(fp)
  j .L2114
.L2115:
  li a0, 0
  sw a0, -300(fp)
.L2114:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2119
  sw a0, -308(fp)
  lw a0, -196(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2119:
  beqz a0, .L2118
  li a0, 1
  sw a0, -304(fp)
  j .L2117
.L2118:
  li a0, 0
  sw a0, -304(fp)
.L2117:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2122
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2122:
  beqz a0, .L2121
  li a0, 1
  sw a0, -200(fp)
  j .L2120
.L2121:
  li a0, 0
  sw a0, -200(fp)
.L2120:
  lw a0, -76(fp)
  snez a0, a0
  bnez a0, .L2125
  sw a0, -304(fp)
  lw a0, -140(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2125:
  beqz a0, .L2124
  li a0, 1
  sw a0, -300(fp)
  j .L2123
.L2124:
  li a0, 0
  sw a0, -300(fp)
.L2123:
  lw a0, -76(fp)
  snez a0, a0
  beqz a0, .L2128
  sw a0, -308(fp)
  lw a0, -140(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2128:
  beqz a0, .L2127
  li a0, 1
  sw a0, -304(fp)
  j .L2126
.L2127:
  li a0, 0
  sw a0, -304(fp)
.L2126:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2130
  li a0, 1
  sw a0, -308(fp)
  j .L2129
.L2130:
  li a0, 0
  sw a0, -308(fp)
.L2129:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2133
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2133:
  beqz a0, .L2132
  li a0, 1
  sw a0, -296(fp)
  j .L2131
.L2132:
  li a0, 0
  sw a0, -296(fp)
.L2131:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L2136
  sw a0, -304(fp)
  lw a0, -200(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2136:
  beqz a0, .L2135
  li a0, 1
  sw a0, -300(fp)
  j .L2134
.L2135:
  li a0, 0
  sw a0, -300(fp)
.L2134:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2139
  sw a0, -308(fp)
  lw a0, -200(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2139:
  beqz a0, .L2138
  li a0, 1
  sw a0, -304(fp)
  j .L2137
.L2138:
  li a0, 0
  sw a0, -304(fp)
.L2137:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2141
  li a0, 1
  sw a0, -308(fp)
  j .L2140
.L2141:
  li a0, 0
  sw a0, -308(fp)
.L2140:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2144
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2144:
  beqz a0, .L2143
  li a0, 1
  sw a0, -264(fp)
  j .L2142
.L2143:
  li a0, 0
  sw a0, -264(fp)
.L2142:
  lw a0, -76(fp)
  snez a0, a0
  beqz a0, .L2147
  sw a0, -304(fp)
  lw a0, -140(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L2147:
  beqz a0, .L2146
  li a0, 1
  sw a0, -300(fp)
  j .L2145
.L2146:
  li a0, 0
  sw a0, -300(fp)
.L2145:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2150
  sw a0, -308(fp)
  lw a0, -200(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2150:
  beqz a0, .L2149
  li a0, 1
  sw a0, -304(fp)
  j .L2148
.L2149:
  li a0, 0
  sw a0, -304(fp)
.L2148:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2153
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2153:
  beqz a0, .L2152
  li a0, 1
  sw a0, -204(fp)
  j .L2151
.L2152:
  li a0, 0
  sw a0, -204(fp)
.L2151:
  lw a0, -80(fp)
  snez a0, a0
  bnez a0, .L2156
  sw a0, -304(fp)
  lw a0, -144(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2156:
  beqz a0, .L2155
  li a0, 1
  sw a0, -300(fp)
  j .L2154
.L2155:
  li a0, 0
  sw a0, -300(fp)
.L2154:
  lw a0, -80(fp)
  snez a0, a0
  beqz a0, .L2159
  sw a0, -308(fp)
  lw a0, -144(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2159:
  beqz a0, .L2158
  li a0, 1
  sw a0, -304(fp)
  j .L2157
.L2158:
  li a0, 0
  sw a0, -304(fp)
.L2157:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2161
  li a0, 1
  sw a0, -308(fp)
  j .L2160
.L2161:
  li a0, 0
  sw a0, -308(fp)
.L2160:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2164
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2164:
  beqz a0, .L2163
  li a0, 1
  sw a0, -296(fp)
  j .L2162
.L2163:
  li a0, 0
  sw a0, -296(fp)
.L2162:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L2167
  sw a0, -304(fp)
  lw a0, -204(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2167:
  beqz a0, .L2166
  li a0, 1
  sw a0, -300(fp)
  j .L2165
.L2166:
  li a0, 0
  sw a0, -300(fp)
.L2165:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2170
  sw a0, -308(fp)
  lw a0, -204(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2170:
  beqz a0, .L2169
  li a0, 1
  sw a0, -304(fp)
  j .L2168
.L2169:
  li a0, 0
  sw a0, -304(fp)
.L2168:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2172
  li a0, 1
  sw a0, -308(fp)
  j .L2171
.L2172:
  li a0, 0
  sw a0, -308(fp)
.L2171:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2175
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2175:
  beqz a0, .L2174
  li a0, 1
  sw a0, -268(fp)
  j .L2173
.L2174:
  li a0, 0
  sw a0, -268(fp)
.L2173:
  lw a0, -80(fp)
  snez a0, a0
  beqz a0, .L2178
  sw a0, -304(fp)
  lw a0, -144(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L2178:
  beqz a0, .L2177
  li a0, 1
  sw a0, -300(fp)
  j .L2176
.L2177:
  li a0, 0
  sw a0, -300(fp)
.L2176:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2181
  sw a0, -308(fp)
  lw a0, -204(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2181:
  beqz a0, .L2180
  li a0, 1
  sw a0, -304(fp)
  j .L2179
.L2180:
  li a0, 0
  sw a0, -304(fp)
.L2179:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2184
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2184:
  beqz a0, .L2183
  li a0, 1
  sw a0, -208(fp)
  j .L2182
.L2183:
  li a0, 0
  sw a0, -208(fp)
.L2182:
  lw a0, -84(fp)
  snez a0, a0
  bnez a0, .L2187
  sw a0, -304(fp)
  lw a0, -148(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2187:
  beqz a0, .L2186
  li a0, 1
  sw a0, -300(fp)
  j .L2185
.L2186:
  li a0, 0
  sw a0, -300(fp)
.L2185:
  lw a0, -84(fp)
  snez a0, a0
  beqz a0, .L2190
  sw a0, -308(fp)
  lw a0, -148(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2190:
  beqz a0, .L2189
  li a0, 1
  sw a0, -304(fp)
  j .L2188
.L2189:
  li a0, 0
  sw a0, -304(fp)
.L2188:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2192
  li a0, 1
  sw a0, -308(fp)
  j .L2191
.L2192:
  li a0, 0
  sw a0, -308(fp)
.L2191:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2195
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2195:
  beqz a0, .L2194
  li a0, 1
  sw a0, -296(fp)
  j .L2193
.L2194:
  li a0, 0
  sw a0, -296(fp)
.L2193:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L2198
  sw a0, -304(fp)
  lw a0, -208(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2198:
  beqz a0, .L2197
  li a0, 1
  sw a0, -300(fp)
  j .L2196
.L2197:
  li a0, 0
  sw a0, -300(fp)
.L2196:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2201
  sw a0, -308(fp)
  lw a0, -208(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2201:
  beqz a0, .L2200
  li a0, 1
  sw a0, -304(fp)
  j .L2199
.L2200:
  li a0, 0
  sw a0, -304(fp)
.L2199:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2203
  li a0, 1
  sw a0, -308(fp)
  j .L2202
.L2203:
  li a0, 0
  sw a0, -308(fp)
.L2202:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2206
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2206:
  beqz a0, .L2205
  li a0, 1
  sw a0, -272(fp)
  j .L2204
.L2205:
  li a0, 0
  sw a0, -272(fp)
.L2204:
  lw a0, -84(fp)
  snez a0, a0
  beqz a0, .L2209
  sw a0, -304(fp)
  lw a0, -148(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L2209:
  beqz a0, .L2208
  li a0, 1
  sw a0, -300(fp)
  j .L2207
.L2208:
  li a0, 0
  sw a0, -300(fp)
.L2207:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2212
  sw a0, -308(fp)
  lw a0, -208(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2212:
  beqz a0, .L2211
  li a0, 1
  sw a0, -304(fp)
  j .L2210
.L2211:
  li a0, 0
  sw a0, -304(fp)
.L2210:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2215
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2215:
  beqz a0, .L2214
  li a0, 1
  sw a0, -212(fp)
  j .L2213
.L2214:
  li a0, 0
  sw a0, -212(fp)
.L2213:
  lw a0, -88(fp)
  snez a0, a0
  bnez a0, .L2218
  sw a0, -304(fp)
  lw a0, -152(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2218:
  beqz a0, .L2217
  li a0, 1
  sw a0, -300(fp)
  j .L2216
.L2217:
  li a0, 0
  sw a0, -300(fp)
.L2216:
  lw a0, -88(fp)
  snez a0, a0
  beqz a0, .L2221
  sw a0, -308(fp)
  lw a0, -152(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2221:
  beqz a0, .L2220
  li a0, 1
  sw a0, -304(fp)
  j .L2219
.L2220:
  li a0, 0
  sw a0, -304(fp)
.L2219:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2223
  li a0, 1
  sw a0, -308(fp)
  j .L2222
.L2223:
  li a0, 0
  sw a0, -308(fp)
.L2222:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2226
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2226:
  beqz a0, .L2225
  li a0, 1
  sw a0, -296(fp)
  j .L2224
.L2225:
  li a0, 0
  sw a0, -296(fp)
.L2224:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L2229
  sw a0, -304(fp)
  lw a0, -212(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2229:
  beqz a0, .L2228
  li a0, 1
  sw a0, -300(fp)
  j .L2227
.L2228:
  li a0, 0
  sw a0, -300(fp)
.L2227:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2232
  sw a0, -308(fp)
  lw a0, -212(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2232:
  beqz a0, .L2231
  li a0, 1
  sw a0, -304(fp)
  j .L2230
.L2231:
  li a0, 0
  sw a0, -304(fp)
.L2230:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2234
  li a0, 1
  sw a0, -308(fp)
  j .L2233
.L2234:
  li a0, 0
  sw a0, -308(fp)
.L2233:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2237
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2237:
  beqz a0, .L2236
  li a0, 1
  sw a0, -276(fp)
  j .L2235
.L2236:
  li a0, 0
  sw a0, -276(fp)
.L2235:
  lw a0, -88(fp)
  snez a0, a0
  beqz a0, .L2240
  sw a0, -304(fp)
  lw a0, -152(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L2240:
  beqz a0, .L2239
  li a0, 1
  sw a0, -300(fp)
  j .L2238
.L2239:
  li a0, 0
  sw a0, -300(fp)
.L2238:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2243
  sw a0, -308(fp)
  lw a0, -212(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2243:
  beqz a0, .L2242
  li a0, 1
  sw a0, -304(fp)
  j .L2241
.L2242:
  li a0, 0
  sw a0, -304(fp)
.L2241:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2246
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2246:
  beqz a0, .L2245
  li a0, 1
  sw a0, -216(fp)
  j .L2244
.L2245:
  li a0, 0
  sw a0, -216(fp)
.L2244:
  lw a0, -92(fp)
  snez a0, a0
  bnez a0, .L2249
  sw a0, -304(fp)
  lw a0, -156(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2249:
  beqz a0, .L2248
  li a0, 1
  sw a0, -300(fp)
  j .L2247
.L2248:
  li a0, 0
  sw a0, -300(fp)
.L2247:
  lw a0, -92(fp)
  snez a0, a0
  beqz a0, .L2252
  sw a0, -308(fp)
  lw a0, -156(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2252:
  beqz a0, .L2251
  li a0, 1
  sw a0, -304(fp)
  j .L2250
.L2251:
  li a0, 0
  sw a0, -304(fp)
.L2250:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2254
  li a0, 1
  sw a0, -308(fp)
  j .L2253
.L2254:
  li a0, 0
  sw a0, -308(fp)
.L2253:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2257
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2257:
  beqz a0, .L2256
  li a0, 1
  sw a0, -296(fp)
  j .L2255
.L2256:
  li a0, 0
  sw a0, -296(fp)
.L2255:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L2260
  sw a0, -304(fp)
  lw a0, -216(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2260:
  beqz a0, .L2259
  li a0, 1
  sw a0, -300(fp)
  j .L2258
.L2259:
  li a0, 0
  sw a0, -300(fp)
.L2258:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2263
  sw a0, -308(fp)
  lw a0, -216(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2263:
  beqz a0, .L2262
  li a0, 1
  sw a0, -304(fp)
  j .L2261
.L2262:
  li a0, 0
  sw a0, -304(fp)
.L2261:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2265
  li a0, 1
  sw a0, -308(fp)
  j .L2264
.L2265:
  li a0, 0
  sw a0, -308(fp)
.L2264:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2268
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2268:
  beqz a0, .L2267
  li a0, 1
  sw a0, -280(fp)
  j .L2266
.L2267:
  li a0, 0
  sw a0, -280(fp)
.L2266:
  lw a0, -92(fp)
  snez a0, a0
  beqz a0, .L2271
  sw a0, -304(fp)
  lw a0, -156(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L2271:
  beqz a0, .L2270
  li a0, 1
  sw a0, -300(fp)
  j .L2269
.L2270:
  li a0, 0
  sw a0, -300(fp)
.L2269:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2274
  sw a0, -308(fp)
  lw a0, -216(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2274:
  beqz a0, .L2273
  li a0, 1
  sw a0, -304(fp)
  j .L2272
.L2273:
  li a0, 0
  sw a0, -304(fp)
.L2272:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2277
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2277:
  beqz a0, .L2276
  li a0, 1
  sw a0, -220(fp)
  j .L2275
.L2276:
  li a0, 0
  sw a0, -220(fp)
.L2275:
  lw a0, -96(fp)
  snez a0, a0
  bnez a0, .L2280
  sw a0, -304(fp)
  lw a0, -160(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2280:
  beqz a0, .L2279
  li a0, 1
  sw a0, -300(fp)
  j .L2278
.L2279:
  li a0, 0
  sw a0, -300(fp)
.L2278:
  lw a0, -96(fp)
  snez a0, a0
  beqz a0, .L2283
  sw a0, -308(fp)
  lw a0, -160(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2283:
  beqz a0, .L2282
  li a0, 1
  sw a0, -304(fp)
  j .L2281
.L2282:
  li a0, 0
  sw a0, -304(fp)
.L2281:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2285
  li a0, 1
  sw a0, -308(fp)
  j .L2284
.L2285:
  li a0, 0
  sw a0, -308(fp)
.L2284:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2288
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2288:
  beqz a0, .L2287
  li a0, 1
  sw a0, -296(fp)
  j .L2286
.L2287:
  li a0, 0
  sw a0, -296(fp)
.L2286:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L2291
  sw a0, -304(fp)
  lw a0, -220(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2291:
  beqz a0, .L2290
  li a0, 1
  sw a0, -300(fp)
  j .L2289
.L2290:
  li a0, 0
  sw a0, -300(fp)
.L2289:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2294
  sw a0, -308(fp)
  lw a0, -220(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2294:
  beqz a0, .L2293
  li a0, 1
  sw a0, -304(fp)
  j .L2292
.L2293:
  li a0, 0
  sw a0, -304(fp)
.L2292:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2296
  li a0, 1
  sw a0, -308(fp)
  j .L2295
.L2296:
  li a0, 0
  sw a0, -308(fp)
.L2295:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2299
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2299:
  beqz a0, .L2298
  li a0, 1
  sw a0, -284(fp)
  j .L2297
.L2298:
  li a0, 0
  sw a0, -284(fp)
.L2297:
  lw a0, -96(fp)
  snez a0, a0
  beqz a0, .L2302
  sw a0, -304(fp)
  lw a0, -160(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L2302:
  beqz a0, .L2301
  li a0, 1
  sw a0, -300(fp)
  j .L2300
.L2301:
  li a0, 0
  sw a0, -300(fp)
.L2300:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2305
  sw a0, -308(fp)
  lw a0, -220(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2305:
  beqz a0, .L2304
  li a0, 1
  sw a0, -304(fp)
  j .L2303
.L2304:
  li a0, 0
  sw a0, -304(fp)
.L2303:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2308
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2308:
  beqz a0, .L2307
  li a0, 1
  sw a0, -224(fp)
  j .L2306
.L2307:
  li a0, 0
  sw a0, -224(fp)
.L2306:
  lw a0, -100(fp)
  snez a0, a0
  bnez a0, .L2311
  sw a0, -304(fp)
  lw a0, -164(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2311:
  beqz a0, .L2310
  li a0, 1
  sw a0, -300(fp)
  j .L2309
.L2310:
  li a0, 0
  sw a0, -300(fp)
.L2309:
  lw a0, -100(fp)
  snez a0, a0
  beqz a0, .L2314
  sw a0, -308(fp)
  lw a0, -164(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2314:
  beqz a0, .L2313
  li a0, 1
  sw a0, -304(fp)
  j .L2312
.L2313:
  li a0, 0
  sw a0, -304(fp)
.L2312:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2316
  li a0, 1
  sw a0, -308(fp)
  j .L2315
.L2316:
  li a0, 0
  sw a0, -308(fp)
.L2315:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2319
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2319:
  beqz a0, .L2318
  li a0, 1
  sw a0, -296(fp)
  j .L2317
.L2318:
  li a0, 0
  sw a0, -296(fp)
.L2317:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L2322
  sw a0, -304(fp)
  lw a0, -224(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2322:
  beqz a0, .L2321
  li a0, 1
  sw a0, -300(fp)
  j .L2320
.L2321:
  li a0, 0
  sw a0, -300(fp)
.L2320:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2325
  sw a0, -308(fp)
  lw a0, -224(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2325:
  beqz a0, .L2324
  li a0, 1
  sw a0, -304(fp)
  j .L2323
.L2324:
  li a0, 0
  sw a0, -304(fp)
.L2323:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2327
  li a0, 1
  sw a0, -308(fp)
  j .L2326
.L2327:
  li a0, 0
  sw a0, -308(fp)
.L2326:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2330
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2330:
  beqz a0, .L2329
  li a0, 1
  sw a0, -288(fp)
  j .L2328
.L2329:
  li a0, 0
  sw a0, -288(fp)
.L2328:
  lw a0, -100(fp)
  snez a0, a0
  beqz a0, .L2333
  sw a0, -304(fp)
  lw a0, -164(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L2333:
  beqz a0, .L2332
  li a0, 1
  sw a0, -300(fp)
  j .L2331
.L2332:
  li a0, 0
  sw a0, -300(fp)
.L2331:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2336
  sw a0, -308(fp)
  lw a0, -224(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2336:
  beqz a0, .L2335
  li a0, 1
  sw a0, -304(fp)
  j .L2334
.L2335:
  li a0, 0
  sw a0, -304(fp)
.L2334:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2339
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2339:
  beqz a0, .L2338
  li a0, 1
  sw a0, -228(fp)
  j .L2337
.L2338:
  li a0, 0
  sw a0, -228(fp)
.L2337:
  lw a0, -104(fp)
  snez a0, a0
  bnez a0, .L2342
  sw a0, -304(fp)
  lw a0, -168(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2342:
  beqz a0, .L2341
  li a0, 1
  sw a0, -300(fp)
  j .L2340
.L2341:
  li a0, 0
  sw a0, -300(fp)
.L2340:
  lw a0, -104(fp)
  snez a0, a0
  beqz a0, .L2345
  sw a0, -308(fp)
  lw a0, -168(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2345:
  beqz a0, .L2344
  li a0, 1
  sw a0, -304(fp)
  j .L2343
.L2344:
  li a0, 0
  sw a0, -304(fp)
.L2343:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2347
  li a0, 1
  sw a0, -308(fp)
  j .L2346
.L2347:
  li a0, 0
  sw a0, -308(fp)
.L2346:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2350
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2350:
  beqz a0, .L2349
  li a0, 1
  sw a0, -296(fp)
  j .L2348
.L2349:
  li a0, 0
  sw a0, -296(fp)
.L2348:
  lw a0, -296(fp)
  snez a0, a0
  bnez a0, .L2353
  sw a0, -304(fp)
  lw a0, -228(fp)
  snez a0, a0
  lw t0, -304(fp)
  or a0, t0, a0
.L2353:
  beqz a0, .L2352
  li a0, 1
  sw a0, -300(fp)
  j .L2351
.L2352:
  li a0, 0
  sw a0, -300(fp)
.L2351:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2356
  sw a0, -308(fp)
  lw a0, -228(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2356:
  beqz a0, .L2355
  li a0, 1
  sw a0, -304(fp)
  j .L2354
.L2355:
  li a0, 0
  sw a0, -304(fp)
.L2354:
  lw a0, -304(fp)
  seqz a0, a0
  beqz a0, .L2358
  li a0, 1
  sw a0, -308(fp)
  j .L2357
.L2358:
  li a0, 0
  sw a0, -308(fp)
.L2357:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2361
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2361:
  beqz a0, .L2360
  li a0, 1
  sw a0, -292(fp)
  j .L2359
.L2360:
  li a0, 0
  sw a0, -292(fp)
.L2359:
  lw a0, -104(fp)
  snez a0, a0
  beqz a0, .L2364
  sw a0, -304(fp)
  lw a0, -168(fp)
  snez a0, a0
  lw t0, -304(fp)
  and a0, t0, a0
.L2364:
  beqz a0, .L2363
  li a0, 1
  sw a0, -300(fp)
  j .L2362
.L2363:
  li a0, 0
  sw a0, -300(fp)
.L2362:
  lw a0, -296(fp)
  snez a0, a0
  beqz a0, .L2367
  sw a0, -308(fp)
  lw a0, -228(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2367:
  beqz a0, .L2366
  li a0, 1
  sw a0, -304(fp)
  j .L2365
.L2366:
  li a0, 0
  sw a0, -304(fp)
.L2365:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2370
  sw a0, -308(fp)
  lw a0, -304(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2370:
  beqz a0, .L2369
  li a0, 1
  sw a0, -40(fp)
  j .L2368
.L2369:
  li a0, 0
  sw a0, -40(fp)
.L2368:
  li a0, 0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -296(fp)
  li a0, 2
  lw t0, -296(fp)
  mulw a0, t0, a0
  sw a0, -296(fp)
  lw a0, -292(fp)
  lw t0, -296(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -296(fp)
  li a0, 2
  lw t0, -296(fp)
  mulw a0, t0, a0
  sw a0, -296(fp)
  lw a0, -288(fp)
  lw t0, -296(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -296(fp)
  li a0, 2
  lw t0, -296(fp)
  mulw a0, t0, a0
  sw a0, -296(fp)
  lw a0, -284(fp)
  lw t0, -296(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -296(fp)
  li a0, 2
  lw t0, -296(fp)
  mulw a0, t0, a0
  sw a0, -296(fp)
  lw a0, -280(fp)
  lw t0, -296(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -296(fp)
  li a0, 2
  lw t0, -296(fp)
  mulw a0, t0, a0
  sw a0, -296(fp)
  lw a0, -276(fp)
  lw t0, -296(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -296(fp)
  li a0, 2
  lw t0, -296(fp)
  mulw a0, t0, a0
  sw a0, -296(fp)
  lw a0, -272(fp)
  lw t0, -296(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -296(fp)
  li a0, 2
  lw t0, -296(fp)
  mulw a0, t0, a0
  sw a0, -296(fp)
  lw a0, -268(fp)
  lw t0, -296(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -296(fp)
  li a0, 2
  lw t0, -296(fp)
  mulw a0, t0, a0
  sw a0, -296(fp)
  lw a0, -264(fp)
  lw t0, -296(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -296(fp)
  li a0, 2
  lw t0, -296(fp)
  mulw a0, t0, a0
  sw a0, -296(fp)
  lw a0, -260(fp)
  lw t0, -296(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -296(fp)
  li a0, 2
  lw t0, -296(fp)
  mulw a0, t0, a0
  sw a0, -296(fp)
  lw a0, -256(fp)
  lw t0, -296(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -296(fp)
  li a0, 2
  lw t0, -296(fp)
  mulw a0, t0, a0
  sw a0, -296(fp)
  lw a0, -252(fp)
  lw t0, -296(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -296(fp)
  li a0, 2
  lw t0, -296(fp)
  mulw a0, t0, a0
  sw a0, -296(fp)
  lw a0, -248(fp)
  lw t0, -296(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -296(fp)
  li a0, 2
  lw t0, -296(fp)
  mulw a0, t0, a0
  sw a0, -296(fp)
  lw a0, -244(fp)
  lw t0, -296(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -296(fp)
  li a0, 2
  lw t0, -296(fp)
  mulw a0, t0, a0
  sw a0, -296(fp)
  lw a0, -240(fp)
  lw t0, -296(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -296(fp)
  li a0, 2
  lw t0, -296(fp)
  mulw a0, t0, a0
  sw a0, -296(fp)
  lw a0, -236(fp)
  lw t0, -296(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -296(fp)
  li a0, 2
  lw t0, -296(fp)
  mulw a0, t0, a0
  sw a0, -296(fp)
  lw a0, -232(fp)
  lw t0, -296(fp)
  addw a0, t0, a0
  sw a0, -32(fp)
  lw a0, -32(fp)
  sw a0, -36(fp)
  lw a0, -36(fp)
  sw a0, 4(sp)
  call fib
  sw a0, -36(fp)
  lw a0, -28(fp)
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -48(fp)
  lw a0, -48(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L2372
  lw a0, -48(fp)
  negw a0, a0
  sw a0, -48(fp)
  j .L2371
.L2372:
.L2371:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -52(fp)
  lw a0, -52(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L2374
  lw a0, -52(fp)
  negw a0, a0
  sw a0, -52(fp)
  j .L2373
.L2374:
.L2373:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -56(fp)
  lw a0, -56(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L2376
  lw a0, -56(fp)
  negw a0, a0
  sw a0, -56(fp)
  j .L2375
.L2376:
.L2375:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -60(fp)
  lw a0, -60(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L2378
  lw a0, -60(fp)
  negw a0, a0
  sw a0, -60(fp)
  j .L2377
.L2378:
.L2377:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -64(fp)
  lw a0, -64(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L2380
  lw a0, -64(fp)
  negw a0, a0
  sw a0, -64(fp)
  j .L2379
.L2380:
.L2379:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -68(fp)
  lw a0, -68(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L2382
  lw a0, -68(fp)
  negw a0, a0
  sw a0, -68(fp)
  j .L2381
.L2382:
.L2381:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -72(fp)
  lw a0, -72(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L2384
  lw a0, -72(fp)
  negw a0, a0
  sw a0, -72(fp)
  j .L2383
.L2384:
.L2383:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -76(fp)
  lw a0, -76(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L2386
  lw a0, -76(fp)
  negw a0, a0
  sw a0, -76(fp)
  j .L2385
.L2386:
.L2385:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -80(fp)
  lw a0, -80(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L2388
  lw a0, -80(fp)
  negw a0, a0
  sw a0, -80(fp)
  j .L2387
.L2388:
.L2387:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -84(fp)
  lw a0, -84(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L2390
  lw a0, -84(fp)
  negw a0, a0
  sw a0, -84(fp)
  j .L2389
.L2390:
.L2389:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -88(fp)
  lw a0, -88(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L2392
  lw a0, -88(fp)
  negw a0, a0
  sw a0, -88(fp)
  j .L2391
.L2392:
.L2391:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -92(fp)
  lw a0, -92(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L2394
  lw a0, -92(fp)
  negw a0, a0
  sw a0, -92(fp)
  j .L2393
.L2394:
.L2393:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -96(fp)
  lw a0, -96(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L2396
  lw a0, -96(fp)
  negw a0, a0
  sw a0, -96(fp)
  j .L2395
.L2396:
.L2395:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -100(fp)
  lw a0, -100(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L2398
  lw a0, -100(fp)
  negw a0, a0
  sw a0, -100(fp)
  j .L2397
.L2398:
.L2397:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -104(fp)
  lw a0, -104(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L2400
  lw a0, -104(fp)
  negw a0, a0
  sw a0, -104(fp)
  j .L2399
.L2400:
.L2399:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  remw a0, t0, a0
  sw a0, -108(fp)
  lw a0, -108(fp)
  sw a0, -116(fp)
  li a0, 0
  lw t0, -116(fp)
  slt a0, t0, a0
  beqz a0, .L2402
  lw a0, -108(fp)
  negw a0, a0
  sw a0, -108(fp)
  j .L2401
.L2402:
.L2401:
  lw a0, -112(fp)
  sw a0, -116(fp)
  li a0, 2
  lw t0, -116(fp)
  divw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -36(fp)
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -112(fp)
  lw a0, -112(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L2404
  lw a0, -112(fp)
  negw a0, a0
  sw a0, -112(fp)
  j .L2403
.L2404:
.L2403:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -116(fp)
  lw a0, -116(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L2406
  lw a0, -116(fp)
  negw a0, a0
  sw a0, -116(fp)
  j .L2405
.L2406:
.L2405:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -120(fp)
  lw a0, -120(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L2408
  lw a0, -120(fp)
  negw a0, a0
  sw a0, -120(fp)
  j .L2407
.L2408:
.L2407:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -124(fp)
  lw a0, -124(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L2410
  lw a0, -124(fp)
  negw a0, a0
  sw a0, -124(fp)
  j .L2409
.L2410:
.L2409:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -128(fp)
  lw a0, -128(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L2412
  lw a0, -128(fp)
  negw a0, a0
  sw a0, -128(fp)
  j .L2411
.L2412:
.L2411:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -132(fp)
  lw a0, -132(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L2414
  lw a0, -132(fp)
  negw a0, a0
  sw a0, -132(fp)
  j .L2413
.L2414:
.L2413:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -136(fp)
  lw a0, -136(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L2416
  lw a0, -136(fp)
  negw a0, a0
  sw a0, -136(fp)
  j .L2415
.L2416:
.L2415:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -140(fp)
  lw a0, -140(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L2418
  lw a0, -140(fp)
  negw a0, a0
  sw a0, -140(fp)
  j .L2417
.L2418:
.L2417:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -144(fp)
  lw a0, -144(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L2420
  lw a0, -144(fp)
  negw a0, a0
  sw a0, -144(fp)
  j .L2419
.L2420:
.L2419:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -148(fp)
  lw a0, -148(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L2422
  lw a0, -148(fp)
  negw a0, a0
  sw a0, -148(fp)
  j .L2421
.L2422:
.L2421:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -152(fp)
  lw a0, -152(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L2424
  lw a0, -152(fp)
  negw a0, a0
  sw a0, -152(fp)
  j .L2423
.L2424:
.L2423:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -156(fp)
  lw a0, -156(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L2426
  lw a0, -156(fp)
  negw a0, a0
  sw a0, -156(fp)
  j .L2425
.L2426:
.L2425:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -160(fp)
  lw a0, -160(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L2428
  lw a0, -160(fp)
  negw a0, a0
  sw a0, -160(fp)
  j .L2427
.L2428:
.L2427:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -164(fp)
  lw a0, -164(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L2430
  lw a0, -164(fp)
  negw a0, a0
  sw a0, -164(fp)
  j .L2429
.L2430:
.L2429:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -168(fp)
  lw a0, -168(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L2432
  lw a0, -168(fp)
  negw a0, a0
  sw a0, -168(fp)
  j .L2431
.L2432:
.L2431:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  remw a0, t0, a0
  sw a0, -172(fp)
  lw a0, -172(fp)
  sw a0, -180(fp)
  li a0, 0
  lw t0, -180(fp)
  slt a0, t0, a0
  beqz a0, .L2434
  lw a0, -172(fp)
  negw a0, a0
  sw a0, -172(fp)
  j .L2433
.L2434:
.L2433:
  lw a0, -176(fp)
  sw a0, -180(fp)
  li a0, 2
  lw t0, -180(fp)
  divw a0, t0, a0
  sw a0, -176(fp)
  lw a0, -48(fp)
  snez a0, a0
  bnez a0, .L2437
  sw a0, -308(fp)
  lw a0, -112(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2437:
  beqz a0, .L2436
  li a0, 1
  sw a0, -304(fp)
  j .L2435
.L2436:
  li a0, 0
  sw a0, -304(fp)
.L2435:
  lw a0, -48(fp)
  snez a0, a0
  beqz a0, .L2440
  sw a0, -312(fp)
  lw a0, -112(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2440:
  beqz a0, .L2439
  li a0, 1
  sw a0, -308(fp)
  j .L2438
.L2439:
  li a0, 0
  sw a0, -308(fp)
.L2438:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2442
  li a0, 1
  sw a0, -312(fp)
  j .L2441
.L2442:
  li a0, 0
  sw a0, -312(fp)
.L2441:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2445
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2445:
  beqz a0, .L2444
  li a0, 1
  sw a0, -300(fp)
  j .L2443
.L2444:
  li a0, 0
  sw a0, -300(fp)
.L2443:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2448
  sw a0, -308(fp)
  li a0, 0
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2448:
  beqz a0, .L2447
  li a0, 1
  sw a0, -304(fp)
  j .L2446
.L2447:
  li a0, 0
  sw a0, -304(fp)
.L2446:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2451
  sw a0, -312(fp)
  li a0, 0
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2451:
  beqz a0, .L2450
  li a0, 1
  sw a0, -308(fp)
  j .L2449
.L2450:
  li a0, 0
  sw a0, -308(fp)
.L2449:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2453
  li a0, 1
  sw a0, -312(fp)
  j .L2452
.L2453:
  li a0, 0
  sw a0, -312(fp)
.L2452:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2456
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2456:
  beqz a0, .L2455
  li a0, 1
  sw a0, -236(fp)
  j .L2454
.L2455:
  li a0, 0
  sw a0, -236(fp)
.L2454:
  lw a0, -48(fp)
  snez a0, a0
  beqz a0, .L2459
  sw a0, -308(fp)
  lw a0, -112(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2459:
  beqz a0, .L2458
  li a0, 1
  sw a0, -304(fp)
  j .L2457
.L2458:
  li a0, 0
  sw a0, -304(fp)
.L2457:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2462
  sw a0, -312(fp)
  li a0, 0
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2462:
  beqz a0, .L2461
  li a0, 1
  sw a0, -308(fp)
  j .L2460
.L2461:
  li a0, 0
  sw a0, -308(fp)
.L2460:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L2465
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L2465:
  beqz a0, .L2464
  li a0, 1
  sw a0, -176(fp)
  j .L2463
.L2464:
  li a0, 0
  sw a0, -176(fp)
.L2463:
  lw a0, -52(fp)
  snez a0, a0
  bnez a0, .L2468
  sw a0, -308(fp)
  lw a0, -116(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2468:
  beqz a0, .L2467
  li a0, 1
  sw a0, -304(fp)
  j .L2466
.L2467:
  li a0, 0
  sw a0, -304(fp)
.L2466:
  lw a0, -52(fp)
  snez a0, a0
  beqz a0, .L2471
  sw a0, -312(fp)
  lw a0, -116(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2471:
  beqz a0, .L2470
  li a0, 1
  sw a0, -308(fp)
  j .L2469
.L2470:
  li a0, 0
  sw a0, -308(fp)
.L2469:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2473
  li a0, 1
  sw a0, -312(fp)
  j .L2472
.L2473:
  li a0, 0
  sw a0, -312(fp)
.L2472:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2476
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2476:
  beqz a0, .L2475
  li a0, 1
  sw a0, -300(fp)
  j .L2474
.L2475:
  li a0, 0
  sw a0, -300(fp)
.L2474:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2479
  sw a0, -308(fp)
  lw a0, -176(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2479:
  beqz a0, .L2478
  li a0, 1
  sw a0, -304(fp)
  j .L2477
.L2478:
  li a0, 0
  sw a0, -304(fp)
.L2477:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2482
  sw a0, -312(fp)
  lw a0, -176(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2482:
  beqz a0, .L2481
  li a0, 1
  sw a0, -308(fp)
  j .L2480
.L2481:
  li a0, 0
  sw a0, -308(fp)
.L2480:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2484
  li a0, 1
  sw a0, -312(fp)
  j .L2483
.L2484:
  li a0, 0
  sw a0, -312(fp)
.L2483:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2487
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2487:
  beqz a0, .L2486
  li a0, 1
  sw a0, -240(fp)
  j .L2485
.L2486:
  li a0, 0
  sw a0, -240(fp)
.L2485:
  lw a0, -52(fp)
  snez a0, a0
  beqz a0, .L2490
  sw a0, -308(fp)
  lw a0, -116(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2490:
  beqz a0, .L2489
  li a0, 1
  sw a0, -304(fp)
  j .L2488
.L2489:
  li a0, 0
  sw a0, -304(fp)
.L2488:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2493
  sw a0, -312(fp)
  lw a0, -176(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2493:
  beqz a0, .L2492
  li a0, 1
  sw a0, -308(fp)
  j .L2491
.L2492:
  li a0, 0
  sw a0, -308(fp)
.L2491:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L2496
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L2496:
  beqz a0, .L2495
  li a0, 1
  sw a0, -180(fp)
  j .L2494
.L2495:
  li a0, 0
  sw a0, -180(fp)
.L2494:
  lw a0, -56(fp)
  snez a0, a0
  bnez a0, .L2499
  sw a0, -308(fp)
  lw a0, -120(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2499:
  beqz a0, .L2498
  li a0, 1
  sw a0, -304(fp)
  j .L2497
.L2498:
  li a0, 0
  sw a0, -304(fp)
.L2497:
  lw a0, -56(fp)
  snez a0, a0
  beqz a0, .L2502
  sw a0, -312(fp)
  lw a0, -120(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2502:
  beqz a0, .L2501
  li a0, 1
  sw a0, -308(fp)
  j .L2500
.L2501:
  li a0, 0
  sw a0, -308(fp)
.L2500:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2504
  li a0, 1
  sw a0, -312(fp)
  j .L2503
.L2504:
  li a0, 0
  sw a0, -312(fp)
.L2503:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2507
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2507:
  beqz a0, .L2506
  li a0, 1
  sw a0, -300(fp)
  j .L2505
.L2506:
  li a0, 0
  sw a0, -300(fp)
.L2505:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2510
  sw a0, -308(fp)
  lw a0, -180(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2510:
  beqz a0, .L2509
  li a0, 1
  sw a0, -304(fp)
  j .L2508
.L2509:
  li a0, 0
  sw a0, -304(fp)
.L2508:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2513
  sw a0, -312(fp)
  lw a0, -180(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2513:
  beqz a0, .L2512
  li a0, 1
  sw a0, -308(fp)
  j .L2511
.L2512:
  li a0, 0
  sw a0, -308(fp)
.L2511:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2515
  li a0, 1
  sw a0, -312(fp)
  j .L2514
.L2515:
  li a0, 0
  sw a0, -312(fp)
.L2514:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2518
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2518:
  beqz a0, .L2517
  li a0, 1
  sw a0, -244(fp)
  j .L2516
.L2517:
  li a0, 0
  sw a0, -244(fp)
.L2516:
  lw a0, -56(fp)
  snez a0, a0
  beqz a0, .L2521
  sw a0, -308(fp)
  lw a0, -120(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2521:
  beqz a0, .L2520
  li a0, 1
  sw a0, -304(fp)
  j .L2519
.L2520:
  li a0, 0
  sw a0, -304(fp)
.L2519:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2524
  sw a0, -312(fp)
  lw a0, -180(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2524:
  beqz a0, .L2523
  li a0, 1
  sw a0, -308(fp)
  j .L2522
.L2523:
  li a0, 0
  sw a0, -308(fp)
.L2522:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L2527
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L2527:
  beqz a0, .L2526
  li a0, 1
  sw a0, -184(fp)
  j .L2525
.L2526:
  li a0, 0
  sw a0, -184(fp)
.L2525:
  lw a0, -60(fp)
  snez a0, a0
  bnez a0, .L2530
  sw a0, -308(fp)
  lw a0, -124(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2530:
  beqz a0, .L2529
  li a0, 1
  sw a0, -304(fp)
  j .L2528
.L2529:
  li a0, 0
  sw a0, -304(fp)
.L2528:
  lw a0, -60(fp)
  snez a0, a0
  beqz a0, .L2533
  sw a0, -312(fp)
  lw a0, -124(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2533:
  beqz a0, .L2532
  li a0, 1
  sw a0, -308(fp)
  j .L2531
.L2532:
  li a0, 0
  sw a0, -308(fp)
.L2531:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2535
  li a0, 1
  sw a0, -312(fp)
  j .L2534
.L2535:
  li a0, 0
  sw a0, -312(fp)
.L2534:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2538
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2538:
  beqz a0, .L2537
  li a0, 1
  sw a0, -300(fp)
  j .L2536
.L2537:
  li a0, 0
  sw a0, -300(fp)
.L2536:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2541
  sw a0, -308(fp)
  lw a0, -184(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2541:
  beqz a0, .L2540
  li a0, 1
  sw a0, -304(fp)
  j .L2539
.L2540:
  li a0, 0
  sw a0, -304(fp)
.L2539:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2544
  sw a0, -312(fp)
  lw a0, -184(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2544:
  beqz a0, .L2543
  li a0, 1
  sw a0, -308(fp)
  j .L2542
.L2543:
  li a0, 0
  sw a0, -308(fp)
.L2542:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2546
  li a0, 1
  sw a0, -312(fp)
  j .L2545
.L2546:
  li a0, 0
  sw a0, -312(fp)
.L2545:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2549
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2549:
  beqz a0, .L2548
  li a0, 1
  sw a0, -248(fp)
  j .L2547
.L2548:
  li a0, 0
  sw a0, -248(fp)
.L2547:
  lw a0, -60(fp)
  snez a0, a0
  beqz a0, .L2552
  sw a0, -308(fp)
  lw a0, -124(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2552:
  beqz a0, .L2551
  li a0, 1
  sw a0, -304(fp)
  j .L2550
.L2551:
  li a0, 0
  sw a0, -304(fp)
.L2550:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2555
  sw a0, -312(fp)
  lw a0, -184(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2555:
  beqz a0, .L2554
  li a0, 1
  sw a0, -308(fp)
  j .L2553
.L2554:
  li a0, 0
  sw a0, -308(fp)
.L2553:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L2558
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L2558:
  beqz a0, .L2557
  li a0, 1
  sw a0, -188(fp)
  j .L2556
.L2557:
  li a0, 0
  sw a0, -188(fp)
.L2556:
  lw a0, -64(fp)
  snez a0, a0
  bnez a0, .L2561
  sw a0, -308(fp)
  lw a0, -128(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2561:
  beqz a0, .L2560
  li a0, 1
  sw a0, -304(fp)
  j .L2559
.L2560:
  li a0, 0
  sw a0, -304(fp)
.L2559:
  lw a0, -64(fp)
  snez a0, a0
  beqz a0, .L2564
  sw a0, -312(fp)
  lw a0, -128(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2564:
  beqz a0, .L2563
  li a0, 1
  sw a0, -308(fp)
  j .L2562
.L2563:
  li a0, 0
  sw a0, -308(fp)
.L2562:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2566
  li a0, 1
  sw a0, -312(fp)
  j .L2565
.L2566:
  li a0, 0
  sw a0, -312(fp)
.L2565:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2569
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2569:
  beqz a0, .L2568
  li a0, 1
  sw a0, -300(fp)
  j .L2567
.L2568:
  li a0, 0
  sw a0, -300(fp)
.L2567:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2572
  sw a0, -308(fp)
  lw a0, -188(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2572:
  beqz a0, .L2571
  li a0, 1
  sw a0, -304(fp)
  j .L2570
.L2571:
  li a0, 0
  sw a0, -304(fp)
.L2570:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2575
  sw a0, -312(fp)
  lw a0, -188(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2575:
  beqz a0, .L2574
  li a0, 1
  sw a0, -308(fp)
  j .L2573
.L2574:
  li a0, 0
  sw a0, -308(fp)
.L2573:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2577
  li a0, 1
  sw a0, -312(fp)
  j .L2576
.L2577:
  li a0, 0
  sw a0, -312(fp)
.L2576:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2580
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2580:
  beqz a0, .L2579
  li a0, 1
  sw a0, -252(fp)
  j .L2578
.L2579:
  li a0, 0
  sw a0, -252(fp)
.L2578:
  lw a0, -64(fp)
  snez a0, a0
  beqz a0, .L2583
  sw a0, -308(fp)
  lw a0, -128(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2583:
  beqz a0, .L2582
  li a0, 1
  sw a0, -304(fp)
  j .L2581
.L2582:
  li a0, 0
  sw a0, -304(fp)
.L2581:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2586
  sw a0, -312(fp)
  lw a0, -188(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2586:
  beqz a0, .L2585
  li a0, 1
  sw a0, -308(fp)
  j .L2584
.L2585:
  li a0, 0
  sw a0, -308(fp)
.L2584:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L2589
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L2589:
  beqz a0, .L2588
  li a0, 1
  sw a0, -192(fp)
  j .L2587
.L2588:
  li a0, 0
  sw a0, -192(fp)
.L2587:
  lw a0, -68(fp)
  snez a0, a0
  bnez a0, .L2592
  sw a0, -308(fp)
  lw a0, -132(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2592:
  beqz a0, .L2591
  li a0, 1
  sw a0, -304(fp)
  j .L2590
.L2591:
  li a0, 0
  sw a0, -304(fp)
.L2590:
  lw a0, -68(fp)
  snez a0, a0
  beqz a0, .L2595
  sw a0, -312(fp)
  lw a0, -132(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2595:
  beqz a0, .L2594
  li a0, 1
  sw a0, -308(fp)
  j .L2593
.L2594:
  li a0, 0
  sw a0, -308(fp)
.L2593:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2597
  li a0, 1
  sw a0, -312(fp)
  j .L2596
.L2597:
  li a0, 0
  sw a0, -312(fp)
.L2596:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2600
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2600:
  beqz a0, .L2599
  li a0, 1
  sw a0, -300(fp)
  j .L2598
.L2599:
  li a0, 0
  sw a0, -300(fp)
.L2598:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2603
  sw a0, -308(fp)
  lw a0, -192(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2603:
  beqz a0, .L2602
  li a0, 1
  sw a0, -304(fp)
  j .L2601
.L2602:
  li a0, 0
  sw a0, -304(fp)
.L2601:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2606
  sw a0, -312(fp)
  lw a0, -192(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2606:
  beqz a0, .L2605
  li a0, 1
  sw a0, -308(fp)
  j .L2604
.L2605:
  li a0, 0
  sw a0, -308(fp)
.L2604:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2608
  li a0, 1
  sw a0, -312(fp)
  j .L2607
.L2608:
  li a0, 0
  sw a0, -312(fp)
.L2607:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2611
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2611:
  beqz a0, .L2610
  li a0, 1
  sw a0, -256(fp)
  j .L2609
.L2610:
  li a0, 0
  sw a0, -256(fp)
.L2609:
  lw a0, -68(fp)
  snez a0, a0
  beqz a0, .L2614
  sw a0, -308(fp)
  lw a0, -132(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2614:
  beqz a0, .L2613
  li a0, 1
  sw a0, -304(fp)
  j .L2612
.L2613:
  li a0, 0
  sw a0, -304(fp)
.L2612:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2617
  sw a0, -312(fp)
  lw a0, -192(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2617:
  beqz a0, .L2616
  li a0, 1
  sw a0, -308(fp)
  j .L2615
.L2616:
  li a0, 0
  sw a0, -308(fp)
.L2615:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L2620
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L2620:
  beqz a0, .L2619
  li a0, 1
  sw a0, -196(fp)
  j .L2618
.L2619:
  li a0, 0
  sw a0, -196(fp)
.L2618:
  lw a0, -72(fp)
  snez a0, a0
  bnez a0, .L2623
  sw a0, -308(fp)
  lw a0, -136(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2623:
  beqz a0, .L2622
  li a0, 1
  sw a0, -304(fp)
  j .L2621
.L2622:
  li a0, 0
  sw a0, -304(fp)
.L2621:
  lw a0, -72(fp)
  snez a0, a0
  beqz a0, .L2626
  sw a0, -312(fp)
  lw a0, -136(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2626:
  beqz a0, .L2625
  li a0, 1
  sw a0, -308(fp)
  j .L2624
.L2625:
  li a0, 0
  sw a0, -308(fp)
.L2624:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2628
  li a0, 1
  sw a0, -312(fp)
  j .L2627
.L2628:
  li a0, 0
  sw a0, -312(fp)
.L2627:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2631
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2631:
  beqz a0, .L2630
  li a0, 1
  sw a0, -300(fp)
  j .L2629
.L2630:
  li a0, 0
  sw a0, -300(fp)
.L2629:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2634
  sw a0, -308(fp)
  lw a0, -196(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2634:
  beqz a0, .L2633
  li a0, 1
  sw a0, -304(fp)
  j .L2632
.L2633:
  li a0, 0
  sw a0, -304(fp)
.L2632:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2637
  sw a0, -312(fp)
  lw a0, -196(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2637:
  beqz a0, .L2636
  li a0, 1
  sw a0, -308(fp)
  j .L2635
.L2636:
  li a0, 0
  sw a0, -308(fp)
.L2635:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2639
  li a0, 1
  sw a0, -312(fp)
  j .L2638
.L2639:
  li a0, 0
  sw a0, -312(fp)
.L2638:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2642
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2642:
  beqz a0, .L2641
  li a0, 1
  sw a0, -260(fp)
  j .L2640
.L2641:
  li a0, 0
  sw a0, -260(fp)
.L2640:
  lw a0, -72(fp)
  snez a0, a0
  beqz a0, .L2645
  sw a0, -308(fp)
  lw a0, -136(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2645:
  beqz a0, .L2644
  li a0, 1
  sw a0, -304(fp)
  j .L2643
.L2644:
  li a0, 0
  sw a0, -304(fp)
.L2643:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2648
  sw a0, -312(fp)
  lw a0, -196(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2648:
  beqz a0, .L2647
  li a0, 1
  sw a0, -308(fp)
  j .L2646
.L2647:
  li a0, 0
  sw a0, -308(fp)
.L2646:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L2651
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L2651:
  beqz a0, .L2650
  li a0, 1
  sw a0, -200(fp)
  j .L2649
.L2650:
  li a0, 0
  sw a0, -200(fp)
.L2649:
  lw a0, -76(fp)
  snez a0, a0
  bnez a0, .L2654
  sw a0, -308(fp)
  lw a0, -140(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2654:
  beqz a0, .L2653
  li a0, 1
  sw a0, -304(fp)
  j .L2652
.L2653:
  li a0, 0
  sw a0, -304(fp)
.L2652:
  lw a0, -76(fp)
  snez a0, a0
  beqz a0, .L2657
  sw a0, -312(fp)
  lw a0, -140(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2657:
  beqz a0, .L2656
  li a0, 1
  sw a0, -308(fp)
  j .L2655
.L2656:
  li a0, 0
  sw a0, -308(fp)
.L2655:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2659
  li a0, 1
  sw a0, -312(fp)
  j .L2658
.L2659:
  li a0, 0
  sw a0, -312(fp)
.L2658:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2662
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2662:
  beqz a0, .L2661
  li a0, 1
  sw a0, -300(fp)
  j .L2660
.L2661:
  li a0, 0
  sw a0, -300(fp)
.L2660:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2665
  sw a0, -308(fp)
  lw a0, -200(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2665:
  beqz a0, .L2664
  li a0, 1
  sw a0, -304(fp)
  j .L2663
.L2664:
  li a0, 0
  sw a0, -304(fp)
.L2663:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2668
  sw a0, -312(fp)
  lw a0, -200(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2668:
  beqz a0, .L2667
  li a0, 1
  sw a0, -308(fp)
  j .L2666
.L2667:
  li a0, 0
  sw a0, -308(fp)
.L2666:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2670
  li a0, 1
  sw a0, -312(fp)
  j .L2669
.L2670:
  li a0, 0
  sw a0, -312(fp)
.L2669:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2673
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2673:
  beqz a0, .L2672
  li a0, 1
  sw a0, -264(fp)
  j .L2671
.L2672:
  li a0, 0
  sw a0, -264(fp)
.L2671:
  lw a0, -76(fp)
  snez a0, a0
  beqz a0, .L2676
  sw a0, -308(fp)
  lw a0, -140(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2676:
  beqz a0, .L2675
  li a0, 1
  sw a0, -304(fp)
  j .L2674
.L2675:
  li a0, 0
  sw a0, -304(fp)
.L2674:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2679
  sw a0, -312(fp)
  lw a0, -200(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2679:
  beqz a0, .L2678
  li a0, 1
  sw a0, -308(fp)
  j .L2677
.L2678:
  li a0, 0
  sw a0, -308(fp)
.L2677:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L2682
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L2682:
  beqz a0, .L2681
  li a0, 1
  sw a0, -204(fp)
  j .L2680
.L2681:
  li a0, 0
  sw a0, -204(fp)
.L2680:
  lw a0, -80(fp)
  snez a0, a0
  bnez a0, .L2685
  sw a0, -308(fp)
  lw a0, -144(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2685:
  beqz a0, .L2684
  li a0, 1
  sw a0, -304(fp)
  j .L2683
.L2684:
  li a0, 0
  sw a0, -304(fp)
.L2683:
  lw a0, -80(fp)
  snez a0, a0
  beqz a0, .L2688
  sw a0, -312(fp)
  lw a0, -144(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2688:
  beqz a0, .L2687
  li a0, 1
  sw a0, -308(fp)
  j .L2686
.L2687:
  li a0, 0
  sw a0, -308(fp)
.L2686:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2690
  li a0, 1
  sw a0, -312(fp)
  j .L2689
.L2690:
  li a0, 0
  sw a0, -312(fp)
.L2689:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2693
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2693:
  beqz a0, .L2692
  li a0, 1
  sw a0, -300(fp)
  j .L2691
.L2692:
  li a0, 0
  sw a0, -300(fp)
.L2691:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2696
  sw a0, -308(fp)
  lw a0, -204(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2696:
  beqz a0, .L2695
  li a0, 1
  sw a0, -304(fp)
  j .L2694
.L2695:
  li a0, 0
  sw a0, -304(fp)
.L2694:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2699
  sw a0, -312(fp)
  lw a0, -204(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2699:
  beqz a0, .L2698
  li a0, 1
  sw a0, -308(fp)
  j .L2697
.L2698:
  li a0, 0
  sw a0, -308(fp)
.L2697:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2701
  li a0, 1
  sw a0, -312(fp)
  j .L2700
.L2701:
  li a0, 0
  sw a0, -312(fp)
.L2700:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2704
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2704:
  beqz a0, .L2703
  li a0, 1
  sw a0, -268(fp)
  j .L2702
.L2703:
  li a0, 0
  sw a0, -268(fp)
.L2702:
  lw a0, -80(fp)
  snez a0, a0
  beqz a0, .L2707
  sw a0, -308(fp)
  lw a0, -144(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2707:
  beqz a0, .L2706
  li a0, 1
  sw a0, -304(fp)
  j .L2705
.L2706:
  li a0, 0
  sw a0, -304(fp)
.L2705:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2710
  sw a0, -312(fp)
  lw a0, -204(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2710:
  beqz a0, .L2709
  li a0, 1
  sw a0, -308(fp)
  j .L2708
.L2709:
  li a0, 0
  sw a0, -308(fp)
.L2708:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L2713
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L2713:
  beqz a0, .L2712
  li a0, 1
  sw a0, -208(fp)
  j .L2711
.L2712:
  li a0, 0
  sw a0, -208(fp)
.L2711:
  lw a0, -84(fp)
  snez a0, a0
  bnez a0, .L2716
  sw a0, -308(fp)
  lw a0, -148(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2716:
  beqz a0, .L2715
  li a0, 1
  sw a0, -304(fp)
  j .L2714
.L2715:
  li a0, 0
  sw a0, -304(fp)
.L2714:
  lw a0, -84(fp)
  snez a0, a0
  beqz a0, .L2719
  sw a0, -312(fp)
  lw a0, -148(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2719:
  beqz a0, .L2718
  li a0, 1
  sw a0, -308(fp)
  j .L2717
.L2718:
  li a0, 0
  sw a0, -308(fp)
.L2717:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2721
  li a0, 1
  sw a0, -312(fp)
  j .L2720
.L2721:
  li a0, 0
  sw a0, -312(fp)
.L2720:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2724
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2724:
  beqz a0, .L2723
  li a0, 1
  sw a0, -300(fp)
  j .L2722
.L2723:
  li a0, 0
  sw a0, -300(fp)
.L2722:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2727
  sw a0, -308(fp)
  lw a0, -208(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2727:
  beqz a0, .L2726
  li a0, 1
  sw a0, -304(fp)
  j .L2725
.L2726:
  li a0, 0
  sw a0, -304(fp)
.L2725:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2730
  sw a0, -312(fp)
  lw a0, -208(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2730:
  beqz a0, .L2729
  li a0, 1
  sw a0, -308(fp)
  j .L2728
.L2729:
  li a0, 0
  sw a0, -308(fp)
.L2728:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2732
  li a0, 1
  sw a0, -312(fp)
  j .L2731
.L2732:
  li a0, 0
  sw a0, -312(fp)
.L2731:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2735
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2735:
  beqz a0, .L2734
  li a0, 1
  sw a0, -272(fp)
  j .L2733
.L2734:
  li a0, 0
  sw a0, -272(fp)
.L2733:
  lw a0, -84(fp)
  snez a0, a0
  beqz a0, .L2738
  sw a0, -308(fp)
  lw a0, -148(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2738:
  beqz a0, .L2737
  li a0, 1
  sw a0, -304(fp)
  j .L2736
.L2737:
  li a0, 0
  sw a0, -304(fp)
.L2736:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2741
  sw a0, -312(fp)
  lw a0, -208(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2741:
  beqz a0, .L2740
  li a0, 1
  sw a0, -308(fp)
  j .L2739
.L2740:
  li a0, 0
  sw a0, -308(fp)
.L2739:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L2744
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L2744:
  beqz a0, .L2743
  li a0, 1
  sw a0, -212(fp)
  j .L2742
.L2743:
  li a0, 0
  sw a0, -212(fp)
.L2742:
  lw a0, -88(fp)
  snez a0, a0
  bnez a0, .L2747
  sw a0, -308(fp)
  lw a0, -152(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2747:
  beqz a0, .L2746
  li a0, 1
  sw a0, -304(fp)
  j .L2745
.L2746:
  li a0, 0
  sw a0, -304(fp)
.L2745:
  lw a0, -88(fp)
  snez a0, a0
  beqz a0, .L2750
  sw a0, -312(fp)
  lw a0, -152(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2750:
  beqz a0, .L2749
  li a0, 1
  sw a0, -308(fp)
  j .L2748
.L2749:
  li a0, 0
  sw a0, -308(fp)
.L2748:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2752
  li a0, 1
  sw a0, -312(fp)
  j .L2751
.L2752:
  li a0, 0
  sw a0, -312(fp)
.L2751:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2755
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2755:
  beqz a0, .L2754
  li a0, 1
  sw a0, -300(fp)
  j .L2753
.L2754:
  li a0, 0
  sw a0, -300(fp)
.L2753:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2758
  sw a0, -308(fp)
  lw a0, -212(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2758:
  beqz a0, .L2757
  li a0, 1
  sw a0, -304(fp)
  j .L2756
.L2757:
  li a0, 0
  sw a0, -304(fp)
.L2756:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2761
  sw a0, -312(fp)
  lw a0, -212(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2761:
  beqz a0, .L2760
  li a0, 1
  sw a0, -308(fp)
  j .L2759
.L2760:
  li a0, 0
  sw a0, -308(fp)
.L2759:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2763
  li a0, 1
  sw a0, -312(fp)
  j .L2762
.L2763:
  li a0, 0
  sw a0, -312(fp)
.L2762:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2766
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2766:
  beqz a0, .L2765
  li a0, 1
  sw a0, -276(fp)
  j .L2764
.L2765:
  li a0, 0
  sw a0, -276(fp)
.L2764:
  lw a0, -88(fp)
  snez a0, a0
  beqz a0, .L2769
  sw a0, -308(fp)
  lw a0, -152(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2769:
  beqz a0, .L2768
  li a0, 1
  sw a0, -304(fp)
  j .L2767
.L2768:
  li a0, 0
  sw a0, -304(fp)
.L2767:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2772
  sw a0, -312(fp)
  lw a0, -212(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2772:
  beqz a0, .L2771
  li a0, 1
  sw a0, -308(fp)
  j .L2770
.L2771:
  li a0, 0
  sw a0, -308(fp)
.L2770:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L2775
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L2775:
  beqz a0, .L2774
  li a0, 1
  sw a0, -216(fp)
  j .L2773
.L2774:
  li a0, 0
  sw a0, -216(fp)
.L2773:
  lw a0, -92(fp)
  snez a0, a0
  bnez a0, .L2778
  sw a0, -308(fp)
  lw a0, -156(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2778:
  beqz a0, .L2777
  li a0, 1
  sw a0, -304(fp)
  j .L2776
.L2777:
  li a0, 0
  sw a0, -304(fp)
.L2776:
  lw a0, -92(fp)
  snez a0, a0
  beqz a0, .L2781
  sw a0, -312(fp)
  lw a0, -156(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2781:
  beqz a0, .L2780
  li a0, 1
  sw a0, -308(fp)
  j .L2779
.L2780:
  li a0, 0
  sw a0, -308(fp)
.L2779:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2783
  li a0, 1
  sw a0, -312(fp)
  j .L2782
.L2783:
  li a0, 0
  sw a0, -312(fp)
.L2782:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2786
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2786:
  beqz a0, .L2785
  li a0, 1
  sw a0, -300(fp)
  j .L2784
.L2785:
  li a0, 0
  sw a0, -300(fp)
.L2784:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2789
  sw a0, -308(fp)
  lw a0, -216(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2789:
  beqz a0, .L2788
  li a0, 1
  sw a0, -304(fp)
  j .L2787
.L2788:
  li a0, 0
  sw a0, -304(fp)
.L2787:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2792
  sw a0, -312(fp)
  lw a0, -216(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2792:
  beqz a0, .L2791
  li a0, 1
  sw a0, -308(fp)
  j .L2790
.L2791:
  li a0, 0
  sw a0, -308(fp)
.L2790:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2794
  li a0, 1
  sw a0, -312(fp)
  j .L2793
.L2794:
  li a0, 0
  sw a0, -312(fp)
.L2793:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2797
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2797:
  beqz a0, .L2796
  li a0, 1
  sw a0, -280(fp)
  j .L2795
.L2796:
  li a0, 0
  sw a0, -280(fp)
.L2795:
  lw a0, -92(fp)
  snez a0, a0
  beqz a0, .L2800
  sw a0, -308(fp)
  lw a0, -156(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2800:
  beqz a0, .L2799
  li a0, 1
  sw a0, -304(fp)
  j .L2798
.L2799:
  li a0, 0
  sw a0, -304(fp)
.L2798:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2803
  sw a0, -312(fp)
  lw a0, -216(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2803:
  beqz a0, .L2802
  li a0, 1
  sw a0, -308(fp)
  j .L2801
.L2802:
  li a0, 0
  sw a0, -308(fp)
.L2801:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L2806
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L2806:
  beqz a0, .L2805
  li a0, 1
  sw a0, -220(fp)
  j .L2804
.L2805:
  li a0, 0
  sw a0, -220(fp)
.L2804:
  lw a0, -96(fp)
  snez a0, a0
  bnez a0, .L2809
  sw a0, -308(fp)
  lw a0, -160(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2809:
  beqz a0, .L2808
  li a0, 1
  sw a0, -304(fp)
  j .L2807
.L2808:
  li a0, 0
  sw a0, -304(fp)
.L2807:
  lw a0, -96(fp)
  snez a0, a0
  beqz a0, .L2812
  sw a0, -312(fp)
  lw a0, -160(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2812:
  beqz a0, .L2811
  li a0, 1
  sw a0, -308(fp)
  j .L2810
.L2811:
  li a0, 0
  sw a0, -308(fp)
.L2810:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2814
  li a0, 1
  sw a0, -312(fp)
  j .L2813
.L2814:
  li a0, 0
  sw a0, -312(fp)
.L2813:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2817
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2817:
  beqz a0, .L2816
  li a0, 1
  sw a0, -300(fp)
  j .L2815
.L2816:
  li a0, 0
  sw a0, -300(fp)
.L2815:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2820
  sw a0, -308(fp)
  lw a0, -220(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2820:
  beqz a0, .L2819
  li a0, 1
  sw a0, -304(fp)
  j .L2818
.L2819:
  li a0, 0
  sw a0, -304(fp)
.L2818:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2823
  sw a0, -312(fp)
  lw a0, -220(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2823:
  beqz a0, .L2822
  li a0, 1
  sw a0, -308(fp)
  j .L2821
.L2822:
  li a0, 0
  sw a0, -308(fp)
.L2821:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2825
  li a0, 1
  sw a0, -312(fp)
  j .L2824
.L2825:
  li a0, 0
  sw a0, -312(fp)
.L2824:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2828
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2828:
  beqz a0, .L2827
  li a0, 1
  sw a0, -284(fp)
  j .L2826
.L2827:
  li a0, 0
  sw a0, -284(fp)
.L2826:
  lw a0, -96(fp)
  snez a0, a0
  beqz a0, .L2831
  sw a0, -308(fp)
  lw a0, -160(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2831:
  beqz a0, .L2830
  li a0, 1
  sw a0, -304(fp)
  j .L2829
.L2830:
  li a0, 0
  sw a0, -304(fp)
.L2829:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2834
  sw a0, -312(fp)
  lw a0, -220(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2834:
  beqz a0, .L2833
  li a0, 1
  sw a0, -308(fp)
  j .L2832
.L2833:
  li a0, 0
  sw a0, -308(fp)
.L2832:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L2837
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L2837:
  beqz a0, .L2836
  li a0, 1
  sw a0, -224(fp)
  j .L2835
.L2836:
  li a0, 0
  sw a0, -224(fp)
.L2835:
  lw a0, -100(fp)
  snez a0, a0
  bnez a0, .L2840
  sw a0, -308(fp)
  lw a0, -164(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2840:
  beqz a0, .L2839
  li a0, 1
  sw a0, -304(fp)
  j .L2838
.L2839:
  li a0, 0
  sw a0, -304(fp)
.L2838:
  lw a0, -100(fp)
  snez a0, a0
  beqz a0, .L2843
  sw a0, -312(fp)
  lw a0, -164(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2843:
  beqz a0, .L2842
  li a0, 1
  sw a0, -308(fp)
  j .L2841
.L2842:
  li a0, 0
  sw a0, -308(fp)
.L2841:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2845
  li a0, 1
  sw a0, -312(fp)
  j .L2844
.L2845:
  li a0, 0
  sw a0, -312(fp)
.L2844:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2848
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2848:
  beqz a0, .L2847
  li a0, 1
  sw a0, -300(fp)
  j .L2846
.L2847:
  li a0, 0
  sw a0, -300(fp)
.L2846:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2851
  sw a0, -308(fp)
  lw a0, -224(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2851:
  beqz a0, .L2850
  li a0, 1
  sw a0, -304(fp)
  j .L2849
.L2850:
  li a0, 0
  sw a0, -304(fp)
.L2849:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2854
  sw a0, -312(fp)
  lw a0, -224(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2854:
  beqz a0, .L2853
  li a0, 1
  sw a0, -308(fp)
  j .L2852
.L2853:
  li a0, 0
  sw a0, -308(fp)
.L2852:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2856
  li a0, 1
  sw a0, -312(fp)
  j .L2855
.L2856:
  li a0, 0
  sw a0, -312(fp)
.L2855:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2859
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2859:
  beqz a0, .L2858
  li a0, 1
  sw a0, -288(fp)
  j .L2857
.L2858:
  li a0, 0
  sw a0, -288(fp)
.L2857:
  lw a0, -100(fp)
  snez a0, a0
  beqz a0, .L2862
  sw a0, -308(fp)
  lw a0, -164(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2862:
  beqz a0, .L2861
  li a0, 1
  sw a0, -304(fp)
  j .L2860
.L2861:
  li a0, 0
  sw a0, -304(fp)
.L2860:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2865
  sw a0, -312(fp)
  lw a0, -224(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2865:
  beqz a0, .L2864
  li a0, 1
  sw a0, -308(fp)
  j .L2863
.L2864:
  li a0, 0
  sw a0, -308(fp)
.L2863:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L2868
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L2868:
  beqz a0, .L2867
  li a0, 1
  sw a0, -228(fp)
  j .L2866
.L2867:
  li a0, 0
  sw a0, -228(fp)
.L2866:
  lw a0, -104(fp)
  snez a0, a0
  bnez a0, .L2871
  sw a0, -308(fp)
  lw a0, -168(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2871:
  beqz a0, .L2870
  li a0, 1
  sw a0, -304(fp)
  j .L2869
.L2870:
  li a0, 0
  sw a0, -304(fp)
.L2869:
  lw a0, -104(fp)
  snez a0, a0
  beqz a0, .L2874
  sw a0, -312(fp)
  lw a0, -168(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2874:
  beqz a0, .L2873
  li a0, 1
  sw a0, -308(fp)
  j .L2872
.L2873:
  li a0, 0
  sw a0, -308(fp)
.L2872:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2876
  li a0, 1
  sw a0, -312(fp)
  j .L2875
.L2876:
  li a0, 0
  sw a0, -312(fp)
.L2875:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2879
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2879:
  beqz a0, .L2878
  li a0, 1
  sw a0, -300(fp)
  j .L2877
.L2878:
  li a0, 0
  sw a0, -300(fp)
.L2877:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2882
  sw a0, -308(fp)
  lw a0, -228(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2882:
  beqz a0, .L2881
  li a0, 1
  sw a0, -304(fp)
  j .L2880
.L2881:
  li a0, 0
  sw a0, -304(fp)
.L2880:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2885
  sw a0, -312(fp)
  lw a0, -228(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2885:
  beqz a0, .L2884
  li a0, 1
  sw a0, -308(fp)
  j .L2883
.L2884:
  li a0, 0
  sw a0, -308(fp)
.L2883:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2887
  li a0, 1
  sw a0, -312(fp)
  j .L2886
.L2887:
  li a0, 0
  sw a0, -312(fp)
.L2886:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2890
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2890:
  beqz a0, .L2889
  li a0, 1
  sw a0, -292(fp)
  j .L2888
.L2889:
  li a0, 0
  sw a0, -292(fp)
.L2888:
  lw a0, -104(fp)
  snez a0, a0
  beqz a0, .L2893
  sw a0, -308(fp)
  lw a0, -168(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2893:
  beqz a0, .L2892
  li a0, 1
  sw a0, -304(fp)
  j .L2891
.L2892:
  li a0, 0
  sw a0, -304(fp)
.L2891:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2896
  sw a0, -312(fp)
  lw a0, -228(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2896:
  beqz a0, .L2895
  li a0, 1
  sw a0, -308(fp)
  j .L2894
.L2895:
  li a0, 0
  sw a0, -308(fp)
.L2894:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L2899
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L2899:
  beqz a0, .L2898
  li a0, 1
  sw a0, -232(fp)
  j .L2897
.L2898:
  li a0, 0
  sw a0, -232(fp)
.L2897:
  lw a0, -108(fp)
  snez a0, a0
  bnez a0, .L2902
  sw a0, -308(fp)
  lw a0, -172(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2902:
  beqz a0, .L2901
  li a0, 1
  sw a0, -304(fp)
  j .L2900
.L2901:
  li a0, 0
  sw a0, -304(fp)
.L2900:
  lw a0, -108(fp)
  snez a0, a0
  beqz a0, .L2905
  sw a0, -312(fp)
  lw a0, -172(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2905:
  beqz a0, .L2904
  li a0, 1
  sw a0, -308(fp)
  j .L2903
.L2904:
  li a0, 0
  sw a0, -308(fp)
.L2903:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2907
  li a0, 1
  sw a0, -312(fp)
  j .L2906
.L2907:
  li a0, 0
  sw a0, -312(fp)
.L2906:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2910
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2910:
  beqz a0, .L2909
  li a0, 1
  sw a0, -300(fp)
  j .L2908
.L2909:
  li a0, 0
  sw a0, -300(fp)
.L2908:
  lw a0, -300(fp)
  snez a0, a0
  bnez a0, .L2913
  sw a0, -308(fp)
  lw a0, -232(fp)
  snez a0, a0
  lw t0, -308(fp)
  or a0, t0, a0
.L2913:
  beqz a0, .L2912
  li a0, 1
  sw a0, -304(fp)
  j .L2911
.L2912:
  li a0, 0
  sw a0, -304(fp)
.L2911:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2916
  sw a0, -312(fp)
  lw a0, -232(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2916:
  beqz a0, .L2915
  li a0, 1
  sw a0, -308(fp)
  j .L2914
.L2915:
  li a0, 0
  sw a0, -308(fp)
.L2914:
  lw a0, -308(fp)
  seqz a0, a0
  beqz a0, .L2918
  li a0, 1
  sw a0, -312(fp)
  j .L2917
.L2918:
  li a0, 0
  sw a0, -312(fp)
.L2917:
  lw a0, -304(fp)
  snez a0, a0
  beqz a0, .L2921
  sw a0, -316(fp)
  lw a0, -312(fp)
  snez a0, a0
  lw t0, -316(fp)
  and a0, t0, a0
.L2921:
  beqz a0, .L2920
  li a0, 1
  sw a0, -296(fp)
  j .L2919
.L2920:
  li a0, 0
  sw a0, -296(fp)
.L2919:
  lw a0, -108(fp)
  snez a0, a0
  beqz a0, .L2924
  sw a0, -308(fp)
  lw a0, -172(fp)
  snez a0, a0
  lw t0, -308(fp)
  and a0, t0, a0
.L2924:
  beqz a0, .L2923
  li a0, 1
  sw a0, -304(fp)
  j .L2922
.L2923:
  li a0, 0
  sw a0, -304(fp)
.L2922:
  lw a0, -300(fp)
  snez a0, a0
  beqz a0, .L2927
  sw a0, -312(fp)
  lw a0, -232(fp)
  snez a0, a0
  lw t0, -312(fp)
  and a0, t0, a0
.L2927:
  beqz a0, .L2926
  li a0, 1
  sw a0, -308(fp)
  j .L2925
.L2926:
  li a0, 0
  sw a0, -308(fp)
.L2925:
  lw a0, -304(fp)
  snez a0, a0
  bnez a0, .L2930
  sw a0, -312(fp)
  lw a0, -308(fp)
  snez a0, a0
  lw t0, -312(fp)
  or a0, t0, a0
.L2930:
  beqz a0, .L2929
  li a0, 1
  sw a0, -44(fp)
  j .L2928
.L2929:
  li a0, 0
  sw a0, -44(fp)
.L2928:
  li a0, 0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -296(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -292(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -288(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -284(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -280(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -276(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -272(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -268(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -264(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -260(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -256(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -252(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -248(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -244(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -240(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  sw a0, -300(fp)
  li a0, 2
  lw t0, -300(fp)
  mulw a0, t0, a0
  sw a0, -300(fp)
  lw a0, -236(fp)
  lw t0, -300(fp)
  addw a0, t0, a0
  sw a0, -40(fp)
  lw a0, -40(fp)
  j .L0
  mv a0, zero
.L0:
  ld ra, 328(sp)
  ld fp, 320(sp)
  addi sp, sp, 336
  ret
  .size fib, .-fib
  .align 1
  .globl main
  .text
  .type main, @function
main:
  addi sp, sp, -64
  sd ra, 56(sp)
  sd fp, 48(sp)
  addi fp, sp, 64
  li a0, 1
  sw a0, -24(fp)
.L2932:
  lw a0, -24(fp)
  sw a0, -28(fp)
  li a0, 20
  lw t0, -28(fp)
  slt a0, a0, t0
  xori a0, a0, 1
  beqz a0, .L2933
  li a0, 102
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, 4(sp)
  call putch
  li a0, 105
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, 4(sp)
  call putch
  li a0, 98
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, 4(sp)
  call putch
  li a0, 40
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, 4(sp)
  call putch
  lw a0, -24(fp)
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, 4(sp)
  call putint
  li a0, 41
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, 4(sp)
  call putch
  li a0, 32
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, 4(sp)
  call putch
  li a0, 61
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, 4(sp)
  call putch
  li a0, 32
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, 4(sp)
  call putch
  lw a0, -24(fp)
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, 4(sp)
  call fib
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, 4(sp)
  call putint
  li a0, 10
  sw a0, -28(fp)
  lw a0, -28(fp)
  sw a0, 4(sp)
  call putch
  lw a0, -24(fp)
  sw a0, -28(fp)
  li a0, 1
  lw t0, -28(fp)
  addw a0, t0, a0
  sw a0, -24(fp)
  j .L2932
.L2933:
  li a0, 0
  j .L2931
  mv a0, zero
.L2931:
  ld ra, 56(sp)
  ld fp, 48(sp)
  addi sp, sp, 64
  ret
  .size main, .-main

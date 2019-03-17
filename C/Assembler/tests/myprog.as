; This is a tester input file written by the student.
; Write your own assembly language source program.
; The program should contain at least 10 insructions, 5 labels, 5 directives
; No errors: the assembler should generate successfully all output files:
;     myprog.ob, myprog.ext, myprog.ent
.extern ex1
.extern ex2
.extern ex3
.extern ex4
.extern ex5

m1: .mat [1][4] 1, 3, 3, 7
m2: .mat [2][2] 127, 0, 0, 1

; they ask... what's the meaning of life?
life: .string "see: man 2 ptrace"

; well than, that certainly gives some answers. How about the questions?
questions: .string "I see you never used valgrind"

; fair enough. However, I still think something is missing
howmanylabels: .string "try leaving your vm and get some sunlight"

; I can't leave my vm! That's the whole point of a vm - 
nomorevm: .string "have you tried rawhammer?"

; I'll go fetch it right away, sir

.entry start

start: not r1
clr r2
hey: lea hey, r2
lea ex2, r2
lea ex3, r2
lea ex3, r4
inc r1
inc hey
mov r1,r2
mov hey,hey
mov #2,hey
mov r2,hey
cmp r2,r6

add r1,r2
add #2,hey
add #2,r2
add hey,r1
add questions,life
add hey,hey
add m1[r1][r2],m1[r1][r2]
add m1[r1][r2],m2[r2][r3]
add ex1,m2[r2][r3]

.extern ex6

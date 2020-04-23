#lang racket
(define (my_count pred lst)
  (cond
    ((null? lst) 0)
    (( pred (car lst)) (+ 1 (my_count pred (cdr lst))))
    (else (my_count pred (cdr lst)))))
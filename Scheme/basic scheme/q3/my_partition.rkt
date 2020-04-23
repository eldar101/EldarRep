#lang racket
(define (my_partition pred L)
    (if (null? L)
        '()
        (list (first pred L) (second pred L))))

(define (first pred L)
    (cond ((null? L) null)
        ((pred (car L)) (cons  (car L) (first pred (cdr L))))
        (else (first pred (cdr L)))))

(define (second pred L)
      (cond ((null? L) null)
        (( not (pred (car L))) (cons  (car L) (second pred (cdr L))))
        (else (second pred (cdr L)))))
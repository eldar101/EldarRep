#lang racket
(define (my_count_foldr ps xs)
  (length (foldr (lambda (p y)
           (if (ps p)
               (cons p y)
               y))
         '()
         xs))
  )

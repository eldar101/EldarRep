#lang racket
(provide my_flatten)

(define (my-append x y)
  (append x y))

(define (my_flatten x)
  (cond
    [(pair? x)
     (my-append (my_flatten (car x))
                (my_flatten (cdr x)))]
    [(null? x)
     '()]
    [else
     (list x)]))

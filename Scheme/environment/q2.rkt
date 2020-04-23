#lang eopl

(define-datatype prefix-exp prefix-exp? ; definition
  (const-exp
   (num integer?)) ; is integer?
  (diff-exp
   (operand1 prefix-exp?) ; check both
   (operand2 prefix-exp?)))

(define (parse-prefix ls)
  
  (define (parser-prefix ls) ;helper function
  (cond ; we verify if an integer or a minus sign
    [(integer? (car ls)) (cons (const-exp (car ls)) (cdr ls))]
    [(equal? '- (car ls)) (begin ;2 operands
     (define operand1 (parser-prefix (cdr ls)))
     (define operand2 (parser-prefix (cdr operand1)))
     (cons (diff-exp (car operand1) (car operand2)) (cdr operand2)))]
    [else (eopl:error "Error: not a valid prefix expression")]))
  
  (if (list? ls) ; Verify input is a list
  (car (parser-prefix ls))
  (eopl:error "Error:parse-prefix should get a list")))

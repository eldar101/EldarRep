#lang racket

(define (number->sequence x) ; sequence with number only
  (if (integer? x)
      (list x (list) (list))
      (error "No integer given")))

(define (seq? seq)
  (and
   (list? seq)
   (= 3 (length seq))
   (integer? (car seq))
   (list? (cadr seq))
   (list? (caddr seq))
   ((lambda (ls) 
      (if (empty? ls) #t
                     (foldr (lambda (num bool) (and bool (integer? num))) #t ls))) ;we check all integers
     (append (cadr seq) (caddr seq))))) ;we run the check for elements in both lists

(define (move-to-left seq); in the new list, the current element is the previous one from input
  (if (and (seq? seq) (> (length (cadr seq)) 0)) ;we check input
      (list (car (cadr seq)) (cdr (cadr seq)) (cons (car seq) (caddr seq)))
      (error "There must be elements it the first list")))
  
(define (move-to-right seq) ;check input, current element would be next one from the input list
  (if (and (seq? seq) (> (length (caddr seq)) 0)) ; exists
      (list (car (caddr seq)) (cons (car seq) (cadr seq)) (cdr (caddr seq)))
      (error "There must be elements in the second list")))

(define (insert-to-left num seq); add num before current number in list
  (if (and (seq? seq) (integer? num)) ;we check input
      (list (car seq) (cons num (cadr seq))(caddr seq))
      (error "Incorrect input")))

(define (insert-to-right num seq); add num after current number in list
  (if (and (seq? seq) (integer? num)) ;we check input
      (list (car seq) (cadr seq) (cons num (caddr seq)))
      (error "Incorrect input")))

(define (current-element seq); we check current element
  (if (seq? seq) ;we check input
      (car seq)
      (error "Incorrect input")))

(define (at-left-end? seq)
  (if (seq? seq) ;Wecheck input
      (empty? (cadr seq))
      (error "Incorrect input")))

(define (at-right-end? seq)
  (if (seq? seq) ;We check input
      (empty? (caddr seq))
      (error "Incorrect input")))

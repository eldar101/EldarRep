#lang eopl

(define empty-env
  (lambda ()
    (list 
     (lambda (search-var) ;searchbinding
      (eopl:error "No binding found: " search-var))  ;Didn't find binding
     (lambda (search-var) #f)))) ;has binding

(define extend-env
  (lambda (saved-var saved-val saved-env)
    (list 
     (lambda (search-var) ;find binding
      (if (eqv? search-var saved-var)
          saved-val
          (apply-env saved-env search-var)))
      (lambda (search-var) ;has binding
        (if (eqv? search-var saved-var)
            #t ; true find
            ((cadr saved-env) search-var))))))

(define apply-env ;Now use the first function
  (lambda (env search-var)
    ((car env) search-var)))

(define (has-binding? env search-var) ;Now use the second function
  ((cadr env) search-var))
      

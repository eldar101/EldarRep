Object subclass: #Employee
   "Employee numbers must be between 1000 and 1999."
   "This is a corporate regulation."
      ((anEmpNum >= 1000) and: [anEmpNum <= 1999])
         ifTrue: [
            empNum := anEmpNum.
         ]
         ifFalse: [
            self error: 'Invalid employee number'
         ]
! !
      empName := aName.
! !
      empSalary := aSalary.
! !
      ^empNum
! !
      ^empName
! !
      ^empSalary.
! !
      Transcript show: ('Employee name ', empName);cr .
      Transcript show: ('Employee number: ', empNum asString);cr .
       Transcript show:  ('Salary:          ', self slary asString);cr.
      Transcript show:  'Skills:';cr.
                empSkills do: [:x | Transcript show: ('   ', x asString);cr ].

     
! !
      empName := 'John Dow'.
      empNum := 0.
      empSkills := Set new
      empSkills add: aSkill
! !
      ^(empSkills occurrencesOf: aSkill) = 1
! !
      employees do: aBlock.
! !
      (anEmployee isKindOf: Employee)
           ifTrue: [
            employees
               at: (anEmployee empNum)
               put: anEmployee
         ifFalse: [
            self error: 'You can add employees only!!'
         ]
! !
      | empsWithSkill |
      empsWithSkill := Set new.
      employees do: [
          :emp |
          (emp hasSkill: aSkill)
             ifTrue: [empsWithSkill add: emp]].
      ^empsWithSkill.
! !
      employees := Dictionary new! !
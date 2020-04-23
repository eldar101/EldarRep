Object subclass: #Employee	instanceVariableNames: 'empName empNum empSalary empSkills'	classVariableNames: ''	poolDictionaries: ''	category: 'Employee-Example'!!Employee methodsFor: 'setting' stamp: 'rm 9/10/2014 15:31'!empNum: anEmpNum		
   "Employee numbers must be between 1000 and 1999."
   "This is a corporate regulation."
      ((anEmpNum >= 1000) and: [anEmpNum <= 1999])
         ifTrue: [
            empNum := anEmpNum.
         ]
         ifFalse: [
            self error: 'Invalid employee number'
         ]
! !!Employee methodsFor: 'setting' stamp: 'kl 10/16/2008 09:42'!name: aName
      empName := aName.
! !!Employee methodsFor: 'setting' stamp: 'kl 10/16/2008 11:01'!salary: aSalary
      empSalary := aSalary.
! !!Employee methodsFor: 'getting' stamp: 'kl 10/16/2008 09:42'!empNum
      ^empNum
! !!Employee methodsFor: 'getting' stamp: 'kl 10/16/2008 09:42'!name
      ^empName
! !!Employee methodsFor: 'getting' stamp: 'kl 10/16/2008 09:43'!salary
      ^empSalary.
! !!Employee methodsFor: 'printing' stamp: 'kl 10/16/2008 11:00'!print
      Transcript show: ('Employee name ', empName);cr .
      Transcript show: ('Employee number: ', empNum asString);cr .
       Transcript show:  ('Salary:          ', self slary asString);cr.
      Transcript show:  'Skills:';cr.
                empSkills do: [:x | Transcript show: ('   ', x asString);cr ].

     
! !!Employee methodsFor: 'initialize-release' stamp: 'rm 9/7/2014 14:15'!initialize
      empName := 'John Dow'.
      empNum := 0.
      empSkills := Set new! !!Employee methodsFor: 'adding' stamp: 'rm 9/7/2014 14:16'!addSkill: aSkill
      empSkills add: aSkill
! !!Employee methodsFor: 'testing' stamp: 'rm 9/7/2014 14:19'!hasSkill: aSkill
      ^(empSkills occurrencesOf: aSkill) = 1
! !Object subclass: #EmployeeDataBase	instanceVariableNames: 'employees'	classVariableNames: ''	poolDictionaries: ''	category: 'Employee-Example'!!EmployeeDataBase methodsFor: 'enumerating' stamp: 'kl 10/16/2008 09:45'!do: aBlock
      employees do: aBlock.
! !!EmployeeDataBase methodsFor: 'adding' stamp: 'rm 9/10/2014 15:31'!add: anEmployee
      (anEmployee isKindOf: Employee)
           ifTrue: [
            employees
               at: (anEmployee empNum)
               put: anEmployee         ]
         ifFalse: [
            self error: 'You can add employees only!!'
         ]
! !!EmployeeDataBase methodsFor: 'testing' stamp: 'ew 3/12/2020 16:21'!countEmployees| sum |sum := 0.(employees isEmpty)           ifTrue: [           ^(self count = 0)         ]         ifFalse: [            employees do: [ :e | sum := sum + 1].		^(sum = self count)         ]! !!EmployeeDataBase methodsFor: 'testing' stamp: 'kl 10/16/2008 09:45'!findSkill: aSkill
      | empsWithSkill |
      empsWithSkill := Set new.
      employees do: [
          :emp |
          (emp hasSkill: aSkill)
             ifTrue: [empsWithSkill add: emp]].
      ^empsWithSkill.
! !!EmployeeDataBase methodsFor: 'initialize-release' stamp: 'kl 10/16/2008 11:56'!initialize
      employees := Dictionary new! !!EmployeeDataBase methodsFor: 'counting' stamp: 'ew 3/12/2020 15:40'!count	^employees size ! !
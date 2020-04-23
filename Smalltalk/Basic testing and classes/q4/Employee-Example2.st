Object subclass: #Employee	instanceVariableNames: 'empName empNum empSkills'	classVariableNames: ''	poolDictionaries: ''	category: 'Employee-Example2'!!Employee methodsFor: 'setting' stamp: 'rm 9/10/2014 15:18'!empNum: anEmpNum		
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
      self subclassResponsibility.
! !!Employee methodsFor: 'getting' stamp: 'kl 10/16/2008 09:42'!empNum
      ^empNum
! !!Employee methodsFor: 'getting' stamp: 'kl 10/16/2008 09:42'!name
      ^empName
! !!Employee methodsFor: 'getting' stamp: 'kl 10/16/2008 09:43'!salary
      self subclassResponsibility.
! !!Employee methodsFor: 'printing' stamp: 'kl 10/16/2008 11:00'!print
      Transcript show: ('Employee name ', empName);cr .
      Transcript show: ('Employee number: ', empNum asString);cr .
       Transcript show:  ('Salary:          ', self slary asString);cr.
      Transcript show:  'Skills:';cr.
                empSkills do: [:x | Transcript show: ('   ', x asString);cr ].

     
! !!Employee methodsFor: 'initialize-release' stamp: 'rm 9/10/2014 15:20'!initialize
      empName := 'JD'.
      empNum := 0.
      empSkills := Set new! !!Employee methodsFor: 'adding' stamp: 'rm 9/10/2014 15:15'!addSkill: aSkill
      empSkills add: aSkill! !!Employee methodsFor: 'testing' stamp: 'kl 10/16/2008 09:44'!hasSkill: aSkill
      ^(empSkills occurrencesOf: aSkill) = 1
! !!Employee methodsFor: 'testing' stamp: 'ew 3/12/2020 18:05'!isHourly^(self isMemberOf: HourlyEmployee)! !!Employee methodsFor: 'testing' stamp: 'ew 3/12/2020 18:06'!isMonthly^(self isMemberOf: MonthlyEmployee )! !Object subclass: #EmployeeDataBase	instanceVariableNames: 'employees'	classVariableNames: ''	poolDictionaries: ''	category: 'Employee-Example2'!!EmployeeDataBase methodsFor: 'enumerating' stamp: 'rm 9/10/2014 15:24'!do: aBlock
      employees do: aBlock
! !!EmployeeDataBase methodsFor: 'adding' stamp: 'rm 9/10/2014 15:24'!add: anEmployee
      (anEmployee isKindOf: Employee)
           ifTrue: [
            employees
               at: (anEmployee empNum)
               put: anEmployee
        ]
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
      employees := Dictionary new! !!EmployeeDataBase methodsFor: 'counting' stamp: 'ew 3/12/2020 15:40'!count	^employees size ! !Employee subclass: #HourlyEmployee	instanceVariableNames: 'hourlyRate numberOfHours'	classVariableNames: ''	poolDictionaries: ''	category: 'Employee-Example2'!!HourlyEmployee methodsFor: 'as yet unclassified' stamp: 'kl 10/21/2008 16:08'!addHours: numHours
      numberOfHours := numberOfHours + numHours

! !!HourlyEmployee methodsFor: 'as yet unclassified' stamp: 'kl 10/21/2008 16:08'!rate: aRate
      hourlyRate := aRate
! !!HourlyEmployee methodsFor: 'as yet unclassified' stamp: 'kl 10/21/2008 16:08'!setHours: numHours
      numberOfHours := numHours
! !!HourlyEmployee methodsFor: 'initialize-release' stamp: 'rm 9/10/2014 15:21'!initialize
      super initialize.
      hourlyRate := 0.
      numberOfHours := 0! !!HourlyEmployee methodsFor: 'getting' stamp: 'kl 10/21/2008 16:07'!salary
      ^hourlyRate * numberOfHours
! !Employee subclass: #MonthlyEmployee	instanceVariableNames: 'monthlySalary'	classVariableNames: ''	poolDictionaries: ''	category: 'Employee-Example2'!!MonthlyEmployee methodsFor: 'initialize-release' stamp: 'rm 9/10/2014 15:23'!initialize
   super initialize.
   monthlySalary := 0! !!MonthlyEmployee methodsFor: 'getting' stamp: 'kl 10/21/2008 16:01'!salary
      ^monthlySalary
! !!MonthlyEmployee methodsFor: 'setting' stamp: 'kl 10/21/2008 16:01'!salary: aSalary
      monthlySalary := aSalary.
! !
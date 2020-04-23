ServiceProvider subclass: #Targil
	instanceVariableNames: ''
	classVariableNames: ''
	poolDictionaries: ''
	category: 'Mmn11_2'!

!Targil methodsFor: 'testing' stamp: 'rm 10/12/2015 23:43'!
doSomething
	|i|
	i:=100.
	[i>0] whileTrue: 
		[Transcript show: 'something';show:i ;cr.
		i := i+1]
! !
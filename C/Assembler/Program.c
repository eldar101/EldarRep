/*
C lab 20465 Maman 14 project.
Creators: Eldar Weiss and Gil Rozental.
Date : 18/08/2017.
This program creates an imaginary assembler that reads a file and creates three AS files for externals, internals and "words" interpented as, 
in base-4Odd ,with all details explained throught the files.
--------------------------------------------------------------------------------------------------------------------------------------------*/

#include <stdio.h>
#include <stdlib.h>
#include "Assembler.h"
int main(int argc, char** argv)
{
	if (argc <= 1)
	{
		printf("Usage: %s [file1] [file2] [...]\n", argv[0]);
		return 1;
	}

	AssembleFiles(argv + 1, argc - 1);

	return 0;
}
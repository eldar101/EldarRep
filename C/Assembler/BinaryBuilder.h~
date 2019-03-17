#ifndef BinaryBuilderHeader
#define BinaryBuilderHeader

#include "Common.h"
#include "ProgramDefinitions.h"


/*The maximal amount of parameters for an opcodeNumber*/
#define MAX_PARAMETERS_LENGTH 3

/*Max parameters size + 1 is the max action statement size */
#define MAX_ACTION_SIZE (MAX_PARAMETERS_LENGTH + 1)

/*RepreSents the first word in each action statement*/
typedef struct sOpcodeDescribed {
	enumAddressing addressingDest;
	enumAddressing addressingSrc;
	int opcodeNumber;
	int parametersCount; /*How many Parameters this opcodeNumber has*/
} sOpcodeDescribed;

typedef enum { Absolute = 0,
External = 1,
Relocatable = 2 } eERA;

typedef struct sParameterOfOpcode{
	eERA ERA;				
	unsigned int value;
} sParameterOfOpcode;

/*Numerically represents an action statement*/
typedef struct sByteCode{
	sOpcodeDescribed opcodeNumber;
	sParameterOfOpcode parameters[MAX_PARAMETERS_LENGTH];
	int length; /*The length of parameters*/
} sByteCode;

/*Creates the actual words to be rendered; Returns length or -1 on failure*/
int generateWords(sOpcode statement, unsigned int *fill);

/*Calculates the length of the statement*/
int calculateLength(sProgramLine statement);

/*Forces the usage of twos complement for negative values - assumes 15bit machine*/
unsigned int twosComplementFull(int value);

/*Forces the usage of twos complement for negative values - assumes 1bit machine*/
unsigned int twosComplement(int value);

/*output must be of size minSize atleast
minSize will be implemented by adding zeros*/
void convertNumberToStringBase4weird(unsigned int value, int minSize, char *output);

/*Just generates the string in base 4*/
void convertNumberBase4(unsigned int value, char *output);

#endif


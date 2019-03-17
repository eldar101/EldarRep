#ifndef ConstantsHeader
#define ConstantsHeader
#include "ProgramDefinitions.h"

/*Legal addressing modes for opcodeNumber*/
typedef struct sAddressingModesLegality{
	/*Addressing modes are: Addressing_IMMEDIATE = 0, Addressing_DIRECT = 1, MatrixAddressingMode = 2, Addressing_REGISTER = 3*/
	Boolean operand_source[4]; /*The legal addressing modes for the source (first) operand*/
	Boolean operand_destination[4]; /*The legal addressing modes for the destination (second) operand*/
} sAddressingModesLegality;


/*Returns 0 if is not random addressing. Otherwise returns the amount of asterisks.*/
int isRandomAddressingActionParameter(char *str, int startIndex);

/*Returns true if the opcodeNumber given has atleast 1 parameter
Returns false if not, or opcodeNumber is invalid*/
Boolean opcodeHasOneParamAtLeast(int opcodeNumber);

/*Returns true if the opcodeNumber given has 2 parameters exactly
Returns false if not, or opcodeNumber is invalid*/
Boolean opcodeHasTwoParameters(int opcodeNumber);

/*Returns 0 if its legal
Returns 1 if the first parameter is illegal
Returns 2 if the second paramter is illegal
Returns 3 if both parameters are illegal*/
int addressingModeIsLegal(sOpcode *statement);



	/*Returns the value of the opcodeNumber
	  Or NOT_FOUND*/
	int findOpcode(char *str, int startIndex);

	/*Returns the value of the directive
	  Or NOT_FOUND*/
	int findDirective(char *str, int startIndex);

	/*Returns the value of the directive
	  Or NOT_FOUND*/
	int findRegister(char *str, int startIndex);



extern char *(Opcodes[]);
extern char *(Directives[]);
extern char *(Registers[]);
extern int ParametersCount[];
extern sAddressingModesLegality OpcodeLegalAddressingModes[];


#endif

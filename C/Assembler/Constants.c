#include <string.h>
#include "Constants.h"
#include "StringHelper.h"

char *(Opcodes[]) = {
	"mov", "cmp", "add", "sub",
	"not", "clr", "lea", "inc",
	"dec", "jmp", "bne", "red",
	"prn", "jsr", "rts", "stop" };

int ParametersCount[] = {
	2, 2, 2, 2,
	1, 1, 2, 1,
	1, 1, 1, 1,
	1, 1, 0, 0 };

char *(Directives[]) = { "data", "string", "mat", "entry", "extern" };

char *(Registers[]) = {
	"r0", "r1", "r2", "r3",
	"r4", "r5", "r6", "r7" };

sAddressingModesLegality OpcodeLegalAddressingModes[] = {
	/*mov*/ { { 1, 1, 1, 1 },{ 0, 1, 1, 1 } },
	/*cmp*/ { { 1, 1, 1, 1 },{ 1, 1, 1, 1 } },
	/*add*/ { { 1, 1, 1, 1 },{ 0, 1, 1, 1 } },
	/*sub*/ { { 1, 1, 1, 1 },{ 0, 1, 1, 1 } },
	/*not*/ { { 0, 0, 0, 0 },{ 0, 1, 1, 1 } },
	/*clr*/ { { 0, 0, 0, 0 },{ 0, 1, 1, 1 } },
	/*lea*/ { { 0, 1, 1, 0 },{ 0, 1, 1, 1 } },
	/*inc*/ { { 0, 0, 0, 0 },{ 0, 1, 1, 1 } },
	/*dec*/ { { 0, 0, 0, 0 },{ 0, 1, 1, 1 } },
	/*jmp*/ { { 0, 0, 0, 0 },{ 0, 1, 1, 1 } },
	/*bne*/ { { 0, 0, 0, 0 },{ 0, 1, 1, 1 } },
	/*red*/ { { 0, 0, 0, 0 },{ 0, 1, 1, 1 } },
	/*prn*/ { { 0, 0, 0, 0 },{ 1, 1, 1, 1 } },
	/*jsr*/ { { 0, 0, 0, 0 },{ 0, 1, 1, 1 } },
	/*rts*/ { { 0, 0, 0, 0 },{ 0, 0, 0, 0 } },
	/*stop*/{ { 0, 0, 0, 0 },{ 0, 0, 0, 0 } }
};

/* Returns true if str starts with startsWith*/
static Boolean startsWith(const char *str, const char *startsWith)
{
	return strncmp(startsWith, str, strlen(startsWith)) == 0;
}

/* returns the index of 's' in 'data', or NOT_FOUND */
static int findStringInArray(char *s, int startIndex, char * data[], size_t count)
{
	unsigned int i;
	for (i = 0; i < count; ++i)
		if (startsWith(s + startIndex, data[i])) /*If s starts with the current value we test*/
			return i;

	return NOT_FOUND;
}

int findOpcode(char *s, int startIndex)
{
	return findStringInArray(s, startIndex, Opcodes, sizeof(Opcodes) / sizeof(char*));
}

int findDirective(char *s, int startIndex)
{
	return findStringInArray(s, startIndex, Directives, sizeof(Directives) / sizeof(char*));
}

int findRegister(char *s, int startIndex)
{
	return findStringInArray(s, startIndex, Registers, sizeof(Registers) / sizeof(char*));
}

Boolean opcodeHasOneParamAtLeast(int opcodeNumber)
{
	if (opcodeNumber >= 0 && opcodeNumber < ARRAY_LENGTH(ParametersCount))
		return ParametersCount[opcodeNumber] >= 1;
	return false;
}

Boolean opcodeHasTwoParameters(int opcodeNumber)
{
	if (opcodeNumber >= 0 && opcodeNumber < ARRAY_LENGTH(ParametersCount))
		return ParametersCount[opcodeNumber] == 2;
	return false;
}

int addressingModeIsLegal(sOpcode *statement)
{
	int returnValue = 0;
	if (statement->parametersCount == 1) /*Single parameter opcodeNumber*/
	{
		if (!OpcodeLegalAddressingModes[statement->opcodeNumber].operand_destination[statement->paramTwo.type]) /*If is illegal*/
			returnValue |= 1;
	}
	if (statement->parametersCount == 2) /*Two parameters opcodeNumber*/
	{
		if (!OpcodeLegalAddressingModes[statement->opcodeNumber].operand_source[statement->paramOne.type]) /*If is illegal*/
			returnValue |= 1;
		if (!OpcodeLegalAddressingModes[statement->opcodeNumber].operand_destination[statement->paramTwo.type]) /*If is illegal*/
			returnValue |= 2;
	}
	return returnValue;
}
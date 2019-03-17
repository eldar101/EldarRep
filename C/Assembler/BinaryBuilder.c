#include <string.h>
#include "Table.h"
#include "AssemblerDataBuilder.h"
#include "BinaryBuilder.h"

/* Adds the appropriate bytes to the code*/
static void addParameter(sParameterInOpcode param, sByteCode * code, Boolean is_second_register)
{
	if (param.type == Addressing_IMMEDIATE)
	{
		code->parameters[code->length].ERA = Absolute;
		code->parameters[code->length].value = twosComplement(param.value);
		(code->length)++; /*Just one byte required*/
	}
	else if (param.type == Addressing_DIRECT)
	{
		if (param.labelIndex.labelAddr == MARKED_EXTERN)
		{
			code->parameters[code->length].ERA = External;
		}
		else
		{
			code->parameters[code->length].ERA = Relocatable;
			code->parameters[code->length].value = param.labelIndex.labelAddr;
		}
		(code->length)++; /*Just one byte required*/
	}
	else if (param.type == Addressing_REGISTER)
	{
		/*This is the first parameter*/
		if (code->length == 0)
		{
			code->parameters[code->length].ERA = Absolute;
			code->parameters[code->length].value = (param.registerIndex << 4);
			code->length++;
		}
		else if (code->length == 1 && is_second_register) /*This is the second register parameter*/
		{
			code->parameters[0].value |= (param.registerIndex << 0);
		}
		else
		{
			code->parameters[code->length].ERA = Absolute;
			code->parameters[code->length].value = (param.registerIndex << 0);
			code->length++;
		}
	}
	else if (param.type == Addressing_MATRIX)
	{
		code->parameters[code->length].ERA = Relocatable;
		code->parameters[code->length].value = param.labelIndex.labelAddr;
		code->length++;
		code->parameters[code->length].ERA = Absolute;
		code->parameters[code->length].value = (param.matrix.register1Index << 4) | (param.matrix.register2Index);
		code->length++;
	}
}

/* Generates an sByteCode struct from an action*/
static sByteCode generateBytecode(sOpcode action)
{
	sByteCode output = { 0 }; /*Initializing to zero*/

	output.opcodeNumber.addressingSrc = action.paramOne.type;
	output.opcodeNumber.addressingDest = action.paramTwo.type;
	output.opcodeNumber.parametersCount = action.parametersCount;

	output.opcodeNumber.opcodeNumber = action.opcodeNumber;

	if (action.parametersCount == 1)
	{
		addParameter(action.paramTwo, &output, false);
	}
	if (action.parametersCount >= 2)
	{
		addParameter(action.paramOne, &output, false);
		addParameter(action.paramTwo, &output, action.paramOne.type == Addressing_REGISTER);
	}
	return output;
}

int calculateLength(sProgramLine s)
{
	if (s.type == Statement_COMMENT || s.type == Statement_EMPTY)
		return 0;
	if (s.type == Statement_ACTION)
	{
		sByteCode code = generateBytecode(s.Statement.action);
		return code.length + 1; /*The opcodeNumber itself plus the amount of parameters*/
	}
	if (s.type == Statement_DIRECTIVE)
	{
		if (s.Statement.directive.type == Directive_DATA)
		{
			return s.Statement.directive.Data.data.Size;
		}
		if (s.Statement.directive.type == Directive_MATRIX)
		{
			return s.Statement.directive.Data.matrix.cols * s.Statement.directive.Data.matrix.rows;
		}
		if (s.Statement.directive.type == Directive_STRING)
		{
			return strlen(s.Statement.directive.Data.string) + 1; /*Plus one for the null terminator*/
		}

		return 0; /*Entry and Extern are just definitions*/
	}

	return 0;
}

int _createWordFromOpcode(sOpcodeDescribed code)
{
	int val = 0;
	val |= code.addressingDest << 2;
	val |= code.addressingSrc << 4;
	val |= code.opcodeNumber << 6;
	return val;
}

int _createWordFromParameter(sParameterOfOpcode code)
{
	int val = 0;
	val |= code.ERA;
	val |= code.value << 2;
	return val;
}

unsigned int twosComplementFull(int value)
{
	int mask = 0x3FF; /*10 rightmost bits*/
	if (value >= 0)
		return (unsigned int)value;
	value *= -1; /*now its positive*/
	return (~0) & (~(value - 1)) & mask;
}

unsigned int twosComplement(int value)
{
	int mask = 0xFF; /*8 rightmost bits*/
	if (value >= 0)
		return (unsigned int)value;
	value *= -1; /*now its positive*/
	return (~0) & (~(value - 1)) & mask;
}

int generateWords(sOpcode action, unsigned int* fill)
{
	int i;
	sByteCode code = generateBytecode(action);
	fill[0] = _createWordFromOpcode(code.opcodeNumber);
	for (i = 0; i < code.length; ++i)
	{
		fill[i + 1] = (code.parameters[i].ERA);
		if (code.parameters[i].ERA != External)
			fill[i + 1] |= code.parameters[i].value << 2;
	}
	return i + 1;
}

void convertNumberBase4(unsigned int value, char* output)
{
	char digits[] = "abcd";
	int i;
	int length = 0;
	unsigned int copy = value;
	while (copy) /*Finding the size of the number*/
	{
		++length;
		copy /= 4;
	}
	output[length] = '\0'; /*NULL terminating*/
	for (i = length - 1; i >= 0; --i)
	{
		output[i] = digits[(value % 4)]; /*length % 4 is the right-most digit; */
		value /= 4; /*removing the right-most digit*/
	}
}

void convertNumberToStringBase4weird(unsigned int value, int minSize, char* output)
{
	char digits[] = "abcd";
	int i;
	int length = 0;
	int padding;
	unsigned int copy = value;
	while (copy) /*Finding the size of the number*/
	{
		++length;
		copy /= 4;
	}
	padding = minSize - length;
	if (padding < 0)
		return;
	for (i = minSize - 1; i >= minSize - length; --i)
	{
		output[i] = digits[(value % 4)]; /*length % 32 is the right-most digit; adding 'a' gives the ASCII value of it*/
		value /= 4; /*removing the right-most digit*/
	}
	output[minSize] = '\0'; /*Terminate string*/
	for (i = minSize - length - 1; i >= 0; --i)
	{
		output[i] = digits[0]; /*Add padding*/
	}
	return;
}

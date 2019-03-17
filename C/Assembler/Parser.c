#include <string.h>
#include <stdlib.h>
#include "Parser.h"
#include "Constants.h"
#include "StringHelper.h"


/* Analyzes statement and returns the type.
Sets label to be the name ,otherwise untouched.
Sets startStatement to the first char of the statement,
post white-space and post label.*/
static sGenericErrorMessage getStatementType(char *line, char *label, int *startStatement, eTypeOfStatement *type)
{
	sGenericErrorMessage res;
	int len = strlen(line);

	if (len == 0 || stringIsWhitespace(line))
	{
		*type = Statement_EMPTY;
		return NO_ERROR;
	}

	*startStatement = firstNonWhitespace(line, *startStatement);

	/*now, its safe to assume len > 0*/
	if (line[*startStatement] == ';')
		/*By definition, if and only if
		the first char is ';', its a comment*/
	{
		*type = Statement_COMMENT;
		return NO_ERROR;
	}
	label[0] = '\0';
	res = findLabelInString(line, label, startStatement); /*Skip the label and whitespace if exists*/

	if (res.isError)
	{
		return res;
	}

	if (line[*startStatement] == '.')
	{
		*type = Statement_DIRECTIVE;
		return NO_ERROR;
	}

	*type = Statement_ACTION;
	return NO_ERROR; /*Defaults to this*/
}

/* Parses a int number according to the rules in the assembly.*/
static sGenericErrorMessage parseNumberExtended(char *line, int index, char **postNumber, int *value, int bitsize, int checkNextChar)
{
	const int max_allowed_value = (1 << (bitsize - 1)) - 1;
	const int min_allowed_value = -(1 << (bitsize - 1));

	*value = strtol(&line[index], postNumber, 0); /*parse*/
	if (*postNumber == &(line[index])) /*the first char isn't a digit*/
	{
		return createError("The number given is of incorrect format", index);
	}
	if (checkNextChar && (
		**postNumber != '\0' &&
		**postNumber != ' ' &&
		**postNumber != '\t' &&
		**postNumber != ',')) /*After the number there is no appropriate seperator*/
	{
		return createError("The given character after the number is illegal. The legal characters are the space, tab and comma characters.",
			*postNumber - line);
	}
	if (*value > max_allowed_value || *value < min_allowed_value)
	{
		return createError("The number given is out of range", index);
	}

	return NO_ERROR; /*its OKAY*/
}

/*Parses a int number, assuming it is used for data.*/
static sGenericErrorMessage parseNumberData(char *line, int index, char **postNumber, int *value)
{
	return parseNumberExtended(line, index, postNumber, value, DATA_BITS_IN_WORD, 1);
}

/*Parses a int number, assuming it is used for matrix.*/
static sGenericErrorMessage parseNumberMatrix(char *line, int index, char **postNumber, unsigned int *value)
{
	int v; /*Because parseNumberExtended deals with negatives, and we don't we have a temp variable*/
	sGenericErrorMessage err = parseNumberExtended(line, index, postNumber, &v, DATA_BITS_IN_WORD, 0);
	if (err.isError)
	{
		return err;
	}
	if (v < 0)
	{
		return createError("You cannot provide a negative size for a matrix", index);
	}
	*value = (unsigned int)v;
	return NO_ERROR;
}

/*Parses a int number, assuming it is used as an immediate.*/
static sGenericErrorMessage parseNumberImmediate(char *line, int index, char **postNumber, int *value)
{
	return parseNumberExtended(line, index, postNumber, value, IMMEDIATE_BITS_IN_WORD, 1);
}

/*Advances the index to after the parameter.*/
static sGenericErrorMessage nextParameterIndex(char *line, int *index)
{
	*index = firstNonWhitespace(line, *index);
	if (!line[*index]) return NO_ERROR; /*End*/
	if (line[*index] == ',')
	{
		(*index)++;
		*index = firstNonWhitespace(line, *index);
		return NO_ERROR;
	}
	else return createError("The appropriate seperator between parameters is the comma \" , \"", *index);

}

/*Verifies '...[...rX...]...' - where ... stand for whitespace, and returns X in reg*/
static sGenericErrorMessage parseMatrixAddressingParameterSize(char *line, int * reg, int * end_index)
{
	int currIndex = firstNonWhitespace(line, 0);

	if (line[currIndex] == '\0')
		return createError("Missing data in matrix addressing", currIndex);

	if (line[currIndex] != '[')
		return createError("You must begin a matrix parameter with a '['", currIndex);
	currIndex++; /*skip '['*/

	currIndex = firstNonWhitespace(line, currIndex);

	/*Check if it is a register*/
	*reg = findRegister(line, currIndex);
	if (*reg == NOT_FOUND)
	{
		return createError("You must put a valid register in a matrix addressing parameter", currIndex);
	}

	currIndex += strlen(Registers[*reg]); /*Advance to after name of register*/

	currIndex = firstNonWhitespace(line, currIndex);
	if (line[currIndex] == '\0')
		return createError("Missing data after matrix addressing", currIndex);

	if (line[currIndex] != ']')
		return createError("You must end matrix addressing with a ']'", currIndex);
	currIndex++; /*skip ']'*/

	*end_index = currIndex;
	return NO_ERROR;
}

static sGenericErrorMessage parseMatrixAddressingParameter(char *line, int * index1reg, int * index2reg, int * index)
{
	sGenericErrorMessage err;

	int tmpCurrIndex; /*used to accumulate end index*/

	err = parseMatrixAddressingParameterSize(&line[*index], index1reg, &tmpCurrIndex);
	if (err.isError)
	{
		err.column += *index;
		return err;
	}
	*index += tmpCurrIndex;

	err = parseMatrixAddressingParameterSize(&line[*index], index2reg, &tmpCurrIndex);
	if (err.isError)
	{
		err.column += *index;
		return err;
	}
	*index += tmpCurrIndex;

	return NO_ERROR;
}

/*Parse the next parameter.*/
static sGenericErrorMessage createParameter(char *line, int *index, sParameterInOpcode *param)
{
	sLabelInCode addr;
	int regIndex;
	int length;

	*index = firstNonWhitespace(line, *index);

	if (!line[*index]) /*End of line*/
	{
		*index = -1; /*Mark it as non - parameter*/
		return NO_ERROR;
	}

	if (line[*index] == '#') /*Its Immidiate Address*/
	{
		char * postNumber;
		sGenericErrorMessage errMsg;
		(*index)++;

		param->type = Addressing_IMMEDIATE;

		errMsg = parseNumberImmediate(line, *index, &postNumber, &(param->value));
		if (errMsg.isError)
			return errMsg;

		*index += postNumber - &(line[*index]); /*Now index is after the parameter*/
		return nextParameterIndex(line, index);
	}

	/*Check if it is a register*/
	if ((regIndex = findRegister(line, *index)) != NOT_FOUND) /*Its Register Address*/
	{
		param->type = Addressing_REGISTER;
		param->registerIndex = regIndex;

		*index += strlen(Registers[regIndex]); /*Advance to after name of register*/

		return nextParameterIndex(line, index);
	}

	/*It should be Direct or Matrix*/
	length = lengthOfNextWord(&line[*index]);

	if (length >= LABEL_SIZE_MAX)
	{
		return createError("The label must be shorter then " LABEL_SIZE_MAX_STRING " characters int", *index);
	}

	addr.labelAddr = NO_ADDRESS_AVAILABLE; /*We don't have an address yet*/
	strncpy(addr.rowName, &line[*index], length); /*Copy the string*/
	addr.rowName[length] = '\0'; /*strncpy doesn't NULL terminate*/

	if (length == 0)
	{
		return createError("The label given is of bad syntax: It must be atleast of length of 1 character", *index);
	}
	if (!isChar(addr.rowName[0]))
	{
		return createError("The label given is of bad syntax: It must start with a valid character", *index);
	}
	*index += length; /*Skip word*/
	*index = firstNonWhitespace(line, *index); /*We need to see if we have matrix addressing here*/

	param->labelIndex = addr;

	if (line[*index] == '[')
	{
		/*this is matrix addressing*/
		sGenericErrorMessage err = parseMatrixAddressingParameter(line, &param->matrix.register1Index, &param->matrix.register2Index, index);
		if (err.isError)
		{
			return err;
		}
		param->type = Addressing_MATRIX;
		return nextParameterIndex(line, index);
	}

	/*this is not matrix addressing*/
	param->type = Addressing_DIRECT;

	return nextParameterIndex(line, index);
}

/*Parse the command (only of type Statement_ACTION, no label).*/
static sGenericErrorMessage createDescriptionActionOpcodeStatement(char *line, sOpcode *desc)
{
	int currIndex;
	int firstParamIndex = 0, secondParamIndex = 0, validateOperandType;
	int opcodeNumber = findOpcode(line, 0);

	desc->opcodeNumber = opcodeNumber;

	if (opcodeNumber == NOT_FOUND)
	{
		return createError("The opcode given is invalid or the command is of bad syntax", 0);
	}

	currIndex = strlen(Opcodes[opcodeNumber]);

	/*For the Error message*/


	/*If there's atleast one parameter (according to definition)*/
	if (opcodeHasOneParamAtLeast(opcodeNumber))
	{
		sGenericErrorMessage c;
		currIndex = firstNonWhitespace(line, currIndex);
		firstParamIndex = currIndex; /*Remember the index for Error message*/
		c = createParameter(line, &currIndex, &(desc->paramOne));
		if (c.isError)
			return c;
		if (currIndex == -1) /*no parameter*/
		{
			if (opcodeHasTwoParameters(opcodeNumber))
				return createError("This opcode requires two parameters and you gave none", firstParamIndex);
			else
				return createError("This opcode requires one parameter and you gave none", firstParamIndex);
		}
		desc->paramOne.type = desc->paramOne.type;
		desc->parametersCount = 1;

		desc->indexes.firstArg = firstParamIndex;

		if (!opcodeHasTwoParameters(opcodeNumber))
		{
			/*If theres only one parameter, its the destination parameter*/
			desc->paramTwo = desc->paramOne;
			desc->paramTwo.type = desc->paramOne.type;

			desc->paramOne.type = 0;
		}
	}

	/*If there are exactly two parameters (according to definition)*/
	if (opcodeHasTwoParameters(opcodeNumber))
	{
		sGenericErrorMessage c;
		secondParamIndex = currIndex; /*Remember the index for Error message*/
		c = createParameter(line, &currIndex, &(desc->paramTwo));
		if (c.isError)
			return c;
		if (currIndex == -1)
			return createError("This opcode requires two parameters and you gave only one", secondParamIndex);
		desc->paramTwo.type = desc->paramTwo.type;
		desc->parametersCount = 2;

		desc->indexes.secondArg = secondParamIndex;
	}

	currIndex = firstNonWhitespace(line, currIndex);
	if (line[currIndex] != '\0' && line[currIndex] != ';') /*Check if the last nonwhitespace character is either comment ';' or it's the end*/
	{
		return createError("Additional data after end of command, what did you try to put?", currIndex);
	}

	validateOperandType = addressingModeIsLegal(desc);
	if (validateOperandType == SUCCESS) /*legal*/
		return NO_ERROR;
	else if (validateOperandType == 1)
		return createError("The first operand is of illegal addressing mode.", firstParamIndex);
	else if (validateOperandType == 2)
		return createError("The second operand is of illegal addressing mode.", secondParamIndex);
	else if (validateOperandType == 3)
		return createError("Both operands are of illegal addressing modes.", firstParamIndex); /*Only first Error given*/
	else
		return createError("Error occured while validating the addressing modes of given statement", 0);
}

/*Parse .data directive arguments*/
static sGenericErrorMessage parseDataParameters(char *line, sArray *arr)
{
	int i;
	int value;
	char *ptr = line;
	
	ptr += firstNonWhitespace(ptr, 0);
	if (*ptr == '\0') /*end*/
		return NO_ERROR;

	for (i = 0; i < CMD_SIZE_MAX; ++i)
	{
		char * postNumber = 0;
		sGenericErrorMessage err;

		err = parseNumberData(ptr, 0, &postNumber, &value);
		if (err.isError)
		{
			err.column += (int)(ptr - line);
			return err;
		}

		arr->Size = i + 1;
		arr->data[i] = value;
		ptr = postNumber;
		ptr += firstNonWhitespace(ptr, 0);

		if (*ptr == '\0') /*end*/
			return NO_ERROR;
		if (*ptr != ',')
			return createError("The only legal seperator for the .data directive is the comma", (int)(ptr - line));
		++ptr; /*Skip comma*/
	}

	if (i == CMD_SIZE_MAX)
	{
		return createError("Too much parameters.", 0);
	}

	return NO_ERROR;
}

/*Parse .string directive argument*/
static sGenericErrorMessage parseStringParameters(char *line, char *str)
{
	int len;
	char * tmp;
	int currIndex = firstNonWhitespace(line, 0);

	if (line[currIndex] == '\0')
		return createError("Missing a legal string after the .string directive", currIndex);

	if (line[currIndex] != '\"')
		return createError("A string should start with apostrophes", currIndex);

	currIndex++; /*Skip apostrophes*/

	tmp = strchr(&line[currIndex], '\"'); /*Seeking for the next apostrophes*/
	if (tmp == NULL) /*not found*/
		return createError("A string should end with apostrophes", strlen(line));
	len = tmp - &(line[currIndex]); /*now its length*/

	strncpy(str, &line[currIndex], len);


	currIndex += len + 1; /*post string*/

	if (!stringIsWhitespace(&line[currIndex]))
		return createError("There should be nothing after the string in a .string directive", currIndex);

	return NO_ERROR;
}

/*Verifies '...[...NUMERIC...]...' - where ... stand for whitespace, and returns NUMERIC in val*/
static sGenericErrorMessage parseMatrixSize(char *line, unsigned int * val, int * end_index)
{
	sGenericErrorMessage err;
	int currIndex = firstNonWhitespace(line, 0);
	char *postNumber;

	if (line[currIndex] == '\0')
		return createError("Missing dimension for .mat directive", currIndex);

	if (line[currIndex] != '[')
		return createError("You must begin a 'mat' directive with a '['", currIndex);
	currIndex++; /*skip '['*/

	err = parseNumberMatrix(line, currIndex, &postNumber, val);
	if (err.isError)
	{
		return err;
	}

	currIndex = (postNumber - line);
	currIndex = firstNonWhitespace(line, currIndex);

	if (line[currIndex] != ']')
		return createError("You must end a 'mat' indexing with a ']'", currIndex);
	currIndex++; /*skip ']'*/

	*end_index = currIndex;
	return NO_ERROR;
}

/*parses '[rows][cols] 1,2,3'*/
static sGenericErrorMessage parseMatrixParameters(char *line, sMatrixDefinition * mat)
{
	sGenericErrorMessage err;
	unsigned int rows;
	unsigned int cols;

	int tmpCurrIndex; /*used to accumulate current index*/
	int currIndex = 0;

	err = parseMatrixSize(line, &rows, &tmpCurrIndex);
	if (err.isError)
	{
		return err;
	}
	currIndex += tmpCurrIndex;

	err = parseMatrixSize(&line[currIndex], &cols, &tmpCurrIndex);
	if (err.isError)
	{
		err.column += currIndex;
		return err;
	}
	currIndex += tmpCurrIndex;

	err = parseDataParameters(&line[currIndex], &mat->values);
	if (err.isError)
	{
		err.column += currIndex;
		return err;
	}

	if (mat->values.Size > (rows * cols))
	{
		/*User has supplied more values than allocated in*/
		return createError("You cannot supply more values than allocated in your matrix", currIndex);
	}

	/*If less than allocated, that's fine*/

	mat->rows = rows;
	mat->cols = cols;

	return NO_ERROR;
}

/*Parse .entry directive argument*/
static sGenericErrorMessage parseEntryParameters(char *line, char *label)
{
	int length;
	int currIndex = firstNonWhitespace(line, 0);

	if (line[currIndex] == '\0')
		return createError("Missing a label after the .entry directive", currIndex);
	length = lengthOfNextWord(&line[currIndex]);

	if (length >= LABEL_SIZE_MAX)
	{
		return createError("The label must be shorter then " LABEL_SIZE_MAX_STRING " characters int", currIndex);
	}

	strncpy(label, &line[currIndex], length); /*Copy the string*/

	label[length] = '\0'; /*strncpy doesn't NULL terminate*/

	if (length == 0)
	{
		return createError("The label given is of bad syntax: It must be atleast of length of 1 character", currIndex);
	}
	if (!isChar(label[0]))
	{
		return createError("The label given is of bad syntax: It must start with a valid character", currIndex);
	}

	return NO_ERROR;
}

/*Parse .extern directive argument*/
static sGenericErrorMessage parseExternParameters(char *line, char *label)
{
	int length;
	int currIndex = firstNonWhitespace(line, 0);

	if (line[currIndex] == '\0')
		return createError("Missing a label after the .extern directive", currIndex);
	length = lengthOfNextWord(&line[currIndex]);

	if (length >= LABEL_SIZE_MAX)
	{
		return createError("The label must be shorter then " LABEL_SIZE_MAX_STRING " characters int", currIndex);
	}

	strncpy(label, &line[currIndex], length); /*Copy the string*/

	label[length] = '\0'; /*strncpy doesn't NULL terminate*/

	if (length == 0)
	{
		return createError("The label given is of bad syntax: It must be atleast of length of 1 character", currIndex);
	}
	if (!isChar(label[0]))
	{
		return createError("The label given is of bad syntax: It must start with a valid character", currIndex);
	}

	return NO_ERROR;
}

/*Parse the command (only of type Statement_DIRECTIVE, no label).*/
static sGenericErrorMessage createDescriptionDataStringEntryExternDirectiveStatement(char *line, sDirective *desc)
{
	int directive;
	int currIndex;
	sGenericErrorMessage err;

	if (line[0] != '.')
		return createError("Error parsing directive statement. It must start with a dot", 0);

	directive = findDirective(line, 1);
	if (directive == NOT_FOUND)
		return createError("Invalid directive specified", 1);

	desc->type = (eDirctvs)directive;
	currIndex = 1 + strlen(Directives[directive]);

	currIndex = firstNonWhitespace(line, currIndex);

	desc->indexArg = currIndex;

	switch (desc->type)
	{

	case Directive_DATA: /*For .data directives*/
		err = parseDataParameters(&(line[currIndex]), &(desc->Data.data));
		if (!err.isError && desc->Data.data.Size == 0)
		{
			err = createError("You cannot provide an empty .data directive", currIndex);
		}
		break;

	case Directive_STRING: /*For .string directives*/
		err = parseStringParameters(&(line[currIndex]), desc->Data.string);
		break;

	case Directive_MATRIX: /*For .mat directives*/
		err = parseMatrixParameters(&(line[currIndex]), &desc->Data.matrix);
		break;

	case Directive_ENTRY: /*For .entry directives*/
		err = parseEntryParameters(&(line[currIndex]), desc->Data.entry.rowName);
		break;

	case Directive_EXTERNAL: /*For .extern directives*/
		err = parseExternParameters(&(line[currIndex]), desc->Data.external.rowName);
		break;
	default:
		return createError("Couldn't detect the type of directive", currIndex);
	}
	if (err.isError)
	{
		err.column += currIndex;
		return err;
	}
	return NO_ERROR;
}

sGenericErrorMessage parseLine(char *line, int lineNumber, sProgramLine *statement)
{
	sGenericErrorMessage err;
	int startStatement = 0;
	strcpy(statement->indexes.text, line);

	err = getStatementType(line, statement->label, &startStatement, &statement->type);
	if (err.isError)
		return err;

	statement->indexes.postLabel = startStatement;
	statement->indexes.lineNumber = lineNumber;

	statement->labelExists = strlen(statement->label) != 0; /*Theres a label; its already set*/


	if (statement->type == Statement_COMMENT || statement->type == Statement_EMPTY)
		return NO_ERROR;

	else if (statement->type == Statement_ACTION)
		err = createDescriptionActionOpcodeStatement(&(line[startStatement]), &(statement->Statement.action));

	else if (statement->type == Statement_DIRECTIVE)
	{
		err = createDescriptionDataStringEntryExternDirectiveStatement(&(line[startStatement]), &(statement->Statement.directive));
	}

	else
		return createError("Error understanding the type of the statement", 0);


	err.column += startStatement;

	return err;
}

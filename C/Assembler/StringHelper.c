#include <string.h>
#include "Error.h"
#include "StringHelper.h"
#include "Parser.h"
#include "Constants.h"

Boolean stringIsWhitespace(char *line)
{
	if (*line == '\0')
		return 1;
	for (; *line; ++line)
		if (*line != ' ' && *line != '\t')
			return false;

	return true;
}

int firstNonWhitespace(char *line, int startIndex)
{
	for (; line[startIndex]; ++startIndex)
		if (line[startIndex] != ' ' && line[startIndex] != '\t')
			return startIndex;

	return startIndex;
}

Boolean isChar(char c)
{
	return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
}

Boolean isDigit(char c)
{
	return (c >= '0' && c <= '9');
}

Boolean isAlphaNumber(char c)
{
	return isChar(c) || isDigit(c);
}

int lengthOfNextWord(char *line)
{
	int i;
	int len = strlen(line);
	for (i = 0; i < len; ++i)
		if (!isAlphaNumber(line[i]))
			return i;
	return i;
}

sGenericErrorMessage findLabelInString(char *line, char *labelName, int *startStatement)
{
	/*Assuming: Non-empty line*/
	/*Assuming: labelName is allocated*/
	int i;

	if (!isChar(line[*startStatement])) /*First char must be a letter*/
	{
		return NO_ERROR; /*NOT A LABEL*/
	}

	for (i = *startStatement; isAlphaNumber(line[i]); ++i); /*Skip alphanumber*/

	if (i - *startStatement >= LABEL_SIZE_MAX)
	{
		return createError("Label is too large, max size is " LABEL_SIZE_MAX_STRING, 0);
	}

	if (line[i] != ':') /*If the last char isn't ':', its not a label*/
	{
		return NO_ERROR;
	}

	if (*startStatement > 0)
	{
		return createError("Label doesn't start at the first column in the line", i);
	}

	strncpy(labelName, line + *startStatement, i - *startStatement); /*Copies the name only*/
	labelName[i + *startStatement] = '\0'; /*Terminate string*/


	if (findRegister(labelName, 0) != NOT_FOUND)
	{
		return createError("That is an illegal name for a label: A label must not be a register.", 0);
	}
	if (findOpcode(labelName, 0) != NOT_FOUND)
	{
		return createError("That is an illegal name for a label: A label must not be an opcode.", 0);
	}
	if (findDirective(labelName, 0) != NOT_FOUND)
	{
		return createError("That is an illegal name for a label: A label must not be a directive.", 0);
	}

	*startStatement = firstNonWhitespace(line, i + 1); /*plus one to skip the ';' char*/

	return NO_ERROR;
}

#include <string.h>
#include "Vector.h"
#include "Constants.h"
#include "AssemblerDataBuilder.h"
#include "BinaryBuilder.h"
#include "Parser.h"
#include "Error.h"

/* Visually print the Error*/
static void displayErr(sGenericErrorMessage err, char* line, int lineNumber)
{
	int i, length;
	printf(" Line: %d\n Error at index %d: \n %s\n ", lineNumber + 1, err.column, err.ErrorMessage);
	length = strlen(line);
	for (i = 0; i < length; ++i)
		printf("%c", line[i] == '\t' ? ' ' : line[i]); /*Replace tabs by spaces*/
	printf("\n ");
	for (i = 0; i < err.column; ++i)
		printf(" ");
	printf("^\n\n");
	return;
}


/* Prints out-of-memory Error message and dies*/
static ERROR_CODE displayMemoryAllocationFailed(void)
{
	printf("Memory allocation failed\n");
	return ALLOCATION_FAILED;
}

/* Removes all \n's and \r's without ruining the line*/
static void removeLineBreaks(char *c)
{
	int len = strlen(c);
	int i, j;
	for (i = 0; i < len; ++i)
		if (c[i] == '\r' || c[i] == '\n')
			c[i] = '\0';
	for (i = 0, j = 0; i < len; ++i)
	{
		if (c[i] != '\0')
		{
			c[j] = c[i];
			++j;
		}
	}
	c[j] = '\0';
}

/* Inserts the data safely to the Table. On Error, returns ERRORS*/
static ERROR_CODE addToTable(sGrowingTable t, char *name, int value, char *line, int lineNumber, int index)
{
	int res = tableAdd(t, name, value);
	if (res == TABLE_NAME_EXISTS)
	{
		displayErr(createError("Warning: The given definition was already defined somewhere", index), line, lineNumber);
		return ERRORS_ALREADY_EXIST;
	}
	else if (res == ALLOCATION_FAILED)
		return displayMemoryAllocationFailed();
	else if (res == TABLE_NAME_TOO_LONG)
	{
		displayErr(createError("The given name is too int", index), line, lineNumber);
		return ERRORS;
	}
	return SUCCESS;
}

/* Reads the file, parses each statememnt,
fills data.statement and data.labels and data.externals*/
static ERROR_CODE initialRun(FILE *fp, sDataASM *data)
{
	int DataCntr = 0;
	int count_of_instructions = 100;
	Boolean ErrorsFound = false;
	unsigned int i;

	char line[LINE_SIZE_MAX + 1];
	int lineNumber = 0;
	char* read;

	/*Read line by line*/
	while ((read = fgets(line, LINE_SIZE_MAX, fp)) != NULL)
	{
		sProgramLine d = { 0 };
		sGenericErrorMessage res = { 0 };
		if (strlen(line) == LINE_SIZE_MAX - 1 && line[strlen(line) - 1] != '\n')
		{
			/*line is too int*/
			displayErr(createError("Line is too int, maximum size is " LABEL_SIZE_MAX_STRING, 0), line, lineNumber);
			++lineNumber;
			ErrorsFound = true;
			/*if fgets didn't read the whole line, it will continue to read the same line, so let's read until the line is finished*/
			while (true)
			{
				int tmp = fgetc(fp);
				if (tmp == '\n' || tmp == EOF)
					break;
			}
			continue;
		}
		removeLineBreaks(line);

		res = parseLine(line, lineNumber, &d);

		if (res.isError)
		{
			/*If an Error is found, we won't parse labels*/
			displayErr(res, line, lineNumber);
			ErrorsFound = true;
		}
		else
		{
			if (d.type == Statement_EMPTY || d.type == Statement_COMMENT)
			{
				++lineNumber;
				continue;
			}
			if (d.labelExists) /*Add label to Table*/
			{
				if (d.type == Statement_ACTION)
				{
					ERROR_CODE err = addToTable(data->labels, d.label, count_of_instructions, line, lineNumber, 0);
					if (err == ALLOCATION_FAILED) /*allocation Error*/
						return err;
					if (err != SUCCESS) /*some other Error*/
						ErrorsFound = true;
				}
				else if (d.type == Statement_DIRECTIVE)
				{
					/*Entry and Extern don't have labels*/
					if (d.Statement.directive.type == Directive_STRING || d.Statement.directive.type == Directive_DATA || d.Statement.directive.type == Directive_MATRIX)
					{
						/*We are negativing the DataCntr to mark the label as of type DATA*/
						/*So we will put then in the end*/
						ERROR_CODE err = addToTable(data->labels, d.label, DataCntr * -1 - 1, line, lineNumber, 0);
						if (err == ALLOCATION_FAILED) /*allocation Error*/
							return err;
						if (err != SUCCESS) /*some other Error*/
							ErrorsFound = true;
					}
				}
			}

			if (d.type == Statement_ACTION)
			{
				count_of_instructions += calculateLength(d);
			}
			else
			{
				sDirective *drctv;
				DataCntr += calculateLength(d);
				drctv = &(d.Statement.directive);
				if (drctv->type == Directive_EXTERNAL)
				{
					ERROR_CODE err;
					if (tableFindByName(data->labels, drctv->Data.external.rowName) != NULL)
					{
						displayErr(createError("The given external name was already defined as a label", d.indexes.postLabel + d.Statement.directive.indexArg), line, lineNumber);
						ErrorsFound = true;
					}
					
					err = addToTable(data->externals, drctv->Data.external.rowName, 0, line,
						lineNumber, d.indexes.postLabel + d.Statement.directive.indexArg);
					if (err == ALLOCATION_FAILED) /*allocation Error*/
						return err;
					if (err != SUCCESS) /*some other Error*/
						ErrorsFound = true;
				}
			}
			if (addLine(data->statement, &d) != SUCCESS) /*We add the statememnt to the statement's array*/
				return displayMemoryAllocationFailed();
		}
		++lineNumber;
	}
	/*Seperation of data and code*/
	for (i = 0; i < data->labels->currSize; ++i) /*Adding the instructions size to the position of each data address*/
	{
		sTableEntry * curr_row = tableFindByIndex(data->labels, i);
		if (curr_row->rowAddress < 0)
			curr_row->rowAddress = (curr_row->rowAddress * -1) + count_of_instructions - 1; /*Removing the minus one thingy*/
	}

	return ErrorsFound ? ERRORS : CONTINUE;
}

/* Updates argument*/
static ERROR_CODE updateArg(sDataASM *data, int argIndex, sParameterInOpcode *p, char *text, int lineNumber)
{
	if (p->type == Addressing_DIRECT || p->type == Addressing_MATRIX)
	{
		sTableEntry* row = tableFindByName(data->labels, p->labelIndex.rowName);
		if (row == NULL)
		{
			row = tableFindByName(data->externals, p->labelIndex.rowName);
			if (row == NULL)
			{
				displayErr(createError("Given label does not exists anywhere", argIndex), text, lineNumber);
				return ERRORS;
			}
			/*else*/
			p->labelIndex.labelAddr = MARKED_EXTERN;
		}
		else
			p->labelIndex.labelAddr = row->rowAddress;
	}
	return SUCCESS;
}

/* Updates statements referencing labels*/
static ERROR_CODE secondRun(sDataASM *data)
{
	Boolean isError = false;
	unsigned int i;
	for (i = 0; i < data->statement->curr_size; ++i) /*Run over all statements*/
	{
		sProgramLine *stmnt = lineAtIndex(data->statement, i);
		if (stmnt->type == Statement_ACTION)
		{
			sOpcode *act = &(stmnt->Statement.action);
			if (act->parametersCount == 1)
			{
				/*Its the second parameter (because its the DESTINATION param)
				But its the first ARGUMENT (because theres ONLY ONE)*/
				isError += updateArg(data,
					stmnt->indexes.postLabel + act->indexes.firstArg,
					&(act->paramTwo),
					stmnt->indexes.text,
					stmnt->indexes.lineNumber);
			}
			if (act->parametersCount == 2)
			{
				isError += updateArg(data,
					stmnt->indexes.postLabel + act->indexes.firstArg,
					&(act->paramOne),
					stmnt->indexes.text,
					stmnt->indexes.lineNumber);

				isError += updateArg(data,
					stmnt->indexes.postLabel + act->indexes.secondArg,
					&(act->paramTwo),
					stmnt->indexes.text,
					stmnt->indexes.lineNumber);
			}
		}
	}

	return isError ? ERRORS : CONTINUE;
}

/* Processes directives:
  Writes the data to dataSegment for .string, .data and .mat
  Adds the appropritate values for .entry*/
static ERROR_CODE writeDataProcessStmnts(sDataASM *data, sOutputASM *out, sProgramLine *stmnt, sGrowingArray dataSegment)
{
	sDirective *drctv = &(stmnt->Statement.directive);
	if (drctv->type == Directive_DATA) /*If its data, add it to the DS*/
	{
		unsigned int i;
		for (i = 0; i < drctv->Data.data.Size; ++i)
			drctv->Data.data.data[i] = twosComplementFull(drctv->Data.data.data[i]);
		if (vectorInsertMany(dataSegment, drctv->Data.data.data, drctv->Data.data.Size) != SUCCESS)
		{
			displayMemoryAllocationFailed();
			out->success = false;
			return ALLOCATION_FAILED;
		}
	}
	if (drctv->type == Directive_STRING) /*Same goes for string*/
	{
		int j;
		int len = strlen(drctv->Data.string);
		for (j = 0; j < len; ++j)
			if (vectorAddInt(dataSegment, (unsigned int)drctv->Data.string[j]) != SUCCESS)
			{
				displayMemoryAllocationFailed();
				out->success = false;
				return ALLOCATION_FAILED;
			}
		if (vectorAddInt(dataSegment, 0) != SUCCESS) /*NULL terminating char*/
		{
			displayMemoryAllocationFailed();
			out->success = false;
			return ALLOCATION_FAILED;
		}
	}
	if (drctv->type == Directive_MATRIX) /*Same goes for MATRIX*/
	{
		unsigned int i;
		unsigned int matrix_size = drctv->Data.matrix.cols * drctv->Data.matrix.rows;
		unsigned int specified_values = drctv->Data.matrix.values.Size;

		for (i = 0; i < matrix_size; ++i)
		{
			unsigned int to_write = 0;
			if (i < specified_values)
				to_write = drctv->Data.matrix.values.data[i];

			if (vectorAddInt(dataSegment, to_write) != SUCCESS)
			{
				displayMemoryAllocationFailed();
				out->success = false;
				return ALLOCATION_FAILED;
			}
		}
	}
	if (drctv->type == Directive_ENTRY) /*Check if labels exists, add it*/
	{
		sTableEntry *row = tableFindByName(data->labels, drctv->Data.entry.rowName);
		if (row == NULL)
		{
			displayErr(createError("The given label doesn't exist. You must use a valid label for an entry directive",
				drctv->indexArg + stmnt->indexes.postLabel), stmnt->indexes.text, stmnt->indexes.lineNumber);
			out->success = false;
		}
		else
		{
			int res = addToTable(out->entries, drctv->Data.entry.rowName, row->rowAddress, stmnt->indexes.text, stmnt->indexes.lineNumber,
				drctv->indexArg);
			if (res == ERRORS) /*allocation Error*/
				return res;
			if (res == -1) /*some other Error*/
				out->success = 0;
		}
	}
	if (drctv->type == Directive_EXTERNAL)
	{
		/*We already managed this, so we don't care*/
	}

	return 0; /*SUCCESS*/
}

/* Processes a parameter  - sets the A,E,R type */
static ERROR_CODE processAsmCodeLineActionParameter(sOutputASM *out, sParameterInOpcode *param, char *A, int indexDirect)
{
	if (param->type == Addressing_IMMEDIATE) /*Immidiate is always absolute*/
	{
		*A = 'A';
	}
	if (param->type == Addressing_DIRECT)
	{
		if (param->labelIndex.labelAddr == MARKED_EXTERN)
		{
			*A = 'E'; /*External marked as external*/
			if (tableAdd(out->externals, param->labelIndex.rowName, indexDirect) != SUCCESS)
			{
				displayMemoryAllocationFailed();
				out->success = false;
				return ALLOCATION_FAILED;
			}
		}
		else
			*A = 'R';
	}

	return SUCCESS;
}

/* Final run. Creates the final compiler output*/
static sOutputASM thirdRun(sDataASM *data)
{
	unsigned int i;
	int IC = 100;
	sOutputASM out;
	ERROR_CODE err;
	sGrowingArray dataSegment;

	out.externals = tableOpen();
	out.externals->allowMultiples = 1;

	out.data = vectorOpen(sizeof(unsigned int));
	out.entries = tableOpen();
	out.entries->allowMultiples = 1;

	out.linkerData = vectorOpen(sizeof(unsigned int));
	out.success = true;

	dataSegment = vectorOpen(sizeof(unsigned int));

	for (i = 0; i < data->statement->curr_size; ++i) /*generate the data bytes*/
	{
		sProgramLine *stmnt = lineAtIndex(data->statement, i);
		if (stmnt->type == Statement_DIRECTIVE)
		{
			writeDataProcessStmnts(data, &out, stmnt, dataSegment);
		}
	}

	for (i = 0; i < data->statement->curr_size; ++i) /*put all the statememnt bytes*/
	{
		sProgramLine *stmnt = lineAtIndex(data->statement, i);
		if (stmnt->type == Statement_ACTION)
		{
			sOpcode *act = &(stmnt->Statement.action);

			int length;
			unsigned int storeWords[MAX_ACTION_SIZE];

			/*These represent the A(bsolute),R(elocaTable),E(xternal) of the parameters*/
			char firstParamA = '\0';
			char secondParamA = '\0';

			if (act->parametersCount == 1)
			{
				if (processAsmCodeLineActionParameter(&out,
					&(stmnt->Statement.action.paramTwo),
					&firstParamA,
					IC + 1))
					return out;
			}
			if (act->parametersCount == 2)
			{
				if (processAsmCodeLineActionParameter(&out,
					&(stmnt->Statement.action.paramOne),
					&firstParamA,
					IC + 1))
					return out;

				if (processAsmCodeLineActionParameter(&out,
					&(stmnt->Statement.action.paramTwo),
					&secondParamA,
					IC + 2))
					return out;
			}
			length = generateWords(*act, storeWords);
			err = vectorAddInt(out.linkerData, (int)'A'); /*The opcodeNumber is always absolute*/
			if (err != SUCCESS)
				return out;

			if (firstParamA != '\0')
			{
				err = vectorAddInt(out.linkerData, (int)firstParamA);
				if (err != SUCCESS)
					return out;
			}
			if (secondParamA != '\0')
			{
				err = vectorAddInt(out.linkerData, (int)secondParamA);
				if (err != SUCCESS)
					return out;
			}
			err = vectorInsertMany(out.data, storeWords, length);
			if (err != SUCCESS)
				return out;
			IC += length;
		}
	}

	err = vectorInsertMany(out.data, dataSegment->data, dataSegment->size_of_array_current);
	if (err != SUCCESS)
		return out;

	return out;
}

sGrowingTable labels;
sOutputASM processData(FILE *fp)
{
	sOutputASM output = { 0 };
	sDataASM data;
	int firstSuccess;
	int secondSuccess;
	output.success = 1;

	data.externals = tableOpen();
	data.externals->allowMultiples = 1;

	data.labels = tableOpen();
	labels = data.labels;
	data.statement = initProgramLinesStruct();
	firstSuccess = !initialRun(fp, &data);

	secondSuccess = !secondRun(&data);

	output = thirdRun(&data);
	freeProgramLinesStruct(data.statement);
	tableClose(data.externals);
	tableClose(data.labels);
	labels = NULL;

	output.success = output.success && firstSuccess && secondSuccess;

	return output;
}

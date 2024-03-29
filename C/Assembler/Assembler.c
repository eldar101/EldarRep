#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "Assembler.h"
#include "AssemblerDataBuilder.h"
#include "BinaryBuilder.h"
#include "Table.h"

/* Prints the Table, each row getting its size*/
static void writeTableToFile(sGrowingTable t, FILE *fp)
{
	unsigned int i;
	char tmp[20] = "";
	for (i = 0; i < t->currSize; ++i)
	{
		sTableEntry * curr_row = tableFindByIndex(t, i);
		convertNumberBase4(curr_row->rowAddress, tmp);
		fprintf(fp, "%s\t%s", curr_row->rowName, tmp);
		if (i != t->currSize - 1)/*If its not last*/
			fprintf(fp, "\n");
	}
}

/* Creates Output files and writes the assembly data to them*/
static void createOutputFiles(sOutputASM o, char *fname)
{
	char name[MAX_FILE_NAME];
	unsigned int i;
	char tmp[20];
	FILE * obj;
	unsigned int markedExtern;
	if (o.entries->currSize > 0)
	{
		FILE * entries;
		strcpy(name, fname);
		strcat(name, ENTRY_FILE_EXTENSION);
		entries = fopen(name, "w");
		if (entries == NULL)
		{
			printf("Couldn't create file '%s' for writing\n", name);
			return;
		}
		writeTableToFile(o.entries, entries);
		fclose(entries);
	}
	if (o.externals->currSize > 0)
	{
		FILE *ext;
		strcpy(name, fname);
		strcat(name, EXTENAL_FILE_EXTENSION);
		ext = fopen(name, "w");
		if (ext == NULL)
		{
			printf("Couldn't create file '%s' for writing\n", name);
			return;
		}
		writeTableToFile(o.externals, ext);
		fclose(ext);
	}
	strcpy(name, fname);
	strcat(name, OBJ_FILE_EXTENSION);
	obj = fopen(name, "w");
	if (obj == NULL)
	{
		printf("Couldn't create file '%s' for writing\n", name);
		return;
	}

	markedExtern = twosComplement(MARKED_EXTERN);

	for (i = 0; i < o.data->size_of_array_current; ++i)
	{
		unsigned int current_value = *(unsigned int *)vectorGet(o.data, i);
		convertNumberToStringBase4weird(i + 100, 4, tmp);
		fprintf(obj, "%s", tmp);
		fprintf(obj, "\t");
		convertNumberToStringBase4weird(current_value == markedExtern ? 0 : current_value, 5, tmp);
		fputs(tmp, obj); /*If its marked as extern, print 0*/
		if (i != o.data->size_of_array_current - 1) /*if its not last*/
			fprintf(obj, "\n");
	}
	fclose(obj);
}

/* Assembles a specific file*/
static void assembleFile(char *file)
{
	FILE * fp;
	char name[MAX_FILE_NAME];
	sOutputASM output;
	if (strlen(file) >= MAX_FILE_NAME - 10) /*Minus ten for added values*/
	{
		printf("The file's '%s' name is too large\n", file);
		return;
	}
	strcpy(name, file);
	strcat(name, ASSEMBLY_FILE_EXTENSION);
	fp = fopen(name, "r");
	if (fp == NULL)
	{
		printf("Can't open file '%s'\n", name);
		return;
	}
	output = processData(fp);
	if (!output.success)
	{
		printf("Errors in file '%s', breaking\n", name);
		return;
	}
	createOutputFiles(output, file);
}

void AssembleFiles(char **files, int filesCount)
{
	int i;
	for (i = 0; i < filesCount; ++i)
		assembleFile(files[i]);
}

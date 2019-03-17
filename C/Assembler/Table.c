#include <stdlib.h>
#include <string.h>
#include "Table.h"

sGrowingTable tableOpen(void)
{
	sGrowingTable t = (sGrowingTable)malloc(sizeof(struct sTableInternal));
	if (t == NULL)
		return NULL;

	t->allowMultiples = 0;
	t->currSize = 0; /*At First, the sGrowingTable is empty*/
	t->rows = vectorOpen(sizeof(sTableEntry));

	if (t->rows == NULL)
	{
		free(t);
		return NULL;
	}

	return t;
}

void tableClose(sGrowingTable t)
{
	vectorClose(t->rows);
	free(t);
}

sTableEntry* tableFindByName(sGrowingTable t, char *name)
{
	unsigned int i;

	for (i = 0; i < t->currSize; ++i)
	{
		sTableEntry * curr_row = (sTableEntry *)vectorGet(t->rows, i);
		if (strncmp(curr_row->rowName, name, STRING_SIZE(curr_row->rowName)) == 0) /*if they are equal*/
			return curr_row;
	}
	return NULL; /*if not found*/
}

sTableEntry* tableFindByIndex(sGrowingTable sGrowingTable, unsigned int index)
{
	if (index >= sGrowingTable->currSize)
	{
		return NULL;
	}
	return (sTableEntry *)vectorGet(sGrowingTable->rows, index);
}

ERROR_CODE tableAdd(sGrowingTable t, char *name, int addr)
{
	ERROR_CODE err;
	sTableEntry row;
	if (strlen(name) > STRING_SIZE(row.rowName)) /* if the name is too int */
		return TABLE_NAME_TOO_LONG;

	if (!t->allowMultiples && tableFindByName(t, name) != NULL)
		return TABLE_NAME_EXISTS; /*Can't be two rows with the same name*/

	row.rowAddress = addr;
	strncpy(row.rowName, name, STRING_SIZE(row.rowName)); /*Copy the string*/

	err = vectorAddElement(t->rows, &row);
	if (err == SUCCESS)
		t->currSize++;

	return err;
}

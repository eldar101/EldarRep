#ifndef TableHeader
#define TableHeader
#include "Vector.h"
#include "Common.h"

/*Maximal size of the name column in a row*/
#define MAX_ROW_STR_SIZE 50

/*A row in the sGrowingTable*/
typedef struct sTableEntry {
	char rowName[MAX_ROW_STR_SIZE];
	int rowAddress;
} sTableEntry;

struct sTableInternal {
	sGrowingArray rows;
	unsigned int currSize;
	int allowMultiples;
};

/*The sGrowingTable. Once maxSize is used, it allocates more memory*/
typedef struct sTableInternal* sGrowingTable;

/*Ctor*/
sGrowingTable tableOpen(void);

/*dtor*/
void tableClose(sGrowingTable sGrowingTable);

/*Find address by name. Returns NULL on failure*/
sTableEntry* tableFindByName(sGrowingTable sGrowingTable, char *name);

/*Returns row at index*/
sTableEntry* tableFindByIndex(sGrowingTable sGrowingTable, unsigned int index);

/*Add address and name to the table. returns SUCCESS, TABLE_NAME_TOO_LONG or ALLOCATION_FAILED.*/
ERROR_CODE tableAdd(sGrowingTable sGrowingTable, char *name, int address);

/*Used by tableAdd*/
#define TABLE_NAME_TOO_LONG (-2)

/*Used by addAdress*/
#define TABLE_NAME_EXISTS (-4)

#endif


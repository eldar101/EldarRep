#ifndef AssemblerDataBuilderHeader
#define AssemblerDataBuilderHeader

#include <stdio.h>
#include "Vector.h"
#include "Table.h"
#include "ProgramArray.h"

/*Internally used extern*/
#define MARKED_EXTERN -123

/*Internal*/
#define CONTINUE 0

/*Internal*/
#define ERRORS (1)
#define ERRORS_ALREADY_EXIST (2)

/*INTERNAL: Used for processing data labels*/
#define MAXIMAL_DATA_LABELS 1000

/*Because triple-asterisk requires choosing a random label, we have to save it globally*/
extern sGrowingTable labels;

/*Just pointers to data structures*/
typedef struct sDataASM{
	sGrowingTable labels;
	sGrowingTable externals;
	sProgramLines statement;
} sDataASM;

/*Final Assembler output*/
typedef struct sOutputASM{
	sGrowingTable entries;
	sGrowingTable externals;
	sGrowingArray data;
	sGrowingArray linkerData;
	Boolean success;
} sOutputASM;

/*Final processing function, creates bytecode. Assumes file is open an ready to go*/
sOutputASM processData(FILE *fp);


#endif


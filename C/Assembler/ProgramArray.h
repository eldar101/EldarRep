#ifndef ProgramArrayHeader
#define ProgramArrayHeader

#include "Vector.h"
#include "ProgramDefinitions.h"

/*Growing array of statements*/
typedef struct sProgramLinesInsied{
	sGrowingArray lines;
	Size curr_size;
} sProgramLinesInsied;

typedef struct sProgramLinesInsied * sProgramLines;

sProgramLines initProgramLinesStruct(void);

sProgramLine* lineAtIndex(sProgramLines prog, unsigned int index);

ERROR_CODE addLine(sProgramLines prog, sProgramLine *s);

/*Frees All associated memory*/
void freeProgramLinesStruct(sProgramLines prog);

#endif

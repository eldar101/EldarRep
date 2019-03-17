#include <stdlib.h>
#include "ProgramArray.h"
/*program array*/

sProgramLines initProgramLinesStruct(void)
{
	sProgramLines program;
	program = (sProgramLines)malloc(sizeof(sProgramLinesInsied));
	if (program == NULL)
		return NULL;
	program->curr_size = 0;
	program->lines = vectorOpen(sizeof(sProgramLine));
	if (program->lines == NULL)
	{
		free(program);
		return NULL;
	}
	return program;
}

sProgramLine* lineAtIndex(sProgramLines prog, unsigned int index)
{
	return (sProgramLine *)vectorGet(prog->lines, index);
}

ERROR_CODE addLine(sProgramLines prog, sProgramLine* s)
{
	ERROR_CODE err = vectorAddElement(prog->lines, s);
	if (err == SUCCESS)
		prog->curr_size++;
	return err;
}

void freeProgramLinesStruct(sProgramLines prog)
{
	vectorClose(prog->lines);
	free(prog);
}

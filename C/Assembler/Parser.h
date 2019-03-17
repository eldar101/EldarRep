#ifndef ParserHeader
#define ParserHeader
#include "ProgramDefinitions.h"
#include "Error.h"

/*Number of bits in a regular word*/
#define DATA_BITS_IN_WORD (10)

/*The Number of bits that can be stored in an opcode parameter, it's  -2 because for ERA (which takes 2 bits)*/
#define IMMEDIATE_BITS_IN_WORD (DATA_BITS_IN_WORD - 2)

/*Parses*/
sGenericErrorMessage parseLine(char *line, int lineNumber, sProgramLine * statement);

#endif

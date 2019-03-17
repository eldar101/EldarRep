#ifndef programDefinitionsHeader 
#define programDefinitionsHeader

#include "Common.h"

#define LABEL_SIZE_MAX 30
#define LABEL_SIZE_MAX_STRING "30"

#define LINE_SIZE_MAX 200
#define LINE_SIZE_MAX_STRING "200"

/*Max size for a Command without the label*/
#define CMD_SIZE_MAX 200

/*To be put in labels before all labels are found*/
#define NO_ADDRESS_AVAILABLE 0

typedef enum {
	DataCntr,
	InstructionCntr
} ePointerType;

typedef enum {
	Addressing_IMMEDIATE = 0,
	Addressing_DIRECT,
	Addressing_MATRIX,
	Addressing_REGISTER
} enumAddressing;

typedef enum {
	Directive_DATA = 0,
	Directive_STRING,
	Directive_MATRIX,
	Directive_ENTRY,
	Directive_EXTERNAL
} eDirctvs;

typedef enum {
	Statement_EMPTY,
	Statement_COMMENT,
	Statement_DIRECTIVE,
	Statement_ACTION
} eTypeOfStatement;

typedef struct sArray {
	unsigned int data[LINE_SIZE_MAX];
	Size Size;
} sArray;

typedef struct sMatrixDefinition {
	unsigned int rows; /*first index*/
	unsigned int cols; /*second index*/
	sArray values; /*values to initialize to*/
} sMatrixDefinition;

/*Description of a label*/
typedef struct sLabelInCode {
	int labelAddr;					/*Address of label (or NO_ADDRESS_AVAILABLE)*/
	char rowName[LABEL_SIZE_MAX + 1];
} sLabelInCode;

/*Description of a parameter*/
typedef struct sParameterInOpcode {
	enumAddressing type;		/*Polymorphistic*/
	int value;					/*for immidiate*/
	int registerIndex;			/*for register*/
	struct {
		int register1Index;
		int register2Index;
	} matrix;					/*add #4, labelIndex[register1Index][register2Index]*/
	sLabelInCode labelIndex;	/*for direct*/
} sParameterInOpcode;

/*Stores information about an action statement*/
typedef struct sOpcodeParameters {
	int firstArg;
	int secondArg;
} sOpcodeParameters;

/*Description of an action statement.
If theres only one parameter, its the SECOND parameter*/
typedef struct sOpcode {
	int opcodeNumber;				/*Opcode index (according to Constants -> Opcodes*/
	int parametersCount;			/*Either 0, 1 or 2*/
	sParameterInOpcode paramOne;	/*First parameter is the source parameter*/
	sParameterInOpcode paramTwo;	/*Second parameter is the destination parameter*/
	sOpcodeParameters indexes;		/*Indexes of the arguments in the line*/
} sOpcode;

/*Description of a directive statement*/
typedef struct sDirective {
	union Data {
		sArray data;
		char string[CMD_SIZE_MAX];	/*The data of the string directive */
		sMatrixDefinition matrix;   /*Matrix directive, its a matrix*/
		sLabelInCode entry;			/*Entry directive; its a label*/
		sLabelInCode external;		/*Extern directive; its a label*/
	} Data;							/*Polyphormistic behavior*/
	int indexArg;					/*The index of the argument in the line*/
	eDirctvs type;					/*The type of the directive; Polyphormistic behavior*/
} sDirective;

/*Stores general indexes about a statement*/
typedef struct sIndexesStatement {
	char text[LINE_SIZE_MAX];	/*The text of the line*/
	int lineNumber;				/*For Error displayment*/
	int postLabel;				/*First non whitespace which is of the command itself*/
} sIndexesStatement;

/*Represents a whole instruction in the file
  If its a comment of empty line, Statement is left untouched.*/
typedef struct sProgramLine {
	union Statment {
		sOpcode action;
		sDirective directive;
	} Statement;				/*Polyphormistic*/
	eTypeOfStatement type;		/*The type of the statement: Polyphormistic bahavior*/
	Boolean labelExists;		/*Does a label exists*/
	char label[LABEL_SIZE_MAX]; /*If a label exists*/
	sIndexesStatement indexes;
} sProgramLine;

#endif
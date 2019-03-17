#ifndef StringHelperHeader
#define StringHelperHeader
#include "Error.h"
#include "Common.h"

/*true if only tabs and spaces. else false*/
Boolean stringIsWhitespace(char *line);

/*Returns index of next non empty char, else end of string*/
int firstNonWhitespace(char *line, int startIndex);

/*gives the length of the next work, i.e. chars until next non-alphanumeric or end*/
int lengthOfNextWord(char *line);

/*Sets labelName to the label, sets startStatement after label. If there is no label, labelName is untouched*/
sGenericErrorMessage findLabelInString(char *line, char *labelName, int *startStatement);

/*Booleans*/

Boolean isChar(char c);

Boolean isDigit(char c);

Boolean isAlphaNumber(char c);

#endif

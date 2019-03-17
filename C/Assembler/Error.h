#ifndef ErrorHeader
#define ErrorHeader

#include "Common.h"

#define ERR_MSG_MAX_SIZE 350

typedef struct sGenericErrorMessage {
	char ErrorMessage[ERR_MSG_MAX_SIZE + 1];	/*Error message to be displayed*/
	int column;									/*The place that the Error occured (index in current line)*/
	Boolean isError;							/*false means no error*/
} sGenericErrorMessage;

/*Create an Error*/
sGenericErrorMessage createError(char *str, int index);

/*Creates a No Error sGenericErrorMessage*/
sGenericErrorMessage createNoError();

/*No Error definition*/
#define NO_ERROR (createNoError())

#endif


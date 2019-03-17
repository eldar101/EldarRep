#include <string.h>
#include "Error.h"
/*Error messages */
sGenericErrorMessage createError(char *str, int index)
{
	sGenericErrorMessage r = { 0 };
	strncpy(r.ErrorMessage, str, STRING_SIZE(r.ErrorMessage));
	r.isError = true;
	r.column = index;

	return r;
}

sGenericErrorMessage createNoError()
{
	sGenericErrorMessage ret = { "", 0, 0 };
	return ret;
}
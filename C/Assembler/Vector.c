#include <stdlib.h>
#include <string.h>
#include "Vector.h"

/* Resizes the generic array to a different size, if necessary*/
static ERROR_CODE resize(sGrowingArray arr, Size new_size)
{
	void * tmp;
	Size actual_new_size;
	if (new_size < arr->allocated_size)
	{
		return SUCCESS; /*Nothing to do*/
	}
	actual_new_size = arr->allocated_size * 2; 
	if (actual_new_size <= new_size) /*Unless, we are asked to enlarge it more*/
		actual_new_size = new_size;

	tmp = realloc(arr->data, arr->size_of_memeber * actual_new_size);
	if (tmp == NULL) /*Allocation failed -> do nothing*/
		return ALLOCATION_FAILED;

	arr->data = tmp;
	arr->allocated_size = actual_new_size;
	return SUCCESS;
}

sGrowingArray vectorOpen(Size size_of_memeber)
{
	sGrowingArray arr = (sGrowingArray)malloc(sizeof(sGrowingArrayInside));

	if (arr == NULL) /*If allocation has failed*/
		return NULL;

	arr->size_of_memeber = size_of_memeber;
	arr->data = calloc(SIZE_START_ARRAY, size_of_memeber);
	if (arr->data == NULL)
	{
		free(arr);
		return NULL;
	}

	arr->size_of_array_current = 0;
	arr->allocated_size = SIZE_START_ARRAY;
	return arr;
}

ERROR_CODE vectorAddInt(sGrowingArray destination, unsigned int value)
{
	ERROR_CODE err;
	if (destination->size_of_memeber > sizeof(value))
		return BAD_SIZE;
	err = resize(destination, destination->size_of_array_current + 1); /*Allocate another element*/
	if (err != SUCCESS)
		return err; /*Rethrow error*/

	*(unsigned int*)(((char *)destination->data) +
		(destination->size_of_array_current * destination->size_of_memeber)) = value;
	destination->size_of_array_current++;
	return SUCCESS;
}

ERROR_CODE vectorAddElement(sGrowingArray destination, void * data)
{
	ERROR_CODE err = resize(destination, destination->size_of_array_current + 1); /*Allocate another element*/
	if (err != SUCCESS)
		return err; /*Rethrow error*/

	memcpy(((char *)destination->data) + (destination->size_of_array_current * destination->size_of_memeber),
		data,
		destination->size_of_memeber);

	destination->size_of_array_current++;
	return SUCCESS;
}

ERROR_CODE vectorInsertMany(sGrowingArray destination, void * data, Size amount)
{
	ERROR_CODE err = resize(destination, destination->size_of_array_current + amount); /*Allocate elements*/
	if (err != SUCCESS)
		return err; /*Rethrow error*/

	memcpy(((char *)destination->data) + (destination->size_of_array_current * destination->size_of_memeber),
		data,
		destination->size_of_memeber * amount);

	destination->size_of_array_current += amount;
	return SUCCESS;
}

void* vectorGet(sGrowingArray destination, unsigned int index)
{
	if (index >= destination->size_of_array_current)
		return NULL;

	return (void *) (((char *)destination->data) + (destination->size_of_memeber * index));

}

void vectorClose(sGrowingArray destination)
{
	free(destination->data);
	free(destination);
}
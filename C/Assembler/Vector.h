#ifndef VectorHeader
#define VectorHeader
#include "Common.h"

#define SIZE_START_ARRAY 10

typedef struct sGrowingArrayInside{
	Size size_of_array_current; /*Amount of cells in place*/
	Size allocated_size;		/*Size allocated in memory*/
	Size size_of_memeber;		/*The size of each element*/
	void * data;				/*A pointer to the first element*/
} sGrowingArrayInside;

typedef sGrowingArrayInside * sGrowingArray;

/*Ctor*/
sGrowingArray vectorOpen(Size size_of_memeber);

/*Frees all allocated resources for the generic array*/
void vectorClose(sGrowingArray destination);

/*this will write the value to the array*/
ERROR_CODE vectorAddInt(sGrowingArray destination, unsigned int value);

/*writes 'data' to the array, copies `size_of_memeber` bytes*/
ERROR_CODE vectorAddElement(sGrowingArray destination, void * data);

/*Copies 'amount' elements, each of size 'size_of_memeber', into destination*/
ERROR_CODE vectorInsertMany(sGrowingArray destination, void * data, Size amount);

/*Receives a pointer to the given value*/
void * vectorGet(sGrowingArray destination, unsigned int index);

#endif

#ifndef commonHeader
#define commonHeader

/*The length of an array is its size divided by the size of any element*/
#define ARRAY_LENGTH(a) (sizeof(a)/(sizeof((a)[0])))

/*When we do string operations, we want to easily access the size of the string.
The size of a string is the amount of cells in it, minus one for null terminator.*/
#define STRING_SIZE(s) (ARRAY_LENGTH(s) - 1)

/*Return type for functions that might fail*/
typedef int ERROR_CODE;

/*CHECK*/
typedef unsigned int Boolean;
typedef unsigned int Size;

#define true 1
#define false 0

/*When an allocation fails*/
#define ALLOCATION_FAILED -10

/*When something is not found*/
#define NOT_FOUND -9

/*When a bad size is specified*/
#define BAD_SIZE -8

/*When something is successful*/
#define SUCCESS 0



#endif
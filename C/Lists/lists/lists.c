/*
 ============================================================================
 Name        : lists.c
 Author      : Eldar Weiss
 Date        : 10/05/2017
 Description : THis program receives input from the user and prints it out using realloc to increase
 the array's size to accommodate the input's length.free(arr);free(arr);free(arr);
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
void printArr(int *arr, int *size); /*declaring function*/

int main()
{
	int count = 0, size = 10;
	char c;
	int *arr = (int *)realloc(NULL,size*sizeof(int)); /* creating array */

	printf("Please enter input: ");
	while ((c=getchar())!=EOF)
		{
		 if (count == size)  /* reaching end of size */
        {
			 size *= 2; /* doubling size*/
			 arr = (int*)realloc(arr,size*sizeof(int)); /* restarting with new size */
        }
		arr[count] = c; /* putting char in array */
        count++;

		}
	printArr(arr, &size); /* printing */
	free(arr); /*freeing memory */
	return 0;
}

/* this function print an array, referred by pointers, using a loop */

void printArr(int *arr, int *size)
{
	int i;
	printf("\nThe input string is: ");
		 for (i = 0; i < *size; i++)
		 {
			printf("%c", arr[i]);
		}
		 printf("\n");
}

/*
 * 		binsem.c
 *  	Created on: 10.11.2018
 *      Author: Eldar Weiss
 *      ID: 303169783
 *      This library defines binary semaphores library of user-level threads.
 */

#include <signal.h>
#include "binsem.h"

/*****************************************************************************
 Initializes a binary semaphore.
 Parameters:
 s - pointer to the semaphore to be initialized.
 init_val - the semaphore initial value. If this parameter is 0, the
 semaphore initial value will be 0, otherwise it will be 1.
 *****************************************************************************/
void binsem_init(sem_t *s, int init_val){
	if(!init_val)
{
		xchg(s,0); //exchanging if init val is 0
	} 
else
{
		xchg(s,1); //exchanging to 1
	}
}

/*****************************************************************************
 The Up() operation.
 Parameters:
 s - pointer to the semaphore to be raised.
 *****************************************************************************/
void binsem_up(sem_t *s)
{
	while(*s == 1) {} //upping the semaphore in a loop
	xchg(s, 1);
}

/*****************************************************************************
 The Down() operation.
 Parameters:
 s - pointer to the semaphore to be decremented. If the semaphore value is
 0, the calling thread will wait until the semaphore is raised by another
 thread.
 Returns:
 0 - on sucess.
 -1 - on a syscall failure.
 *****************************************************************************/
int binsem_down(sem_t *s)
{
	while(xchg(s, 0) == 0)
{
		if(raise(SIGALRM)) //sending signal
{
			return -1; //syscall failure
		}
	}
	return 0; //success
}

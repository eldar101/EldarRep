/*
 * 		ut.c
 *  	Created on: Nov 9, 2018
 *      Author: Eldar Weiss
 *      303169783
 *      this file shows a basic definition for creation and scheduling of user-level threads.
 */
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <ucontext.h>
#include <sys/time.h>
#include <unistd.h>
#include "ut.h"


static int thread_num; 
static int s_thread_size;
static tid_t curr_thread_num;
static ut_slot ut_slot_threads;

static void handler(int signal);

/*****************************************************************************
 Initialize the library data structures. Create the threads table. If the given
 size is otside the range [MIN_TAB_SIZE,MAX_TAB_SIZE], the table size will be
 MAX_TAB_SIZE.

 Parameters:
    tab_size - the threads_table_size.

 Returns:
    0 - on success.
	SYS_ERR - on table allocation failure.
*****************************************************************************/
int ut_init(int tab_size) {
	// initialize this
	if (tab_size < MAX_TAB_SIZE && tab_size > MIN_TAB_SIZE)
 {
		thread_num = tab_size +1; //increment
	} 
else
 {
		thread_num = MAX_TAB_SIZE; //else just the default max
	}

	// memory allocaton for the threads
	if ((ut_slot_threads = malloc(sizeof(ut_slot_threads[0]) * thread_num)) == NULL){
		return SYS_ERR; //if allocation fails
	}
	return 0;
}


/*****************************************************************************
 Add a new thread to the threads table. Allocate the thread stack and update the
 thread context accordingly. This function DOES NOT cause the new thread to run.
 All threads start running only after ut_start() is called.

 Parameters:
    func - a function to run in the new thread. We assume that the function is
	infinite and gets a single int argument.
	arg - the argument for func.

 Returns:
	non-negative TID of the new thread - on success (the TID is the thread's slot
	                                     number.
    SYS_ERR - on system failure (like failure to allocate the stack).
    TAB_FULL - if the threads table is already full.
 ****************************************************************************/
tid_t ut_spawn_thread(void (*func)(int), int arg)
 {
	// We return TAB_FULL when reaching the max
	if (s_thread_size +1 >= thread_num) {
		return TAB_FULL;
	}

	//context created
	ut_slot_threads[s_thread_size].arg = arg;
	ut_slot_threads[s_thread_size].func = func;
	ut_slot_threads[s_thread_size].vtime = 0;

	int getContextRet = getcontext(&ut_slot_threads[s_thread_size].uc);
	if ( getContextRet == -1)
 {	
		return SYS_ERR; //failed context
	}

	ut_slot_threads[s_thread_size].uc.uc_stack.ss_size = STACKSIZE;
	ut_slot_threads[s_thread_size].uc.uc_link = NULL;

	
	if ((ut_slot_threads[s_thread_size].uc.uc_stack.ss_sp = malloc(STACKSIZE)) <= 0) {
		return SYS_ERR; //no space
	}

	makecontext(&(ut_slot_threads[s_thread_size].uc),
			(void(*)(void)) ut_slot_threads[s_thread_size].func, 1,
			ut_slot_threads[s_thread_size].arg);

	s_thread_size++;
	return s_thread_size - 1; //current thread
}


/*****************************************************************************
 Starts running the threads, previously created by ut_spawn_thread. Sets the
 scheduler to switch between threads every second (this is done by registering
 the scheduler function as a signal handler for SIGALRM, and causing SIGALRM to
 arrive every second). Also starts the timer used to collect the threads CPU usage
 statistics and establishes an appropriate handler for SIGVTALRM,issued by the
 timer.
 The first thread to run is the thread with TID 0.

 Parameters:
    None.

 Returns:
    SYS_ERR - on system failure (like failure to establish a signal handler).
    Under normal operation, this function should start executing threads and
	never return.
 ****************************************************************************/
int ut_start(void) {
	struct sigaction sa;
	struct itimerval itv;

	/* Initialize the data structures for SigALRM*/
	sa.sa_flags = SA_RESTART;
	sigfillset(&sa.sa_mask);
	sa.sa_handler = handler;

	/* set up vtimer*/
	itv.it_interval.tv_sec = 0;
	itv.it_interval.tv_usec = 100000;
	itv.it_value = itv.it_interval;

	printf("Press CTRL-C to print the statistics and terminate\n"); //print statistics

	if (setitimer(ITIMER_VIRTUAL, &itv, NULL) < 0 || sigaction(SIGVTALRM, &sa, NULL) < 0|| (sigaction(SIGALRM, &sa, NULL) < 0))
		return SYS_ERR; //in case timer fails or sigction doesn't wor

	alarm(1); //set alarm
	curr_thread_num = 0;

	setcontext(&(ut_slot_threads[0].uc)); //setting context for first thread


	perror("Error in set first context"); 	// in case of failure for first thread's context
	exit(1);
}

/* Handler for scheduling and handling vtime*/
static void handler(int signal)
 {
	if (signal == SIGALRM)
 {
		alarm(1);
		// set the next context. works according to Round Robin principle
		tid_t lastcurr_thread_num = curr_thread_num;
		curr_thread_num++;
		if (curr_thread_num +1 == thread_num)
 {
			curr_thread_num = 0;
		}
		if(swapcontext(&ut_slot_threads[lastcurr_thread_num].uc, &ut_slot_threads[curr_thread_num].uc) == -1)
{
			perror("Error: Swapping contexts failed");
			exit(1);
		}
	}
	else if (signal == SIGVTALRM)
{
		ut_slot_threads[curr_thread_num].vtime += 100; //adding time to cpu
	}
}

/*****************************************************************************
 Returns the CPU-time consumed by the given thread.

 Parameters:
    tid - a thread ID.

 Returns:
	the thread CPU-time (in millicseconds).
 ****************************************************************************/
unsigned long ut_get_vtime(tid_t tid)
{
	if(tid >= 0 && tid <= thread_num) //checking range of tid, to see if it's valid
{
		return ut_slot_threads[tid].vtime;
	}
	return SYS_ERR;
}


/*
 * par.c
 * This program receive an input file that may have parentheses in it. it prints out an error when a for each line
 * that doesn't have a matching closing and opening square and round braces. it also prints out an error for curly braces
 * that don't have a matching closing/opening in all the file. all error lines include the printed line,the line number and the mismatched brace
 * Created on: Mar 28, 2017
 * Author: Eldar Weiss
 */

#include <stdio.h>

#define MAX 100     /* maximum input line size */

int check_line(char line[],char tempLine[],int lim,int lineNum); /*declaring functions*/
int push(int st); /*stack*/
int pop();
int isfull();
int isempty();


enum status {OUT,IN_STRING,LEFT_SLASH,IN_COMMENT,RIGHT_STAR,RIGHT_SLASH,IN_CHAR,RIGHT_SLASH_CHAR}; /*states*/
char stack[MAX]; /*stack*/
int top = -1; /*top of stack*/

int main()
{
int lineNum,i,state,c,j,k;
char tempLine[MAX];
char line[MAX];
state=OUT;
lineNum=1;
k = i = j = 0;
	while ((c=getchar())!=EOF)
	{
		tempLine[j++]=c;
		switch(state){
				case OUT:
					if (c == '/')
						state = LEFT_SLASH;
					else {
						line[i++]=c;
						if (c == '\"')
						state = IN_STRING;
						if (c=='\'')
						state = IN_CHAR;
					}
					break;
				case LEFT_SLASH:
					if (c == '*')
						state = IN_COMMENT;
					else {
						line[i++]=c;
						state = OUT;
					}
					break;
				case IN_COMMENT:
					if (c == '*')
						state = RIGHT_STAR;
					break;
				case RIGHT_STAR:
					if (c == '/')
						state = OUT;
					else if (c != '*')
						state = IN_COMMENT;
					break;
				case IN_STRING:
					if(c=='\\')
					state=RIGHT_SLASH;
					else if (c == '\"')
						state = OUT;
					break;
				case RIGHT_SLASH:
					state=IN_STRING;
					break;
				case IN_CHAR:
					if(c=='\\')
					state=RIGHT_SLASH_CHAR;
					else if (c == '\'')
					state = OUT;
					break;
				case RIGHT_SLASH_CHAR:
					state = IN_CHAR;
					break;
			}
		if (c=='\n'){/*end of line (not EOF)*/
		line[i++] = tempLine[j++] = c;
		line[i] = tempLine[j]= '\0';/*set the end of the line array*/
		i = j = 0;
		k = k+check_line(line,tempLine,MAX,lineNum);/*check this line ,if t is different from 0 the {} parentheses are'nt balanced */
		if (k<0)
		{/*there is closing parentheses without opening*/
		printf("\n Error:line %d '}' without begin '{'\n",lineNum);
		k = 0;
		}
		++lineNum;
		}
	}
if (k>0)/*missing }*/
	printf("\nError:line %d missing '}'\n",lineNum);
return 0;
}

/*return 0 if the the curly parentheses are balanced a different number if not,
 * check line,check if all the parentheses are balanced*/
int check_line(char s[],char o[],int lim,int lineNum)
{
char braces[MAX];
char currChar,nextChar;
int index,c,i;
index = i = c = 0;
while (s[index]!='\0'){/*check the {},set all braces of the line in array*/
	if ((s[index]=='{'||s[index]=='}') && s[index+1] !='\n')/*check if {,} is only at the end of the line*/
		printf("\nError:line %d {,} has to be in the end of the line.The line:%s",lineNum,o);
	if ((s[index]== '{') && s[index+1] == '\n')
		c=1;
	if ((s[index]=='}') && s[index+1] == '\n')
		c=-1;
	if (s[index]== '['||s[index]== ']'||s[index]== '('||s[index]== ')' ){
		braces[i++]=s[index];
	}
	++index;
}
braces[i]='\0';
i=0;
while (braces[i]!='\0' && i < MAX)
{/*check all braces in the line*/
	currChar=braces[i];
	if (currChar=='(' || currChar=='[')
		push(currChar);
	if (currChar==')' || currChar==']' ) /*checking closing braces*/
	{
		if (isempty())
			printf("\nError:line %d ,'%c' without opening bracket. The line:%s\n",lineNum,braces[i],o);
		else
		{
			nextChar = pop();/*next*/
			if ((nextChar=='[' && currChar!=']') || (nextChar=='(' && currChar!=')' )) /*checking closing braces*/
				printf("\nError:line %d ,'%c is'nt match to the opening bracket. The line:%s\n",lineNum,braces[i],o);
		}
	}
	++i;/*increment*/
}
/*checking after end of loop*/
braces[i]='\0';
if (!isempty())
	printf("\nError:line %d ,'%c' without closing bracket. The line:%s\n",lineNum,pop(),o);
return c;
}

/*push variable into stack*/
int push(int st) {

   if(!isfull()) /*if it isn't empty*/
   {
      top = top + 1; /*increase*/
      stack[top] = st;
   }
   return 0;
}

/*return first variable in the stack and remove it from stack*/
int pop()
{
   int st;
   if(!isempty())
   {
      st = stack[top];
      top = top - 1;
      return st;
   }
      return 0;
   }

/*check if the stack is full*/
int isfull()
{

   if(top == MAX)
      return 1; /*return if stack has reached it's max*/
   else
      return 0;
}

/*check if the stack is empty*/
int isempty()
{
   if (top == -1)
      return 1;
   else
      return 0;
}

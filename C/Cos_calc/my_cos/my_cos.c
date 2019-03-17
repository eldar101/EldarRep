/*
 * my_cos.c
 * This program calculates the cosine of a given value by calculating it in a formula up to the accuracy of 1.0e-6
 * The program calculates and sums and the series until the last number is 10^-6 and then returns the sum;
 * We then print a value's cosine by Math.h's cos function in order to validate our answer.
 * Created on: Mar 26, 2017
 * Author: Eldar Weiss
 */

#include <stdio.h>
#include <math.h>
double my_cos(double); /* declaring functions */
const double min = 0.0000001; /*10e-6*/

int main()
{
double input; /*input*/
printf("Please enter an integer: \n");
scanf("%lf",&input); /*receive input*/
printf("the integer is: %f\n", input);
printf("Cosine in My cos: %f\n", my_cos(input)); /*call my function and print */
printf("Cosine in Math.h: %f\n", cos(input)); /*call math.cos and print */
return 0;
}

double	my_cos(double num)
{
double i = 0, sum = 0, temp; /*declaring variables*/
num = fmod(num,2*M_PI); /*for calculating cosine*/
temp = 1; /*first number in the series*/
while (fabs(temp)>= min)
{
sum += temp; /*summing up the series */
temp *= -1*(num/((2*i)+1))*(num /((2*i)+2)); /*calculating each number in the series*/
i++; /*next number in the series*/
}
return sum; /*cosine*/
}

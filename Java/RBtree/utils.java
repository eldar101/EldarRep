/*this file contains the definitions of functions used for filling and printing arrays*/
/*------------------------------------------------------------------------------------------------------------*/
class Utils
{
    // fill array with random numbers that range from zero to a limit
    public  void fillRandArr(int[] arr, int limit)
    {
        for (int i =0; i< arr.length ; i++)
        {
            arr[i] = (int) (Math.random() * (limit));
        }
    }
    // print said array with brackets
    public void printArr(int[] arr)
    {
        System.out.print("[");
        for (int i =0; i< arr.length-1 ; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println(arr[arr.length-1] +"]\n");
    }

}

//This class defines a person and traits like first name, last name, id, year of birth and lexicographic comparison

package q1;

public class Person implements Comparable<Person> {
    private int birthYear;
    private String firstName;
    private String lastName;
    private String id;

    public Person() {
        birthYear = 0;
        firstName = "default";
        lastName = "default";
        id = "default";
    }

    public Person(int birthYear, String firstName, String lastName, String id) {
        this.birthYear = birthYear;
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }

    //we compare a person lexicographically
    @Override
    public int compareTo(Person o) {
        if (this == null || o == null) {
            return 4;
        }
        if (stringCompare(this.lastName, o.lastName) > 0) { //the first Lastname is larger
            return 1;
        } else if (stringCompare(this.lastName, o.lastName) < 0)
        { //the other Lastname is larger
            return -1;
        } else if (stringCompare(this.lastName, o.lastName) == 0) { //if Lastname is equal, start comparing firstName:


            if (stringCompare(this.firstName, o.firstName) > 0) {  //the first firstName is larger
                return 1;
            }

            if (stringCompare(this.firstName, o.firstName) < 0) {//the other firstName is larger
                return -1;
            }
            if (stringCompare(this.firstName, o.firstName) == 0) { //names are completely equal
                return 0;
            }
        }
        return 0;
    }


    // This method compares two strings
    // lexicographically without using
    // library functions
    public static int stringCompare(String str1,
                                    String str2) {
        for (int i = 0; i < str1.length() &&
                i < str2.length(); i++) {
            if ((int) str1.charAt(i) ==
                    (int) str2.charAt(i)) {
                continue;
            } else {
                return (int) str1.charAt(i) -
                        (int) str2.charAt(i);
            }
        }

        // Edge case for strings like
        // String 1="Geeky" and String 2="Geekyguy"
        if (str1.length() < str2.length()) {
            return (str1.length() - str2.length());
        } else if (str1.length() > str2.length()) {
            return (str1.length() - str2.length());
        }

        // If none of the above conditions is true,
        // it implies both the strings are equal
        else {
            return 0;
        }
    }


    //Tostring method that in order for us to print the details
    public String toString() {
        return "First name: " + firstName + "\nLast name: " + lastName + "\nYear of birth: " + birthYear + "\nID: " + id;
    }

}//end of class Person

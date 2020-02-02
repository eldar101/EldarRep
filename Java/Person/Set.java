//This generic class defines sets for variables, as requested in q1a, using ArrayLists.

package q1;
//libraries

import java.util.ArrayList;
import java.util.Iterator;

public class Set<E> {
    private ArrayList<E> group; //generic Array List for each set

    //constructor with no parameters for empty set
    public Set() {
        group = new ArrayList<>();

    } //end of constructor

    //constructor that receives a generic array as a parameter and fills the set with its members
    public Set(E[] array) {
        group = new ArrayList<>();
        for (int i = 0; i < array.length; i++) { //set grows according to array size
            this.insert(array[i]);
        }
    } //end of constructor

    //Union method to receive another set and we add it to our set
    public void union(Set<E> other) {

        for (int i = 0; i < other.group.size(); i++) { //loop according to the other set's size
            this.insert(other.group.get(i));
        }

    } //end of union

    //intersect method which deletes members that are unique to the other set, thus leaving the intersection, by easy comparison
    public void intersect(Set<E> other) {
        for (int i = 0; i < this.group.size(); i++) { //we check if each member of the other group appears in our group
            if (!other.isMember(this.group.get(i))) {
                delete(this.group.get(i)); //if unique, delete off our group
                i--;
            }
        }


    } //end of intersect

    //This method checks if a set is a subset or not.
    public boolean isSubset(Set<E> other) {
        for (int i = 0; i < other.group.size(); i++) {
            if (!this.isMember(other.group.get(i))) { //if none of the set appear in our set, it is not a subset
                return false;
            }
        } //otherwise it is a subset
        return true;

    }

    //This method simply returns true when a generic set is a set member or not by scanning
    public boolean isMember(E other) {
        return this.groupMember(other) != -1;
    }
    //end of IsMember

    //This method uses equals to compare each member of the group to the other or not
    private int groupMember(E other) {
        for (int i = 0; i < this.group.size(); i++) {
            if (group.get(i).equals(other)) {
                return i; //as long as it doesn't return -1, it is a member
            }
        }
        return -1;
    }

    //This method insert receives a variable and add it to the set, but only after we scanned and checked it's not already in our set
    public void insert(E other) {
        if (!this.isMember(other)) {
            group.add(other);
        }
    } //end of insert

    //the method removes member from set,after verifying it is on our set
    public void delete(E other) {
        if (this.isMember(other)) {
            group.remove(groupMember(other));
        }
    } //end of delete

    //This method allows us to iterate members of the set
    public Iterator<E> iterator() {
        return group.iterator();
    }

    //simple method to print the set with proper string syntax
    public String toString() {
        String retVal = "";
        for (int i = 0; i < this.group.size(); i++) {
            retVal += group.get(i).toString() + " ";
        }
        return retVal;
    } //end of toString method
} //end of class

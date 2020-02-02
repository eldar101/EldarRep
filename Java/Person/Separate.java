//This class is used to separate members during an iteration in order to find the youngest person

package q1;

import java.util.Iterator;

public class Separate {

    public static <E extends Comparable> E minSet(Set<E> set) {
        E min = null;
        Iterator<E> s = set.iterator();
        if (!s.hasNext()) { //if empty
            return null;
        }


        for (Iterator<E> it = set.iterator(); it.hasNext(); ) {
            E next = it.next();
            if (next.compareTo(min) == 4) {
                min = next;
            }
            if (next.compareTo(min) == -1) { //finding the minimum by comparison
                min = next;
            }
        }

        return min; //return the minimum
    } //end of minSet
} //end of class

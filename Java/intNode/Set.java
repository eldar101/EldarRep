/**
 *
 * @author Eldar Weiss
 *	This pgorgram contains a set of odd numbers
 */
public class Set {
    private IntNode _head;
    private int _counter;

    /**
     * Constructor for objects of class Set
     */
    //The memory complexity is O(1) and the runtime efficiency is O(1)
    public Set() {
        _head = null;
        _counter = 0;
    }

    /**
     * Copy constructor for objects of class Set
     * @param other another Set
     */
    //The memory complexity is O(n) and the runtime efficiency is O(n)
    public Set(Set other) {
        _counter = other._counter;

        if (other.isEmpty())
            return;

        int x = other._head.getValue();
        IntNode h = new IntNode(x, null);
        _head = h;

        for (IntNode node = _head, otherNode = other._head.getNext(); otherNode != null; otherNode = otherNode.getNext()) {
            int val = otherNode.getValue();
            IntNode temp = new IntNode (val, null);
            node.setNext(temp);
            node = node.getNext();
        }
    }

    /**
     * Return true if the set is empty
     * @return true if the set is empty
     */
    //The memory complexity is O(1) and the runtime efficiency is O(1)
    public boolean isEmpty() {
        if (_head == null)
            return true;
        else
            return false;
    }

    /**
     * Adding a number to the set
     * @param x a number
     */
    //The memory complexity is O(1) and the runtime efficiency is O(n)
    public void addToSet(int x) {
        if (x % 2 == 0 || isMember(x))
            return;

        if (isEmpty()) {
            IntNode temp = new IntNode(x, null);
            _head = temp;
            _counter++;
        }
        //if x is smaller than _head
        else if (x < _head.getValue()) {
            IntNode temp = new IntNode (x, _head);
            _head = temp;
            _counter++;
        }
        //if (_head.getValue() < x)
        else {
            IntNode i;
            //looking for the place to put x
            for (i = _head; i.getNext() != null && i.getNext().getValue() < x; i = i.getNext());
            //if it's the last in the list
            if (i.getNext() == null) {
                IntNode temp = new IntNode(x, null);
                i.setNext(temp);
                _counter++;
                return;
            }
            //than it's the right place to add it because it's not equal
            IntNode temp = new IntNode(x, i.getNext());
            i.setNext(temp);
            _counter++;
        }
    }


    /**
     * Remove the number x from the set
     * @param x a number
     */
    //The memory complexity is O(1) and the runtime efficiency is O(n)
    public void removeFromSet(int x) {
        if (isEmpty() || x % 2 == 0 || !isMember(x))
            return;
            //if it's the _head
        else if (_head.getValue() == x) {
            _head = _head.getNext();
            _counter--;
        }
        else {
            IntNode curr = _head, next = _head.getNext();
            while (next != null && next.getValue() < x) {
                curr = next;
                next = next.getNext();
            }

            if (next == null)
                return;
            if (next.getValue() == x) {
                curr.setNext(next.getNext());
                _counter--;
            }
        }
    }


    /**
     * return true if the number is in the set
     * @param num a number
     * @return true if the number is in the set
     */
    //The memory complexity is O(1) and the runtime efficiency is O(n)
    public boolean isMember(int num) {
        if (isEmpty() || num % 2 == 0)
            return false;
        else if (num < _head.getValue())
            return false;
        else {
            for (IntNode i = _head; i != null && i.getValue() <= num; i = i.getNext()) {
                if (i.getValue() == num)
                    return true;
            }
        }
        return false;
    }

    /**
     * Return true if the sets has the same objects
     * @param other another list
     * @return true if the sets has the same objects
     */
    //The memory complexity is O(1) and the runtime efficiency is O(n)
    public boolean equal(Set other) {
        if (_counter != other._counter)
            return false;
        //if both of them are empty
        if (_head == null && other._head == null)
            return true;

        for (IntNode node = _head, otherNode = other._head; node != null && otherNode != null; node = node.getNext(), otherNode = otherNode.getNext()) {
            if (otherNode.getValue() != node.getValue())
                return false;
        }
        return true;
    }

    /**
     * Return the number of elements in the set
     * @return the number of elements in the set
     */
    //The memory complexity is O(1) and the runtime efficiency is O(1)
    public int numOfElements() {
        return _counter;
    }

    /**
     * Return true if all the numbers of the set given appear in the current set
     * @param other another set
     * @return true if all the numbers of the set given appear in the current set
     */
    //The memory complexity is O(1) and the runtime efficiency is O(n)
    public boolean subSet(Set other) {
        if (other.isEmpty())
            return true;
        //if the given list is bigger then the current one
        if (_counter < other._counter)
            return false;

        IntNode node, otherNode;
        for (node = _head, otherNode = other._head; node != null && otherNode != null; node = node.getNext()) {
            if (node.getValue() == otherNode.getValue()) {
                otherNode = otherNode.getNext();
            }
        }
        //meaning all the numbers appeared
        if (otherNode != null)
            return false;
        else
            return true;
    }

    /**
     * Return a new set with numbers that appear at least in one of the sets
     * @param other another set
     * @return a new set with numbers that appear at least in one of the sets
     */
    //The memory complexity is O(n) and the runtime efficiency is O(n)
    public Set union(Set other) {
        //Than it's this set
        if (subSet(other)) {
            Set res = new Set(this);
            return res;
        }
        //Then it's the other set
        else if (other.subSet(this)) {
            Set res = new Set(other);
            return res;
        }
        //They are not the same sets and not empty
        else {
            Set res = new Set();
            int val1 = _head.getValue(), val2 = other._head.getValue();
            if (val1 <= val2) {
                IntNode temp = new IntNode(val1, null);
                res._head = temp;
            }
            //if (val2 < val1)
            else {
                IntNode temp = new IntNode(val2, null);
                res._head = temp;

            }
            res._counter++;
            return union(_head, other._head, res._head, res);
        }
    }


    //nodeSet1 - the node's of this set
    //nodeSet2 - the node's of the other set
    //nodeRes - the new set's node
    //res - the new set
    //The memory complexity is O(n) and the runtime efficiency is O(n)
    private Set union(IntNode nodeSet1, IntNode nodeSet2, IntNode nodeRes, Set res) {
        if (nodeSet1 == null && nodeSet2 == null)
            return res;

        else if (nodeSet1 == null) {
            int val = nodeSet2.getValue();
            IntNode temp = new IntNode (val, null);
            nodeRes.setNext(temp);
            res._counter++;
            return union(nodeSet1, nodeSet2.getNext(), nodeRes.getNext(), res);
        }

        else if (nodeSet2 == null) {
            int val = nodeSet1.getValue();
            IntNode temp = new IntNode (val, null);
            nodeRes.setNext(temp);
            res._counter++;
            return union(nodeSet1.getNext(), nodeSet2, nodeRes.getNext(), res);
        }

        //if (nodeSet1 != null && nodeSet2 != null)
        else {
            int val1 = nodeSet1.getValue(), val2 = nodeSet2.getValue(), valRes = nodeRes.getValue();

            if (val1 < val2) {
                if (val1 == valRes)
                    return union(nodeSet1.getNext(), nodeSet2, nodeRes, res);
                else {
                    IntNode temp = new IntNode(val1, null);
                    nodeRes.setNext(temp);
                    res._counter++;
                    return union(nodeSet1.getNext(), nodeSet2, nodeRes.getNext(), res);
                }
            }
            else if (val2 < val1) {
                if (val2 == valRes)
                    return union(nodeSet1, nodeSet2.getNext(), nodeRes, res);
                else {
                    IntNode temp = new IntNode(val2, null);
                    nodeRes.setNext(temp);
                    res._counter++;
                    return union(nodeSet1, nodeSet2.getNext(), nodeRes.getNext(), res);
                }
            }
            //if (val1 == val2)
            else {
                if (val1 == valRes)
                    return union(nodeSet1.getNext(), nodeSet2.getNext(), nodeRes, res);
                else {
                    IntNode temp = new IntNode(val1, null);
                    nodeRes.setNext(temp);
                    res._counter++;
                    return union(nodeSet1.getNext(), nodeSet2.getNext(), nodeRes.getNext(), res);
                }
            }
        }
    }


    /**
     * Return a new set with the numbers that appear in the current set and the given one
     * @param other another set
     * @return a new set with the numbers that appear in the current set and the given one
     */
    //The memory complexity is O(n) and the runtime efficiency is O(n)
    public Set intersection(Set other) {
        //Than it's this set
        if (subSet(other)) {
            Set res = new Set(other);
            return res;
        }
        //Then it's the other set
        else if (other.subSet(this)) {
            Set res = new Set(this);
            return res;
        }
        //They are not the same sets and not empty
        else {
            Set res = new Set();
            IntNode temp = new IntNode(0, null);
            res._head = temp;
            return intersection(_head, other._head, res._head, res);
        }
    }

    //nodeSet1 - the node's of this set
    //nodeSet2 - the node's of the other set
    //nodeRes - the new set's node
    //res - the new set
    //The memory complexity is O(n) and the runtime efficiency is O(n)
    private Set intersection(IntNode nodeSet1, IntNode nodeSet2, IntNode nodeRes, Set res) {
        if (nodeSet1 == null || nodeSet2 == null) {
            //Because the first node is dummy
            res._head = res._head.getNext();
            return res;
        }
        else {
            int val1 = nodeSet1.getValue(), val2 = nodeSet2.getValue();
            if (val1 == val2) {
                IntNode temp = new IntNode(val1, null);
                nodeRes.setNext(temp);
                res._counter++;
                return intersection(nodeSet1.getNext(), nodeSet2.getNext(), nodeRes.getNext(), res);
            }
            else if (val1 < val2)
                return intersection(nodeSet1.getNext(), nodeSet2, nodeRes, res);
                //if (val2 < val1)
            else
                return intersection(nodeSet1, nodeSet2.getNext(), nodeRes, res);
        }
    }

    /**
     * Return a new set that has the numbers that are in this set and not in the other set
     * @param other another set
     * @return a new set that has the numbers that are in this set and not in the other set
     */
    //The memory complexity is O(n) and the runtime efficiency is O(n)
    public Set difference(Set other) {
        if (isEmpty() || other.isEmpty()) {
            Set res = new Set(this);
            return res;
        }
        else {
            Set intersection = new Set(intersection(other));
            Set res = new Set(this);

            if (intersection.isEmpty())
                return res;
            else {
                IntNode temp = new IntNode(0, res._head);
                res._head = temp;
                return difference(res._head, res._head.getNext(), intersection._head, res);
            }
        }
    }

    //prev - the previous node of the res set
    //curr - the current node of the res set
    //nodeInter - the node's of the intersection set
    //res - the new set
    //The memory complexity is O(1) and the runtime efficiency is O(n)
    private Set difference(IntNode prev, IntNode curr, IntNode nodeInter, Set res) {
        if (nodeInter == null) {
            //Because the first node is dummy
            res._head = res._head.getNext();
            return res;
        }

        else {
            int val = curr.getValue(), valInter = nodeInter.getValue();

            if (val == valInter) {
                prev.setNext(curr.getNext());
                res._counter--;
                return difference(prev, curr.getNext(), nodeInter.getNext(), res);
            }

            //if (val < valInter)
            else
                return difference(curr, curr.getNext(), nodeInter, res);
        }
    }


    /**
     * Prints the set
     */
    //The memory complexity is O(1) and the runtime efficiency is O(n)
    public String toString() {
        if (_head == null) {
            return "{}" + "\n";
        }
        String res = "{";
        IntNode i;
        for (i = _head; i.getNext() != null; i = i.getNext())
            res = res + i.getValue() + ",";
        res = res + i.getValue() + "}" + "\n";
        return res;
    }

}

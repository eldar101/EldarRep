/**
 * Created by eldar on 22/01/2017.
 */
public class IntNode {
    private int _value;
    private IntNode _next;

    /**
     * Constructor for objects of class IntNode
     * @param v a number
     * @param n the nest node
     */
    public IntNode(int v, IntNode n) {

        _value = v;
        _next = n;
    }


    /**
     * Return the value of the node
     * @return the value of the node
     */
    public int getValue()
    {
        return _value;
    }

    /**
     * Return the next node
     * @return the next node
     */
    public IntNode getNext()
    {
        return _next;
    }

    /**
     * Set the value of the node with the given number
     * @param v a number
     */
    public void setValue(int v)
    {
        _value = v;
}

    /**
     * Set the next node
     * @param n the next node
     */
    public void setNext(IntNode n)
    {
        _next = n;
    }

}


package ex11Q2;

/**
 * Represents a pair of two arbitrary objects grouped together.
 */
public class Pair<T, S> {
    private T _firstElement; // The first element in the pair.
    private S _secondElement; // The second element in the pair.

    /**
     * Creates a Pair object grouping together the two input elements.
     */
    public Pair(T firstElement, S secondElement) {
        _firstElement = firstElement;
        _secondElement = secondElement;
    }

    /**
     * Returns the first element in the pair.
     */
    public T getFirstElement() {
        return _firstElement;
    }

    /**
     * Returns the second element in the pair.
     */
    public S getSecondElement() {
        return _secondElement;
    }

    /**
     * Sets the first element's field in the pair to reference the input object.
     */

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object other) {
        Pair<T, S> otherPair = null;
        try {
            otherPair = (Pair<T, S>) other;
        } catch (ClassCastException e) {
            return false;
        }
        return (this.getFirstElement().equals(otherPair.getFirstElement())
                &&
                this.getSecondElement().equals(otherPair.getSecondElement()));
    }
}

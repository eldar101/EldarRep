package ex11Q2;

/**
 * Represents an ordered pair of (x,y) coordinates in a 2-D dimensional Cartesian coordinate system.
 */
public class Position {
    int _row; // The index of the row of a cell in a 2-D automaton's space.
    int _col; // The index of the column of a cell in a 2-D automaton's space.

    /**
     * Creates a new Position object representing a (row, col) ordered pair consisting of the input
     * coordinates.
     */
    public Position(int row, int col) {
        setRow(row);
        setCol(col);
    }

    /**
     * Sets the row index contained in the object to the input value
     */
    public void setRow(int row) {
        _row = row;
    }

    /**
     * Sets the column index contained in the object to the input value
     */
    public void setCol(int col) {
        _col = col;
    }

    /**
     * Returns the index of the row  of a cell in a 2-D automaton's space whose position is represented
     * by the object.
     */
    public int getRow() {
        return _row;
    }

    /**
     * Returns the index of the column  of a cell in a 2-D automaton's space whose position is
     * represented by the object.
     *
     * @return The index of the column  of a cell in a 2-D automaton's space whose position is
     * represented by the object.
     */
    public int getCol() {
        return _col;
    }
}

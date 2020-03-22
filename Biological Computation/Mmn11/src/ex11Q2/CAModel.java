package ex11Q2;

import java.util.ArrayList;

public class CAModel {
    private static final int SIZE = 10; // The number of cells on one side of the square space
    // of a 2-D automaton.
    // Total number of cells is SIZE x SIZE.
    private static final int NUM_OF_MEN = 50; // Number of men to include in the CA's cells.
    private static final int NUM_OF_WOMEN = 50; // Number of women to include in the CA's cells.

    private State[][] _states; // The state of each cell in the CA.
    private int _numberOfGenerations; // Number of generations passed since the start of the current
    // run.

    /**
     * Creates a new model object with initial states.
     */
    public CAModel() {
        initializeStates();
    }

    /**
     * Creates a new collection of "empty" states for the automaton's states and sets it as
     * the current collective state of the automaton.
     */
    private void initializeStates() {
        _states = new State[SIZE][SIZE];
        _numberOfGenerations = 0;
        CollectionUtilities.setNullReferences(_states);
    }

    /**
     * Returns the number of the current generation in the current run of the automaton.
     *
     * @return The number of the current generation in the current run of the automaton.
     */
    public int getGenerationNumber() {
        return _numberOfGenerations;
    }

    /**
     * Increments the number of generations that have passed since the start of the current run
     * of the CA.
     */
    public void incrementGeneration() {
        _numberOfGenerations++;
    }

    /**
     * Sets the states of the automaton's cells to an "empty" state and randomly fills them
     * with the defined number of "man" and "woman" states.
     */
    public void resetModel() {
        initializeStates();
        fillInitialStates();
    }

    /**
     * Randomly fills the cells of the automaton with "man" or "woman" states.
     */
    private void fillInitialStates() {
        ArrayList<Position> possiblePositions = getInitialPossiblePositions();
        randomizedFillEmptyCells(State.StateType.MAN, possiblePositions);
        randomizedFillEmptyCells(State.StateType.WOMAN, possiblePositions);
    }

    /**
     * Returns all initially empty cells where new males and females can be placed.
     *
     * @return All initially empty cells where new males and females can be placed.
     */
    private ArrayList<Position> getInitialPossiblePositions() {
        ArrayList<Position> positions = new ArrayList<Position>();
        for (int i = 0; i < _states.length; i++)
            for (int j = 0; j < _states.length; j++)
                if (State.isEmptyState(_states[i][j]))
                    positions.add(new Position(i, j));
        return positions;
    }

    /**
     * Fills randomly selected cells of the automaton and sets their state to the input
     * type.
     *
     * @param stateType The type to set for each randomly selected state.
     * @param positions Currently empty cells.
     */
    private void randomizedFillEmptyCells(State.StateType stateType,
                                          ArrayList<Position> positions) {
        Position position;
        int numOfObjects = 0;
        int numOfPossiblePositions;
        int positionIndex;
        int row;
        int col;
        if (stateType == State.StateType.MAN)
            numOfObjects = NUM_OF_MEN;
        else if (stateType == State.StateType.WOMAN)
            numOfObjects = NUM_OF_WOMEN;
        for (int i = 0; i < numOfObjects; i++) {
            numOfPossiblePositions = positions.size();
            positionIndex = RNGUtilities.generateRandomInteger(0, numOfPossiblePositions - 1);
            position = positions.remove(positionIndex);
            row = position.getRow();
            col = position.getCol();
            _states[row][col] = new State(stateType);
        }
    }

    /**
     * Returns a copy of a structure containing data about the state of each cell in the CA.
     *
     * @return a copy of a structure containing data about the state of each cell in the CA.
     */
    public State[][] getCurrentStates() {
        State[][] newStates = new State[SIZE][SIZE];
        for (int i = 0; i < newStates.length; i++)
            for (int j = 0; j < newStates[i].length; j++)
                if (_states[i][j] != null)
                    newStates[i][j] = (State) _states[i][j].clone();
        return newStates;
    }

    /**
     * Sets the input states to be the new states contained in the model.
     *
     * @param newStates The new states to save as the current states of the automaton's cells.
     */
    public void setStates(ex11Q2.State[][] newStates) {
        _states = newStates;
    }
}

package ex11Q2;

import java.util.Arrays;

/**
 * Represents the state of a single cell of the CA.
 */
public class State {
    /**
     * The type of object represented by the state (e.g. a man).
     */
    public enum StateType {
        MAN, // The state represents a man.
        WOMAN, // The state represents a woman.
        COUPLE // The state represents a couple made up of a man and a woman.
    }

    public static final int IRRELEVANT = -1; // Denotes when a certain index value is
    // irrelevant (e.g. when the state represents
    // a man, the _womanIndex and _combinedRelationshipIndex
    // fields are irrelevant.
    // Also used to denote when other values related to
    // a state's operation are irrelevant and should
    // not be used as if they are "correct" values.
    // Required: For code to function properly, it
    // must be satisfied that IRRELEVANT < MIN_RAND_NUM.

    public static final int MIN_RAND_NUM = 0; // The minimum value a relationship index can have.
    public static final int MAX_RAND_NUM = 100; // The maximum value a relationship index can have.
    // Used to determine whether a neighbor of a single man or woman of the opposite sex is a
    // suitable partner.
    public static final int PAIRING_THRESHOLD = (int) ((MAX_RAND_NUM - MIN_RAND_NUM) * 0.95);
    public static final int MAX_FRMR_PARTNERS_REMEMBRED = 5; // The maximum number of former partners
    // a single male/woman represented by
    // can remember in order to avoid pairing
    // with worse partners in the future.
    // Minimum is assumed to be 0.

    private static int _pastPartnersMemoryCapacity; // Maximum number of past partners' relationship
    // indices a state can remember.

    private StateType _stateType; // The type of object represented by the state.
    private int _manIndex; // The man's relationship index.
    private int _womanIndex; // The woman's relationship index.
    private int _combinedRelationshipIndex; // The combined relationship index.
    private boolean _isPaired; // Whether in the calculation of the states of the automaton's
    // cells in the next generation the single man/woman represented by
    // the state has been already paired with another person of the
    // opposite sex.
    private StateType _pairedMember; // If the current state represents a couple and a member
    // of the couple plans to separate from its partner and
    // form a new couple in the next generation, the sex
    // of that partner who plans to separate from its current
    // partner.
    private int[] _manPastPartnerIndices; // The relationship indices of past partners for a state
    // representing a man.
    private int[] _womanPastPartnerIndices; // The relationship indices of past partners for a
    // state representing a woman.
    private int _manOldestPastPartnerIndex; // The oldest saved past partner's relationship index
    // in a man's array of past partners' indices.
    private int _womanOldestPastPartnerIndex; // The oldest saved past partner's relationship index
    // in a woman's array of past partners' indices.

    /**
     * Creates a new State object with all of its properties having default values.
     */
    public State() {
        this(StateType.MAN, IRRELEVANT, IRRELEVANT, IRRELEVANT, false, null, null, 0, 0);
    }

    /**
     * Used for creating a new state with a new random single person (man/woman) relationship
     * index.
     */
    public State(StateType stateType) {
        this(stateType, IRRELEVANT, IRRELEVANT, IRRELEVANT, true, null, null, 0, 0);
    }

    /**
     * Creates a new State object with the input type, man's relationship index, woman's relationship
     * index and a combined relationship index calculated using the two preceding indices.
     */
    public State(StateType stateType, int manIndex, int womanIndex, int combinedIndex,
                 int[] manPastPartnerIndices, int[] womanPastPartnerIndices,
                 int manOldestPastPartnerArrayIndex, int womanOldestPastPartnerArrayIndex) {
        this(stateType, manIndex, womanIndex, combinedIndex, false,
                manPastPartnerIndices, womanPastPartnerIndices, manOldestPastPartnerArrayIndex,
                womanOldestPastPartnerArrayIndex);
    }

    /**
     * Creates a new State object with the input type, man's relationship index, woman's relationship
     * index and a combined relationship index calculated using the two preceding indices.
     */
    public State(StateType stateType, int manIndex, int womanIndex, int combinedIndex,
                 boolean createRandomIndex,
                 int[] manPastPartnerIndices, int[] womanPastPartnerIndices,
                 int manOldestPastPartnerArrayIndex, int womanOldestPastPartnerArrayIndex) {
        this(stateType, manIndex, womanIndex, combinedIndex, false, createRandomIndex,
                manPastPartnerIndices, womanPastPartnerIndices, manOldestPastPartnerArrayIndex,
                womanOldestPastPartnerArrayIndex);
    }

    /**
     * Creates a new State object with the input type, man's relationship index, woman's relationship
     * index and a combined relationship index calculated using the two preceding indices.
     * Also sets the _pairedMember field to a default value of StateType.MAN.
     */
    public State(StateType stateType, int manIndex, int womanIndex, int combinedIndex,
                 boolean isPaired, boolean createRandomIndex,
                 int[] manPastPartnerIndices, int[] womanPastPartnerIndices,
                 int manOldestPastPartnerArrayIndex, int womanOldestPastPartnerArrayIndex) {
        this(stateType, manIndex, womanIndex, combinedIndex, isPaired, createRandomIndex,
                StateType.MAN,
                manPastPartnerIndices, womanPastPartnerIndices, manOldestPastPartnerArrayIndex,
                womanOldestPastPartnerArrayIndex);
    }

    /**
     * Creates a new State object with the input type, man's relationship index, woman's relationship
     * index, a combined relationship index calculated using the two preceding indices,
     * a boolean value indicating whether a man/person represented by the state has been paired
     * with person of the opposite sex for creating a couple in the next generation,
     * a boolean value indicating whether to create a random relationship index for the
     * new state and a state type value indicating, in case the state represents a couple,
     * which member of the couple wishes to create a new couple in the next generation with
     * a different partner.
     */
    public State(StateType stateType, int manIndex, int womanIndex, int combinedIndex,
                 boolean isPaired, boolean createRandomIndex, StateType pairedMember,
                 int[] manPastPartnerIndices, int[] womanPastPartnerIndices,
                 int manOldestPastPartnerIndex, int womanOldestPastPartnerIndex) {
        int relationshipIndex = 0;
        if (createRandomIndex == true) // Assign the new state a new random relationship index
        // and assign it to the relevant field.
        {
            relationshipIndex = RNGUtilities.generateRandomInteger(0, MAX_RAND_NUM);
            setState(stateType, relationshipIndex);
        } else {
            setManIndex(manIndex);
            setWomanIndex(womanIndex);
            setCombinedRelationshipIndex(combinedIndex);
            setStateType(stateType);
        }
        setPairedMember(pairedMember);
        setPaired(isPaired);
        if (manPastPartnerIndices == null)
            InitializePastPartnerIndicesArray(State.StateType.MAN);
        else {
            setPastPartnerIndicesArray(State.StateType.MAN, manPastPartnerIndices);
            setOldestPartnerIndex(State.StateType.MAN, manOldestPastPartnerIndex);
        }
        if (womanPastPartnerIndices == null)
            InitializePastPartnerIndicesArray(State.StateType.WOMAN);
        else {
            setPastPartnerIndicesArray(State.StateType.WOMAN, womanPastPartnerIndices);
            setOldestPartnerIndex(State.StateType.WOMAN, womanOldestPastPartnerIndex);
        }
    }

    /**
     * Sets the maximum number of former partners a state can remember to the input capacity, unless
     * the capacity exceeds MAX_FRMR_PARTNERS_REMEMBRED, in which case the capacity is set
     * to MAX_FRMR_PARTNERS_REMEMBRED.
     *
     * @param capacity The maximum number of former partners' relationship indices a state can
     *                 remember.
     */
    public static void setPastPartnersMemoryCapacity(int capacity) {
        if (capacity > MAX_FRMR_PARTNERS_REMEMBRED)
            _pastPartnersMemoryCapacity = MAX_FRMR_PARTNERS_REMEMBRED;
        else
            _pastPartnersMemoryCapacity = capacity;
    }

    /**
     * Receives a state's type, expected to represent a person's sex and returns the opposite sex.
     * In case a state type not representing a person's sex was received as input, returns the
     * same state to the caller.
     */
    public static StateType getOppositeSex(StateType stateType) {
        if (stateType == StateType.MAN) // A MAN state type was input.
            return StateType.WOMAN;
        else if (stateType == StateType.WOMAN) // A WOMAN state type was input.
            return StateType.MAN;
        return stateType; // A different state type was input, return same state type.
    }

    /**
     * Returns the array of past partners' relationship indices corresponding to the input
     * state type (e.g. MAN denotes the memory of the state when representing a man should be
     * retrieved).
     *
     * @param stateType The type of state for which to retrieve the corresponding "memory" array
     *                  of past partners' relationship indices.
     * @return The array of past partners' relationship indices corresponding to the input
     * state type.
     */
    public int[] getPastPartnerIndicesArray(StateType stateType) {
        if (stateType == StateType.MAN)
            return _manPastPartnerIndices;
        else if (stateType == StateType.WOMAN)
            return _womanPastPartnerIndices;
        return null;
    }

    /**
     * Returns the array index of the oldest saved past partner's relationship index for the sex
     * corresponding to the input state type (e.g. for an input of MAN the array index of the oldest
     * saved past relationship index of a woman should be retrieved).
     * past partner's relationship index for the sex
     * corresponding to the input state type
     */
    public int getOldestPartnerIndex(StateType stateType) {
        if (stateType == StateType.MAN)
            return _manOldestPastPartnerIndex;
        else if (stateType == StateType.WOMAN)
            return _womanOldestPastPartnerIndex;
        return IRRELEVANT;
    }

    /**
     * Saves the input array of past partners' relationship indices in the state's memory according
     * to the input state type (e.g. if the input state type is MAN, saves the array in the memory
     * set aside for when the state represents a man).
     */
    private void setPastPartnerIndicesArray(StateType stateType, int[] pastPartnerIndices) {
        if (stateType == StateType.MAN)
            _manPastPartnerIndices = pastPartnerIndices;
        else if (stateType == StateType.WOMAN)
            _womanPastPartnerIndices = pastPartnerIndices;
    }

    /**
     * Sets the index denoting the oldest saved past partner's relationship index in the
     * appropriate array to the input value based on the input state type.
     */
    private void setOldestPartnerIndex(StateType stateType, int oldestPastPartnerIndex) {
        if (stateType == StateType.MAN)
            _manOldestPastPartnerIndex = oldestPastPartnerIndex;
        else if (stateType == StateType.WOMAN)
            _womanOldestPastPartnerIndex = oldestPastPartnerIndex;
    }

    /**
     * Creates a new array of past partners' relationship indices and fills it with values each
     * equal to the constant IRRELEVANT.
     */
    private void InitializePastPartnerIndicesArray(State.StateType stateType) {
        int[] pastPartnerIndices = null;
        if (_pastPartnersMemoryCapacity >= 0) {
            pastPartnerIndices = new int[_pastPartnersMemoryCapacity];
            Arrays.fill(pastPartnerIndices, State.IRRELEVANT);
            setPastPartnerIndicesArray(stateType, pastPartnerIndices);
            setOldestPartnerIndex(stateType, 0);
        }
    }

    /**
     * Saves the input relationship index in the state's memory. If the number of former indices
     * stored in the state's memory exceeds the defined capacity, the oldest saved relationship
     * index is overridden.
     * Precondition: The input relationship index is higher than any of those already saved in
     * the state's memory.
     *
     * @param relationshipIndex The new relationship index to save into the state's memory.
     * @param partnerStateType  The state type of the partner (e.g. MAN/WOMAN).
     */
    public void saveNewPartnerIndex(int relationshipIndex, StateType partnerStateType) {
        int newLocation = IRRELEVANT;
        StateType relevantStateType = State.getOppositeSex(partnerStateType);
        int[] relevantPastPartnerIndices = getPastPartnerIndicesArray(relevantStateType);
        int oldestPastPartnerArrayIndex = getOldestPartnerIndex(relevantStateType);
        if (relevantPastPartnerIndices.length > 0) {
            for (int i = 0; i < relevantPastPartnerIndices.length; i++)
                if (relevantPastPartnerIndices[i] == IRRELEVANT)
                    newLocation = i;
            if (newLocation == IRRELEVANT) // No available vacant memory spot, override
            // oldest element.
            {
                newLocation = oldestPastPartnerArrayIndex;
                oldestPastPartnerArrayIndex = (oldestPastPartnerArrayIndex + 1)
                        %
                        relevantPastPartnerIndices.length;
            }
            relevantPastPartnerIndices[newLocation] = relationshipIndex;
        }
        setPastPartnerIndicesArray(relevantStateType, relevantPastPartnerIndices);
        setOldestPartnerIndex(relevantStateType, oldestPastPartnerArrayIndex);
    }

    /**
     * Returns the relationship index corresponding to a person of the sex opposite to that
     * represented by the input state of a neighboring cell.
     */
    public int getOppositeSexIndex(StateType neighborStateType) {
        if (neighborStateType == StateType.MAN) // Neighbor is a man.
            return getIndex(StateType.WOMAN);
        else if (neighborStateType == StateType.WOMAN) // Neighbor is a woman.
            return getIndex(StateType.MAN);
        return IRRELEVANT; // Invalid input.
    }

    /**
     * Returns the sex of the member of a couple represented by the state who has been paired with
     * a different partner in the next generation.
     */
    public StateType getPairedMember() {
        return _pairedMember;
    }

    /**
     * Receives a state type, expected to be one representing a single man or woman and marks
     * it as the one paired with another single neighbor, instead of its current partner in
     * the current couple, in the event the current state represents a couple.
     */
    public void setPairedMember(StateType stateType) {
        if (getStateType() == StateType.COUPLE
                &&
                (stateType == StateType.MAN || stateType == StateType.WOMAN))
            _pairedMember = stateType;
    }

    /**
     * Returns the index corresponding to the input type of object that can be represented by the
     * state.
     */
    public int getIndex(StateType stateType) {
        if (stateType == StateType.MAN)
            return _manIndex;
        else if (stateType == StateType.WOMAN)
            return _womanIndex;
        return _combinedRelationshipIndex;
    }

    /**
     * Returns the type of object represented by the state.
     */
    public StateType getStateType() {
        return _stateType;
    }

    /**
     * Sets the type of object represented by the state and the internal index fields as appropriate
     * according to the type of object the state is supposed to represent.
     */
    public void setState(StateType stateType, int index) {
        if (stateType == StateType.MAN) // Dispose of woman, combined indices and create a new
        // man relationship index.
        {
            _manIndex = index;
            _womanIndex = IRRELEVANT;
            _combinedRelationshipIndex = IRRELEVANT;
        } else if (stateType == StateType.WOMAN) // Dispose of man, combined indices and create
        // a new woman relationship index.
        {
            _womanIndex = index;
            _manIndex = IRRELEVANT;
            _combinedRelationshipIndex = IRRELEVANT;
        } else if (stateType == StateType.COUPLE) // Save individual indices but create a new combined
            // index value.
            _combinedRelationshipIndex = index;
        setStateType(stateType);
    }

    /**
     * Receives a state type value and sets it as the new type for the state.
     */
    public void setStateType(StateType stateType) {
        _stateType = stateType;
    }

    /**
     * Receives an integer and sets it as the new value for the man's relationship index contained
     * in the state.
     */
    public void setManIndex(int manIndex) {
        _manIndex = manIndex;
    }

    /**
     * Receives an integer and sets it as the new value for the woman's relationship index contained
     * in the state.
     */
    public void setWomanIndex(int womanIndex) {
        _womanIndex = womanIndex;
    }

    /**
     * Receives an integer and sets it as the new value for the combined relationship index contained
     * in the state.
     */
    public void setCombinedRelationshipIndex(int combinedRelationshipIndex) {
        _combinedRelationshipIndex = combinedRelationshipIndex;
    }

    /**
     * Returns whether the input cell has an "empty" state.
     */
    public static boolean isEmptyState(State state) {
		return state == null;
	}

    /**
     * Returns whether in the calculation of the next generation, the single man/woman represented
     * by the state has been paired with another single person of the opposite sex arleady.
     */
    public boolean isPaired() {
        return _isPaired;
    }

    /**
     * Sets whether the current state, while representing a single man or woman, has been paired
     * in the calculation of the next generation with a state representing another single person
     * of the opposite sex.
     */
    public void setPaired(boolean paired) {
        _isPaired = paired;
    }

    /**
     * Creates a new state with identical values to the cloned state in all relevant fields
     * (e.g. _stateType, _manIndex, _womanIndex, _combinedRelationshipIndex).
     */
    @Override
    public Object clone() {
        State newState = new State();
        newState.setStateType(_stateType);
        newState.setManIndex(_manIndex);
        newState.setWomanIndex(_womanIndex);
        newState.setCombinedRelationshipIndex(_combinedRelationshipIndex);
        return newState;
    }
}

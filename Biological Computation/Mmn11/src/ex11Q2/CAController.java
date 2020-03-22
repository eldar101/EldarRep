package ex11Q2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Timer;

import ex11Q2.State.StateType;

/**
 * Represents the transition rules of the pairing simulation 2-D CA.
 */
public class CAController implements ActionListener {
    /**
     * rules
     */
    public enum Rule {
        ALWAYS_PAIR, // When single male and female first meet, they immediately pair, regardless
        // of the difference between their relationship indices.
        STATIONARY_PAIR, // When a couple is formed, they remain stationary, not moving unless separated
        // once again.
        LOYAL, // When a couple is formed, they never separate.
        MEMORY_ENABLED // A male/female doesn't pair with a partner they've already had up to
        // _genNumRemembered former generations.
    }

    private static final int UPDATE_INTERVAL = 25; // The pace at which to update the states of
    // the automaton's cells in milliseconds.
    private static final int WRT_TEST_RESULT_INTERVAL = 20; // Each time this number of generations
    // has passed since the last time
    // results were written to disk during
    // a test, write results for the recently
    // calculated new generation.
    private static final int TEST_LENGTH = 100; // The number of generations for which to run
    // a single test of the automaton.

    private CAModel _model; // The states of the automaton.
    private CAView _view; // The graphical display of the automaton's collective state.
    private Timer _newGenTimer; // Ticks every UPDATE_INTERVAL milliseconds to signal the passing
    // of the current generation and the start of a new one.

    private boolean _limitedTimeRun; // Whether to run the automaton for a limited number of
    // generations.
    private int _numOfGenToRunFor; // In the event the user defined to run the automaton for a
    // limited predefined number of generations, designates
    // the desired number of generations.
    private boolean[] _activeRules; // Saves for each existing rule in the Rule enumeration
    // whether it is currently active or not.
    private boolean _firstTickOccurred; // Whether the generation transition timer has ticked
    // already at least once.
    private boolean _stopped; // Whether the "Stop" button has been pressed without a subsequent
    // click on either the "Start" or "Continue" buttons.
    private boolean _continueButtonPressed; // Whether the continue button has been pressed
    // since the last time the current run of the automaton
    // has been stopped.
    private boolean _testRunning; // Whether tests are in progress, controls writing a report
    // of test results to disk.
    private int _currentTestRunning; // The ordinal number of the subset of rules designating
    // a certain test.
    List<List<Rule>> _ruleSubsets; // Rule subsets, each indicating a certain test with the subset
    // of rules applied.

    /**
     * Creates a new controller object with an initial timer for controlling the amount of time
     * passing between generations and a random number generator.
     */
    public CAController() {
        _newGenTimer = new Timer(UPDATE_INTERVAL, this);
        _activeRules = new boolean[Rule.values().length];
        _firstTickOccurred = false;
        _currentTestRunning = 0;
        Arrays.fill(_activeRules, false);
    }

    /**
     * Saves the references to the input model and view objects in the relevant fields and sets
     * up an initial display in the automaton's view.
     */
    public void initializeController(CAModel model, CAView view) {
        setModel(model);
        setView(view);
        view.updateView(model.getCurrentStates(), model.getGenerationNumber(), 0);
    }

    /**
     * Sets the current attached model to reference the input model.
     */
    public void setModel(CAModel model) {
        _model = model;
    }

    /**
     * Associates the controller with the input view object reference.
     */
    public void setView(CAView view) {
        _view = view;
    }

    /**
     * Begins calculating and updating the states of the CA's states at each generation.
     * A new generation is calculated every UPDATE_INTERVAL milliseconds.
     */
    public void startTimers(boolean limitedTimeRun, int numOfGenToRunFor,
                            boolean isAlwaysPairRuleActive,
                            boolean isStationaryPairRuleActive,
                            boolean isLoyalPairsRuleActive,
                            boolean isRememberExesRuleActive,
                            int numOfRememberedExes) {
        if (canRun(isRememberExesRuleActive, numOfRememberedExes)) {
            _limitedTimeRun = limitedTimeRun;
            _numOfGenToRunFor = numOfGenToRunFor;
            configureRules(isAlwaysPairRuleActive,
                    isStationaryPairRuleActive,
                    isLoyalPairsRuleActive,
                    isRememberExesRuleActive,
                    numOfRememberedExes);
            _model.resetModel();
            _newGenTimer.start();
            _stopped = false;
        }
    }

    /**
     * If a current run has been stopped, resumes it.
     */
    public void continueTimers(boolean isAlwaysPairRuleActive,
                               boolean isStationaryPairRuleActive,
                               boolean isLoyalPairsRuleActive,
                               boolean isRememberExesRuleActive,
                               int numOfRememberedExes) {
        if (canRun(isRememberExesRuleActive, numOfRememberedExes) && _stopped == true) {
            configureRules(isAlwaysPairRuleActive,
                    isStationaryPairRuleActive,
                    isLoyalPairsRuleActive,
                    isRememberExesRuleActive,
                    numOfRememberedExes);
            _newGenTimer.start();
            _stopped = false;
            _continueButtonPressed = true; // Used for continuing beyond a limit of time expressed
            // in number of generations.
        }
    }

    /**
     * Receives the number of remembered former partners' relationship indices and returns
     * whether the automaton can start a new run or resume an existing one with the input
     * parameters and current internal state.
     */
    private boolean canRun(boolean isRememberExesRuleActive, int numOfRememberedExes) {
        // User entered valid input.
        return _newGenTimer != null // Timer object exists.
                &&
                _newGenTimer.isRunning() == false // Timer is currently stopped.
                &&
                (isRememberExesRuleActive == false
                        ||
                        (isRememberExesRuleActive == true
                                &&
                                numOfRememberedExes != CAView.INVALID_INPUT));
    }

    /**
     * Enables or disables each possible rule in the automaton's logic according to the input
     * boolean values received from the user.
     */
    private void configureRules(boolean isAlwaysPairRuleActive,
                                boolean isStationaryPairRuleActive,
                                boolean isLoyalPairsRuleActive,
                                boolean isRememberExesRuleActive,
                                int numOfRememberedExes) {
        setRule(Rule.ALWAYS_PAIR, isAlwaysPairRuleActive);
        setRule(Rule.STATIONARY_PAIR, isStationaryPairRuleActive);
        setRule(Rule.LOYAL, isLoyalPairsRuleActive);
        setRule(Rule.MEMORY_ENABLED, isRememberExesRuleActive);
        State.setPastPartnersMemoryCapacity(numOfRememberedExes);
    }

    /**
     * Stops calculating new generations, freezing the CA's collective state at its current
     * generation.
     */
    public void stopTimers() {
        _newGenTimer.stop();
        _firstTickOccurred = false;
        _stopped = true;
        _continueButtonPressed = false;
    }

    /**
     * Runs a sequence of predefined tests for each subset of the automaton's defined rules
     * and writes the results during each test every certain number of generations.
     */
    public void runTests() {
        int subsetSize = 0;
        List<Rule> ruleSubset = null;
        if (_ruleSubsets == null)
            _ruleSubsets = getAllRuleSubsets(Rule.values());
        if (_currentTestRunning == 0)
            _testRunning = true;
        if (_currentTestRunning >= _ruleSubsets.size()) {
            _testRunning = false;
            _currentTestRunning = 0;
        } else {
            ruleSubset = _ruleSubsets.get(_currentTestRunning);
            disableAllRules();
            subsetSize = ruleSubset.size();
            for (int i = 0; i < subsetSize; i++)
                enableRule(ruleSubset.get(i));
            startTimers(true, TEST_LENGTH + 1, isRuleEnabled(Rule.ALWAYS_PAIR),
                    isRuleEnabled(Rule.STATIONARY_PAIR),
                    isRuleEnabled(Rule.LOYAL),
                    isRuleEnabled(Rule.MEMORY_ENABLED),
                    5);
            _currentTestRunning++;
        }
    }

    /**
     * Returns all subsets of the automaton's defined rules that can be optionally applied to
     * the automaton's logic.
     */
    private List<List<Rule>> getAllRuleSubsets(Rule[] currentSubset) {
        List<Rule> rules = new ArrayList<Rule>(Arrays.asList(currentSubset));
        int numOfRules = rules.size();
        List<List<Rule>> receivedRuleSubsets = null;
        List<List<Rule>> ruleSubsetsToReturn = new ArrayList<List<Rule>>();
        List<Rule> modifiedList = null;
        Rule removedRule = Rule.ALWAYS_PAIR; // Arbitrary default value.

        if (numOfRules > 0) // At least one rule exists in received list of subsets, decompose
        // and construct additional subsets.
        {
            removedRule = rules.remove(numOfRules - 1);
            numOfRules = rules.size();
            receivedRuleSubsets = getAllRuleSubsets(rules.toArray(new Rule[numOfRules]));
            for (List<Rule> ruleSubset : receivedRuleSubsets) {
                ruleSubsetsToReturn.add(ruleSubset);
                modifiedList = new ArrayList<Rule>(ruleSubset);
                modifiedList.add(removedRule);
                ruleSubsetsToReturn.add(modifiedList);
            }
        } else // Return a list containing only the empty set of rules.
            ruleSubsetsToReturn.add(new ArrayList<Rule>(0));
        return ruleSubsetsToReturn;
    }

    /**
     * Whenever the _newGenTimer timer ticks, calculates the next generation based on the current
     * states of the CA's cells and updates the CA's view with the new states calculated.
     * Also, if a test is in progress and a given certain number of generations has passed
     * since the last write to disk, results of the test are written to disk.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        State[][] currStates = null;
        State[][] newStates = null;
        int overallHappiness = 0;
        int currentGeneration = 0;
        int newGeneration = 0;
        currentGeneration = _model.getGenerationNumber();
        if (e.getSource() == _newGenTimer) {
            if (_limitedTimeRun == true // Limited run time in generations set.
                    &&
                    currentGeneration >= _numOfGenToRunFor // Runtime limit in generations reached.
                    &&
                    _continueButtonPressed == false) // Continue button not pressed once run time limit
                // has been reached.
                stopTimers();
            else if (_testRunning == true && currentGeneration == TEST_LENGTH) // Still in a sequence
            // of tests.
            // Current test ended.
            {
                stopTimers();
                runTests(); // Run next tests.
            } else {
                currStates = _model.getCurrentStates();
                if (_firstTickOccurred == false) {
                    _view.updateView(currStates, currentGeneration, overallHappiness);
                    _firstTickOccurred = true;
                } else {
                    newStates = calculateNextGen(currStates);
                    _model.incrementGeneration();
                    newGeneration = _model.getGenerationNumber();
                    overallHappiness = calcOverallHappiness(newStates);
                    _model.setStates(newStates);
                    _view.updateView(newStates, newGeneration, overallHappiness);
                }
                if (_testRunning == true
                        &&
                        newGeneration > 0
                        &&
                        (newGeneration % WRT_TEST_RESULT_INTERVAL == 0))
                    writeTestResult(newGeneration, newStates, overallHappiness);
            }
        }
    }

    /**
     * Receives the recently calculated new generation number, set of states in the next generation
     * and overall happiness in the next generation and writes it to disk.
     */
    private void writeTestResult(int newGenerationNumber, State[][] newStates, int overallHappiness) {
        String output = "";
        BufferedWriter writer = null;
        if (newGenerationNumber == WRT_TEST_RESULT_INTERVAL) // First write in the test.
            output += LocalDateTime.now().toString()
                    +
                    " Test - Active Rules: (" + getActiveRules() + "):"
                    +
                    System.getProperty("line.separator");
        output += "Generation No. "
                +
                newGenerationNumber + " - " + "Overall Happiness: " + overallHappiness
                +
                System.getProperty("line.separator");
        try {
            writer = new BufferedWriter(new FileWriter("testResults.txt", true));
            writer.write(output);
            writer.flush();
            writer.close();
        } catch (Exception exception) {
            _view.notifyOfException(exception.getMessage());
        }
    }

    /**
     * Returns a string containing the names of all currently active rules.
     */
    private String getActiveRules() {
        StringBuilder stringBuilder = new StringBuilder();
        int numOfRules = Rule.values().length;
        Rule rule = Rule.ALWAYS_PAIR;

        for (int i = 0; i < numOfRules; i++) {
            rule = getRule(i);
            if (rule != null && isRuleEnabled(rule))
                stringBuilder.append(rule.toString() + ", ");
        }
        if (stringBuilder.length() == 0)
            stringBuilder.append("None");
        else
            stringBuilder.setLength(stringBuilder.length() - 2);
        return stringBuilder.toString();
    }

    /**
     * Calculates the states of the cells of the automaton in the next generation and returns the 2-D array of states of the 2-D automaton created.
     */
    private State[][] calculateNextGen(State[][] currentStates) {
        State[][] newStates = null;
        Position newPosition = null;
        State newState = null;
        int matrixDim = currentStates.length; // Square matrix assumed.
        newStates = new State[matrixDim][matrixDim];
        CollectionUtilities.setNullReferences(newStates);
        for (int i = 0; i < currentStates.length; i++)
            for (int j = 0; j < currentStates[i].length; j++)
                if (!State.isEmptyState(currentStates[i][j])) {
                    newState = calcNewState(currentStates, newStates, i, j);
                    newPosition = calcNewPosition(currentStates, newStates, i, j);
                    if (newPosition == null) // Nowhere to move.
                        newStates[i][j] = newState;
                    else // A nearby cell is available for moving into.
                        newStates[newPosition.getRow()][newPosition.getCol()] = newState;
                }
        return newStates;
    }

    /**
     * Calculates the state of the cell denoted by the input row and column indices in the next generation and returns it.
     */
    private State calcNewState(State[][] currStates, State[][] newStates, int row, int col) {
        State currentState = currStates[row][col];
        State returnedState = currentState;
        if (returnedState != null) {
            if (isUnpairedSingle(currentState)) // Not a couple and not already paired with another
                // partner in the next generation.
                returnedState = tryPair(currStates, newStates, row, col);
            else if (currentState.isPaired() == true) // A man/woman represented by the current state
                // has been paired with another person in
                // the next generation.
                if (currentState.getStateType() != State.StateType.COUPLE) // A man/woman paired with
                    // another single person
                    // in the next generation.
                    returnedState = null;
                else // A couple.
                {
                    if (currentState.getPairedMember() == State.StateType.MAN) // Man paired, woman
                        // becomes single.
                        returnedState = new State(State.StateType.WOMAN,
                                State.IRRELEVANT,
                                currentState.getIndex(State.StateType.WOMAN),
                                State.IRRELEVANT,
                                null,
                                currentState.getPastPartnerIndicesArray(State.StateType.WOMAN),
                                0,
                                currentState.getOldestPartnerIndex(State.StateType.WOMAN));
                    else // Woman paired, man becomes single.
                        returnedState = new State(State.StateType.MAN,
                                currentState.getIndex(State.StateType.MAN),
                                State.IRRELEVANT,
                                State.IRRELEVANT,
                                currentState.getPastPartnerIndicesArray(State.StateType.MAN),
                                null,
                                currentState.getOldestPartnerIndex(State.StateType.MAN),
                                0);
                }

        }
        return returnedState;
    }

    /**
     * Tries to pair the single man or woman represented by the state of the cell with
     * the input row and column indices and returns the result (the same single man/woman or
     * a couple that has been formed).
     */
    private State tryPair(State[][] currStates, State[][] newStates, int row, int col) {
        State currentState = currStates[row][col];
        State chosenPartner = null;
        State newState = currentState;
        State[] neighbors = getPossiblePartners(currentState, getNeighborhood(currStates, row, col));
        int currMaxCombIndex = 0;
        int maxCombIndex = 0; // The highest combined relationship index for the currently inspected
        // single man/woman and currently inspected single neighbor.
        State maxNeighbor = null; // The single neighbor for which the combined relationship index
        // is the highest.
        for (int i = 0; i < neighbors.length; i++) {
            currMaxCombIndex = calcCombinedRelationshipIndex(currentState,
                    neighbors[i]);
            if (currMaxCombIndex > maxCombIndex) {
                maxCombIndex = currMaxCombIndex;
                maxNeighbor = neighbors[i];
            }
        }
        if (isRuleEnabled(Rule.ALWAYS_PAIR)) // Pair with most suitable partner.
            chosenPartner = maxNeighbor;
            // If the ALWAYS_PAIR rule is disabled, try to create a couple with the most suitable
            // neighbor if their level of compatibility is at or above a certain threshold.
        else
            chosenPartner = (maxCombIndex >= State.PAIRING_THRESHOLD) ? maxNeighbor : null;
        if (chosenPartner != null)
            newState = createCouple(currentState, chosenPartner, maxCombIndex);
        return newState;
    }

    /**
     * Returns an array of the neighbor states of the state of the cell with the input coordinates.
     */
    private State[] getNeighborhood(State[][] currStates, int row, int col) {
        int[] possibleRows = new int[]{row - 1, row, row + 1};
        int[] possibleCols = new int[]{col - 1, col, col + 1};
        int currRow = 0;
        int currCol = 0;
        ArrayList<State> neighbors = new ArrayList<State>();
        for (int i = 0; i < possibleRows.length; i++) {
            currRow = possibleRows[i];
            for (int j = 0; j < possibleCols.length; j++) {
                currCol = possibleCols[j];
                if (!(row == currRow && col == currCol)
                        &&
                        currRow >= 0 && currCol >= 0
                        &&
                        currRow < currStates.length && currCol < currStates.length)
                    neighbors.add(currStates[currRow][currCol]);
            }
        }
        return neighbors.toArray(new State[neighbors.size()]);
    }

    /**
     * Returns all neighbor states in the input array of neighbors of the input currently inspected
     * state who might be suitable as new partners for the single man/woman represented by the state.
     */
    private State[] getPossiblePartners(State currentState, State[] neighbors) {
        ArrayList<State> possiblePartners = new ArrayList<State>();
        int newCombinedIndex = 0;
        for (int i = 0; i < neighbors.length; i++)
            if (!State.isEmptyState(neighbors[i])
                    &&
                    neighbors[i].isPaired() == false)
                if (!isRuleEnabled(Rule.MEMORY_ENABLED) || !hadBetterPartner(currentState, neighbors[i])) {
                    if (neighbors[i].getStateType() != State.StateType.COUPLE) {
                        if (isOppositeSex(currentState, neighbors[i]))
                            possiblePartners.add(neighbors[i]);
                    } else if (!isRuleEnabled(Rule.LOYAL)) // Members of a couple can separate and
                    // form new couples.
                    {
                        newCombinedIndex = calcCombinedRelationshipIndex(currentState,
                                neighbors[i]);
                        if (newCombinedIndex > neighbors[i].getIndex(State.StateType.COUPLE))
                            possiblePartners.add(neighbors[i]);
                    }
                }
        return possiblePartners.toArray(new State[possiblePartners.size()]);
    }

    /**
     * Returns whether the input state represents a single man or woman who haven't been paired with
     * another partner yet in the next generation, or a couple.
     */
    private boolean isUnpairedSingle(State state) {
        State.StateType stateType = state.getStateType();
        return stateType != StateType.COUPLE && !state.isPaired(); // Not single.
// Single.
    }

    /**
     * Returns whether the two states each represent a person, each with a sex opposite to the other.
     */
    private boolean isOppositeSex(State currentState, State neighborState) {
        State.StateType currStateType = currentState.getStateType();
        State.StateType neighborStateType = neighborState.getStateType();

        return (currStateType == StateType.MAN && neighborStateType == StateType.WOMAN)
                ||
                (currStateType == StateType.WOMAN && neighborStateType == StateType.MAN);
    }

    /**
     * Calculates the combined relationship index of two potential partners of opposite sexes.
     * The first partner is a single person while the second, their neighbor, may be either
     * single or currently part of a couple.
     */
    private int calcCombinedRelationshipIndex(State currentState, State neighborState) {
        State.StateType currentStateType = currentState.getStateType();
        State.StateType neighborStateType = neighborState.getStateType();
        int currentStateIndex = currentState.getIndex(currentStateType);
        int neighborStateIndex = State.MIN_RAND_NUM;
        if (neighborState.getStateType() != State.StateType.COUPLE) // Neighbor state represents a
            // single person of the opposite
            // sex.
            neighborStateIndex = neighborState.getIndex(neighborStateType);
        else // Neighbor state represents a couple.
            neighborStateIndex = neighborState.getOppositeSexIndex(currentStateType);
        return calcCombinedRelationshipIndex(currentStateIndex, neighborStateIndex);
    }

    /**
     * Calculates the combined relationship index of two potential partners of opposite sexes.
     * The first partner is a single person while the second, their neighbor, may be either
     * single or currently part of a couple.
     */
    private int calcCombinedRelationshipIndex(int currIndex, int neighborIndex) {
        int difference = Math.abs(currIndex - neighborIndex);
        return (State.MAX_RAND_NUM - difference);
    }

    /**
     * Receives a reference to a rule enum member and returns whether the corresponding rule is
     * enabled or not.
     */
    private boolean isRuleEnabled(Rule rule) {
        return _activeRules[rule.ordinal()];
    }

    /**
     * Sets whether a certain input rule should be enabled or not according to the input boolean
     * value.
     */
    public void setRule(Rule rule, boolean isEnabled) {
        _activeRules[rule.ordinal()] = isEnabled;
    }

    /**
     * Turns on the input rule.
     *
     * @param rule The rule to turn on.
     */
    public void enableRule(Rule rule) {
        _activeRules[rule.ordinal()] = true;
    }

    /**
     * Turns off the input rule.
     */
    public void disableRule(Rule rule) {
        _activeRules[rule.ordinal()] = false;
    }

    /**
     * Disables all possible defined rules.
     */
    public void disableAllRules() {
        for (int i = 0; i < _activeRules.length; i++)
            _activeRules[i] = false;
    }

    /**
     * If a rule with the input ordinal number exists, returns the corresponding rule.
     * Otherwise, returns null.
     */
    private Rule getRule(int ruleIndex) {
        Rule[] rules = Rule.values();
        for (int i = 0; i < rules.length; i++)
            if (ruleIndex == rules[i].ordinal())
                return rules[i];
        return null;
    }

    /**
     * Creates a new state representing a man-woman couple with the relationship indices of each
     * separate entity constituting the couple together and a combined relationship index based on
     * the two singular separate indices..
     */
    private State createCouple(State currentState,
                               State chosenPartner,
                               int combinedRelationshipIndex) {
        State.StateType currentStateType = currentState.getStateType();
        State maleState;
        State femaleState;
        int manIndex;
        int womanIndex = 0;

        if (currentStateType == State.StateType.MAN) // Current state represents a man
        // and neighbor state a female partner.
        {
            manIndex = currentState.getIndex(State.StateType.MAN);
            womanIndex = chosenPartner.getIndex(State.StateType.WOMAN);
            maleState = currentState;
            femaleState = chosenPartner;
        } else // Current state represents a woman and neighbor state a male partner.
        {
            womanIndex = currentState.getIndex(State.StateType.WOMAN);
            manIndex = chosenPartner.getIndex(State.StateType.MAN);
            femaleState = currentState;
            maleState = chosenPartner;
        }
        currentState.setPaired(true);
        chosenPartner.setPaired(true);
        if (chosenPartner.getStateType() == State.StateType.COUPLE) // Chosen partner is part of a
            // couple in the current
            // generation.
            chosenPartner.setPairedMember(State.getOppositeSex(currentStateType));
        if (isRuleEnabled(Rule.MEMORY_ENABLED))
            addToMemory(currentState, chosenPartner, manIndex, womanIndex);
        return new State(State.StateType.COUPLE, manIndex, womanIndex, combinedRelationshipIndex,
                maleState.getPastPartnerIndicesArray(StateType.MAN),
                femaleState.getPastPartnerIndicesArray(StateType.WOMAN),
                maleState.getOldestPartnerIndex(StateType.MAN),
                femaleState.getOldestPartnerIndex(StateType.WOMAN));
    }

    /**
     * Adds the input indices to the input neighbor states' memory of past relationships'
     * quality as appropriate.
     */
    private void addToMemory(State currentState, State chosenPartner, int manIndex, int womanIndex) {
        StateType currentStateType = currentState.getStateType();
        StateType effectiveChosenPartnerStateType = State.getOppositeSex(currentStateType);
        int currentStateNewIndex;
        int chosenPartnerStateNewIndex;
        if (currentStateType == StateType.MAN) // Add chosen partner's relationship index
        // to current state's memory of women as a man.
        // Do the opposite for chosen partner.
        {
            currentStateNewIndex = womanIndex;
            chosenPartnerStateNewIndex = manIndex;
        } else  // Current state represents a man.
        // Add chosen partner's relationship index to current state's memory of men as a woman.
        // Do the opposite for chosen partner.
        {
            currentStateNewIndex = manIndex;
            chosenPartnerStateNewIndex = womanIndex;
        }
        currentState.saveNewPartnerIndex(currentStateNewIndex, effectiveChosenPartnerStateType);
        chosenPartner.saveNewPartnerIndex(chosenPartnerStateNewIndex, currentStateType);
    }

    /**
     * Calculates the position of the current object represented by the cell denoted by the input
     * row and column indices (e.g. man, woman, couple) in the next generations.
     * Returns the position calculated.
     */
    private Position calcNewPosition(State[][] currStates, State[][] newStates, int row, int col) {
        ArrayList<Position> possibleNewPositions = getPossibleNewPositions(currStates,
                newStates,
                row,
                col);
        int newPositionIndex;
        int numOfNewPositions;

        if (possibleNewPositions != null) // Not a recently formed pair.
        {
            numOfNewPositions = possibleNewPositions.size();
            if (numOfNewPositions > 0) // Vacant locations exist to move into.
            {
                newPositionIndex = RNGUtilities.generateRandomInteger(0, numOfNewPositions - 1);
                return possibleNewPositions.get(newPositionIndex); // Return new position to move into.
            }
        }

        return null; // No new position to move into.
    }

    /**
     * Calculates the overall level of happiness among all people represented by the states of
     * the automaton's cells and returns it.
     */
    private int calcOverallHappiness(State[][] states) {
        int totalHappiness = 0;
        int numOfStates = 0;
        State.StateType stateType;
        for (int i = 0; i < states.length; i++)
            for (int j = 0; j < states.length; j++)
                if (!State.isEmptyState(states[i][j])) {
                    stateType = states[i][j].getStateType();
                    if (stateType == State.StateType.COUPLE) {
                        totalHappiness += states[i][j].getIndex(stateType); // Multiply by 2 since
                        numOfStates += 2; // Couple consists of 2 people, count as 2 states.
                    } else // Single person, count as one state.
                        numOfStates++;
                }
        if (numOfStates > 0)
            return (totalHappiness / numOfStates);
        return 0; // Default value if all cells have an "empty" state.
    }

    /**
     * Returns possible new positions for the object represented by the state of the cell
     * denoted by the input row and column indices (e.g. man, woman, couple) based
     * on the positions of all objects in the current generation and already made placements
     * for the next generation.
     */
    private ArrayList<Position> getPossibleNewPositions(State[][] currStates,
                                                        State[][] newStates,
                                                        int row, int col) {
        int[] possibleRowIndices = new int[]{row, row - 1, row + 1};
        int[] possibleColumnIndices = new int[]{col, col - 1, col + 1};
        int currRow;
        int currCol;
        ArrayList<Position> possibleNewPositions = null;
        if (currStates[row][col] != null && currStates[row][col].isPaired() == false
                &&
                !(currStates[row][col].getStateType() == StateType.COUPLE && isRuleEnabled(Rule.STATIONARY_PAIR))) {
            possibleNewPositions = new ArrayList<>();
            for (int i = 0; i < possibleRowIndices.length; i++)
                for (int j = 0; j < possibleColumnIndices.length; j++) // Assumes a square 2-D array.
                {
                    currRow = possibleRowIndices[i];
                    currCol = possibleColumnIndices[j];
                    if (currRow >= 0 && currCol >= 0
                            &&
                            currRow < currStates.length && currCol < currStates.length
                            &&
                            !(currRow == row && currCol == col)
                            &&
                            State.isEmptyState(currStates[currRow][currCol])
                            &&
                            State.isEmptyState(newStates[currRow][currCol]))
                        possibleNewPositions.add(new Position(currRow, currCol));
                }
        }
        return possibleNewPositions;
    }

    /**
     * Returns whether the state has already had a past partner (including its current partner
     * if the state is of a COUPLE type) who is a better match in terms of their relationship
     * index.
     */
    private boolean hadBetterPartner(State currentState, State partnerState) {
        StateType relevantStateType = currentState.getStateType();
        int[] relevantIndicesArray = currentState.getPastPartnerIndicesArray(relevantStateType);
        int currRelationshipIndex = calcCombinedRelationshipIndex(currentState, partnerState);
        int pastCombRelationshipIndex;
        for (int i = 0; i < relevantIndicesArray.length; i++)
            if (relevantIndicesArray[i] != State.IRRELEVANT) {
                pastCombRelationshipIndex = calcCombinedRelationshipIndex(currentState.getIndex(relevantStateType),
                        relevantIndicesArray[i]);
                if (currRelationshipIndex <= pastCombRelationshipIndex)
                    return true;
            }
        return false;
    }
}

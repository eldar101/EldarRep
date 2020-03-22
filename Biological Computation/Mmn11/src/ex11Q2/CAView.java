package ex11Q2;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 * Represents the graphical interface of the couple matching cellular automaton exercise.
 *
 * @author Eldar Weiss
 */
public class CAView extends JFrame implements ActionListener, ItemListener {

    /**
     * Needed for extending JFrame.
     */
    private static final long serialVersionUID = -5682010608865087523L;
    private static final int NUM_OF_INTR_PNL_CONTROL_ROWS = 5; // Number of rows in the panel
    // containing interactive controls.
    private static final String IMAGE_LOAD_ERROR_MESSAGE = "Failed loading all graphical resources.";

    public static final int INVALID_INPUT = -1; // Denotes invalid input entered by a user.

    private CAController _controller = null; // Used for notifying controller about events.

    private JPanel _outerPanel; // Contains all controls within it.
    private JPanel _sidePanel; // Contains controls for changing some of the rules of
    // the automaton and statistics about the current run
    // of the automaton.
    private BoardView _board; // The graphical representation of the CA's board.

    private Font _sectionHeaderFont; // Used for section headers.
    private Font _regularFont;  // Used for the text of components which aren't titles.
    private Insets _gridInsets; // Used as insets for all grid layouts.
    private Border _sectionBorder; // The design of a border between sections.

    private Image _transparentFImage; // Represents a female.
    private Image _transparentMImage; // Represents a male.
    private Image _transparentCouple; // Represents a couple.
    private Image _transparentarentMaleFemaleCoupleIconImage; // Used as the icon at the top left side
    // of the window.

    private JButton _startButton; // Used for starting the automaton's simulation.
    private JButton _continueButton; // Used for continuing the current run if stopped.
    private JButton _stopButton; // Used for stopping the automaton's simulation.
    private JButton _runTestsButton; // Used for launching a sequence of predefined tests
    // and writing their results into disk.
    private JLabel _generationNumberDisplayLabel; // Used for displaying the number of the current
    // generation the automaton is at.
    private JLabel _overallHappinessDisplayLabel; // Used for displaying the average level of
    // happiness among couples in the automaton.
    private JCheckBox _limitGenerationsCheckBox; // Designates whether to run the automaton
    // for a limited number of generations before
    // stopping on its own.
    private JTextField _numOfGenToRunTextField; // Number of generations to run the automaton for.
    private JCheckBox _alwaysPairCheckbox; // Whether a single man/woman should always pair with
    // a nearby single person of the opposite sex who is
    // the closest match of all neighbors.
    private JCheckBox _stationaryPairsCheckbox; // Controls whether a formed couple should remain
    // stationary and not move.
    private JCheckBox _loyalPairsCheckBox; // Controls whether formed couples should never separate
    // or separate and form new couples with more compatible
    // partners.
    private JCheckBox _memoryEnabledCheckBox; // Controls whether single men/women learn from past
    // disappointments and avoid pairing with former
    // partners or can pair again with former partners from
    // whom they separated.
    private JTextField _numOfPartnersRememberedTextField; // Controls how many former partners a
    // single man/woman can remember (up to
    // State.MAX_FRMR_PARTNERS_REMEMBRED).


    /**
     * Creates the graphical display of the pairing simulation CA program.
     */
    public CAView() {
        LayoutManager gridLayout = new GridLayout(1, 2, 0, 5);
        Border outsideBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
        Border insideBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);

        loadImages();
        setTitle("Couple Matching Cellular Automaton Simulator");
        setIconImage(_transparentarentMaleFemaleCoupleIconImage);
        setSize((int) (screenSize.getWidth() * 0.9), (int) (screenSize.getHeight() * 0.9));

        // Set up graphical decorations.
        _sectionHeaderFont = new Font(Font.SERIF, Font.BOLD, 18);
        _regularFont = new Font(Font.SERIF, Font.PLAIN, 16);
        _gridInsets = new Insets(5, 5, 5, 5);
        _sectionBorder = BorderFactory
                .createCompoundBorder(outsideBorder,
                        insideBorder);

        // Create child containers.
        _outerPanel = new JPanel();
        _board = createBoard();
        _sidePanel = createSidePanel();

        // Set layout and borders.
        _outerPanel.setLayout(gridLayout);
        _board.setBorder(_sectionBorder);
        _outerPanel.setBorder(_sectionBorder);

        _outerPanel.add(_board);
        _outerPanel.add(_sidePanel);
        scrollPane.add(_outerPanel);
        add(scrollPane);
        //pack();
        addHandlers();
        setLocationRelativeTo(null); // Center window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setVisible(true);
    }

    /**
     * Sets the look and feel of the UI.
     */
    private void setLookAndFeel(String lookAndFeelClassName) {
        try {
            UIManager.setLookAndFeel(lookAndFeelClassName);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    /**
     * Sets the view's controller to the input reference.
     */
    public void setController(CAController controller) {
        _controller = controller;
    }

    /**
     * Loads all images relevant for the program.
     */
    private void loadImages() {
        try {
            _transparentFImage = loadImage("ex11Q2/transparentFemaleImage.png");
            _transparentMImage = loadImage("ex11Q2/transparentMaleImage.png");
            _transparentCouple = loadImage("ex11Q2/transparentMaleFemaleCoupleImage.png");
            _transparentarentMaleFemaleCoupleIconImage = loadImage("ex11Q2/transparentMaleFemaleCoupleIconImage.png");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, IMAGE_LOAD_ERROR_MESSAGE);
        }
    }

    /**
     * Loads an image using an input in-memory buffer and returns it.
     */
    private Image loadImage(String path) {
        Image image = null;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResource(path));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, IMAGE_LOAD_ERROR_MESSAGE);
        }
        return image;
    }

    /**
     * Creates an initial graphical display of the automaton's board.
     */
    private BoardView createBoard() {
        BoardView boardView = new BoardView(_transparentFImage, _transparentMImage,
                _transparentCouple);
        return boardView;
    }

    /**
     * Creates the side panel of the automaton's GUI containing controls and information for the user
     * and returns it.
     */
    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel();
        JPanel rulesPanel = createRulesPanel();
        JPanel statisticsPanel = createStatisticsPanel();
        JPanel runControlPanel = createRunControlPanel();
        LayoutManager gridLayout = new GridLayout(3, 1, 5, 0);

        sidePanel.setLayout(gridLayout);
        sidePanel.add(rulesPanel);
        sidePanel.add(statisticsPanel);
        sidePanel.add(runControlPanel);

        return sidePanel;
    }

    /**
     * Creates the panel containing controls for altering the automaton's rules and returns it.
     */
    private JPanel createRulesPanel() {
        JPanel rulesPanel = new JPanel();
        JLabel rulesPanelTitle = new JLabel("Rules/Parameters:");
        JLabel alwaysPairLabel = new JLabel("Always Pair:");
        JLabel stationaryPairsLabel = new JLabel("Stationary Pairs:");
        JLabel loyalPairsLabel = new JLabel("Loyal Pairs:");
        JLabel memoryEnabledLabel = new JLabel("Memory Enabled:");
        JLabel numOfPartnersRememberedLabel = new JLabel("Number of Partners Remembered:");

        ArrayList<JLabel> labels = new ArrayList<JLabel>(); // For applying fonts.
        ArrayList<JComponent> interactiveControls = new ArrayList<JComponent>(); // For adding
        // tooltips.
        ArrayList<JComponent> allControls = null; // All child controls in the interactive panel.
        // Used for laying out components in a systematic manner.
        HashMap<Pair<Integer, Integer>, JComponent> controlDictionary = new HashMap<Pair<Integer, Integer>, JComponent>();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        JPanel surroundingPanel = new JPanel();

        _alwaysPairCheckbox = new JCheckBox();
        _stationaryPairsCheckbox = new JCheckBox();
        _loyalPairsCheckBox = new JCheckBox();
        _memoryEnabledCheckBox = new JCheckBox();
        _numOfPartnersRememberedTextField = new JTextField(1);
        _numOfPartnersRememberedTextField.setEnabled(false);

        allControls = new ArrayList<JComponent>(Arrays
                .asList(rulesPanelTitle,
						alwaysPairLabel,
						_alwaysPairCheckbox,
						stationaryPairsLabel,
						_stationaryPairsCheckbox,
						loyalPairsLabel,
						_loyalPairsCheckBox,
						memoryEnabledLabel,
						_memoryEnabledCheckBox,
						numOfPartnersRememberedLabel,
						_numOfPartnersRememberedTextField));

        surroundingPanel.setLayout(flowLayout);
        surroundingPanel.setBorder(_sectionBorder);

        labels.addAll(Arrays.asList(rulesPanelTitle, alwaysPairLabel,
				stationaryPairsLabel, loyalPairsLabel,
				memoryEnabledLabel, numOfPartnersRememberedLabel));
        interactiveControls.addAll(Arrays.asList(_alwaysPairCheckbox,
				_stationaryPairsCheckbox,
				_loyalPairsCheckBox,
				_memoryEnabledCheckBox,
				_numOfPartnersRememberedTextField));
        controlDictionary = createControlDictionary(allControls);

        layoutRulesPanel(rulesPanel, controlDictionary);
        applyFonts(labels);
        addRulesPanelTooltips(interactiveControls);
        surroundingPanel.add(rulesPanel);

        return surroundingPanel;
    }

    /**
     * Receives a list of all child controls in the section of the window containing controls
     * for changing the rules of the CA and returns a dictionary structure mapping (x,y) pairs
     * of coordinates to each control for layout purposes.
     */
    private HashMap<Pair<Integer, Integer>, JComponent> createControlDictionary(ArrayList<JComponent> allControls) {
        HashMap<Pair<Integer, Integer>, JComponent> controlDictionary = new HashMap<Pair<Integer, Integer>, JComponent>();
        int controlIndex = 0;
        int thirdColumnStart = 9; // The index of the control where a different logic is first needed.
        int numberOfFirstColumns = 2; // The number of columns where each row has at least one control.
        int memorySizeLabelIndex = 9;
        int memorySizeTextFieldIndex = 10;

        controlDictionary.put(new Pair<>(0, 0), allControls.get(controlIndex));
        controlIndex++;
        for (int j = 1; j < NUM_OF_INTR_PNL_CONTROL_ROWS; j++)
            for (int i = 0; i < numberOfFirstColumns; i++)
                if (controlIndex < thirdColumnStart) {
                    controlDictionary.put(new Pair<>(i, j), allControls.get(controlIndex));
                    controlIndex++;
                }
        controlDictionary.put(new Pair<>(2, 4), allControls.get(memorySizeLabelIndex));
        controlDictionary.put(new Pair<>(3, 4), allControls.get(memorySizeTextFieldIndex));
        return controlDictionary;
    }

    /**
     * Configures the layout of the interactive control panel.
     */
    private void layoutRulesPanel(JPanel rulesPanel,
                                  HashMap<Pair<Integer, Integer>, JComponent> coordinateMapping) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();

        rulesPanel.setLayout(gridBagLayout);
        constraints.insets = _gridInsets;
        for (Pair<Integer, Integer> coordinate : coordinateMapping.keySet()) {
            constraints.gridx = coordinate.getFirstElement();
            constraints.gridy = coordinate.getSecondElement();

            constraints.anchor = (constraints.gridx == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;
            constraints.weightx = (constraints.gridx == 0) ? 0.1 : 1;
            constraints.weighty = 1.0;

            rulesPanel.add(coordinateMapping.get(coordinate), constraints);
        }
    }

    /**
     * Applies a section header font to the first label and regular font to all other labels
     * following it.
     */
    private void applyFonts(ArrayList<JLabel> labels) {
        int numOfLabels = labels.size();
        if (numOfLabels > 0)
            labels.get(0).setFont(_sectionHeaderFont);
        if (numOfLabels > 1)
            for (int i = 1; i < numOfLabels; i++)
                labels.get(i).setFont(_regularFont);
    }

    /**
     * Receives the interactive controls of the interactive panel and assigns a helpful tooltip
     * to each of them.
     */
    private void addRulesPanelTooltips(ArrayList<JComponent> interactiveControls) {
        String alwaysPairToolTipText = "When a man meets any woman, they form a couple immediately";
        String stationaryPairsToolTipText = "When a couple is formed, "
                +
                "they remain stationary unless they decide to separate";
        String loyalPairsToolTipText = "When a couple is formed, they never separate.";
        String memoryEnabledToolTipText = "Remembers the defined number of former partners, "
                +
                "never bonding with them again.";
        interactiveControls.get(0).setToolTipText(alwaysPairToolTipText);
        interactiveControls.get(1).setToolTipText(stationaryPairsToolTipText);
        interactiveControls.get(2).setToolTipText(loyalPairsToolTipText);
        interactiveControls.get(3).setToolTipText(memoryEnabledToolTipText);
    }

    /**
     * Creates the section of the screen displaying statistics about the current run
     * of the pairing CA simulation.
     */
    private JPanel createStatisticsPanel() {
        JPanel statisticsPanel = new JPanel();
        JPanel surroundingPanel = new JPanel();
        JLabel statisticsPanelTitle = new JLabel("Statistics:");
        JLabel generationNumberLabel = new JLabel("Generation No.: ");
        JLabel overallHappinessLabel = new JLabel("Overall Happiness: ");
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        ArrayList<JLabel> labels = null;

        _generationNumberDisplayLabel = new JLabel("-");
        _overallHappinessDisplayLabel = new JLabel("-");
        labels = new ArrayList<JLabel>(Arrays.asList(statisticsPanelTitle,
				generationNumberLabel,
				_generationNumberDisplayLabel,
				overallHappinessLabel,
				_overallHappinessDisplayLabel));
        statisticsPanel.setLayout(gridBagLayout);
        surroundingPanel.setLayout(flowLayout);
        surroundingPanel.setBorder(_sectionBorder);

        constraints.insets = _gridInsets;
        constraints.weightx = 0.5;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 0;
        constraints.gridy = 0;
        statisticsPanel.add(statisticsPanelTitle, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        statisticsPanel.add(generationNumberLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        statisticsPanel.add(_generationNumberDisplayLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        statisticsPanel.add(overallHappinessLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        statisticsPanel.add(_overallHappinessDisplayLabel, constraints);

        applyFonts(labels);

        surroundingPanel.add(statisticsPanel);
        return surroundingPanel;
    }

    /**
     * Creates the panel used to control when the automaton starts and stops updating itself,
     * calculating the next generation each time and displaying new states afterwards.
     * The method then returns the created panel
     */
    private JPanel createRunControlPanel() {
        JPanel runControlPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel limitGenerationsLabel = new JLabel("Limit Generations: ");
        JLabel numOfGenToRunLabel = new JLabel("Number: ");
        _limitGenerationsCheckBox = new JCheckBox();
        _numOfGenToRunTextField = new JTextField(5);
        _numOfGenToRunTextField.setEnabled(false);
        _startButton = new JButton("Start");
        _continueButton = new JButton("Continue");
        _stopButton = new JButton("Stop");
        _runTestsButton = new JButton("Run Tests");

        runControlPanel.setLayout(gridBagLayout);

        constraints.weightx = 0.5;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = _gridInsets;

        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonsPanel.add(_startButton);
        buttonsPanel.add(_continueButton);
        buttonsPanel.add(_stopButton);

        addRunControlPanelTooltips();

        constraints.gridy = 0;
        constraints.gridx = 0;
        runControlPanel.add(limitGenerationsLabel, constraints);
        constraints.gridx = 1;
        runControlPanel.add(_limitGenerationsCheckBox, constraints);
        constraints.gridx = 2;
        runControlPanel.add(numOfGenToRunLabel, constraints);
        constraints.gridx = 3;
        runControlPanel.add(_numOfGenToRunTextField, constraints);
        constraints.gridy = 1;
        constraints.gridx = 0;
        constraints.insets = new Insets(0, 0, 0, 0);
        runControlPanel.add(buttonsPanel, constraints);
        constraints.gridy = 2;
        constraints.insets = _gridInsets;
        runControlPanel.add(_runTestsButton, constraints);

        return surroundPanel(runControlPanel, new FlowLayout(FlowLayout.LEFT));
    }

    /**
     * Adds tool tips to the controls in the section of the screen controlling starting and
     * stopping the CA.
     */
    private void addRunControlPanelTooltips() {
        _limitGenerationsCheckBox.setToolTipText("Run the CA for a limited number of generations.");
        _numOfGenToRunTextField.setToolTipText("The number of generations to run the CA for.");
        _startButton.setToolTipText("Starts a new run of the automaton from scratch with a random "
                +
                "initial placement of single men and women.");
        _continueButton.setToolTipText("Continues a current stopped run of the automaton.");
        _stopButton.setToolTipText("Freezes a current run of the automaton at the current generation.");
        _runTestsButton.setToolTipText("Runs the automaton for 100 generations for each "
                +
                "subset of possible applied rules and writes a report of "
                +
                "the results to disk.");
    }

    /**
     * Receives a JPanel container and surrounds it with a new JPanel whose layout is the input
     * layout.
     */
    private JPanel surroundPanel(JPanel innerPanel, LayoutManager layout) {
        JPanel surroundingPanel = new JPanel();
        surroundingPanel.setBorder(_sectionBorder);
        surroundingPanel.setLayout(layout);
        surroundingPanel.add(innerPanel);

        return surroundingPanel;
    }

    /**
     * Adds event handlers to all interactive components.
     */
    private void addHandlers() {
        _startButton.addActionListener(this);
        _continueButton.addActionListener(this);
        _stopButton.addActionListener(this);
        _runTestsButton.addActionListener(this);
        _limitGenerationsCheckBox.addItemListener(this);
        _memoryEnabledCheckBox.addItemListener(this);
    }

    /**
     * Receives a 2-D array describing all cell's states and updates the graphical display accordingly.
     */
    public void updateView(ex11Q2.State[][] board, int newGeneration, int averageHappiness) {
        _board.updateBoardDisplay(board);
        _generationNumberDisplayLabel.setText(Integer.toString(newGeneration));
        _overallHappinessDisplayLabel.setText(Integer.toString(averageHappiness));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean isNumOfGenLimited = isNumOfGenLimited();
        boolean isRememberExesRuleActive = isRememberExesRuleActive();
        int numOfGenLimit = (isNumOfGenLimited == true) ? getNumOfGenLimit() : 0;
        int numOfRememberedExes = (isRememberExesRuleActive == true) ? getNumOfRememberedExes()
                :
                INVALID_INPUT;
        if (e.getSource() == _startButton)
            _controller.startTimers(isNumOfGenLimited,
                    numOfGenLimit,
                    isAlwaysPairRuleActive(),
                    isStationaryPairRuleActive(),
                    isLoyalPairsRuleActive(),
                    isRememberExesRuleActive,
                    numOfRememberedExes);
        if (e.getSource() == _continueButton)
            _controller.continueTimers(isAlwaysPairRuleActive(),
                    isStationaryPairRuleActive(),
                    isLoyalPairsRuleActive(),
                    isRememberExesRuleActive,
                    numOfRememberedExes);
        else if (e.getSource() == _stopButton)
            _controller.stopTimers();
        else if (e.getSource() == _runTestsButton)
            _controller.runTests();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == _limitGenerationsCheckBox) // "Limit Generations" check box selection
        // changed.
        {
            if (_limitGenerationsCheckBox.isSelected())
                _numOfGenToRunTextField.setEnabled(true);
            else _numOfGenToRunTextField.setEnabled(false);
        } else if (e.getSource() == _memoryEnabledCheckBox) // "Remember Ex's" check box selection
        // changed.
        {
            if (_memoryEnabledCheckBox.isSelected())
                _numOfPartnersRememberedTextField.setEnabled(true);
            else _numOfPartnersRememberedTextField.setEnabled(false);
        }
    }

    /**
     * Returns whether to limit the number of generations to run the automaton for.
     */
    private boolean isNumOfGenLimited() {
        if (_limitGenerationsCheckBox != null)
            return _limitGenerationsCheckBox.isSelected();
        return false;
    }

    /**
     * Returns the content of the _numOfGenToRunTextField field if it contains an integer.
     * If the field contains invalid input (i.e. not an integer) returns a default value.
     */
    private int getNumOfGenLimit() {
        int numOfGenLimit = 0;
        try {
            numOfGenLimit = Integer.parseInt(_numOfGenToRunTextField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid input in the number of generations "
                    +
                    "field:\n"
                    +
                    e.getMessage());
        }
        return numOfGenLimit;
    }

    /**
     * Returns whether the "Always Pair" rule is enabled.
     */
    private boolean isAlwaysPairRuleActive() {
        return _alwaysPairCheckbox.isSelected();
    }

    /**
     * Returns whether the "Stationary Pairs" rule is active. This rule dictates that couples that
     * have been formed remain stationary and don't change their location on the board.
     */
    private boolean isStationaryPairRuleActive() {
        return _stationaryPairsCheckbox.isSelected();
    }

    /**
     * Returns whether formed couples should separate and have at least one of their members create
     * a new couple with a different single person of the opposite sex if the couple formed
     * has a higher measure of compatibility than the current couple.
     */
    private boolean isLoyalPairsRuleActive() {
        return _loyalPairsCheckBox.isSelected();
    }

    /**
     * Returns whether the "Remember Ex's" rule is active. This rule dictates that men/women
     * considering a potential partner for creating a couple should remember past partners
     * and avoid recreating a couple with them.
     */
    private boolean isRememberExesRuleActive() {
        return _memoryEnabledCheckBox.isSelected();
    }

    /**
     * Returns the number of former partners to be remembered by men/women in order to avoid
     * recreating a couple with them.
     */
    private int getNumOfRememberedExes() {
        int numOfRememberedExes = INVALID_INPUT;
        try {
            numOfRememberedExes = Integer.parseInt(_numOfPartnersRememberedTextField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid input in the number of remembered partners "
                    +
                    "field:\n"
                    +
                    e.getMessage());
        }
        if (numOfRememberedExes < 1) {
            JOptionPane.showMessageDialog(null,
                    "Number of remembered former partners must be equal "
                            +
                            "or greater than 1.");
        }
        return numOfRememberedExes;
    }

    /**
     * Notifies of an exception in the automaton's internal logic.
     */
    public void notifyOfException(String exceptionString) {
        JOptionPane.showMessageDialog(null, exceptionString);
    }
}

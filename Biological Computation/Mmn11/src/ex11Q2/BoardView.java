package ex11Q2;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.JPanel;

/**
 * This class sets up the board of the 2-D automaton.
 *
 * @author Eldar Weiss
 */
public class BoardView extends JPanel {

    /**
     * Needed for extending JPanel.
     */
    private static final long serialVersionUID = 1942482025238702421L;

    private static final double IMAGE_SCALE_FACTOR = 0.4; // image scaling
    private static final double IMAGE_DRAWING_OFFSET = 0.3; // coordinates


    private int _boardCellSideLength; // variables
    private int _imageSize;
    private int _width;
    private int _height;
    private State[][] _states; // The states of each cell of the CA.
    private Image _transparentMImage; // Represents a male.
    private Image _transparentFImage; //female.
    private Image _transparentCouple; // couples

    /**
     * Creates the section of the window displaying the state of each cell of the CA.
     */
    public BoardView(Image femaleImage, Image maleImage,
                     Image maleFemaleCoupleImage) {
        setBackground(Color.WHITE);
        _transparentFImage = femaleImage;
        _transparentMImage = maleImage;
        _transparentCouple = maleFemaleCoupleImage;
    }

    /**
     * We update the board for the new state
     */
    public void updateBoardDisplay(State[][] states) {
        Insets insets = null;
        int minDim = 0;
        Container currentComponent = this;
        _height = getHeight();
        _width = getWidth();
        while (currentComponent != null) {
            insets = currentComponent.getInsets();
            _width -= insets.right + insets.left;
            _height -= insets.top + insets.bottom;
            currentComponent = currentComponent.getParent();
        }
        _states = states;
        minDim = (_width < _height) ? _width : _height;
        _boardCellSideLength = (minDim / _states.length) * 1;
        _imageSize = (int) (_boardCellSideLength * IMAGE_SCALE_FACTOR);
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int cellStartX, cellStartY, imageStartX, imageStartY, stringStartX, stringStartY;
        int imageDrawingOffset = (int) (_boardCellSideLength * IMAGE_DRAWING_OFFSET);
        State.StateType stateType;

        if (_states != null)
            for (int i = 0; i < _states.length; i++) {
                cellStartX = i * _boardCellSideLength;
                imageStartX = cellStartX + imageDrawingOffset;
                stringStartX = imageStartX + _imageSize / 4;
                for (int j = 0; j < _states.length; j++) {
                    cellStartY = j * _boardCellSideLength;
                    imageStartY = cellStartY + imageDrawingOffset;
                    stringStartY = imageStartY + _imageSize + 10;
                    g.drawRect(cellStartX, cellStartY, _boardCellSideLength, _boardCellSideLength);
                    if (_states[i][j] != null) {
                        stateType = _states[i][j].getStateType();
                        if (stateType == State.StateType.MAN)
                            drawMan(g, imageStartX, imageStartY);
                        else if (stateType == State.StateType.WOMAN)
                            drawWoman(g, imageStartX, imageStartY);
                        else if (stateType == State.StateType.COUPLE)
                            drawCouple(g, imageStartX, imageStartY);
                        g.drawString(Integer.toString(_states[i][j].getIndex(stateType)),
                                stringStartX,
                                stringStartY);
                    }
                }
            }
    }

    /**
     * Draws the image of a man in the middle of the cell.
     */
    private void drawMan(Graphics g, int row, int col) {
        drawImage(g, _transparentMImage, row, col);
    }

    /**
     * Draws the image of a woman in the middle of the cell.
     */
    private void drawWoman(Graphics g, int row, int col) {
        drawImage(g, _transparentFImage, row, col);
    }

    /**
     * Draws the image of a woman in the middle of the cell.
     */
    private void drawCouple(Graphics g, int row, int col) {
        drawImage(g, _transparentCouple, row, col);
    }

    /**
     * Draws the input image on the input Graphics object with the input coordinates.
     */
    private void drawImage(Graphics g, Image image, int row, int col) {
        g.drawImage(image, row, col, _imageSize, _imageSize, null);
    }
}

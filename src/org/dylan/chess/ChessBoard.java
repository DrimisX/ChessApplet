/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dylan.chess;

import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Color.GREEN;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Dylan Huculak - c0630163
 *
 */
public class ChessBoard extends JPanel {

    // CONSTANTS
    private final static int ROWS = 8, COLS = 8;                                // Number of rows and columns on the board
    private final static int SQUARES = ROWS * COLS;                             // Number of total squares
    private final static String TITLE = "Chess";                                // Title for board
    private final static Color VERY_LIGHT_GREY = new Color(235, 235, 235);      // Color for light squares
    
    // ARRAYS TO HOLD VARIOUS PATTERNS FOR TESTING
    private final static Integer[] WHITE_PAWN_ROW = {48,49,50,51,52,53,54,55};
    private final static Integer[] BLACK_PAWN_ROW = {8,9,10,11,12,13,14,15};
    private final static Integer[] TOP_BORDER = {0,1,2,3,4,5,6,7};
    private final static Integer[] LEFT_BORDER = {0,8,16,24,32,40,48,56};
    private final static Integer[] RIGHT_BORDER = {7,15,23,31,39,47,55,63};
    private final static Integer[] BOTTOM_BORDER = {56,57,58,59,60,61,62,63};
    private final static Integer[] ROW2 = {8,9,10,11,12,13,14,15};
    private final static Integer[] COL2 = {1,9,17,25,33,41,49,57};
    private final static Integer[] COL7 = {6,14,22,30,38,46,54,62};
    private final static Integer[] ROW7 = {48,49,50,51,52,53,54,55};
    private final static Integer[] KING_MOVES = {-9,-8,-7,-1,1,7,8,9};
    
    private static ArrayList topSquares;
    private static ArrayList leftSquares;
    private static ArrayList rightSquares;
    private static ArrayList bottomSquares;
    private static ArrayList top2rows;
    private static ArrayList left2columns;
    private static ArrayList right2columns;
    private static ArrayList bottom2rows;
    
    // CHESS PIECES
    private static ImageIcon wPawn;
    private static ImageIcon wBishop;
    private static ImageIcon wKnight;
    private static ImageIcon wRook;
    private static ImageIcon wQueen;
    private static ImageIcon wKing;
    private static ImageIcon bPawn;
    private static ImageIcon bBishop;
    private static ImageIcon bKnight;
    private static ImageIcon bRook;
    private static ImageIcon bQueen;
    private static ImageIcon bKing;
    
    // ARRAYS FOR PIECES
    private static ImageIcon[] wBack;
    private static ImageIcon[] bBack;
    private static ArrayList<ImageIcon> wPieces;
    private static ArrayList<ImageIcon> bPieces;

    // DEFAULT SETTINGS
    private static final Dimension squareSize = new Dimension(80, 80);          // Dimension for each square

    // VARIABLES
    private final JPanel panelButton;                                           // Panel to hold buttons
    private final JButton buttonArray[];                                        // Button array for board                                                
    private final JButton buttonReset;                                          // Button to reset board

    private final JTextArea textArea;                                           // Text area to display game feedback
    private String boardFEN;                                              // String to hold Forsyth-Edwards Notation (board state)

    private char file;
    private int rank;
    
    // VARIABLES TO HOLD GAME STATUS
    private int move;
    private int turn;
    private int selectedSquare;
    private ImageIcon selectedPiece;
    private ImageIcon piece;
    private Color originalColor;
    private boolean isPieceSelected;

    // CONSTRUCTOR
    public ChessBoard() {
        //Set piece resources
        wPawn = new ImageIcon(getClass().getClassLoader().getResource("resources/Pawn-W.png"));
        wBishop = new ImageIcon(getClass().getClassLoader().getResource("resources/Bishop-W.png"));
        wKnight = new ImageIcon(getClass().getClassLoader().getResource("resources/Knight-W.png"));
        wRook = new ImageIcon(getClass().getClassLoader().getResource("resources/Rook-W.png"));
        wQueen = new ImageIcon(getClass().getClassLoader().getResource("resources/Queen-W.png"));
        wKing = new ImageIcon(getClass().getClassLoader().getResource("resources/King-W.png"));
        bPawn = new ImageIcon(getClass().getClassLoader().getResource("resources/Pawn-B.png"));
        bBishop = new ImageIcon(getClass().getClassLoader().getResource("resources/Bishop-B.png"));
        bKnight = new ImageIcon(getClass().getClassLoader().getResource("resources/Knight-B.png"));
        bRook = new ImageIcon(getClass().getClassLoader().getResource("resources/Rook-B.png"));
        bQueen = new ImageIcon(getClass().getClassLoader().getResource("resources/Queen-B.png"));
        bKing = new ImageIcon(getClass().getClassLoader().getResource("resources/King-B.png"));
        wBack = new ImageIcon[] {wRook, wKnight, wBishop, wQueen, wKing, wBishop, wKnight, wRook};
        bBack = new ImageIcon[] {bRook, bKnight, bBishop, bQueen, bKing, bBishop, bKnight, bRook};
        
        wPieces = new ArrayList<>();
        wPieces.addAll(Arrays.asList(wBack));
        wPieces.add(wPawn);
        bPieces = new ArrayList<>();
        bPieces.addAll(Arrays.asList(bBack));
        bPieces.add(bPawn);
        
        // SET SQUARE PATTERN ARRAYLISTS
        topSquares = new ArrayList(Arrays.asList(TOP_BORDER));
        leftSquares = new ArrayList(Arrays.asList(LEFT_BORDER));
        rightSquares = new ArrayList(Arrays.asList(RIGHT_BORDER));
        bottomSquares = new ArrayList(Arrays.asList(BOTTOM_BORDER));
        
        top2rows = new ArrayList(Arrays.asList(TOP_BORDER));
        top2rows.addAll(Arrays.asList(ROW2));
        left2columns = new ArrayList(Arrays.asList(LEFT_BORDER));
        left2columns.addAll(Arrays.asList(COL2));
        right2columns = new ArrayList(Arrays.asList(RIGHT_BORDER));
        right2columns.addAll(Arrays.asList(COL7));
        bottom2rows = new ArrayList(Arrays.asList(BOTTOM_BORDER));
        bottom2rows.addAll(Arrays.asList(ROW7));
        
        boardFEN = new String();                                                // Instantiate FEN

        panelButton = new JPanel();                                             // Instantiate button panel
        panelButton.setLayout(new GridLayout(ROWS, COLS));                      // Sets Layout
        buttonArray = new JButton[SQUARES];                                     // Create array of buttons for squares
        buttonReset = new JButton("Reset");                                     // Create reset button
        buttonReset.addActionListener((ActionEvent ae) -> {
            resetGame();
        });
        this.add(buttonReset, BorderLayout.SOUTH);                              // Add reset button to frame

        textArea = new JTextArea();                                             // Create a text area to display game info
        this.add(textArea, BorderLayout.EAST);                                  // Add textArea to frame

        for (int i = 0; i < ROWS * COLS; i++) {                 //Initialize array of buttons
            buttonArray[i] = new JButton();
            //buttonArray[i].setFocusPainted(rootPaneCheckingEnabled);
            buttonArray[i].setActionCommand(Integer.toString(i));
            buttonArray[i].setPreferredSize(squareSize);
            buttonArray[i].addActionListener(new ButtonClickHandler());
            buttonArray[i].setOpaque(true);
            buttonArray[i].setBorder(null);

            if (((i + 1) % 2 == 0 && (i / 8 + 1) % 2 != 0)
                    || ((i + 1) % 2 != 0 && (i / 8 + 1) % 2 == 0)) {

                buttonArray[i].setBackground(GREEN.darker().darker());
            } else {
                buttonArray[i].setBackground(VERY_LIGHT_GREY);
            }

            Component add = panelButton.add(buttonArray[i]);
        }
        add(panelButton);

        resetGame();
    }

    // METHOD TO LISTEN FOR CLICKED BUTTONS
    private class ButtonClickHandler implements ActionListener {

        // METHOD TO EXECUTE WHEN BUTTON PRESSED
        @Override
        public void actionPerformed(ActionEvent event) {
            JButton button = (JButton) event.getSource();
            int square = Integer.parseInt(button.getActionCommand());
            int row = square / ROWS;
            int col = square % COLS;

            file = getFileForColumn(col + 1);
            rank = 8 - row;

            if (!isPieceSelected) {
                selectSquare(square);
            } else {
                movePiece(square);
            }
            setFEN();
            textArea.setText("Turn: " + turn + "\n" 
                    + "FEN: " + boardFEN);
        }
    }

    // RESETS THE CHESS GAME
    private void resetGame() {
        
        
        for (int i = 0; i < 8; i++) {
            placePiece(i, bBack[i]);
        }
        for (int i = 8; i < 16; i++) {
            placePiece(i, bPawn);
        }
        for (int i = 16; i < 48; i++) {
            removePiece(i);
        }
        for (int i = 48; i < 56; i++) {
            placePiece(i, wPawn);
        }
        for (int i = 56; i < 64; i++) {
            placePiece(i, wBack[i - 56]);
        }

        move = turn = 1;
        //isWhiteTurn = true;
        selectedSquare = -1;
        isPieceSelected = false;
        boardFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        textArea.setText("Turn: " + turn + "\n" 
                    + "FEN: " + boardFEN);
    }
    
    // THIS METHOD NEEDS WORK, BUT WILL READ THE BOARD AND SET THE FEN
    private void setFEN() {
        
        textArea.setText("Turn: " + turn + "\n" 
                    + "FEN: " + boardFEN);
    }

    // PLACES A PIECE ON THE BOARD
    private void placePiece(int square, ImageIcon piece) {
        buttonArray[square].setIcon(piece);
        buttonArray[square].setOpaque(true);
        buttonArray[square].setBorder(null);
    }

    // REMOVES A PIECE FROM THE BOARD
    private void removePiece(int square) {
        buttonArray[square].setIcon(null);
        buttonArray[square].setOpaque(true);
        buttonArray[square].setBorder(null);
    }

    // CALLED WHEN THE USER CLICKS A SQUARE
    private void selectSquare(int square) {
        if (selectedSquare != -1) {
            buttonArray[selectedSquare].setBackground(originalColor);
        }
        piece = (ImageIcon) buttonArray[square].getIcon();
        if (piece != null && isValidPiece(piece)) {
            isPieceSelected = true;
            selectedSquare = square;
            selectedPiece = (ImageIcon) buttonArray[square].getIcon();
            originalColor = buttonArray[square].getBackground();
            buttonArray[square].setBackground(Color.blue);
            piece = null;
        }
    }

    // CALLED WHEN THE USER MOVES A PIECE
    private void movePiece(int square) {
        buttonArray[selectedSquare].setBackground(originalColor);
        
        // IF A PIECE HAS BEEN SELECTED, AND MOVE IS LEGAL, MOVE PIECE
        if (isPieceSelected && isMoveLegal(selectedSquare, square)) {
            removePiece(selectedSquare);
            placePiece(square, (ImageIcon) selectedPiece);
            if (move % 2 == 0)
                turn++;
            move++;      
        }
        isPieceSelected = false;
        selectedSquare = -1;
        selectedPiece = null;
    }

    // CHECKS TO SEE IF MOVE IS LEGAL
    private boolean isMoveLegal(int startSquare, int endSquare) {
        boolean isLegal = false;
        
        if ((selectedPiece == wPawn || selectedPiece == bPawn)
                && isPawnMove(startSquare, endSquare))
            isLegal = true;
        else
            if (getValidSquares(selectedPiece, selectedSquare).contains((Integer) endSquare))
                isLegal = true;

        return isLegal;
    }
    
    // CHECKS FOR VALID PAWN MOVE
    private boolean isPawnMove(int startSquare, int endSquare) {
        boolean isMove = false;
        List startRow;
        int twoForward;
        int oneForward;
        int lDiagonal;
        int rDiagonal;
        if (isWhitePiece(selectedPiece)) {
            startRow = Arrays.asList(WHITE_PAWN_ROW);
            twoForward = startSquare-16;
            oneForward = startSquare-8;
            lDiagonal = startSquare-9;
            rDiagonal = startSquare-7;
        } else {
            startRow = Arrays.asList(BLACK_PAWN_ROW);
            twoForward = startSquare+16;
            oneForward = startSquare+8;
            lDiagonal = startSquare+7;
            rDiagonal = startSquare+9;
        }
        
        if (buttonArray[endSquare].getIcon() == null) {                         // is moving to empty square 
            if (startRow.contains(startSquare)                                    // is moving from start point AND
              && endSquare == twoForward)                                         // AND is moving two squares forward
                isMove = true;
            else if (endSquare == oneForward)                                     // is moving one square forward
                isMove = true;
        } else if (isCapturing((ImageIcon) buttonArray[endSquare].getIcon())) { // is capturing opponent piece
            if ( (endSquare == rDiagonal && !isInRightColumn(startSquare))
              || (endSquare == lDiagonal && !isInLeftColumn(startSquare)) )
                isMove = true;
        }
        return isMove;
    }

    // RETURNS AN ARRAY LIST FOR ALL VALID MOVES (EXCEPT FOR PAWNS)
    private ArrayList getValidSquares(ImageIcon piece, int square) {
        int checkSquare;
        ImageIcon checkPiece;
        ArrayList validSquares = new ArrayList();
        
        // ------ GET VALID MOVES FOR KNIGHT -----
        if (piece == wKnight || piece == bKnight) {
            checkSquare = square-17;
            if (!top2rows.contains(square) 
                    && !leftSquares.contains(square)
                    && !(checkSquare < 0)) {
                if (!isOwnPiece(getPiece(checkSquare)))
                    validSquares.add(checkSquare);
            }
            checkSquare = square-15;
            if (!top2rows.contains(square)
                    && !rightSquares.contains(square)
                    && !(checkSquare < 0)) {
                if (!isOwnPiece(getPiece(checkSquare)))
                    validSquares.add(checkSquare);
            }
            checkSquare = square-10;
            if (!topSquares.contains(square)
                    && !left2columns.contains(square)
                    && !(checkSquare < 0)) {
                if (!isOwnPiece(getPiece(checkSquare)))
                    validSquares.add(checkSquare);
            }
            checkSquare = square-6;
            if (!topSquares.contains(square)
                    && !right2columns.contains(square)
                    && !(checkSquare < 0)) {
                if (!isOwnPiece(getPiece(checkSquare)))
                    validSquares.add(checkSquare);
            }
            checkSquare = square+6;
            if (!bottomSquares.contains(square)
                    && !left2columns.contains(square)
                    && !(checkSquare > 63)) {
                if (!isOwnPiece(getPiece(checkSquare)))
                    validSquares.add(checkSquare);
            }
            checkSquare = square+10;
            if (!bottomSquares.contains(square)
                    && !right2columns.contains(square)
                    && !(checkSquare > 63)) {
                if (!isOwnPiece(getPiece(checkSquare)))
                    validSquares.add(checkSquare);
            }
            checkSquare = square+15;
            if (!bottom2rows.contains(square)
                    && !leftSquares.contains(square)
                    && !(checkSquare > 63)) {
                if (!isOwnPiece(getPiece(checkSquare)))
                    validSquares.add(checkSquare);
            }
            checkSquare = square+17;
            if (!bottom2rows.contains(square)
                    && !rightSquares.contains(square)
                    && !(checkSquare > 63)) {
                if (!isOwnPiece(getPiece(checkSquare)))
                    validSquares.add(checkSquare);
            }
        } // - END OF GET VALID KNIGHT MOVES -
        
        // ------ GET VALID MOVES FOR BISHOP OR QUEEN ------
        if (piece == wBishop || piece == bBishop 
                || piece == wQueen || piece == bQueen ) {
            // Check squares to top left
            if (!topSquares.contains(square) && !leftSquares.contains(square)) {
                checkSquare = square;
                boolean cannotContinue = false; 
                do {
                    checkSquare -= 9;
                    if (!canKeepGoing(checkSquare))
                        cannotContinue = true;
                    else {
                        validSquares.add(checkSquare);
                        if (!isEmptySquare(checkSquare))
                            cannotContinue = true;
                    }
                } while (!cannotContinue);
            }
            
            // Check squares to top right
            if (!topSquares.contains(square) && !rightSquares.contains(square)) {
                checkSquare = square;
                boolean cannotContinue = false; 
                do {
                    checkSquare -=7;
                    if (!canKeepGoing(checkSquare))
                        cannotContinue = true;
                    else {
                        validSquares.add(checkSquare);
                        if (!isEmptySquare(checkSquare))
                            cannotContinue = true;
                    }
                } while (!cannotContinue);
            }
            
            // Check squares to bottom left
            if (!bottomSquares.contains(square) && !leftSquares.contains(square)) {
                checkSquare = square;
                boolean cannotContinue = false; 
                do {
                    checkSquare +=7;
                    if (!canKeepGoing(checkSquare))
                        cannotContinue = true;
                    else {
                        validSquares.add(checkSquare);
                        if (!isEmptySquare(checkSquare))
                            cannotContinue = true;
                    }
                } while (!cannotContinue);
            }
            
            // Check squares to bottom right
            if (!bottomSquares.contains(square) && !rightSquares.contains(square)) {
                checkSquare = square;
                boolean cannotContinue = false;    
                do {
                    checkSquare +=9;
                    if (!canKeepGoing(checkSquare))
                        cannotContinue = true;
                    else {
                        validSquares.add(checkSquare);
                        if (!isEmptySquare(checkSquare))
                            cannotContinue = true;
                    }
                } while (!cannotContinue);   

            }
        } // - END OF GET VALID BISHOP OR QUEEN MOVES -
        
        // ------ GET VALID MOVES FOR ROOK OR QUEEN ------
        if (piece == wRook || piece == bRook 
                || piece == wQueen || piece == bQueen ) {
            // Check squares to top
            if (!topSquares.contains(square)) {
                checkSquare = square;
                boolean cannotContinue = false; 
                do {
                    checkSquare -=8;
                    if (!canKeepGoing(checkSquare))
                        cannotContinue = true;
                    else {
                        validSquares.add(checkSquare);
                        if (!isEmptySquare(checkSquare))
                            cannotContinue = true;
                    }
                } while (!cannotContinue);
            }
            
            // Check squares to left
            if (!leftSquares.contains(square)) {
                checkSquare = square;
                boolean cannotContinue = false; 
                do {
                    checkSquare -=1;
                    if (!canKeepGoing(checkSquare))
                        cannotContinue = true;
                    else {
                        validSquares.add(checkSquare);
                        if (!isEmptySquare(checkSquare))
                            cannotContinue = true;
                    }
                } while (!cannotContinue);
            }
            
            // Check squares to right
            if (!rightSquares.contains(square)) {
                checkSquare = square;
                boolean cannotContinue = false; 
                do {
                    checkSquare += 1;
                    if (!canKeepGoing(checkSquare))
                        cannotContinue = true;
                    else {
                        validSquares.add(checkSquare);
                        if (!isEmptySquare(checkSquare))
                            cannotContinue = true;
                    }
                } while (!cannotContinue);
            }
            
            // Check squares to bottom
            if (!bottomSquares.contains(square)) {
                checkSquare = square;
                boolean cannotContinue = false;    
                do {
                    checkSquare +=9;
                    if (!canKeepGoing(checkSquare))
                        cannotContinue = true;
                    else {
                        validSquares.add(checkSquare);
                        if (!isEmptySquare(checkSquare))
                            cannotContinue = true;
                    }
                } while (!cannotContinue);   

            }
        } // - END OF GET VALID ROOK OR QUEEN MOVES -
        
        // ------ GET VALID KING MOVES ------
        if (piece == wKing || piece == bKing) { 
            for (int eachMove : KING_MOVES) {
                checkSquare = square + eachMove;
                if (!(checkSquare < 0 || checkSquare > 63)) {                  
                        checkPiece = (ImageIcon) getPiece(checkSquare);
                        if (!isOwnPiece(checkPiece)) {                   
                            if (!isKingThreatened(checkSquare)) {
                                validSquares.add(checkSquare);
                            }                          
                        }
                }
            }
            if ((leftSquares.contains(square) || topSquares.contains(square))
                    && validSquares.contains(-9))
                validSquares.remove(-9);
            if (topSquares.contains(square)
                    && validSquares.contains(-8))
                validSquares.remove(-8);
            if ((topSquares.contains(square) || rightSquares.contains(square))
                    && validSquares.contains(-7))
                validSquares.remove(-7);
            if (leftSquares.contains(square)
                    && validSquares.contains(-1))
                validSquares.remove(-1);
            if (rightSquares.contains(square)
                    && validSquares.contains(1))
                validSquares.remove(1);
            if ((leftSquares.contains(square) || bottomSquares.contains(square))
                    && validSquares.contains(7))
                validSquares.remove(7);
            if (bottomSquares.contains(square)
                    && validSquares.contains(8))
                validSquares.remove(8);
            if ((bottomSquares.contains(square) || rightSquares.contains(square))
                    && validSquares.contains(9))
                validSquares.remove(9);
        }
        
        return validSquares;
    }
        
    // CHECKS TO SEE IF KING IS/WOULD BE THREATENED ON A SQUARE
    private boolean isKingThreatened(int square) {
        boolean isThreatened = false;
        int checkSquare;
        ImageIcon checkPiece = null;
        
        // Check squares to top left
        if (!topSquares.contains(square) && !leftSquares.contains(square)) {
            checkSquare = square;
            boolean cannotContinue = false; 
            do {
                checkSquare -= 9;
                if (!canKeepGoing(checkSquare))
                    cannotContinue = true;
                else if (isCapturing(getPiece(checkSquare)))
                    checkPiece = (ImageIcon) getPiece(checkSquare);
                    if (checkPiece == wBishop || checkPiece == bBishop
                            || checkPiece == wQueen || checkPiece == bQueen) {
                        isThreatened = true;
                        cannotContinue = true;
                    }
            } while (!cannotContinue);
        }
        
        // Check squares to top
        if (!topSquares.contains(square)) {
            checkSquare = square;
            boolean cannotContinue = false; 
            do {
                checkSquare -=8;
                if (!canKeepGoing(checkSquare))
                    cannotContinue = true;
                else if (isCapturing(getPiece(checkSquare)))
                    checkPiece = (ImageIcon) getPiece(checkSquare);
                    if (checkPiece == wRook || checkPiece == bRook
                            || checkPiece == wQueen || checkPiece == bQueen) {
                        isThreatened = true;
                        cannotContinue = true;
                    }
            } while (!cannotContinue);
        }
            
        // Check squares to top right
        if (!topSquares.contains(square) && !rightSquares.contains(square)) {
            checkSquare = square;
            boolean cannotContinue = false; 
            do {
                checkSquare -=7;
                if (!canKeepGoing(checkSquare))
                    cannotContinue = true;
                else if (isCapturing(getPiece(checkSquare)))
                    checkPiece = (ImageIcon) getPiece(checkSquare);
                    if (checkPiece == wBishop || checkPiece == bBishop
                            || checkPiece == wQueen || checkPiece == bQueen) {
                        isThreatened = true;
                        cannotContinue = true;
                    }
            } while (!cannotContinue);
        }
            
        // Check squares to left
        if (!leftSquares.contains(square)) {
            checkSquare = square;
            boolean cannotContinue = false; 
            do {
                checkSquare -=1;
                if (!canKeepGoing(checkSquare))
                    cannotContinue = true;
                else if (isCapturing(getPiece(checkSquare)))
                    checkPiece = (ImageIcon) getPiece(checkSquare);
                    if (checkPiece == wRook || checkPiece == bRook
                            || checkPiece == wQueen || checkPiece == bQueen) {
                        isThreatened = true;
                        cannotContinue = true;
                    }
            } while (!cannotContinue);
        }
            
        // Check squares to right
        if (!rightSquares.contains(square)) {
            checkSquare = square;
            boolean cannotContinue = false; 
            do {
                checkSquare += 1;
                if (!canKeepGoing(checkSquare))
                    cannotContinue = true;
                 else if (isCapturing(getPiece(checkSquare)))
                    checkPiece = (ImageIcon) getPiece(checkSquare);
                    if (checkPiece == wRook || checkPiece == bRook
                            || checkPiece == wQueen || checkPiece == bQueen) {
                        isThreatened = true;
                        cannotContinue = true;
                    }
            } while (!cannotContinue);
        }
        
        // Check squares to bottom left
        if (!bottomSquares.contains(square) && !leftSquares.contains(square)) {
            checkSquare = square;
            boolean cannotContinue = false; 
            do {
                checkSquare +=7;
                if (!canKeepGoing(checkSquare))
                    cannotContinue = true;
                else if (isCapturing(getPiece(checkSquare)))
                    checkPiece = (ImageIcon) getPiece(checkSquare);
                    if (checkPiece == wBishop || checkPiece == bBishop
                            || checkPiece == wQueen || checkPiece == bQueen) {
                        isThreatened = true;
                        cannotContinue = true;
                    }
            } while (!cannotContinue);
        }
            
        // Check squares to bottom
        if (!bottomSquares.contains(square)) {
            checkSquare = square;
            boolean cannotContinue = false;    
            do {
                checkSquare +=9;
                if (!canKeepGoing(checkSquare))
                    cannotContinue = true;
                else if (isCapturing(getPiece(checkSquare)))
                    checkPiece = (ImageIcon) getPiece(checkSquare);
                    if (checkPiece == wRook || checkPiece == bRook
                            || checkPiece == wQueen || checkPiece == bQueen) {
                        isThreatened = true;
                        cannotContinue = true;
                    }
            } while (!cannotContinue);   
        }
            
        // Check squares to bottom right
        if (!bottomSquares.contains(square) && !rightSquares.contains(square)) {
            checkSquare = square;
            boolean cannotContinue = false;    
            do {
                checkSquare +=9;
                if (!canKeepGoing(checkSquare))
                    cannotContinue = true;
                else if (isCapturing(getPiece(checkSquare)))
                    checkPiece = (ImageIcon) getPiece(checkSquare);
                    if (checkPiece == wBishop || checkPiece == bBishop
                            || checkPiece == wQueen || checkPiece == bQueen) {
                        isThreatened = true;
                        cannotContinue = true;
                    }
            } while (!cannotContinue);   

        }

        return isThreatened;
    }
    
    // WHEN GATHERING VALID MOVES IN A LINE, CHECKS IF IT SHOULD KEEP LOOKING
    private boolean canKeepGoing(int square) {
        boolean outOfBounds = false;
        boolean hitOwnPiece = false;
        
        if (square < 0 || square > 63)
            outOfBounds = true;
        else if (isOwnPiece(buttonArray[square].getIcon()))
            hitOwnPiece = true;
        
        return !(outOfBounds || hitOwnPiece);
    }
    
    // CHECKS TO SEE IF A PIECE IS WHITE (That's Racist!)
    private boolean isWhitePiece(ImageIcon piece) {
        boolean isWhite = false;

        if (wPieces.contains(piece)) {
            isWhite = true;
        }

        return isWhite;
    }
    
    // CHECKS TO SEE IF A PIECE IS BLACK (That's racial profiling!)
    private boolean isBlackPiece(ImageIcon piece) {
        boolean isBlack = false;

        if (bPieces.contains(piece)) {
            isBlack = true;
        }

        return isBlack;
    }
    
    // CHECKS TO SEE IF A SQUARE IS EMPTY
    private boolean isEmptySquare(int square) {
        return ( !( isWhitePiece((ImageIcon) getPiece(square)) 
                || isBlackPiece((ImageIcon) getPiece(square)) ) );
    }
    
    // CHECKS TO SEE IF A PIECE WOULD BE CAPTURING ANOTHER PIECE
    private boolean isCapturing(Icon piece) {
        boolean isCap = false;
        
        if ( (isWhitePiece(selectedPiece) && isBlackPiece((ImageIcon) piece))
                || (isBlackPiece(selectedPiece) && isWhitePiece((ImageIcon) piece)) )
            isCap = true;
        
        return isCap;
    }
    
    // CHECKS TO SEE IF A PIECE WOULD BE TRYING TO CAPTURE IT'S OWN PIECE
    private boolean isOwnPiece(Icon piece) {
        boolean isOwn = false;
        
        if ( (isWhitePiece(selectedPiece) && isWhitePiece((ImageIcon) piece))
                || (isBlackPiece(selectedPiece) && isBlackPiece((ImageIcon) piece)) )
            isOwn = true;
        
        return isOwn;
    }

    // CHECKS TO SEE IF CLICKED PIECE IS VALID TO SELECT
    private boolean isValidPiece(ImageIcon piece) {
        boolean isValid = false;

        if ((move % 2 == 0 && !isWhitePiece(piece))
                || (move % 2 != 0 && isWhitePiece(piece))) {
            isValid = true;
        }

        return isValid;
    }
    
    // CHECKS TO SEE IF A SQUARE IS IN THE LEFT COLUMN
    private boolean isInLeftColumn(int square) {
        boolean onLeft = false;
        if ((square+8) % 8 == 0)
            onLeft = true;
        return onLeft;
    }
    
    // CHECKS TO SEE IF A SQUARE IS IN THE RIGHT COLUMN
    private boolean isInRightColumn(int square) {
        boolean onRight = false;
        if ((square+1) % 8 == 0)
            onRight = true;
        return onRight;
    }

    // METHOD TO RETURN FILE (a-h) CORRESPONDING TO COLUMN NUMBER (1-8)
    private char getFileForColumn(int c) {
        char retVal;
        if (c > 0 && c < 9) {
            retVal = (char) (c + 96);
        } else {
            retVal = (char) 0;
        }
        return retVal;
    }
    
    // METHOD TO RETURN PIECE IN A SQUARE
    private Icon getPiece(int square) {
        return buttonArray[square].getIcon();
    }
    
    // ALTERS THE FEN AT A POSITION
    private void setFEN(int pos, char c) {
        StringBuilder sb = new StringBuilder(boardFEN);
        sb.setCharAt(pos, c);
        boardFEN = sb.toString();
    }
    
    // USED TO DISPLAY AN ERROR WHEN DEBUGGING
    private void displayError(String errMsg) {
        JOptionPane optionPane = new JOptionPane(errMsg, JOptionPane.ERROR_MESSAGE);   
        JDialog dialog = optionPane.createDialog("Error");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
}

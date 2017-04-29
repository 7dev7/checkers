package com.dev.ui;

import com.dev.checkers.Checker;
import com.dev.checkers.CheckerBox;
import com.dev.checkers.NetworkAnimator;
import com.dev.connection.ConnectionPacket;
import com.dev.connection.ConnectionSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class CheckerBoard extends JPanel implements MouseListener {
    public static final int BOTTOM_OFFSET = 26;
    private final int RED = 1, BLACK = 2;
    private CheckerBox checkers;
    private Checker selectedChecker;
    private int squareWidth, checkersWidth;
    private int turn;
    private boolean moveSingle;
    private int clickableRange;
    private BottomControlPane bottomPane;
    private Image kingImg, blackImg, redImg;
    private int kingOffset;
    private int winner;

    private ConnectionSettings connectionSettings;
    private boolean mouseDownTriggered = false;

    public CheckerBoard(int squareWidth, int checkersWidth, ConnectionSettings connectionSettings) {
        this.connectionSettings = connectionSettings;
        setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.DARK_GRAY));
        NetworkAnimator.setCheckerBoard(this);

        loadImages();
        clickableRange = squareWidth * 8;
        this.squareWidth = squareWidth;
        this.checkersWidth = checkersWidth;

        newGame();

        this.addMouseListener(this);
    }

    private void loadImages() {
        Class<? extends CheckerBoard> resourceLoader = getClass();
        blackImg = Toolkit.getDefaultToolkit().getImage(resourceLoader.getResource("/black.png"));
        redImg = Toolkit.getDefaultToolkit().getImage(resourceLoader.getResource("/red.png"));
        kingImg = Toolkit.getDefaultToolkit().getImage(resourceLoader.getResource("/king.gif"));
        int height = kingImg.getHeight(this);
        kingOffset = (int) ((squareWidth / 2.0) - (height / 2.0));
    }

    public void newGame() {
        winner = -1;
        turn = RED;
        moveSingle = true;
        if (bottomPane != null) {
            bottomPane.displayMessage("Red Starts");
        }
        checkers = new CheckerBox(checkersWidth);
        selectedChecker = null;
        int alternate = 1;
        int direction = 1;

        Image img = blackImg;
        int ident = BLACK;
        for (int i = 0; i < 8; ++i) {
            if (i == 5) {
                img = redImg;
                direction = -1;
                ident = RED;
            }
            if (i != 3 && i != 4) {
                for (int j = 0; j < 8; ++j) {
                    if (j % 2 == alternate) {
                        checkers.addChecker(j, i, direction, img, ident);
                    }
                }
                if (alternate == 1) alternate = 0;
                else alternate = 1;
            }
        }
        repaint();
    }

    public boolean validMove(int row, int col) {
        int selectedRow, selectedCol;
        selectedRow = selectedChecker.getRow();
        selectedCol = selectedChecker.getCol();
        boolean temp;

        if (selectedChecker.getType() == turn) {
            if (!selectedChecker.isKing()) {
                if (moveSingle && selectedChecker.getCol() + selectedChecker.getDirection() == col) {
                    if (selectedRow + 1 == row || selectedRow - 1 == row) {
                        temp = checkers.moveChecker(selectedRow, selectedChecker.getCol(), row, col);
                        if (temp) changeTurns();
                        return temp;
                    }
                } else if (selectedChecker.getCol() + selectedChecker.getDirection() * 2 == col) {
                    return validate(selectedRow, row, col);
                }
            } else {
                if (moveSingle && (selectedRow + 1 == row || selectedRow - 1 == row) && (selectedCol + 1 == col || selectedCol - 1 == col)) {
                    temp = checkers.moveChecker(selectedRow, selectedChecker.getCol(), row, col);
                    if (temp) changeTurns();
                    return temp;
                } else if (selectedChecker.getCol() + 2 == col || selectedChecker.getCol() - 2 == col) {
                    return validate(selectedRow, row, col);
                }
            }
        }
        return false;
    }

    private boolean validate(int selectedRow, int row, int col) {
        boolean state = false;
        if (selectedRow + 2 == row || selectedRow - 2 == row) {
            state = isValidJump(row, col, true);
            if (!state) return false;
            state = checkers.moveChecker(selectedRow, selectedChecker.getCol(), row, col);
            if (state) {
                moveSingle = false;
                autoChangeTurn();
            }
        }
        return state;
    }

    public void setBottomControlPane(BottomControlPane bottomPane) {
        this.bottomPane = bottomPane;
        bottomPane.displayMessage("Red Starts");
    }

    public void networkDoMove(int fromRow, int fromCol, int toRow, int toCol) {
        if ((turn == RED && connectionSettings.isRedNetworkPlayer()) || (turn == BLACK && connectionSettings.isBlackNetworkPlayer())) {
            NetworkAnimator.doAnimatedMove(checkers, fromRow, fromCol, toRow, toCol);
        }
    }

    public void networkDoEndTurn() {
        changeTurns();
    }

    public void networkSendMove(int fromRow, int fromCol, int toRow, int toCol) {
        ConnectionPacket packet = ConnectionPacket.newBuilder()
                .setPacketStatus(ConnectionPacket.PacketStatus.MOVE)
                .setFromValues(fromRow, fromCol)
                .setToValues(toRow, toCol)
                .setGameId(connectionSettings.getGameID()).build();
        connectionSettings.getConnector().move(packet);
    }

    public void networkStartGame() {
        newGame();
    }

    private boolean isValidJump(int row, int col, boolean removeChecker) {
        int middleCol = col, middleRow = row;
        boolean success;

        if (selectedChecker.getCol() < col) middleCol--;
        else middleCol++;
        if (selectedChecker.getRow() < row) middleRow--;
        else middleRow++;

        Checker c = checkers.getChecker(middleRow, middleCol);
        if (c == null || (c.getImage() == selectedChecker.getImage()) || (checkers.getChecker(row, col) != null))
            return false;
        if (removeChecker) {
            success = checkers.removeChecker(middleRow, middleCol);
            repaint();
            return success;
        } else
            return true;
    }

    private void drawSquares(Graphics page) {
        page.setColor(Color.GRAY);
        page.drawRect(-5, -5, squareWidth * 8 + 5, squareWidth * 8 + 5);
        page.setColor(Color.WHITE);
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if ((i + j) % 2 == 0) {
                    page.fillRect(i * squareWidth, j * squareWidth, squareWidth, squareWidth);
                }
            }
        }
    }

    private void drawCheckers(Graphics page) {
        Checker[] checkArray = checkers.getCheckers();
        for (Checker aCheckArray : checkArray) {
            if (aCheckArray != null) {
                int chekerWidth = checkers.getWidth();
                int offset = (squareWidth - chekerWidth) / 2;

                if (aCheckArray.isSelected()) {
                    Graphics2D gd = (Graphics2D) page;

                    if (aCheckArray.isKing()) gd.setStroke(new BasicStroke(14.0f));
                    else gd.setStroke(new BasicStroke(6.0f));

                    gd.drawOval(aCheckArray.getRow() * squareWidth + offset,
                            aCheckArray.getCol() * squareWidth + offset,
                            chekerWidth, chekerWidth);
                }
                page.drawImage(aCheckArray.getImage(),
                        aCheckArray.getRow() * squareWidth + offset,
                        aCheckArray.getCol() * squareWidth + offset, this);

                if (aCheckArray.isKing()) {
                    page.drawImage(kingImg,
                            aCheckArray.getRow() * squareWidth + kingOffset,
                            aCheckArray.getCol() * squareWidth + kingOffset, this);
                }
            }
        }
    }

    private void changeTurns() {
        checkForKingMe();
        if (turn == RED) {
            turn = BLACK;
            bottomPane.displayMessage("It is Blacks Turn");
        } else {
            turn = RED;
            bottomPane.displayMessage("It is Reds Turn");
        }
        checkForWinner();
        selectedChecker.setSelected(false);
        repaint();
        moveSingle = true;
    }

    private void checkForKingMe() {
        if (selectedChecker != null && !selectedChecker.isKing()) {
            if (selectedChecker.getCol() == 7 && selectedChecker.getDirection() == 1) {
                selectedChecker.setKing(true);
            } else if (selectedChecker.getCol() == 0 && selectedChecker.getDirection() == -1) {
                selectedChecker.setKing(true);
            }
        }
    }

    private void checkForWinner() {
        if (winner == -1) {
            int b = 0, r = 0;
            Checker[] checkArray = checkers.getCheckers();
            for (Checker aCheckArray : checkArray) {
                if (aCheckArray != null) {
                    if (aCheckArray.getType() == RED) r++;
                    else b++;
                }
            }
            if (b == 0) winner = RED;
            else if (r == 0) winner = BLACK;
        }
    }

    private void drawWinnerScreen() {
        if (winner == RED) MessageViewer.showMessage(this, "Red Wins!", "Win");
        else if (winner == BLACK) MessageViewer.showMessage(this, "Black Wins!", "Win");
        else MessageViewer.showMessage(this, "Dead heat!", "");
        newGame();
    }

    private void autoChangeTurn() {
        int col = selectedChecker.getCol();
        int row = selectedChecker.getRow();
        boolean temp, isMove = false;
        int[] r, c;

        if (selectedChecker.isKing()) {
            r = new int[]{row + 2, row + 2, row - 2, row - 2};
            c = new int[]{col + 2, col - 2, col + 2, col - 2};
        } else {
            r = new int[]{row + 2, row - 2};
            c = new int[]{col + selectedChecker.getDirection() * 2, col + selectedChecker.getDirection() * 2};
        }

        for (int i = 0; i < r.length; i++) {
            if (r[i] >= 0 && c[i] >= 0 && r[i] < 8 && c[i] < 8) {
                temp = isValidJump(r[i], c[i], false);

                if (temp) {
                    Checker cck = checkers.getChecker(r[i], c[i]);
                    if (cck == null) {
                        isMove = true;
                    }
                }
            }
        }
        if (!isMove) changeTurns();
    }

    public void setSelectedChecker(Checker c) {
        selectedChecker = c;
    }

    public boolean doMove(int row, int col) {
        if (selectedChecker != null) {
            if (!validMove(row, col) && moveSingle)
                selectedChecker.setSelected(false);
            else {
                checkForKingMe();
                return true;
            }
        }
        if (moveSingle) {
            selectedChecker = checkers.getChecker(row, col);

            if (selectedChecker != null && selectedChecker.getType() == turn)
                selectedChecker.setSelected(true);
        }
        return false;
    }

    protected void paintComponent(Graphics image) {
        super.paintComponent(image);
        drawSquares(image);
        drawCheckers(image);
        if (winner > 0) {
            bottomPane.displayMessage("Game Over");
            drawWinnerScreen();
        }
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (!mouseDownTriggered && event.getX() <= clickableRange && event.getY() <= clickableRange) {
            if (turn == RED && connectionSettings.isRedNetworkPlayer()) {
                return;
            } else if (turn == BLACK && connectionSettings.isBlackNetworkPlayer()) {
                return;
            }
            int frow = -1;
            int fcol = -1;
            if (selectedChecker != null) {
                frow = selectedChecker.getRow();
                fcol = selectedChecker.getCol();
            }
            int row = (int) Math.floor(event.getX() / (double) squareWidth);
            int col = (int) Math.floor(event.getY() / (double) squareWidth);
            boolean goodMove = doMove(row, col);
            if (frow != -1 && goodMove && ((connectionSettings.isRedNetworkPlayer()) || (connectionSettings.isBlackNetworkPlayer()))) {
                networkSendMove(frow, fcol, row, col);
            }
            mouseDownTriggered = true;
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        mouseDownTriggered = false;
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    public ConnectionSettings getConnectionSettings() {
        return connectionSettings;
    }
}

package com.dev.checkers;

import com.dev.ui.CheckerBoard;

public class NetworkAnimator {
    private static CheckerBoard game;

    public static void setCheckerBoard(CheckerBoard game) {
        NetworkAnimator.game = game;
    }

    private static void doAnimatedMove(Checker c, int row, int col) {
        game.setSelectedChecker(c);
        game.repaint();
        try {
            Thread.sleep(700);
            game.repaint();
        } catch (InterruptedException e) {
        }
        game.doMove(row, col);
        game.repaint();
    }

    public static void doAnimatedMove(CheckerBox checkers, int fromRow, int fromCol, int toRow, int toCol) {
        Checker c = checkers.getChecker(fromRow, fromCol);
        c.setSelected(true);
        doAnimatedMove(c, toRow, toCol);
    }
}

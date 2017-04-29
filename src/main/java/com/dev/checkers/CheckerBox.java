package com.dev.checkers;

import java.awt.*;

public class CheckerBox {
    private Checker[][] checkerBox;
    private Checker[] references;
    private int checkerWidth, refIndex;

    public CheckerBox(int checkerWidth) {
        this.checkerWidth = checkerWidth;
        references = new Checker[24];
        refIndex = 0;
        checkerBox = new Checker[8][8];
    }

    public boolean addChecker(int row, int col, int direction, Image img, int ident) {
        if (checkerBox[row][col] != null) return false;
        Checker checker = new Checker(row, col, direction, img, ident);
        checkerBox[row][col] = checker;
        if (refIndex < references.length) {
            references[refIndex] = checker;
            refIndex++;
        } else {
            int i = 0;
            while (i < references.length) {
                if (references[i] == null) {
                    references[i] = checker;
                    break;
                }
                i++;
            }
        }
        return true;
    }

    public boolean moveChecker(int rowFrom, int colFrom, int rowTo, int colTo) {
        if (getChecker(rowTo, colTo) == null) {
            Checker tempChecker = getChecker(rowFrom, colFrom);
            checkerBox[rowFrom][colFrom] = null;
            checkerBox[rowTo][colTo] = tempChecker;
            tempChecker.setRow(rowTo);
            tempChecker.setCol(colTo);
            return true;
        }
        return false;
    }

    public boolean removeChecker(int row, int col) {
        Checker removeMe = getChecker(row, col);
        if (removeMe != null) {
            for (int i = 0; i < references.length; ++i) {
                if (removeMe == references[i])
                    references[i] = null;
            }
            checkerBox[row][col] = null;
            return true;
        }
        return false;
    }

    public Checker getChecker(int row, int col) {
        return checkerBox[row][col];
    }

    public Checker[] getCheckers() {
        return references;
    }

    public int getWidth() {
        return checkerWidth;
    }
}

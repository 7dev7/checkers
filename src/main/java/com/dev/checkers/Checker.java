package com.dev.checkers;

import java.awt.*;

public class Checker {
    private Image image;
    private boolean isKing;
    private boolean isSelected;
    private int row, col, direction;
    //1 = RED, 2 = BLACK
    private int type;

    public Checker(int row, int col, int direction, Image image, int type) {
        this.direction = direction;
        this.row = row;
        this.col = col;
        this.image = image;
        isKing = false;
        isSelected = false;
        this.type = type;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isKing() {
        return isKing;
    }

    public void setKing(boolean king) {
        isKing = king;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Checker{" +
                "image=" + image +
                ", isKing=" + isKing +
                ", isSelected=" + isSelected +
                ", row=" + row +
                ", col=" + col +
                ", direction=" + direction +
                ", type=" + type +
                '}';
    }
}

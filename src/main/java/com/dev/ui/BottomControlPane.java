package com.dev.ui;

import javax.swing.*;
import java.awt.*;

public class BottomControlPane extends JPanel {
    private JLabel messageBox;

    public BottomControlPane(int width, int height) {
        setPreferredSize(new Dimension(width + 2, height));
        this.setLayout(new BorderLayout(10, 0));
        messageBox = new JLabel("");
        messageBox.setFont(new Font(null, Font.BOLD, 14));
        add(messageBox, BorderLayout.CENTER);
    }

    public void displayMessage(String message) {
        messageBox.setText(message);
    }
}

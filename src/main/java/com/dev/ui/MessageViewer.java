package com.dev.ui;

import javax.swing.*;
import java.awt.*;

public class MessageViewer {

    public static void showMessage(Component parentComponent, String message, String title) {
        JOptionPane.showConfirmDialog(parentComponent,
                message, title, JOptionPane.DEFAULT_OPTION);
    }
}

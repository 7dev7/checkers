package com.dev.checkers;

import com.dev.connection.ConnectionSettings;
import com.dev.ui.BottomControlPane;
import com.dev.ui.CheckerBoard;
import com.dev.ui.ConnectGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ProgramChecker {
    private static JMenuItem[] menuItems;
    private static JFrame frame;
    private static CheckerBoard board;

    private ConnectionSettings connectionSettings = new ConnectionSettings();

    public void start() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        frame = new JFrame("Checkers");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JMenuBar menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);

        board = new CheckerBoard(50, 38, connectionSettings);
        board.setPreferredSize(new Dimension(50 * 8, 50 * 8));
        BottomControlPane controlPane = new BottomControlPane(50 * 8, CheckerBoard.BOTTOM_OFFSET);

        board.setBottomControlPane(controlPane);

        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.getContentPane().add(board);
        frame.getContentPane().add(controlPane);
        frame.pack();
        frame.setVisible(true);

        board.repaint();
    }

    private JMenuBar createMenuBar() {
        JMenu menu = new JMenu("Game");
        MenuItemListener listener = new MenuItemListener();

        menuItems = new JMenuItem[2];

        menuItems[0] = new JMenuItem("New Game");
        menuItems[0].addActionListener(listener);
        menuItems[1] = new JMenuItem("New Network Game");
        menuItems[1].addActionListener(listener);

        menu.add(menuItems[0]);
        menu.add(menuItems[1]);

        JMenuBar jmb = new JMenuBar();
        jmb.add(menu);
        return jmb;
    }

    private class MenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            Object source = event.getSource();
            if (source == menuItems[0]) {
                //New Game
                if (connectionSettings.getConnector() != null) {
                    connectionSettings.getConnector().destroyConnection();
                }
                board.newGame();
            } else if (source == menuItems[1]) {
                ConnectGUI con = new ConnectGUI(frame, "Network Game Setup", board);
                con.pack();
                con.setVisible(true);
            }
        }
    }
}

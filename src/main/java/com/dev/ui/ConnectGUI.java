package com.dev.ui;

import com.dev.connection.ConnectionSettings;
import com.dev.connection.Connector;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ConnectGUI extends JDialog implements ActionListener {
    private JTextField joinUserName;
    private JTextField joinHostIP;
    private JTextField joinPort;
    private JTextField hostUserName;
    private JTextField hostPort;

    private JRadioButton hostButton;
    private JRadioButton joinButton;

    private JPanel joinPanel;
    private JPanel hostPanel;

    private JButton connectWait;
    private JButton cancel;

    private ConnectionSettings connectionSettings;
    private CheckerBoard checkerBoard;
    private ConnectionWaiter hostWait;

    public ConnectGUI(JFrame frame, String name, CheckerBoard checkerBoard) {
        super(frame, name, true);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.connectionSettings = checkerBoard.getConnectionSettings();

        if (connectionSettings.getConnector() != null) {
            connectionSettings.getConnector().destroyConnection();
        }

        this.checkerBoard = checkerBoard;
        setResizable(false);
        renderGUI();
    }

    private void renderGUI() {
        JPanel temp, temp2;
        JPanel container = new JPanel();
        container.setPreferredSize(new Dimension(450, 310));
        container.setLayout(new FlowLayout(FlowLayout.LEFT));

        ButtonGroup group = new ButtonGroup();
        hostButton = new JRadioButton("Host");
        hostButton.addActionListener(this);
        joinButton = new JRadioButton("Join");
        joinButton.addActionListener(this);
        group.add(joinButton);
        group.add(hostButton);

        joinPanel = new JPanel();
        joinPanel.setBorder(BorderFactory.createEtchedBorder());
        joinPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        joinPanel.setPreferredSize(new Dimension(400, 100));

///////// Begin JoinUserName
        temp = new JPanel();
        temp.setLayout(new BorderLayout());
        temp2 = new JPanel();
        temp2.setPreferredSize(new Dimension(80, 25));
        temp2.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel("Username ");
        temp2.add(label);
        temp.add(temp2, BorderLayout.WEST);

        temp2 = new JPanel();
        temp2.setPreferredSize(new Dimension(250, 25));
        temp2.setLayout(new FlowLayout(FlowLayout.LEFT));
        joinUserName = new JTextField();
        joinUserName.setColumns(16);
        temp2.add(joinUserName);
        temp.add(temp2, BorderLayout.EAST);
        joinPanel.add(temp);

///////// Begin JoinHostIP
        temp = new JPanel();
        temp.setLayout(new BorderLayout());
        temp2 = new JPanel();
        temp2.setPreferredSize(new Dimension(80, 25));
        temp2.setLayout(new FlowLayout(FlowLayout.LEFT));
        label = new JLabel("Host IP ");
        temp2.add(label);
        temp.add(temp2, BorderLayout.WEST);

        temp2 = new JPanel();
        temp2.setPreferredSize(new Dimension(250, 25));
        temp2.setLayout(new FlowLayout(FlowLayout.LEFT));
        joinHostIP = new JTextField();
        joinHostIP.setColumns(15);
        temp2.add(joinHostIP);
        temp.add(temp2, BorderLayout.EAST);
        joinPanel.add(temp);

///////// Begin JoinPort
        temp = new JPanel();
        temp.setLayout(new BorderLayout());
        temp2 = new JPanel();
        temp2.setPreferredSize(new Dimension(80, 25));
        temp2.setLayout(new FlowLayout(FlowLayout.LEFT));
        label = new JLabel("Port ");
        temp2.add(label);
        temp.add(temp2, BorderLayout.WEST);

        temp2 = new JPanel();
        temp2.setPreferredSize(new Dimension(250, 25));
        temp2.setLayout(new FlowLayout(FlowLayout.LEFT));
        joinPort = new JTextField();
        joinPort.setColumns(5);
        temp2.add(joinPort);
        temp.add(temp2, BorderLayout.EAST);
        joinPanel.add(temp);

        hostPanel = new JPanel();
        hostPanel.setBorder(BorderFactory.createEtchedBorder());
        hostPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        hostPanel.setPreferredSize(new Dimension(400, 70));

///////// Begin HostUserName
        temp = new JPanel();
        temp.setLayout(new BorderLayout());
        temp2 = new JPanel();
        temp2.setPreferredSize(new Dimension(80, 25));
        temp2.setLayout(new FlowLayout(FlowLayout.LEFT));
        label = new JLabel("Username ");
        temp2.add(label);
        temp.add(temp2, BorderLayout.WEST);

        temp2 = new JPanel();
        temp2.setPreferredSize(new Dimension(250, 25));
        temp2.setLayout(new FlowLayout(FlowLayout.LEFT));
        hostUserName = new JTextField();
        hostUserName.setColumns(16);
        temp2.add(hostUserName);
        temp.add(temp2, BorderLayout.EAST);
        hostPanel.add(temp);

///////// Begin HostPort
        temp = new JPanel();
        temp.setLayout(new BorderLayout());
        temp2 = new JPanel();
        temp2.setPreferredSize(new Dimension(80, 25));
        temp2.setLayout(new FlowLayout(FlowLayout.LEFT));
        label = new JLabel("Port ");
        temp2.add(label);
        temp.add(temp2, BorderLayout.WEST);

        temp2 = new JPanel();
        temp2.setPreferredSize(new Dimension(250, 25));
        temp2.setLayout(new FlowLayout(FlowLayout.LEFT));
        hostPort = new JTextField();
        hostPort.setColumns(5);
        temp2.add(hostPort);
        temp.add(temp2, BorderLayout.EAST);
        hostPanel.add(temp);

//////// buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setPreferredSize(new Dimension(440, 70));

        connectWait = new JButton();
        connectWait.addActionListener(this);
        connectWait.setText("Connect / Wait");
        buttonPanel.add(connectWait);

        cancel = new JButton();
        cancel.addActionListener(this);
        cancel.setText("Cancel");
        buttonPanel.add(cancel);

///////// Add Join Radio Button
        temp = new JPanel();
        temp.setLayout(new FlowLayout(FlowLayout.LEFT));
        temp.add(joinButton);
        container.add(temp);

////////// Add the join panel
        temp = new JPanel();
        temp.setPreferredSize(new Dimension(440, 105));
        temp.setLayout(new FlowLayout(FlowLayout.CENTER));
        temp.add(joinPanel);
        container.add(temp);

////////// Add the host Radio Button
        temp = new JPanel();
        temp.setLayout(new FlowLayout(FlowLayout.LEFT));
        temp.add(hostButton);
        container.add(temp);

//////////Add the join panel
        temp = new JPanel();
        temp.setPreferredSize(new Dimension(440, 75));
        temp.setLayout(new FlowLayout(FlowLayout.CENTER));
        temp.add(hostPanel);
        container.add(temp);

///////// Add the button panel
        container.add(buttonPanel);

        getContentPane().add(container);

        joinButton.setSelected(true);
        setHostEnabled(false);

        hostPort.setText("5000");
        joinPort.setText("5000");
        hostUserName.setText("UserHost");
        joinUserName.setText("UserClient");
        joinHostIP.setText("127.0.0.1");
    }

    private void setHostEnabled(boolean value) {
        hostUserName.setEnabled(value);
        hostPort.setEnabled(value);
        hostPanel.setEnabled(value);
    }

    private void setJoinEnabled(boolean value) {
        joinUserName.setEnabled(value);
        joinHostIP.setEnabled(value);
        joinPort.setEnabled(value);
        joinPanel.setEnabled(value);
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == hostButton || event.getSource() == joinButton) {
            setHostEnabled(hostButton.isSelected());
            setJoinEnabled(joinButton.isSelected());
        } else if (event.getSource() == connectWait) {
            if (validInput()) {
                connectWait.setEnabled(false);
                if (hostButton.isSelected()) setHostEnabled(false);
                if (joinButton.isSelected()) setJoinEnabled(false);
                joinButton.setEnabled(false);
                hostButton.setEnabled(false);
                createConnector();
            }
        } else if (event.getSource() == cancel) {
            if (hostWait != null) {
                cancelHostWait();
            } else {
                connectionSettings.cleanup();
                dispose();
            }
        }
    }

    private void createConnector() {
        Connector connector;
        connectionSettings.setGameID(-1);
        if (this.hostButton.isSelected()) {
            connectionSettings.setLocalUsername(hostUserName.getText());
            connectionSettings.setGameID((int) (Math.random() * 10000 + 12345));
            connectionSettings.setPort(Integer.parseInt(hostPort.getText()));
            connectionSettings.setRedNetworkPlayer(true);
            connectionSettings.setBlackNetworkPlayer(false);
            connectWait.setText("Waiting...");
            connectWait.setEnabled(false);
            hostWait = new ConnectionWaiter(checkerBoard);
            hostWait.start();
        } else {
            connectionSettings.setLocalUsername(joinUserName.getText());
            connectionSettings.setPort(Integer.parseInt(joinPort.getText()));
            connectionSettings.setIpAddress(joinHostIP.getText());
            connectionSettings.setRedNetworkPlayer(false);
            connectionSettings.setBlackNetworkPlayer(true);
            connector = new Connector(Connector.CLIENT, checkerBoard);
            connectionSettings.setConnector(connector);
            connector.connect();
            dispose();
        }
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(checkerBoard,
                message,
                "Error",
                JOptionPane.WARNING_MESSAGE);
    }

    private boolean validInput() {
        if (hostButton.isSelected()) {
            String port = hostPort.getText();
            String userName = hostUserName.getText();
            return validateFields(userName, port);
        } else {
            String port = joinPort.getText();
            String userName = joinUserName.getText();
            String hostIP = joinHostIP.getText();
            return validateFields(userName, hostIP, port);
        }
    }

    private boolean validateFields(String userName, String hostIP, String port) {
        boolean isValid;
        if (!StringUtils.isEmpty(hostIP)) {
            isValid = validateFields(userName, port);
        } else {
            isValid = false;
            showWarning("You must enter a host IP address");
        }
        return isValid;
    }

    private boolean validateFields(String userName, String port) {
        if (!StringUtils.isEmpty(port)) {
            if (!StringUtils.isEmpty(userName)) {
                try {
                    int num = Integer.parseInt(port);
                    if (num < 1 || num > 10000) showWarning("Invalid port number. Must be between 1-10000");
                    else return true;
                } catch (NumberFormatException e) {
                    showWarning("Invalid port number. Must be between 1-10000");
                }
            } else {
                showWarning("You must enter a username");
            }
        } else {
            showWarning("You must enter a port number");
        }
        return false;
    }

    private void startGame() {
        if (hostWait != null && connectWait.getText().equals("Waiting...")) {
            hostWait = null;
            checkerBoard.networkStartGame();
            dispose();
        }
    }

    private void cancelHostWait() {
        hostWait.interrupt();
        hostWait = null;
        connectionSettings.getConnector().destroyConnection();
        connectionSettings.setConnector(null);
        joinButton.setEnabled(true);
        hostButton.setEnabled(true);
        setHostEnabled(true);
        connectWait.setText("Connect / Wait");
        connectWait.setEnabled(true);
    }

    private class ConnectionWaiter extends Thread {
        private CheckerBoard checkerBoard;

        public ConnectionWaiter(CheckerBoard checkerBoard) {
            this.checkerBoard = checkerBoard;
        }

        public void run() {
            Connector connector = new Connector(Connector.HOST, checkerBoard);
            connectionSettings.setConnector(connector);
            connector.connect();
            startGame();
        }
    }
}

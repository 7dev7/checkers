package com.dev.connection;

import com.dev.ui.CheckerBoard;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connector {
    public static final int HOST = 0;
    public static final int CLIENT = 1;

    private ObjectOutputStream out;

    private Socket socket;
    private CheckerBoard checkerBoard;
    private ConnectionSettings connectionSettings;
    private ServerSocket serverSocket;
    private int player;

    public Connector(int player, CheckerBoard checkerBoard) {
        this.checkerBoard = checkerBoard;
        connectionSettings = checkerBoard.getConnectionSettings();
        this.player = player;
    }

    public void connect() {
        try {
            if (player == HOST) {
                serverSocket = new ServerSocket(connectionSettings.getPort());
                serverSocket.setSoTimeout(0);
                socket = serverSocket.accept();
            } else {
                socket = new Socket(connectionSettings.getIpAddress(), connectionSettings.getPort());
            }
            out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            InputListener inputThread = new InputListener(in);
            inputThread.start();

            ConnectionPacket.Builder builder = ConnectionPacket.newBuilder()
                    .setUserName(connectionSettings.getLocalUsername())
                    .setPacketStatus(ConnectionPacket.PacketStatus.SETUP);

            if (player == HOST) {
                builder.setGameId(connectionSettings.getGameID());
                setup(builder.build());
            } else {
                builder.setGameId(0);
                setup(builder.build());
            }
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(checkerBoard,
                    "Unknown host, please check your spelling",
                    "Unknown Host",
                    JOptionPane.WARNING_MESSAGE);
            connectionSettings.cleanup();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(checkerBoard,
                    "An error occured while connecting to the host",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            connectionSettings.cleanup();
        }
    }

    private synchronized void handleInput(ConnectionPacket packet) {
        if (connectionSettings.getGameID() != packet.getGameID() && connectionSettings.getGameID() != -1) {
            System.err.println("Invalid Game ID, Continue playing at your own risk");
        }

        switch (packet.getPacketStatus()) {
            case MOVE:
                doMove(packet);
                break;
            case ERROR:
                doError(packet);
                break;
            case SETUP:
                doSetup(packet);
                break;
            case END_TURN:
                doEndTurn();
                break;
            default:
                doError(packet);
        }
    }

    private void doEndTurn() {
        checkerBoard.networkDoEndTurn();
    }

    private void doSetup(ConnectionPacket packet) {
        connectionSettings.setRemoteUsername(packet.getUserName());
        int id = packet.getGameID();
        if (id != 0) {
            connectionSettings.setGameID(id);
        }
    }

    private void doMove(ConnectionPacket packet) {
        checkerBoard.networkDoMove(packet.getFromRow(), packet.getFromCol(), packet.getToRow(), packet.getToCol());
    }

    private void doError(ConnectionPacket packet) {
        System.err.println("Error for packet: " + packet);
    }

    private void setup(ConnectionPacket packet) {
        try {
            out.writeObject(packet);
        } catch (IOException e) {
            System.err.println("Error for packet: " + packet);
        }
    }

    public void move(ConnectionPacket packet) {
        try {
            out.writeObject(packet);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void destroyConnection() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("IOException during closing serverSocket: " + e.getMessage());
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("IOException during socket: " + e.getMessage());
            }
        }
        connectionSettings.cleanup();
        connectionSettings.setConnector(null);
    }

    private class InputListener extends Thread {
        private ObjectInputStream in;

        InputListener(ObjectInputStream in) {
            this.in = in;
        }

        public void run() {
            try {
                Object obj;
                while ((obj = in.readObject()) != null) {
                    System.out.println(obj);
                    handleInput((ConnectionPacket) obj);
                }
            } catch (IOException e) {
                if (player == HOST) {
                    JOptionPane.showMessageDialog(checkerBoard,
                            "The client has been disconnected",
                            "Lost connection",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(checkerBoard,
                            "The host has been disconnected",
                            "Lost connection",
                            JOptionPane.WARNING_MESSAGE);
                }
                destroyConnection();
            } catch (ClassNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}

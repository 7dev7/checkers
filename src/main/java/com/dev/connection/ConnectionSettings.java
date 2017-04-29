package com.dev.connection;

public class ConnectionSettings {
    private String localUsername, remoteUsername;
    private boolean redNetworkPlayer, blackNetworkPlayer;
    private Connector connector;
    private String ipAddress;
    private int port;
    private int gameID;

    public ConnectionSettings() {
        redNetworkPlayer = false;
        blackNetworkPlayer = false;
    }

    public boolean isBlackNetworkPlayer() {
        return blackNetworkPlayer;
    }

    public void setBlackNetworkPlayer(boolean value) {
        blackNetworkPlayer = value;
    }

    public boolean isRedNetworkPlayer() {
        return redNetworkPlayer;
    }

    public void setRedNetworkPlayer(boolean value) {
        redNetworkPlayer = value;
    }

    public String getRemoteUsername() {
        return remoteUsername;
    }

    public void setRemoteUsername(String remoteUsername) {
        this.remoteUsername = remoteUsername;
    }

    public String getLocalUsername() {
        return localUsername;
    }

    public void setLocalUsername(String localUsername) {
        this.localUsername = localUsername;
    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void cleanup() {
        setLocalUsername(null);
        setRemoteUsername(null);
        setGameID(-1);
        setPort(-1);
        setRedNetworkPlayer(true);
        setBlackNetworkPlayer(false);
        setConnector(null);
    }

    @Override
    public String toString() {
        return "ConnectionSettings{" +
                "localUsername='" + localUsername + '\'' +
                ", remoteUsername='" + remoteUsername + '\'' +
                ", redNetworkPlayer=" + redNetworkPlayer +
                ", blackNetworkPlayer=" + blackNetworkPlayer +
                ", connector=" + connector +
                ", ipAddress='" + ipAddress + '\'' +
                ", port=" + port +
                ", gameID=" + gameID +
                '}';
    }
}

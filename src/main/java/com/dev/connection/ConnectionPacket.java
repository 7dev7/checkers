package com.dev.connection;

import java.io.Serializable;

public class ConnectionPacket implements Serializable {
    private PacketStatus packetStatus;
    private int gameID;
    private int fromCol, fromRow;
    private int toCol, toRow;
    private String userName;

    private ConnectionPacket() {
    }

    public static Builder newBuilder() {
        return new ConnectionPacket().new Builder();
    }

    public PacketStatus getPacketStatus() {
        return packetStatus;
    }

    public int getGameID() {
        return gameID;
    }

    public int getFromCol() {
        return fromCol;
    }

    public int getFromRow() {
        return fromRow;
    }

    public int getToCol() {
        return toCol;
    }

    public int getToRow() {
        return toRow;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "ConnectionPacket{" +
                "packetStatus=" + packetStatus +
                ", gameID=" + gameID +
                ", fromCol=" + fromCol +
                ", fromRow=" + fromRow +
                ", toCol=" + toCol +
                ", toRow=" + toRow +
                ", userName='" + userName + '\'' +
                '}';
    }

    public enum PacketStatus {
        MOVE,
        END_TURN,
        SETUP,
        ERROR
    }

    public class Builder {
        private Builder() {
        }

        public Builder setPacketStatus(PacketStatus packetStatus) {
            ConnectionPacket.this.packetStatus = packetStatus;
            return this;
        }

        public Builder setGameId(int gameId) {
            ConnectionPacket.this.gameID = gameId;
            return this;
        }

        public Builder setFromCol(int fromCol) {
            ConnectionPacket.this.fromCol = fromCol;
            return this;
        }

        public Builder setFromRow(int fromRow) {
            ConnectionPacket.this.fromRow = fromRow;
            return this;
        }

        public Builder setToCol(int toCol) {
            ConnectionPacket.this.toCol = toCol;
            return this;
        }

        public Builder setToRow(int toRow) {
            ConnectionPacket.this.toRow = toRow;
            return this;
        }

        public Builder setFromValues(int fromRow, int fromCol) {
            ConnectionPacket.this.fromRow = fromRow;
            ConnectionPacket.this.fromCol = fromCol;
            return this;
        }

        public Builder setToValues(int toRow, int toCol) {
            ConnectionPacket.this.toRow = toRow;
            ConnectionPacket.this.toCol = toCol;
            return this;
        }

        public Builder setUserName(String userName) {
            ConnectionPacket.this.userName = userName;
            return this;
        }

        public ConnectionPacket build() {
            return ConnectionPacket.this;
        }
    }
}

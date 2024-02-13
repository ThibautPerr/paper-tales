package com.example;

public class Result {
    private int playerId;
    private int playerPlace;
    private int playerPoint;
    private int playerGold;

    public Result(int playerId, int playerPoint, int playerGold) {
        this.playerId = playerId;
        this.playerPlace = 0;
        this.playerPoint = playerPoint;
        this.playerGold = playerGold;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getPlayerPlace() {
        return playerPlace;
    }

    public void setPlayerPlace(int playerPlace) {
        this.playerPlace = playerPlace;
    }

    public int getPlayerPoint() {
        return playerPoint;
    }

    public int getPlayerGold() {
        return playerGold;
    }
}

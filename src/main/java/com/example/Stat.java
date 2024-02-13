package com.example;

import java.util.ArrayList;
import java.util.List;

public class Stat {
    int playerId;
    List<Integer> playerPlaces;
    List<Integer> playerPoints;
    int gamesPlayed;

    public Stat(int playerId) {
        this.playerId = playerId;
        this.gamesPlayed = 0;
        this.playerPlaces = new ArrayList<Integer>();
        this.playerPoints = new ArrayList<Integer>();
    }

    public int getPlayerId() {
        return playerId;
    }

    public void addGame(int place, int points) {
        this.playerPlaces.add(place);
        this.playerPoints.add(points);
        this.gamesPlayed++;
    }

    public float averagePlace() {
        return (float) this.playerPlaces.stream().mapToInt(Integer::intValue).sum() / this.gamesPlayed;
    }

    public float averagePoints() {
        return (float) this.playerPoints.stream().mapToInt(Integer::intValue).sum() / this.gamesPlayed;
    }
}

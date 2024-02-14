package com.example;

import java.util.ArrayList;
import java.util.List;

public class Stat {
    int playerId;
    List<Integer> playerPlaces;
    List<Integer> playerPoints;

    public Stat(int playerId) {
        this.playerId = playerId;
        this.playerPlaces = new ArrayList<Integer>();
        this.playerPoints = new ArrayList<Integer>();
    }

    public int getPlayerId() {
        return playerId;
    }

    public void addGame(int place, int points) {
        this.playerPlaces.add(place);
        this.playerPoints.add(points);
    }

    public float averagePlace() {
        return (float) this.playerPlaces.stream().mapToInt(Integer::intValue).average().getAsDouble();
    }

    public float averagePoints() {
        return (float) this.playerPoints.stream().mapToInt(Integer::intValue).average().getAsDouble();
    }
}

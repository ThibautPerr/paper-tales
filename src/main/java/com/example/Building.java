package com.example;

import java.util.List;

public class Building {
    private int id;
    private String name;

    private int currentBuildingPhase;
    private List<BuildingPhase> buildingPhases;

    public Building(int id, String name, int currentBuildingPhase, List<BuildingPhase> buildingPhases) {
        this.id = id;
        this.name = name;
        this.currentBuildingPhase = currentBuildingPhase;
        this.buildingPhases = buildingPhases;
    }

    public Building(Building building) {
        this.id = building.id;
        this.name = building.name;
        this.currentBuildingPhase = building.currentBuildingPhase;
        this.buildingPhases = building.buildingPhases;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCurrentBuildingPhase() {
        return currentBuildingPhase;
    }

    public void setCurrentBuildingPhase(int currentBuildingPhase) {
        this.currentBuildingPhase = currentBuildingPhase;
    }

    public List<BuildingPhase> getBuildingPhases() {
        return buildingPhases;
    }

    public BuildingPhase getNextBuildingPhase() {
        if (currentBuildingPhase < buildingPhases.size()) {
            for (BuildingPhase buildingPhase : buildingPhases) {
                if (buildingPhase.getPhase() == currentBuildingPhase + 1) {
                    return buildingPhase;
                }
            }
        }
        return null;
    }

    public void printBuilding() {
        System.out.println("Building: " + name + " (id: " + id + ")");
        System.out.println("Current building phase: " + currentBuildingPhase);
        for (BuildingPhase buildingPhase : buildingPhases) {
            buildingPhase.printBuildingPhase();
        }
        System.out.println();
    }
}

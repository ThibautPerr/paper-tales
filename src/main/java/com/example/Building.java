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
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getCurrentBuildingPhase() {
        return this.currentBuildingPhase;
    }

    public void setCurrentBuildingPhase(int currentBuildingPhase) {
        this.currentBuildingPhase = currentBuildingPhase;
    }

    public BuildingPhase getBuildingPhase(int phase) {
        for (BuildingPhase buildingPhase : this.buildingPhases) {
            if (buildingPhase.getPhase() == phase) {
                return buildingPhase;
            }
        }
        return null;
    }

    public List<BuildingPhase> getBuildingPhases() {
        return this.buildingPhases;
    }

    public BuildingPhase getNextBuildingPhase() {
        if (this.currentBuildingPhase < this.buildingPhases.size()) {
            for (BuildingPhase buildingPhase : this.buildingPhases) {
                if (buildingPhase.getPhase() == this.currentBuildingPhase + 1) {
                    return buildingPhase;
                }
            }
        }
        return null;
    }

    public void printBuilding() {
        System.out.println("Building: " + this.name + " (id: " + id + ")");
        System.out.println("Current building phase: " + this.currentBuildingPhase);
        for (BuildingPhase buildingPhase : this.buildingPhases) {
            buildingPhase.printBuildingPhase();
        }
        System.out.println();
    }
}

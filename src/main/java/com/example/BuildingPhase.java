package com.example;

import java.util.List;

public class BuildingPhase {
    private int phase;
    private List<Resource> requirements;
    private List<Resource> optionnalRequirements;
    // private Effect bonus;
    private int point;

    public BuildingPhase(int phase, List<Resource> requirements, List<Resource> optionnalRequirements, int point) {
        this.phase = phase;
        this.requirements = requirements;
        this.optionnalRequirements = optionnalRequirements;
        this.point = point;
    }

    public int getPhase() {
        return phase;
    }

    public List<Resource> getRequirements() {
        return requirements;
    }

    public List<Resource> getOptionnalRequirements() {
        return optionnalRequirements;
    }

    public int getPoint() {
        return point;
    }

    public void printBuildingPhase() {
        System.out.println("Building phase: " + phase);
        System.out.print("Requirements: ");
        for (Resource resource : requirements) {
            resource.printResource();
            System.out.print(", ");
        }
        System.out.print("\nOptionnal requirements: ");
        if (optionnalRequirements != null)
            for (Resource resource : optionnalRequirements)
                resource.printResource();
        else
            System.out.print("null");
        System.out.println("\nPoint: " + point);
    }
}

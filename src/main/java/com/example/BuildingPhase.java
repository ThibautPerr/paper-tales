package com.example;

import java.util.List;

import com.example.effects.Effect;

public class BuildingPhase {
    private int phase;
    private List<Resource> requirements;
    private List<Resource> optionnalRequirements;
    private Effect effect;
    private int point;

    public BuildingPhase(int phase, List<Resource> requirements, List<Resource> optionnalRequirements, Effect effect, int point) {
        this.phase = phase;
        this.requirements = requirements;
        this.optionnalRequirements = optionnalRequirements;
        this.effect = effect;
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

    public Effect getEffect() {
        return effect;
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
        effect.printEffect();
        System.out.println("\nPoint: " + point);
    }
}

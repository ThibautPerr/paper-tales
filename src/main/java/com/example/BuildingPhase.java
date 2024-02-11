package com.example;

import java.util.List;

import com.example.effects.Effect;

public class BuildingPhase {
    private int phase;
    private List<Resource> requirements;
    private List<Resource> optionnalRequirements;
    private List<Effect> effects;
    private int point;

    public BuildingPhase(int phase, List<Resource> requirements, List<Resource> optionnalRequirements,
            List<Effect> effects, int point) {
        this.phase = phase;
        this.requirements = requirements;
        this.optionnalRequirements = optionnalRequirements;
        this.effects = effects;
        this.point = point;
    }

    public int getPhase() {
        return this.phase;
    }

    public List<Resource> getRequirements() {
        return this.requirements;
    }

    public List<Resource> getOptionnalRequirements() {
        return this.optionnalRequirements;
    }

    public List<Effect> getEffects() {
        return this.effects;
    }

    public int getPoint() {
        return this.point;
    }

    public void printBuildingPhase() {
        System.out.println("Building phase: " + this.phase);
        System.out.print("Requirements: ");
        for (Resource resource : this.requirements) {
            resource.printResource();
            System.out.print(", ");
        }
        System.out.print("\nOptionnal requirements: ");
        if (this.optionnalRequirements != null)
            for (Resource resource : this.optionnalRequirements)
                resource.printResource();
        else
            System.out.print("null");
        for (Effect effect : this.effects)
            effect.printEffect();
        System.out.println("\nPoint: " + point);
    }
}

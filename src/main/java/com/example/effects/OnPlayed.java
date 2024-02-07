package com.example.effects;

import java.util.List;

import com.example.Player;
import com.example.Resource;

public class OnPlayed extends Effect {
    private Resource resource;
    private List<Resource> resources;

    public OnPlayed(String function) {
        super(function);
    }

    public OnPlayed(String function, Resource resource) {
        super(function);
        this.resource = resource;
    }

    public OnPlayed(String function, List<Resource> resources) {
        super(function);
        this.resources = resources;
    }

    public String getFunction() {
        return super.getFunction();
    }

    public void addResource(Player player) {
        super.addResource(player, this.resource);
    }

    public void addResources(Player player) {
        super.addResources(player, this.resources);
    }

    @Override
    public void playEffect(Player player) {
        switch (this.getFunction()) {
            case "addResource":
                addResource(player);
                break;
            case "addResources":
                addResources(player);
                break;
            default:
                System.out.println("OnPlayed playEffect : unknown function " + this.getFunction());
                break;
        }
    }

    @Override
    public void printEffect() {
        System.out.println("\nEffect: type " + getClass() + ", function " + getFunction());
        if (this.resource != null)
            this.resource.printResource();
        if (this.resources != null)
            for (Resource resource : this.resources)
                resource.printResource();
        System.out.print(", ");
    }

}

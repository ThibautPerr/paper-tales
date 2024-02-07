package com.example.effects;

import java.util.List;

import com.example.Player;
import com.example.Resource;

public class OnPhase4 extends Effect {
    private Resource resource;
    private List<Resource> resources;

    private Resource addResource;
    private Resource perResource;

    public OnPhase4(String function) {
        super(function);
    }

    public OnPhase4(String function, Resource resource) {
        super(function);
        this.resource = resource;
    }

    public OnPhase4(String function, List<Resource> resources) {
        super(function);
        this.resources = resources;
    }

    public OnPhase4(String function, Resource addResource, Resource perResource) {
        super(function);
        this.addResource = addResource;
        this.perResource = perResource;
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

    public void addResourcePerResource(Player player) {
        super.addResourcePerResource(player, this.addResource, this.perResource);
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
            case "addResourcePerResource":
                addResourcePerResource(player);
                break;
            default:
                System.out.println("OnPhase4 playEffect : unknown function " + this.getFunction());
                break;
        }
    }

    @Override
    public void printEffect() {
        System.out.println("\nEffect: type " + getClass().getSimpleName() + ", function " + getFunction());
        if (this.resource != null)
            this.resource.printResource();
        else if (this.resources != null)
            for (Resource resource : this.resources)
                resource.printResource();
        else if (this.addResource != null && this.perResource != null) {
            this.addResource.printResource();
            System.out.print(", ");
            this.perResource.printResource();
        }
    }
}

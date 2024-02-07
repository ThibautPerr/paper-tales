package com.example.effects;

import java.util.List;

import com.example.Player;
import com.example.Resource;

public class OnDeath extends Effect {
    private Resource resource;
    private List<Resource> resources;

    OnDeath(String function) {
        super(function);
    }

    OnDeath(String function, Resource resource) {
        super(function);
        this.resource = resource;
    }

    OnDeath(String function, List<Resource> resources) {
        super(function);
        this.resources = resources;
    }

    public String getFunction() {
        return super.getFunction();
    }

    void removeResource(Player player) {
        super.removeResource(player, this.resource);
    }

    void removeResources(Player player) {
        super.removeResources(player, this.resources);
    }

    @Override
    public void playEffect(Player player) {
        switch (this.getFunction()) {
            case "removeResource":
                removeResource(player);
                break;
            case "removeResources":
                removeResources(player);
                break;
            default:
                System.out.println("Error: function " + this.getFunction() + " not found");
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
    }
}

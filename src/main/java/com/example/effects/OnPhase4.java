package com.example.effects;

import com.example.Player;
import com.example.Resource;

public class OnPhase4 extends Effect {
    private Resource resource;
    private Resource[] resources;

    private Resource.ResourceType addType;
    private int addQuantity;
    private Resource.ResourceType perType;
    private int perQuantity;

    OnPhase4(String function) {
        super(function);
    }

    OnPhase4(String function, Resource resource) {
        super(function);
        this.resource = resource;
    }

    OnPhase4(String function, Resource[] resources) {
        super(function);
        this.resources = resources;
    }

    OnPhase4(String function, Resource.ResourceType addType, int addQuantity, Resource.ResourceType perType, int perQuantity) {
        super(function);
        this.addType = addType;
        this.addQuantity = addQuantity;
        this.perType = perType;
        this.perQuantity = perQuantity;
    }

    public String getFunction() {
        return super.getFunction();
    }

    void addResource(Player player) {
        super.addResource(player, this.resource);
    }

    void addResources(Player player) {
        super.addResources(player, this.resources);
    }

    void addResourcePerResource(Player player) {
        super.addResourcePerResource(player, this.addType, this.addQuantity, this.perType, this.perQuantity);
    }
}

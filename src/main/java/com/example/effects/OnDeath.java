package com.example.effects;

import com.example.Player;
import com.example.Resource;

public class OnDeath extends Effect {
    private Resource resource;
    private Resource[] resources;

    OnDeath(String function) {
        super(function);
    }

    OnDeath(String function, Resource resource) {
        super(function);
        this.resource = resource;
    }

    OnDeath(String function, Resource[] resources) {
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
}

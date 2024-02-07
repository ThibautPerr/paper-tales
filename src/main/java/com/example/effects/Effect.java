package com.example.effects;

import com.example.Player;
import com.example.Resource;

public abstract class Effect {
    private String function;

    Effect(String function) {
        this.function = function;
    }

    public String getFunction() {
        return this.function;
    }

    void addResource(Player player, Resource resource) {
        player.addResource(resource);
    }

    void addResources(Player player, Resource[] resources) {
        for (Resource resource : resources) {
            player.addResource(resource);
        }
    }

    void addResourcePerResource(Player player, Resource.ResourceType addType, int addQuantity, Resource.ResourceType perType, int perQuantity) {
        player.addResourcePerResource(addType, addQuantity, perType, perQuantity);
    }

    void removeResource(Player player, Resource resource) {
        player.removeResource(resource);
    }

    void removeResources(Player player, Resource[] resources) {
        for (Resource resource : resources) {
            player.removeResource(resource);
        }
    }

    void addPoint(Player player, int point) {
        player.addPoint(point);
    }

    void addPointPerResource(Player player, int point, Resource.ResourceType type, int quantity) {
        player.addPointPerResource(point, type, quantity);
    }

    void addPointPerUnitWithMinAttack(Player player, int point, int minAttack) {
        player.addPointPerUnitWithMinAttack(point, minAttack);
    }

    void addWarPointPerResource(Player player, int point, Resource.ResourceType type, int quantity) {
        player.addWarPointPerResource(point, type, quantity);
    }
}

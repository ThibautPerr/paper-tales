package com.example.effects;

import java.util.List;

import com.example.Player;
import com.example.Resource;

public abstract class Effect {
    private String function;

    protected Effect(String function) {
        this.function = function;
    }

    protected String getFunction() {
        return this.function;
    }

    protected void addResource(Player player, Resource resource) {
        player.addResource(resource);
    }

    protected void addResources(Player player, List<Resource> resources) {
        for (Resource resource : resources) {
            player.addResource(resource);
        }
    }

    protected void addResourcePerResource(Player player, Resource addResource, Resource perResource) {
        player.addResourcePerResource(addResource, perResource);
    }

    protected void removeResource(Player player, Resource resource) {
        player.removeResource(resource);
    }

    protected void removeResources(Player player, List<Resource> resources) {
        for (Resource resource : resources) {
            player.removeResource(resource);
        }
    }

    protected void addPoint(Player player, int point) {
        player.addPoint(point);
    }

    protected void addPointPerResource(Player player, int point, Resource resource) {
        player.addPointPerResource(point, resource);
    }

    protected void addPointPerUnitWithMinAttack(Player player, int point, int minAttack) {
        player.addPointPerUnitWithMinAttack(point, minAttack);
    }

    protected void addWarPointPerResource(Player player, int point, Resource resource) {
        player.addWarPointPerResource(point, resource);
    }

    protected abstract void playEffect(Player player);

    public abstract void printEffect();
}

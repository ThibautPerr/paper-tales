package com.example.effects;

import com.example.Player;
import com.example.Resource;

public class OnDeath extends Effect {
    private int moon;
    private Resource resource;

    public OnDeath(String function, int idCard) {
        super(function, idCard);
    }

    public OnDeath(String function, int idCard, Resource resource) {
        super(function, idCard);
        this.resource = resource;
    }

    public OnDeath(String function, int idCard, int moon, Resource resource) {
        super(function, idCard);
        this.moon = moon;
        this.resource = resource;
    }

    public OnDeath(OnDeath effect) {
        super(effect.getFunction(), effect.getIdCard());
        this.moon = effect.moon;
        this.resource = effect.resource;
    }

    public String getFunction() {
        return super.getFunction();
    }

    private void removeResource(Player player) {
        super.removeResource(player, this.resource);
    }

    public void removeResourcePerMoon(Player player) {
        super.removeResourcePerMoon(player, this.getIdCard(), this.resource, this.moon);
    }

    private void removeResourcePerAtLeastMoon(Player player) {
        super.removeResourcePerAtLeastMoon(player, this.getIdCard(), this.resource, this.moon);
    }

    @Override
    public void playEffect(Player player) {
        switch (this.getFunction()) {
            case "removeResource":
                removeResource(player);
                break;
            case "removeResourcePerMoon":
                removeResourcePerMoon(player);
                break;
            case "removeResourcePerAtLeastMoon":
                removeResourcePerAtLeastMoon(player);
                break;
            default:
                System.out.println("Error: function " + this.getFunction() + " not found");
                break;
        }
    }

    @Override
    public void printEffect() {
        System.out.println("Effect: type " + getClass().getSimpleName() + ", function " + getFunction());
        if (this.resource != null)
            this.resource.printResource();
        if (this.moon != 0)
            System.out.println("Moon: " + this.moon);
        System.out.println();
    }

    @Override
    public OnDeath deepCopy() {
        return new OnDeath(this);
    }
}

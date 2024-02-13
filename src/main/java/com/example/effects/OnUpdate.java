package com.example.effects;

import com.example.Player;
import com.example.Resource;

public class OnUpdate extends Effect {
    private int moon;
    private Resource resource;

    public OnUpdate(String function, int idCard) {
        super(function, idCard);
    }

    public OnUpdate(String function, int idCard, Resource resource) {
        super(function, idCard);
        this.resource = resource;
    }

    public OnUpdate(String function, int idCard, int moon, Resource resource) {
        super(function, idCard);
        this.moon = moon;
        this.resource = resource;
    }

    public OnUpdate(OnUpdate effect) {
        super(effect.getFunction(), effect.getIdCard());
        this.moon = effect.moon;
        this.resource = effect.resource;
    }

    public String getFunction() {
        return super.getFunction();
    }

    public int getMoon() {
        return this.moon;
    }

    public void updateResourcePerMoon(Player player, int moonToAdd) {
        super.updateResourcePerMoon(player, this.resource, this.moon, moon);
    }

    public void addResourceIfFrontUnit(Player player) {
        super.addResourceIfFrontUnit(player, this.getIdCard(), this.resource);
    }

    public void removeResourceIfFrontUnit(Player player) {
        super.addResourceIfFrontUnit(player, this.getIdCard(), this.resource);
    }

    public void addResourcePerAtLeastMoon(Player player) {
        super.addResourcePerAtLeastMoon(player, this.getIdCard(), this.moon, this.resource, true);
    }

    @Override
    public void playEffect(Player player) {
        switch (this.getFunction()) {
            default:
                System.out.println("OnUpdated playEffect : unknown function " + this.getFunction());
                break;
        }
    }

    @Override
    public void printEffect() {
        System.out.println("Effect: type " + getClass().getSimpleName() + ", function " + getFunction());
        if (this.moon != 0)
            System.out.println("moon " + this.moon);
        if (this.resource != null)
            this.resource.printResource();
        System.out.println();
    }

    @Override
    public OnUpdate deepCopy() {
        return new OnUpdate(this);
    }
}

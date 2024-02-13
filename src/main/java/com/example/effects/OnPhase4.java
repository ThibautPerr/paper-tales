package com.example.effects;

import com.example.Player;
import com.example.Resource;
import com.example.Resource.ResourceType;

public class OnPhase4 extends Effect {
    private Resource resource;
    private int moon;

    private Resource addResource;
    private Resource perResource;

    public OnPhase4(String function, int idCard) {
        super(function, idCard);
    }

    public OnPhase4(String function, int idCard, Resource resource) {
        super(function, idCard);
        this.resource = resource;
    }

    public OnPhase4(String function, int idCard, int moon, Resource resource) {
        super(function, idCard);
        this.resource = resource;
        this.moon = moon;
    }

    public OnPhase4(String function, int idCard, Resource addResource, Resource perResource) {
        super(function, idCard);
        this.addResource = addResource;
        this.perResource = perResource;
    }

    public OnPhase4(OnPhase4 effect) {
        super(effect.getFunction(), effect.getIdCard());
        this.resource = effect.resource;
        this.moon = effect.moon;
        this.addResource = effect.addResource;
        this.perResource = effect.perResource;
    }

    public String getFunction() {
        return super.getFunction();
    }

    public Resource getResource() {
        return this.resource;
    }

    public Resource getAddResource() {
        return this.addResource;
    }

    private void addResource(Player player) {
        if (this.resource.getResourceType() == ResourceType.GOLD)
            super.addGoldInPhase4(player, this.resource);
        else
            super.addResource(player, this.resource);
    }

    private void addResourcePerResource(Player player) {
        if (this.addResource.getResourceType() == ResourceType.GOLD)
            super.addGoldPerResourceInPhase4(player, this.addResource, this.perResource);
        else
            super.addResourcePerResource(player, this.addResource, this.perResource);
    }

    private void addResourcePerAtLeastMoon(Player player) {
        if (this.resource.getResourceType() == ResourceType.GOLD)
            super.addGoldPerAtLeastMoon(player, this.getIdCard(), this.moon, this.resource);
        else
            super.addResourcePerAtLeastMoon(player, this.getIdCard(), this.moon, this.resource, false);
    }

    @Override
    public void playEffect(Player player) {
        switch (this.getFunction()) {
            case "addResource":
                addResource(player);
                break;
            case "addResourcePerResource":
                addResourcePerResource(player);
                break;
            case "addResourcePerAtLeastMoon":
                addResourcePerAtLeastMoon(player);
                break;
            default:
                System.out.println("OnPhase4 playEffect : unknown function " + this.getFunction());
                break;
        }
    }

    @Override
    public void printEffect() {
        System.out.println("Effect: type " + getClass().getSimpleName() + ", function " + getFunction());
        if (this.moon != 0)
            System.out.print("Moon: " + this.moon);
        if (this.resource != null)
            this.resource.printResource();
        else if (this.addResource != null && this.perResource != null) {
            this.addResource.printResource();
            System.out.print(", ");
            this.perResource.printResource();
        }
        System.out.println();
    }

    @Override
    public OnPhase4 deepCopy() {
        return new OnPhase4(this);
    }
}

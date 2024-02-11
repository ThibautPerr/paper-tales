package com.example.effects;

import java.util.List;

import com.example.Player;
import com.example.Resource;

public class OnPhase5 extends Effect {
    private int gold;
    private Resource resource;

    public OnPhase5(String function, int idCard) {
        super(function, idCard);
    }

    public OnPhase5(String function, int idCard, int gold) {
        super(function, idCard);
        this.gold = gold;
    }

    public OnPhase5(String function, int idCard, int gold, Resource resource) {
        super(function, idCard);
        this.gold = gold;
        this.resource = resource;
    }

    public OnPhase5(OnPhase5 effect) {
        super(effect.getFunction(), effect.getIdCard());
        this.gold = effect.gold;
        this.resource = effect.resource;
    }

    public String getFunction() {
        return super.getFunction();
    }

    public void payGoldInsteadOfResource(Player player, int nb) {
        super.payGoldInsteadOfResource(player, this.gold, this.resource, nb);
    }

    public void payGoldInsteadOfResources(Player player, List<Resource> resources) {
        for (Resource resource : resources) {
            super.payGoldInsteadOfResource(player, this.gold, resource, resource.getQuantity());
        }
    }

    @Override
    public void playEffect(Player player) {
        switch (this.getFunction()) {
            case "ignoreBuildingCost":
                ignoreBuildingCost(player);
                break;
            default:
                System.out.println("OnPhase3 playEffect : unknown function " + this.getFunction());
                break;
        }
    }

    @Override
    public void printEffect() {
        System.out.println("\nEffect: type " + getClass() + ", function " + getFunction());
        if (this.gold != 0)
            System.out.println("Gold: " + this.gold);
        if (this.resource != null)
            this.resource.printResource();
    }

    @Override
    public OnPhase5 deepCopy() {
        return new OnPhase5(this);
    }
}

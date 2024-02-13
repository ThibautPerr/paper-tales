package com.example.effects;

import com.example.Player;
import com.example.Resource;

public class OnPhase2 extends Effect {
    private int moon;
    private Resource resource;

    public OnPhase2(String function, int idCard) {
        super(function, idCard);
    }

    public OnPhase2(String function, int idCard, int moon) {
        super(function, idCard);
        this.moon = moon;
    }

    public OnPhase2(String function, int idCard, int moon, Resource resource) {
        super(function, idCard);
        this.moon = moon;
        this.resource = resource;
    }

    public OnPhase2(OnPhase2 effect) {
        super(effect.getFunction(), effect.getIdCard());
        this.moon = effect.moon;
        this.resource = effect.resource;
    }

    public String getFunction() {
        return super.getFunction();
    }

    public void addMoonPerResourceOnAnotherUnit(Player player, int idCard) {
        super.addMoonPerResourceOnAnotherUnit(player, idCard, this.resource, this.moon);
    }

    public void cannotFightIfAtLeastMoon(Player player) {
        super.cannotFightIfAtLeastMoon(player, getIdCard(), this.moon);
    }

    @Override
    public void playEffect(Player player) {
        switch (this.getFunction()) {
            case "cannotFightIfAtLeastMoon":
                cannotFightIfAtLeastMoon(player);
                break;
            default:
                System.out.println("OnPhase2 playEffect : unknown function " + this.getFunction());
                break;
        }
    }

    @Override
    public void printEffect() {
        System.out.println("Effect: type " + getClass().getSimpleName() + ", function " + getFunction());
        if (this.resource != null)
            this.resource.printResource();
        if (this.moon != 0)
            System.out.print(", moon " + this.moon);
        System.out.println();
    }

    @Override
    public OnPhase2 deepCopy() {
        return new OnPhase2(this);
    }
}

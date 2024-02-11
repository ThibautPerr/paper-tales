package com.example.effects;

import com.example.Card;
import com.example.Player;

public class OnPhase6 extends Effect {
    int moon;
    int point;

    public OnPhase6(String function, int idCard) {
        super(function, idCard);
    }

    public OnPhase6(String function, int idCard, int moon) {
        super(function, idCard);
        this.moon = moon;
    }

    public OnPhase6(String function, int idCard, int moon, int point) {
        super(function, idCard);
        this.moon = moon;
        this.point = point;
    }

    public OnPhase6(OnPhase6 effect) {
        super(effect.getFunction(), effect.getIdCard());
        this.moon = effect.moon;
        this.point = effect.point;
    }

    public String getFunction() {
        return super.getFunction();
    }

    private void avoidDeath(Player player) {
        super.avoidDeath(player, getIdCard());
    }

    private void avoidDeathIfLessThanMoon(Player player) {
        super.avoidDeathIfLessThanMoon(player, getIdCard(), this.moon);
    }

    public void avoidDeathForOneUnitWithMoon(Player player, Card card) {
        super.avoidDeathForOneUnitWithMoon(player, card);
    }

    private void addPointPerMoonOnDyingUnit(Player player) {
        super.addPointPerMoonOnDyingUnit(player, this.moon, this.point);
    }

    @Override
    public void playEffect(Player player) {
        switch (this.getFunction()) {
            case "avoidDeath":
                avoidDeath(player);
                break;
            case "avoidDeathIfLessThanMoon":
                avoidDeathIfLessThanMoon(player);
                break;
            case "addPointPerMoonOnDyingUnit":
                addPointPerMoonOnDyingUnit(player);
                break;
            default:
                System.out.println("OnPhase6 playEffect : unknown function " + this.getFunction());
                break;
        }
    }

    @Override
    public void printEffect() {
        System.out.println("\nEffect: type " + getClass().getSimpleName() + ", function " + getFunction());
        if (this.moon != 0)
            System.out.println("Moon: " + this.moon);
        if (this.point != 0)
            System.out.println("Point: " + this.point);
    }

    @Override
    public OnPhase6 deepCopy() {
        return new OnPhase6(this);
    }
}

package com.example.effects;

import com.example.Card;
import com.example.Player;
import com.example.Resource;

public class OnFlip extends Effect {
    private int moon;
    private int point;
    private int phase;
    private Resource resource;

    public OnFlip(String function, int idCard) {
        super(function, idCard);
    }

    public OnFlip(String function, int idCard, Resource resource) {
        super(function, idCard);
        this.resource = resource;
    }

    public OnFlip(String function, int idCard, int moon) {
        super(function, idCard);
        this.moon = moon;
    }

    public OnFlip(int idCard, int phase, String function) {
        super(function, idCard);
        this.phase = phase;
    }

    public OnFlip(String function, int idCard, int point, int phase) {
        super(function, idCard);
        this.phase = phase;
        this.point = point;
    }

    public OnFlip(OnFlip effect) {
        super(effect.getFunction(), effect.getIdCard());
        this.moon = effect.moon;
        this.point = effect.point;
        this.phase = effect.phase;
        this.resource = effect.resource;
    }

    public String getFunction() {
        return super.getFunction();
    }

    private void addResource(Player player) {
        super.addResource(player, this.resource);
    }

    private void addMoon(Player player) {
        super.addMoon(player, getIdCard(), this.moon);
    }

    public void addMoonOnAnotherUnit(Player player, int idCard) {
        super.addMoonOnAnotherUnit(player, idCard, this.moon);
    }

    private void addMoonToAllOtherUnits(Player player) {
        super.addMoonToAllOtherUnits(player, this.moon, getIdCard());
    }

    private void addPointPerBuildingInPhase(Player player) {
        super.addPointPerBuildingInPhase(player, this.point, this.phase);
    }

    private void upgradeBuildingsInPhase(Player player) {
        super.upgradeBuildingsInPhase(player, phase);
    }

    public void changeform(Player player, Card card) {
        super.changeform(player, this.getIdCard(), card);
    }

    @Override
    public void playEffect(Player player) {
        switch (this.getFunction()) {
            case "addResource":
                addResource(player);
                break;
            case "addMoon":
                addMoon(player);
                break;
            case "addMoonToAllOtherUnits":
                addMoonToAllOtherUnits(player);
                break;
            case "addPointPerBuildingInPhase":
                addPointPerBuildingInPhase(player);
                break;
            case "upgradeBuildingsInPhase":
                upgradeBuildingsInPhase(player);
                break;
            default:
                System.out.println("OnFlip playEffect : unknown function " + this.getFunction());
                break;
        }
    }

    @Override
    public void printEffect() {
        System.out.println("Effect: type " + getClass().getSimpleName() + ", function " + getFunction());
        if (this.moon != 0)
            System.out.println("Moon: " + this.moon);
        if (this.point != 0)
            System.out.println("Point: " + this.point);
        if (this.phase != 0)
            System.out.println("Phase: " + this.phase);
        if (this.resource != null)
            this.resource.printResource();
        System.out.println();
    }

    @Override
    public OnFlip deepCopy() {
        return new OnFlip(this);
    }
}

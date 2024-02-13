package com.example.effects;

import com.example.Player;
import com.example.Resource;

public class OnPhase3 extends Effect {
    private int point;
    private Resource resource;
    private int minAttack;
    private int moon;

    public OnPhase3(String function, int idCard) {
        super(function, idCard);
    }

    public OnPhase3(String function, int idCard, int point) {
        super(function, idCard);
        this.point = point;
    }

    public OnPhase3(String function, int idCard, int point, Resource resource) {
        super(function, idCard);
        this.point = point;
        this.resource = resource;
    }

    public OnPhase3(String function, int idCard, int point, int minAttack) {
        super(function, idCard);
        this.point = point;
        this.minAttack = minAttack;
    }

    public OnPhase3(int idCard, int point, int moon, String function) {
        super(function, idCard);
        this.point = point;
        this.moon = moon;
    }

    public OnPhase3(OnPhase3 effect) {
        super(effect.getFunction(), effect.getIdCard());
        this.point = effect.point;
        this.resource = effect.resource;
        this.minAttack = effect.minAttack;
        this.moon = effect.moon;
    }

    public String getFunction() {
        return super.getFunction();
    }

    private void addPoint(Player player) {
        super.addPoint(player, this.point);
    }

    private void addPointPerResource(Player player) {
        super.addPointPerResource(player, this.point, this.resource);
    }

    private void addPointPerUnitWithMinAttack(Player player) {
        super.addPointPerUnitWithMinAttack(player, this.point, this.minAttack);
    }

    private void addWarpointPerMoon(Player player) {
        super.addWarpointPerMoon(player, this.getIdCard(), this.point, this.moon);
    }

    private void addWarpointPerMoonOnEveryUnit(Player player) {
        super.addWarpointPerMoonOnEveryUnit(player, point, moon);
    }

    private void addWarpointPerAtLeastMoon(Player player) {
        super.addWarpointPerAtLeastMoon(player, this.getIdCard(), this.point, this.moon);
    }

    private void addWarpointPerResource(Player player) {
        super.addWarpointPerResource(player, this.getIdCard(), this.point, this.resource);
    }

    private void addWarpointPerGoldAddedAtNextPhase4(Player player) {
        super.addWarpointPerGoldAddedAtNextPhase4(player, this.getIdCard(), this.point, this.resource);
    }

    private void addWarpointPerAtLeastResource(Player player) {
        super.addWarpointPerAtLeastResource(player, this.getIdCard(), this.point, this.resource);
    }

    private void canFightFromBehind(Player player) {
        super.canFightFromBehind(player, getIdCard());
    }

    @Override
    public void playEffect(Player player) {
        switch (this.getFunction()) {
            case "addPoint":
                addPoint(player);
                break;
            case "addPointPerResource":
                addPointPerResource(player);
                break;
            case "addWarpointPerMoon":
                addWarpointPerMoon(player);
                break;
            case "addWarpointPerMoonOnEveryUnit":
                addWarpointPerMoonOnEveryUnit(player);
                break;
            case "addWarpointPerAtLeastMoon":
                addWarpointPerAtLeastMoon(player);
                break;
            case "addWarpointPerResource":
                addWarpointPerResource(player);
                break;
            case "addWarpointPerGoldAddedAtNextPhase4":
                addWarpointPerGoldAddedAtNextPhase4(player);
                break;
            case "addWarpointPerAtLeastResource":
                addWarpointPerAtLeastResource(player);
                break;
            case "addPointPerUnitWithMinAttack":
                addPointPerUnitWithMinAttack(player);
                break;
            case "canFightFromBehind":
                canFightFromBehind(player);
                break;
            default:
                System.out.println("OnPhase3 playEffect : unknown function " + this.getFunction());
                break;
        }
    }

    @Override
    public void printEffect() {
        System.out.println("Effect: type " + getClass().getSimpleName() + ", function " + getFunction());
        if (this.point != 0)
            System.out.println("Point: " + this.point);
        if (this.resource != null)
            this.resource.printResource();
        if (this.minAttack != 0)
            System.out.println("MinAttack: " + this.minAttack);
        if (this.moon != 0)
            System.out.println("Moon: " + this.moon);
        System.out.println();
    }

    @Override
    public OnPhase3 deepCopy() {
        return new OnPhase3(this);
    }
}

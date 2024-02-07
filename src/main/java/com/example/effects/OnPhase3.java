package com.example.effects;

import com.example.Player;
import com.example.Resource;

public class OnPhase3 extends Effect {
    private int point;
    private Resource resource;
    private int minAttack;

    public OnPhase3(String function) {
        super(function);
    }

    public OnPhase3(String function, int point) {
        super(function);
        this.point = point;
    }

    public OnPhase3(String function, int point, Resource resource) {
        super(function);
        this.point = point;
        this.resource = resource;
    }

    public OnPhase3(String function, int point, int minAttack) {
        super(function);
        this.point = point;
        this.minAttack = minAttack;
    }

    public String getFunction() {
        return super.getFunction();
    }

    public int getPoint() {
        return this.point;
    }

    public void addPoint(Player player) {
        super.addPoint(player, this.point);
    }

    public void addPointPerResource(Player player) {
        super.addPointPerResource(player, this.point, this.resource);
    }

    public void addPointPerUnitWithMinAttack(Player player) {
        super.addPointPerUnitWithMinAttack(player, this.point, this.minAttack);
    }

    public void addWarPointPerResource(Player player) {
        super.addWarPointPerResource(player, this.point, this.resource);
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
            case "addWarPointPerResource":
                addWarPointPerResource(player);
                break;
            case "addPointPerUnitWithMinAttack":
                addPointPerUnitWithMinAttack(player);
                break;
            default:
                System.out.println("OnPhase3 playEffect : unknown function " + this.getFunction());
                break;
        }
    }

    @Override
    public void printEffect() {
        System.out.println("\nEffect: type " + getClass() + ", function " + getFunction());
        if (this.point != 0)
            System.out.println("Point: " + this.point);
        if (this.resource != null)
            this.resource.printResource();
        if (this.minAttack != 0)
            System.out.println("MinAttack: " + this.minAttack);
    }
}

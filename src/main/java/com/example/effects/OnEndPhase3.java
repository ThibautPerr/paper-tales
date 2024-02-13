package com.example.effects;

import com.example.Player;
import com.example.Resource;

public class OnEndPhase3 extends Effect {
    private int point;
    private Resource resource;

    public OnEndPhase3(String function, int idCard) {
        super(function, idCard);
    }

    public OnEndPhase3(String function, int idCard, int point) {
        super(function, idCard);
        this.point = point;
    }

    public OnEndPhase3(String function, int idCard, Resource resource) {
        super(function, idCard);
        this.resource = resource;
    }

    public OnEndPhase3(String function, int idCard, int point, Resource resource) {
        super(function, idCard);
        this.point = point;
        this.resource = resource;
    }

    public OnEndPhase3(OnEndPhase3 effect) {
        super(effect.getFunction(), effect.getIdCard());
        this.point = effect.point;
        this.resource = effect.resource;
    }

    public String getFunction() {
        return super.getFunction();
    }

    private void addPointPerWarWon(Player player) {
        super.addPointPerWarWon(player, this.point);
    }

    private void addResourcePerWarWon(Player player) {
        super.addResourcePerWarWon(player, this.resource);
    }

    private void addPointPerResourcePerWarWon(Player player) {
        super.addPointPerResourcePerWarWon(player, this.point, this.resource);
    }

    @Override
    public void playEffect(Player player) {
        switch (this.getFunction()) {
            case "addResourcePerWarWon":
                addResourcePerWarWon(player);
                break;
            case "addPointPerResourcePerWarWon":
                addPointPerResourcePerWarWon(player);
                break;
            case "addPointPerWarWon":
                addPointPerWarWon(player);
                break;
            default:
                System.out.println("OnEndPhase3 playEffect : unknown function " + this.getFunction());
                break;
        }
    }

    @Override
    public void printEffect() {
        System.out.println("Effect: type " + getClass().getSimpleName() + ", function " + getFunction());
        if (point != 0)
            System.out.println("Point: " + this.point);
        if (this.resource != null)
            this.resource.printResource();
        System.out.println();
    }

    @Override
    public OnEndPhase3 deepCopy() {
        return new OnEndPhase3(this);
    }

}

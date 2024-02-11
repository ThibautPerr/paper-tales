package com.example.effects;

import com.example.Player;
import com.example.Resource;

public class OnPhase6Death extends Effect {
    private int point;
    private int moon;
    private Resource resource;

    public OnPhase6Death(String function, int idCard) {
        super(function, idCard);
    }

    public OnPhase6Death(String function, int idCard, int point) {
        super(function, idCard);
        this.point = point;
    }

    public OnPhase6Death(String function, int idCard, int point, int moon) {
        super(function, idCard);
        this.point = point;
        this.moon = moon;
    }

    public OnPhase6Death(String function, int idCard, int moon, Resource resource) {
        super(function, idCard);
        this.moon = moon;
        this.resource = resource;
    }

    public OnPhase6Death(OnPhase6Death effect) {
        super(effect.getFunction(), effect.getIdCard());
        this.point = effect.point;
        this.moon = effect.moon;
        this.resource = effect.resource;
    }

    private void addPoint(Player player) {
        super.addPoint(player, this.point);
    }

    private void addResourcePerMoon(Player player) {
        super.addResourcePerMoon(player, this.resource, this.moon);
    }

    private void removePointIfOnlyXMoon(Player player) {
        super.removePointIfOnlyXMoon(player, getIdCard(), this.point, this.moon);
    }

    @Override
    public void playEffect(Player player) {
        switch (this.getFunction()) {
            case "addPoint":
                addPoint(player);
                break;
            case "addResourcePerMoon":
                addResourcePerMoon(player);
                break;
            case "removePointIfOnlyXMoon":
                removePointIfOnlyXMoon(player);
                break;
            default:
                System.out.println("Error: function " + this.getFunction() + " not found");
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
        if (this.moon != 0)
            System.out.println("Moon: " + this.moon);
    }

    @Override
    public OnPhase6Death deepCopy() {
        return new OnPhase6Death(this);
    }
}

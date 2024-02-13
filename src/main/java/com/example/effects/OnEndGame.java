package com.example.effects;

import com.example.Player;

public class OnEndGame extends Effect {
    private int point;
    private int moon;

    public OnEndGame(String function, int idCard) {
        super(function, idCard);
    }

    public OnEndGame(String function, int idCard, int point, int moon) {
        super(function, idCard);
        this.point = point;
        this.moon = moon;
    }

    public OnEndGame(OnEndGame effect) {
        super(effect.getFunction(), effect.getIdCard());
        this.point = effect.point;
        this.moon = effect.moon;
    }

    public String getFunction() {
        return super.getFunction();
    }

    private void addPointPerMoon(Player player) {
        super.addPointPerMoon(player, getIdCard(), this.point, this.moon);
    }

    @Override
    public void playEffect(Player player) {
        switch (this.getFunction()) {
            case "addPointPerMoon":
                addPointPerMoon(player);
                break;
            default:
                System.out.println("OnEndGame playEffect : unknown function " + this.getFunction());
                break;
        }
    }

    @Override
    public void printEffect() {
        System.out.println("Effect: type " + getClass().getSimpleName() + ", function " + getFunction());
        if (moon != 0)
            System.out.println("Moon: " + this.moon);
        if (point != 0)
            System.out.println("Point: " + this.point);
        System.out.println();
    }

    @Override
    public OnEndGame deepCopy() {
        return new OnEndGame(this);
    }
}

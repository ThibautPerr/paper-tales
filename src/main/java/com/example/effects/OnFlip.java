package com.example.effects;

import com.example.Player;

public class OnFlip extends Effect {
    OnFlip(String function) {
        super(function);
    }

    public String getFunction() {
        return super.getFunction();
    }

    @Override
    public void playEffect(Player player) {
        System.out.println("Effect: type " + getClass() + ", function " + getFunction());
    }

    @Override
    public void printEffect() {
        System.out.println("\nEffect: type " + getClass() + ", function " + getFunction());
    }
}

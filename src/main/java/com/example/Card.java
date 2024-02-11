package com.example;

import java.util.List;
import java.util.stream.Collectors;

import com.example.effects.Effect;

public class Card {
    private int id;
    private String name;
    private int cost;
    private int attack;
    private List<Effect> effects;
    private int moon;
    private boolean avoidDeath;
    private boolean canFight;
    private boolean canFightFromBehind;
    private boolean flipped;
    private boolean moved;

    public Card(int id, String name, int cost, int attack, List<Effect> effects) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.attack = attack;
        this.effects = effects;
        this.moon = 0;
        this.avoidDeath = false;
        this.canFight = true;
        this.canFightFromBehind = false;
        this.flipped = false;
        this.moved = false;
    }

    public Card(Card card) {
        this.id = card.id;
        this.name = card.name;
        this.cost = card.cost;
        this.attack = card.attack;
        this.effects = Effect.deepCopyEffects(card.effects);
        this.moon = card.moon;
        this.avoidDeath = card.avoidDeath;
        this.canFight = card.canFight;
        this.canFightFromBehind = card.canFightFromBehind;
        this.flipped = card.flipped;
        this.moved = card.moved;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getCost() {
        return this.cost;
    }

    public int getAttack() {
        return this.attack;
    }

    public List<Effect> getEffects() {
        return this.effects;
    }

    public int getMoon() {
        return this.moon;
    }

    public void setMoon(int moon) {
        this.moon = moon;
    }

    public boolean isAvoidDeath() {
        return this.avoidDeath;
    }

    public void setAvoidDeath(boolean avoidDeath) {
        this.avoidDeath = avoidDeath;
    }

    public boolean canFight() {
        return this.canFight;
    }

    public void setCanFight(boolean canFight) {
        this.canFight = canFight;
    }

    public boolean canFightFromBehind() {
        return this.canFightFromBehind;
    }

    public void setCanFightFromBehind(boolean canFightFromBehind) {
        this.canFightFromBehind = canFightFromBehind;
    }

    public boolean isFlipped() {
        return this.flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    public boolean isMoved() {
        return this.moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public static List<Card> deepCopyCards(List<Card> cards) {
        return cards.stream().map(Card::new).collect(Collectors.toList());
    }

    public void printCard() {
        System.out.println("Card: " + this.name + " (id: " + this.id + ")");
        System.out.println("Cost: " + this.cost);
        System.out.println("Attack: " + this.attack + "\n");
        for (Effect effect : this.effects) {
            effect.printEffect();
        }
    }
}

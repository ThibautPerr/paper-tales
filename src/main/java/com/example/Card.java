package com.example;

import java.util.List;
import java.util.stream.Collectors;

public class Card {
    private int id;
    private String name;
    private int cost;
    private int attack;
    private int moon; // Could create a extension of Card for cards in play

    public Card(int id, String name, int cost, int attack) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.attack = attack;
        this.moon = 0;
    }

    public Card(Card card) {
        this.id = card.id;
        this.name = card.name;
        this.cost = card.cost;
        this.attack = card.attack;
        this.moon = card.moon;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getAttack() {
        return attack;
    }

    public int getMoon() {
        return moon;
    }

    public void setMoon(int moon) {
        this.moon = moon;
    }

    public static List<Card> deepCopyCards(List<Card> cards) {
        return cards.stream().map(Card::new).collect(Collectors.toList());
    }

    public void printCard() {
        System.out.println("Card: " + name + " (id: " + id + ")");
        System.out.println("Cost: " + cost);
        System.out.println("Attack: " + attack + "\n");
    }

}

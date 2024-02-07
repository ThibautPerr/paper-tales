package com.example;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Card> frontCards;
    private List<Card> backCards;

    private int maxFrontCards;
    private int maxBackCards;

    public Board() {
        this.frontCards = new ArrayList<Card>();
        this.backCards = new ArrayList<Card>();
        this.maxFrontCards = 2;
        this.maxBackCards = 2;
    }

    public List<Card> getFrontCards() {
        return frontCards;
    }

    public List<Card> getBackCards() {
        return backCards;
    }

    public int getMaxFrontCards() {
        return maxFrontCards;
    }

    public int getMaxBackCards() {
        return maxBackCards;
    }

    public void addFrontCard(Card card) {
        this.frontCards.add(card);
    }

    public void addBackCard(Card card) {
        this.backCards.add(card);
    }

    public void removeFrontCard(Card card) {
        this.frontCards.remove(card);
    }

    public void removeFrontCardById(int id) {
        for (Card card : frontCards) {
            if (card.getId() == id) {
                frontCards.remove(card);
                return;
            }
        }
    }

    public void removeBackCard(Card card) {
        this.backCards.remove(card);
    }

    public void removeBackCardById(int id) {
        for (Card card : backCards) {
            if (card.getId() == id) {
                backCards.remove(card);
                return;
            }
        }
    }

    public void setMaxFrontCards(int maxFrontCards) {
        this.maxFrontCards = maxFrontCards;
    }

    public void printBoard() {
        System.out.print("\tFront cards: ");
        for (Card card : frontCards) {
            System.out.print(card.getName() + ", ");
        }
        System.out.print("\n\tBack cards: ");
        for (Card card : backCards) {
            System.out.print(card.getName() + ", ");
        }
        System.out.println();
    }
}

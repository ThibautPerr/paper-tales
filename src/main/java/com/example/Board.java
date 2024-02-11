package com.example;

import java.util.ArrayList;
import java.util.Iterator;
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

    public List<Card> getCards() {
        List<Card> cards = new ArrayList<Card>();
        cards.addAll(this.frontCards);
        cards.addAll(this.backCards);
        return cards;
    }

    public List<Card> getFrontCards() {
        return this.frontCards;
    }

    public List<Card> getBackCards() {
        return this.backCards;
    }

    public int getMaxFrontCards() {
        return this.maxFrontCards;
    }

    public int getMaxBackCards() {
        return this.maxBackCards;
    }

    public void addFrontCard(Card card) {
        this.frontCards.add(card);
    }

    public void addBackCard(Card card) {
        this.backCards.add(card);
    }

    public void removeCardById(int idCard) {
        Iterator<Card> it = this.frontCards.iterator();
        while (it.hasNext()) {
            Card card = it.next();
            if (card.getId() == idCard) {
                it.remove();
                return;
            }
        }

        it = this.backCards.iterator();
        while (it.hasNext()) {
            Card card = it.next();
            if (card.getId() == idCard) {
                it.remove();
                return;
            }
        }
    }

    public void removeFrontCard(Card card) {
        this.frontCards.remove(card);
    }

    public void removeFrontCardById(int id) {
        for (Card card : frontCards) {
            if (card.getId() == id) {
                this.frontCards.remove(card);
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
                this.backCards.remove(card);
                return;
            }
        }
    }

    public void setMaxFrontCards(int maxFrontCards) {
        this.maxFrontCards = maxFrontCards;
    }

    public boolean isPresentFrontCard(int id) {
        for (Card card : this.frontCards) {
            if (card.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public boolean isPresentBackCard(int id) {
        for (Card card : this.backCards) {
            if (card.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public void printBoard() {
        System.out.print("\tFront cards: ");
        for (Card card : this.frontCards) {
            System.out.print(card.getName() + ", ");
        }
        System.out.print("\n\tBack cards: ");
        for (Card card : this.backCards) {
            System.out.print(card.getName() + ", ");
        }
        System.out.println();
    }
}

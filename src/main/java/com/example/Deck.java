package com.example;

import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> deck;

    public Deck(List<Card> deck) {
        this.deck = deck;
    }

    public List<Card> getDeck() {
        return this.deck;
    }
    
    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public void shuffle() {
        Collections.shuffle(this.deck);
    }

    public Card getFirstCard() {
        Card card = new Card (this.deck.get(0));
        this.deck.remove(0);
        return card;
    }
}

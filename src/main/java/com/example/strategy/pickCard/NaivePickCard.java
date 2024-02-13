package com.example.strategy.pickCard;

import java.util.List;

import com.example.Card;
import com.example.Player;

public class NaivePickCard extends PickCard {

    public NaivePickCard() {
        super();
    }

    @Override
    public Card pickCard(Player player, List<Card> cards) {
        return cards.get(0);
    }

}
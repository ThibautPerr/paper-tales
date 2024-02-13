package com.example.strategy.keepCard;

import com.example.Card;
import com.example.Player;

public class NaiveKeepCard extends KeepCard {
    public NaiveKeepCard() {
        super();
    }

    @Override
    public Card keepCard(Player player) {
        return player.getHand().size() > 0 ? player.getHand().get(0) : null;
    }
}

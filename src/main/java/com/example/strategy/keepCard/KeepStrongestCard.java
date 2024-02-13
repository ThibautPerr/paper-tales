package com.example.strategy.keepCard;

import com.example.Card;
import com.example.Player;

public class KeepStrongestCard extends KeepCard {
    public KeepStrongestCard() {
        super();
    }

    @Override
    public Card keepCard(Player player) {
        return player.getHand().size() > 0
                ? player.getHand().stream().reduce((a, b) -> a.getAttack() > b.getAttack() ? a : b).get()
                : null;
    }
}

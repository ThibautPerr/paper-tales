package com.example.strategy.playCard;

import java.util.List;

import com.example.Card;
import com.example.Player;

public class NaivePlayCard extends PlayCard {
    public NaivePlayCard() {
        super();
    }

    @Override
    public List<Card> playFrontCard(Player player) {
        return player.getHand();
    }

    @Override
    public List<Card> playBackCards(Player player) {
        return player.getHand();
    }

}

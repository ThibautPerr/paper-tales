package com.example.strategy.playCard;

import java.util.List;

import com.example.Card;
import com.example.Player;

public class PlayStrongCard extends PlayCard {
    public PlayStrongCard() {
        super();
    }

    @Override
    public List<Card> playFrontCard(Player player) {
        return player.getHand().stream().sorted((a, b) -> b.getAttack() - a.getAttack()).toList();
    }

    @Override
    public List<Card> playBackCards(Player player) {
        return player.getHand().stream().sorted((a, b) -> a.getAttack() - b.getAttack()).toList();
    }

}

package com.example.strategy.playCard;

import java.util.List;

import com.example.Card;
import com.example.Player;

public abstract class PlayCard {
    public PlayCard() {
    }

    public abstract List<Card> playFrontCard(Player player);
    public abstract List<Card> playBackCards(Player player);
}

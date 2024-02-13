package com.example.strategy.pickCard;

import java.util.List;

import com.example.Card;
import com.example.Player;

public abstract class PickCard {
    public PickCard() {
    }

    public abstract Card pickCard(Player player, List<Card> cards);
}

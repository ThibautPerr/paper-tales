package com.example.strategy.keepCard;

import com.example.Card;
import com.example.Player;

public abstract class KeepCard {
    public KeepCard() {
    }

    public abstract Card keepCard(Player player);
}

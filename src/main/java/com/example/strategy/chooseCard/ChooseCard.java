package com.example.strategy.chooseCard;

import com.example.Card;
import com.example.Player;

public abstract class ChooseCard {
    public ChooseCard() {
    }

    public abstract Card chooseCardToAvoidDeath(Player player);

    public abstract int chooseUnitToAddMoon(Player player);
}

package com.example.strategy.chooseCard;

import com.example.Card;
import com.example.Player;

public class NaiveChooseCard extends ChooseCard {
    public NaiveChooseCard() {
        super();
    }

    @Override
    public Card chooseCardToAvoidDeath(Player player) {
        return player.getBoard().getCards().stream()
                .filter(card -> card.getMoon() >= 0 && !card.isAvoidDeath()).findFirst().orElse(null);
    }

    @Override 
    public int chooseUnitToAddMoon(Player player) {
        return player.getBoard().getCards().get(0).getId();
    }
}

package com.example.strategy.chooseCard;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.Card;
import com.example.Player;

public class SmartChooseCard extends ChooseCard {
    public SmartChooseCard() {
        super();
    }

    @Override
    public Card chooseCardToAvoidDeath(Player player) {
        List<Card> cards = player.getBoard().getCards().stream()
                .filter(card -> card.getMoon() >= 0 && !card.isAvoidDeath()).toList();

        if (cards.isEmpty())
            return null;

        return cards.stream().reduce((a, b) -> a.getAttack() > b.getAttack() ? a : b).get();
    }

    @Override
    public int chooseUnitToAddMoon(Player player) {
        List<Card> cards = player.getBoard().getCards();

        List<String> moonFunctions = Arrays.asList("addResourcePerMoon", "updateResourcePerMoon", "addWarpointPerMoon",
                "addWarpointPerAtLeastMoon", "removePointIfOnlyXMoon", "addPointPerMoon");
        Optional<Card> cardWithMoonFunction = cards.stream()
                .filter(card -> card.getEffects().stream()
                        .anyMatch(effect -> moonFunctions.contains(effect.getFunction())))
                .findFirst();

        if (cardWithMoonFunction.isPresent())
            return cardWithMoonFunction.get().getId();

        return cards.stream().reduce((a, b) -> a.getAttack() < b.getAttack() ? a : b).get().getId();
    }

}

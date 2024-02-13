package com.example.strategy;

import java.util.List;

import com.example.BuildingPhase;
import com.example.Card;
import com.example.Player;
import com.example.strategy.boardReorganisation.BoardReorganisation;
import com.example.strategy.build.Build;
import com.example.strategy.chooseCard.ChooseCard;
import com.example.strategy.keepCard.KeepCard;
import com.example.strategy.pickCard.PickCard;
import com.example.strategy.playCard.PlayCard;

public class Strategy {
    private PickCard pickCard;
    private BoardReorganisation boardReorganisation;
    private PlayCard playCard;
    private KeepCard keepCard;
    private Build build;
    private ChooseCard chooseCard;

    public Strategy(PickCard pickCard, BoardReorganisation boardReorganisation, PlayCard playCard,
            KeepCard keepCard, Build build, ChooseCard chooseCard) {
        this.pickCard = pickCard;
        this.boardReorganisation = boardReorganisation;
        this.playCard = playCard;
        this.keepCard = keepCard;
        this.build = build;
        this.chooseCard = chooseCard;
    }

    public Card pickCard(Player player, List<Card> cards) {
        return pickCard.pickCard(player, cards);
    }

    public void boardReorganisation(Player player) {
        boardReorganisation.boardReorganisation(player);
    }

    public List<Card> playFrontCard(Player player) {
        return playCard.playFrontCard(player);
    }

    public List<Card> playBackCards(Player player) {
        return playCard.playBackCards(player);
    }

    public Card keepCard(Player player) {
        return keepCard.keepCard(player);
    }

    public BuildingPhase build(Player player, List<BuildingPhase> buildablePhases) {
        return build.build(player, buildablePhases);
    }

    public Card chooseCardToAvoidDeath(Player player) {
        return chooseCard.chooseCardToAvoidDeath(player);
    }

    public int chooseUnitToAddMoon(Player player) {
        return chooseCard.chooseUnitToAddMoon(player);
    }
}

package com.example.strategy.boardReorganisation;

import java.util.Comparator;
import java.util.stream.IntStream;

import com.example.Card;
import com.example.Player;

public class SmartBoardReorganisation extends BoardReorganisation {
    public SmartBoardReorganisation() {
        super();
    }

    @Override
    public void boardReorganisation(Player player) {
        reorderCardsByAttack(player);
        setBackCantFightCards(player);
        setBackCanFightFromBehindCards(player);
    }

    private void reorderCardsByAttack(Player player) {
        if (player.getBoard().getCards().size() > 0) {
            if (player.getBoard().getCards().size() <= player.getBoard().getMaxFrontCards())
                player.getBoard().getCards().stream()
                        .filter(card -> !player.getBoard().isPresentFrontCard(card.getId()))
                        .forEach(card -> player.getBoard().moveBackCardToFront(card, player));
            else {
                player.getBoard().getCards().sort(Comparator.comparingInt(Card::getAttack));
                IntStream.range(0, player.getBoard().getMaxFrontCards())
                        .forEach(i -> {
                            if (player.getBoard().isPresentBackCard(player.getBoard().getCards().get(i).getId()))
                                player.getBoard().moveBackCardToFront(player.getBoard().getCards().get(i), player);
                        });
                IntStream.range(player.getBoard().getMaxFrontCards(), player.getBoard().getCards().size())
                        .forEach(i -> {
                            if (player.getBoard().isPresentFrontCard(player.getBoard().getCards().get(i).getId()))
                                player.getBoard().moveFrontCardToBack(player.getBoard().getCards().get(i), player);
                        });
            }
        }

    }

    private void setBackCantFightCards(Player player) {
        if (player.getBoard().getFrontCards().size() > 0) {
            player.getBoard().getFrontCards().stream()
                    .filter(frontCard -> !frontCard.canFight() && !frontCard.isMoved())
                    .map(Card::new)
                    .forEach(frontCard -> {
                        if (player.getBoard().getBackCards().size() < player.getBoard().getMaxBackCards()) {
                            player.getBoard().moveFrontCardToBack(frontCard, player);
                            frontCard.setMoved(true);
                        } else {
                            player.getBoard().getBackCards().stream().filter(backCard -> !backCard.canFight())
                                    .max(Comparator.comparingInt(Card::getAttack))
                                    .ifPresent(backCard -> {
                                        player.getBoard().exchangeCards(frontCard, backCard);
                                        frontCard.setMoved(true);
                                    });
                        }
                    });
        }
    }

    private void setBackCanFightFromBehindCards(Player player) {
        if (player.getBoard().getFrontCards().size() > 0) {
            player.getBoard()
                    .getFrontCards().stream()
                    .filter(frontCard -> frontCard.canFightFromBehind() && !frontCard.isMoved())
                    .map(Card::new)
                    .forEach(frontCard -> {
                        if (player.getBoard().getBackCards().size() < player.getBoard().getMaxBackCards()) {
                            player.getBoard().moveFrontCardToBack(frontCard, player);
                            frontCard.setMoved(true);
                        } else {
                            player.getBoard().getBackCards().stream().filter(backCard -> backCard.canFightFromBehind())
                                    .max(Comparator.comparingInt(Card::getAttack))
                                    .ifPresent(backCard -> {
                                        player.getBoard().exchangeCards(frontCard, backCard);
                                        frontCard.setMoved(true);
                                    });
                        }
                    });
        }
    }
}
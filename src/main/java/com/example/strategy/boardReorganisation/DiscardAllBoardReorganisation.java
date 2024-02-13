package com.example.strategy.boardReorganisation;

import com.example.Player;

public class DiscardAllBoardReorganisation extends BoardReorganisation {
    public DiscardAllBoardReorganisation() {
        super();
    }

    @Override
    public void boardReorganisation(Player player) {
        player.discardCards(player.getBoard().getCards());
    }

}

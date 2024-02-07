package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.Resource.ResourceType;
import com.example.utils.Utils;

public class App {
    public static void main(String[] args) {
        // Setup the game
        List<Card> deck = Utils.createCards();
        Collections.shuffle(deck);
        List<Card> discardPile = new ArrayList<Card>();
        boolean[] realPlayers = { true, false, false, false };
        List<Player> players = Utils.createPlayers(4, realPlayers);
        List<Building> buildings = Utils.createBuildings();
        for (Player player : players) {
            player.setBuildings(buildings);
            player.setResource(ResourceType.GOLD, 3);
        }

        // set resources
        players.get(0).setResource(ResourceType.WOOD, 1);
        players.get(1).setResource(ResourceType.MEAT, 1);
        players.get(2).setResource(ResourceType.CRYSTAL, 2);
        players.get(3).setResource(ResourceType.WOOD, 2);
        players.get(3).setResource(ResourceType.MEAT, 1);

        // There are 4 turns played
        for (int i = 0; i < 4; i++) {
            System.out.println("Turn " + (i + 1));

            // Phase 1 : Each player draws 5 cards, picks one, and pass it to the next
            // Repeat until there is no more cards
            Utils.phase1(players, deck);

            // Phase 2 : Each player reoarganises his board
            // Then play as many cards as he wants and pay the cost

            // Cards are revealed

            // Each player can only keep one card in hand
            Utils.phase2(players, discardPile);

            // Phase 3 : For each player, if he has more attack than his neighbour, he gains
            // 3 points
            Utils.phase3(players);

            // Phase 4 : Each player gains 2 golds
            Utils.phase4(players);

            // Phase 5 : Each player can build one building, if he has the requirements and
            // has built the previous phase of the building
            Utils.phase5(players);

            // Phase 6 : Each units with a moon counter dies
            // Then each units gains a moon counter
            Utils.phase6(players, discardPile);

        }

        // Trigger end game effects

        // Add buildings points to the players points

        // The player with the most points wins
        // If there is a tie, the player with the most gold wins
        // If there is still a tie, the players share the victory

    }
}

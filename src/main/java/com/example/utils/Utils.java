package com.example.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.example.Building;
import com.example.BuildingPhase;
import com.example.Card;
import com.example.Player;
import com.example.Resource.ResourceType;

public abstract class Utils {
    public static final boolean PRINT_START_PHASE_1 = false;
    public static final boolean PRINT_END_PHASE_1 = true;
    public static final boolean PRINT_START_PHASE_2 = false;
    public static final boolean PRINT_END_PHASE_2 = true;
    public static final boolean PRINT_START_PHASE_3 = false;
    public static final boolean PRINT_END_PHASE_3 = true;
    public static final boolean PRINT_START_PHASE_4 = false;
    public static final boolean PRINT_END_PHASE_4 = true;
    public static final boolean PRINT_START_PHASE_5 = false;
    public static final boolean PRINT_END_PHASE_5 = true;
    public static final boolean PRINT_START_PHASE_6 = false;
    public static final boolean PRINT_END_PHASE_6 = true;

    public static List<Card> createCards() {
        JSONParser parser = new JSONParser();
        List<Card> cards = new ArrayList<Card>();

        try (FileReader reader = new FileReader("src/main/resources/cards.json")) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            cards = JSONUtils.readCardsFromJSON(jsonArray);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return cards;
    }

    public static List<Player> createPlayers(int numberOfPlayers, boolean[] realPlayers) {
        List<Player> players = new ArrayList<Player>();
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player(i, realPlayers[i]));
        }
        return players;
    }

    public static List<Building> createBuildings() {
        JSONParser parser = new JSONParser();
        List<Building> buildings = new ArrayList<Building>();

        try (FileReader reader = new FileReader("src/main/resources/buildings.json")) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            buildings = JSONUtils.readBuildingsFromJSON(jsonArray);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return buildings;
    }

    public static void phase1(List<Player> players, List<Card> deck) {
        if (PRINT_START_PHASE_1)
            System.out.println("\n--------------- Start phase1 ---------------");

        // Each player draws 5 cards
        List<List<Card>> drewCards = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            List<Card> cards = new ArrayList<Card>();
            for (int j = 0; j < 5; j++) {
                cards.add(deck.get(0));
                deck.remove(0);
            }
            drewCards.add(cards);
        }

        // Each player picks one, and pass it to the next player
        int drewCardsSize = drewCards.get(0).size();
        for (int i = 0; i < drewCardsSize; i++) {
            for (int j = 0; j < drewCards.size(); j++) {
                List<Card> cardsSet = drewCards.get((j + i) % drewCards.size());
                drawCard(players.get(j), cardsSet);
            }
        }

        if (PRINT_END_PHASE_1) {
            for (Player player : players) {
                System.out.print("Player " + player.getId() + " hand: ");
                for (Card card : player.getHand()) {
                    System.out.print(card.getName() + ", ");
                }
                System.out.println();
            }
            System.out.println("--------------- End phase1 ---------------");
        }
    }

    public static void drawCard(Player player, List<Card> cards) {
        // TODO Create a pick method
        player.addCardToHand(cards.get(0));
        cards.remove(0);
    }

    public static void phase2(List<Player> players, List<Card> discardPile) {
        if (PRINT_START_PHASE_2) {
            System.out.println("\n--------------- Start phase2 ---------------");
            for (Player player : players) {
                System.out.print("Player " + player.getId() + " starting hand: ");
                for (Card card : player.getHand()) {
                    System.out.print(card.getName() + ", ");
                }
                System.out.println();
                System.out.println("Player " + player.getId() + " starting board: ");
                player.getBoard().printBoard();
                System.out.println("Player " + player.getId() + " golds: "
                        + player.getResourceByResourceType(ResourceType.GOLD).getQuantity());
            }
        }
        boardsReorganisation(players);
        playCards(players);
        discardCards(players, discardPile);
        if (PRINT_END_PHASE_2) {
            for (Player player : players) {
                System.out.print("Player " + player.getId() + " ending hand: ");
                for (Card card : player.getHand()) {
                    System.out.print(card.getName() + ", ");
                }
                System.out.println();
                System.out.println("Player " + player.getId() + " ending board: ");
                player.getBoard().printBoard();
                System.out.println("Player " + player.getId() + " golds: "
                        + player.getResourceByResourceType(ResourceType.GOLD).getQuantity());
            }
            System.out.println("--------------- End phase2 ---------------");
        }
    }

    public static void boardsReorganisation(List<Player> players) {
        // TODO Create a reorganisation method

        // List<Card> newFrontCard =
        // Card.deepCopyCards(player.getBoard().getFrontCards());
        // for (int i = 0; i < newFrontCard.size(); i++) {
        // player.getBoard().removeFrontCardById(newFrontCard.get(0).getId());
        // }

        // List<Card> newBackCard =
        // Card.deepCopyCards(player.getBoard().getBackCards());
        // for (int i = 0; i < newBackCard.size(); i++) {
        // player.getBoard().removeBackCardById(newBackCard.get(0).getId());
        // }
    }

    public static void playCards(List<Player> players) {
        for (Player player : players) {
            // TODO Create a play method
            // Play card if there is space on the front board
            List<Card> newHand = Card.deepCopyCards(player.getHand());
            newHand.sort((c1, c2) -> c2.getAttack() - c1.getAttack());
            for (Card cardToPlay : newHand) {
                if (cardToPlay.getCost() <= player.getResourceByResourceType(ResourceType.GOLD).getQuantity()
                        && player.getBoard().getFrontCards().size() < player.getBoard().getMaxFrontCards()) {
                    player.playCardById(cardToPlay.getId(), true);
                }
            }

            // Play card if there is space on the back board
            newHand = Card.deepCopyCards(player.getHand());
            for (Card cardToPlay : newHand) {
                if (cardToPlay.getCost() <= player.getResourceByResourceType(ResourceType.GOLD).getQuantity()
                        && player.getBoard().getBackCards().size() < player.getBoard().getMaxBackCards()) {
                    player.playCardById(cardToPlay.getId(), false);
                }
            }
        }
    }

    public static void discardCards(List<Player> players, List<Card> discardPile) {
        for (Player player : players) {
            // TODO Create a discard method
            if (player.getHand().size() > 1) {
                for (int i = 1; i < player.getHand().size(); i++) {
                    discardPile.add(player.getHand().get(i));
                }
                player.setHand(player.getHand().subList(0, 1));
            }
        }
    }

    public static void phase3(List<Player> players) {
        if (PRINT_START_PHASE_3)
            System.out.println("\n--------------- Start phase3 ---------------");

        for (Player player : players) {
            if (PRINT_START_PHASE_3) {
                System.out.println("Player " + player.getId() + " starting board: ");
                player.getBoard().printBoard();
            }

            player.setWarPoint(0);
            for (Card frontCard : player.getBoard().getFrontCards()) {
                player.addWarPoint(frontCard.getAttack());
            }

            player.playPhase3Effects();
        }

        if (PRINT_END_PHASE_3) {
            for (Player player : players) {
                System.out.println("Player " + player.getId() + " war points: " + player.getWarPoint() + ", points : "
                        + player.getPoint());
            }
        }

        // Each player confronts his two neighbours, if he has more attack than his
        // neighbour, he gains 3 points
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Player leftPlayer = players.get(i == 0 ? players.size() - 1 : i - 1);
            Player rightPlayer = players.get(i == players.size() - 1 ? 0 : i + 1);

            if (leftPlayer.getWarPoint() <= player.getWarPoint()) {
                player.setPoint(player.getPoint() + 3);
            }
            if (rightPlayer.getWarPoint() <= player.getWarPoint()) {
                player.setPoint(player.getPoint() + 3);
            }
        }

        if (PRINT_END_PHASE_3) {
            for (Player player : players) {
                System.out.println("Player " + player.getId() + " points : " + player.getPoint());
            }
            System.out.println("--------------- End phase3 ---------------");
        }
    }

    public static void phase4(List<Player> players) {
        if (PRINT_START_PHASE_4)
            System.out.println("\n--------------- Start phase4 ---------------");

        if (PRINT_START_PHASE_4)
            for (Player player : players)
                System.out.println("Player " + player.getId() + " gold: "
                        + player.getResourceByResourceType(ResourceType.GOLD).getQuantity());

        for (Player player : players) {
            player.setResource(ResourceType.GOLD,
                    player.getResourceByResourceType(ResourceType.GOLD).getQuantity() + 2);
            player.playPhase4Effects();
        }

        if (PRINT_END_PHASE_4)
            for (Player player : players) {
                System.out.print("Player " + player.getId() + " : ");
                player.printResources();
            }

        if (PRINT_END_PHASE_4)
            System.out.println("--------------- End phase4 ---------------");
    }

    public static void phase5(List<Player> players) {
        if (PRINT_START_PHASE_5)
            System.out.println("\n--------------- Start phase5 ---------------");

        if (PRINT_START_PHASE_5)
            for (Player player : players) {
                player.printBuiltBuildings();
                player.printResources();
            }

        for (Player player : players) {
            List<BuildingPhase> buildableBuildingPhases = player.getBuildableBuildingPhases();

            // TODO : Create a build BuildingPhase method
            int i = 0;
            boolean built = false;
            while (!built && i < buildableBuildingPhases.size()) {
                BuildingPhase buildingPhase = buildableBuildingPhases.get(i);
                if (player.hasEnoughResources(buildingPhase.getRequirements())
                        || player.hasEnoughResources(buildingPhase.getOptionnalRequirements())) {
                    player.buildBuildingPhase(buildingPhase, player.builtWithGold(buildingPhase));
                    built = true;
                }
                i++;
            }
        }

        if (PRINT_END_PHASE_5)
            for (Player player : players) {
                player.printBuiltBuildings();
                System.out.println("Player " + player.getId() + " gold: "
                        + player.getResourceByResourceType(ResourceType.GOLD).getQuantity());
            }

        if (PRINT_END_PHASE_5)
            System.out.println("--------------- End phase5 ---------------");
    }

    public static void phase6(List<Player> players, List<Card> discardPile) {
        if (PRINT_START_PHASE_6)
            System.out.println("\n--------------- Start phase6 ---------------");
        for (Player player : players) {
            if (PRINT_START_PHASE_6) {
                System.out.println("Player " + player.getId() + " starting board: ");
                player.getBoard().printBoard();
            }
            List<Card> newFrontCard = Card.deepCopyCards(player.getBoard().getFrontCards());
            for (int i = 0; i < newFrontCard.size(); i++)
                if (newFrontCard.get(i).getMoon() > 0)
                    player.getBoard().removeFrontCardById(newFrontCard.get(i).getId());

            List<Card> newBackCard = Card.deepCopyCards(player.getBoard().getBackCards());
            for (int i = 0; i < newBackCard.size(); i++)
                if (newBackCard.get(i).getMoon() > 0)
                    player.getBoard().removeBackCardById(newBackCard.get(i).getId());

            for (Card card : player.getBoard().getFrontCards())
                card.setMoon(card.getMoon() + 1);

            for (Card card : player.getBoard().getBackCards())
                card.setMoon(card.getMoon() + 1);

            if (PRINT_END_PHASE_6) {
                System.out.println("Player " + player.getId() + " ending board: ");
                player.getBoard().printBoard();
            }
        }
        if (PRINT_END_PHASE_6)
            System.out.println("--------------- End phase6 ---------------");
    }

}

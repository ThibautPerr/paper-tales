package com.example.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.example.Building;
import com.example.BuildingPhase;
import com.example.Card;
import com.example.Deck;
import com.example.Player;
import com.example.Resource;
import com.example.Result;
import com.example.Resource.ResourceType;
import com.example.strategy.Strategy;

public abstract class Utils {
    public static boolean LOG_START_PHASE_1;
    public static boolean LOG_END_PHASE_1;
    public static boolean LOG_START_PHASE_2;
    public static boolean LOG_END_PHASE_2;
    public static boolean LOG_START_PHASE_3;
    public static boolean LOG_END_PHASE_3;
    public static boolean LOG_START_PHASE_4;
    public static boolean LOG_END_PHASE_4;
    public static boolean LOG_START_PHASE_5;
    public static boolean LOG_END_PHASE_5;
    public static boolean LOG_START_PHASE_6;
    public static boolean LOG_END_PHASE_6;

    public static void setStartLogs(boolean value) {
        LOG_START_PHASE_1 = value;
        LOG_START_PHASE_2 = value;
        LOG_START_PHASE_3 = value;
        LOG_START_PHASE_4 = value;
        LOG_START_PHASE_5 = value;
        LOG_START_PHASE_6 = value;
    }

    public static void setEndLogs(boolean value) {
        LOG_END_PHASE_1 = value;
        LOG_END_PHASE_2 = value;
        LOG_END_PHASE_3 = value;
        LOG_END_PHASE_4 = value;
        LOG_END_PHASE_5 = value;
        LOG_END_PHASE_6 = value;
    }

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

    public static List<Player> createPlayers(int numberOfPlayers, List<Strategy> strategies) {
        List<Player> players = new ArrayList<Player>();
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player(i, strategies.get(i)));
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

    public static void phase1(List<Player> players, Deck deck) {
        if (LOG_START_PHASE_1)
            System.out.println("\n--------------- Start phase1 ---------------");

        // Each player draws 5 cards
        List<List<Card>> cardsSet = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            List<Card> cards = new ArrayList<Card>();
            for (int j = 0; j < 5; j++)
                cards.add(deck.getFirstCard());
            cardsSet.add(cards);
        }

        // Each player picks one, and pass it to the next player
        for (int i = 0; i < cardsSet.size(); i++) {
            List<Card> cardSetCopy = new ArrayList<>(cardsSet.get(i));
            for (int j = 0; j < cardsSet.get(i).size(); j++) {
                Player player = players.get((i + j) % players.size());
                Card card = player.getStrategy().pickCard(player, cardSetCopy);
                player.addCardToHand(card);
                cardSetCopy.remove(card);
            }
        }

        if (LOG_END_PHASE_1) {
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

    public static void phase2(List<Player> players, Deck deck, List<Card> discardPile) {
        if (LOG_START_PHASE_2) {
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

        for (Player player : players) {
            player.playPhase2Effects();
            boardReorganisation(player);
            playCards(player, deck);
            keepCards(player, discardPile);
        }

        if (LOG_END_PHASE_2) {
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

    public static void boardReorganisation(Player player) {
        List<Card> cards = player
                .findCardsWithAddResourceIfFrontUnit(player.getBoard().getCards());
        player.getStrategy().boardReorganisation(player);
        player.playCardsWithAddResourceIfFrontUnit(cards);
    }

    public static void playCards(Player player, Deck deck) {
        Iterator<Card> itFront = (new ArrayList<Card>(player.getStrategy().playFrontCard(player))).iterator();
        while (itFront.hasNext()) {
            Card card = itFront.next();
            if (card.getCost() <= player.getResourceByResourceType(ResourceType.GOLD).getQuantity()
                    && player.getBoard().getFrontCards().size() < player.getBoard().getMaxFrontCards()) {
                player.playCardById(card.getId(), true);
            }
        }

        Iterator<Card> itBack = (new ArrayList<Card>(player.getStrategy().playBackCards(player))).iterator();
        while (itBack.hasNext()) {
            Card card = itBack.next();
            if (card.getCost() <= player.getResourceByResourceType(ResourceType.GOLD).getQuantity()
                    && player.getBoard().getBackCards().size() < player.getBoard().getMaxBackCards()) {
                player.playCardById(card.getId(), false);
            }
        }

        turnFlippedCards(player, deck);
    }

    public static void turnFlippedCards(Player player, Deck deck) {
        for (Card card : player.getBoard().getCards().stream().filter(card -> card.isFlipped()).toList()) {
            card.setFlipped(false);
            player.playOnFlipEffect(card, deck);
        }
    }

    public static void keepCards(Player player, List<Card> discardPile) {
        Card keepCard = player.getStrategy().keepCard(player);
        player.keepCard(keepCard, discardPile);
    }

    public static void phase3(List<Player> players) {
        if (LOG_START_PHASE_3)
            System.out.println("\n--------------- Start phase3 ---------------");

        for (Player player : players) {
            if (LOG_START_PHASE_3) {
                System.out.println("Player " + player.getId() + " board: ");
                player.getBoard().printBoard();
                System.out.println("Player " + player.getId() + " points : " + player.getPoint());
            }

            player.setWarPoint(0);
            player.setWarWon(0);
            player.playPhase3Effects();

            for (Card card : player.getBoard().getCards())
                if (card.canFight() &&
                        (player.getBoard().isPresentFrontCard(card.getId()) || card.canFightFromBehind()))
                    player.addWarPoint(card.getAttack());

        }

        // Each player confronts his two neighbours, if he has more attack than his
        // neighbour, he gains 3 points
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Player leftPlayer = players.get(i == 0 ? players.size() - 1 : i - 1);
            Player rightPlayer = players.get(i == players.size() - 1 ? 0 : i + 1);

            if (leftPlayer.getWarPoint() <= player.getWarPoint()) {
                player.setWarWon(player.getWarWon() + 1);
                player.setPoint(player.getPoint() + 3);
            }
            if (rightPlayer.getWarPoint() <= player.getWarPoint()) {
                player.setWarWon(player.getWarWon() + 1);
                player.setPoint(player.getPoint() + 3);
            }

            player.playOnEndPhase3Effects();
        }

        if (LOG_END_PHASE_3) {
            for (Player player : players) {
                System.out.println("Player " + player.getId() + " war points: " + player.getWarPoint() + ", points : "
                        + player.getPoint());
            }
            System.out.println("--------------- End phase3 ---------------");
        }
    }

    public static void phase4(List<Player> players) {
        if (LOG_START_PHASE_4)
            System.out.println("\n--------------- Start phase4 ---------------");

        if (LOG_START_PHASE_4)
            for (Player player : players)
                System.out.println("Player " + player.getId() + " gold: "
                        + player.getResourceByResourceType(ResourceType.GOLD).getQuantity());

        for (Player player : players) {
            player.setGoldToAddInPhase4(player.getGoldToAddInPhase4());
            player.playPhase4Effects();
            player.setResource(ResourceType.GOLD, player.getResourceByResourceType(ResourceType.GOLD).getQuantity()
                    + player.getGoldToAddInPhase4());
            player.setGoldToAddInPhase4(2);
        }

        if (LOG_END_PHASE_4)
            for (Player player : players) {
                player.printResources();
            }

        if (LOG_END_PHASE_4)
            System.out.println("--------------- End phase4 ---------------");
    }

    public static void phase5(List<Player> players) {
        if (LOG_START_PHASE_5)
            System.out.println("\n--------------- Start phase5 ---------------");

        if (LOG_START_PHASE_5)
            for (Player player : players) {
                player.printBuiltBuildings();
                player.printResources();
            }

        for (Player player : players) {
            player.playPhase5Effects();
            BuildingPhase buildingPhase = player.getStrategy().build(player, player.getBuildablePhases());
            if (buildingPhase != null) {
                List<Resource> paidResources = player.payResources(buildingPhase);
                player.buildBuildingPhase(buildingPhase, player.builtWithGold(buildingPhase));
                player.deletePaidResources(paidResources);
            }
        }

        if (LOG_END_PHASE_5)
            for (Player player : players) {
                player.printBuiltBuildings();
                System.out.println("Player " + player.getId() + " gold: "
                        + player.getResourceByResourceType(ResourceType.GOLD).getQuantity());
            }

        if (LOG_END_PHASE_5)
            System.out.println("--------------- End phase5 ---------------");
    }

    public static void phase6(List<Player> players, List<Card> discardPile) {
        if (LOG_START_PHASE_6)
            System.out.println("\n--------------- Start phase6 ---------------");
        for (Player player : players) {
            if (LOG_START_PHASE_6) {
                System.out.println("Player " + player.getId() + " starting board: ");
                player.getBoard().printBoard();
            }

            player.playPhase6Effects();
            player.playAvoidDeathForOneUnitWithMoon();

            List<Card> cards = player.getBoard().getCards();
            for (Card card : cards) {
                if (card.getMoon() > 0) {
                    player.playOnPhase6DeathEffects(card);
                    player.playOnDeathEffects(card);
                    player.discardCardWithAddResourceIfFrontUnit(card);
                    player.removeEffectsByCardId(card.getId());
                    player.getBoard().removeCardById(card.getId());
                }
            }

            for (Card card : player.getBoard().getCards())
                player.addMoon(card.getId(), 1);

            if (LOG_END_PHASE_6) {
                System.out.println("Player " + player.getId() + " ending board: ");
                player.getBoard().printBoard();
            }
        }
        if (LOG_END_PHASE_6)
            System.out.println("--------------- End phase6 ---------------");
    }

    // Trigger end game effects
    // Add buildings points to the players points

    // The player with the most points wins
    // If there is a tie, the player with the most gold wins
    // If there is still a tie, the players share the victory
    public static List<Result> endGame(List<Player> players) {
        for (Player player : players) {
            player.playEndGameEffects();
            player.addPointFromBuildings();
        }

        List<Result> results = players.stream()
                .map(player -> new Result(player.getId(), player.getPoint(),
                        player.getResourceByResourceType(ResourceType.GOLD).getQuantity()))
                .sorted(Comparator.comparing(Result::getPlayerPoint)
                        .thenComparing(Result::getPlayerGold))
                .toList();

        results.stream().forEachOrdered(result -> result.setPlayerPlace(results.size() - results.indexOf(result)));
        
        return results;
    }
}

package com.example.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
import com.example.Resource.ResourceType;
import com.example.effects.Effect;
import com.example.effects.OnFlip;
import com.example.effects.OnPhase6;
import com.example.effects.OnUpdate;

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

    public static void phase1(List<Player> players, Deck deck) {
        if (PRINT_START_PHASE_1)
            System.out.println("\n--------------- Start phase1 ---------------");

        // Each player draws 5 cards
        List<List<Card>> drewCards = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            List<Card> cards = new ArrayList<Card>();
            for (int j = 0; j < 5; j++) {
                cards.add(deck.getDeck().get(0));
                deck.getDeck().remove(0);
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

    public static void phase2(List<Player> players, Deck deck, List<Card> discardPile) {
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

        for (Player player : players)
            player.playPhase2Effects();
        boardsReorganisation(players);
        playCards(players, deck);
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
        // Current : remove every card from the board
        // If a card with an effect addResourceIfFrontUnit is moved from back to front,
        // trigger adding effect, else trigger remove

        for (Player player : players) {
            List<Card> cards = player
                    .findCardsWithAddResourceIfFrontUnit(player.getBoard().getCards());
            // discardCards(player, player.getBoard().getFrontCards());
            // discardCards(player, player.getBoard().getBackCards());
            player.playCardsWithAddResourceIfFrontUnit(cards);
        }
    }

    public static void discardCards(Player player, List<Card> cards) {
        List<Card> copyCards = new ArrayList<>(cards);

        for (Card card : copyCards) {
            player.playOnDeathEffects();

            // particular case for cards with addResourceIfFrontUnit effect
            player.discardCardsWithAddResourceIfFrontUnit(
                    player.findCardsWithAddResourceIfFrontUnit(cards));

            player.removeEffectsByCardId(card.getId());
            player.getBoard().removeCardById(card.getId());
        }
    }

    public static void playCards(List<Player> players, Deck deck) {
        for (Player player : players) {
            // TODO Create a play method
            // Play card if there is space on the front board
            Iterator<Card> itFront = player.getHand().stream().sorted(
                    (c1, c2) -> c2.getAttack() - c1.getAttack()).iterator(); // In priority, play cards with most attack
            while (itFront.hasNext()) {
                Card card = itFront.next();
                if (card.getCost() <= player.getResourceByResourceType(ResourceType.GOLD).getQuantity()
                        && player.getBoard().getFrontCards().size() < player.getBoard().getMaxFrontCards()) {
                    player.playCardById(card.getId(), true);
                }
            }

            // Play card if there is space on the back board
            Iterator<Card> itBack = player.getHand().stream().sorted(
                    (c1, c2) -> c2.getAttack() - c1.getAttack()).iterator(); // In priority, play cards with most attack
            while (itBack.hasNext()) {
                Card card = itBack.next();
                if (card.getCost() <= player.getResourceByResourceType(ResourceType.GOLD).getQuantity()
                        && player.getBoard().getBackCards().size() < player.getBoard().getMaxBackCards()) {
                    player.playCardById(card.getId(), false);
                }
            }

            // Turn flipped cards
            for (Card card : player.getBoard().getCards().stream().filter(card -> card.isFlipped()).toList()) {
                card.setFlipped(false);
                for (Effect effect : card.getEffects()) {
                    if (effect instanceof OnFlip && ((OnFlip) effect).getFunction().equals("changeform"))
                        ((OnFlip) effect).changeform(player, deck.getFirstCard());
                }
                for (Effect effect : card.getEffects()) {
                    if (effect instanceof OnFlip)
                        player.playOnFlipEffect((OnFlip) effect, card);
                    else
                        player.addEffect(effect);
                    if (effect instanceof OnUpdate
                            && ((OnUpdate) effect).getFunction().equals("addResourceIfFrontUnit")) {
                        ((OnUpdate) effect).addResourceIfFrontUnit(player);
                    }
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

        if (PRINT_END_PHASE_3) {
            for (Player player : players) {
                System.out.println("Player " + player.getId() + " war points: " + player.getWarPoint() + ", points : "
                        + player.getPoint());
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
            player.setGoldToAddInPhase4(player.getGoldToAddInPhase4());
            player.playPhase4Effects();
            player.setResource(ResourceType.GOLD, player.getResourceByResourceType(ResourceType.GOLD).getQuantity()
                    + player.getGoldToAddInPhase4());
            player.setGoldToAddInPhase4(2);
        }

        if (PRINT_END_PHASE_4)
            for (Player player : players) {
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
            player.playPhase5Effects();
            List<BuildingPhase> buildableBuildingPhases = player.getBuildableBuildingPhases();

            // TODO : Create a build BuildingPhase method
            int i = 0;
            boolean built = false;
            while (!built && i < buildableBuildingPhases.size()) {
                BuildingPhase buildingPhase = buildableBuildingPhases.get(i);

                List<Resource> paidResources = player.payResources(buildingPhase);

                if (player.hasEnoughResources(buildingPhase.getRequirements())
                        || player.hasEnoughResources(buildingPhase.getOptionnalRequirements())
                        || player.isIgnoreBuildingCost()) {
                    player.buildBuildingPhase(buildingPhase, player.builtWithGold(buildingPhase));
                    built = true;
                    player.deletePaidResources(paidResources);
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

            player.playPhase6Effects();
            playAvoidDeathForOneUnitWithMoon(player);

            List<Card> cards = player.getBoard().getCards();
            for (Card card : cards) {
                if (card.getMoon() > 0) {
                    player.playOnPhase6DeathEffects();
                    player.playOnDeathEffects();
                    player.discardCardWithAddResourceIfFrontUnit(card);
                    player.removeEffectsByCardId(card.getId());
                    player.getBoard().removeCardById(card.getId());
                }
            }

            for (Card card : player.getBoard().getCards())
                player.addMoon(card.getId(), 1);

            if (PRINT_END_PHASE_6) {
                System.out.println("Player " + player.getId() + " ending board: ");
                player.getBoard().printBoard();
            }
        }
        if (PRINT_END_PHASE_6)
            System.out.println("--------------- End phase6 ---------------");
    }

    public static void playAvoidDeathForOneUnitWithMoon(Player player) {
        List<OnPhase6> avoidDeathForOneUnitWithMoonEffects = player.getEffects().stream()
                .filter(effect -> effect instanceof OnPhase6).map(effect -> (OnPhase6) effect)
                .filter(effect -> effect.getFunction().equals("avoidDeathForOneUnitWithMoon")).toList();
        if (avoidDeathForOneUnitWithMoonEffects.size() > 0) {
            int i = 0;
            for (OnPhase6 effect : avoidDeathForOneUnitWithMoonEffects) {
                effect.avoidDeathForOneUnitWithMoon(player, player.getBoard().getCards().stream()
                        .filter(card -> card.getMoon() >= 0 && !card.isAvoidDeath()).toList().get(i));
                i++;
            }
        }
    }

    // Trigger end game effects
    // Add buildings points to the players points

    // The player with the most points wins
    // If there is a tie, the player with the most gold wins
    // If there is still a tie, the players share the victory
    public static void endGame(List<Player> players) {
        for (Player player : players) {
            player.playEndGameEffects();
            player.addPointFromBuildings();
        }

        players.stream().sorted((p1, p2) -> p2.getPoint() - p1.getPoint())
                .forEach(player -> System.out.println("Player "
                        + player.getId() + " has " + player.getPoint() + " points and "
                        + player.getResourceByResourceType(ResourceType.GOLD).getQuantity() + " golds"));
    }
}

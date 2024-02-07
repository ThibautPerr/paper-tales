package com.example;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.example.Resource.ResourceType;

public class App {
    public static final boolean PRINT_START_DRAW_CARDS = false;
    public static final boolean PRINT_END_DRAW_CARDS = false;
    public static final boolean PRINT_START_BOARDS_REORGANISATION = false;
    public static final boolean PRINT_END_BOARDS_REORGANISATION = false;
    public static final boolean PRINT_START_PLAY_CARDS = false;
    public static final boolean PRINT_END_PLAY_CARDS = true;
    public static final boolean PRINT_START_DISCARD_CARDS = false;
    public static final boolean PRINT_END_DISCARD_CARDS = true;
    public static final boolean PRINT_START_WAR = false;
    public static final boolean PRINT_END_WAR = true;
    public static final boolean PRINT_START_GIVE_GOLD = false;
    public static final boolean PRINT_END_GIVE_GOLD = false;
    public static final boolean PRINT_START_BUILD_BUILDINGS = false;
    public static final boolean PRINT_END_BUILD_BUILDINGS = true;
    public static final boolean PRINT_START_DIING_AGING = false;
    public static final boolean PRINT_END_DIING_AGING = true;

    public static void main(String[] args) {
        // Setup the game
        List<Card> deck = createCards();
        Collections.shuffle(deck);
        List<Card> discardPile = new ArrayList<Card>();
        boolean[] realPlayers = { true, false, false, false };
        List<Player> players = createPlayers(4, realPlayers);
        List<Building> buildings = createBuildings();
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
            drawCards(players, deck);

            // Phase 2 : Each player reoarganises his board
            boardsReorganisation(players);
            // Then play as many cards as he wants and pay the cost
            playCards(players);

            // Cards are revealed

            // Each player can only keep one card in hand
            discardCards(players, discardPile);

            // Phase 3 : For each player, if he has more attack than his neighbour, he gains
            // 3 points
            war(players);

            // Phase 4 : Each player gains 2 golds
            giveGold(players);

            // Phase 5 : Each player can build one building, if he has the requirements and
            // has built the previous phase of the building
            buildBuildings(players);

            // Phase 6 : Each units with a moon counter dies
            // Then each units gains a moon counter
            diingAndAging(players, discardPile);

        }

        // Trigger end game effects

        // Add buildings points to the players points

        // The player with the most points wins
        // If there is a tie, the player with the most gold wins
        // If there is still a tie, the players share the victory

    }

    public static List<Card> createCards() {
        JSONParser parser = new JSONParser();
        List<Card> cards = new ArrayList<Card>();

        try (FileReader reader = new FileReader("src/main/resources/cards.json")) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            cards = readCardsFromJSON(jsonArray);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return cards;
    }

    public static List<Card> readCardsFromJSON(JSONArray jsonCards) {
        List<Card> cards = new ArrayList<Card>();
        int id = 0;
        for (Object item : jsonCards) {
            JSONObject jsonObject = (JSONObject) item;

            for (int i = 0; i < ((Long) jsonObject.get("copy")).intValue(); i++) {
                Card card = new Card(
                        id,
                        (String) jsonObject.get("name"),
                        ((Long) jsonObject.get("cost")).intValue(),
                        ((Long) jsonObject.get("attack")).intValue());
                cards.add(card);
                id++;
            }
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
            buildings = readBuildingsFromJSON(jsonArray);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return buildings;
    }

    public static List<Building> readBuildingsFromJSON(JSONArray jsonBuildings) {
        List<Building> buildings = new ArrayList<Building>();
        for (Object item : jsonBuildings) {
            JSONObject jsonObject = (JSONObject) item;

            // Build the building phases
            JSONArray buildingPhasesArray = (JSONArray) jsonObject.get("buildingPhases");
            List<BuildingPhase> buildingPhases = readBuildingPhasesFromJSON(buildingPhasesArray);

            Building building = new Building(
                    ((Long) jsonObject.get("id")).intValue(),
                    (String) jsonObject.get("name"),
                    0,
                    buildingPhases);
            buildings.add(building);
        }

        return buildings;
    }

    public static List<BuildingPhase> readBuildingPhasesFromJSON(JSONArray buildingPhasesArray) {
        List<BuildingPhase> buildingPhases = new ArrayList<BuildingPhase>();
        for (Object buildingPhaseItem : buildingPhasesArray) {
            JSONObject buildingPhaseObject = (JSONObject) buildingPhaseItem;

            JSONArray requirementsArray = (JSONArray) buildingPhaseObject.get("requirements");
            List<Resource> requirements = null;
            if (requirementsArray != null)
                requirements = readResourcesFromJSON(requirementsArray);

            JSONArray optionnalRequirementsArray = (JSONArray) buildingPhaseObject.get("optionnalRequirements");
            List<Resource> optionnalRequirements = null;
            if (optionnalRequirementsArray != null)
                optionnalRequirements = readResourcesFromJSON(optionnalRequirementsArray);

            BuildingPhase buildingPhase = new BuildingPhase(
                    ((Long) buildingPhaseObject.get("phase")).intValue(),
                    requirements,
                    optionnalRequirements,
                    ((Long) buildingPhaseObject.get("point")).intValue());

            buildingPhases.add(buildingPhase);
        }
        return buildingPhases;
    }

    public static List<Resource> readResourcesFromJSON(JSONArray resourcesArray) {
        List<Resource> resources = new ArrayList<Resource>();
        for (Object resourceItem : resourcesArray) {
            JSONObject resourceObject = (JSONObject) resourceItem;
            Resource resource = new Resource(
                    ResourceType.valueOf((String) resourceObject.get("resourceType")),
                    ((Long) resourceObject.get("quantity")).intValue());
            resources.add(resource);
        }
        return resources;
    }

    public static void drawCards(List<Player> players, List<Card> deck) {
        if (PRINT_START_DRAW_CARDS)
            System.out.println("\n--------------- Start drawCards ---------------");

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

        if (PRINT_END_DRAW_CARDS) {
            for (Player player : players) {
                System.out.print("Player " + player.getId() + " hand: ");
                for (Card card : player.getHand()) {
                    System.out.print(card.getName() + ", ");
                }
                System.out.println();
            }
            System.out.println("--------------- End drawCards ---------------");
        }
    }

    public static void drawCard(Player player, List<Card> cards) {
        // TODO Create a pick method
        player.addCardToHand(cards.get(0));
        cards.remove(0);
    }

    public static void boardsReorganisation(List<Player> players) {
        if (PRINT_START_BOARDS_REORGANISATION)
            System.out.println("\n--------------- Start boardsReorganisation ---------------");

        for (Player player : players) {
            if (PRINT_START_BOARDS_REORGANISATION) {
                System.out.println("Player " + player.getId() + " starting board: ");
                player.getBoard().printBoard();
            }

            // TODO Create a reorganisation method
            // List<Card> newFrontCard = Card.deepCopyCards(player.getBoard().getFrontCards());
            // for (int i = 0; i < newFrontCard.size(); i++) {
            //     player.getBoard().removeFrontCardById(newFrontCard.get(0).getId());
            // }

            // List<Card> newBackCard = Card.deepCopyCards(player.getBoard().getBackCards());
            // for (int i = 0; i < newBackCard.size(); i++) {
            //     player.getBoard().removeBackCardById(newBackCard.get(0).getId());
            // }

            if (PRINT_END_BOARDS_REORGANISATION) {
                System.out.println("Player " + player.getId() + " ending board: ");
                player.getBoard().printBoard();
            }
        }
        if (PRINT_END_BOARDS_REORGANISATION)
            System.out.println("--------------- End boardsReorganisation ---------------");
    }

    public static void playCards(List<Player> players) {
        if (PRINT_START_PLAY_CARDS)
            System.out.println("\n--------------- Start playCards ---------------");

        for (Player player : players) {
            if (PRINT_START_PLAY_CARDS) {
                System.out.print("Player " + player.getId() + " starting hand: ");
                for (Card card : player.getHand()) {
                    System.out.print(card.getName() + ", ");
                }
                System.out.println();
                player.getBoard().printBoard();
            }

            // TODO Create a play method
            // Play card if there is space on the front board
            List<Card> newHand = Card.deepCopyCards(player.getHand());
            newHand.sort((c1, c2) -> c2.getAttack() - c1.getAttack());
            for (Card cardToPlay : newHand) {
                if (cardToPlay.getCost() <= player.getResourceByType(ResourceType.GOLD).getQuantity()
                        && player.getBoard().getFrontCards().size() < player.getBoard().getMaxFrontCards()) {
                    player.playCardById(cardToPlay.getId(), true);
                }
            }

            // Play card if there is space on the back board
            newHand = Card.deepCopyCards(player.getHand());
            for (Card cardToPlay : newHand) {
                if (cardToPlay.getCost() <= player.getResourceByType(ResourceType.GOLD).getQuantity()
                        && player.getBoard().getBackCards().size() < player.getBoard().getMaxBackCards()) {
                    player.playCardById(cardToPlay.getId(), false);
                }
            }

            if (PRINT_END_PLAY_CARDS) {
                System.out.print("Player " + player.getId() + " ending hand: ");
                for (Card card : player.getHand()) {
                    System.out.print(card.getName() + ", ");
                }
                System.out.println();
                System.out.println("Player " + player.getId() + " ending board: ");
                player.getBoard().printBoard();
            }
        }
        if (PRINT_END_PLAY_CARDS)
            System.out.println("--------------- End playCards ---------------");
    }

    public static void discardCards(List<Player> players, List<Card> discardPile) {
        if (PRINT_START_DISCARD_CARDS)
            System.out.println("\n--------------- Start discardCards ---------------");

        for (Player player : players) {
            if (PRINT_START_DISCARD_CARDS) {
                System.out.print("Player " + player.getId() + " starting hand: ");
                for (Card card : player.getHand()) {
                    System.out.print(card.getName() + ", ");
                }
                System.out.println();
            }

            // TODO Create a discard method
            if (player.getHand().size() > 1) {
                for (int i = 1; i < player.getHand().size(); i++) {
                    discardPile.add(player.getHand().get(i));
                }
                player.setHand(player.getHand().subList(0, 1));
            }

            if (PRINT_END_DISCARD_CARDS) {
                System.out.print("Player " + player.getId() + " ending hand: ");
                for (Card card : player.getHand()) {
                    System.out.print(card.getName() + ", ");
                }
                System.out.println();
            }
        }
        if (PRINT_END_DISCARD_CARDS)
            System.out.println("--------------- End discardCards ---------------");
    }

    public static void war(List<Player> players) {
        if (PRINT_START_WAR)
            System.out.println("\n--------------- Start war ---------------");

        for (Player player : players) {
            if (PRINT_START_WAR) {
                System.out.println("Player " + player.getId() + " starting board: ");
                player.getBoard().printBoard();
            }

            int warPoints = 0;
            for (Card frontCard : player.getBoard().getFrontCards()) {
                warPoints += frontCard.getAttack();
            }
            player.setWarPoint(warPoints);
        }

        if (PRINT_END_WAR) {
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

        if (PRINT_END_WAR) {
            for (Player player : players) {
                System.out.println("Player " + player.getId() + " points : " + player.getPoint());
            }
            System.out.println("--------------- End war ---------------");
        }
    }

    public static void giveGold(List<Player> players) {
        if (PRINT_START_GIVE_GOLD)
            System.out.println("\n--------------- Start giveGold ---------------");

        if (PRINT_START_GIVE_GOLD)
            for (Player player : players)
                System.out.println("Player " + player.getId() + " gold: "
                        + player.getResourceByType(ResourceType.GOLD).getQuantity());

        for (Player player : players) {
            player.setResource(ResourceType.GOLD,
                    player.getResourceByType(ResourceType.GOLD).getQuantity() + 2);
        }

        if (PRINT_END_GIVE_GOLD)
            for (Player player : players)
                System.out.println("Player " + player.getId() + " gold: "
                        + player.getResourceByType(ResourceType.GOLD).getQuantity());

        if (PRINT_END_GIVE_GOLD)
            System.out.println("--------------- End giveGold ---------------");
    }

    public static void buildBuildings(List<Player> players) {
        if (PRINT_START_BUILD_BUILDINGS)
            System.out.println("\n--------------- Start buildBuildings ---------------");

        if (PRINT_START_BUILD_BUILDINGS)
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

        if (PRINT_END_BUILD_BUILDINGS)
            for (Player player : players) {
                player.printBuiltBuildings();
                System.out.println("Player " + player.getId() + " gold: "
                        + player.getResourceByType(ResourceType.GOLD).getQuantity());
            }

        if (PRINT_END_BUILD_BUILDINGS)
            System.out.println("--------------- End buildBuildings ---------------");
    }

    public static void diingAndAging(List<Player> players, List<Card> discardPile) {
        if (PRINT_START_DIING_AGING)
            System.out.println("\n--------------- Start diingAndAging ---------------");
        for (Player player : players) {
            if (PRINT_START_DIING_AGING) {
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

            if (PRINT_END_DIING_AGING) {
                System.out.println("Player " + player.getId() + " ending board: ");
                player.getBoard().printBoard();
            }
        }
        if (PRINT_END_DIING_AGING)
            System.out.println("--------------- End diingAndAging ---------------");
    }

}

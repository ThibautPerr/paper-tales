package com.example;

import java.util.ArrayList;
import java.util.List;

import com.example.Resource.ResourceType;

public class Player {
    private int id;
    private boolean realPlayer;

    private List<Card> hand;
    private Board board;

    private List<Building> buildings;

    private List<Resource> resources;

    private int warPoint;

    private int point;

    public Player(int id, boolean realPlayer) {
        this.id = id;
        this.realPlayer = realPlayer;
        this.hand = new ArrayList<Card>();
        this.board = new Board();
        this.buildings = new ArrayList<Building>();
        this.resources = new ArrayList<Resource>();
        for (Resource.ResourceType type : Resource.ResourceType.values()) {
            this.resources.add(new Resource(type, 0));
        }
        this.warPoint = 0;
        this.point = 0;
    }

    public int getId() {
        return id;
    }

    public boolean isRealPlayer() {
        return realPlayer;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public Board getBoard() {
        return board;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        for (Building building : buildings)
            this.buildings.add(new Building(building));
    }

    public List<BuildingPhase> getBuildableBuildingPhases() {
        List<BuildingPhase> buildableBuildingPhases = new ArrayList<BuildingPhase>();
        for (Building building : buildings) {
            BuildingPhase nextBuildingPhase = building.getNextBuildingPhase();
            if (nextBuildingPhase != null) {
                buildableBuildingPhases.add(nextBuildingPhase);
            }
        }
        return buildableBuildingPhases;
    }

    public int builtWithGold(BuildingPhase buildingPhase) {
        for (Resource resource : buildingPhase.getRequirements()) {
            if (resource.getType() != ResourceType.GOLD
                    && getResourceByType(resource.getType()).getQuantity() >= resource.getQuantity()) {
                        return 0;
            }
        }

        for (Resource resource : buildingPhase.getOptionnalRequirements()) {
            if (resource.getType() != ResourceType.GOLD
                    && getResourceByType(resource.getType()).getQuantity() >= resource.getQuantity()) {
                return 0;
            }
        }

        for (Resource resource : buildingPhase.getRequirements()) {
            if (resource.getType() == ResourceType.GOLD) {
                return resource.getQuantity();
            }
        }

        for (Resource resource : buildingPhase.getOptionnalRequirements()) {
            if (resource.getType() == ResourceType.GOLD) {
                return resource.getQuantity();
            }
        }

        return 0;
    }

    public void buildBuildingPhase(BuildingPhase newBuildingPhase, int usedGold) {
        for (Building building : this.buildings) {
            for (BuildingPhase buildingPhase : building.getBuildingPhases()) {
                if (buildingPhase == newBuildingPhase) {
                    if (usedGold > 0) {
                        setResource(ResourceType.GOLD,
                                this.resources.get(ResourceType.GOLD.ordinal()).getQuantity() - usedGold);
                    }
                    building.setCurrentBuildingPhase(newBuildingPhase.getPhase());
                }
            }
        }
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResource(Resource.ResourceType resourceType, int quantity) {
        this.resources.get(resourceType.ordinal()).setQuantity(quantity);
    }

    public boolean hasEnoughResources(List<Resource> requirements) {
        if (requirements == null)
            return false;
        for (Resource requirement : requirements) {
            int requiredQuantity = requirement.getQuantity();
            Resource playerResource = getResourceByType(requirement.getType());

            if (playerResource.getQuantity() < requiredQuantity)
                return false;
        }
        return true;
    }

    public Resource getResourceByType(Resource.ResourceType type) {
        for (Resource resource : this.resources) {
            if (resource.getType() == type) {
                return resource;
            }
        }
        return null;
    }

    public int getWarPoint() {
        return warPoint;
    }

    public void setWarPoint(int warPoint) {
        this.warPoint = warPoint;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public Card getCardInHandById(int idCard) {
        for (Card card : this.hand) {
            if (card.getId() == idCard) {
                return card;
            }
        }
        return null;
    }

    public void addCardToHand(Card card) {
        this.hand.add(card);
    }

    public void playCardById(int idCard, boolean front) {
        Card card = this.getCardInHandById(idCard);
        Card newCard = new Card(card);
        if (front)
            this.board.addFrontCard(newCard);
        else
            this.board.addBackCard(newCard);
        this.hand.remove(card);
        setResource(ResourceType.GOLD,
                this.resources.get(ResourceType.GOLD.ordinal()).getQuantity() - card.getCost());
    }

    public void printPlayer() {
        System.out.println("Player: " + id);
        System.out.println("Gold: " + getResourceByType(ResourceType.GOLD).getQuantity());
        System.out.println("Point: " + point);

        System.out.println("Hand: ");
        if (hand != null)
            for (Card card : hand)
                card.printCard();
        else
            System.out.println("Empty");

        System.out.println("Board: ");

        System.out.println("Front: ");
        if (board.getFrontCards() != null)
            for (Card card : board.getFrontCards())
                card.printCard();
        else
            System.out.println("Empty");

        System.out.println("Back: ");
        if (board.getBackCards() != null)
            for (Card card : board.getBackCards())
                card.printCard();
        else
            System.out.println("Empty");

        System.out.println("Buildings: ");
        if (buildings != null)
            for (Building building : buildings)
                building.printBuilding();
        else
            System.out.println("Empty");

        System.out.println("Resources: ");
        if (resources != null)
            for (Resource resource : resources)
                System.out.println(resource);
        else
            System.out.println("Empty");
        System.out.println("\n");
    }

    public void printResources() {
        System.out.print("Player " + this.id + " resources: ");
        for (Resource resource : resources) {
            resource.printResource();
            System.out.print(", ");
        }
        System.out.println();
    }

    public void printBuildings() {
        System.out.print("Player " + this.id + " buildings: ");
        for (Building building : buildings) {
            building.printBuilding();
        }
    }

    public void printBuiltBuildings() {
        System.out.print("Player " + this.id + " built buildings: ");
        for (Building building : buildings) {
            if (building.getCurrentBuildingPhase() != 0)
                System.out.print(building.getName() + "(" + building.getCurrentBuildingPhase() + "), ");
        }
        System.out.println();
    }
}

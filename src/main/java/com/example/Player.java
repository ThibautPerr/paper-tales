package com.example;

import java.util.ArrayList;
import java.util.List;

import com.example.Resource.ResourceType;
import com.example.effects.Effect;
import com.example.effects.OnPhase3;
import com.example.effects.OnPhase4;
import com.example.effects.OnPlayed;

public class Player {
    private int id;
    private boolean realPlayer;

    private List<Card> hand;
    private Board board;

    private List<Building> buildings;

    private List<Resource> resources;

    private List<Effect> effects;

    private int warPoint;

    private int point;

    public Player(int id, boolean realPlayer) {
        this.id = id;
        this.realPlayer = realPlayer;
        this.hand = new ArrayList<Card>();
        this.board = new Board();
        this.buildings = new ArrayList<Building>();
        this.resources = new ArrayList<Resource>();
        for (Resource.ResourceType resourceType : Resource.ResourceType.values()) {
            this.resources.add(new Resource(resourceType, 0));
        }
        this.effects = new ArrayList<Effect>();
        this.warPoint = 0;
        this.point = 0;
    }

    public int getId() {
        return this.id;
    }

    public boolean isRealPlayer() {
        return this.realPlayer;
    }

    public List<Card> getHand() {
        return this.hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public Board getBoard() {
        return this.board;
    }

    public List<Building> getBuildings() {
        return this.buildings;
    }

    public void setBuildings(List<Building> buildings) {
        for (Building building : buildings)
            this.buildings.add(new Building(building));
    }

    public List<BuildingPhase> getBuildableBuildingPhases() {
        List<BuildingPhase> buildableBuildingPhases = new ArrayList<BuildingPhase>();
        for (Building building : this.buildings) {
            BuildingPhase nextBuildingPhase = building.getNextBuildingPhase();
            if (nextBuildingPhase != null) {
                buildableBuildingPhases.add(nextBuildingPhase);
            }
        }
        return buildableBuildingPhases;
    }

    public int builtWithGold(BuildingPhase buildingPhase) {
        for (Resource resource : buildingPhase.getRequirements()) {
            if (resource.getResourceType() != ResourceType.GOLD
                    && getResourceByResourceType(resource.getResourceType()).getQuantity() >= resource.getQuantity()) {
                return 0;
            }
        }

        for (Resource resource : buildingPhase.getOptionnalRequirements()) {
            if (resource.getResourceType() != ResourceType.GOLD
                    && getResourceByResourceType(resource.getResourceType()).getQuantity() >= resource.getQuantity()) {
                return 0;
            }
        }

        for (Resource resource : buildingPhase.getRequirements()) {
            if (resource.getResourceType() == ResourceType.GOLD) {
                return resource.getQuantity();
            }
        }

        for (Resource resource : buildingPhase.getOptionnalRequirements()) {
            if (resource.getResourceType() == ResourceType.GOLD) {
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
                    if (newBuildingPhase.getEffect() instanceof OnPlayed) {
                        OnPlayed onPlayedEffect = (OnPlayed) newBuildingPhase.getEffect();
                        onPlayedEffect.playEffect(this);
                    } else
                        this.effects.add(newBuildingPhase.getEffect());
                }
            }
        }
    }

    public List<Resource> getResources() {
        return this.resources;
    }

    public void setResource(Resource.ResourceType resourceType, int quantity) {
        this.resources.get(resourceType.ordinal()).setQuantity(quantity);
    }

    public void addResource(Resource resource) {
        Resource playerResource = getResourceByResourceType(resource.getResourceType());
        playerResource.setQuantity(playerResource.getQuantity() + resource.getQuantity());
    }

    public void addResources(List<Resource> resources) {
        for (Resource resource : resources) {
            addResource(resource);
        }
    }

    public void addResourcePerResource(Resource addResource, Resource perResource) {
        Resource playerResource = getResourceByResourceType(addResource.getResourceType());
        int perResourceQuantity = getResourceByResourceType(perResource.getResourceType()).getQuantity();
        playerResource.setQuantity(playerResource.getQuantity()
                + (perResourceQuantity / perResource.getQuantity()) * addResource.getQuantity());
    }

    public void removeResource(Resource resource) {
        Resource playerResource = getResourceByResourceType(resource.getResourceType());
        playerResource.setQuantity(playerResource.getQuantity() - resource.getQuantity());
    }

    public void removeResources(List<Resource> resources) {
        for (Resource resource : resources) {
            removeResource(resource);
        }
    }

    public boolean hasEnoughResources(List<Resource> requirements) {
        if (requirements == null)
            return false;
        for (Resource requirement : requirements) {
            int requiredQuantity = requirement.getQuantity();
            Resource playerResource = getResourceByResourceType(requirement.getResourceType());

            if (playerResource.getQuantity() < requiredQuantity)
                return false;
        }
        return true;
    }

    public Resource getResourceByResourceType(Resource.ResourceType resourceType) {
        for (Resource resource : this.resources) {
            if (resource.getResourceType() == resourceType) {
                return resource;
            }
        }
        return null;
    }

    public List<Effect> getEffects() {
        return this.effects;
    }

    public void addEffect(Effect effect) {
        this.effects.add(effect);
    }

    public void playPhase3Effects() {
        for (Effect effect : this.effects) {
            if (effect instanceof OnPhase3) {
                OnPhase3 onPhase3Effect = (OnPhase3) effect;
                onPhase3Effect.playEffect(this);
            }
        }
    }

    public void playPhase4Effects() {
        for (Effect effect : this.effects) {
            if (effect instanceof OnPhase4) {
                OnPhase4 onPhase4Effect = (OnPhase4) effect;
                onPhase4Effect.playEffect(this);
            }
        }
    }

    public int getWarPoint() {
        return this.warPoint;
    }

    public void setWarPoint(int warPoint) {
        this.warPoint = warPoint;
    }

    public void addWarPoint(int warPoint) {
        this.warPoint += warPoint;
    }

    public void addWarPointPerResource(int warPoint, Resource resource) {
        this.warPoint += warPoint
                * (getResourceByResourceType(resource.getResourceType()).getQuantity() / resource.getQuantity());
    }

    public int getPoint() {
        return this.point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void addPoint(int point) {
        this.point += point;
    }

    public void addPointPerResource(int point, Resource resource) {
        this.point += point
                * (getResourceByResourceType(resource.getResourceType()).getQuantity() / resource.getQuantity());
    }

    public void addPointPerUnitWithMinAttack(int point, int minAttack) {
        for (Card card : this.board.getFrontCards()) {
            if (card.getAttack() >= minAttack) {
                this.point += point;
            }
        }
        for (Card card : this.board.getBackCards()) {
            if (card.getAttack() >= minAttack) {
                this.point += point;
            }
        }
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
        System.out.println("Player: " + this.id);
        System.out.println("Gold: " + getResourceByResourceType(ResourceType.GOLD).getQuantity());
        System.out.println("Point: " + this.point);

        System.out.println("Hand: ");
        if (this.hand != null)
            for (Card card : this.hand)
                card.printCard();
        else
            System.out.println("Empty");

        System.out.println("Board: ");

        System.out.println("Front: ");
        if (this.board.getFrontCards() != null)
            for (Card card : this.board.getFrontCards())
                card.printCard();
        else
            System.out.println("Empty");

        System.out.println("Back: ");
        if (this.board.getBackCards() != null)
            for (Card card : board.getBackCards())
                card.printCard();
        else
            System.out.println("Empty");

        System.out.println("Buildings: ");
        if (this.buildings != null)
            for (Building building : this.buildings)
                building.printBuilding();
        else
            System.out.println("Empty");

        System.out.println("Resources: ");
        if (this.resources != null)
            for (Resource resource : this.resources)
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
        for (Building building : this.buildings) {
            building.printBuilding();
        }
    }

    public void printBuiltBuildings() {
        System.out.print("Player " + this.id + " built buildings: ");
        for (Building building : this.buildings) {
            if (building.getCurrentBuildingPhase() != 0)
                System.out.print(building.getName() + "(" + building.getCurrentBuildingPhase() + "), ");
        }
        System.out.println();
    }
}

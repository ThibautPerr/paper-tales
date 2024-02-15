package com.example;

import java.util.ArrayList;
import java.util.List;

import com.example.Resource.ResourceType;
import com.example.effects.Effect;
import com.example.effects.OnDeath;
import com.example.effects.OnEndGame;
import com.example.effects.OnEndPhase3;
import com.example.effects.OnFlip;
import com.example.effects.OnPhase2;
import com.example.effects.OnPhase3;
import com.example.effects.OnPhase4;
import com.example.effects.OnPhase5;
import com.example.effects.OnPhase6;
import com.example.effects.OnPhase6Death;
import com.example.effects.OnUpdate;
import com.example.strategy.Strategy;

public class Player {
    private int id;
    private Strategy strategy;

    private List<Card> hand;
    private Board board;

    private boolean ignoreBuildingCost;
    private List<Building> buildings;

    private int goldToAddInPhase4;
    private List<Resource> resources;

    private List<Effect> effects;

    private int warWon;
    private int warPoint;

    private int point;

    public Player(int id, Strategy strategy) {
        this.id = id;
        this.strategy = strategy;
        this.hand = new ArrayList<Card>();
        this.board = new Board();
        this.ignoreBuildingCost = false;
        this.buildings = new ArrayList<Building>();
        this.goldToAddInPhase4 = 2;
        this.resources = new ArrayList<Resource>();
        for (Resource.ResourceType resourceType : Resource.ResourceType.values()) {
            this.resources.add(new Resource(resourceType, 0));
        }
        this.effects = new ArrayList<Effect>();
        this.warWon = 0;
        this.warPoint = 0;
        this.point = 0;
    }

    public int getId() {
        return this.id;
    }

    public Strategy getStrategy() {
        return this.strategy;
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

    public boolean isIgnoreBuildingCost() {
        return this.ignoreBuildingCost;
    }

    public void setIgnoreBuildingCost(boolean ignoreBuildingCost) {
        this.ignoreBuildingCost = ignoreBuildingCost;
    }

    public List<Building> getBuildings() {
        return this.buildings;
    }

    public void setBuildings(List<Building> buildings) {
        for (Building building : buildings)
            this.buildings.add(new Building(building));
    }

    public List<BuildingPhase> getBuildablePhases() {
        List<BuildingPhase> buildableBuildingPhases = new ArrayList<BuildingPhase>();
        for (Building building : this.buildings) {
            BuildingPhase nextBuildingPhase = building.getNextBuildingPhase();
            if (nextBuildingPhase != null &&
                    (hasEnoughResources(nextBuildingPhase.getRequirements())
                            || hasEnoughResources(nextBuildingPhase.getOptionnalRequirements())
                            || isIgnoreBuildingCost())) {
                buildableBuildingPhases.add(nextBuildingPhase);
            }
        }
        return buildableBuildingPhases;
    }

    public int builtWithGold(BuildingPhase buildingPhase) {
        if (this.ignoreBuildingCost)
            return 0;
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
                    if (newBuildingPhase.getPhase() == 2 && this.board.getMaxFrontCards() == 2) {
                        this.board.setMaxFrontCards(3);
                    }
                    for (Effect effect : newBuildingPhase.getEffects())
                        if (effect instanceof OnFlip) {
                            OnFlip onPlayedEffect = (OnFlip) effect;
                            onPlayedEffect.playEffect(this);
                        } else
                            this.effects.add(effect);
                }
            }
        }
    }

    public int getGoldToAddInPhase4() {
        return this.goldToAddInPhase4;
    }

    public void setGoldToAddInPhase4(int goldToAddInPhase4) {
        this.goldToAddInPhase4 = goldToAddInPhase4;
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

    public void addGoldInPhase4(Resource resource) {
        this.goldToAddInPhase4 += resource.getQuantity();
    }

    public void addResourcePerResource(Resource addResource, Resource perResource) {
        Resource playerResource = getResourceByResourceType(addResource.getResourceType());
        int perResourceQuantity = getResourceByResourceType(perResource.getResourceType()).getQuantity();
        playerResource.setQuantity(playerResource.getQuantity()
                + (perResourceQuantity / perResource.getQuantity()) * addResource.getQuantity());
    }

    public void addGoldPerResourceInPhase4(Resource addResource, Resource perResource) {
        this.goldToAddInPhase4 += (getResourceByResourceType(perResource.getResourceType()).getQuantity()
                / perResource.getQuantity()) * addResource.getQuantity();
    }

    public void addResourcePerMoon(Resource resource, int moon) {
        Resource playerResource = getResourceByResourceType(resource.getResourceType());
        playerResource.setQuantity(playerResource.getQuantity() + resource.getQuantity() * moon);
    }

    public void addResourcePerAtLeastMoon(int idCard, int moon, Resource resource, boolean onUpdate) {
        if (onUpdate || getCardInBoardById(idCard).getMoon() >= moon)
            addResource(resource);
    }

    public void checkAddResourcePerAtLeastMoon(int idCard, int moon) {
        List<OnUpdate> addResourcePerAtLeastMoonEffects = this.effects.stream()
                .filter(effect -> effect instanceof OnUpdate)
                .map(effect -> (OnUpdate) effect)
                .filter(effect -> effect.getFunction().equals("addResourcePerAtLeastMoon")
                        && idCard == effect.getIdCard())
                .toList();
        if (addResourcePerAtLeastMoonEffects.size() > 0)
            for (OnUpdate effect : addResourcePerAtLeastMoonEffects) {
                Card card = getCardInBoardById(idCard);
                if (card.getMoon() < effect.getMoon() && card.getMoon() + moon >= effect.getMoon())
                    effect.addResourcePerAtLeastMoon(this);
            }
    }

    public void addGoldPerAtLeastMoon(int idCard, int moon, Resource resource) {
        if (getCardInBoardById(idCard).getMoon() >= moon)
            addGoldInPhase4(resource);
    }

    public void addResourceIfFrontUnit(int idCard, Resource resource) {
        if (this.board.isPresentFrontCard(idCard)) {
            addResource(resource);
        }
    }

    public void addResourcePerWarWon(Resource resource) {
        for (int i = 0; i < this.warWon; i++)
            addResource(resource);
    }

    public void payGoldInsteadOfResource(int gold, Resource resource, int nb) {
        Resource playerResource = getResourceByResourceType(resource.getResourceType());
        playerResource.setQuantity(playerResource.getQuantity() + (gold * nb));
        setResource(ResourceType.GOLD, getResourceByResourceType(ResourceType.GOLD).getQuantity() - (gold * nb));
    }

    public void ignoreBuildingCost() {
        setIgnoreBuildingCost(true);
    }

    public List<Resource> payResources(BuildingPhase buildingPhase) {
        if (this.effects.stream().filter(effect -> effect instanceof OnPhase5).map(effect -> (OnPhase5) effect)
                .anyMatch(effect -> effect.getFunction().equals("payGoldInsteadOfResource")))
            return null;
        else if (this.effects.stream().filter(effect -> effect instanceof OnPhase5).map(effect -> (OnPhase5) effect)
                .anyMatch(effect -> effect.getFunction().equals("payGoldInsteadOfResources")))
            return null;
        else
            return null;
    }

    public void deletePaidResources(List<Resource> paidResources) {
        if (paidResources != null)
            for (Resource paidResource : paidResources)
                removeResource(paidResource);
    }

    public void updateResourcePerMoon(Resource resource, int moon, int moonToAdd) {
        Resource playerResource = getResourceByResourceType(resource.getResourceType());
        playerResource.setQuantity(playerResource.getQuantity() + resource.getQuantity() * (moonToAdd / moon));
    }

    public void checkUpdateResourcePerMoon(int idCard, int moon) {
        List<OnUpdate> updateResourcePerMoonEffects = this.effects.stream().filter(effect -> effect instanceof OnUpdate)
                .map(effect -> (OnUpdate) effect)
                .filter(effect -> effect.getFunction().equals("updateResourcePerMoon") && idCard == effect.getIdCard())
                .toList();
        if (updateResourcePerMoonEffects.size() > 0)
            for (OnUpdate effect : updateResourcePerMoonEffects)
                effect.updateResourcePerMoon(this, moon);
    }

    public void removeResource(Resource resource) {
        Resource playerResource = getResourceByResourceType(resource.getResourceType());
        playerResource.setQuantity(playerResource.getQuantity() - resource.getQuantity());
    }

    public void removeResourcePerMoon(int idCard, Resource resource, int moon) {
        Resource playerResource = getResourceByResourceType(resource.getResourceType());
        playerResource.setQuantity(playerResource.getQuantity()
                - (resource.getQuantity() * (getCardInBoardById(idCard).getMoon() / moon)));
    }

    public void removeResourcePerAtLeastMoon(int idCard, int moon, Resource resource) {
        if (getCardInBoardById(idCard).getMoon() >= moon)
            removeResource(resource);
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

    public void addMoon(int idCard, int moon) {
        Card card = getCardInBoardById(idCard);
        checkUpdateResourcePerMoon(idCard, moon);
        checkAddResourcePerAtLeastMoon(idCard, moon);

        card.setMoon(card.getMoon() + moon);
        checkCannotFightIfAtLeastMoon(idCard);
    }

    public void addMoonOnAnotherUnit(int idCard, int moon) {
        addMoon(idCard, moon);
    }

    public void addMoonPerResourceOnAnotherUnit(int idCard, Resource resource, int moon) {
        addMoon(idCard,
                (getResourceByResourceType(resource.getResourceType()).getQuantity() / resource.getQuantity())
                        * moon);
    }

    public void addMoonToAllOtherUnits(int moon, int idCard) {
        List<Card> cards = this.board.getCards().stream().filter(card -> card.getId() != idCard)
                .toList();
        if (cards.size() > 0)
            for (Card card : cards)
                addMoon(card.getId(), moon);
    }

    public void upgradeBuildingsInPhase(int phase) {
        for (Building building : this.buildings) {
            if (building.getCurrentBuildingPhase() == phase) {
                buildBuildingPhase(building.getNextBuildingPhase(), 0);
            }
        }
    }

    public void changeform(int idChangeform, Card card) {
        Card changeformCard = getCardInBoardById(idChangeform);
        if (this.board.isPresentBackCard(idChangeform)) {
            this.board.removeBackCard(changeformCard);
            this.removeEffectsByCardId(idChangeform);
            this.board.addBackCard(card);
            this.playOnFlipEffect(card, null);
        } else if (this.board.isPresentFrontCard(idChangeform)) {
            this.board.removeFrontCard(changeformCard);
            this.removeEffectsByCardId(idChangeform);
            this.board.addFrontCard(card);
            this.playOnFlipEffect(card, null);
        } else
            System.out.println("Changeform : card not found");
    }

    public int getWarWon() {
        return this.warWon;
    }

    public void setWarWon(int warWon) {
        this.warWon = warWon;
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

    public void addWarpointPerMoon(int idCard, int warPoint, int moon) {
        if (this.board.isPresentFrontCard(idCard) || getCardInBoardById(idCard).canFightFromBehind())
            this.warPoint += (getCardInBoardById(idCard).getMoon() * warPoint * moon);
    }

    public void addWarpointPerMoonOnEveryUnit(int warPoint, int moon) {
        for (Card card : this.board.getCards()) {
            this.warPoint += (card.getMoon() * warPoint * moon);
        }
    }

    public void addWarpointPerAtLeastMoon(int idCard, int warPoint, int moon) {
        if (this.board.isPresentFrontCard(idCard) || getCardInBoardById(idCard).canFightFromBehind())
            if (getCardInBoardById(idCard).getMoon() >= moon)
                this.warPoint += warPoint;
    }

    public void addWarpointPerResource(int idCard, int warPoint, Resource resource) {
        if (idCard == 0 || ((this.board.isPresentFrontCard(idCard)
                || getCardInBoardById(idCard).canFightFromBehind()) && getCardInBoardById(idCard).canFight()))
            this.warPoint += warPoint
                    * (getResourceByResourceType(resource.getResourceType()).getQuantity() / resource.getQuantity());
    }

    public void addWarpointPerGoldAddedAtNextPhase4(int idCard, int warPoint, Resource resource) {
        if (this.board.isPresentFrontCard(idCard) || getCardInBoardById(idCard).canFightFromBehind()) {
            int oldGoldToAddInPhase4 = this.goldToAddInPhase4;
            for (Effect effect : this.effects) {
                if (effect instanceof OnPhase4) {
                    OnPhase4 onPhase4Effect = (OnPhase4) effect;
                    if ((onPhase4Effect.getResource() != null
                            && onPhase4Effect.getResource().getResourceType() == resource.getResourceType())
                            || (onPhase4Effect.getAddResource() != null
                                    && onPhase4Effect.getAddResource().getResourceType() == resource.getResourceType()))
                        onPhase4Effect.playEffect(this);
                }
            }
            this.warPoint += warPoint * this.goldToAddInPhase4;
            this.goldToAddInPhase4 = oldGoldToAddInPhase4;
        }
    }

    public void addWarpointPerAtLeastResource(int idCard, int warPoint, Resource resource) {
        if (this.board.isPresentFrontCard(idCard) || getCardInBoardById(idCard).canFightFromBehind())
            if (getResourceByResourceType(resource.getResourceType()).getQuantity() >= resource.getQuantity())
                this.warPoint += warPoint;
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

    public void addPointPerWarWon(int point) {
        for (int i = 0; i < this.warWon; i++)
            addPoint(point);
    }

    public void addPointPerMoon(int idCard, int point, int moon) {
        addPoint(getCardInBoardById(idCard).getMoon() * point * moon);
    }

    public void addPointPerMoonOnDyingUnit(int moon, int point) {
        for (Card card : this.board.getCards()) {
            if (card.getMoon() > 0 && !card.isAvoidDeath()) {
                addPoint(card.getMoon() / moon * point);
            }
        }
    }

    public void addPointPerResource(int point, Resource resource) {
        this.point += point
                * (getResourceByResourceType(resource.getResourceType()).getQuantity() / resource.getQuantity());
    }

    public void addPointPerResourcePerWarWon(int point, Resource resource) {
        for (int i = 0; i < this.warWon; i++)
            addPointPerResource(point, resource);
    }

    public void removePointIfOnlyXMoon(int idCard, int point, int moon) {
        if (getCardInBoardById(idCard).getMoon() == moon)
            if (this.point >= point)
                this.point -= point;
            else
                this.point = 0;
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

    public void addPointPerBuildingInPhase(int point, int phase) {
        for (Building building : this.buildings) {
            if (building.getCurrentBuildingPhase() == phase) {
                if (this.point + point >= 0)
                    this.point += point;
                else
                    this.point = 0;
            }
        }
    }

    public void avoidDeath(int idCard) {
        getCardInBoardById(idCard).setAvoidDeath(true);
    }

    public void avoidDeathIfLessThanMoon(int idCard, int moon) {
        Card card = getCardInBoardById(idCard);
        if (card.getMoon() < moon)
            card.setAvoidDeath(true);
    }

    public void avoidDeathForOneUnitWithMoon(Card card) {
        card.setAvoidDeath(true);
    }

    public void canFightFromBehind(int idCard) {
        if (this.board.isPresentBackCard(idCard))
            getCardInBoardById(idCard).setCanFightFromBehind(true);
    }

    public void checkCannotFightIfAtLeastMoon(int idCard) {
        Card card = getCardInBoardById(idCard);
        for (Effect effect : card.getEffects()) {
            if (effect instanceof OnPhase2 && ((OnPhase2) effect).getFunction().equals("cannotFightIfAtLeastMoon"))
                ((OnPhase2) effect).cannotFightIfAtLeastMoon(this);
        }
    }

    public void cannotFightIfAtLeastMoon(int idCard, int moon) {
        Card card = getCardInBoardById(idCard);
        if (card.getMoon() >= moon)
            card.setCanFight(false);
    }

    public Card getCardInHandById(int idCard) {
        for (Card card : this.hand) {
            if (card.getId() == idCard) {
                return card;
            }
        }
        return null;
    }

    public Card getCardInBoardById(int idCard) {
        for (Card card : this.board.getCards()) {
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
        Card card = getCardInHandById(idCard);
        Card newCard = new Card(card);
        newCard.setFlipped(true);
        if (front)
            this.board.addFrontCard(newCard);
        else
            this.board.addBackCard(newCard);
        this.hand.remove(card);
        setResource(ResourceType.GOLD,
                this.resources.get(ResourceType.GOLD.ordinal()).getQuantity() - card.getCost());
    }

    public void discardCards(List<Card> cards) {
        if (cards != null) {
            List<Card> copyCards = new ArrayList<Card>(cards);

            for (Card card : copyCards) {
                playOnDeathEffects(card);

                // particular case for cards with addResourceIfFrontUnit effect
                discardCardsWithAddResourceIfFrontUnit(findCardsWithAddResourceIfFrontUnit(cards));

                removeEffectsByCardId(card.getId());
                this.board.removeCardById(card.getId());
            }
        }
    }

    public void keepCard(Card card, List<Card> discardPile) {
        if (!this.hand.isEmpty()) {
            List<Card> newHand = new ArrayList<Card>(this.hand);
            newHand.stream()
                    .filter(handCard -> card == null || handCard.getId() != card.getId())
                    .forEach(handCard -> {
                        discardPile.add(handCard);
                        this.hand.remove(handCard);
                    });
        }
    }

    public List<Effect> getEffects() {
        return this.effects;
    }

    public void addEffect(Effect effect) {
        this.effects.add(effect);
    }

    public void removeEffect(Effect effect) {
        this.effects.remove(effect);
    }

    public void removeEffectsByCardId(int cardId) {
        List<Effect> effects = this.effects.stream().filter(effect -> effect.getIdCard() == cardId).toList();
        for (Effect effect : effects)
            this.removeEffect(effect);
    }

    public List<Card> findCardsWithAddResourceIfFrontUnit(List<Card> cards) {
        return cards.stream().filter(card -> card.getEffects().stream().anyMatch(effect -> effect instanceof OnUpdate
                && ((OnUpdate) effect).getFunction().equals("addResourceIfFrontUnit"))).toList();
    }

    public void discardCardsWithAddResourceIfFrontUnit(List<Card> cards) {
        if (cards.size() > 0)
            for (Card card : cards)
                discardCardWithAddResourceIfFrontUnit(card);
    }

    public void discardCardWithAddResourceIfFrontUnit(Card card) {
        if (this.board.isPresentFrontCard(card.getId()))
            for (Effect effect : card.getEffects())
                if (effect instanceof OnUpdate
                        && ((OnUpdate) effect).getFunction().equals("removeResourceIfFrontUnit"))
                    ((OnUpdate) effect).removeResourceIfFrontUnit(this);
    }

    public void playOnFlipEffect(Card card, Deck deck) {
        if (card.getEffects().size() > 0) {
            for (Effect effect : card.getEffects()) {
                if (effect instanceof OnFlip) {
                    if (((OnFlip) effect).getFunction().equals("changeform"))
                        ((OnFlip) effect).changeform(this, deck.getFirstCard());
                    else if (((OnFlip) effect).getFunction().equals("addMoonOnAnotherUnit"))
                        ((OnFlip) effect).addMoonOnAnotherUnit(this, this.strategy.chooseUnitToAddMoon(this));
                    else
                        ((OnFlip) effect).playEffect(this);
                } else
                    this.addEffect(effect);

                if (effect instanceof OnUpdate
                        && ((OnUpdate) effect).getFunction().equals("addResourceIfFrontUnit")) {
                    ((OnUpdate) effect).addResourceIfFrontUnit(this);
                }
            }
        }
    }

    public void playPhase2Effects() {
        for (Effect effect : this.effects) {
            if (effect instanceof OnPhase2) {
                if (((OnPhase2) effect).getFunction().equals("addMoonPerResourceOnAnotherUnit"))
                    ((OnPhase2) effect).addMoonPerResourceOnAnotherUnit(this, this.strategy.chooseUnitToAddMoon(this));
                else
                    ((OnPhase2) effect).playEffect(this);
            }
        }
    }

    public void playPhase3Effects() {
        for (Effect effect : this.effects) {
            if (effect instanceof OnPhase3) {
                if (!((OnPhase3) effect).getFunction().equals("payGoldInsteadOfResources"))
                    ((OnPhase3) effect).playEffect(this);
            }
        }
    }

    public void playOnEndPhase3Effects() {
        for (Effect effect : this.effects) {
            if (effect instanceof OnEndPhase3) {
                ((OnEndPhase3) effect).playEffect(this);
            }
        }
    }

    public void playPhase4Effects() {
        for (Effect effect : this.effects) {
            if (effect instanceof OnPhase4) {
                ((OnPhase4) effect).playEffect(this);
            }
        }
    }

    public void playPhase5Effects() {
        for (Effect effect : this.effects) {
            if (effect instanceof OnPhase5) {
                if (!((OnPhase5) effect).getFunction().equals("payGoldInsteadOfResource")
                        && !((OnPhase5) effect).getFunction().equals("payGoldInsteadOfResources"))
                    ((OnPhase5) effect).playEffect(this);
            }
        }
    }

    public void playPhase6Effects() {
        for (Effect effect : this.effects) {
            if (effect instanceof OnPhase6) {
                if (!((OnPhase6) effect).getFunction().equals("avoidDeathForOneUnitWithMoon"))
                    ((OnPhase6) effect).playEffect(this);
            }
        }
    }

    public void playOnPhase6DeathEffects(Card card) {
        for (Effect effect : card.getEffects()) {
            if (effect instanceof OnPhase6Death) {
                ((OnPhase6Death) effect).playEffect(this);
            }
        }
    }

    public void playOnDeathEffects(Card card) {
        for (Effect effect : card.getEffects()) {
            if (effect instanceof OnDeath) {
                ((OnDeath) effect).playEffect(this);
            }
        }
    }

    public void playEndGameEffects() {
        for (Effect effect : this.effects) {
            if (effect instanceof OnEndGame) {
                OnEndGame onEndGameEffect = (OnEndGame) effect;
                onEndGameEffect.playEffect(this);
            }
        }
    }

    public void playAvoidDeathForOneUnitWithMoon() {
        List<OnPhase6> avoidDeathForOneUnitWithMoonEffects = this.effects.stream()
                .filter(effect -> effect instanceof OnPhase6)
                .map(effect -> (OnPhase6) effect)
                .filter(effect -> effect.getFunction().equals("avoidDeathForOneUnitWithMoon")).toList();
        if (avoidDeathForOneUnitWithMoonEffects.size() > 0) {
            for (OnPhase6 effect : avoidDeathForOneUnitWithMoonEffects) {
                Card card = this.strategy.chooseCardToAvoidDeath(this);
                if (card != null)
                    effect.avoidDeathForOneUnitWithMoon(this, card);
            }
        }
    }

    public void addPointFromBuildings() {
        for (Building building : this.buildings) {
            BuildingPhase buildingPhase = building.getBuildingPhase(building.getCurrentBuildingPhase());
            if (buildingPhase != null)
                this.point += buildingPhase.getPoint();
        }
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
            for (Card card : this.board.getBackCards())
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
        for (Resource resource : this.resources) {
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

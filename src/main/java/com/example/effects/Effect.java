package com.example.effects;

import java.util.ArrayList;
import java.util.List;

import com.example.Card;
import com.example.Player;
import com.example.Resource;

public abstract class Effect {
    private String function;
    private int idCard;

    protected Effect(String function, int idCard) {
        this.function = function;
        this.idCard = idCard;
    }

    protected String getFunction() {
        return this.function;
    }

    public int getIdCard() {
        return this.idCard;
    }

    protected void addResource(Player player, Resource resource) {
        player.addResource(resource);
    }

    protected void addGoldInPhase4(Player player, Resource resource) {
        player.addGoldInPhase4(resource);
    }

    protected void addResourcePerResource(Player player, Resource addResource, Resource perResource) {
        player.addResourcePerResource(addResource, perResource);
    }

    protected void addGoldPerResourceInPhase4(Player player, Resource addResource, Resource perResource) {
        player.addGoldPerResourceInPhase4(addResource, perResource);
    }

    protected void addResourcePerMoon(Player player, Resource resource, int moon) {
        player.addResourcePerMoon(resource, moon);
    }

    protected void addResourcePerAtLeastMoon(Player player, int idCard, int moon, Resource resource, boolean onUpdate) {
        player.addResourcePerAtLeastMoon(idCard, moon, resource, onUpdate);
    }

    protected void addGoldPerAtLeastMoon(Player player, int idCard, int moon, Resource resource) {
        player.addGoldPerAtLeastMoon(idCard, moon, resource);
    }

    protected void addResourceIfFrontUnit(Player player, int idCard, Resource resource) {
        player.addResourceIfFrontUnit(idCard, resource);
    }

    protected void addResourcePerWarWon(Player player, Resource resource) {
        player.addResourcePerWarWon(resource);
    }

    protected void payGoldInsteadOfResource(Player player, int gold, Resource resource, int nb) {
        player.payGoldInsteadOfResource(gold, resource, nb);
    }

    protected void ignoreBuildingCost(Player player) {
        player.ignoreBuildingCost();
    }

    protected void updateResourcePerMoon(Player player, Resource resource, int moon, int moonToAdd) {
        player.updateResourcePerMoon(resource, moon, moonToAdd);
    }

    protected void removeResource(Player player, Resource resource) {
        player.removeResource(resource);
    }

    protected void removeResourcePerMoon(Player player, int idCard, Resource resource, int moon) {
        player.removeResourcePerMoon(idCard, resource, moon);
    }

    protected void removeResourcePerAtLeastMoon(Player player, int idCard, Resource resource, int moon) {
        player.removeResourcePerAtLeastMoon(idCard, moon, resource);
    }

    protected void addPoint(Player player, int point) {
        player.addPoint(point);
    }

    protected void addPointPerWarWon(Player player, int point) {
        player.addPointPerWarWon(point);
    }

    protected void addPointPerMoon(Player player, int idCard, int point, int moon) {
        player.addPointPerMoon(idCard, point, moon);
    }

    protected void addPointPerMoonOnDyingUnit(Player player, int moon, int point) {
        player.addPointPerMoonOnDyingUnit(moon, point);
    }

    protected void addPointPerResource(Player player, int point, Resource resource) {
        player.addPointPerResource(point, resource);
    }

    protected void addPointPerResourcePerWarWon(Player player, int point, Resource resource) {
        player.addPointPerResourcePerWarWon(point, resource);
    }

    protected void addPointPerUnitWithMinAttack(Player player, int point, int minAttack) {
        player.addPointPerUnitWithMinAttack(point, minAttack);
    }

    protected void addPointPerBuildingInPhase(Player player, int point, int phase) {
        player.addPointPerBuildingInPhase(point, phase);
    }

    protected void removePointIfOnlyXMoon(Player player, int idCard, int point, int moon) {
        player.removePointIfOnlyXMoon(idCard, point, moon);
    }

    protected void addWarpointPerMoon(Player player, int idCard, int point, int moon) {
        player.addWarpointPerMoon(idCard, point, moon);
    }

    protected void addWarpointPerMoonOnEveryUnit(Player player, int point, int moon) {
        player.addWarpointPerMoonOnEveryUnit(point, moon);
    }

    protected void addWarpointPerAtLeastMoon(Player player, int idCard, int point, int moon) {
        player.addWarpointPerAtLeastMoon(idCard, point, moon);
    }

    protected void addWarpointPerResource(Player player, int idCard, int point, Resource resource) {
        player.addWarpointPerResource(idCard, point, resource);
    }

    protected void addWarpointPerGoldAddedAtNextPhase4(Player player, int idCard, int point, Resource resource) {
        player.addWarpointPerGoldAddedAtNextPhase4(idCard, point, resource);
    }

    protected void addWarpointPerAtLeastResource(Player player, int idCard, int point, Resource resource) {
        player.addWarpointPerAtLeastResource(idCard, point, resource);
    }

    protected void addMoon(Player player, int idCard, int moon) {
        player.addMoon(idCard, moon);
    }

    protected void addMoonOnAnotherUnit(Player player, int idCard, int moon) {
        player.addMoonOnAnotherUnit(idCard, moon);
    }

    protected void addMoonPerResourceOnAnotherUnit(Player player, Card card, Resource resource, int moon) {
        player.addMoonPerResourceOnAnotherUnit(card, resource, moon);
    }

    protected void addMoonToAllOtherUnits(Player player, int moon, int idCard) {
        player.addMoonToAllOtherUnits(moon, idCard);
    }

    protected void avoidDeath(Player player, int idCard) {
        player.avoidDeath(idCard);
    }

    protected void avoidDeathIfLessThanMoon(Player player, int idCard, int moon) {
        player.avoidDeathIfLessThanMoon(idCard, moon);
    }

    protected void avoidDeathForOneUnitWithMoon(Player player, Card card) {
        player.avoidDeathForOneUnitWithMoon(card);
    }

    protected void upgradeBuildingsInPhase(Player player, int phase) {
        player.upgradeBuildingsInPhase(phase);
    }

    protected void changeform(Player player, int idChangeform, Card card) {
        player.changeform(idChangeform, card);
    }

    protected void canFightFromBehind(Player player, int idCard) {
        player.canFightFromBehind(idCard);
    }

    protected void cannotFightIfAtLeastMoon(Player player, int idCard, int moon) {
        player.cannotFightIfAtLeastMoon(idCard, moon);
    }

    protected abstract void playEffect(Player player);

    public abstract void printEffect();

    public abstract Effect deepCopy();

    public static List<Effect> deepCopyEffects(List<Effect> effects) {
        List<Effect> newEffects = new ArrayList<Effect>();
        for (Effect effect : effects) {
            newEffects.add(effect.deepCopy());
        }
        return newEffects;
    }
}

package com.example.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.example.Building;
import com.example.BuildingPhase;
import com.example.Card;
import com.example.Resource;
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

public abstract class JSONUtils {
    public static List<Card> readCardsFromJSON(JSONArray jsonCards) {
        List<Card> cards = new ArrayList<Card>();
        int id = 1;
        for (Object item : jsonCards) {
            JSONObject jsonObject = (JSONObject) item;

            for (int i = 0; i < ((Long) jsonObject.get("copy")).intValue(); i++) {
                JSONArray jsonEffects = (JSONArray) jsonObject.get("effects");
                List<Effect> effects = new ArrayList<Effect>();
                if (jsonEffects != null)
                    effects = readEffectsFromJSON(jsonEffects, id);
                Card card = new Card(
                        id,
                        (String) jsonObject.get("name"),
                        ((Long) jsonObject.get("cost")).intValue(),
                        ((Long) jsonObject.get("attack")).intValue(),
                        effects);
                cards.add(card);
                id++;
            }
        }
        return cards;
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

            JSONArray effectsArray = (JSONArray) buildingPhaseObject.get("effects");
            List<Effect> effects = null;
            if (effectsArray != null)
                effects = readEffectsFromJSON(effectsArray, 0);

            BuildingPhase buildingPhase = new BuildingPhase(
                    ((Long) buildingPhaseObject.get("phase")).intValue(),
                    requirements,
                    optionnalRequirements,
                    effects,
                    ((Long) buildingPhaseObject.get("point")).intValue());

            buildingPhases.add(buildingPhase);
        }
        return buildingPhases;
    }

    public static List<Resource> readResourcesFromJSON(JSONArray resourcesArray) {
        List<Resource> resources = new ArrayList<Resource>();
        for (Object resourceItem : resourcesArray) {
            JSONObject resourceObject = (JSONObject) resourceItem;
            Resource resource = readResourceFromJSON(resourceObject);
            resources.add(resource);
        }
        return resources;
    }

    public static Resource readResourceFromJSON(JSONObject resourceObject) {
        return new Resource(
                ResourceType.valueOf((String) resourceObject.get("resourceType")),
                ((Long) resourceObject.get("quantity")).intValue());
    }

    public static Effect readEffectFromJSON(JSONObject jsonEffect, int idCard) {
        String type = (String) jsonEffect.get("type");
        String function = (String) jsonEffect.get("function");
        switch (type) {
            case "OnPhase2":
                switch (function) {
                    case "addMoonPerResourceOnAnotherUnit":
                        return new OnPhase2(function, idCard, ((Long) jsonEffect.get("moon")).intValue(),
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "cannotFightIfAtLeastMoon":
                        return new OnPhase2(function, idCard, ((Long) jsonEffect.get("moon")).intValue());
                    default:
                        System.out.println("Creating OnPhase2 Effect : unknown function " + function);
                        return null;
                }
            case "OnFlip":
                switch (function) {
                    case "addResource":
                        return new OnFlip(function, idCard,
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "addMoon":
                        return new OnFlip(function, idCard, ((Long) jsonEffect.get("moon")).intValue());
                    case "addMoonOnAnotherUnit":
                        return new OnFlip(function, idCard, ((Long) jsonEffect.get("moon")).intValue());
                    case "addMoonToAllOtherUnits":
                        return new OnFlip(function, idCard, ((Long) jsonEffect.get("moon")).intValue());
                    case "addPointPerBuildingInPhase":
                        return new OnFlip(function, idCard, ((Long) jsonEffect.get("point")).intValue(),
                                ((Long) jsonEffect.get("phase")).intValue());
                    case "upgradeBuildingsInPhase":
                        return new OnFlip(idCard, ((Long) jsonEffect.get("phase")).intValue(), function);
                    case "changeform":
                        return new OnFlip(function, idCard);
                    default:
                        System.out.println("Creating OnFlip Effect : unknown function " + function);
                        return null;
                }
            case "OnPhase3":
                switch (function) {
                    case "addPoint":
                        return new OnPhase3(function, idCard,
                                ((Long) jsonEffect.get("point")).intValue());
                    case "addPointPerResource":
                        return new OnPhase3(function, idCard,
                                ((Long) jsonEffect.get("point")).intValue(),
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "addWarpointPerResource":
                        return new OnPhase3(function, idCard, ((Long) jsonEffect.get("point")).intValue(),
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "addWarpointPerGoldAddedAtNextPhase4":
                        return new OnPhase3(function, idCard, ((Long) jsonEffect.get("point")).intValue(),
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "addWarpointPerAtLeastResource":
                        return new OnPhase3(
                                function, idCard, ((Long) jsonEffect.get("point")).intValue(),
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "addWarpointPerMoon":
                        return new OnPhase3(idCard, ((Long) jsonEffect.get("point")).intValue(),
                                ((Long) jsonEffect.get("moon")).intValue(), function);
                    case "addWarpointPerAtLeastMoon":
                        return new OnPhase3(idCard, ((Long) jsonEffect.get("point")).intValue(),
                                ((Long) jsonEffect.get("moon")).intValue(), function);
                    case "addPointPerUnitWithMinAttack":
                        return new OnPhase3(function, idCard,
                                ((Long) jsonEffect.get("point")).intValue(),
                                ((Long) jsonEffect.get("minAttack")).intValue());
                    case "canFightFromBehind":
                        return new OnPhase3(function, idCard);
                    default:
                        System.out.println("Creating OnPhase3 Effect : unknown function " + function);
                        return null;
                }
            case "OnEndPhase3":
                switch (function) {
                    case "addResourcePerWarWon":
                        return new OnEndPhase3(function, idCard,
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "addPointPerResourcePerWarWon":
                        return new OnEndPhase3(function, idCard,
                                ((Long) jsonEffect.get("point")).intValue(),
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "addPointPerWarWon":
                        return new OnEndPhase3(function, idCard,
                                ((Long) jsonEffect.get("point")).intValue());
                    default:
                        System.out.println("Creating OnEndPhase3 Effect : unknown function " + function);
                        return null;
                }
            case "OnPhase4":
                switch (function) {
                    case "addResource":
                        return new OnPhase4(function, idCard,
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "addResourcePerResource":
                        return new OnPhase4(function, idCard,
                                readResourceFromJSON((JSONObject) jsonEffect.get("addResource")),
                                readResourceFromJSON((JSONObject) jsonEffect.get("perResource")));
                    case "addResourcePerAtLeastMoon":
                        return new OnPhase4(function, idCard,
                                ((Long) jsonEffect.get("moon")).intValue(),
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    default:
                        System.out.println("Creating OnPhase4 Effect : unknown function " + function);
                        return null;
                }
            case "OnPhase5":
                switch (function) {
                    case "payGoldInsteadOfResource":
                        return new OnPhase5(function, idCard,
                                ((Long) jsonEffect.get("gold")).intValue(),
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "payGoldInsteadOfResources":
                        return new OnPhase5(function, idCard,
                                ((Long) jsonEffect.get("gold")).intValue());
                    case "ignoreBuildingCost":
                        return new OnPhase5(function, idCard);
                    default:
                        System.out.println("Creating OnPhase5 Effect : unknown function " + function);
                        return null;
                }
            case "OnPhase6":
                switch (function) {
                    case "avoidDeath":
                        return new OnPhase6(function, idCard);
                    case "avoidDeathIfLessThanMoon":
                        return new OnPhase6(function, idCard,
                                ((Long) jsonEffect.get("moon")).intValue());
                    case "avoidDeathForOneUnitWithMoon":
                        return new OnPhase6(function, idCard,
                                ((Long) jsonEffect.get("moon")).intValue());
                    case "addPointPerMoonOnDyingUnit":
                        return new OnPhase6(function, idCard,
                                ((Long) jsonEffect.get("point")).intValue(),
                                ((Long) jsonEffect.get("moon")).intValue());
                    default:
                        System.out.println("Creating OnPhase6 Effect : unknown function " + function);
                        return null;
                }
            case "OnUpdate":
                switch (function) {
                    case "updateResourcePerMoon":
                        return new OnUpdate(function, idCard, ((Long) jsonEffect.get("moon")).intValue(),
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "addResourceIfFrontUnit":
                        return new OnUpdate(function, idCard,
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "removeResourceIfFrontUnit":
                        return new OnUpdate(function, idCard,
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "addResourcePerAtLeastMoon":
                        return new OnUpdate(function, idCard,
                                ((Long) jsonEffect.get("moon")).intValue(),
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    default:
                        System.out.println("Creating OnUpdate Effect : unknown function " + function);
                        return null;
                }
            case "OnDeath":
                switch (function) {
                    case "removeResource":
                        return new OnDeath(function, idCard, 0,
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "removeResourcePerMoon":
                        return new OnDeath(function, idCard, ((Long) jsonEffect.get("moon")).intValue(),
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "removeResourcePerAtLeastMoon":
                        return new OnDeath(function, idCard, ((Long) jsonEffect.get("moon")).intValue(),
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    default:
                        System.out.println("Creating OnDeath Effect : unknown function " + function);
                        return null;
                }
            case "OnPhase6Death":
                switch (function) {
                    case "addPoint":
                        return new OnPhase6Death(function, idCard, ((Long) jsonEffect.get("point")).intValue());
                    case "addResourcePerMoon":
                        return new OnPhase6Death(function, idCard, ((Long) jsonEffect.get("moon")).intValue(),
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "removePointIfOnlyXMoon":
                        return new OnPhase6Death(function, idCard, ((Long) jsonEffect.get("point")).intValue(),
                                ((Long) jsonEffect.get("moon")).intValue());
                    default:
                        System.out.println("Creating OnPhase6Death Effect : unknown function " + function);
                        return null;
                }
            case "OnEndGame":
                switch (function) {
                    case "addPointPerMoon":
                        return new OnEndGame(function, idCard, ((Long) jsonEffect.get("point")).intValue(),
                                ((Long) jsonEffect.get("moon")).intValue());
                    default:
                        System.out.println("Creating OnEndGame Effect : unknown function " + function);
                        return null;
                }
            default:
                System.out.println("Creating effect : unknown type " + type);
                return null;
        }
    }

    public static List<Effect> readEffectsFromJSON(JSONArray jsonEffects, int idCard) {
        List<Effect> effects = new ArrayList<Effect>();
        for (Object effectObject : jsonEffects) {
            Effect effect = readEffectFromJSON((JSONObject) effectObject, idCard);
            effects.add(effect);
        }
        return effects;
    }
}

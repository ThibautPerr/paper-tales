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
import com.example.effects.OnPhase3;
import com.example.effects.OnPhase4;
import com.example.effects.OnPlayed;

public abstract class JSONUtils {
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

            JSONObject jsonEffect = (JSONObject) buildingPhaseObject.get("effect");
            Effect effect = readEffectFromJSON(jsonEffect);

            BuildingPhase buildingPhase = new BuildingPhase(
                    ((Long) buildingPhaseObject.get("phase")).intValue(),
                    requirements,
                    optionnalRequirements,
                    effect,
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

    public static Effect readEffectFromJSON(JSONObject jsonEffect) {
        String type = (String) jsonEffect.get("type");
        String function = (String) jsonEffect.get("function");
        switch (type) {
            case "OnPlayed":
                switch (function) {
                    case "addResource":
                        return new OnPlayed(function,
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "addResources":
                        return new OnPlayed(function,
                                readResourcesFromJSON((JSONArray) jsonEffect.get("resources")));
                    default:
                        System.out.println("Creating OnPlayed Effect : unknown function " + function);
                        return null;
                }
            case "OnPhase3":
                switch (function) {
                    case "addPoint":
                        return new OnPhase3(function,
                                ((Long) jsonEffect.get("point")).intValue());
                    case "addPointPerResource":
                        return new OnPhase3(function,
                                ((Long) jsonEffect.get("point")).intValue(),
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "addWarPointPerResource":
                        return new OnPhase3(function, ((Long) jsonEffect.get("point")).intValue(),
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "addPointPerUnitWithMinAttack":
                        return new OnPhase3(function,
                                ((Long) jsonEffect.get("point")).intValue(),
                                ((Long) jsonEffect.get("minAttack")).intValue());
                    default:
                        System.out.println("Creating OnPhase3 Effect : unknown function " + function);
                        return null;
                }
            case "OnPhase4":
                switch (function) {
                    case "addResource":
                        return new OnPhase4(function,
                                readResourceFromJSON((JSONObject) jsonEffect.get("resource")));
                    case "addResources":
                        return new OnPhase4(function,
                                readResourcesFromJSON((JSONArray) jsonEffect.get("resources")));
                    case "addResourcePerResource":
                        return new OnPhase4(function,
                                readResourceFromJSON((JSONObject) jsonEffect.get("addResource")),
                                readResourceFromJSON((JSONObject) jsonEffect.get("perResource")));
                    default:
                        System.out.println("Creating OnPhase4 Effect : unknown function " + function);
                        return null;
                }
            default:
                System.out.println("Creating effect : unknown type " + type);
                return null;
        }
    }
}

package com.example.strategy.build;

import java.util.Collections;
import java.util.List;

import com.example.BuildingPhase;
import com.example.Player;

public class RandomBuild extends Build {
    public RandomBuild() {
        super();
    }

    @Override
    public BuildingPhase build(Player player, List<BuildingPhase> buildablePhases) {
        if (buildablePhases.isEmpty())
            return null;
        Collections.shuffle(buildablePhases);
        return buildablePhases.get(0);
    }
    
}

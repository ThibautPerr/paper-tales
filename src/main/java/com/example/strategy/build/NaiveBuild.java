package com.example.strategy.build;

import java.util.List;

import com.example.BuildingPhase;
import com.example.Player;

public class NaiveBuild extends Build {
    public NaiveBuild() {
        super();
    }

    @Override
    public BuildingPhase build(Player player, List<BuildingPhase> buildablePhases) {
        if (buildablePhases.isEmpty())
            return null;
        return buildablePhases.get(0);
    }
}

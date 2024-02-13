package com.example.strategy.build;

import java.util.List;

import com.example.BuildingPhase;
import com.example.Player;

public abstract class Build {
    public Build() {
    }

    public abstract BuildingPhase build(Player player, List<BuildingPhase> buildablePhases);
}

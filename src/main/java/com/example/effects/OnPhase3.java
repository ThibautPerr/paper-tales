package com.example.effects;

import com.example.Player;
import com.example.Resource;

public class OnPhase3 extends Effect {
    private int point;
    private Resource.ResourceType type;
    private int quantity;
    private int minAttack;

    OnPhase3(String function) {
        super(function);
    }

    OnPhase3(String function, int point) {
        super(function);
        this.point = point;
    }

    OnPhase3(String function, int point, Resource.ResourceType type, int quantity) {
        super(function);
        this.point = point;
        this.type = type;
        this.quantity = quantity;
    }

    OnPhase3(String function, int point, int minAttack) {
        super(function);
        this.point = point;
        this.minAttack = minAttack;
    }

    public String getFunction() {
        return super.getFunction();
    }

    public int getPoint() {
        return this.point;
    }

    public void addPoint(Player player) {
        super.addPoint(player, this.point);
    }

    void addPointPerResource(Player player) {
        super.addPointPerResource(player, this.point, this.type, this.quantity);
    }

    void addWarPointPerResource(Player player) {
        super.addWarPointPerResource(player, this.point, this.type, this.quantity);
    }

    void addPointPerUnitWithMinAttack(Player player) {
        super.addPointPerUnitWithMinAttack(player, this.point, this.minAttack);
    }
}

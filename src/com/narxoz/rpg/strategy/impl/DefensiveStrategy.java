package com.narxoz.rpg.strategy.impl;

import com.narxoz.rpg.strategy.CombatStrategy;

public class DefensiveStrategy implements CombatStrategy {
    @Override
    public int calculateDamage(int basePower) {
        return (int) Math.round(basePower * 0.8);
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return (int) Math.round(baseDefense * 1.4);
    }

    @Override
    public String getName() {
        return "Defensive";
    }
}

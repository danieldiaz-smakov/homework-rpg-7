package com.narxoz.rpg.strategy.impl;

import com.narxoz.rpg.strategy.CombatStrategy;

public class BossPhaseTwoStrategy implements CombatStrategy {
    @Override
    public int calculateDamage(int basePower) {
        return (int) Math.round(basePower * 1.5);
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return (int) Math.round(baseDefense * 0.9);
    }

    @Override
    public String getName() {
        return "Boss Phase 2: Aggressive";
    }
}

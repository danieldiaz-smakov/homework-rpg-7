package com.narxoz.rpg.strategy.impl;

import com.narxoz.rpg.strategy.CombatStrategy;

public class BossPhaseOneStrategy implements CombatStrategy {
    @Override
    public int calculateDamage(int basePower) {
        return (int) Math.round(basePower * 1.1);
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return (int) Math.round(baseDefense * 1.2);
    }

    @Override
    public String getName() {
        return "Boss Phase 1: Calculated";
    }
}

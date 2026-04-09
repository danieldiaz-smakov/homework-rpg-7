package com.narxoz.rpg.strategy.impl;

import com.narxoz.rpg.strategy.CombatStrategy;

public class BossPhaseThreeStrategy implements CombatStrategy {
    @Override
    public int calculateDamage(int basePower) {
        return (int) Math.round(basePower * 2.0);
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return (int) Math.round(baseDefense * 0.4);
    }

    @Override
    public String getName() {
        return "Boss Phase 3: Desperate";
    }
}

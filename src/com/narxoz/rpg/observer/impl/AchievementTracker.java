package com.narxoz.rpg.observer.impl;

import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.observer.GameObserver;

import java.util.HashSet;
import java.util.Set;

public class AchievementTracker implements GameObserver {
    private final Set<String> unlocked = new HashSet<>();
    private int attacksLanded;

    @Override
    public void onEvent(GameEvent event) {
        GameEventType type = event.getType();

        if (type == GameEventType.ATTACK_LANDED) {
            attacksLanded++;
            unlock("First Blood", attacksLanded == 1);
            unlock("Relentless", attacksLanded >= 10);
        } else if (type == GameEventType.BOSS_DEFEATED) {
            unlock("Boss Slayer", true);
        } else if (type == GameEventType.HERO_DIED) {
            unlock("Tragic Loss", true);
        }
    }

    private void unlock(String name, boolean condition) {
        if (condition && unlocked.add(name)) {
            System.out.println("[AchievementTracker] Unlocked: " + name);
        }
    }
}

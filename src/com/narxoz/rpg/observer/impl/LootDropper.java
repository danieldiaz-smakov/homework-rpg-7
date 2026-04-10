package com.narxoz.rpg.observer.impl;

import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.observer.GameObserver;

import java.util.Random;

public class LootDropper implements GameObserver {
    private static final String[] PHASE_LOOT = {"Minor Rune", "Spiked Shield", "Rage Elixir"};
    private static final String[] BOSS_LOOT = {"Legendary Sword", "Dragon Scale Armor", "Crown of Shadows"};
    private final Random random = new Random();

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() == GameEventType.BOSS_PHASE_CHANGED) {
            String loot = PHASE_LOOT[random.nextInt(PHASE_LOOT.length)];
            System.out.printf("[LootDropper] Phase loot dropped: %s (phase=%d)%n", loot, event.getValue());
        } else if (event.getType() == GameEventType.BOSS_DEFEATED) {
            String loot = BOSS_LOOT[random.nextInt(BOSS_LOOT.length)];
            System.out.printf("[LootDropper] Boss defeated loot: %s%n", loot);
        }
    }
}

package com.narxoz.rpg.observer.impl;

import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameObserver;

public class BattleLogger implements GameObserver {
    @Override
    public void onEvent(GameEvent event) {
        System.out.printf("[BattleLogger] %-18s source=%-12s value=%d%n",
                event.getType(), event.getSourceName(), event.getValue());
    }
}

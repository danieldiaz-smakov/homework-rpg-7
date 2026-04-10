package com.narxoz.rpg.observer.impl;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.observer.GameObserver;

import java.util.List;

public class HeroStatusMonitor implements GameObserver {
    private final List<Hero> heroes;

    public HeroStatusMonitor(List<Hero> heroes) {
        this.heroes = heroes;
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() != GameEventType.HERO_LOW_HP && event.getType() != GameEventType.HERO_DIED) {
            return;
        }

        StringBuilder status = new StringBuilder("[HeroStatusMonitor] Party status: ");
        for (int i = 0; i < heroes.size(); i++) {
            Hero hero = heroes.get(i);
            status.append(hero.getName())
                    .append("=")
                    .append(hero.getHp())
                    .append("/")
                    .append(hero.getMaxHp())
                    .append(hero.isAlive() ? " ALIVE" : " DEAD");
            if (i < heroes.size() - 1) {
                status.append(" | ");
            }
        }
        System.out.println(status);
    }
}

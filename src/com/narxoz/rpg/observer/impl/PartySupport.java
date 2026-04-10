package com.narxoz.rpg.observer.impl;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.observer.GameObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PartySupport implements GameObserver {
    private final List<Hero> heroes;
    private final int healAmount;
    private final Random random = new Random();

    public PartySupport(List<Hero> heroes, int healAmount) {
        this.heroes = heroes;
        this.healAmount = healAmount;
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() != GameEventType.HERO_LOW_HP) {
            return;
        }

        List<Hero> livingHeroes = new ArrayList<>();
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                livingHeroes.add(hero);
            }
        }

        if (livingHeroes.isEmpty()) {
            return;
        }

        Hero target = livingHeroes.get(random.nextInt(livingHeroes.size()));
        int hpBefore = target.getHp();
        target.heal(healAmount);
        int healed = target.getHp() - hpBefore;
        System.out.printf("[PartySupport] Emergency heal: %s +%d HP%n", target.getName(), healed);
    }
}

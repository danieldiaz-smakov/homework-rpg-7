package com.narxoz.rpg.engine;

import com.narxoz.rpg.boss.DungeonBoss;
import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.observer.publisher.GameEventBus;
import com.narxoz.rpg.strategy.impl.AggressiveStrategy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DungeonEngine {
    private final List<Hero> heroes;
    private final DungeonBoss boss;
    private final GameEventBus eventBus;
    private final int maxRounds;
    private final Set<String> lowHpTriggered = new HashSet<>();
    private boolean heroSwitched;

    public DungeonEngine(List<Hero> heroes, DungeonBoss boss, GameEventBus eventBus, int maxRounds) {
        this.heroes = heroes;
        this.boss = boss;
        this.eventBus = eventBus;
        this.maxRounds = maxRounds;
    }

    public EncounterResult runEncounter() {
        int round = 0;
        while (round < maxRounds && boss.isAlive() && hasLivingHeroes()) {
            round++;
            System.out.printf("%n=== Round %d ===%n", round);
            maybeSwitchHeroStrategy(round);

            heroesTurn();
            if (!boss.isAlive()) {
                break;
            }

            bossTurn();
        }

        boolean heroesWon = !boss.isAlive();
        int survivors = countLivingHeroes();
        return new EncounterResult(heroesWon, round, survivors);
    }

    private void heroesTurn() {
        for (Hero hero : heroes) {
            if (!hero.isAlive() || !boss.isAlive()) {
                continue;
            }
            int effectiveAttack = hero.getStrategy().calculateDamage(hero.getAttackPower());
            int effectiveDefense = boss.getStrategy().calculateDefense(boss.getDefense());
            int damage = Math.max(1, effectiveAttack - effectiveDefense);

            System.out.printf("%s attacks %s with %s for %d damage%n",
                    hero.getName(), boss.getName(), hero.getStrategy().getName(), damage);
            boss.takeDamage(damage);
            eventBus.publish(new GameEvent(GameEventType.ATTACK_LANDED, hero.getName(), damage));
        }
    }

    private void bossTurn() {
        for (Hero hero : heroes) {
            if (!hero.isAlive() || !boss.isAlive()) {
                continue;
            }
            int effectiveAttack = boss.getStrategy().calculateDamage(boss.getAttackPower());
            int effectiveDefense = hero.getStrategy().calculateDefense(hero.getDefense());
            int damage = Math.max(1, effectiveAttack - effectiveDefense);

            System.out.printf("%s attacks %s with %s for %d damage%n",
                    boss.getName(), hero.getName(), boss.getStrategy().getName(), damage);
            hero.takeDamage(damage);
            eventBus.publish(new GameEvent(GameEventType.ATTACK_LANDED, boss.getName(), damage));

            int lowHpThreshold = (int) Math.ceil(hero.getMaxHp() * 0.30);
            if (hero.getHp() > 0 && hero.getHp() < lowHpThreshold && lowHpTriggered.add(hero.getName())) {
                eventBus.publish(new GameEvent(GameEventType.HERO_LOW_HP, hero.getName(), hero.getHp()));
            }

            if (!hero.isAlive()) {
                eventBus.publish(new GameEvent(GameEventType.HERO_DIED, hero.getName(), 0));
            }
        }
    }

    private boolean hasLivingHeroes() {
        return countLivingHeroes() > 0;
    }

    private int countLivingHeroes() {
        int count = 0;
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                count++;
            }
        }
        return count;
    }

    private void maybeSwitchHeroStrategy(int round) {
        if (heroSwitched || round < 2 || heroes.isEmpty()) {
            return;
        }
        Hero hero = heroes.get(0);
        if (hero.isAlive()) {
            hero.setStrategy(new AggressiveStrategy());
            heroSwitched = true;
            System.out.printf("[DungeonEngine] %s switches strategy to %s%n",
                    hero.getName(), hero.getStrategy().getName());
        }
    }
}

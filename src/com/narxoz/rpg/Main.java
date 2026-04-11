package com.narxoz.rpg;

import com.narxoz.rpg.boss.DungeonBoss;
import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.engine.DungeonEngine;
import com.narxoz.rpg.engine.EncounterResult;
import com.narxoz.rpg.observer.impl.AchievementTracker;
import com.narxoz.rpg.observer.impl.BattleLogger;
import com.narxoz.rpg.observer.impl.HeroStatusMonitor;
import com.narxoz.rpg.observer.impl.LootDropper;
import com.narxoz.rpg.observer.impl.PartySupport;
import com.narxoz.rpg.observer.publisher.GameEventBus;
import com.narxoz.rpg.strategy.impl.AggressiveStrategy;
import com.narxoz.rpg.strategy.impl.BalancedStrategy;
import com.narxoz.rpg.strategy.impl.DefensiveStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for Homework 7 — The Cursed Dungeon: Boss Encounter System.
 *
 * Build your heroes, boss, observers, and engine here, then run the encounter.
 */
public class Main {

    public static void main(String[] args) {
        List<Hero> heroes = new ArrayList<>();
        heroes.add(new Hero("Arin", 130, 36, 14, new AggressiveStrategy()));
        heroes.add(new Hero("Brom", 170, 26, 24, new DefensiveStrategy()));
        heroes.add(new Hero("Cira", 145, 30, 18, new BalancedStrategy()));

        GameEventBus eventBus = new GameEventBus();
        DungeonBoss boss = new DungeonBoss("Cursed Warden", 760, 42, 18, eventBus);

        BattleLogger battleLogger = new BattleLogger();
        AchievementTracker achievementTracker = new AchievementTracker();
        PartySupport partySupport = new PartySupport(heroes, 18);
        HeroStatusMonitor heroStatusMonitor = new HeroStatusMonitor(heroes);
        LootDropper lootDropper = new LootDropper();

        eventBus.register(battleLogger);
        eventBus.register(achievementTracker);
        eventBus.register(partySupport);
        eventBus.register(heroStatusMonitor);
        eventBus.register(lootDropper);
        eventBus.register(boss);

        DungeonEngine engine = new DungeonEngine(heroes, boss, eventBus, 50);
        EncounterResult result = engine.runEncounter();

        System.out.println("\n=== Encounter Result ===");
        System.out.println("Heroes won: " + result.isHeroesWon());
        System.out.println("Rounds played: " + result.getRoundsPlayed());
        System.out.println("Surviving heroes: " + result.getSurvivingHeroes());
    }
}

package com.narxoz.rpg.boss;

import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.observer.GameObserver;
import com.narxoz.rpg.observer.publisher.GameEventBus;
import com.narxoz.rpg.strategy.CombatStrategy;
import com.narxoz.rpg.strategy.impl.BossPhaseOneStrategy;
import com.narxoz.rpg.strategy.impl.BossPhaseThreeStrategy;
import com.narxoz.rpg.strategy.impl.BossPhaseTwoStrategy;

public class DungeonBoss implements GameObserver {
    private final String name;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private final GameEventBus eventBus;
    private final CombatStrategy phaseOneStrategy = new BossPhaseOneStrategy();
    private final CombatStrategy phaseTwoStrategy = new BossPhaseTwoStrategy();
    private final CombatStrategy phaseThreeStrategy = new BossPhaseThreeStrategy();

    private int hp;
    private int currentPhase;
    private CombatStrategy strategy;
    private boolean defeatedEventPublished;

    public DungeonBoss(String name, int hp, int attackPower, int defense, GameEventBus eventBus) {
        this.name = name;
        this.maxHp = hp;
        this.hp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.eventBus = eventBus;
        this.currentPhase = 1;
        this.strategy = phaseOneStrategy;
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttackPower() { return attackPower; }
    public int getDefense() { return defense; }
    public int getCurrentPhase() { return currentPhase; }
    public CombatStrategy getStrategy() { return strategy; }
    public boolean isAlive() { return hp > 0; }

    public void takeDamage(int amount) {
        if (!isAlive()) {
            return;
        }
        int previousHp = hp;
        hp = Math.max(0, hp - amount);
        checkAndPublishPhaseChanges(previousHp, hp);
        if (hp == 0 && !defeatedEventPublished) {
            defeatedEventPublished = true;
            eventBus.publish(new GameEvent(GameEventType.BOSS_DEFEATED, name, 0));
        }
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() != GameEventType.BOSS_PHASE_CHANGED) {
            return;
        }
        int phaseByHp = resolvePhaseByHp();
        if (phaseByHp != currentPhase) {
            currentPhase = phaseByHp;
            if (currentPhase == 2) {
                strategy = phaseTwoStrategy;
            } else if (currentPhase == 3) {
                strategy = phaseThreeStrategy;
            } else {
                strategy = phaseOneStrategy;
            }
            System.out.printf("[DungeonBoss] %s switched to %s%n", name, strategy.getName());
        }
    }

    private int resolvePhaseByHp() {
        double hpRatio = hp / (double) maxHp;
        if (hpRatio < 0.30) {
            return 3;
        }
        if (hpRatio < 0.60) {
            return 2;
        }
        return 1;
    }

    private void checkAndPublishPhaseChanges(int previousHp, int newHp) {
        if (crossedThreshold(previousHp, newHp, 0.60)) {
            eventBus.publish(new GameEvent(GameEventType.BOSS_PHASE_CHANGED, name, 2));
        }
        if (crossedThreshold(previousHp, newHp, 0.30)) {
            eventBus.publish(new GameEvent(GameEventType.BOSS_PHASE_CHANGED, name, 3));
        }
    }

    private boolean crossedThreshold(int previousHp, int newHp, double thresholdRatio) {
        int threshold = (int) Math.ceil(maxHp * thresholdRatio);
        return previousHp >= threshold && newHp < threshold;
    }
}

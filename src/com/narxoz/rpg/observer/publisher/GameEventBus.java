package com.narxoz.rpg.observer.publisher;

import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameObserver;

import java.util.ArrayList;
import java.util.List;

public class GameEventBus {
    private final List<GameObserver> observers = new ArrayList<>();

    public void register(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unregister(GameObserver observer) {
        observers.remove(observer);
    }

    public void publish(GameEvent event) {
        List<GameObserver> snapshot = new ArrayList<>(observers);
        for (GameObserver observer : snapshot) {
            observer.onEvent(event);
        }
    }
}

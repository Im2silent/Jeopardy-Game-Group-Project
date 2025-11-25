package com.project;

import java.util.*;

public class Player {
    private final String playerId;
    private final String name;
    private int score;
    private final List<GameTurn> turns;

    public Player(String playerId, String name) {
        this.playerId = playerId;
        this.name = name;
        this.score = 0;
        this.turns = new ArrayList<>();
    }

    // Getters and setters
    public String getPlayerId() { return playerId; }
    public String getName() { return name; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public List<GameTurn> getTurns() { return turns; }
    
    public void addTurn(GameTurn turn) {
        turns.add(turn);
    }
}

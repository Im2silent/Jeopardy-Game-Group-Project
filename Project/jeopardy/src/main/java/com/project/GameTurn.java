package com.project;

import java.time.LocalDateTime;

public class GameTurn {
    private final Player player;
    private final Question question;
    private final String givenAnswer;
    private final boolean correct;
    private final int pointsEarned;
    private final int runningTotal;
    private final LocalDateTime timestamp;

    public GameTurn(Player player, Question question, String givenAnswer, boolean correct, int pointsEarned, int runningTotal) {
        this.player = player;
        this.question = question;
        this.givenAnswer = givenAnswer;
        this.correct = correct;
        this.pointsEarned = pointsEarned;
        this.runningTotal = runningTotal;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public Player getPlayer() { return player; }
    public Question getQuestion() { return question; }
    public String getGivenAnswer() { return givenAnswer; }
    public boolean isCorrect() { return correct; }
    public int getPointsEarned() { return pointsEarned; }
    public int getRunningTotal() { return runningTotal; }
    public LocalDateTime getTimestamp() { return timestamp; }
}

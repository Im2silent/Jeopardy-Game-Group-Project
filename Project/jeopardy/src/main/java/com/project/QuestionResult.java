package com.project;

public class QuestionResult {
    private final boolean correct;
    private final int pointsEarned;
    private final int currentScore;

    public QuestionResult(boolean correct, int pointsEarned, int currentScore) {
        this.correct = correct;
        this.pointsEarned = pointsEarned;
        this.currentScore = currentScore;
    }

    // Getters
    public boolean isCorrect() { return correct; }
    public int getPointsEarned() { return pointsEarned; }
    public int getCurrentScore() { return currentScore; }
}
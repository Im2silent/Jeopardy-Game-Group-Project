package com.project;

import java.util.*;

public class GameService {
    private List<Player> players;
    private List<Question> questions;
    private int currentPlayerIndex;
    private final GameEventLogger eventLogger;
    private boolean gameRunning;

    public GameService() {
        this.players = new ArrayList<>();
        this.questions = new ArrayList<>();
        this.eventLogger = new GameEventLogger();
        this.gameRunning = false;
    }

    public void initializeGame(List<Question> questions, List<String> playerNames) {
        this.questions = questions;
        this.players.clear();
        
        for (int i = 0; i < playerNames.size(); i++) {
            players.add(new Player("P" + (i + 1), playerNames.get(i)));
        }
        
        this.currentPlayerIndex = 0;
        this.gameRunning = true;
        
        eventLogger.logGameStart();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public List<String> getAvailableCategories() {
        return questions.stream()
                .filter(q -> !q.isUsed())
                .map(Question::getCategory)
                .distinct()
                .toList();
    }

    public List<Question> getAvailableQuestionsByCategory(String category) {
        return questions.stream()
                .filter(q -> q.getCategory().equals(category) && !q.isUsed())
                .toList();
    }

    public QuestionResult answerQuestion(Question question, String answer) {
        Player currentPlayer = getCurrentPlayer();
        int previousScore = currentPlayer.getScore();
        boolean isCorrect = question.checkAnswer(answer);
        int pointsEarned = isCorrect ? question.getValue() : -question.getValue();
        
        // Update score
        int newScore = Math.max(0, previousScore + pointsEarned);
        currentPlayer.setScore(newScore);
        question.setUsed(true);
        
        // Record turn
        GameTurn turn = new GameTurn(currentPlayer, question, answer, isCorrect, pointsEarned, newScore);
        currentPlayer.addTurn(turn);
        
        // Log event
        eventLogger.logAnswerQuestion(currentPlayer, question, answer, isCorrect, newScore);
        
        // Move to next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        
        return new QuestionResult(isCorrect, pointsEarned, newScore);
    }

    public boolean isGameOver() {
        return questions.stream().allMatch(Question::isUsed) || !gameRunning;
    }

    public void endGame() {
        this.gameRunning = false;
        eventLogger.logGameEnd();
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    public GameEventLogger getEventLogger() {
        return eventLogger;
    }
}
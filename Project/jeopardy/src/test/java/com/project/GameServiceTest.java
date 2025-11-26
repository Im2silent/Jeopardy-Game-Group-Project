package com.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    private GameService gameService;
    private List<Question> questions;
    private List<String> players;

    @BeforeEach
    void setup() {
        gameService = new GameService();

        questions = new ArrayList<>();
        questions.add(new Question("Lion", 100, "Q1?", "Answer1"));
        questions.add(new Question("Pig", 200, "Q2?", "Answer2"));

        players = new ArrayList<>();
        players.add("Jared");
        players.add("Clarke");

        gameService.initializeGame(questions, players);
    }

    @Test
    void testGetCurrentPlayerAndTurnRotation() {
        Player firstPlayer = gameService.getCurrentPlayer();
        assertEquals("Jared", firstPlayer.getName());

        Question q = questions.get(0);
        gameService.answerQuestion(q, "Answer1");
        Player nextPlayer = gameService.getCurrentPlayer();
        assertEquals("Clarke", nextPlayer.getName());
    }

    @Test
    void testAnswerQuestionCorrect() {
        Question q = questions.get(0);
        QuestionResult result = gameService.answerQuestion(q, "Answer1");

        assertTrue(result.isCorrect());
        assertEquals(100, result.getPointsEarned());
        assertEquals(100, result.getCurrentScore());
        assertTrue(q.isUsed());
    }

    @Test
    void testAnswerQuestionIncorrect() {
        Question q = questions.get(0);
        QuestionResult result = gameService.answerQuestion(q, "Wrong");

        assertFalse(result.isCorrect());
        assertEquals(-100, result.getPointsEarned());
        assertEquals(0, result.getCurrentScore());
        assertTrue(q.isUsed());
    }

    @Test
    void testGameOverDetection() {
        assertFalse(gameService.isGameOver());
        for (Question q : questions) {
            gameService.answerQuestion(q, q.getCorrectAnswer());
        }
        assertTrue(gameService.isGameOver());
    }

    @Test
    void testGetAvailableCategories() {
        List<String> animals = gameService.getAvailableCategories();
        assertEquals(2, animals.size());
        assertTrue(animals.contains("Lion"));
        assertTrue(animals.contains("Pig"));
    }
}

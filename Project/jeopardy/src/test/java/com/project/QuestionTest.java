package com.project;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    @Test
    void testCheckAnswerWithOptions() {
        Map<String, String> options = new HashMap<>();
        options.put("A", "Option1");
        options.put("B", "Option2");
        options.put("C", "Option3");
        options.put("D", "Option4");
        Question q = new Question("Category1", 100, "Sample?", options, "A");

        assertTrue(q.checkAnswer("A"));
        assertTrue(q.checkAnswer("a"));
        assertFalse(q.checkAnswer("B"));
        assertFalse(q.checkAnswer(null));
    }

    @Test
    void testCheckAnswerWithoutOptions() {
        Question q = new Question("Category1", 100, "Sample?", "AnswerText");
        assertTrue(q.checkAnswer("AnswerText"));
        assertTrue(q.checkAnswer("answertext"));
        assertFalse(q.checkAnswer("WrongAnswer"));
    }

    @Test
    void testSetUsed() {
        Question q = new Question("Cat", 50, "Q?", "A");
        assertFalse(q.isUsed());
        q.setUsed(true);
        assertTrue(q.isUsed());
    }
}

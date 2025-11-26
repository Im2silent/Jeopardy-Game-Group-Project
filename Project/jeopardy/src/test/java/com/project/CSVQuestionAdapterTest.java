package com.project;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVQuestionAdapterTest {

    @Test
    void testLoadQuestions() {
        File file = new File("data/sample_game_CSV.csv"); 
        CSVQuestionAdapter adapter = new CSVQuestionAdapter();
        List<Question> questions = adapter.loadQuestions(file);

        assertFalse(questions.isEmpty());
        Question q = questions.get(0);
        assertNotNull(q.getCategory());
        assertTrue(q.getValue() > 0);
        assertNotNull(q.getQuestionText());
        assertNotNull(q.getCorrectAnswer());
    }
}

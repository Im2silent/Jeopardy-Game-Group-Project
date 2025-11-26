package com.project;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONQuestionAdapter implements QuestionDataAdapter {

    @Override
    public List<Question> loadQuestions(File file) {
        List<Question> questions = new ArrayList<>();

        try {
            // Read entire file
            String content = Files.readString(file.toPath());

            // Parse JSON array
            JSONArray array = new JSONArray(content);

            // Loop over question objects
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                String category = obj.getString("Category");
                int value = obj.getInt("Value");
                String questionText = obj.getString("Question");
                String correctAnswer = obj.getString("CorrectAnswer");

                // Extract multiple-choice options
                JSONObject opts = obj.getJSONObject("Options");
                Map<String, String> options = new HashMap<>();

                options.put("A", opts.getString("A"));
                options.put("B", opts.getString("B"));
                options.put("C", opts.getString("C"));
                options.put("D", opts.getString("D"));

                // Create Question object using MCQ constructor
                Question q = new Question(
                        category,
                        value,
                        questionText,
                        options,
                        correctAnswer
                );

                questions.add(q);
            }

        } catch (Exception e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }

        return questions;
    }
}

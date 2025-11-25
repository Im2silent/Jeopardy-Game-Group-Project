package com.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class CSVQuestionAdapter implements QuestionDataAdapter {
    @Override
    public List<Question> loadQuestions(File file) {
        List<Question> questions = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 4) continue;
                
                String category = values[0].trim();
                int value = Integer.parseInt(values[1].trim());
                String questionText = values[2].trim();
                
                if (values.length >= 8) {
                    // Multiple choice
                    Map<String, String> options = new HashMap<>();
                    options.put("A", values[3].trim());
                    options.put("B", values[4].trim());
                    options.put("C", values[5].trim());
                    options.put("D", values[6].trim());
                    questions.add(new Question(category, value, questionText, options, values[7].trim()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading CSV", e);
        }
        
        return questions;
    }
}
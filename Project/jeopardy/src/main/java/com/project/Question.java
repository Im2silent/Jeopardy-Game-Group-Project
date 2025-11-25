package com.project;

import java.util.Map;
import java.util.HashMap;

public class Question {
    private final String category;
    private final int value;
    private final String questionText;
    private final Map<String, String> options;
    private final String correctAnswer;
    private boolean used;

    // Constructor for multiple-choice questions
    public Question(String category, int value, String questionText, 
                   Map<String, String> options, String correctAnswer) {
        this.category = category;
        this.value = value;
        this.questionText = questionText;
        this.options = options != null ? options : new HashMap<>();
        this.correctAnswer = correctAnswer;
        this.used = false;
    }

    // Simple constructor for backward compatibility
    public Question(String category, int value, String questionText, String answer) {
        this.category = category;
        this.value = value;
        this.questionText = questionText;
        this.options = new HashMap<>();
        this.correctAnswer = answer;
        this.used = false;
    }

    // Getters
    public String getCategory() { return category; }
    public int getValue() { return value; }
    public String getQuestionText() { return questionText; }
    public Map<String, String> getOptions() { return options; }
    public String getCorrectAnswer() { return correctAnswer; }
    public String getAnswer() { return correctAnswer; } // For backward compatibility
    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }

    public boolean checkAnswer(String userAnswer) {
        if (userAnswer == null) return false;
        
        String cleanAnswer = userAnswer.trim().toUpperCase();
        // Check if answer is a letter (A, B, C, D) or the full text
        if (cleanAnswer.matches("[A-D]")) {
            return cleanAnswer.equals(correctAnswer.toUpperCase());
        }
        return cleanAnswer.equalsIgnoreCase(correctAnswer);
    }
}
package com.project;

import java.io.File;

public class QuestionLoaderFactory {
    public static QuestionDataAdapter getAdapter(File file) {
        String fileName = file.getName().toLowerCase();
        
        if (fileName.endsWith(".csv")) {
            return new CSVQuestionAdapter();
        } else if (fileName.endsWith(".json")) {
            return new JSONQuestionAdapter();
        } else if (fileName.endsWith(".xml")) {
            return new XMLQuestionAdapter();
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + fileName);
        }
    }
}
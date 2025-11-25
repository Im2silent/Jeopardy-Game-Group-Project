package com.project;

import java.io.File;
import java.util.List;

public interface QuestionDataAdapter {
    List<Question> loadQuestions(File file);
}
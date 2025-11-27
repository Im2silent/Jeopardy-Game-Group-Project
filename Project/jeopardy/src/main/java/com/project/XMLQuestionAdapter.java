package com.project;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLQuestionAdapter implements QuestionDataAdapter {

    @Override
    public List<Question> loadQuestions(File file) {
        List<Question> questions = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            NodeList questionNodes = document.getElementsByTagName("QuestionItem");

            for (int i = 0; i < questionNodes.getLength(); i++) {

                Element qElement = (Element) questionNodes.item(i);

                // Read simple fields
                String category = getText(qElement, "Category");
                int value = Integer.parseInt(getText(qElement, "Value"));
                String questionText = getText(qElement, "QuestionText");
                String correctAnswer = getText(qElement, "CorrectAnswer");

                // Extract options
                Map<String, String> options = new HashMap<>();
                Element optionsElement = (Element) qElement.getElementsByTagName("Options").item(0);

                if (optionsElement != null) {
                    options.put("A", getText(optionsElement, "OptionA"));
                    options.put("B", getText(optionsElement, "OptionB"));
                    options.put("C", getText(optionsElement, "OptionC"));
                    options.put("D", getText(optionsElement, "OptionD"));
                }

                // Create Question object (MCQ constructor)
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
            throw new RuntimeException("Error loading XML file: " + e.getMessage(), e);
        }

        return questions;
    }

    private String getText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent().trim();
        }
        return "";
    }
}

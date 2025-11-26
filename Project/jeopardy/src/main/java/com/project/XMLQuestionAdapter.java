package com.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
            
            NodeList questionNodes = document.getElementsByTagName("question");
            
            for (int i = 0; i < questionNodes.getLength(); i++) {
                Element questionElement = (Element) questionNodes.item(i);
                String category = getElementText(questionElement, "category");
                int value = Integer.parseInt(getElementText(questionElement, "value"));
                String questionText = getElementText(questionElement, "text");
                String answer = getElementText(questionElement, "answer");
                
                questions.add(new Question(category, value, questionText, answer));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading XML file: " + e.getMessage(), e);
        }
        
        return questions;
    }
    
    private String getElementText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return "";
    }
}

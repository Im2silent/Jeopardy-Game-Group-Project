package com.project;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameEventLogger {
    private static final String LOG_FILE = "logs/game_event_log.csv";
    private final String caseId;
    private final List<String> events;

    public GameEventLogger() {
        this.caseId = UUID.randomUUID().toString();
        this.events = new ArrayList<>();
        initializeLogFile();
    }

    private void initializeLogFile() {
        try {
            // Create logs directory if it doesn't exist
            Files.createDirectories(Paths.get("logs"));
            
            try (FileWriter writer = new FileWriter(LOG_FILE, false)) {
                writer.write("Case_ID,Player_ID,Activity,Timestamp,Category,Question_Value,Answer_Given,Result,Score_After_Play\n");
            }
        } catch (IOException e) {
            System.err.println("Error initializing log file: " + e.getMessage());
        }
    }

    private void logEvent(String playerId, String activity, String category, 
                         String questionValue, String answerGiven, String result, String scoreAfterPlay) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String event = String.join(",",
                caseId,
                playerId != null ? playerId : "",
                activity,
                timestamp,
                category != null ? category : "",
                questionValue != null ? questionValue : "",
                answerGiven != null ? answerGiven : "",
                result != null ? result : "",
                scoreAfterPlay != null ? scoreAfterPlay : ""
        );
        
        events.add(event);
        writeEventToFile(event);
    }

    private void writeEventToFile(String event) {
        try {
            // Ensure directory exists before writing
            Files.createDirectories(Paths.get("logs"));
            
            try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
                writer.write(event + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    // ... rest of the logging methods remain the same ...
    // Specific logging methods
    public void logGameStart() {
        logEvent(null, "Start Game", null, null, null, null, null);
    }

    public void logLoadFile(String filename) {
        logEvent(null, "Load File", null, null, null, filename, null);
    }

    public void logFileLoaded() {
        logEvent(null, "File Loaded Successfully", null, null, null, "Success", null);
    }

    public void logSelectPlayerCount(int count) {
        logEvent(null, "Select Player Count", null, null, null, String.valueOf(count), null);
    }

    public void logEnterPlayerName(String playerId, String name) {
        logEvent(playerId, "Enter Player Name", null, null, null, name, null);
    }

    public void logSelectCategory(String playerId, String category) {
        logEvent(playerId, "Select Category", category, null, null, null, null);
    }

    public void logSelectQuestion(String playerId, String category, int value) {
        logEvent(playerId, "Select Question", category, String.valueOf(value), null, null, null);
    }

    public void logAnswerQuestion(Player player, Question question, String answer, boolean correct, int scoreAfter) {
        logEvent(player.getPlayerId(), "Answer Question", question.getCategory(), 
                String.valueOf(question.getValue()), answer, correct ? "Correct" : "Incorrect", 
                String.valueOf(scoreAfter));
    }

    public void logScoreUpdated(String playerId, int newScore) {
        logEvent(playerId, "Score Updated", null, null, null, null, String.valueOf(newScore));
    }

    public void logGenerateReport() {
        logEvent(null, "Generate Report", null, null, null, null, null);
    }

    public void logGenerateEventLog() {
        logEvent(null, "Generate Event Log", null, null, null, null, null);
    }

    public void logGameEnd() {
        logEvent(null, "Exit Game", null, null, null, null, null);
    }
}
package com.project;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportGenerator {
    
    public void generateTextReport(List<Player> players, String filename) {
        try {
            // Create reports directory if it doesn't exist
            Files.createDirectories(Paths.get("reports"));
            
            try (FileWriter writer = new FileWriter(filename)) {
                writer.write("JEOPARDY GAME SUMMARY REPORT\n");
                writer.write("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
                writer.write("=" .repeat(50) + "\n\n");
                
                // Final Scores
                writer.write("FINAL SCORES:\n");
                writer.write("-".repeat(20) + "\n");
                for (Player player : players) {
                    writer.write(String.format("%s: %d points\n", player.getName(), player.getScore()));
                }
                writer.write("\n");
                
                // Turn-by-turn details
                writer.write("TURN-BY-TURN DETAILS:\n");
                writer.write("=" .repeat(50) + "\n");
                
                for (Player player : players) {
                    writer.write("\nPlayer: " + player.getName() + "\n");
                    writer.write("-".repeat(30) + "\n");
                    
                    int turnNumber = 1;
                    for (GameTurn turn : player.getTurns()) {
                        writer.write(String.format("Turn %d:\n", turnNumber++));
                        writer.write(String.format("  Category: %s\n", turn.getQuestion().getCategory()));
                        writer.write(String.format("  Question Value: %d\n", turn.getQuestion().getValue()));
                        writer.write(String.format("  Question: %s\n", turn.getQuestion().getQuestionText()));
                        writer.write(String.format("  Given Answer: %s\n", turn.getGivenAnswer()));
                        writer.write(String.format("  Correct: %s\n", turn.isCorrect() ? "Yes" : "No"));
                        writer.write(String.format("  Points Earned: %+d\n", turn.getPointsEarned()));
                        writer.write(String.format("  Running Total: %d\n", turn.getRunningTotal()));
                        writer.write("\n");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error generating text report: " + e.getMessage());
        }
    }
}
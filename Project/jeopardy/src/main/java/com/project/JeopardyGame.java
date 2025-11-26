package com.project;

import java.io.File;
import java.util.*;

public class JeopardyGame {
    private final GameService gameService;
    private final Scanner scanner;
    
    public JeopardyGame() {
        this.gameService = new GameService();
        this.scanner = new Scanner(System.in);
    }
    
    public void start() {
        System.out.println("Welcome to Multi-Player Jeopardy!");
        
        try {
            // Create data directory if it doesn't exist
            createDataDirectory();
            
            // Load questions from file
            List<Question> questions = loadQuestions();
            
            // Setup players
            List<String> playerNames = setupPlayers();
            
            // Initialize game
            gameService.initializeGame(questions, playerNames);
            
            // Main game loop
            gameLoop();
            
            // End game and generate reports
            endGame();
            
        } catch (Exception e) {
            System.err.println("Error during game: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private List<Question> loadQuestions() {
        while (true) {
            System.out.println("\nEnter the path to questions file (CSV, JSON, or XML): ");
            System.out.println("Available files in data/ directory:");
            
            // Show available files
            List<String> availableFiles = showAvailableFiles();
            
            if (availableFiles.isEmpty()) {
                System.out.println("No files found in data/ directory.");
                System.out.println("Please place your question files in the data/ directory and try again.");
                System.out.print("Press Enter to continue...");
                scanner.nextLine();
                continue;
            }
            
            System.out.print("Enter filename (or full path): ");
            String userInput = scanner.nextLine().trim();
            
            // Allow user to go back to file listing
            if (userInput.equalsIgnoreCase("back") || userInput.equalsIgnoreCase("list")) {
                continue;
            }
            
            File file = findFile(userInput);
            
            if (file == null || !file.exists()) {
                System.out.println("File not found: " + userInput);
                System.out.println("Please try again or type 'back' to see the file list again.");
                continue;
            }
            
            gameService.getEventLogger().logLoadFile(file.getPath());
            
            try {
                QuestionDataAdapter adapter = QuestionLoaderFactory.getAdapter(file);
                List<Question> questions = adapter.loadQuestions(file);
                gameService.getEventLogger().logFileLoaded();
                System.out.println("Loaded " + questions.size() + " questions successfully from: " + file.getName());
                return questions;
            } catch (Exception e) {
                System.out.println("Error loading file: " + e.getMessage());
                System.out.println("Please try again with a different file.");
            }
        }
    }
    
    private File findFile(String userInput) {
        // Try the exact path first
        File file = new File(userInput);
        if (file.exists()) return file;
        
        // Try in data directory
        file = new File("data/" + userInput);
        if (file.exists()) return file;
        
        // Try with common extensions
        String[] extensions = {".csv", ".json", ".xml"};
        for (String ext : extensions) {
            if (!userInput.toLowerCase().endsWith(ext)) {
                file = new File("data/" + userInput + ext);
                if (file.exists()) return file;
            }
        }
        
        return null;
    }
    
    private List<String> showAvailableFiles() {
        List<String> fileNames = new ArrayList<>();
        File dataDir = new File("data");
        
        if (dataDir.exists() && dataDir.isDirectory()) {
            File[] files = dataDir.listFiles((dir, name) -> 
                name.toLowerCase().endsWith(".csv") || 
                name.toLowerCase().endsWith(".json") || 
                name.toLowerCase().endsWith(".xml"));
            
            if (files != null) {
                for (File file : files) {
                    fileNames.add(file.getName());
                    System.out.println("  - " + file.getName());
                }
            }
        }
        
        if (fileNames.isEmpty()) {
            System.out.println("  No files found in data/ directory");
        }
        
        return fileNames;
    }
    
    private void createDataDirectory() {
        try {
            // Create data directory if it doesn't exist
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("data"));
        } catch (Exception e) {
            System.err.println("Warning: Could not create data directory: " + e.getMessage());
        }
    }
    
    private List<String> setupPlayers() {
        System.out.println("\nEnter number of players (1-4): ");
        int playerCount = Integer.parseInt(scanner.nextLine());
        
        if (playerCount < 1 || playerCount > 4) {
            throw new IllegalArgumentException("Player count must be between 1 and 4");
        }
        
        gameService.getEventLogger().logSelectPlayerCount(playerCount);
        
        List<String> playerNames = new ArrayList<>();
        for (int i = 1; i <= playerCount; i++) {
            System.out.println("Enter name for Player " + i + ": ");
            String name = scanner.nextLine();
            playerNames.add(name);
            gameService.getEventLogger().logEnterPlayerName("P" + i, name);
        }
        
        return playerNames;
    }
    
    private void gameLoop() {
        while (!gameService.isGameOver()) {
            Player currentPlayer = gameService.getCurrentPlayer();
            
            System.out.println("\n" + "=" .repeat(50));
            System.out.println(currentPlayer.getName() + "'s Turn!");
            System.out.println("Current Score: " + currentPlayer.getScore());
            
            // Show available categories
            List<String> categories = gameService.getAvailableCategories();
            if (categories.isEmpty()) {
                System.out.println("No more questions available!");
                break;
            }
            
            System.out.println("\nAvailable Categories:");
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i));
            }
            
            // Select category
            System.out.println("Choose a category (1-" + categories.size() + "): ");
            int categoryChoice = Integer.parseInt(scanner.nextLine()) - 1;
            String selectedCategory = categories.get(categoryChoice);
            
            gameService.getEventLogger().logSelectCategory(currentPlayer.getPlayerId(), selectedCategory);
            
            // Show available questions in category
            List<Question> availableQuestions = gameService.getAvailableQuestionsByCategory(selectedCategory);
            System.out.println("\nAvailable Questions in " + selectedCategory + ":");
            for (int i = 0; i < availableQuestions.size(); i++) {
                Question q = availableQuestions.get(i);
                System.out.println((i + 1) + ". $" + q.getValue());
            }
            
            // Select question
            System.out.println("Choose a question (1-" + availableQuestions.size() + "): ");
            int questionChoice = Integer.parseInt(scanner.nextLine()) - 1;
            Question selectedQuestion = availableQuestions.get(questionChoice);
            
            gameService.getEventLogger().logSelectQuestion(
                currentPlayer.getPlayerId(), selectedCategory, selectedQuestion.getValue());
            
            // Present question with options
            System.out.println("\n" + "=" .repeat(50));
            System.out.println("Question: " + selectedQuestion.getQuestionText());
            System.out.println("\nOptions:");
            Map<String, String> options = selectedQuestion.getOptions();
            if (!options.isEmpty()) {
                for (Map.Entry<String, String> entry : options.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
                System.out.println("\nEnter your answer (A, B, C, D or the full answer text): ");
            } else {
                System.out.print("Your answer: ");
            }
            
            String answer = scanner.nextLine();
            
            // Process answer
            QuestionResult result = gameService.answerQuestion(selectedQuestion, answer);
            
            System.out.println("\n" + (result.isCorrect() ? "Correct!" : "Incorrect!"));
            if (!options.isEmpty()) {
                String correctOptionText = options.get(selectedQuestion.getCorrectAnswer());
                System.out.println("Correct answer was: " + selectedQuestion.getCorrectAnswer() + ": " + correctOptionText);
            } else {
                System.out.println("Answer was: " + selectedQuestion.getAnswer());
            }
            System.out.println("Points earned: " + result.getPointsEarned());
            System.out.println("New score: " + result.getCurrentScore());
        }
    }
    
    private void endGame() {
        gameService.endGame();
        
        System.out.println("\n" + "=" .repeat(50));
        System.out.println("GAME OVER!");
        System.out.println("Final Scores:");
        
        // Create a new modifiable list for sorting
        List<Player> players = new ArrayList<>(gameService.getPlayers());
        players.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));
        
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getScore() + " points");
        }
        
        // Generate reports
        generateReports(players);
        
        System.out.println("\nReports generated in 'reports/' directory");
        System.out.println("Event log saved to 'logs/game_event_log.csv'");
    }
    
    private void generateReports(List<Player> players) {
        ReportGenerator reportGenerator = new ReportGenerator();
        
        // Generate text report
        String reportFile = "reports/game_summary_" + 
            System.currentTimeMillis() + ".txt";
        reportGenerator.generateTextReport(players, reportFile);
        
        gameService.getEventLogger().logGenerateReport();
        gameService.getEventLogger().logGenerateEventLog();
    }
    
    public static void main(String[] args) {
        new JeopardyGame().start();
    }
}
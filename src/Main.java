import java.util.Random;
import java.util.Scanner;

// Abstract class representing a move in the game
abstract class Move {
    protected String name;
    
    public Move(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    // Abstract method to determine if this move beats another move
    public abstract boolean beats(Move other);
}

// Concrete move classes
class Rock extends Move {
    public Rock() {
        super("Rock");
    }
    
    @Override
    public boolean beats(Move other) {
        return other instanceof Scissors;
    }
}

class Paper extends Move {
    public Paper() {
        super("Paper");
    }
    
    @Override
    public boolean beats(Move other) {
        return other instanceof Rock;
    }
}

class Scissors extends Move {
    public Scissors() {
        super("Scissors");
    }
    
    @Override
    public boolean beats(Move other) {
        return other instanceof Paper;
    }
}

// Player class (can be human or computer)
abstract class Player {
    protected String name;
    protected int score;
    
    public Player(String name) {
        this.name = name;
        this.score = 0;
    }
    
    public String getName() {
        return name;
    }
    
    public int getScore() {
        return score;
    }
    
    public void incrementScore() {
        score++;
    }
    
    public void resetScore() {
        score = 0;
    }
    
    // Abstract method for making a move
    public abstract Move makeMove();
}

// Human player class
class HumanPlayer extends Player {
    private Scanner scanner;
    
    public HumanPlayer(String name, Scanner scanner) {
        super(name);
        this.scanner = scanner;
    }
    
    @Override
    public Move makeMove() {
        System.out.println("\nChoose your move:");
        System.out.println("1. Rock");
        System.out.println("2. Paper");
        System.out.println("3. Scissors");
        System.out.print("Enter your choice (1-3): ");
        
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            choice = 0; // Invalid choice
        }
        
        switch (choice) {
            case 1:
                return new Rock();
            case 2:
                return new Paper();
            case 3:
                return new Scissors();
            default:
                System.out.println("Invalid choice! Please try again.");
                return makeMove(); // Recursive call for invalid input
        }
    }
}

// Computer player class
class ComputerPlayer extends Player {
    private Random random;
    
    public ComputerPlayer(String name) {
        super(name);
        this.random = new Random();
    }
    
    @Override
    public Move makeMove() {
        int choice = random.nextInt(3) + 1;
        
        switch (choice) {
            case 1:
                return new Rock();
            case 2:
                return new Paper();
            case 3:
            default:
                return new Scissors();
        }
    }
}

// Game class to manage the game flow
class RockPaperScissorsGame {
    private Player player1;
    private Player player2;
    private int rounds;
    private Scanner scanner;
    
    public RockPaperScissorsGame(Player player1, Player player2, int rounds, Scanner scanner) {
        this.player1 = player1;
        this.player2 = player2;
        this.rounds = rounds;
        this.scanner = scanner;
    }
    
    public void play() {
        System.out.println("Starting Rock Paper Scissors Game!");
        System.out.println(player1.getName() + " vs " + player2.getName());
        System.out.println("Best of " + rounds + " rounds\n");
        
        for (int round = 1; round <= rounds; round++) {
            System.out.println("=== Round " + round + " ===");
            
            Move move1 = player1.makeMove();
            Move move2 = player2.makeMove();
            
            System.out.println(player1.getName() + " chose: " + move1.getName());
            System.out.println(player2.getName() + " chose: " + move2.getName());
            
            determineRoundWinner(move1, move2);
            
            System.out.println("Current Score: " + 
                              player1.getName() + " " + player1.getScore() + " - " + 
                              player2.getName() + " " + player2.getScore() + "\n");
            
            // Check if a player has already won
            if (player1.getScore() > rounds / 2 || player2.getScore() > rounds / 2) {
                break;
            }
        }
        
        announceWinner();
        
        // Ask if player wants to play again
        System.out.print("\nWould you like to play again? (y/n): ");
        String playAgain = scanner.nextLine().toLowerCase();
        
        if (playAgain.equals("y") || playAgain.equals("yes")) {
            player1.resetScore();
            player2.resetScore();
            play();
        } else {
            System.out.println("Thanks for playing!");
        }
    }
    
    private void determineRoundWinner(Move move1, Move move2) {
        if (move1.beats(move2)) {
            System.out.println(player1.getName() + " wins this round!");
            player1.incrementScore();
        } else if (move2.beats(move1)) {
            System.out.println(player2.getName() + " wins this round!");
            player2.incrementScore();
        } else {
            System.out.println("It's a tie!");
        }
    }
    
    private void announceWinner() {
        System.out.println("\n=== GAME OVER ===");
        System.out.println("Final Score: " + 
                          player1.getName() + " " + player1.getScore() + " - " + 
                          player2.getName() + " " + player2.getScore());
        
        if (player1.getScore() > player2.getScore()) {
            System.out.println(player1.getName() + " wins the game!");
        } else if (player2.getScore() > player1.getScore()) {
            System.out.println(player2.getName() + " wins the game!");
        } else {
            System.out.println("The game is a draw!");
        }
    }
}

// Main class to run the game - changed to Main for Online GDB
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Welcome to Rock Paper Scissors!");
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();
        
        // create players
        Player humanPlayer = new HumanPlayer(playerName, scanner);
        Player computerPlayer = new ComputerPlayer("Computer");
        
        // set number of rounds
        int rounds = 3;
        
        // to create and start the game
        RockPaperScissorsGame game = new RockPaperScissorsGame(humanPlayer, computerPlayer, rounds, scanner);
        game.play();
        
        scanner.close();
    }
}
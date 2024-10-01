package org.sportradar.game;

import org.sportradar.constants.GameConstants;
import org.sportradar.repository.IRepository;
import org.sportradar.screen.IScreen;
import org.sportradar.util.TerminalColors;

import java.util.*;

/**
 * Wordle
 */
public class Wordle implements IGuessGame {

    private final IScreen gameScreen;
    private int currentRound;
    private String correctWord;
    private final int maxRounds;
    private GameState gameState;
    private final IRepository repository;

    public Wordle(IRepository repository, IScreen gameScreen, int maxRounds) {
        this.gameScreen = gameScreen;
        this.repository = repository;
        this.correctWord = repository.getRandomWord();
        this.maxRounds = maxRounds;
        this.gameState = GameState.INITIAL;
    }

    public void newRound() {
        this.correctWord = repository.getRandomWord();
        this.currentRound = 0;
        this.gameScreen.newBoard(this.maxRounds, this.correctWord.length());
        this.gameState = GameState.INITIAL;
    }

    @Override
    public void play() {
        Scanner scanner = new Scanner(System.in);
        while (this.gameState != GameState.END) {
            switch (this.gameState) {
                case INITIAL -> {
                    System.out.println(GameConstants.welcomeString);
                    this.gameState = GameState.PLAYING;
                }
                case PLAYING -> {
                    System.out.println("press ? for help, press q to quit");
                    System.out.println(this.gameScreen.drawBoard(currentRound));
                    System.out.print("Enter your guess: ");
                    String playerInput = scanner.nextLine().trim().toUpperCase();
                    handlePlayerGuess(playerInput);
                }
                case WON -> {
                    System.out.println(this.gameScreen.drawBoard(currentRound));
                    System.out.println(GameConstants.wonString);
                    System.out.println("Do you want to play again ? y/n");
                    String playerInput = scanner.nextLine().trim().toUpperCase();
                    if (playerInput.equals("Y")) {
                        newRound();
                    } else {
                        this.gameState = GameState.END;
                    }
                }
                case LOST -> {
                    System.out.println(this.gameScreen.drawBoard(currentRound));
                    System.out.println(GameConstants.lostString);
                    System.out.println("You've used all your guesses. The correct word was: "
                            + correctWord);
                    System.out.println("Do you want to play again ? y/n");
                    String playerInput = scanner.nextLine().trim().toUpperCase();
                    if (playerInput.equals("Y")) {
                        newRound();
                    } else {
                        this.gameState = GameState.END;
                    }
                }
                case HELP -> {
                    System.out.println(this.gameScreen.drawHelp());
                    System.out.print("Press Enter to return to the game...");
                    scanner.nextLine();
                    this.gameState = GameState.PLAYING;
                }
                default -> this.gameState = GameState.END;
            }
        }
        scanner.close();
        System.out.println("Goodbye!");
    }

    /**
     * Mutates state based on player interaction.
     * - if input is ?, it sets state to display help information.
     * - if Q, it sets the game state to end.
     * - otherwise handles player interaction to proceed with the game.
     *
     * @param playerInput either a commando from the player to quit or get help, or a guess
     */
    private void handlePlayerGuess(String playerInput) {
        switch (playerInput) {
            case "?" -> this.gameState = GameState.HELP;
            case "Q" -> this.gameState = GameState.END;
            default -> {
                if (playerInput.length() != correctWord.length()) {
                    System.out.println(TerminalColors.red(String.format("Invalid input. Please enter a %d-letter word."
                            , this.correctWord.length())));
                    break;
                }
                String guessString = guess(playerInput);
                this.gameScreen.updateGameBoard(guessString, currentRound);
                this.currentRound++;
                if (playerInput.equals(correctWord)) {
                    this.gameState = GameState.WON;
                } else if (currentRound >= maxRounds) {
                    this.gameState = GameState.LOST;
                }

            }
        }
    }

    @Override
    public String guess(String guess) {
        StringBuilder sb = new StringBuilder();
        List<LetterResult> charPlacementResults = getLetterFeedback(guess);
        for (int i = 0; i < guess.length(); i++) {
            String guessedChar = "[" + guess.charAt(i) + "]";
            switch (charPlacementResults.get(i)) {
                case CORRECT -> sb.append(TerminalColors.green(guessedChar.toUpperCase()));
                case MISPLACED -> sb.append(TerminalColors.yellow(guessedChar.toUpperCase()));
                case INCORRECT -> sb.append(guessedChar.toUpperCase());
            }
        }
        return sb.toString();
    }

    /**
     * Evaluates the player's guess against the correct word to get a list of which word was
     * guessed right, misplaced and so on.
     *
     * @param guess The player's guessed word.
     * @return A list of {@link LetterResult} where each element corresponds to which color should be used for that index.
     * - {@link LetterResult#CORRECT} if the letter is in the correct position.
     * - {@link LetterResult#MISPLACED} if the letter exists in the correct word but is in the wrong position.
     * - {@link LetterResult#INCORRECT} if the letter does not exist in the correct word.
     */
    private List<LetterResult> getLetterFeedback(String guess) {
        List<LetterResult> letterResults = new ArrayList<>(
                Collections.nCopies(guess.length(), LetterResult.INCORRECT));
        Map<Character, Integer> letterCount = new HashMap<>();

        for (char c : this.correctWord.toCharArray()) {
            letterCount.put(c, letterCount.getOrDefault(c, 0) + 1);
        }

        for (int i = 0; i < guess.length(); i++) {
            char guessedChar = guess.charAt(i);
            if (this.correctWord.charAt(i) == guessedChar) {
                letterResults.set(i, LetterResult.CORRECT);
                letterCount.put(guessedChar, letterCount.get(guessedChar) - 1);
            }
        }

        for (int i = 0; i < guess.length(); i++) {
            char guessedChar = guess.charAt(i);
            if (letterResults.get(i) != LetterResult.CORRECT
                    && letterCount.getOrDefault(guessedChar, 0) > 0) {
                letterResults.set(i, LetterResult.MISPLACED);
                letterCount.put(guessedChar, letterCount.get(guessedChar) - 1);
            }
        }

        return letterResults;
    }
}
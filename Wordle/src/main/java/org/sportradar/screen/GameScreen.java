package org.sportradar.screen;

import java.util.ArrayList;
import java.util.List;

/**
 * GameScreen
 */
public class GameScreen implements IScreen {
	private List<String> gameBoard;
	private int numLettersInWord;
	private  int MAX_GUESSES;

	public GameScreen(int numLettersInWord, int maxGuesses) {
		this.MAX_GUESSES = maxGuesses;
		this.numLettersInWord = numLettersInWord;
		this.gameBoard = initializeBoard(numLettersInWord * MAX_GUESSES);
	}

	@Override
	public IScreen newBoard(int maxGuesses, int numLettersInWord) {
		this.MAX_GUESSES = maxGuesses;
		this.numLettersInWord = numLettersInWord;
		this.gameBoard = initializeBoard(numLettersInWord * MAX_GUESSES);
		return this;
	}

	public void updateGameBoard(String guess, int roundNumber) {
		gameBoard.set(roundNumber, guess);
	}

	@Override
	public String drawBoard(int roundNumber) {
		return "guess " + numLettersInWord + " letters:\n" +
				String.join("\n", gameBoard)
				+ "\n" + roundNumber + "/6\n";
	}

	@Override
	public String drawHelp() {
		return String.format("""
            How to Play Wordle:
            -------------------
            1. Guess the hidden %d-letter word in %d tries.
            2. Each guess must be a valid %d-letter word.
            3. After each guess, the color of the tiles will change to show how close your guess was to the word.

            Color Meanings:
            - Green: The letter is correct and in the right position.
            - Yellow: The letter is in the word but in the wrong position.
            - Gray: The letter is not in the word.

            Commands:
            - Type a %d-letter word and press Enter to make a guess.
            - Type '?' and press Enter to see this help message again.
            - Type 'q' and press Enter to quit the help screen and return to the game.

            Good luck and have fun!
            """, numLettersInWord, MAX_GUESSES, numLettersInWord, numLettersInWord);
	}

	private List<String> initializeBoard(int boardSize){
		List<String> newBoard = new ArrayList<>(boardSize);
		for (int i = 0; i < MAX_GUESSES; i++) {
            newBoard.add("[_]".repeat(Math.max(0, numLettersInWord)));
		}
		return newBoard;
	}
}

package org.sportradar.screen;

import java.util.List;

/**
 * IScreen
 */
public interface IScreen {

	String drawBoard(int roundNumber);

	String drawHelp();
	void updateGameBoard(String guess, int roundNumber);
	IScreen newBoard(int maxGuesses, int numLettersInWord);
}

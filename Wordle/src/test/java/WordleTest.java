import org.junit.Before;
import org.junit.Test;
import org.sportradar.game.IGuessGame;
import org.sportradar.game.Wordle;
import org.sportradar.repository.IRepository;
import org.sportradar.repository.Repository;
import org.sportradar.screen.GameScreen;
import org.sportradar.screen.IScreen;
import org.sportradar.util.TerminalColors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WordleTest {
    private Wordle wordle;

    @Before
    public void setUp() {
        IRepository repository = mock(Repository.class);
        when(repository.getRandomWord()).thenReturn("WATER");
        IScreen screen = new GameScreen(5, 5);
        IGuessGame game = new Wordle(repository, screen, 5);
        this.wordle = (Wordle) game;
    }

    @Test
    public void shouldMarkCorrectLettersAsGreenInGuessOutput() {
        // Given
        String guessWord = "HATER";
        String expectedResult = "[H]"
                + TerminalColors.green("[A]")
                + TerminalColors.green("[T]")
                + TerminalColors.green("[E]")
                + TerminalColors.green("[R]");
        // When
        String actualResult = wordle.guess(guessWord);
        // Then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldMarkMisplacedLettersAsYellowInGuessOutput() {
        // Given
        String guessWord = "JELLO";
        String expectedResult = "[J]"
                + TerminalColors.yellow("[E]")
                + "[L][L][O]";
        // When
        String actualResult = wordle.guess(guessWord);
        // Then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldMarkSingleYellowForDuplicateGuess() {
        // Given
        String guessWord = "BEEFY";
        String expectedResult = "[B]"
                + TerminalColors.yellow("[E]")
                + "[E][F][Y]";
        // When
        String actualResult = wordle.guess(guessWord);
        // Then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldMarkLetterAsGreenWhenDuplicateInGuessIsCorrectlyPlaced() {
        // Given
        String guessWord = "BLEED";
        String expectedResult = "[B][L][E]"
                + TerminalColors.green("[E]")
                + "[D]";
        // When
        String actualResult = wordle.guess(guessWord);
        // Then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldInvalidateNumberInUserInput() {
        // Given
        String userInput = "1337L";
        // When
        boolean notValid = wordle.validateUserInput(userInput);
        // Then
        assertFalse(notValid);

    }

    @Test
    public void shouldAllowNorwegianSpecialCharacters() {
        // Given
        String userInput = "ÆÆÆÆÆ";
        // When
        boolean valid = wordle.validateUserInput(userInput);
        // Then
        assertTrue(valid);
    }

    @Test
    public void shouldNotAllowToShortWord() {
        // Given
        String userInput = "h";
        // When
        boolean inValid = wordle.validateUserInput(userInput);
        // Then
        assertFalse(inValid);
    }

    @Test
    public void shouldNotAllowToLongWord() {
        // Given
        String userInput = "HHHHHHH";
        // When
        boolean inValid = wordle.validateUserInput(userInput);
        // Then
        assertFalse(inValid);

    }
}

import org.junit.Before;
import org.junit.Test;
import org.sportradar.enums.LetterResult;
import org.sportradar.game.IGuessGame;
import org.sportradar.game.Wordle;
import org.sportradar.repository.IRepository;
import org.sportradar.repository.Repository;
import org.sportradar.screen.GameScreen;
import org.sportradar.screen.IScreen;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

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
    public void guess_givesGreenOnCorrectGuess() {
        // Given
        String guessWord = "HATER";
        List<LetterResult> expectedResult = Arrays.asList(
                LetterResult.INCORRECT, LetterResult.CORRECT, LetterResult.CORRECT, LetterResult.CORRECT, LetterResult.CORRECT
        );
        // When
        List<LetterResult> actualResult = wordle.getLetterResults(guessWord);
        // Then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void guess_givesYellowOnMisplacedGuess() {
        // Given
        String guessWord = "JELLO";
        List<LetterResult> expectedResult = Arrays.asList(
                LetterResult.INCORRECT, LetterResult.MISPLACED, LetterResult.INCORRECT, LetterResult.INCORRECT, LetterResult.INCORRECT
        );
        // When
        List<LetterResult> actualResult = wordle.getLetterResults(guessWord);
        // Then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void guess_givesSingleYellowWhenGuessHasDuplicateButWordHasOne() {
        // Given
        String guessWord = "BEEFY";
        List<LetterResult> expectedResult = Arrays.asList(
                LetterResult.INCORRECT, LetterResult.MISPLACED, LetterResult.INCORRECT, LetterResult.INCORRECT, LetterResult.INCORRECT
        );
        // When
        List<LetterResult> actualResult = wordle.getLetterResults(guessWord);
        // Then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void guess_givesGreenIfGuessHasDuplicateWhereOneIsCorrect() {
        // Given
        String guessWord = "BLEED";
        List<LetterResult> expectedResult = Arrays.asList(
                LetterResult.INCORRECT, LetterResult.INCORRECT, LetterResult.INCORRECT, LetterResult.CORRECT, LetterResult.INCORRECT
        );
        // When
        List<LetterResult> actualResult = wordle.getLetterResults(guessWord);
        // Then
        assertEquals(expectedResult, actualResult);
    }
}
package org.sportradar;

import org.sportradar.game.IGuessGame;
import org.sportradar.game.Wordle;
import org.sportradar.repository.IRepository;
import org.sportradar.repository.Repository;
import org.sportradar.screen.GameScreen;
import org.sportradar.screen.IScreen;

public class Main {
    public static void main(String[] args) {
        IRepository repository = new Repository();
        IScreen gameScreen = new GameScreen(5, 5);
        IGuessGame wordle = new Wordle(repository, gameScreen, 5);
        wordle.play();
    }
}
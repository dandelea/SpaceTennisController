package tennis;

import tennis.screens.MainMenuScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.WindowedMean;

/**
 * SpaceTennisController Application Listener. Manages the whole libgdx life
 * cycle.
 * 
 * @author Daniel de los Reyes Leal
 * @version 1
 */
public class SpaceTennisController extends Game {
	public static final String TITLE = "Space Tennis Controller";
	public static final String VERSION = "ver 0.1";
	public static int WIDTH;
	public static int HEIGHT;
	public static WindowedMean movement;
	/**
	 * Window size to store a swing movement.
	 */
	private final static int WINDOW_SIZE = 10;

	@Override
	public void create() {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		movement = new WindowedMean(WINDOW_SIZE);
		setScreen(new MainMenuScreen());
	}

	public void dispose() {
		super.dispose();
	}

	public void render() {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		super.render();
	}

	public void resize(int witdh, int height) {
		super.resize(witdh, height);
	}

	public void pause() {
		super.pause();
	}

	public void resume() {
		super.resume();
	}

	/**
	 * Static version of {@link #setScreen(Screen)} . Easier to call
	 * 
	 * @param screen
	 *            Screen to display
	 */
	public static void goTo(Screen screen) {
		Game game = (Game) Gdx.app.getApplicationListener();
		game.setScreen(screen);
	}

}

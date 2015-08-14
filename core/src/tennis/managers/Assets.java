package tennis.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

/**
 * Manage all the assets from the game, located in the /assets folder. Provide
 * methods to easily loads the required assets from a certain screen.
 * 
 * @author Daniel de los Reyes Leal
 * @version 1
 */
public class Assets implements Disposable {
	public static final int MAIN_MENU_SCREEN = 1;
	public static final int RULES_SCREEN = 2;
	public static final int CONNECT_SCREEN = 3;
	public static final int GAME_SCREEN = 4;

	private static final String URL_SKIN = "ui/uiskin.json";
	private static final String URL_FONT1 = "fonts/space_age.ttf";

	public Skin skin;
	public FreeTypeFontGenerator titleGenerator;

	public AssetManager assetManager;

	/**
	 * Initializes assets.
	 */
	public Assets() {
		assetManager = new AssetManager();
	}

	/**
	 * Loads the required assets for a specific screen.
	 * 
	 * @param screen
	 *            Screen code for actual screen. See {@link #Assets()}
	 */
	public void loadScreen(int screen) {
		switch (screen) {
		case MAIN_MENU_SCREEN:
			loadSkin();
			break;
		case RULES_SCREEN:
			loadSkin();
			loadImages();
			break;
		case CONNECT_SCREEN:
			loadSkin();
			break;
		case GAME_SCREEN:
			loadSkin();
			break;
		}
		finish();
		skin = assetManager.get(URL_SKIN);

	}

	/**
	 * Only loads skin and fonts located in assets folder. Invoke with
	 * {@link #skin}, {@link #titleGenerator} and {@link #fontGenerator}
	 */
	private void loadSkin() {
		assetManager.load(URL_SKIN, Skin.class);
		titleGenerator = new FreeTypeFontGenerator(
				Gdx.files.internal(URL_FONT1));
	}

	/**
	 * Only images located in assets folder.
	 */
	private void loadImages() {

	}

	/**
	 * Get a previously loaded object with {@link Assets} in the
	 * {@link #assetManager}
	 * 
	 * @param direction
	 *            Related URI of resource in assets folder
	 * @param type
	 *            Class of object loaded
	 * @return Object loaded
	 */
	public <T> T get(String direction, Class<T> type) {
		return assetManager.get(direction, type);
	}

	/**
	 * Finish loading the {@link #assetManager}
	 */
	private void finish() {
		assetManager.finishLoading();
	}

	/**
	 * Dispose objects loaded by the {@link #assetManager}.
	 */
	public void dispose() {
		assetManager.dispose();
		if (skin != null)
			skin.dispose();
	}
}

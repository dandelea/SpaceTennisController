package tennis.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {
	public static final int MAIN_MENU_SCREEN = 1;
	public static final int RULES_SCREEN = 2;
	public static final int CONNECT_SCREEN = 3;
	public static final int GAME_SCREEN = 4;

	public Skin skin;
	public FreeTypeFontGenerator titleGenerator;

	public AssetManager assetManager;

	public static final String URL_SKIN = "ui/uiskin.json";
	public static final String URL_FONT1 = "fonts/space_age.ttf";

	public Assets() {
		assetManager = new AssetManager();
	}

	public void loadScreen(int screen) {
		switch (screen) {
		case MAIN_MENU_SCREEN:
			loadSkin();
			break;
		case RULES_SCREEN:
			loadSkin();
			loadTextures();
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

	private void loadSkin() {
		assetManager.load(URL_SKIN, Skin.class);
		titleGenerator = new FreeTypeFontGenerator(
				Gdx.files.internal(URL_FONT1));
	}

	private void loadTextures() {

	}

	public <T> T get(String direction, Class<T> type) {
		return assetManager.get(direction, type);
	}

	private void finish() {
		assetManager.finishLoading();
	}

	public void dispose() {
		assetManager.dispose();
		if (skin != null)
			skin.dispose();
	}
}

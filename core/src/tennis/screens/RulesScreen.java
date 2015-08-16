package tennis.screens;

import tennis.SpaceTennisController;
import tennis.managers.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RulesScreen implements Screen {
	private Assets assets;
	private Skin skin;
	private BitmapFont titleFont;

	private Stage stage;
	private Table table;

	private Label heading, rules1, rules2;
	private Image image1;
	private TextButton btnExit;

	private static final String RULES1 = "Para conectarte necesitas tener un\nordenador y un móvil con Bluetooth.";
	private static final String RULES2 = "Empareja tu móvil con tu ordenador\n"
			+ "desde Ajustes de Android. Despues\n"
			+ "abre el juego en el ordenador y\n"
			+ "conectate a través de esta aplicación.";

	@SuppressWarnings("deprecation")
	@Override
	public void show() {
		assets = new Assets();
		assets.loadScreen(Assets.RULES_SCREEN);

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		skin = assets.skin;
		
		float ratio = SpaceTennisController.WIDTH / 1280f; // Use 1920x1280 as baseline
		float baseSize = 90;  // for 64 sized fonts at baseline width above define other sizes
		int size = (int) (baseSize * ratio);
		titleFont = assets.titleGenerator.generateFont(size);

		table = new Table(skin);
		table.setFillParent(true);

		heading = new Label("Cómo conectarse", skin);
		heading.setStyle(new LabelStyle(titleFont, Color.WHITE));

		baseSize = 0.6f;
		rules1 = new Label(RULES1, skin);
		rules1.setFontScale(baseSize * ratio);
		rules2 = new Label(RULES2, skin);
		rules2.setFontScale(baseSize * ratio);

		baseSize = 1f;
		btnExit = new TextButton("Volver", skin);
		btnExit.pad(10);
		btnExit.getLabel().setFontScale(baseSize * ratio);
		btnExit.addListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				SpaceTennisController.goTo(new MainMenuScreen());
			}

		});

		table.add(heading).spaceBottom(0.07f * SpaceTennisController.HEIGHT)
				.colspan(2).row();
		table.add(rules1).spaceBottom(0.05f * SpaceTennisController.HEIGHT)
				.row();
		table.add(rules2).row();
		table.add(btnExit).spaceTop(0.2f * SpaceTennisController.HEIGHT).center();
		stage.addActor(table);
	}

	@Override
	public void render(float delta) {
		handleInput();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
	}

	public void handleInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
			SpaceTennisController.goTo(new MainMenuScreen());
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		table.invalidateHierarchy();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		stage.dispose();
		assets.dispose();
		skin.dispose();
		titleFont.dispose();
	}

}

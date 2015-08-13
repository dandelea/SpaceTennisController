package tennis.screens;

import tennis.SpaceTennisController;
import tennis.android.BluetoothClient;
import tennis.managers.Assets;
import tennis.tween.ActorAccessor;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
	private Assets assets;

	private Stage stage;
	private Table table;
	private Label label;
	private TextButton btnExit;

	private BitmapFont titleFont;

	private final String MESSAGE_CONNECTING = "Conectando";
	private final String MESSAGE_READY = "Listo";
	private final String MESSAGE_ERROR = "Error:\n\n" + 
										"- Reinicia la aplicación\nde ordenador.\n" + 
										"- Error técnico\nde Bluetooth.";

	private Skin skin;

	private TweenManager tweenManager;

	private long time = 0;

	@SuppressWarnings("deprecation")
	@Override
	public void show() {
		assets = new Assets();
		assets.loadScreen(Assets.GAME_SCREEN);

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		skin = assets.skin;

		table = new Table(skin);
		table.setFillParent(true);

		titleFont = assets.titleGenerator.generateFont(50);
		label = new Label(MESSAGE_CONNECTING, skin);
		label.setStyle(new LabelStyle(titleFont, Color.WHITE));

		btnExit = new TextButton("Cancelar", skin);
		btnExit.pad(20);
		btnExit.addListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				BluetoothClient.endConnection();
				SpaceTennisController.goTo(new MainMenuScreen());
			}
		});

		table.add(label).row();
		table.add(btnExit).spaceTop(SpaceTennisController.HEIGHT * 0.5f);
		stage.addActor(table);

		// Creating animations
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		// Heading color animation
		Timeline.createSequence().beginSequence()
				.push(Tween.to(label, ActorAccessor.RGB, .4f).target(0, 0, 1))
				.push(Tween.to(label, ActorAccessor.RGB, .4f).target(0, 1, 0))
				.push(Tween.to(label, ActorAccessor.RGB, .4f).target(1, 0, 0))
				.push(Tween.to(label, ActorAccessor.RGB, .4f).target(1, 1, 0))
				.push(Tween.to(label, ActorAccessor.RGB, .4f).target(0, 1, 1))
				.push(Tween.to(label, ActorAccessor.RGB, .4f).target(1, 0, 1))
				.push(Tween.to(label, ActorAccessor.RGB, .4f).target(1, 1, 1))
				.end().repeat(Tween.INFINITY, 0).start(tweenManager);

		// table fade-in
		Tween.from(table, ActorAccessor.ALPHA, .75f).target(0)
				.start(tweenManager);
		Tween.from(table, ActorAccessor.Y, .75f)
				.target(Gdx.graphics.getHeight() / 8).start(tweenManager);

		tweenManager.update(Gdx.graphics.getDeltaTime());

		time = TimeUtils.nanoTime();

		BluetoothClient.connect();
	}

	@Override
	public void render(float delta) {
		handleInput();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();

		if (BluetoothClient.connected) {

			if (TimeUtils.timeSinceNanos(time) > 100000000) {
				BluetoothClient.sendAccelerometer();
				time = TimeUtils.nanoTime();
				if (SpaceTennisController.movement.hasEnoughData()) {
					label.setText(MESSAGE_READY);
				}
			}

		} else {

			if (BluetoothClient.connection_attempts >= BluetoothClient.MAX_CONNECTION_ATTEMPTS) {
				label.setText(MESSAGE_ERROR);
			} else {
				BluetoothClient.connect();
				label.setText(MESSAGE_CONNECTING);
			}
			

		}

		tweenManager.update(delta);
	}

	private void handleInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
			BluetoothClient.endConnection();
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
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		assets.dispose();
		stage.dispose();
		skin.dispose();
	}

}

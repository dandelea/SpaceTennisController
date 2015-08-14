package tennis.screens;

import tennis.SpaceTennisController;
import tennis.android.BluetoothClient;
import tennis.managers.Assets;
import tennis.tween.ActorAccessor;
import android.bluetooth.BluetoothDevice;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
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

public class PairedDevicesScreen implements Screen {
	private Stage stage;
	private Table table;

	private Assets assets;
	private Skin skin;
	private BitmapFont titleFont;

	private Label heading;
	private TextButton btnExit;

	private final static String HEADING = "Selecciona \ndispositivo:";

	private TweenManager tweenManager;

	@SuppressWarnings("deprecation")
	@Override
	public void show() {
		assets = new Assets();
		assets.loadScreen(Assets.CONNECT_SCREEN);

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		skin = assets.skin;
		titleFont = assets.titleGenerator
				.generateFont((SpaceTennisController.HEIGHT * 64)
						/ SpaceTennisController.WIDTH);

		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		heading = new Label(HEADING, skin);
		heading.setStyle(new LabelStyle(titleFont, Color.WHITE));

		btnExit = new TextButton("Volver", skin);
		btnExit.pad(20);
		btnExit.addListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				SpaceTennisController.goTo(new MainMenuScreen());
			}

		});

		table.add(heading).spaceBottom(100);

		for (final String device : BluetoothClient.pairedDevices()) {
			TextButton btn = new TextButton(device, skin);
			btn.pad(20);
			btn.addListener(new ClickListener() {

				public void clicked(InputEvent event, float x, float y) {
					BluetoothDevice btDevice = BluetoothClient
							.getBTDevice(device);
					BluetoothClient.setPreferedDevice(btDevice);
					SpaceTennisController.goTo(new GameScreen());
				}

			});
			table.row();
			table.add(btn).spaceBottom(100);
		}
		table.row();
		table.add(btnExit).spaceTop(SpaceTennisController.HEIGHT * 0.2f);
		stage.addActor(table);

		// TWEEN ANIMATION

		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		// HEADING COLOR ANIMATION
		Timeline.createSequence()
				.beginSequence()
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 0, 1))
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 1, 0))
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 0, 0))
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 1, 0))
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 1, 1))
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 0, 1))
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 1, 1))
				.end().repeat(Tween.INFINITY, 0).start(tweenManager);

		tweenManager.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();

		tweenManager.update(delta);
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
		assets.dispose();
		stage.dispose();
		skin.dispose();
		titleFont.dispose();
	}

}

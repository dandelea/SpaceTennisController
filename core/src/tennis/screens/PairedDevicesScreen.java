package tennis.screens;

import tennis.SpaceTennisController;
import tennis.android.BluetoothClient;
import tennis.managers.Assets;
import tennis.tween.ActorAccessor;
import android.bluetooth.BluetoothDevice;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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

	private Skin skin;

	private Table table;
	private BitmapFont titleFont;
	private Label heading;

	private String headingTitle = " Disposisivos\nemparejados";

	private TweenManager tweenManager;

	private Assets assets;
	
	private TextButton btnExit;

	@SuppressWarnings("deprecation")
	@Override
	public void show() {
		assets = new Assets();
		assets.loadAll();

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		skin = Assets.skin;

		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/space age.ttf"));

		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		titleFont = gen.generateFont((SpaceTennisController.HEIGHT * 64)
				/ SpaceTennisController.WIDTH);

		heading = new Label(headingTitle, skin);
		heading.setStyle(new LabelStyle(titleFont, Color.WHITE));
		
		btnExit = new TextButton("Volver", skin);
		btnExit.pad(20);
		btnExit.addListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener())
				.setScreen(new MainMenuScreen());
			}

		});

		table.add(heading).spaceBottom(100);

		for (final String device : BluetoothClient.pairedDevices()) {
			TextButton btn = new TextButton(device, skin);
			btn.pad(20);
			btn.addListener(new ClickListener() {

				public void clicked(InputEvent event, float x, float y) {
					BluetoothDevice btDevice = BluetoothClient.getBTDevice(device);
					BluetoothClient.setPreferedDevice(btDevice);
				}

			});
			table.row();
			table.add(btn).spaceBottom(100);
		}
		table.row();
		table.add(btnExit);
		stage.addActor(table);
		
		// Register Tween Manager
		
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		// Heading color animation
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
		// TODO Auto-generated method stub

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

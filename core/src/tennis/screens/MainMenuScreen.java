package tennis.screens;

import tennis.SpaceTennisController;
import tennis.android.BluetoothClient;
import tennis.managers.Assets;
import tennis.tween.ActorAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Orientation;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

public class MainMenuScreen implements Screen {

	private Stage stage;

	private Skin skin;

	private Table table;
	private BitmapFont font2;
	private BitmapFont titleFont;
	private Label heading;
	private SpriteBatch batch;
	private float highestY = 0.0f;
	private String message = "Do something already!";

	private Assets assets;

	private TweenManager tweenManager;

	private TextButton btnStart, btnRules, btnExit;

	@SuppressWarnings("deprecation")
	@Override
	public void show() {

		assets = new Assets();
		assets.loadScreen(Assets.MAIN_MENU_SCREEN);
		font2 = new BitmapFont();

		batch = new SpriteBatch();

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		skin = assets.skin;

		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		btnStart = new TextButton("Conectar", skin);
		btnStart.pad(20);
		btnStart.addListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				SpaceTennisController.goTo(new PairedDevicesScreen());
			}

		});

		btnRules = new TextButton("Cómo conectarse", skin);
		btnRules.pad(20);
		btnRules.addListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				SpaceTennisController.goTo(new RulesScreen());
			}

		});

		btnExit = new TextButton("Salir", skin);
		btnExit.pad(20);
		btnExit.addListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				BluetoothClient.endConnection();
				exitFadeOut();
			}

		});

		// Creating heading
		titleFont = assets.titleGenerator.generateFont((SpaceTennisController.HEIGHT * 64)
				/ SpaceTennisController.WIDTH);

		heading = new Label("Space Tennis\nController", skin);
		heading.setStyle(new LabelStyle(titleFont, Color.WHITE));

		table.add(heading);

		table.getCell(heading).spaceBottom(0.1f * SpaceTennisController.HEIGHT);
		table.row();
		if (BluetoothClient.supportsBluetooth()
				&& Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {

			table.add(btnStart).spaceBottom(0.05f * SpaceTennisController.HEIGHT).row();

		} else {

			Label label = new Label("Dispositivo no compatible", skin);
			label.setColor(Color.RED);
			table.add(label).spaceBottom(100).row();

		}

		table.add(btnRules).spaceBottom(0.05f * SpaceTennisController.HEIGHT).row();
		table.add(btnExit);
		stage.addActor(table);

		// Creating animations
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		// Animation Fade-In.

		// Heading color animation
		Timeline.createSequence()
				.beginSequence()
				.push(Tween.to(heading, ActorAccessor.RGB, .4f).target(0, 0, 1))
				.push(Tween.to(heading, ActorAccessor.RGB, .4f).target(0, 1, 0))
				.push(Tween.to(heading, ActorAccessor.RGB, .4f).target(1, 0, 0))
				.push(Tween.to(heading, ActorAccessor.RGB, .4f).target(1, 1, 0))
				.push(Tween.to(heading, ActorAccessor.RGB, .4f).target(0, 1, 1))
				.push(Tween.to(heading, ActorAccessor.RGB, .4f).target(1, 0, 1))
				.push(Tween.to(heading, ActorAccessor.RGB, .4f).target(1, 1, 1))
				.end().repeat(Tween.INFINITY, 0).start(tweenManager);

		// Heading and buttons fade-in
		Timeline.createSequence().beginSequence()
				.push(Tween.set(btnStart, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(btnExit, ActorAccessor.ALPHA).target(0))
				.push(Tween.from(heading, ActorAccessor.ALPHA, .25f).target(0))
				.push(Tween.to(btnStart, ActorAccessor.ALPHA, .25f).target(1))
				.push(Tween.to(btnExit, ActorAccessor.ALPHA, .25f).target(1))
				.end().start(tweenManager);

		// table fade-in
		Tween.from(table, ActorAccessor.ALPHA, .75f).target(0)
				.start(tweenManager);
		Tween.from(table, ActorAccessor.Y, .75f)
				.target(Gdx.graphics.getHeight() / 8).start(tweenManager);

		tweenManager.update(Gdx.graphics.getDeltaTime());

		time = TimeUtils.nanoTime();
	}

	public void exitFadeOut() {
		Timeline.createParallel()
				.beginParallel()
				.push(Tween.to(table, ActorAccessor.ALPHA, .75f).target(0))
				.push(Tween.to(table, ActorAccessor.Y, .75f)
						.target(table.getY() - 50)
						.setCallback(new TweenCallback() {

							@Override
							public void onEvent(int arg0, BaseTween<?> arg1) {
								Gdx.app.exit();

							}
						})).end().start(tweenManager);
	}

	public long time = 0;
	float lastHit[] = { 0, 0, 0 };

	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		handleInput();

		stage.act(delta);
		stage.draw();

		batch.begin();
		int deviceAngle = Gdx.input.getRotation();
		Orientation orientation = Gdx.input.getNativeOrientation();
		float accelZ = Gdx.input.getAccelerometerZ();
		if (accelZ > highestY)
			highestY = accelZ;
		message = "Device rotated to:" + Integer.toString(deviceAngle)
				+ " degrees\n";
		message += "Device orientation is ";
		switch (orientation) {
		case Landscape:
			message += " landscape.\n";
			break;
		case Portrait:
			message += " portrait. \n";
			break;
		default:
			message += " complete crap!\n";
			break;
		}

		message += "Device Resolution: "
				+ Integer.toString(SpaceTennisController.WIDTH) + ","
				+ Integer.toString(SpaceTennisController.HEIGHT) + "\n";
		message += "Z axis accel: " + Float.toString(accelZ) + " \n";
		message += "Highest Y value: " + Float.toString(highestY) + " \n";

		if (Gdx.input.isPeripheralAvailable(Peripheral.Vibrator)) {
			if (accelZ > 10) {
				Gdx.input.vibrate((int) delta);
				lastHit[0] = Gdx.input.getAccelerometerX();
				lastHit[1] = Gdx.input.getAccelerometerY();
				lastHit[2] = Gdx.input.getAccelerometerZ();
			}
			message += "Last hit: " + lastHit[0] + ", " + lastHit[1] + ", "
					+ lastHit[2] + "\n";
		}
		message += "Accelerometer " + lastHit.toString() + "\n";
		if (Gdx.input.isPeripheralAvailable(Peripheral.Compass)) {
			message += "Azmuth:" + Float.toString(Gdx.input.getAzimuth())
					+ "\n";
			message += "Pitch:" + Float.toString(Gdx.input.getPitch()) + "\n";
			message += "Roll:" + Float.toString(Gdx.input.getRoll()) + "\n";
		} else {
			message += "No compass available\n";
		}
		font2.drawMultiLine(batch, message, 0, SpaceTennisController.HEIGHT);
		batch.end();

		if (TimeUtils.timeSinceNanos(time) > 100000000
				&& BluetoothClient.connected) {
			BluetoothClient.sendAccelerometer();
			time = TimeUtils.nanoTime();
		}

		tweenManager.update(delta);
	}

	private void handleInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
			BluetoothClient.endConnection();
			exitFadeOut();
		}
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
		font2.dispose();
		titleFont.dispose();

		batch.dispose();
	}

}

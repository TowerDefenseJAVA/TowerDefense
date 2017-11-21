package gamedev.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import gamedev.td.GDSprite;
import gamedev.td.SpriteManager;
import gamedev.td.TowerDefense;

public class SettingScreen extends GDScreen{
	
	
	OrthographicCamera camera;
	SpriteBatch spriteBatch;
	BitmapFont font;
	private List<GDSprite> buttons;
	public final static int MAIN_MENU = 0;
	GDSprite background,menuBtn;
	
	public SettingScreen(TowerDefense towerDefense) {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(true);
		
		spriteBatch = new SpriteBatch();
		
		spriteBatch.setProjectionMatrix(camera.combined);
		
		initializeFont();
		initializeButtons();
		//this.inputProcessor = new LvlSelectInputProcessor(towerDefense, this);
	}	
	
	
	private void initializeButtons() {
		buttons = new ArrayList<GDSprite>();
		SpriteManager spriteManager = SpriteManager.getInstance();
		background = spriteManager.getSprite("lvlselectbg");
		background.setPosition(0, 0);
		
		menuBtn = spriteManager.getSprite("back_to_menu_button");
		menuBtn.setPosition(160,520);
		buttons.add(menuBtn);
	}
	
	private void initializeFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Minecraftia.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 14;
		parameter.flip = true;
		font = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
	}
	public List<GDSprite> getButtons() {
		return buttons;
	}

	

	@Override
	public void render(float delta) {
		// TODO 自動產生的方法 Stub
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT |
				(Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

		spriteBatch.begin();
		background.draw(spriteBatch);
			for(GDSprite button : buttons) {
				button.draw(spriteBatch);
			}
		spriteBatch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO 自動產生的方法 Stub
		
	}

	@Override
	public void show() {
		// TODO 自動產生的方法 Stub
		
	}

	@Override
	public void hide() {
		// TODO 自動產生的方法 Stub
		
	}

	@Override
	public void pause() {
		// TODO 自動產生的方法 Stub
		
	}

	@Override
	public void resume() {
		// TODO 自動產生的方法 Stub
		
	}

	@Override
	public void dispose() {
		// TODO 自動產生的方法 Stub
		
	}

}

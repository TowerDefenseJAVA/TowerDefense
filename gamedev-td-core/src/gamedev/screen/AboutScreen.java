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

import gamedev.input.AboutInputProcessor;
import gamedev.td.GDSprite;
import gamedev.td.SpriteManager;
import gamedev.td.TowerDefense;

public class AboutScreen extends GDScreen{

	public static final int MAIN_MENU = 0, NEXT_PAGE = 1, PRE_PAGE = 2;
	OrthographicCamera camera;
	SpriteBatch spriteBatch;
	BitmapFont font;
	private List<GDSprite> buttons;
	private static int currentbg=1;
	
	static GDSprite background,menuBtn,nextBtn,preBtn;
	
	public AboutScreen(TowerDefense towerDefense) {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(true);
		
		spriteBatch = new SpriteBatch();
		
		spriteBatch.setProjectionMatrix(camera.combined);
		
		initializeFont();
		initializeButtons();
		this.inputProcessor = new AboutInputProcessor(towerDefense, this);
		//AboutInputProcessor的建構子
	}	
	
	
	private void initializeButtons() {
		buttons = new ArrayList<GDSprite>();
		SpriteManager spriteManager = SpriteManager.getInstance();
		background = spriteManager.getSprite("about1");
		background.setPosition(0, 0);
		
		//給回到主頁面的按鈕位置
		menuBtn = spriteManager.getSprite("back_to_menu_button");
		menuBtn.setPosition(150,600);
		buttons.add(menuBtn);
		nextBtn = spriteManager.getSprite("button_next");
		nextBtn.setPosition(600,600);
		buttons.add(nextBtn);
		preBtn = spriteManager.getSprite("button_pre");
		preBtn.setPosition(0,600);
		buttons.add(preBtn);
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
		// TODO �۰ʲ��ͪ���k Stub
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
	
	public static void changebg(int num) {
		SpriteManager spriteManager = SpriteManager.getInstance();
		if(currentbg+num<1||currentbg+num>3) {
			return;
		}
		currentbg=currentbg+num;
		String nextbg = String.valueOf(currentbg);
		//System.out.printf(nextbg);
		background = spriteManager.getSprite("about"+nextbg);
		background.setPosition(0, 0);
		return;
	}

	@Override
	public void resize(int width, int height) {
		// TODO �۰ʲ��ͪ���k Stub
		
	}

	@Override
	public void show() {
		// TODO �۰ʲ��ͪ���k Stub
		
	}

	@Override
	public void hide() {
		// TODO �۰ʲ��ͪ���k Stub
		
	}

	@Override
	public void pause() {
		// TODO �۰ʲ��ͪ���k Stub
		
	}

	@Override
	public void resume() {
		// TODO �۰ʲ��ͪ���k Stub
		
	}

	@Override
	public void dispose() {
		// TODO �۰ʲ��ͪ���k Stub
		
	}

}

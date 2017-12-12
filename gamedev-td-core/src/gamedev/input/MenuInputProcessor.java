package gamedev.input;

import gamedev.screen.MainMenuScreen;
import gamedev.td.GDSprite;
import gamedev.td.TowerDefense;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;

public class MenuInputProcessor extends GDInputProcessor{


	private MainMenuScreen mainMenuScreen;
	private Music music;
	List<GDSprite> buttons;

	Color red = new Color(1,0,0,.5f);
	Color white = new Color(1,1,1,.5f);

	
	public MenuInputProcessor(TowerDefense towerDefense, MainMenuScreen screen){
		super(towerDefense);
		this.mainMenuScreen = screen;
		music = Gdx.audio.newMusic(Gdx.files.internal("assets/menu_bg.ogg"));
		music.play();
	}
	
		
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		buttons = mainMenuScreen.getButtons();
		for (int i = 0; i < buttons.size(); i++) {
			GDSprite sprite = buttons.get(i);
			
			if(Gdx.input.isButtonPressed(Buttons.LEFT))
				if(sprite.contains(screenX, screenY)) {
					switch(i) {
					case MainMenuScreen.START_GAME:
						music.pause();
						towerDefense.switchScreen(towerDefense.getLvlSelectScreen());
						break;
					case MainMenuScreen.LEVEL_SELECT:
						music.pause();
						towerDefense.switchScreen(towerDefense.getSettingScreen());
						break;
					case MainMenuScreen.ABOUT:
						music.pause();
						towerDefense.switchScreen(towerDefense.getAboutScreen());
						break;
					case MainMenuScreen.EXIT:
						System.exit(1);
						break;
					}
				}
		}
		
		return false;
	}
	
	


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		buttons = mainMenuScreen.getButtons();
		for (int i = 0; i < buttons.size(); i++) {
			GDSprite sprite = buttons.get(i);
				if(screenX >= sprite.getX() && screenX < sprite.getX() + sprite.getWidth()
						&& screenY >= sprite.getY() && screenY < sprite.getY() + sprite.getHeight()) {
						
					buttons.get(i).setAlpha(0.8f);
				}
				else buttons.get(i).setAlpha(1);
		}
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}

package gamedev.input;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;

import gamedev.entity.GameState;
import gamedev.screen.GameOverScreen;
import gamedev.screen.MainMenuScreen;
import gamedev.screen.SettingScreen;
import gamedev.screen.LvlSelectScreen;
import gamedev.td.GDSprite;
import gamedev.td.TowerDefense;

public class SettingInputProcessor extends GDInputProcessor{

	

	List<GDSprite> buttons;
	private SettingScreen settingScreen;

	public SettingInputProcessor(TowerDefense towerDefense, SettingScreen screen){
		super(towerDefense);
		this.settingScreen = screen;
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
		// Ĳ���쪺�禡
		
		buttons = settingScreen.getButtons();
		for (int i = 0; i < buttons.size(); i++) {
			GDSprite sprite = buttons.get(i);
			
			if(Gdx.input.isButtonPressed(Buttons.LEFT))
				if(sprite.contains(screenX, screenY)) {
					switch(i) {
					case SettingScreen.MAIN_MENU:
						towerDefense.switchScreen(towerDefense.getMainMenuScreen());
						break;
					
					case SettingScreen.Level_easy:
						System.out.println("easy mode");
						GameState.getInstance().setLevel_Mode(1);
						towerDefense.switchScreen(towerDefense.getLvlSelectScreen());
						break;
					
					case SettingScreen.Level_normal:
						System.out.println("normal mode");
						GameState.getInstance().setLevel_Mode(2);
						towerDefense.switchScreen(towerDefense.getLvlSelectScreen());
						break;
						
					case SettingScreen.Level_hard:
						System.out.println("hard mode");
						GameState.getInstance().setLevel_Mode(4);
						towerDefense.switchScreen(towerDefense.getLvlSelectScreen());
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
		buttons = settingScreen.getButtons();
		for (int i = 0; i < buttons.size(); i++) {
			GDSprite sprite = buttons.get(i);
				if(screenX >= sprite.getX() && screenX < sprite.getX() + sprite.getWidth()
						&& screenY >= sprite.getY() && screenY < sprite.getY() + sprite.getHeight()) {
					
					if(buttons.get(i).equals("menuBtn")){
						buttons.get(i).setAlpha(0.8f);
					}
					else
					buttons.get(i).setAlpha(1);
				}
				else {
					if(buttons.get(i).equals("menuBtn")){
						buttons.get(i).setAlpha(1);
					}
					else
					buttons.get(i).setAlpha(0.7f);
				}
		}
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}

package gamedev.input;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import gamedev.screen.AboutScreen;
import gamedev.screen.SettingScreen;
import gamedev.td.GDSprite;
import gamedev.td.TowerDefense;

public class AboutInputProcessor extends GDInputProcessor{

	

	List<GDSprite> buttons;
	private AboutScreen aboutScreen;//宣告一個設定About螢幕的物件

	public AboutInputProcessor(TowerDefense towerDefense, AboutScreen screen){
		super(towerDefense);
		this.aboutScreen=screen;
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
		// 觸擊到呼叫的函式
		
		buttons = aboutScreen.getButtons();
		for (int i = 0; i < buttons.size(); i++) {
			GDSprite sprite = buttons.get(i);
			
			if(Gdx.input.isButtonPressed(Buttons.LEFT))
				if(sprite.contains(screenX, screenY)) {
					switch(i) {
					case SettingScreen.MAIN_MENU:
						towerDefense.switchScreen(towerDefense.getMainMenuScreen());
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
		buttons = aboutScreen.getButtons();
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

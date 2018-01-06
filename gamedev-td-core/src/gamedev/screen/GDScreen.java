package gamedev.screen;

import gamedev.input.GDInputProcessor;
import com.badlogic.gdx.Screen;

public abstract class GDScreen implements Screen{
	//繼承Screen的子類別
	
	protected GDInputProcessor inputProcessor;
	//宣告一個保護的處理頁面的物件

	//建構子
	public GDInputProcessor getInputProcessor() {
		return inputProcessor;
		//回傳處理這個頁面的物件
	}
	

}

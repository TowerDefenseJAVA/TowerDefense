package gamedev.screen;

import gamedev.input.GDInputProcessor;
import com.badlogic.gdx.Screen;

public abstract class GDScreen implements Screen{
	//�~��Screen���l���O
	
	protected GDInputProcessor inputProcessor;
	//�ŧi�@�ӫO�@���B�z����������

	//�غc�l
	public GDInputProcessor getInputProcessor() {
		return inputProcessor;
		//�^�ǳB�z�o�ӭ���������
	}
	

}

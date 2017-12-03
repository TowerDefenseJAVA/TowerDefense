package gamedev.td;

import com.badlogic.gdx.graphics.Color;

public class Config {
	public static final int tileSize = 40;
	
	public static Color white = new Color(1f,1f,1f,.5f);
	public static Color yellow = new Color(1f,1f,0f,1f);	//slow ailment
	public static Color green = new Color(.5f, 1, .5f, 1f); //potion ailment
	public static Color red = new Color(1,0,0,.5f);		//burned ailment
	public static Color blue = new Color(0f,0f,1f,1f);	//iced ailment
	public static Color normal = new Color(1f,1f,1f, 1f);
	public static Color clear = new Color(0,0,0,1f);
	public static Color clearer = new Color(0,0,0,0f);
}

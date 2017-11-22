package gamedev.entity;

import gamedev.td.GDSprite;
import gamedev.td.SpriteManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tile extends Entity {									//Tile是瓦片的意思 她主要用於拼湊出整個BackGround
	
	public enum TileType {											//Tile的類型 共有五種 第一種是預設類型
		Used, Grass, Dirt, Dirt_Dark, Steve			//  預設/草地/泥土/有陰影的泥土/STEVE角色本身
	}

	public static Tile create(TileType type){		//根據類型 創建出對應的瓦片含圖案
		GDSprite tile = SpriteManager.getInstance().getTile(type);
		Tile t = new Tile(tile);
		return t;
	}
	
	public Tile(GDSprite sprite) {
		super(sprite);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		sprite.draw(spriteBatch);
	}

	public static TileType interpretType(int val) {			//一個數字對應一個類型
		switch(val){
		case -1: return TileType.Used;
		case  0: return TileType.Grass;
		case  1: return TileType.Dirt;
		case  2: return TileType.Dirt_Dark;
		case  3: return TileType.Steve;
		}
		return TileType.Used;
	}

}

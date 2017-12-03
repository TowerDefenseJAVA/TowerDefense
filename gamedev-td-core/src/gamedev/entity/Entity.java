package gamedev.entity;

import gamedev.td.GDSprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {								//實體類別

	protected Vector2 position;								//實體位置
	protected GDSprite sprite;
	protected boolean active;								//實體當前存活狀態
	
	public Entity(GDSprite sprite){
		this.sprite = sprite;
		this.active = false;
	}
	
	public abstract void draw(SpriteBatch spriteBatch);
	
	public void update(float delta){
		sprite.setX(this.position.x);
		sprite.setY(this.position.y);
	}
	
	
	public GDSprite getSprite() {
		return sprite;
	}
	
	public void setSprite(GDSprite sprite) {					
		this.sprite = sprite;
	}
	
	public boolean isActive() {									//判斷該物體是否存活
		return active;
	}
	
	public void setActive(boolean active) {						//設定該物體是否存活
		this.active = active;
	}
	
	public void setPosition(Vector2 position){					//設定該實體位置座標
		this.position = position;
		sprite.setX(position.x);
		sprite.setY(position.y);
	}
	
	
	public Vector2 getPosition() {								//取得該實體座標
		return position;
	}
}

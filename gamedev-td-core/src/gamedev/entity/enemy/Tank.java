package gamedev.entity.enemy;

import gamedev.entity.Enemy;
import gamedev.td.GDSprite;

import java.awt.Point;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tank extends Enemy {

	public Tank(GDSprite sprite, int health, int moneyReward, float speed, List<Point> waypoints) {
		super(sprite, health, moneyReward, speed, waypoints);
		sprite.setSize(50,50);
		
		
	}
	@Override
	public void draw(SpriteBatch spriteBatch) {
		if(active) {
			if(icedAilmentTimer > 0){
			}else if(burnedAilmentTimer > 0) {
			}else if(slowAilmentTimer > 0) {
			}
			else{
			}
			sprite.setRegion(0,0,284,192);
			sprite.setX(this.position.x);
			sprite.setY(this.position.y);
			
			sprite.setFlip(false, true);
			sprite.draw(spriteBatch);
		}
	}

	public void update(float delta) {
		if(active) {
			super.update(delta);
		}
	}
}

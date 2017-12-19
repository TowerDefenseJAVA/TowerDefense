package gamedev.entity.enemy;

import gamedev.entity.Enemy;
import gamedev.td.Config;
import gamedev.td.GDSprite;

import java.awt.Point;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Spider extends Enemy {

	private int condition =0;
	public Spider(GDSprite sprite, int health, int moneyReward, float speed, List<Point> waypoints) {
		super(sprite, health, moneyReward, speed, waypoints);
		sprite.setSize(50,50);
		
		
	}
	@Override
	public void draw(SpriteBatch spriteBatch) {
		if(active) {
			int offset = conditionToInt(condition);
			sprite.setRegion(0,offset,44,44);
			sprite.setX(this.position.x);
			sprite.setY(this.position.y);
			if(icedAilmentTimer > 0){
				condition = 1;
			}else if(burnedAilmentTimer > 0) {
				condition = 2;
			}else if(slowAilmentTimer > 0) {
				condition = 3;
			}
			else{
				condition = 0;
			}
			sprite.setFlip(false, true);
			sprite.draw(spriteBatch);
		}
	}

	public void update(float delta) {
		if(active) {
			super.update(delta);
		}
	}
	private int conditionToInt(int condition) {
		if(condition == 0) {
			return 88;
		}else if(condition == 1) {
			return 44;
		}else if(condition == 2) {
			return 0;
		}else if(condition == 3) {
			return 132;
		}
		return 88;
	}
}

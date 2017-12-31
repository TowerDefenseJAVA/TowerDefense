package gamedev.entity.enemy;

import gamedev.entity.Enemy;
import gamedev.td.Config;
import gamedev.td.GDSprite;

import java.awt.Point;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Horse extends Enemy {

	private int condition;
	public Horse(GDSprite sprite, int health, int moneyReward, float speed, List<Point> waypoints) {
		super(sprite, health, moneyReward, speed, waypoints);
		sprite.setSize(50,50);
		
		
	}
	@Override
	public void draw(SpriteBatch spriteBatch) {
		if(active) {
			if(icedAilmentTimer > 0){
				condition = 1;
			}else if(burnedAilmentTimer > 0) {
				condition = 2;
			}else if(slowAilmentTimer > 0) {
				condition = 3;
			}else if(potionAilmentTimer > 0) {
				condition = 4;
			}
			else{
				condition = 0;
			}
			int offset = conditionToInt(condition);
			sprite.setRegion(0,offset,300,300);
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
	private int conditionToInt(int condition) {
		if(condition == 0) {		//origin
			return 300;
		}else if(condition == 1) {	//iced
			return 600;
		}else if(condition == 2) {	//burned
			return 0;
		}else if(condition == 3) {	//slowed
			return 1200;
		}else if(condition == 4) {	//potioned  TODO
			return 900;
		}
		return 300;
	}
}

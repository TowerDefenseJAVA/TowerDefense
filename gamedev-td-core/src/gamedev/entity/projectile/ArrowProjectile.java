package gamedev.entity.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;

import gamedev.entity.Enemy;
import gamedev.entity.Projectile;
import gamedev.td.GDSprite;

public class ArrowProjectile extends Projectile {

	public ArrowProjectile(GDSprite sprite, Vector2 position, int damage,
			float speed, Enemy target) {
		super(sprite, position, damage, speed, target);
		this.angle = getAngle();
		// TODO Auto-generated constructor stub
		Music music = Gdx.audio.newMusic(Gdx.files.internal("assets/arrow.ogg"));
		music.play();
	}

	protected float getAngle(){
		return super.getAngle() + 45;
	}


}

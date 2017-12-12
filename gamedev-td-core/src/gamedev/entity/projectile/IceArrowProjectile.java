package gamedev.entity.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import gamedev.entity.Enemy;
import gamedev.td.GDSprite;

public class IceArrowProjectile extends ArrowProjectile{

	public IceArrowProjectile(GDSprite sprite, Vector2 position, int damage,
			float speed, Enemy target) {
		super(sprite, position, damage, speed, target);
		this.angle = getAngle();
		this.icedDuration = 1;
		Music music = Gdx.audio.newMusic(Gdx.files.internal("assets/ice.ogg"));
		music.play();
	}
	
	protected float getAngle() {
		return super.getAngle()+45;
	}

	
}

package gamedev.entity.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;

import gamedev.entity.Enemy;
import gamedev.entity.Projectile;
import gamedev.td.GDSprite;

public class EggProjectile extends Projectile{

	public EggProjectile(GDSprite sprite, Vector2 position, int damage,
			float speed, Enemy target) {
		super(sprite, position, damage, speed, target);
		slowDuration = 1;
		Music music = Gdx.audio.newMusic(Gdx.files.internal("assets/egg_crash.ogg"));
		music.play();
	}



}

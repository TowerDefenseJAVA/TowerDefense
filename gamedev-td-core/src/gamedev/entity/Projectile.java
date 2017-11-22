package gamedev.entity;

import gamedev.entity.projectile.ArrowProjectile;
import gamedev.entity.projectile.CorruptedEggProjectile;
import gamedev.entity.projectile.DirtProjectile;
import gamedev.entity.projectile.EggProjectile;
import gamedev.entity.projectile.FireArrowProjectile;
import gamedev.entity.projectile.IceArrowProjectile;
import gamedev.entity.projectile.PotionProjectile;
import gamedev.td.GDSprite;
import gamedev.td.SpriteManager;
import gamedev.td.helper.MathHelper;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Projectile extends Entity {				//塔射出的物件類別
	public enum ProjectileType {
		Dirt, Arrow, Egg, Potion, Cegg, Fire_Arrow, Ice_Arrow	//塔射出的物件共有這幾種
	}
	
	private int damage;											//該物件可造成的傷害
	protected float speed, angle, slowDuration;					//該物件的速度 角度 以及減速
	protected Enemy target;										//該物件瞄準的敵人目標
	
	public Projectile(GDSprite sprite, Vector2 position, int damage, float speed, Enemy target){
		super(sprite);
		this.position = position;								//初始位置
		this.damage = damage;									//初始傷害
		this.speed = speed;										//初始速度
		this.target = target;									//初始目標
		this.angle = getAngle();								//初始角度
		slowDuration = 0;										//緩速時間歸零
		active = true;											//設定物件為 存活
	}
	
	public void draw(SpriteBatch spriteBatch){
		if(active){
			sprite.setRotation(angle);
			sprite.draw(spriteBatch);
		}
	}
	
	
	public void update(float delta){				//update函式用於更新當前物件的狀況
		if(active){
			super.update(delta);
			angle = getAngle();
			if(!target.active)		//如果目標已經死亡 則瞄準該敵人的物件也將自行死亡
				active = false;
			boolean collided = false;	//產生一個變數用於判斷是否碰撞到敵人
			GameState state = GameState.getInstance();			//透過getInstance取得當前所有敵人狀態
			for(Enemy enemy : state.getEnemies()){				//將每一個敵人都確認過
				if(enemy.active){								//在敵人活著的前提之下
					collided = checkCollision(enemy);			//逐一確定是否有與任一敵人產生碰撞
					if(collided && active){						//如果有產生碰撞 且此物件本身仍為存活狀態
						enemy.damagedBySource(this.damage);		//對敵人造成 damage的傷害
						enemy.slowedBySource(this.slowDuration);//對敵人造成slowDuration秒數的緩速延遲效果
						this.active = false;					//由於已經打到敵人  因此將此物件殺死
					}
				}
			}
			if(!collided){										//若仍然沒有與敵人產生碰撞  則繼續移動接近目標
				moveProjectile(target);
				
			}
		}
	}
	
	private void moveProjectile(Enemy target) {									//moveProjectile 用於移動此物件
		Vector2 enemyPosition = MathHelper.getCenterOfTile(target.position);	//取得目標當前位置
		Vector2 center = MathHelper.getCenterOfSprite(sprite);					//取得自己當前位置
		if(enemyPosition.x > center.x){			//若對方在自己右邊  則自己往右邊移動 speed 的位移
			position.x += speed;
		}
		else if (enemyPosition.x < center.x){	//若對方在自己左邊 則自己往左邊移動 speed 的位移
			position.x -= speed;
		}
		if(enemyPosition.y > center.y){			//若對方在自己上面 則自己往上方移動 speed 的位移
			position.y += speed;
		}
		else if (enemyPosition.y < center.y){	//若對方在自己下面 則自己往下方移動 speed 的位移
			position.y -= speed;
		}
	}

	private boolean checkCollision(Enemy target) {								//用於判斷自己是否有與目標產生碰撞
		Rectangle minRect = sprite.getBoundingRectangle();
		return minRect.overlaps(target.getSprite().getBoundingRectangle());
		
	}

	public static Projectile createProjectile(Tower tower, ProjectileType type, Enemy target){	//根據塔的種類產生對應的物件 並同時給予敵人位置
		Projectile projectile = null;
		SpriteManager handler = SpriteManager.getInstance();
		GDSprite projectileSprite = handler.getProjectile(type);		
		Vector2 position = new Vector2(tower.getPosition());			//由於物件是由塔產生 故初始位置與塔位置相同
		int damage = tower.getDamage();									//賦予該物件 等同 塔 的攻擊力
		float speed;
		
		switch(type){ 													//根據物件的種類 來給予對應初始值
			case Dirt:				//回傳一個速度為 3 的Dirt物件
				speed = 3f;
				return new DirtProjectile(projectileSprite, position, damage, speed, target);
			case Arrow:				//回傳一個速度為 5 的Arrow物件
				speed = 5f;
				return new ArrowProjectile(projectileSprite, position, damage, speed, target);
			case Egg:				//回傳一個速度為 3 的Egg物件
				speed = 3f;
				return new EggProjectile(projectileSprite, position, damage, speed, target);
			case Potion:			//回傳一個速度為 3 的Potion物件
				speed = 3f;
				return new PotionProjectile(projectileSprite, position, damage, speed, target);
			case Cegg:				//回傳一個速度為 4 的Cegg物件
				speed = 4f;
				return new CorruptedEggProjectile(projectileSprite, position, damage, speed, target);
			case Fire_Arrow:		//回傳一個速度為 6 的 Fire_Arrow物件
				speed = 6f;
				return new FireArrowProjectile(projectileSprite, position, damage, speed, target);
			case Ice_Arrow:			//回傳一個速度為 6 的Ice_Arrow物件
				speed = 6f;
				return new IceArrowProjectile(projectileSprite, position, damage, speed, target);
				
		}
		
		return projectile;			//若type皆不屬於以上幾種 則回傳一個原本的沒有初始化的projectile  *可能需要再確認*
	}
	
	public static ProjectileType interpretTypeFromTowerName(String name) {	//該function用於根據塔的種類 取得 物件的種類名稱
		if(name.equals("Dirt Tower"))					//若塔為Dirt Tower則產生的物件為Dirt 
			return ProjectileType.Dirt;					//
		else if(name.equals("Arrow Tower"))				//若塔為Arrow Tower則產生的物件為Arrow
			return ProjectileType.Arrow;				//
		else if(name.equals("Egg Tower"))				//若塔為Egg Tower則產生的物件為Egg
			return ProjectileType.Egg;					//
		else if(name.equals("Potion Tower"))			//若塔為Potion Tower則產生的物件為Potion
			return ProjectileType.Potion;				//
		else if(name.equals("Corrupted Egg Tower"))		//若塔為Corrupted Tower則產生的物件為Cegg
			return ProjectileType.Cegg;					//
		else if(name.equals("Ice Arrow Tower"))			//若塔為IceArrowTower則產生的物件為Ice Arrow
			return ProjectileType.Ice_Arrow;			//
		else if(name.equals("Fire Arrow Tower"))		//若塔為FireArrowTower則產生的物件為Fire Arrow
			return ProjectileType.Fire_Arrow;			//
		return null; 									//若以上種類皆不是 則回傳NULL
	}
	
	protected float getAngle(){									//取得與怪物的相對位置射擊角度
		Vector2 targetCenter = MathHelper.getCenterOfTile(target.getPosition());
		
		float deltaX = (float)(this.position.x - targetCenter.x);
		float deltaY = (float)(this.position.y - targetCenter.y);
		float angleInDegrees = (float) (Math.atan2(deltaY, deltaX) * 180 / Math.PI);
		
		return angleInDegrees;
		
	}
	
}

package gamedev.entity;

import gamedev.entity.enemy.Dragon;
import gamedev.entity.enemy.Horse;
import gamedev.entity.enemy.Potato;
import gamedev.entity.enemy.Skeleton;
import gamedev.entity.enemy.Slime;
import gamedev.entity.enemy.Spider;
import gamedev.entity.enemy.Tank;
import gamedev.td.Config;
import gamedev.td.GDSprite;
import gamedev.td.SpriteManager;
import gamedev.td.helper.MathHelper;

import java.awt.Point;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Enemy extends Entity {		//Enemy型態的物件
	protected enum Dir {
		LEFT, RIGHT, UP, DOWN						//Enemy移動時只會有四個Direction方向 左右上下
	}

	public enum EnemyType {							//Enemy種類  目前只有蜘蛛   骷髏   *可增加*
		Spider, Skeleton, Tank, Dragon, Potato, Slime, Horse
	}

	protected float angle;
	private int health;
	private final int maxHealth;
	private int moneyReward;
	protected float speed;
	protected float slowedSpeed;
	protected float icedSpeed;
	protected float potionAilmentTimer = 0;
	protected float burnedAilmentTimer =0;
	protected float potionColdDown = 0;
	protected float burnedColdDown = 0;
	protected float icedAilmentTimer =0;
	protected List<Point> waypoints;
	protected Dir dir;
	protected float slowAilmentTimer = 0;

	// enemy factory pattern
	/**
	 * The enemy factory pattern allows you to create the enemy
	 * @param type
	 * @param waypoints
	 * @return
	 */
	public static Enemy createEnemy(EnemyType type) {					//產生Enemy物件
		Enemy enemy = null;
		
		GameState state = GameState.getInstance();						//取得當前遊戲狀態
		List<Point> waypointList = state.getWaypoints();				//取得可以行走的路徑 將各點存在ArrayList中

		SpriteManager handler = SpriteManager.getInstance();			
		GDSprite sprite = handler.getEnemy(type);

		int health;					//怪物血量
		int moneyReward;			//擊殺金錢獎勵
		float speed;				//怪物速度

		switch (type) {				//根據怪物種類 給予不同的初始值
		
		
		case Spider:				//怪物為  蜘蛛
			health = 100;
			moneyReward = 50;
			speed = 0.5f;
			enemy = new Spider(sprite, health, moneyReward, speed, waypointList);
			return enemy;
			
		case Skeleton:				//怪物為 骷髏
			health = 250;
			moneyReward = 100;
			speed = 1;
			enemy = new Skeleton(sprite, health, moneyReward, speed, waypointList);
			return enemy;
		case Tank:				//怪物為  坦克
			health = 500;
			moneyReward = 500;
			speed = 0.3f;
			enemy = new Tank(sprite, health, moneyReward, speed, waypointList);
			return enemy;
		case Dragon:  //怪物為 龍
			health = 500;
			moneyReward = 5000;
			speed = 0.5f;
			enemy = new Dragon(sprite,health,moneyReward,speed,waypointList);
			return enemy;
		case Horse:  //怪物為 馬
			health = 500;
			moneyReward = 5000;
			speed = 0.5f;
			enemy = new Horse(sprite,health,moneyReward,speed,waypointList);
			return enemy;
		case Slime:  //史萊姆
			health = 500;
			moneyReward = 5000;
			speed = 0.5f;
			enemy = new Slime(sprite,health,moneyReward,speed,waypointList);
			return enemy;
		case Potato:  //馬鈴薯
			health = 500;
			moneyReward = 5000;
			speed = 0.5f;
			enemy = new Potato(sprite,health,moneyReward,speed,waypointList);
			return enemy;
			
		default:					//非 預設的怪物 回傳enemy型態  *可能另有她用*
			return enemy;
		}

	}

	protected Enemy(GDSprite sprite, int health, int moneyReward, float speed, List<Point> waypointList) {
		super(sprite);
		this.active = true;												//設定怪物為 活著
		this.angle = 0;													//初始角度為0
		this.health = health;											//設定怪物血量
		this.maxHealth = health;
		this.moneyReward = moneyReward;									//設定金幣獎賞數量
		this.speed = speed;												//設定怪物速度
		this.icedSpeed = 0;
		this.slowedSpeed = speed * .6f;									//設定怪物遭緩速debuff時的移動速度
		this.position = Vector2.Zero;									//初始化怪物位置為 ZERO這個位置  尚未定義
		this.waypoints = waypointList;									//給予怪物一條 可以行走的路徑值
		setPosition(MathHelper.PointToVector2(waypointList.get(0)));	//預設怪物最初的起始點 為 可行走路徑的第一點
		
	}

	public void update(float delta) {									//更新怪物當前狀態
		super.update(delta);
		checkIfReachedThePlayer(delta);									//用於檢查怪物是否已經走到終點
		float actualSpeed = getSpeed(delta);							//取得怪物當前實際速度
		damagedByPotion(delta);											//若當前為中毒狀態則扣除總血量10%
		damagedByBurned(delta);
		if (!waypoints.isEmpty()) {										//當還沒有走到終點時
			Point waypoint = waypoints.get(0);							//取得下一個可以走的位置

			if (position.x > waypoint.x)								//若該點在左邊  則往左走
				dir = Dir.LEFT;			
			else if (position.x < waypoint.x)							//若該點在右邊  則往右走
				dir = Dir.RIGHT;
			else if (position.y > waypoint.y)							//若該點在下面  則往下面
				dir = Dir.UP;	
			else if (position.y < waypoint.y)							//若該點在上面  則往上走
				dir = Dir.DOWN;

			if (dir == Dir.LEFT) {										//往左邊走
				angle = 180;											//移動角度為 180度
				position.x -= actualSpeed;								//當前位置 X 座標 往左邊 移動 ActualSpeed個單位
				if (position.x <= waypoint.x)							//若超出目標點的 X 座標 則強制修正回目標點的 X 座標
					position.x = waypoint.x;
			} else if (dir == Dir.RIGHT) {								//往右邊走
				angle = 0;												//移動角度為 0 度
				position.x += actualSpeed;								//當前位置 X 座標 往右邊 移動 ActualSpeed個單位
				if (position.x >= waypoint.x)							//若超出目標點的X座標 則強制修正回目標點的X座標
					position.x = waypoint.x;							
			} else if (dir == Dir.UP) {									//往下面走
				angle = 270;											//移動角度為270度
				position.y -= actualSpeed;								//當前位置Y 座標 往下方移動 ActualSpeed個單位
				if (position.y <= waypoint.y)							//若超出目標點的Y座標 則強制修正回目標點的Y座標
					position.y = waypoint.y;
			} else if (dir == Dir.DOWN) {								//往上面走
				angle = 90;												//移動角度為90度
				position.y += actualSpeed;								//當前位置Y座標 往上方移動ActualSpeed個單位
				if (position.y >= waypoint.y)							//若超出目標點的Y座標 則強制修正回目標點的Y座標
					position.y = waypoint.y;
			}

			if (position.x == waypoint.x && position.y == waypoint.y) {	//當抵達目標點時 將Enemy中可行走的當前目標點移除
				waypoints.remove(0);									//並由原本的Waypoints.get(1)變為Waypoints.get(0)
			}
		}
	}

	/**
	 * This method runs when the enemy has reached the player.
	 * 
	 * @param delta
	 */
	protected void checkIfReachedThePlayer(float delta) {				//檢查怪物是否抵達終點
		if (waypoints.size() == 0 && isActive()) {						//所有可以行走的路徑都已經走完 且 怪物當前為 存活
			setActive(false);											//將此怪物殺死
			GameState.getInstance().getDamaged();						//並同時 對玩家造成傷害
		}
	}

	public void draw(SpriteBatch spriteBatch) {							//
		if (active) {
			sprite.setX(this.position.x);
			sprite.setY(this.position.y);
			sprite.setRotation(this.angle);
			if(burnedAilmentTimer > 0) {
				sprite.setColor(Config.red);
			}
			else if(icedAilmentTimer > 0) {
				sprite.setColor(Config.blue);
			}
			else if(slowAilmentTimer > 0){
				sprite.setColor(Config.yellow);
			}
			else if(potionAilmentTimer > 0) {
				sprite.setColor(Config.green);
			}
			else{
				sprite.setColor(Config.normal);
			}
			sprite.draw(spriteBatch);
		}
	}
	
	protected float getSpeed(float delta){								//取得當前速度
		if(this.icedAilmentTimer >0) {
			icedAilmentTimer -= delta;
			return icedSpeed;
		}
		else if(slowAilmentTimer > 0){										//若當前為被緩速的狀態
			slowAilmentTimer -= delta;									//緩速持續時間減少 delta
			return slowedSpeed;											//回傳 被緩速時的速度
		}
		else
			return speed;												//回傳 正常速度
	}
	
	public void slowedBySource(float time){								//設定被緩速持續時間
		slowAilmentTimer = time;
	}
	
	public void icedBySource(float time) {
		icedAilmentTimer = time;
	}
	
	public void burnedBySource(float time) {
		burnedAilmentTimer = time;
	}
	
	public void potionBySource(float time) {
		potionAilmentTimer = time;
	}
	
	public void damagedBySource(int damage){							//怪物被物件攻擊時 扣除damage值的血量
		health -= damage;
		if(health <= 0){												//當怪物生命值歸零時 
			this.active = false;										//怪物狀態變更為 死亡
			GameState state = GameState.getInstance();					//取得當前遊戲狀態
			state.addMoney(this.moneyReward);							//為玩家添加相對應的金錢獎勵
		}
	}
	
	public void damagedByPotion(float delta) {
		if(this.potionColdDown <= 0 && this.active == true) {
			if(potionAilmentTimer > 0) {
				potionAilmentTimer -= delta;
				health -= 0.1*maxHealth;
			}
			if(health<=0 && this.active == true) {
				this.active = false;
				GameState state = GameState.getInstance();
				state.addMoney(this.moneyReward);
			}
			potionColdDown = 1;
		}else {
			potionColdDown -= delta;
		}
		
	}
	
	public void damagedByBurned(float delta) {
		if(this.burnedColdDown <= 0 && this.active == true) {
			if(burnedAilmentTimer > 0) {
				burnedAilmentTimer -= delta;
				health -= 0.2*maxHealth;
			}
			if(health<=0 && this.active == true) {
				this.active = false;
				GameState state = GameState.getInstance();
				state.addMoney(this.moneyReward);
			}
			burnedColdDown = 1;
		}else {
			burnedColdDown -= delta;
		}
	}
	
	public static EnemyType interpretType(int type){				//根據輸入的type來回傳對應的怪物EnumType類型
		switch(type - 1){
			case 0:
				return EnemyType.Spider;
			case 1:
				return EnemyType.Skeleton;
			case 2:
				return EnemyType.Tank;
			case 3:
				return EnemyType.Dragon;
			case 4:
				return EnemyType.Slime;
			case 5:
				return EnemyType.Horse;
			case 6:
				return EnemyType.Potato;
			default:
				return EnemyType.Spider;
		}
	}
	

	public int getMoneyReward() {				//回傳擊殺怪物可獲得的金錢
		return moneyReward;
	}


	public List<Point> getWaypoints() {			//回傳怪物可以走的ArrayList路徑點
		return waypoints;
	}

	public void addPath(List<Point> points) {	//為怪物添加 可以走的ArrayList路徑點
		waypoints = points;
	}

	public Dir getDir() {						//取得怪物移動方向
		return dir;
	}

	public void setDir(Dir dir) {				//設定怪物移動方向
		this.dir = dir;
	}

	public float getAngle() {					//設定怪物移動角度
		return this.angle;
	}
	
}

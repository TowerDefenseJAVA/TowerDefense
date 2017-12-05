package gamedev.entity;

import gamedev.entity.Enemy.EnemyType;
import gamedev.entity.Tile.TileType;
import gamedev.level.Level;
import gamedev.level.Map;
import gamedev.td.Config;
import gamedev.td.helper.MathHelper;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameState {										//此Class用於管理當前遊戲當前整體狀況
	private static GameState instance;

	public static final int GRIDX = 17, GRIDY = 12;

	/**
	 * One full round lasts thirty (30) seconds.
	 */
	private static final float ROUND_DURATION = 60;				//回合持續時間

	private static final float PRE_ROUND_WAIT_DURATION = 5;		//每回合等待時間

	private Level currentLevel;									//用於存取當前關卡數
	private int mapType;										//用於存取當前是第幾張地圖

	private int level;											//關卡
	private int money = 0;										//預設金錢
	private int playerLife = 10;								//預設玩家生命值
	private int spawnedEnemies;									//已經產生的怪物數量

	private TileType grid[][];
	private float roundTime, spawnDelay;						//回合時間   及   每波怪物的出生延遲時間

	private List<Enemy> enemies;								//ArrayList用於紀錄怪物
	private List<Integer> enemiesToBeSpawned;					//ArrayList用於儲存每波要生的怪物數量
	private List<Tower> deployedTowers;							//ArrayList用於紀錄已經建好的塔資訊
	private List<Projectile> projectiles;						//ArrayList用於紀錄塔射出的物件資訊

	private boolean roundHasStarted;							//用於確認回合是否已經開始

	public static GameState getInstance() {						//回傳當前遊戲狀況
		if (instance == null)
			instance = new GameState();
		return instance;
	}

	/**
	 * Game state cannot be instantiated outside of the class. To get a reference to this object, call the static method getInstance().
	 */
	//遊戲當前狀況無法透過GameState以外的Class更改  當需要取得當前遊戲狀況時 透過呼叫GameState會回傳Instance僅供讀取
	private GameState() {
		instance = this;
		createMap();
	}

	private void createMap() {								//產生地圖
		grid = new TileType[GRIDX][GRIDY];
		for (int i = 0; i < GRIDX; i++) {
			for (int j = 0; j < GRIDY; j++) {
				grid[i][j] = TileType.Used;
			}
		}
	}

	public void initialize() {								//將遊戲狀態初始化
		newRoundInitialization();							//將回合資訊初始化
		level = 1;											//將關卡重新設定至第一關
		currentLevel = Level.generateLevel(level);			//當前關卡設定為 第一關 並且產生第一關的地圖等 相關資訊
		money = 1000;										//最初金錢為1000
		playerLife = 10;									//最初玩家生命值為10點
		roundTime = PRE_ROUND_WAIT_DURATION;				//回合持續時間 設定為 每回合間等待時間 時間一到結束該回合
		deployedTowers = new ArrayList<Tower>();			//將所有塔狀態 初始化為 沒有建立任何塔的狀態
		
	}
	
	public void newRoundInitialization(){					//用於將回合資訊初始化
		spawnDelay = 0;										//同一波敵人中 每一隻敵人產生的間隔時間
		spawnedEnemies = 0;									//已經產生的怪物為 0隻
		enemiesToBeSpawned = new ArrayList<Integer>();		//用於讀取每波必須要產生的怪物總數量
		enemies = new ArrayList<Enemy>();					//用於紀錄當前已經產生的怪物資訊
		projectiles = new ArrayList<Projectile>();			//用於記錄當前已經產生的投擲物資訊
		
	}
	
	public void update(float delta) {						//用於更新當前遊戲狀態
		updateRoundTimer(delta);

		if (roundHasStarted) {								//若回合為開始的狀態
			checkForEnemySpawn(delta);						//檢查是否要出怪

			for (Enemy enemy : enemies)						//更新每一隻已經產生的怪物資訊
				enemy.update(delta);

			for (Tower tower : deployedTowers)				//更新每一座蓋好的塔的資訊
				tower.update(delta);
			
			for(Projectile projectile : projectiles)		//更新每一個塔的投擲物的資訊
				projectile.update(delta);
			
		}
		
	}

	private void updateRoundTimer(float delta) {			//用於更新遊戲狀況
		if (roundTime > 0) {								//若該回合還有剩餘遊戲時間
			roundTime -= delta;								//繼續保持下去 不進行任何動作
		}
		else {												//若該回合已經沒有剩餘時間 即可以結束
			roundHasStarted = true;							//剛才有進行遊戲回合
			roundTime = ROUND_DURATION;						//重新設定roundTime 給予新的一輪遊戲時間
			
			prepareLevel(level++);							//準備下一關
			spawnedEnemies = 0;								//將已經產生的怪物數量歸零
		}
	}

	public void render(SpriteBatch spriteBatch) {			//
		displayMap(spriteBatch);							//用於顯示地圖
		displayEnemies(spriteBatch);						//用於顯示敵人
		displayTowers(spriteBatch); 						//用於顯示塔
		displayProjectiles(spriteBatch);					//用於顯示投擲物
	}
	
	private void displayProjectiles(SpriteBatch spriteBatch) {		//用於顯示投擲物
		spriteBatch.begin();
		for(Projectile projectile : projectiles){					//每一個投擲物件都run過
			projectile.draw(spriteBatch);							//繪製出每一個投擲物件
		}
		spriteBatch.end();
		
	}

	private void displayTowers(SpriteBatch spriteBatch) {			//用於顯示塔
		spriteBatch.begin();
		for(Tower tower : deployedTowers) {							//每一座塔都run過
			tower.draw(spriteBatch);								//繪製出每一座塔
		}
		
		spriteBatch.end();
	}

	private void displayEnemies(SpriteBatch spriteBatch){			//用於顯示怪物
		spriteBatch.begin();
		for(Enemy enemy : enemies){									//每一個敵人都run過
			enemy.draw(spriteBatch);								//繪製出每一個怪物
		}
		
		spriteBatch.end();
	}

	private void displayMap(SpriteBatch spriteBatch) {				//用於繪製地圖
		spriteBatch.begin();
		for (int i = 0; i < grid.length; i++) {						//繪製出整個地圖
			for (int j = 0; j < grid[i].length; j++) {
				TileType type = grid[i][j];

				Tile newTile = Tile.create(type);
				Vector2 position = new Vector2(i * Config.tileSize, j * Config.tileSize);
				newTile.setPosition(position);
				newTile.draw(spriteBatch);
			}
		}
		spriteBatch.end();
	}

	public boolean checkProjectileCollision() {//用於檢查投擲物是否與怪物產生碰撞

		return false;
	}


	/*
	 * TODO prepare enemies gets the list of enemies, instances e.g. { {1,2} , {2,1} , {1,2} } 2 spiders, 1 skeleton, 2 spiders in order
	 */

	public void checkForEnemySpawn(float delta) {		//用於檢察怪物是否需要繼續產生怪物
		spawnDelay += delta;

		if (spawnDelay >= .5 && spawnedEnemies < enemiesToBeSpawned.size()) {	//若當前已生成的怪物少於該波應生成的怪物總數

			EnemyType type = Enemy.interpretType(enemiesToBeSpawned.get(spawnedEnemies));	//取得接下來要生成的怪物類型
			Enemy enemy = Enemy.createEnemy(type);				//利用怪物類型產生對應的怪物
			enemies.add(enemy);									//將新增的怪物加入ArrayList中
			spawnDelay = 0;										
			++spawnedEnemies;									//已生成的怪物數量+1
		}
	}

	/**
	 * This should be called after every round.
	 * �C�@�Ӧ^�X����
	 */
	public void prepareLevel(int lvl) {												//用於準備關卡
		newRoundInitialization();													//先對新關卡進行初始化的動作
		currentLevel = Level.generateLevel(lvl);									//當前關卡 設定為新產生的關卡
		if((enemiesToBeSpawned = currentLevel.getEnemiesToBeSpawned()) == null){	//若當前關卡要出的怪物數量為NULL
			prepareLevel(lvl++);													//準備下一關
		}else enemiesToBeSpawned = currentLevel.getEnemiesToBeSpawned();			//取得目前關卡要出的怪物數量
		
	}


	public void deployTower(Tower tower) {						//用於建立新的塔
		if (canBuyTower(tower)) {								//若玩家可以購買新的塔
			money -= tower.getCost();							//扣除玩家金錢
			deployedTowers.add(tower);							//蓋好新的塔 並將其加入arraylist中
		}
	}

	public boolean canBuyTower(Tower tower) {					//用於確認玩家是否買得起新的塔
		return money >= tower.getCost();
	}

	//�]�w�@�i�X��ͥX
	public void setWaveSpawnTime(float waveSpawnTime) {			//設定每波怪物出生時間
		if (waveSpawnTime < 0)									//若傳入非法參數
			this.roundTime = 5;									//則預設為5秒
		else													//若傳入合法參數
			this.roundTime = waveSpawnTime;						//則設定為傳入的參數
	}

	//����
	public void getDamaged() {									//當怪物抵達終點 對玩家造成傷害
		playerLife--;											//扣除一點生命值
	}
	
	public List<Projectile> getProjectiles(){					//用於取得投擲物件列表
		return projectiles;
	}

	public Level getCurrentLevel() {							//取得當前關卡資訊
		return currentLevel;
	}

	public int getPlayerLife() {								//取得玩家剩餘生命值
		return playerLife;
	}

	public List<Enemy> getEnemies() {							//取得敵人資訊列表
		return enemies;
	}
	
	public float getRoundTime() {								//取得回合時間
		return roundTime;
	}
	
	public int getMoney() {										//取得玩家金錢資訊
		return money;
	}
	
	public void addMoney(int bounty){							//增加玩家金錢
		this.money += bounty;
	}

	public List<Tower> getDeployedTowers() {					//取得已經建立的塔 列表
		return deployedTowers;
	}
	
	public List<Point> getWaypoints(){							//取得當前地圖可以走的路徑點列表
		return Map.getInstance().getWaypoints(mapType);
	}
	
	public void setMap(int type){								//產生編號type的地圖
		mapType = type;
		this.grid = Map.generateMap(type);
		
	}
	

	public boolean isTowerPlaceable(Point point) {				//用於確認該目標點是否可以建造塔
		try {
			return point.x > 0 && point.y > 0 &&  ( (grid[point.x / 40][point.y / 40] == TileType.floor_yellow) || (grid[point.x / 40][point.y / 40] == TileType.glass_special)) ;	//座標合法 且 該座標並非怪物行走路徑		
		}catch (Exception e){
			
		}
		return false;
	}

	public void buildTower(Tower towerToBuild, Point point) {	//給定欲建構的塔資訊  以及塔的建構座標
		// grid[point.x / 40][point.y / 40] = TileType.Used;

		Vector2 position = MathHelper.PointToVector2(point);	//取得塔的建構位置
		towerToBuild.setPosition(position);						//設定塔的建構位置

		towerToBuild.setCenter((float) point.x + Config.tileSize / 2, (float) point.y + Config.tileSize / 2);
		towerToBuild.getPosition().set(MathHelper.PointToVector2(point));
		deployTower(towerToBuild);								//建構該塔
	}
}

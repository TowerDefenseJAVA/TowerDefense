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

public class GameState {										//甇七lass��蝞∠�������擃�瘜�
	private static GameState instance;

	public static final int GRIDX = 17, GRIDY = 12;

	/**
	 * One full round lasts thirty (30) seconds.
	 */
	private static final float ROUND_DURATION = 60;				//��������

	private static final float PRE_ROUND_WAIT_DURATION = 5;		//瘥�������

	private Level currentLevel;									//��摮�����
	private int mapType;										//��摮���蝚砍嗾撘萄���
	
	private int Level_Mode=1;
	private int level;											//��
	private int money = 0;										//��身��
	private int playerLife = 10;								//��身�摰嗥���
	private int spawnedEnemies;									//撌脩�����芰����

	private TileType grid[][];
	private TileType record[][]= new TileType[GRIDX][GRIDY];
	private float roundTime, spawnDelay;						//������   ���   瘥郭�芰����辣�����

	private List<Enemy> enemies;								//ArrayList��蝝����芰
	private List<Integer> enemiesToBeSpawned;					//ArrayList���摮�郭閬���芰����
	private List<Tower> deployedTowers;							//ArrayList��蝝���歇蝬遣憟賜�����
	private List<Projectile> projectiles;						//ArrayList��蝝�������隞嗉���

	private boolean roundHasStarted;							//��蝣箄����撌脩����

	public static GameState getInstance() {						//��������瘜�
		if (instance == null)
			instance = new GameState();
		return instance;
	}

	/**
	 * Game state cannot be instantiated outside of the class. To get a reference to this object, call the static method getInstance().
	 */
	private GameState() {
		instance = this;
		createMap();
	}

	private void createMap() {								
		grid = new TileType[GRIDX][GRIDY];
		for (int i = 0; i < GRIDX; i++) {
			for (int j = 0; j < GRIDY; j++) {
				grid[i][j] = TileType.Used;
				record[i][j] = TileType.Used;
			}
		}
	}
	public void restart()
	{
		for (int i = 0; i < GRIDX; i++) {
			for (int j = 0; j < GRIDY; j++) {
				record[i][j] = TileType.Used;
			}
		}
	}
	public void Upgrade(List<Tower> list)
	{
		
	}
	public void initialize() {								
		newRoundInitialization();							
		level = 1;											
		currentLevel = Level.generateLevel(level);			
		money = 1000;										
		playerLife = 10;									
		roundTime = PRE_ROUND_WAIT_DURATION;				
		deployedTowers = new ArrayList<Tower>();			
		
	}
	
	public void newRoundInitialization(){					
		spawnDelay = 0;										
		spawnedEnemies = 0;									
		enemiesToBeSpawned = new ArrayList<Integer>();		
		enemies = new ArrayList<Enemy>();					
		projectiles = new ArrayList<Projectile>();			
		
	}
	
	public void update(float delta) {						
		updateRoundTimer(delta);

		if (roundHasStarted) {								
			checkForEnemySpawn(delta);						

			for (Enemy enemy : enemies)						
				enemy.update(delta);

			for (Tower tower : deployedTowers)				
				tower.update(delta);
			
			for(Projectile projectile : projectiles)		
				projectile.update(delta);
			
		}
		
	}

	private void updateRoundTimer(float delta) {			
		if (roundTime > 0) {								
			roundTime -= delta;								
		}
		else {												
			roundHasStarted = true;							
			roundTime = ROUND_DURATION;						
			
			prepareLevel(level++);							
			spawnedEnemies = 0;								
		}
	}

	public void render(SpriteBatch spriteBatch) {			
		displayMap(spriteBatch);							
		displayEnemies(spriteBatch);						
		displayTowers(spriteBatch); 						
		displayProjectiles(spriteBatch);					
	}
	
	private void displayProjectiles(SpriteBatch spriteBatch) {		
		spriteBatch.begin();
		for(Projectile projectile : projectiles){					
			projectile.draw(spriteBatch);							
		}
		spriteBatch.end();
		
	}

	private void displayTowers(SpriteBatch spriteBatch) {			
		spriteBatch.begin();
		for(Tower tower : deployedTowers) {							
			tower.draw(spriteBatch);								
		}
		
		spriteBatch.end();
	}

	private void displayEnemies(SpriteBatch spriteBatch){			
		spriteBatch.begin();
		for(Enemy enemy : enemies){									
			enemy.draw(spriteBatch);								
		}
		
		spriteBatch.end();
	}

	private void displayMap(SpriteBatch spriteBatch) {				
		spriteBatch.begin();
		for (int i = 0; i < grid.length; i++) {						
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

	public boolean checkProjectileCollision() {

		return false;
	}


	/*
	 * TODO prepare enemies gets the list of enemies, instances e.g. { {1,2} , {2,1} , {1,2} } 2 spiders, 1 skeleton, 2 spiders in order
	 */

	public void checkForEnemySpawn(float delta) {
		spawnDelay += delta;

		if (spawnDelay >= .5 && spawnedEnemies < enemiesToBeSpawned.size()) {

			EnemyType type = Enemy.interpretType(enemiesToBeSpawned.get(spawnedEnemies));	
			Enemy enemy = Enemy.createEnemy(type);				
			enemies.add(enemy);									
			spawnDelay = 0;										
			++spawnedEnemies;									
		}
	}

	/**
	 * This should be called after every round.
	 * 嚙瘠嚙瑾嚙諉回嚙碼嚙踝蕭嚙踝蕭
	 */
	public void prepareLevel(int lvl) {												
		newRoundInitialization();													
		currentLevel = Level.generateLevel(lvl);									
		if((enemiesToBeSpawned = currentLevel.getEnemiesToBeSpawned()) == null){	
			prepareLevel(lvl++);													
		}else enemiesToBeSpawned = currentLevel.getEnemiesToBeSpawned();			
		
	}


	public void deployTower(Tower tower) {						
		if (canBuyTower(tower)) {								
			money -= tower.getCost();							
			deployedTowers.add(tower);							
		}
	}

	public boolean canBuyTower(Tower tower) {					
		return money >= tower.getCost();
	}
	public boolean canUpgradeTower(Tower tower) {
		return money >= 100;
	}
	//嚙稽嚙緩嚙瑾嚙箠嚙碼嚙踝蕭穸X
	public void setWaveSpawnTime(float waveSpawnTime) {			
		if (waveSpawnTime < 0)								
			this.roundTime = 5;								
		else													
			this.roundTime = waveSpawnTime;						
	}

	//嚙踝蕭嚙踝蕭
	public void getDamaged() {									
		playerLife--;									
	}
	
	public List<Projectile> getProjectiles(){				
		return projectiles;
	}

	public Level getCurrentLevel() {						
		return currentLevel;
	}

	public int getPlayerLife() {							
		return playerLife;
	}

	public List<Enemy> getEnemies() {						
		return enemies;
	}
	
	public float getRoundTime() {					
		return roundTime;
	}
	
	public int getMoney() {						
		return money;
	}
	
	public void addMoney(int bounty){					
		this.money += bounty;
	}

	public List<Tower> getDeployedTowers() {				
		return deployedTowers;
	}
	
	public List<Point> getWaypoints(){							
		return Map.getInstance().getWaypoints(mapType);
	}
	
	public void setMap(int type){							
		mapType = type;
		this.grid = Map.generateMap(type);
		
	}
	public void setLevel_Mode(int setLevel)
	{
		Level_Mode = setLevel;
	}
	
	public int getLevel_Mode()
	{
		return Level_Mode;
	}

	public boolean isTowerPlaceable(Point point) {			
		try {
			return point.x > 0 && point.y > 0 &&  ( (grid[point.x / 40][point.y / 40] == TileType.floor_yellow) || (grid[point.x / 40][point.y / 40] == TileType.glass_special))&& (record[point.x / 40][point.y / 40] != TileType.floor_yellow) ;			
		}catch (Exception e){
			
		}
		return false;
	}

	public void buildTower(Tower towerToBuild, Point point) {	
		 

		Vector2 position = MathHelper.PointToVector2(point);	
		towerToBuild.setPosition(position);						

		towerToBuild.setCenter((float) point.x + Config.tileSize / 2, (float) point.y + Config.tileSize / 2);
		towerToBuild.getPosition().set(MathHelper.PointToVector2(point));
		deployTower(towerToBuild);						
		record[point.x / 40][point.y / 40] = TileType.floor_yellow;
	}
	public void UpgradeTower()
	{
			money -= 100;
	}
}

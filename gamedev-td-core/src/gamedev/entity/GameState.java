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
	//������瘜瘜��ameState隞亙��lass��  ���閬�������瘜�� ���GameState���Instance�������
	private GameState() {
		instance = this;
		createMap();
	}

	private void createMap() {								//������
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
	public void initialize() {								//撠���������
		newRoundInitialization();							//撠��������
		level = 1;											//撠���閮剖�蝚砌����
		currentLevel = Level.generateLevel(level);			//����閮剖� 蝚砌���� 銝虫���洵銝�������� ������
		money = 1000;										//������1000
		playerLife = 10;									//����摰嗥��潛10暺�
		roundTime = PRE_ROUND_WAIT_DURATION;				//�������� 閮剖� 瘥�������� �����蝯�府����
		deployedTowers = new ArrayList<Tower>();			//撠�������� ���� 瘝�遣蝡遙雿������
		
	}
	
	public void newRoundInitialization(){					//��撠��������
		spawnDelay = 0;										//���瘜Ｘ鈭箔葉 瘥���鈭箇��������
		spawnedEnemies = 0;									//撌脩�����芰� 0�
		enemiesToBeSpawned = new ArrayList<Integer>();		//��霈����郭敹������芰蝮賣���
		enemies = new ArrayList<Enemy>();					//��蝝�����歇蝬����芰鞈��
		projectiles = new ArrayList<Projectile>();			//��閮���歇蝬�����鞈��
		
	}
	
	public void update(float delta) {						//�������������
		updateRoundTimer(delta);

		if (roundHasStarted) {								//������������
			checkForEnemySpawn(delta);						//瑼Ｘ��閬��

			for (Enemy enemy : enemies)						//��瘥��撌脩�����芰鞈��
				enemy.update(delta);

			for (Tower tower : deployedTowers)				//��瘥�摨扯�末�������
				tower.update(delta);
			
			for(Projectile projectile : projectiles)		//��瘥�����������
				projectile.update(delta);
			
		}
		
	}

	private void updateRoundTimer(float delta) {			//��������瘜�
		if (roundTime > 0) {								//�閰脣����擗�����
			roundTime -= delta;								//蝜潛���� 銝�脰�遙雿���
		}
		else {												//�閰脣��歇蝬��擗��� ��隞亦���
			roundHasStarted = true;							//�����脰������
			roundTime = ROUND_DURATION;						//��閮剖�oundTime 蝯虫����頛芷�����
			
			prepareLevel(level++);							//皞������
			spawnedEnemies = 0;								//撠歇蝬����芰���飛�
		}
	}

	public void render(SpriteBatch spriteBatch) {			//
		displayMap(spriteBatch);							//��憿舐內����
		displayEnemies(spriteBatch);						//��憿舐內�鈭�
		displayTowers(spriteBatch); 						//��憿舐內憛�
		displayProjectiles(spriteBatch);					//��憿舐內���
	}
	
	private void displayProjectiles(SpriteBatch spriteBatch) {		//��憿舐內���
		spriteBatch.begin();
		for(Projectile projectile : projectiles){					//瘥����隞園run���
			projectile.draw(spriteBatch);							//蝜芾ˊ�瘥����隞�
		}
		spriteBatch.end();
		
	}

	private void displayTowers(SpriteBatch spriteBatch) {			//��憿舐內憛�
		spriteBatch.begin();
		for(Tower tower : deployedTowers) {							//瘥�摨批�run���
			tower.draw(spriteBatch);								//蝜芾ˊ�瘥�摨批��
		}
		
		spriteBatch.end();
	}

	private void displayEnemies(SpriteBatch spriteBatch){			//��憿舐內�芰
		spriteBatch.begin();
		for(Enemy enemy : enemies){									//瘥��鈭粹run���
			enemy.draw(spriteBatch);								//蝜芾ˊ�瘥���芰
		}
		
		spriteBatch.end();
	}

	private void displayMap(SpriteBatch spriteBatch) {				//��蝜芾ˊ����
		spriteBatch.begin();
		for (int i = 0; i < grid.length; i++) {						//蝜芾ˊ������
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

	public boolean checkProjectileCollision() {//��瑼Ｘ��������芰���１���

		return false;
	}


	/*
	 * TODO prepare enemies gets the list of enemies, instances e.g. { {1,2} , {2,1} , {1,2} } 2 spiders, 1 skeleton, 2 spiders in order
	 */

	public void checkForEnemySpawn(float delta) {		//��瑼Ｗ��芰����閬匱蝥���芰
		spawnDelay += delta;

		if (spawnDelay >= .5 && spawnedEnemies < enemiesToBeSpawned.size()) {	//����歇�����芰撠閰脫郭������芰蝮賣

			EnemyType type = Enemy.interpretType(enemiesToBeSpawned.get(spawnedEnemies));	//���銝������芰憿��
			Enemy enemy = Enemy.createEnemy(type);				//���芰憿�������芰
			enemies.add(enemy);									//撠憓��芰��ArrayList銝�
			spawnDelay = 0;										
			++spawnedEnemies;									//撌脩����芰����+1
		}
	}

	/**
	 * This should be called after every round.
	 * 嚙瘠嚙瑾嚙諉回嚙碼嚙踝蕭嚙踝蕭
	 */
	public void prepareLevel(int lvl) {												//��皞��
		newRoundInitialization();													//������脰��������
		currentLevel = Level.generateLevel(lvl);									//���� 閮剖�������
		if((enemiesToBeSpawned = currentLevel.getEnemiesToBeSpawned()) == null){	//�����閬���芰���NULL
			prepareLevel(lvl++);													//皞������
		}else enemiesToBeSpawned = currentLevel.getEnemiesToBeSpawned();			//������閬���芰����
		
	}


	public void deployTower(Tower tower) {						//��撱箇�����
		if (canBuyTower(tower)) {								//��摰嗅隞亥頃鞎瑟����
			money -= tower.getCost();							//���摰園�
			deployedTowers.add(tower);							//��末����� 銝血���arraylist銝�
		}
	}

	public boolean canBuyTower(Tower tower) {					//��蝣箄�摰嗆�鞎瑕�絲�����
		return money >= tower.getCost();
	}

	//嚙稽嚙緩嚙瑾嚙箠嚙碼嚙踝蕭穸X
	public void setWaveSpawnTime(float waveSpawnTime) {			//閮剖��郭�芰������
		if (waveSpawnTime < 0)									//�������
			this.roundTime = 5;									//���身�5蝘�
		else													//�������
			this.roundTime = waveSpawnTime;						//��身摰�����
	}

	//嚙踝蕭嚙踝蕭
	public void getDamaged() {									//��芰������ 撠摰園��摰�
		playerLife--;											//��銝�暺���
	}
	
	public List<Projectile> getProjectiles(){					//�������隞嗅�”
		return projectiles;
	}

	public Level getCurrentLevel() {							//������鞈��
		return currentLevel;
	}

	public int getPlayerLife() {								//���摰嗅擗���
		return playerLife;
	}

	public List<Enemy> getEnemies() {							//���鈭箄���”
		return enemies;
	}
	
	public float getRoundTime() {								//��������
		return roundTime;
	}
	
	public int getMoney() {										//���摰園�鞈��
		return money;
	}
	
	public void addMoney(int bounty){							//憓�摰園�
		this.money += bounty;
	}

	public List<Tower> getDeployedTowers() {					//���歇蝬遣蝡��� ��”
		return deployedTowers;
	}
	
	public List<Point> getWaypoints(){							//�������隞亥粥��楝敺��”
		return Map.getInstance().getWaypoints(mapType);
	}
	
	public void setMap(int type){								//���楊��ype�����
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

	public boolean isTowerPlaceable(Point point) {				//��蝣箄�府�璅���隞亙遣���
		try {
			return point.x > 0 && point.y > 0 &&  ( (grid[point.x / 40][point.y / 40] == TileType.floor_yellow) || (grid[point.x / 40][point.y / 40] == TileType.glass_special))&& (record[point.x / 40][point.y / 40] != TileType.floor_yellow) ;	//摨扳���� 銝� 閰脣漣璅蒂���芰銵粥頝臬��		
		}catch (Exception e){
			
		}
		return false;
	}

	public void buildTower(Tower towerToBuild, Point point) {	//蝯血�炬撱箸������  隞亙���遣瑽漣璅�
		 

		Vector2 position = MathHelper.PointToVector2(point);	//�����遣瑽�蔭
		towerToBuild.setPosition(position);						//閮剖���遣瑽�蔭

		towerToBuild.setCenter((float) point.x + Config.tileSize / 2, (float) point.y + Config.tileSize / 2);
		towerToBuild.getPosition().set(MathHelper.PointToVector2(point));
		deployTower(towerToBuild);								//撱箸�府憛�
		record[point.x / 40][point.y / 40] = TileType.floor_yellow;
	}
}

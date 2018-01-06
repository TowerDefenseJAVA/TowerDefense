package gamedev.input;

import gamedev.entity.GameState;
import gamedev.entity.Tower;
import gamedev.entity.TowerFactory;
import gamedev.entity.TowerFactory.TowerType;
import gamedev.screen.GameUserInterface;
import gamedev.td.Config;
import gamedev.td.GDSprite;
import gamedev.td.TowerDefense;
import gamedev.td.helper.MathHelper;

import java.awt.Point;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;

public class GameInputProcessor extends GDInputProcessor {

	private Tower towerToBuild, selectedTower;

	private GameUserInterface userInterface;

	public GameInputProcessor(TowerDefense towerDefense) {
		super(towerDefense);
		userInterface = new GameUserInterface();
		towerToBuild = null;
		selectedTower = null;
	}

	@Override
	public boolean keyDown(int keycode) {
		
		if(keycode == 27){
			System.out.println("d");
			towerDefense.switchScreen(towerDefense.getPauseScreen());
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if(character == 'p'){
			towerDefense.switchScreen(towerDefense.getPauseScreen());
		}
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {

		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			// Step 1
			selectTowerToBuild(x, y, pointer, button);

			// Step 2
			buildSelectedTower(x, y, pointer, button);

			// Step 1
	
		} else if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			selectTowerToUpgrade(x, y, pointer, button);
			resetInteractions();

		}

		return false;
	}

	private void resetInteractions() {

	}

	private void selectTowerToUpgrade(int x, int y, int pointer, int button) {
		GameState state = GameState.getInstance();

		List<Tower> deployedTowers = state.getDeployedTowers();

		for (int i = 0; i < deployedTowers.size(); i++) {
			Tower tower = deployedTowers.get(i);
			GDSprite sprite = tower.getSprite();

			if (sprite.contains(x, y)) {
				selectedTower = tower;
				userInterface.setTowerToUpgrade(tower);
				System.out.println("[Input] User selected a tower, " + selectedTower.getTowerName() + " found at " + selectedTower.getPosition());
				
			}
		}
	}

	private void selectTowerToBuild(int x, int y, int pointer, int button) {
		List<GDSprite> availableTowers = userInterface.getBuildTowerButtons();

		for (int type = 0; type < availableTowers.size(); type++) {
			GDSprite sprite = availableTowers.get(type);
			if (sprite.contains(x, y)) {
				TowerType towerType = TowerFactory.interpretType(type);
				System.out.println("[Input] User is trying to build a " + towerType);
				towerToBuild = TowerFactory.createTower(towerType);
				userInterface.setTowerToBuild(towerToBuild, towerType);
				userInterface.setGhostTower(towerType);
				userInterface.setTowerRange(towerToBuild);
			}
		}
	}

	private void buildSelectedTower(int x, int y, int pointer, int button) {
		if (towerToBuild == null)
			return; // Do nothing if there is no tower to build yet.

		GameState state = GameState.getInstance();
		Point point = getGridCoordinate(x, y);
		if(point != null) {
			towerToBuild.getPosition().set(MathHelper.PointToVector2(point));
		}
		
		if (point != null && state.isTowerPlaceable(point)) {
			if (state.canBuyTower(towerToBuild)){
				System.out.println("[Input] User built a " + towerToBuild.getTowerName() + " on " + point);
				state.buildTower(towerToBuild.clone(), point);			
			}else{
				System.out.println("[Input] User cannot build a " + towerToBuild.getTowerName() + " because he/she does not have money.");
				towerToBuild = null;
				userInterface.setTowerRange(null);
				userInterface.setGhostTower(null);
			}
			userInterface.reset();
		}
	}
	
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {

		Point point = getGridCoordinate(x, y);
		userInterface.setHighlightedCell(point);
		if (towerToBuild != null){
			userInterface.setGhostTowerLocation(point);
			towerToBuild.getPosition().set(MathHelper.PointToVector2(point));
		}
		
		


		List<GDSprite> towerSprites = userInterface.getBuildTowerButtons();
		userInterface.getTowerBtnHighlight().setPosition(-50, -50);
		for (int i = 0; i < towerSprites.size(); i++) {
			GDSprite sprite = towerSprites.get(i);
			if (sprite.contains(x, y)) {
				userInterface.getTowerBtnHighlight().setPosition(sprite.getX(), sprite.getY());
				
//				boolean showTooltip = true;
//				Point spritePoint = sprite.getPosition();

				TowerType towerType = TowerFactory.interpretType(i);
				//System.out.println(towerType);//test only
				towerToBuild = TowerFactory.createTower(towerType);
				
				userInterface.setTowerToBuild(towerToBuild, towerType);
			} else {
				userInterface.reset();
			}
		}
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	private Point getGridCoordinate(int screenX, int screenY) {
		if (screenX < 0 || screenY < 0)
			return null;

		
		int truncateX = screenX / Config.tileSize;
		int truncateY = screenY / Config.tileSize;
		return new Point(truncateX * Config.tileSize, truncateY * Config.tileSize);
	}

	public GameUserInterface getUserInterface() {
		return userInterface;
	}
}

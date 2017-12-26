package gamedev.entity.tower;

import gamedev.entity.GameState;
import gamedev.entity.Tower;
import gamedev.td.GDSprite;


public class FireArrowTower extends Tower {
	
	private int maxLevel = 5;
	private static int damageLevels[] = {5,10,15,20,25,1000000};
	private static int rangeLevels[] = {100,100,100,100,100,150};
	private static float attackRateLevels[] = {0.8f,10f,10f,10f,10f,10f};

	public FireArrowTower(GDSprite sprite, int level, int cost) {
		super(sprite, damageLevels[level], rangeLevels[level], attackRateLevels[level], cost, level, "Fire Arrow Tower");
	}

	@Override
	public void upgrade() {
		
		if(level+1 <= maxLevel) {
			level++;
			if (GameState.getInstance().canUpgradeTower(this))
			{
				damage = damageLevels[level];
				attackRange = rangeLevels[level];
				attackRate = attackRateLevels[level];
				setAttackCooldown(attackRate);
				GameState.getInstance().UpgradeTower();
			}
			else System.out.println("Money is not enough");
		}
		else System.out.println("Level is Max");
	}

}
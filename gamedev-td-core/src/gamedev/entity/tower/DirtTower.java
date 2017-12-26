package gamedev.entity.tower;

import gamedev.entity.GameState;
import gamedev.entity.Tower;
import gamedev.td.GDSprite;

public class DirtTower extends Tower {
	
	private int maxLevel = 5;
	private static int damageLevels[] = {1,2,3,4,5,6};
	private static int rangeLevels[] = {80,90,100,110,120,130};
	private static float attackRateLevels[] = {5f,6f,7f,8f,9f,10f};
	
	public DirtTower(GDSprite sprite, int level, int cost) {
		super(sprite, damageLevels[level], rangeLevels[level], attackRateLevels[level], cost, level, "Dirt Tower");
		
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

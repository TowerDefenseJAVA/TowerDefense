package gamedev.entity.tower;

import gamedev.entity.GameState;
import gamedev.entity.Tower;
import gamedev.td.GDSprite;

public class EggTower extends Tower {

	private int maxLevel = 5;
	private static int damageLevels[] = {7,9,11,13,15,17};
	private static int rangeLevels[] = {100,110,120,130,140,150};
	private static float attackRateLevels[] = {1.0f,1.2f,1.4f,1.6f,1.8f,2.0f};
	
	public EggTower(GDSprite sprite, int level, int cost) {
		super(sprite, damageLevels[level], rangeLevels[level], attackRateLevels[level], cost, level, "Egg Tower");
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

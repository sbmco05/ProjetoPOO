package movables;

import java.util.Iterator;
import java.util.List;

import Catchable.Catchable;
import Elements.Door;
import pt.iscte.poo.utils.Point2D;
import starter.GameEngine;

public abstract class Movable extends GameElement implements IMovable {

	protected int HP;

	public Movable(int layer, String name, Point2D position, int hitpoints) {
		super(layer, name, position);
		this.HP = hitpoints;
	}

	public boolean canMove(Point2D p) {

		List<GameElement> elementsList = GameEngine.getInstance().getElementsList();
		for (GameElement element : elementsList) {
			if (element.getPosition().equals(p) && element.getLayer() >= layer) {
				if (element instanceof Hero) {
					attack(element);
					checkDeath(element);
				}
				return false;
			}
		}

		if (p.getX() < 0)
			return false;
		if (p.getY() < 0)
			return false;
		if (p.getX() >= GameEngine.GRID_WIDTH)
			return false;
		if (p.getY() >= GameEngine.GRID_HEIGHT)
			return false;

		return true;
	}

	public void checkDeath(GameElement victim) {
		if (victim instanceof Movable && ((Movable) victim).getHP() <= 0) {
			if (victim instanceof Hero) {
				GameEngine.getInstance().setDeadHero(true);
			} else if (victim instanceof Thief) {
				((Thief) victim).drop();
				GameEngine.getInstance().rmvElement(victim);
			} else {
				GameEngine.getInstance().rmvElement(victim);
			}
		}
	}

	@Override
	public void decayHP(int howMuch) {
		setHP(getHP() - howMuch);
		if (HP > 10)
			setHP(10);
	}

	public void damage(int dmg, GameElement victim) {
		int hitpoints = ((Movable) victim).getHP();
		if (!(victim instanceof Movable) || (!getName().equals("Hero") && !(victim.getName().equals("Hero")))
				&& ((Movable) victim).getHP() > 0) {
			return;

//	If the hero is attacking and they have a sword, they deal double the damage.
		} else if (!(victim instanceof Hero) && GameEngine.getInstance().getHero().hasSword) {
			((Movable) victim).setHP(hitpoints - (2 * dmg));
//	If the hero is attacked and they have armor, they only take damage 50% of the time.
		} else if (victim instanceof Hero && GameEngine.getInstance().getHero().hasArmor) {
			int random = (int) (Math.random() * 100);
			if (random < 50) {
				((Movable) victim).setHP(hitpoints - dmg);
			}
		} else {
			((Movable) victim).setHP(hitpoints - dmg);
		}
		if (((Movable) victim).getHP() <= 0) {
			GameEngine.getInstance().rmvElement(victim);
		}

	}

	public int getHP() {
		return HP;
	}

	public void setHP(int hP) {
		HP = hP;
	}

}

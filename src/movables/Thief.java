package movables;

import Catchable.Catchable;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;
import starter.GameEngine;

public class Thief extends Movable {

	public Thief(Point2D position) {
		super(2, "Thief", position, 5);
	}

	GameElement stolenItem = null;

	public void move(Direction d) {

		if (stolenItem == null) {
			Vector2D vct = Vector2D.movementVector(getPosition(), GameEngine.getInstance().getHeroPosition());
			Point2D newPosition = super.getPosition().plus(vct);
			if (canMove(newPosition)) {
				setPosition(newPosition);
			}
		} else {
			Vector2D vct = Vector2D.movementVector(getPosition(), GameEngine.getInstance().getHeroPosition());
			Vector2D invertedvct = new Vector2D(-(vct.getX()), -(vct.getY()));
			Point2D newPosition = super.getPosition().plus(invertedvct);
			if (canMove(newPosition)) {
				setPosition(newPosition);
			}
		}
	}

	@Override
	public int getHP() {
		return this.HP;
	}

	public GameElement getStolenItem() {
		return stolenItem;
	}

	@Override
	public void setHP(int hitpoints) {
		HP = hitpoints;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return layer;
	}

	@Override
	public void attack(GameElement victim) {
		if (victim instanceof Hero) {
			int a = (int) (Math.random() * GameEngine.getInstance().getHero().getCatchedItems().size());
			if (GameEngine.getInstance().getHero().getCatchedItems().size() > 0) {
				this.stolenItem = GameEngine.getInstance().getHero().getCatchedItems().get(a);
				GameEngine.getInstance().getHero().getCatchedItems().get(a).setPosition(new Point2D(11, 11));
				GameEngine.getInstance().getHero().getCatchedItems().remove(a);
				GameEngine.getInstance().updateCatchedItems();
			}
		}

	}

	public void drop() {
		if (stolenItem != null) {
			Point2D dropPosition = position;
			stolenItem.setPosition(dropPosition);
		} else {
			return;
		}
	}

	@Override
	public void damage(int HP) {
		decayHP(HP);
	}

}

package movables;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;
import starter.GameEngine;

public class Skeleton extends Movable {

	public Skeleton(Point2D position) {
		super(2, "Skeleton", position, 5);
	}

	public void move(Direction d) {

		int turns = GameEngine.getInstance().getTurns();
		if (turns % 2 == 0) {
			Vector2D vct = Vector2D.movementVector(getPosition(), GameEngine.getInstance().getHeroPosition());
			Point2D newPosition = super.getPosition().plus(vct);
			if (canMove(newPosition)) {
				setPosition(newPosition);
			}
		}
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
			((Movable) victim).damage(1);

		}

	}

	@Override
	public void damage(int HP) {
		decayHP(HP);
	}

}

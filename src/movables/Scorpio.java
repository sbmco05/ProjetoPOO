package movables;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;
import starter.GameEngine;

public class Scorpio extends Movable {

	public Scorpio(Point2D position) {
		super(2,  "Scorpio", position, 2);
	}

	public void move(Direction d) {
			Vector2D vct = Vector2D.movementVector(getPosition(), GameEngine.getInstance().getHeroPosition());
			Point2D newPosition = super.getPosition().plus(vct);
			if (canMove(newPosition)) {
				setPosition(newPosition);
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
		if(victim instanceof Hero) {
			GameEngine.getInstance().getHero().setWasPoisoned(true);
			((Movable) victim).damage(1);
		}
		
	}

	@Override
	public void damage(int HP) {
		System.out.println("Tenho de vida " + HP);
		decayHP(HP);
		
	}

}

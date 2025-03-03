package movables;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;
import starter.GameEngine;

public class Thug extends Movable {
	
	public Thug(Point2D position) {	
		super(2, "Thug", position, 10);	//camada 3, para se sobrepor aos outros elementos
	}
	
	@Override
	public String getName() {
		return name;
	}
	
    public void move(Direction d) {
    	Vector2D vct = Vector2D.movementVector(getPosition(), GameEngine.getInstance().getHeroPosition());
		Point2D newPosition = super.getPosition().plus(vct);
		if (canMove(newPosition)) {
			setPosition(newPosition);
		}
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
	public int getHP() {
		// TODO Auto-generated method stub
		return HP;
	}

	@Override
	public void setHP(int hitpoints) {
		// TODO Auto-generated method stub
		HP = hitpoints;
	}

	@Override
	public void attack(GameElement victim) {
		if(victim instanceof Hero) {
			if ( Math.random() * 10 <= 3 ) {
				((Movable) victim).damage(3);
			}
		}
		
	}

	@Override
	public void damage(int HP) {
		decayHP(HP);
		
	}


}

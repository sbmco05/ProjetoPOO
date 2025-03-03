package movables;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;
import starter.GameEngine;

public class Bat extends Movable {

	public Bat(Point2D position) {
		super(2, "Bat", position, 3); // camada 2, para se sobrepor aos outros elementos
	}
	
	

	@Override
	public String getName() {
		return name;
	}

	public void move(Direction d) {
		int random = (int) (Math.random() * 2);
		if (random == 1) {
			Vector2D vct = Vector2D.movementVector(getPosition(), GameEngine.getInstance().getHeroPosition());
			Point2D newPosition = super.getPosition().plus(vct);
			if (canMove(newPosition)) {
				setPosition(newPosition);
			}
		} else {
			Direction randDirection = Direction.random();
			Vector2D randVector = randDirection.asVector();
			Point2D newPosition = super.getPosition().plus(randVector);
			if (canMove(newPosition)) {
				setPosition(newPosition);
			}
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

	public int getHP(){
		return HP;
	}
	
	@Override
	public void setHP(int hitpoints){
		HP = hitpoints;
	}

	@Override
	public void attack(GameElement victim) {
		if(victim instanceof Hero) {
			int heroHP = GameEngine.getInstance().getHero().getHP();
			if ((int) (Math.random() * 2) == 1) {
				((Movable) victim).damage(1);
				int heroHPafter = GameEngine.getInstance().getHero().getHP();
				if( heroHP > heroHPafter && getHP() < 3 ) {
					decayHP(-1);
				}
			}
		}
		
	}

	@Override
	public void damage(int HP) {
		decayHP(HP);
	}
	
}

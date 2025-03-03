package Catchable;

import pt.iscte.poo.utils.Point2D;
import starter.GameEngine;

public class Sword extends Catchable {

	public Sword(Point2D position) {
		super(1, "Sword", position);
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
	public void action() {
		// TODO Auto-generated method stub
		
		GameEngine.getInstance().getHero().hasSword = true;
		
	}

	@Override
	public void reverseAction() {
		// TODO Auto-generated method stub
		GameEngine.getInstance().getHero().hasSword = false;
	}

}


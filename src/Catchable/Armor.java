package Catchable;

import movables.GameElement;
import pt.iscte.poo.utils.Point2D;
import starter.GameEngine;

public class Armor extends Catchable {
	
	public Armor(Point2D position) {
		super(1, "Armor", position);
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

		GameEngine.getInstance().getHero().hasArmor = true;

	}

	@Override
	public void reverseAction() {
		
		GameEngine.getInstance().getHero().hasArmor = false;
		
	}
}

package Catchable;

import movables.GameElement;
import pt.iscte.poo.utils.Point2D;
import starter.GameEngine;

public class HealingPotion extends Catchable {
	
	private boolean used = false;

	public HealingPotion(Point2D position) {
		super(1, "HealingPotion", position);
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
	
	public boolean isUsed() {
		return used;
	}

	@Override
	public void action() {
		return;
	}

	@Override
	public void reverseAction() {
		return;
	}
	
	public void consume() {
		GameEngine.getInstance().getHero().decayHP(-5);
		GameEngine.getInstance().getHero().setWasPoisoned(false);
		this.used = true;
		GameEngine.getInstance().getHero().setScore(GameEngine.getInstance().getHero().getScore()+5);
	}
}

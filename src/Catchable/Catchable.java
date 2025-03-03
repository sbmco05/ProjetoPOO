package Catchable;

import movables.GameElement;
import movables.IMovable;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import starter.GameEngine;

public abstract class Catchable extends GameElement implements ICatchable {

	public Catchable(int layer, String name, Point2D position) {
		super(layer, name, position);
		// TODO Auto-generated constructor stub
	}

	public void Drop(int ListPosition, Point2D dropPosition) {
		setPosition(dropPosition);
		GameEngine.getInstance().getElementsList()
				.add(GameEngine.getInstance().getHero().getCatchedItems().get(ListPosition));
		GameEngine.getInstance().getHero().getCatchedItems().remove(ListPosition);
		reverseAction();

	}
}

package Catchable;

import Elements.Door;
import pt.iscte.poo.utils.Point2D;

public class Key extends Catchable {

	private String keyID;

	public Key(Point2D position, String keyID) {
		super(1, "Key", position);
		this.keyID = keyID;
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

	public String getKeyID() {
		return keyID;
	}
	
	@Override
	public void action() {
		//nothing
	}

	@Override
	public void reverseAction() {
		//nothing
	}

	public boolean CanOpen(Door door) {
		if (door.getKeyID().equals(keyID))
			return true;
		else
			return false;
	}
}

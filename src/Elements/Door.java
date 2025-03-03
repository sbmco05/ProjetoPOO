package Elements;

import movables.GameElement;
import pt.iscte.poo.utils.Point2D;
import starter.GameEngine;

public class Door extends GameElement {

	private String salaDestino = null;
	private String keyID = null;
	private Point2D finalPosition;
	private boolean isOpen;

	public Door(Point2D position, String salaDestino, Point2D finalPosition, String keyID) {
		super(3, "DoorClosed", position);
		this.salaDestino = salaDestino;
		this.finalPosition = finalPosition;
		this.keyID = keyID;
		isOpen = false;
	}

	public Door(Point2D position, String salaDestino, Point2D finalPosition) {
		super(1, "DoorOpen", position);
		this.salaDestino = salaDestino;
		this.finalPosition = finalPosition;
		isOpen = true;
	}

	public Door reverseDoor() {
		Door door = new Door(finalPosition, GameEngine.getInstance().getLevel(), position, keyID);
		door.setOpen(true);
		return door;
	}

	@Override
	public String getName() {
		if (isOpen)
			return "DoorOpen";
		else
			return "DoorClosed";
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
		if (isOpen)
			setLayer(1);
	}

	public String getSalaDestino() {
		return salaDestino;
	}

	public String getKeyID() {
		return keyID;
	}

	public Point2D getFinalPosition() {
		return finalPosition;
	}

}

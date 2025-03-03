package movables;

import pt.iscte.poo.gui.*;
import pt.iscte.poo.utils.*;
import pt.iscte.poo.utils.Point2D;

public class GameElement implements ImageTile {

	protected int layer;
	protected String name;
	protected Point2D position;

	public GameElement(int layer, String name, Point2D position) {
		this.layer = layer;
		this.name = name;
		this.position = position;
	}

	public int getLayer() {
		return layer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}

	public Point2D getPosition() {
		return position;
	}
	
}

package Elements;

import movables.GameElement;
import pt.iscte.poo.utils.Point2D;

public class Treasure extends GameElement {


	public Treasure(Point2D position) {
		super(1, "Treasure", position);
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
	
}

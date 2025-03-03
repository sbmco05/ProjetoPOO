package Elements;

import pt.iscte.poo.utils.Point2D;
import movables.GameElement;
import pt.iscte.poo.gui.*;

public class Wall extends GameElement {

	public Wall(Point2D position) {
		super(3, "Wall", position);
		this.position = position;
	}

	@Override
	public String getName() {
		return "Wall";
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return 3;
	}
}

package Elements;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Color implements ImageTile {
	
	private Point2D position;
	public int layer = 0;
	public String name;
	

	public Color(Point2D position, String Color) {
		this.position = position;
		if( !Color.equals("Red") && !Color.equals("Green") && !Color.equals("RedGreen") && !Color.equals("Black")){
			throw new IllegalArgumentException(Color + " is not aplicable");
		}else{
			this.name = Color;
		}
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public Point2D getPosition() {
		// TODO Auto-generated method stub
		return this.position;
	}

	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return this.layer;
	}

}


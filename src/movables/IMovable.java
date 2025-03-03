package movables;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
public interface IMovable {

	void move(Direction d);
	boolean canMove(Point2D p);
	
	public void decayHP(int howMuch);

	public void attack(GameElement victim); // no atacante

	public void damage(int HP); //na vítima
}

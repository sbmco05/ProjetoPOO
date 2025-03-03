package movables;

import java.util.ArrayList;
import java.util.List;

import Catchable.Armor;
import Catchable.Catchable;
import Catchable.Key;
import Catchable.Sword;
import Elements.Door;
import Elements.Treasure;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import starter.GameEngine;

public class Hero extends Movable {

	private List<Catchable> catchedItems = new ArrayList<>();
	private boolean ShouldIPass = false;
	public boolean hasArmor = false;
	public boolean hasSword = false;
	private boolean wasPoisoned = false;
	private int score = 0;
	private static final int HP_MAX = 10;

	public Hero(Point2D position) {
		super(2, "Hero", position, 10); // camada 2, para se sobrepor aos outros elementos
	}

	public boolean hasArmor() {
		return hasArmor;
	}

	public int getScore() {
		return score;
	}

	public boolean wasPoisoned() {
		return this.wasPoisoned;
	}

	public void poisonEffect() {
		decayHP(1);
	}

	public void setWasPoisoned(boolean wasPoisoned) {
		this.wasPoisoned = wasPoisoned;
	}

	public void setHasArmor(boolean hasArmor) {
		this.hasArmor = hasArmor;
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

	public List<Catchable> getCatchedItems() {
		return catchedItems;
	}

	public void setShouldIPass(boolean shouldIPass) {
		this.ShouldIPass = shouldIPass;
	}

	public boolean getShouldIPass() {
		return this.ShouldIPass;
	}

	public void setScore(int score) {
		this.score = score;
		if (this.score < 0)
			this.score = 0;
	}

	@Override
	public boolean canMove(Point2D p) {

		List<GameElement> elementsList = GameEngine.getInstance().getElementsList();
		for (GameElement element : elementsList) {
			if (element.getPosition().equals(p)) {
				if (element.getLayer() < layer) {
					if (element instanceof Catchable)
						GameEngine.getInstance().getHero().grabItems(p);
					else if (element instanceof Door && ((Door) element).isOpen())
						GameEngine.getInstance().getHero().passDoor(element);
					else if (element instanceof Treasure) {
						GameEngine.getInstance().endGame("win");
					}
				} else {
					if (element instanceof Movable) {
						attack(element);
						checkDeath(element);
					} else if (element instanceof Door && getName() == "Hero") {
						GameEngine.getInstance().getHero().doorOpenner(element);
					}

					return false;
				}
			}
		}

		if (p.getX() < 0)
			return false;
		if (p.getY() < 0)
			return false;
		if (p.getX() >= GameEngine.GRID_WIDTH)
			return false;
		if (p.getY() >= GameEngine.GRID_HEIGHT)
			return false;

		return true;
	}

	public void move(Direction d) {

		Point2D newPosition = super.getPosition().plus(d.asVector());
		if (canMove(newPosition)) {
			setPosition(newPosition);

		}
		if (this.wasPoisoned) {
			setScore(score--);
			poisonEffect();
			checkDeath(GameEngine.getInstance().getHero());
		}

	}

	// Ao morrer, o heroi larga todos os objetos no seu inventário

	public void catchablesDrop() {
		List<Point2D> dropPositions = position.getWideNeighbourhoodPoints();
		for (int i = catchedItems.size(); i > 0; i--) {
			GameElement item = catchedItems.get(i - 1);
			((Catchable) item).Drop(i - 1, clearPosition(dropPositions));
		}
	}

	public Point2D clearPosition(List<Point2D> positions) {

		for (Point2D position : positions) {
			if (GameEngine.getInstance().clearCatchPosition(position)) {
				return position;
			}
		}

		Point2D p = new Point2D((int) (Math.random() * 8), (int) (Math.random() * 8));

		if (GameEngine.getInstance().clearCatchPosition(p)) {
			return p;
		}

		return p;
	}

	public void grabItems(Point2D p) {
		for (GameElement element : GameEngine.getInstance().getElementsList()) {
			if (element.getPosition().equals(p) && element instanceof Catchable && catchedItems.size() < 3) {
				catchedItems.add((Catchable) element);
				GameEngine.getInstance().rmvElement(element);
			}
		}
	}

	@Override
	public void attack(GameElement victim) {

		if (this.hasSword) {
			((Movable) victim).damage(2 * 1);
			score++;
		} else
			((Movable) victim).damage(1);
		score++;
	}

	@Override
	public void damage(int HP) {
		if (this.hasArmor()) {
			if (Math.random() * 2 < 1)
				decayHP(HP);
			return;
		} else {
			decayHP(HP);
		}
		setScore(score - HP);
	}

	public void doorOpenner(GameElement door) {
		for (GameElement key : getCatchedItems()) {
			if (key instanceof Key && ((Key) key).CanOpen((Door) door)) {
				((Door) door).setOpen(true);
			}
		}
	}

	public void passDoor(GameElement Porta) {
		GameEngine.getInstance().save();
		GameEngine.getInstance().elementsCleaner();
		GameEngine.getInstance().setPorta(((Door) Porta));
		setShouldIPass(true);
	}
}

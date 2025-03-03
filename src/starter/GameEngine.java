package starter;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import Catchable.Armor;
import Catchable.Catchable;
import Catchable.HealingPotion;
import Catchable.Key;
import Catchable.Sword;
import Elements.Color;
import Elements.Door;
import Elements.Floor;
import Elements.Sala;
import Elements.Score;
import Elements.Treasure;
import Elements.Wall;
import movables.Bat;
import movables.GameElement;
import movables.Hero;
import movables.Movable;
import movables.Scorpio;
import movables.Skeleton;
import movables.Thief;
import movables.Thug;
import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class GameEngine implements Observer {

	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;

	private static GameEngine INSTANCE = null;
	private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();

	private Hero hero;
	private String nomeJog;
	private int turns;
	private String level = "room0";
	private Door door;
	private boolean end = false;
	private boolean deadHero = false;
	private boolean newLevel = true;

	private List<Score> scores = new ArrayList<>();
	private List<GameElement> elements = new ArrayList<>();
	private List<GameElement> itemsToRemove = new ArrayList<>();
	private List<Sala> rooms = new ArrayList<>();

/////////////////////////////// Getters/Setters ///////////////////////////////

	public Hero getHero() {
		return hero;
	}

	public Point2D getHeroPosition() {
		return hero.getPosition();
	}

	public void setPorta(Door door) {
		this.door = door;
	}

	public int getTurns() {
		return turns;
	}

	public String getLevel() {
		return level;
	}

	public List<GameElement> getElementsList() {
		return elements;
	}

	public static GameEngine getInstance() {
		if (INSTANCE == null)
			INSTANCE = new GameEngine();
		return INSTANCE;
	}

	public List<Sala> getRooms() {
		return rooms;
	}

	public void setDeadHero(boolean deadHero) {
		this.deadHero = deadHero;
	}

/////////////////////////////// Start Game ///////////////////////////////

	private GameEngine() {
		gui.registerObserver(this);
		gui.setSize(GRID_WIDTH, GRID_HEIGHT + 1);
		gui.go();
	}

//	The game starts by asking the user their name and place the game elements in the Window

	public void start() {
		if (turns == 0)
			this.nomeJog = gui.askUser("Insira o seu nome: ");
		addObjects();
		updateHealthBar();
		gui.setStatusMessage("ROGUE Starter Package - Turns:" + turns + "   Score: " + getHero().getScore() + " pts");
		gui.update();
	}

//	All actions are triggered when the hero moves, i.e. when pressing any supported key except
//		"1", "2", "3" and "q", "w", "e". These keys are responsible for dropping or consuming "catchable" objects.
//	After each hero action, the health and inventory bars are updated. If a "catchable" object is consumed, 
//		the corresponding effect is applied.
//	Finally, the "killer" function is called to remove all relevant objects, such as a defeated "movable" 
//		or any collected "catchable".
	
	@Override
	public void update(Observed source) {
		if (!end) {

			if (ImageMatrixGUI.getInstance().wasWindowClosed()) {
				endGame("close");
				System.out.println("Ending");
			}

			int key = ((ImageMatrixGUI) source).keyPressed();
			keyEvent(key);

			if (hero.getCatchedItems().size() > 0)
				for (GameElement element : hero.getCatchedItems()) {
					if (element instanceof Catchable) {
						((Catchable) element).action();
					}
				}
			killer();

			if (((Hero) hero).getShouldIPass()) {
				start();
				((Hero) hero).setShouldIPass(false);
			}

			if (deadHero) {
				endGame("death");
				deadHero = false;
				((Hero) hero).setWasPoisoned(false);
			}

			turns++;

			updateHealthBar();
			updateCatchedItems();
			gui.setStatusMessage(
					"ROGUE Starter Package - Turns:" + turns + "   Score: " + getHero().getScore() + " pts");
			gui.update();

		}
	}

	public void keyEvent(int key) {
		if (key == KeyEvent.VK_1) {
			dropCatchable(1);
		} else if (key == KeyEvent.VK_2) {
			dropCatchable(2);
		} else if (key == KeyEvent.VK_3) {
			dropCatchable(3);
		}
		if (key == KeyEvent.VK_Q) {
			consume(1);
		} else if (key == KeyEvent.VK_W) {
			consume(2);
		} else if (key == KeyEvent.VK_E) {
			consume(3);
		} else {
			Direction m = Direction.directionFor(key);
			if (m != null)
				for (GameElement element : elements) {
					if (element instanceof Movable) {
						((Movable) element).move(m);
					}
				}
		}
	}

/////////////////////////////// Level Start ///////////////////////////////

//	The file read operation depends on the level the player is in, as the filename changes accordingly. 
//	An auxiliary variable, "count", is used to track the current line being read. Since the first n lines 
//		of the file correspond to the walls present in the level, the variable ensures these n lines are read 
//		first (n being the window height). After this point, each line is processed by separating its contents 
//		and creating a vector containing the name of the object to be added along with its corresponding position.

	private void readFile() throws IOException {
		String fileName = (level + ".txt");
		Scanner scan = new Scanner(new File(fileName));
		int count = 0;

		while (scan.hasNextLine()) {

			String line = scan.nextLine();
			if (count < GRID_HEIGHT) {

				String[] split = line.split(""); // A funcao divide a linha de "nada" em "nada" dando um vetor com os
													// caracteres
				for (int i = 0; i < split.length; i++) {

					if (split[i].equals("#")) {
						Wall wall = new Wall(new Point2D(i, count));
						elements.add(wall);
						gui.addImage(wall);
					}
				}

			}
			if (count > GRID_HEIGHT) {
				String[] split = line.split(",");
				addElement(split[0], split);
			}
			count++;
		}

	}

	private void addFloor() {
		List<ImageTile> tileList = new ArrayList<>();
		for (int x = 0; x != GRID_WIDTH; x++)
			for (int y = 0; y != GRID_HEIGHT + 1; y++) {
				if (y < (GRID_HEIGHT)) {
					Floor floor = new Floor(new Point2D(x, y));
					tileList.add(floor);
				} else if (y == (GRID_HEIGHT)) {
					Color bb = new Color(new Point2D(x, y), "Black");
					tileList.add(bb);
				}
			}
		gui.addImages(tileList);
	}

//	Takes as arguments the name of the object to be created and its corresponding position in the game window.
//	All objects are then added to the elements list. This way, the elements list stores all "movables"and "catchables".
	
	private void addElement(String ElementName, String[] lineSplit) {
		int x = Integer.parseInt(lineSplit[1]); // String -> int
		int y = Integer.parseInt(lineSplit[2]);
		Point2D point = new Point2D(x, y);
		switch (ElementName) {
		case "Skeleton":
			Skeleton skeleton = new Skeleton(point);
			elements.add(skeleton);
			break;
		case "Scorpio":
			Scorpio scorpio = new Scorpio(point);
			elements.add(scorpio);
			break;
		case "Thief":
			Thief thief = new Thief(point);
			elements.add(thief);
			break;
		case "Bat":
			Bat bat = new Bat(point);
			elements.add(bat);
			break;
		case "Thug":
			Thug thug = new Thug(point);
			elements.add(thug);
			break;
		case "Sword":
			Sword sword = new Sword(point);
			elements.add(sword);
			break;
		case "Armor":
			Armor armor = new Armor(point);
			elements.add(armor);
			break;
		case "HealingPotion":
			HealingPotion HealingPotion = new HealingPotion(point);
			elements.add(HealingPotion);
			break;
		case "Key":
			Key key = new Key(point, lineSplit[3]);
			elements.add(key);
			break;
		case "Door":
			Point2D finalPosition = new Point2D(Integer.parseInt(lineSplit[4]), Integer.parseInt(lineSplit[5]));
			if (lineSplit.length == 6) {
				Door door = new Door(point, lineSplit[3], finalPosition);
				elements.add(door);
			} else if (lineSplit.length == 7) {
				Door door = new Door(point, lineSplit[3], finalPosition, lineSplit[6]);
				elements.add(door);
			}
			break;
		case "Treasure":
			Treasure treasure = new Treasure(point);
			elements.add(treasure);
			break;
		}
	}

//	All elements that make up the game are visually added, including the ground and all objects in the elements list.

	private void addObjects() {
		addFloor();
		if (this.door != null) {
			level = this.door.getSalaDestino();
		}
		if (thisRoomExists()) {
			for (Sala room : rooms) {
				if (room.getLevel().equals(level)) {

					for (GameElement element : room.getElementsList()) {
						elements.add(element);
					}
				}
			}
			if (door != null)
				hero.setPosition(this.door.getFinalPosition());
		} else {
			if (rooms.size() > 0 || hero != null) {
				if (door != null)
					hero.setPosition(this.door.getFinalPosition());
				else
					hero.setPosition(new Point2D(1, 1));
			} else {
				hero = new Hero(new Point2D(1, 1));
				elements.add(hero);
			}
			try {
				readFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (this.door != null)
			setDoor();

		for (GameElement element : elements) {
			if ((element instanceof Movable && ((Movable) element).getHP() > 0) || !(element instanceof Movable)) {
				gui.addImage((ImageTile) element);
			} else
				rmvElement(element);
		}
	}

	public boolean thisRoomExists() {
		for (Sala thisRoom : rooms) {
			if (level.equals(thisRoom.getLevel()))
				return true;
		}
		return false;
	}
	
//	In the first version of the rooms, there was a door whose final position differed from any existing door.
//	This method ensures that if the door does not already exist, the wall at that position is removed, and the 
//		new door is added in an open state. If the door already exists, it is simply opened.

	public void setDoor() {
		GameElement ge = null;
		if (door != null) {
			this.door = this.door.reverseDoor();
			for (GameElement wall : elements) {
				if (wall instanceof Wall && wall.getPosition().equals(this.door.getPosition())) {
					ge = wall;
				}
				if (wall instanceof Door && wall.getPosition().equals(this.door.getPosition())) {
					((Door) wall).setOpen(true);
				}
			}
			if (ge != null) {
				elements.remove(ge);
				elements.add(this.door);
			}
		}
	}

/////////////////////////////// Update Bars ///////////////////////////////

	public void updateHealthBar() {
		int health = hero.getHP();
		int HP = 10 - health;
		if (HP < 0)
			HP = 0;

		// Set Green Bar
		if (health > 0) {
			for (int i = 0; i <= health / 2; i++) {
				Color green = new Color(new Point2D(4 - i, GRID_HEIGHT), "Green");
				gui.addImage(green);
			}
		}

		// Set Red Bar
		if (HP > 0) {
			for (int i = 0; i <= HP; i++) {
				Color red = new Color(new Point2D((int) ((i / 2) - 0.5), GRID_HEIGHT), "Red");
				gui.addImage(red);
			}
		}

		// If Health is odd
		if (health % 2 == 1) {
			int x = 4 - health / 2;
			Color redGreen = new Color(new Point2D(x, GRID_HEIGHT), "RedGreen");
			gui.addImage(redGreen);
		}
	}

	public void updateCatchedItems() {
		int aux = 0;
		Point2D position = new Point2D(GRID_WIDTH - 3 + aux, GRID_HEIGHT);
		Color black = new Color(position, "Black");
		gui.addImage(black);
		for (GameElement element : hero.getCatchedItems()) {
			Point2D p = new Point2D(GRID_WIDTH - 3 + aux, GRID_HEIGHT);
			element.setPosition(p);
			gui.addImage(element);
			aux++;
		}

	}

/////////////////////////////// Key drops ///////////////////////////////

	public void consume(int tecla) {
		int ListPosition = tecla - 1;
		if (tecla <= hero.getCatchedItems().size()
				&& hero.getCatchedItems().get(ListPosition) instanceof HealingPotion) {
			GameElement potion = hero.getCatchedItems().get(ListPosition);
			((HealingPotion) potion).consume();
			dropCatchable(tecla);
			rmvElement(potion);
		} else {
			return;
		}

	}

	public void dropCatchable(int position) {

		Point2D dropPosition = GameEngine.getInstance().getHeroPosition();
		if (position <= hero.getCatchedItems().size() && clearCatchPosition(dropPosition)) {
			int ListPosition = position - 1;
			GameElement droppedItem = hero.getCatchedItems().get(ListPosition);
			((Catchable) droppedItem).Drop(ListPosition, dropPosition);

		} else {
			return;
		}

	}
	
	
//	Based on a given position, the function returns false if a "catchable" exists at that position and true otherwise. 
	
	public boolean clearCatchPosition(Point2D p) {
		for (GameElement element : elements) {
			if (element.getPosition().equals(p) && !(element instanceof Hero))
				return false;
		}
		return true;
	}

////////////////////////////////////////////// Scores File //////////////////////////////////////////////

//	Before the game closes, a scores file is created. First, the existing scores are read and added to 
//		the scores array. Then, the scores are sorted in descending order using the Comparator interface, 
//		and only the top five scores are kept, removing the rest. Finally, the top five scores are 
//	written to a new file, replacing the previous one.

////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void createScoresFile() {
		try {
			File f = new File("pontuações.txt");
			readScores(f);
			get5BestScores();

			if (f.exists()) {
				f.delete();
			}

			f = new File("pontuações.txt");

			PrintWriter writer = new PrintWriter(f);
			writer.println("Nome Jogador:Pontuacões\n-----------------------");

			for (Score score : scores) {
				writer.println(score);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readScores(File f) {

		scores.add(new Score(nomeJog, getHero().getScore()));
		if (f.exists()) {
			try {
				Scanner in = new Scanner(f);
				int nline = 0;
				while (in.hasNext()) {
					String line = in.next();
					if (nline > 2) {
						String split[] = line.split(":");
						if (split.length == 2) {
							Score scr = new Score(split[0], Integer.parseInt(split[1]));
							scores.add(scr);
						}
					}
					nline++;
				}
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void get5BestScores() {
		Comparator<Score> comparadorScores = ((t1, t2) -> t2.getScore() - t1.getScore());
		if (scores.size() >= 2) {
			scores.sort(comparadorScores);
		}
		if (scores.size() > 5) {
			for (int i = 4; i < scores.size(); i++) {
				scores.remove(i);
			}
		}
	}

//////////////////////////////////////////// Salvar Nível /////////////////////////////////////////////

//	É criada uma nova Sala com o nome do nível e com os elementos que o jogo tem no momento. Se a sala 
//		existir, apenas é feito um update dos elementos guardados na lista de salas do jogo e, caso 
//		contrário, a sala é adicionada a essa lista.

//////////////////////////////////////////////////////////////////////////////////////////////////////

	public void save() {
		killer();
		for (Sala sala : rooms) {
			if (sala.getLevel().equals(level)) {
				sala.setElementsList(elements);
				return;
			}
		}
		rooms.add(new Sala(level, elements));
	}

/////////////////////////////// End Game ///////////////////////////////

	public void endGame(String str) {
		if (str.equals("win")) {
			end = true;
			int x = GRID_WIDTH / 2;
			int y = GRID_HEIGHT / 2;
			// place GameOver
			for (int i = 1; i <= 5; i++) {
				GameElement img = new GameElement(4, "GameOver" + i, new Point2D(x - 3 + i, y));
				gui.addImage(img);
			}
			// place trofeu
			for (int i = 0; i <= 1; i++) {
				for (int j = 0; j <= 1; j++) {
					GameElement img = new GameElement(4, "Trofeu" + i + j, new Point2D(x - 1 + i, y - 2 + j));
					gui.addImage(img);
				}
			}
			createScoresFile();
			gui.setMessage("A sua pontuação é de " + getHero().getScore() + " pontos.");
		}
		if (str.equals("death")) {
			Sala remove = null;
			for (Sala sala : rooms) {
				if (sala.getLevel() == level) {
					remove = sala;
				}
			}
			if (remove != null)
				rooms.remove(remove);
			((Movable) hero).decayHP(-10);
			elementsCleaner();
			hero.catchablesDrop();
			killer();
			setDoor();
			addObjects();

			gui.setMessage("O herói morreu? Deseja recomeçar no último checkPoint?");
		}
		if (str.equals("close")) {
			if (this.nomeJog != null) {
				createScoresFile();
			}
		}
	}

//////////////////////////////////////// Remove Elements ////////////////////////////////////////

//	Arrays containing the objects to be removed are used to ensure the safe removal of elements
//		from the main list.

/////////////////////////////////////////////////////////////////////////////////////////////////

	public void rmvElement(GameElement victim) {
		itemsToRemove.add(victim);
	}

	public void killer() {
		if (itemsToRemove.size() > 0) {
			List<ImageTile> remove = new ArrayList<>();
			for (GameElement element : itemsToRemove) {
				remove.add(((ImageTile) element));
				if (element instanceof Thief && ((Thief) element).getStolenItem() != null) {
					elements.add(((Thief) element).getStolenItem());
				}
			}

			elements.removeAll(itemsToRemove);
			gui.removeImages(remove);
			itemsToRemove.clear();
		}
	}

	public void elementsCleaner() {
		for (GameElement element : elements) {
			if (!(element instanceof Hero))
				itemsToRemove.add(element);
		}
	}
}

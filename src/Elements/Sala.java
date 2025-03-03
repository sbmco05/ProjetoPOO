package Elements;

import java.util.ArrayList;
import java.util.List;

import movables.GameElement;
import movables.Hero;
import starter.GameEngine;

public class Sala {
	private String level;
	private List<GameElement> ElementsList = new ArrayList<>();

	public Sala(String level, List<GameElement> elementsList) {
		super();
		this.level = level;
		for (GameElement ge : elementsList) {
			if (!(ge instanceof Hero)) {
				this.ElementsList.add(ge);
			}
		}
	}

	public String getLevel() {
		return level;
	}

	public List<GameElement> getElementsList() {
		return this.ElementsList;
	}

	public void setElementsList(List<GameElement> elementsList) {
		this.ElementsList.clear();
		for (GameElement ge : elementsList) {
			if (!(ge instanceof Hero))
				this.ElementsList.add(ge);
		}
	}

}

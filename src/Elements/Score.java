package Elements;

public class Score {

	private String nomeJog;
	private int score;

	public Score(String nomeJog, int score) {
		this.nomeJog = nomeJog;
		if (nomeJog.equals("") || nomeJog == null)
			this.nomeJog = "Jogador";

		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getNomeJog() {
		return nomeJog;
	}

	@Override
	public String toString() {
		return nomeJog + ":" + score;
	}

}

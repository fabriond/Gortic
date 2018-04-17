
public class User {
	private final String name;
	private int score = 0;
	
	public User(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getScore() {
		return score;
	}
	
	public void addScore() {
		score++;
	}
}
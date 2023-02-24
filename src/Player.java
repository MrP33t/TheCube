import java.awt.Color;
import java.awt.Graphics2D;

public class Player {
	public int x, y;
	public boolean player;
	private int speed;
	public int UID;
	
	public Player(int x, int y, boolean player) {
		this.UID = (int) (Math.random() * 100000);
		this.x = x;
		this.y = y;
		
		this.player = player;
		this.speed = 5;
		
		System.out.println("Player UID:" + UID);
	}
	
	public Player (int x, int y, boolean player, int UID) {
		this.UID = UID;
		this.x = x;
		this.y = y;
		
		this.player = player;
		this.speed = 5;
	}
	
	public int getX() { 
		return x;
	}
	public int getY() { 
		return y;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void moveUP() {
		this.y -= speed;
	}
	public void moveDOWN() {
		this.y += speed;
	}
	public void moveLEFT() {
		this.x -= speed;
	}
	public void moveRIGHT() {
		this.x += speed;
	}
	
	public void update() {
		
	}
	
	public void draw(Graphics2D g2D) {
		if (this.player) {
			g2D.setColor(Color.RED);
		} else {
			g2D.setColor(Color.WHITE);
		}
		
		g2D.fillRect(this.x, this.y, GamePanel.TILE_WIDTH, GamePanel.TILE_HEIGHT);
	}
}

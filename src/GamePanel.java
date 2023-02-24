import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
	
	// Getting screen Size
	public final static int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	public final static int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
	
	// Calculating Window Size
	public final static int WINDOW_WIDTH = SCREEN_WIDTH / 5 * 4;
	public final static int WINDOW_HEIGHT = SCREEN_HEIGHT / 5 * 4;
	
	public final static int TILE_WIDTH = WINDOW_WIDTH / 20;
	public final static int TILE_HEIGHT = WINDOW_HEIGHT / 20;
	
	// Program Frames Per Second
	private final static int FPS = 60;
	
	// Variables for calculating delta (for run() method)
	private final static double drawInterval = 1000000000 / FPS;
	private double delta;
	private long lastTime, currentTime, timer;
	private int drawCount;
	private int lastFPSCount = 0;
	
	// Game Thread
	Thread gameThread;
	
	// Players
	public static ArrayList<Player> players = new ArrayList<>();
	
	
	// Handlers
	private KeyboardHandler keyH;
	
	// Networking 
	
	private static Server server;
	private static Client client;
	
	private static String serverAddress = "localhost";
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.setBackground(Color.BLACK);
		this.setDoubleBuffered(true);
		
		
		players.add(new Player(600, 600, true));
		this.keyH = new KeyboardHandler();
		this.addKeyListener(keyH);
		
		this.setFocusable(true);
	}
	
	public void startGameThread() {
		this.gameThread = new Thread(this);
		this.gameThread.start();
	}

	public static void startServer() {
		try {
			server = new Server();
			joinServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void joinServer() {
		try {
			Socket socket = new Socket(serverAddress, 2243);
			client = new Client(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void updatePlayerLocation(Location location) {
		
		
	}
	
	@Override
	public void run() {
		
		delta = 0;
		lastTime = System.nanoTime();
		timer = 0;
		drawCount = 0;
		
		while(this.gameThread != null) {
			
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			
			if (delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}
			
			if (timer >= 1000000000) {
				lastFPSCount = drawCount;
				drawCount = 0;
				timer = 0;
			}
		}
		
	}
	
	private void update() {
		for (Player p: players) {
			if (p.player) {
				Client.update(p.x, p.y, p.UID);
				
			}
		}
		// get location array from client and update players
		boolean flag;
		synchronized(Client.lock) {
			for (Location l: Client.playersLocation) {
				flag = false;
				for (Player p: players) {
					if (l.UID == p.UID) {
						p.x = l.x;
						p.y = l.y;
						flag = true;
					}
				}
				if (flag == false) {
					if (l.UID != 0) {
						players.add(new Player(l.x, l.y, false, l.UID));
					}
				}
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2D = (Graphics2D) g;
		
		for (Player p: players) {
			p.draw(g2D);
		}
		
		g2D.dispose();
	}
}

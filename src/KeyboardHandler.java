import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardHandler implements KeyListener {

	public KeyboardHandler () {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		for (Player p: GamePanel.players) {
			if (p.player) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_W:
					p.moveUP();
					break;
				case KeyEvent.VK_A:
					p.moveLEFT();
					break;
				case KeyEvent.VK_S:
					p.moveDOWN();
					break;
				case KeyEvent.VK_D:
					p.moveRIGHT();
					break;
				}
			}
		}
		switch (e.getKeyCode()) {
		case KeyEvent.VK_O:
			GamePanel.startServer();
			break;
		case KeyEvent.VK_P:
			GamePanel.joinServer();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}

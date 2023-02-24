import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable{
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Thread clientThread;
	
	private static int clientX = 0;
	private static int clientY = 0;
	private static int clientUID = 0;
	
	Location location;
	
	public static Object lock = new Object();
	
	public static ArrayList<Location> playersLocation = new ArrayList<>();
	
	public Client(Socket socket) {
		try {
			this.socket = socket;
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
			sendLocation();
			this.clientThread = new Thread(this);
			clientThread.start();
			
			System.out.println("Client created");
			
		} catch (IOException e) {
			e.printStackTrace();
			closeEverything(socket, out, in);
		}
	}
	
	public void sendLocation() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					location = new Location(clientX, clientY, clientUID);
					try {
						out.writeObject(location);
						out.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						closeEverything(socket, out, in);
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}).start();
	}
	
	@Override
	public void run() {
		
		try {
			while (socket.isConnected()) {

				location = (Location) in.readObject();
				// update static array
				boolean flag;
				synchronized (lock) {
					if (!(clientUID == location.UID)) {
						if (!playersLocation.isEmpty()) {
							flag = false;
							for (Location l: playersLocation) {
								if (l.UID == location.UID) {
									l.x = location.x;
									l.y = location.y;
									flag = true;
								}
							}
							if (flag == false) {
								playersLocation.add(location);
							}
						} else {
							playersLocation.add(location);
						}
			
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			closeEverything(socket, out, in);
		}
	}
	
	public static void update(int x, int y, int uid) {
		clientX = x;
		clientY = y;
		clientUID = uid;
	}
	private void closeEverything(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
		try {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

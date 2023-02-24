import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	private static Object lock = new Object();
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Location location;
	private int id;
	
	public ClientHandler(Socket socket, int id) {
		try {
			this.id = id;
			this.socket = socket;
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
			synchronized (lock) {
				clientHandlers.add(this);
			}
			System.out.println("Client Handler " + this.id +" created");
			
		} catch (IOException e) {
			e.printStackTrace();
			closeEverything(socket, out, in);
		}
	}
	
	private void removeClientHandler() {
		clientHandlers.remove(this);
	}
	private void closeEverything(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
		removeClientHandler();
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
	
	private void broadcastLocation(Location location) {
		synchronized (lock) {
			for (ClientHandler clientHandler: ClientHandler.clientHandlers) {
				try {
					clientHandler.out.writeObject(location);
				} catch (Exception e) {
					e.printStackTrace();
					closeEverything(socket, out, in);
				}
				
				try {
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void run() {
		while (socket.isConnected()) {
			try {
				location = (Location) in.readObject();
				broadcastLocation(location);
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				closeEverything(socket, out, in);
				break;
			}
		}
	}

}

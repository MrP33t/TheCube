import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable{
	
	public static ServerSocket serverSocket;
	private Thread serverThread;
	private static int idCounter = 1;
	
	public Server() throws IOException {
		serverSocket = new ServerSocket(2243);
		
		this.serverThread = new Thread(this);
		serverThread.start();
		
		System.out.println("Server created");
		
	}
	
	public void closeServer() {
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while(!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();

				ClientHandler clientHandler = new ClientHandler(socket, idCounter);
				idCounter++;
				
				Thread thread = new Thread(clientHandler);
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			closeServer();
		}
	}
}

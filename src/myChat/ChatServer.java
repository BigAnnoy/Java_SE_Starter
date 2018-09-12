package myChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author BigAnnoy Last_update 2018年9月12日上午9:20:10
 *
 */
public class ChatServer {
	ServerSocket serverSocket = null;
	Socket socket = null;
	private boolean started = false;
	List<Client> clients = new ArrayList<Client>();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChatServer().startServer();
	}

	/**
	 * 
	 */
	private void startServer() {
		// TODO Auto-generated method stub
		try {
			serverSocket = new ServerSocket(2345);
			started = true;
			while (started) {
				socket = serverSocket.accept();
				System.out.println("a client connected");
				Client client = new Client(socket);
				clients.add(client);
				new Thread(client).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public class Client implements Runnable{
		private Socket clientSocket = null;
		private DataInputStream dataInputStream = null;
		private boolean clientConnected = false;
		private String str = null;
		private DataOutputStream dataOutputStream = null;
		
		public Client(Socket s) {
			this.clientSocket = s;
			clientConnected = true;
			try {
				dataInputStream = new DataInputStream(clientSocket.getInputStream());
				dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			}
		private void post(String str) {
			for(Client c :clients) {
				try {
					c.dataOutputStream.writeUTF(str);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public void run() {
			
			while (clientConnected) {
				try {
					str = dataInputStream.readUTF();
					System.out.println(str);
					post(str);
				} catch (EOFException e) {
					System.out.println("One Client Disconnected!");
					clientConnected = false;
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			// TODO Auto-generated method stub

		}
	}
	
		
	}
}


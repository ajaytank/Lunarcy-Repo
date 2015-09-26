package control;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;


public class Server {
	// Network related fields
		private ServerSocket serverSocket;
		private int maxClients;
		private static final int PORT = 58627;
		private ArrayList<ClientConnection> clientList = new ArrayList<ClientConnection>();
		private LinkedBlockingQueue<NetworkAction> messageQueue = new LinkedBlockingQueue<NetworkAction>();
	
		Server(int maxClients){
			this.maxClients = maxClients;
			try {
				serverSocket = new ServerSocket(PORT);
				listenForClients();
			} catch (IOException e) {
				e.printStackTrace();
			}
			processActions();
		}
		
	private void processActions() {
			while(true){
				if(!messageQueue.isEmpty()){
					System.out.println();
				}
			}
		}

	private void listenForClients() throws IOException{
		System.out.println("Listeneing for clients");
		//wait for all clients to connect
		while(clientList.size() < maxClients){
			Socket s = serverSocket.accept();
			int clientID = clientList.size();
			ClientConnection client = new ClientConnection(s,clientID);
			//client.start();
			clientList.add(client);
		}
	}




	private class ClientConnection{
		Socket socket;
		ObjectInputStream inputFromClient;
        ObjectOutputStream outputToClient;
		int clientID;
		String userName;
		//Color colour;
		
		ClientConnection(Socket socket, int id) throws IOException {
			this.socket = socket;
			clientID = id;
			inputFromClient = new ObjectInputStream(socket.getInputStream());
        	outputToClient = new ObjectOutputStream(socket.getOutputStream());
        	// Sleep for a bit
			try{
			    Thread.sleep(500);
			} catch(InterruptedException ex){Thread.currentThread().interrupt();}
			
			// Read the user name sent from the client
        	try{
				this.userName = (String) inputFromClient.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
        	System.out.println("new Client" + clientID);
        	
        	// Begin listening to this client
        	new Thread(new Runnable(){ public void run(){
        		listenToClient();
            }}).start();
		}
		
		public void listenToClient(){
    		// While the client is sending messages
    		while (true){
    			NetworkAction action = null;
				try {
					action = (NetworkAction)inputFromClient.readObject();
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(action != null)messageQueue.add(action);
    			  
    			// If the client disconnected
//    			if (line.equals("CQUIT"))
//    			{
//    				// Close the socket
//    				try
//    				{
//		                this.socket.close();
//		                this.socket = null;
//    		        } catch(IOException e){System.out.println("Failed to close client socket " + e + "\n");}
//    				
//    				// Shut down the server
//    				System.out.println("\nClient Disconnected, Shutting Server Down\n");
//    				
//    				// Sleep for a bit
//    				try
//    				{
//    				    Thread.sleep(2000);
//    				} catch(InterruptedException ex){Thread.currentThread().interrupt();}
//    				quit();
//    			}
    		}
    	}
    	
    	/**
    	 * Send a message to the client
    	 */
    	public void write(String message)
    	{
    		if (!message.equals(null))
    		{
    			
    			try {
    				outputToClient.writeObject("Yo");
					outputToClient.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
	}
}

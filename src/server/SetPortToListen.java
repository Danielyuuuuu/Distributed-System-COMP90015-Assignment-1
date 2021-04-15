package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import client.ExceptionHandling;

/**
 * Set up a Server port that constantly listen for connections
	 */
public class SetPortToListen {
	//Create a server socket listening on port
	private ServerSocket listeningSocket;
	private int port;
	
	public SetPortToListen(int port) throws ExceptionHandling {

		this.port = port;
		try {
			this.listeningSocket = new ServerSocket(port);
			if (listeningSocket != null && listeningSocket.isBound()) {
				System.out.println("in if");
				new Thread(() -> run()).start();
			}else {
				System.out.println("in else");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//			String exception = "The port " + port + " has been occupied";
//			throw new ExceptionHandling(exception, "Try again using another port");
			System.out.println(e.getMessage());
			
		}
	}
	
	
	public Boolean isServerActive() {
		if (listeningSocket != null && listeningSocket.isBound()) {
			return true;
		}
		return false;
	}
	
	public void run() {
//		serverStatus.setText("Listening on port: " + port);
		System.out.println("Server listening on port " + port +  " for a connection");
		
        // Constantly listening on a specific port
        while (true) {

        	// Client socket
            Socket client;
            
			try {
				// Connect a new client
				client = listeningSocket.accept();
				
	            System.out.println("New client connected: ");
	            
	            // Create a new thread for handling the new client connection
	            new Thread(new HandleClientConnection(client)).start();
	            
			} catch (IOException e) {
				System.out.println(e.getMessage());
				System.exit(1);
				
			}
        }			
	}		
}

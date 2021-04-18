/*
 * The program is written by Yifei Yu
 * Student ID: 945753
 */

package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Set up a Server port that constantly listen for connections
 */
public class SetPortToListen {
	//Create a server socket listening on port
	private ServerSocket listeningSocket;
	
	public SetPortToListen(int port){
		try {
			this.listeningSocket = new ServerSocket(port);
			if (listeningSocket != null && listeningSocket.isBound()) {
				new Thread(() -> run()).start();
			}else {
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());	
		}
	}
	
	
	public Boolean isServerActive() {
		if (listeningSocket != null && listeningSocket.isBound()) {
			return true;
		}
		return false;
	}
	
	
	/*
	 * Constantly listening on a specific port
	 */
	public void run() {
		
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

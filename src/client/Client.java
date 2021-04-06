package client;
import java.io.*;
import java.net.Socket;

public class Client {
	String hostname;
	int port;
	static ClientGUI clientWindow;
	
	public static void main(String[] args) throws ExceptionHandling {
		System.out.print("Hello from client\n");
		
//		if (args.length != 2) {
//			throw new ExceptionHandling("Input error", "Not enough input");
//		}
		
		clientWindow = new ClientGUI();
		clientWindow.frame.setVisible(true);
		System.out.print("After initializing client gui\n");
		
	}
	
	
	
//	public Client(String txt){
//		System.out.println(txt);
//	}
}



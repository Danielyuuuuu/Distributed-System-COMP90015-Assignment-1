package client;
import java.io.*;
import java.net.Socket;

public class Client {
	String hostname;
	int port;
	
	public static void main(String[] args) throws ExceptionHandling {
		System.out.print("Hello from client");
		
		if (args.length != 2) {
			throw new ExceptionHandling("Not enough input");
		}
	}
}



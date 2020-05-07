package Serverside;
import java.net.*;
import java.io.*;
public class Server {

	public ServerSocket server;
	InetAddress ip;
	
	
	public void generateServer() {
		Server server1 = new Server();
		Server server2 = new Server();
		Server server3 = new Server();
		server1.create(3012);
		server1.start();
		server2.create(3012);
		server2.start();
		server3.create(3012);
		server3.start();
		
	}
	
	public void create(int port) {
		try {	
			server = new ServerSocket(port);
			// Schliest nach 10 Sekunden wenn 
			// keine Verbindung zum Client existiert
			// server.setSoTimeout(100000);	
			System.out.println("Server created");
			
		} 	
		catch (IOException e) {	
			e.printStackTrace();
		}	
	}

	public void start() {
		try {
			ip = InetAddress.getLocalHost();
		} 
		catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("Wait for Client on port: " + server.getLocalPort());
		System.out.println("IP Address:- " + ip );
		
		while(true) {	
			try {
				Socket client = server.accept();
				System.out.println("Client accept");
				
				while(true) {
					final InputStream inFromServer = client.getInputStream();
		        final BufferedReader in = new BufferedReader(new InputStreamReader(inFromServer));
		        System.out.println(in.readLine());
				}

				//client.close(); // Disconnect Server
				//System.out.println("Client close");
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void close() {
		try {
			server.close();
			System.out.println("Close Server");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
}

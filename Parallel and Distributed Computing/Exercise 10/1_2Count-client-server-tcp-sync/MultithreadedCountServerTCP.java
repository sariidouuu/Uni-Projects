import java.net.*;
import java.io.*;
import java.util.HashMap;

public class MultithreadedCountServerTCP {
	private static final int PORT = 1234;

	// Μοιραζόμενη δομή: Κρατάει το n ως κλειδί και το pi ως τιμή 
    public static HashMap<Long, Double> resultsCache = new HashMap<>();

	//public static Count n = new Count();

	public static void main(String args[]) throws IOException {
		ServerSocket connectionSocket = connectionSocket = new ServerSocket(PORT);
		
		while (true) {	
			System.out.println("Server is listening to port: " + PORT);
			Socket dataSocket = connectionSocket.accept();
			System.out.println("Received request from " + dataSocket.getInetAddress());

			ServerThread sthread = new ServerThread(dataSocket);
			sthread.start();
		}
	}
}
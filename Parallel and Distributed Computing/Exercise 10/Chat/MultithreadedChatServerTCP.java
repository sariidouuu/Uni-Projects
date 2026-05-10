import java.net.*;
import java.util.Vector;
import java.io.*;

public class MultithreadedChatServerTCP {
	private static final int PORT = 1234;
	
	// στατική δομή 
	static Vector<PrintWriter> writers = new Vector<>();

	public static void main(String args[]) throws IOException {

		// Αρχικοποίηση του server 
		// Δημιουργεί ένα "socket υποδοχής". Αυτό το αντικείμενο δεσμεύει τη θύρα 1234 στον υπολογιστή και περιμένει αιτήματα σύνδεσης από πελάτες.
		ServerSocket connectionSocket = connectionSocket = new ServerSocket(PORT);
		
		while (true) {	// Ένας ατέρμονος βρόχος που επιτρέπει στον server να μην κλείνει ποτέ.
			// Μόλις ολοκληρώσει τη σύνδεση ενός ζευγαριού πελατών, επιστρέφει στην αρχή για να περιμένει το επόμενο ζευγάρι.
			System.out.println("Server is waiting first client in port: " + PORT);
			
			Socket dataSocket = connectionSocket.accept(); // Δέχεται ΕΝΑΝ πελάτη τη φορά
			System.out.println("Received request from " + dataSocket.getInetAddress());

    		// Ξεκινάει αμέσως το thread για αυτόν τον πελάτη
			ServerThread sthread = new ServerThread(dataSocket); 
			sthread.start();
		}
	}
}
// Η αρχιτεκτονική του συγκεκριμένου project χρησιμοποιεί συνολικά 6 threads για μια συνομιλία μεταξύ δύο ατόμων.

// Στην πλευρά του Εξυπηρετητή (Server): 2 threads για το ζευγάρι των πελατών
// Στην πλευρά του κάθε Πελάτη (Client): κάθε εφαρμογή πελάτη που εκτελείται χρησιμοποιεί 2 threads. Για 2 πελάτες είναι 4 threads
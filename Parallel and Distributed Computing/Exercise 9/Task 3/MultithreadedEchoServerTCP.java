// MultithreadedEchoServerTCP (Ταυτόχρονος/Πολυνηματικός Διακομιστής)
// 1. Πολλαπλοί πελάτες ταυτόχρονα: Διαθέτει εσωτερικούς μηχανισμούς ταυτοχρονισμού, επιτρέποντας την ταυτόχρονη εξυπηρέτηση πολλών πελατών.
// 2. Ανάθεση σε Νήματα (Threads): Μόλις ο διακομιστής αποδεχτεί μια σύνδεση (accept()), δεν ξεκινάει την επικοινωνία ο ίδιος. 
// Αντίθετα, δημιουργεί ένα νέο, ξεχωριστό νήμα (ServerThread sthread = new ServerThread(dataSocket);) και του περνάει την υποδοχή.
// 3. Ανεξάρτητη εκτέλεση: Το νέο νήμα εκκινείται (sthread.start()) 
// και αναλαμβάνει εξ ολοκλήρου τον βρόχο ανταλλαγής μηνυμάτων while(!outmsg.equals(EXIT)) για τον συγκεκριμένο πελάτη.
// 4. Άμεση επιστροφή σε αναμονή: Ο κύριος βρόχος while(true) του διακομιστή επιστρέφει ακαριαία στην εντολή accept() 
// και είναι αμέσως έτοιμος να δεχτεί τον επόμενο πελάτη, χωρίς να περιμένει να ολοκληρωθεί η επικοινωνία με τον πρώτο.

import java.net.*;
import java.io.*;

public class MultithreadedEchoServerTCP {
	private static final int PORT = 1234;
	
	public static void main(String args[]) throws IOException {

		ServerSocket connectionSocket = new ServerSocket(PORT);
		
		while (true) {	

			System.out.println("Server is listening to port: " + PORT);
			Socket dataSocket = connectionSocket.accept();
			System.out.println("Received request from " + dataSocket.getInetAddress());

			ServerThread sthread = new ServerThread(dataSocket);
			sthread.start();
		}
	}
}
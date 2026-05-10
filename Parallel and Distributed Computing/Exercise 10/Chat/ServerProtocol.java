import java.net.*;
import java.io.*;

public class ServerProtocol {
	public String processRequest(String theInput) {
		System.out.println("Received message from client: " + theInput); // Λαμβάνει το μήνυμα από το ServerThread
		String theOutput = theInput;
		System.out.println("Send message to client: " + theOutput); // Εκτυπώνει στην κονσόλα του server (για logging) τι παραλήφθηκε και τι θα προωθηθεί
		return theOutput; // Επιστρέφει το ίδιο το μήνυμα (theOutput = theInput) ώστε να προωθηθεί αυτούσιο στον άλλον πελάτη.
	}
}
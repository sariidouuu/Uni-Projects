import java.net.*;
import java.io.*;

public class ClientProtocol_Ex1 {
	BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
	
	// Στην εργασία, εδώ θα μπει το μενού επιλογών και η σύνθεση του μηνύματος
	// Διαβάζει την είσοδο του χρήστη από το πληκτρολόγιο (System.in) και προετοιμάζει το μήνυμα/αίτημα (request) στη μορφή που "καταλαβαίνει" ο διακομιστής.
	public String prepareRequest() throws IOException {
		System.out.println("\n--- Options Menu ---");
        System.out.println("1. Convert to Lowercase");
        System.out.println("2. Convert to Uppercase");
        System.out.println("3. Caesar Encoding");
        System.out.println("4. Caesar Decoding");
        System.out.println("Type 'CLOSE' to exit.");
        System.out.print("Choice: ");
		
		String choice = user.readLine();
		
		if (choice.equalsIgnoreCase("CLOSE")) return "CLOSE";

		// Ζητάμε key μόνο αν είναι για κωδικοποίηση ή αποκωδικοποίηση (3 ή 4)
        String offset = "0";
        if (choice.equals("3") || choice.equals("4")) {
            System.out.print("Enter key: ");
            offset = user.readLine();
        }

        System.out.print("Write the message: ");
        String message = user.readLine();

        // Επιστρέφουμε ΕΝΑ ενιαίο String στο EchoClientTCP
		// "ο πελάτης στέλνει όλη τη πληροφορία σε ένα μήνυμα, όχι σε πολλά διαδοχικά μηνύματα".
		// Χωρίς διαχωριστικό: Αν τα ενώναμε απλά, θα πήγαινε στον διακομιστή το κείμενο: "35hello"
		// Με διαχωριστικό ;: Το κείμενο γίνεται: "3;5;hello".
        return choice + ";" + offset + ";" + message;
	}
	
	// Παραλαμβάνει την απάντηση (reply) που ήρθε από τον διακομιστή (το κρυπτογραφημένο κείμενο) και αποφασίζει πώς θα την εμφανίσει στην οθόνη του χρήστη.
	public void processReply(String theInput) throws IOException {
		System.out.println("Message received from server: " + theInput);
	}
}

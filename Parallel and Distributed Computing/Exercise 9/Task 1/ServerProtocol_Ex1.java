import java.net.*;
import java.io.*;

public class ServerProtocol_Ex1 {
	// Παίρνει το ακατέργαστο μήνυμα που έστειλε ο πελάτης, το "αποκωδικοποιεί" (διαβάζει την εντολή), κάνει την απαιτούμενη επεξεργασία 
	// (π.χ. μετατροπή σε πεζά, κωδικοποίηση Caesar) και παράγει το τελικό αποτέλεσμα (theOutput). 
	// Το αποτέλεσμα αυτό το επιστρέφει στον κεντρικό κώδικα του server για να αποσταλεί πίσω.
	public String processRequest(String theInput) {
		if (theInput.equals("CLOSE")) return "CLOSE";

		// Σπάμε το μήνυμα στα 3 μέρη του
        String[] parts = theInput.split(";", 3);
        String choice = parts[0];
        int offset = Integer.parseInt(parts[1]);
        String message = parts[2];

        switch (choice) {
            case "1": return message.toLowerCase(); // Μετατροπή σε πεζά
            case "2": return message.toUpperCase(); // Μετατροπή σε κεφαλαία
            case "3": return caesar(message, offset); // Κρυπτογράφηση
            case "4": return caesar(message, -offset); // Αποκωδικοποίηση (αρνητικό offset)
            default: return "Wrong choice!";
        }
	}

    private String caesar(String message, int offset) {
        StringBuilder result = new StringBuilder();
        // Διασφάλιση ότι το offset είναι πάντα θετικό (0-25) για το modulo
        int shift = (offset % 26 + 26) % 26; 

        for (char character : message.toCharArray()) {
            if (Character.isLetter(character)) {
                char base = Character.isUpperCase(character) ? 'A' : 'a';
                int originalPos = character - base;
                int newPos = (originalPos + shift) % 26;
                result.append((char) (base + newPos));
            } else {
                result.append(character); // Οι άλλοι χαρακτήρες μένουν ίδιοι[cite: 9]
            }
        }
        return result.toString();
    }
}
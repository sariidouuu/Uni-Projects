import java.net.*;
import java.io.*;

public class ChatClientProtocol {

	BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
	
	public String sendMessage() throws IOException {
		System.out.print("Send message, CLOSE for exit:");
		String theOutput = user.readLine();
		return theOutput;
        }

	public String receiveMessage(String theInput) throws IOException {
		System.out.println("\nReceived message: " + theInput);
        System.out.print("Send a reply, CLOSE for exit:");
		return theInput;
	}
}

// Η χρήση του throws IOException στις μεθόδους της κλάσης ChatClientProtocol είναι απαραίτητη για τη διαχείριση σφαλμάτων 
// που μπορεί να προκύψουν κατά τη διάρκεια της εισόδου ή εξόδου δεδομένων (Input/Output).
//

// Στη μέθοδο sendMessage(), χρησιμοποιείτε την εντολή user.readLine() για να διαβάσετε αυτό που γράφει ο χρήστης.
// Η μέθοδος readLine() της Java είναι προγραμματισμένη να μπορεί να "πετάξει" (throw) μια IOException 
// αν κάτι πάει στραβά με τη ροή δεδομένων (π.χ. αν κλείσει απρόσμενα το stream εισόδου).

// Επειδή η IOException ανήκει στις checked exceptions, η Java σας υποχρεώνει είτε να τη διαχειριστείτε τοπικά με ένα try-catch block, 
// είτε να δηλώσετε ότι η μέθοδος "μεταφέρει" το σφάλμα παραπάνω χρησιμοποιώντας το throws.

// Μεταβίβαση Ευθύνης (Exception Delegation) !
// Ο κύριος ρόλος του throws IOException στο ChatClientProtocol είναι να ενημερώσει το πρόγραμμα που καλεί αυτές τις μεθόδους 
// (στην περίπτωσή σας τις κλάσεις SendThread και ReceiveThread στο αρχείο ChatClient.java) ότι υπάρχει πιθανότητα σφάλματος.
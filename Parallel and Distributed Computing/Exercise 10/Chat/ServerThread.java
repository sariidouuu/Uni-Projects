import java.io.*;
import java.net.*;

class ServerThread extends Thread{
	private Socket myDataSocket; // ο πελάτης που "ακούει" αυτό το thread
	//private Socket otherDataSocket; // ο πελάτης στον οποίο θα "στείλει" τα μηνύματα.
	private InputStream is;
	private BufferedReader in;
	private OutputStream os;
	private PrintWriter out;
	private static final String EXIT = "CLOSE";

	public ServerThread(Socket socket){
		myDataSocket = socket;
		try {
			is = myDataSocket.getInputStream(); // Διαβάζει από τον Client A.
			in = new BufferedReader(new InputStreamReader(is));
			//os = otherDataSocket.getOutputStream(); // Γράφει στον Client B.
			os = myDataSocket.getOutputStream(); // Ροή εξόδου: δείχνει στον ΙΔΙΟ τον πελάτη
			out = new PrintWriter(os,true); // Αυτό το 'out' θα χρησιμοποιηθεί για να λαμβάνει ο πελάτης μηνύματα από το chat
		}
		catch (IOException e){		
			System.out.println("I/O Error " + e);
		}
	}

	public void run(){
		String inmsg, outmsg;
		
		try {
			// Προσθήκη του πελάτη στη μοιραζόμενη δομή 
            MultithreadedChatServerTCP.writers.add(out);

			inmsg = in.readLine(); // Διαβάζει το μήνυμα από τον έναν πελάτη
			ServerProtocol app = new ServerProtocol(); 
			outmsg = app.processRequest(inmsg); // Το περνάει από το ServerProtocol.
			
			while(!outmsg.equals(EXIT)) {
				for (PrintWriter writer : MultithreadedChatServerTCP.writers) {
   					if (writer != out) { // Ελέγχει αν ο παραλήπτης ΔΕΝ είναι ο ίδιος ο αποστολέας, ώστε να μην εμφανίζεται το το μήνυμα του αποστολέα στον ίδιο
						writer.println(outmsg); 
					}
				}
				
				inmsg = in.readLine();
            	if (inmsg == null) break; // Διαχείριση απότομης αποσύνδεσης
				outmsg = app.processRequest(inmsg);
			}		
			MultithreadedChatServerTCP.writers.remove(out);
			myDataSocket.close();
			System.out.println("Data socket closed");

		} catch (IOException e)	{		
			System.out.println("I/O Error " + e);
			// Αφαίρεση ακόμα και σε περίπτωση σφάλματος
			MultithreadedChatServerTCP.writers.remove(out);
		}
	}	
}
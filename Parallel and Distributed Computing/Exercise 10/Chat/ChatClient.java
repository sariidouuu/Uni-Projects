import java.net.*;
import java.io.*;

// Spawn a different thread for receiving messages
// Due to multiple threads used, simultaneous communication is now possibl

// Δημιουργεί δύο ξεχωριστά threads: ένα για την αποστολή μηνυμάτων και ένα για τη λήψη, 
// ώστε να μπορεί ο χρήστης να μιλάει και να ακούει ταυτόχρονα (Full Duplex).

public class ChatClient {
	private static final int PORT = 1234;
        //private static final InetAddress HOST = InetAddress.getLocalHost();
        private static final String HOST = "localhost";

	public static void main(String args[]) throws IOException{
		Socket dataSocket = new Socket(HOST,PORT); // Ανοίγει τη σύνδεση με τον server
        System.out.println("Connection to " + HOST + " established");

		// Threads: Δημιουργεί και ξεκινά την SendThread και την ReceiveThread. 
		// Αυτό είναι το κλειδί για να μην "κολλάει" το πρόγραμμα περιμένοντας μόνο είσοδο από το πληκτρολόγιο.
		SendThread send = new SendThread(dataSocket);
		Thread thread = new Thread(send);
		thread.start();
		ReceiveThread receive = new ReceiveThread(dataSocket);
		Thread thread2 = new Thread(receive);
		thread2.start();
	}
}	

class SendThread implements Runnable{

	private Socket dataSocket;
        private OutputStream os; // Παίζει τον ρόλο του αγωγού για την αποστολή δεδομένων προς το δίκτυο
        private PrintWriter out; // Στέλνει δεδομένα
	
	public SendThread(Socket soc) throws IOException {
		dataSocket = soc;
		// Μέσω της εντολής os = dataSocket.getOutputStream(); στον constructor, το πρόγραμμα αποκτά πρόσβαση 
		// στο κανάλι επικοινωνίας που έχει ανοίξει η dataSocket με τον server.
		os = dataSocket.getOutputStream();
		// Χρησιμοποιείται ως παράμετρος για τη δημιουργία του PrintWriter out = new PrintWriter(os,true);. 
		// Ενώ η OutputStream διαχειρίζεται τα byte, ο PrintWriter "πατάει" πάνω της για να επιτρέψει στον προγραμματιστή να στέλνει κείμενο (Strings)
		// με ευκολία, χρησιμοποιώντας μεθόδους όπως η println().
		out = new PrintWriter(os,true);
	}
	
	public void run() {
		try{
            String outmsg;
            ChatClientProtocol app = new ChatClientProtocol();
			outmsg = app.sendMessage();
			while(!outmsg.equals("CLOSE")) {
				out.println(outmsg);
				outmsg = app.sendMessage();
			}	
			out.println(outmsg);
			dataSocket.close();
			
		}catch (IOException e){}
	}
	
}

class ReceiveThread implements Runnable{

	private Socket dataSocket;
        private InputStream is;
        private BufferedReader in;
	
	public ReceiveThread(Socket soc) throws IOException {
		dataSocket = soc;
        is = dataSocket.getInputStream();
		in = new BufferedReader(new InputStreamReader(is));
	}
	
	public void run() {
		try{
			String inmsg;
            ChatClientProtocol app = new ChatClientProtocol();
            inmsg = app.receiveMessage(in.readLine()); // Περιμένει συνεχώς για νέα μηνύματα.
			while(inmsg != null) {
				inmsg = app.receiveMessage(in.readLine()); // Μόλις λάβει κάτι, το εμφανίζει στην οθόνη μέσω της μεθόδου receiveMessage.
			}
		}catch (IOException e){}	
	}
	
}
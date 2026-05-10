import java.net.*;
import java.io.*;

public class CountClientTCP {
	private static final String HOST = "localhost";
	private static final int PORT = 1234;
	
	public static void main(String args[]) throws IOException {

		Socket dataSocket = new Socket(HOST, PORT);
		
		InputStream is = dataSocket.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		OutputStream os = dataSocket.getOutputStream();
		PrintWriter out = new PrintWriter(os,true);

		// Διάβασμα από το πληκτρολόγιο
        BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));

		String inmsg, outmsg;
		ClientProtocol app = new ClientProtocol();
		
		while(true){
			System.out.print("Give a number (-1 to exit): ");
			outmsg = userIn.readLine(); // Διαβάζεις το n από το πληκτρολόγιο
			
			// Στέλνουμε το n στον server
			out.println(outmsg);

			if(outmsg == "-1") {
				outmsg = app.prepareExit();
				out.println(outmsg);
				break;
			}

			inmsg = in.readLine(); // Λαμβάνεις το υπολογισμένο π
            if (inmsg == null) break;
			app.processReply(inmsg); // Το εκτυπώνεις
		}

		/*
		//Ο πελάτης συνδέεται στον server και στέλνει 10.000 αιτήματα για αύξηση του μετρητή πριν αποσυνδεθεί
		int iters = 10000;
		for (int i = 0; i < iters; i++)  {
			outmsg = app.prepareRequest();
			out.println(outmsg);
			inmsg = in.readLine();
			app.processReply(inmsg);
		}

		outmsg = app.prepareExit();
		out.println(outmsg);
		//inmsg = in.readLine();
	    //app.processReply(inmsg);
		*/

		dataSocket.close();
		System.out.println("Connection closed.");
	}
}
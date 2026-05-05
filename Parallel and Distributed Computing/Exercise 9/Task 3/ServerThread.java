import java.io.*;
import java.net.*;

class ServerThread extends Thread{
	private Socket dataSocket;
	private InputStream is;
	private BufferedReader in;
	private OutputStream os;
	private PrintWriter out;
	private static final String EXIT = "CLOSE";

	public ServerThread(Socket socket){
		dataSocket = socket;
		try {
			is = dataSocket.getInputStream();
			in = new BufferedReader(new InputStreamReader(is));
			os = dataSocket.getOutputStream();
			out = new PrintWriter(os,true);
		}
		catch (IOException e)	{		
			System.out.println("I/O Error " + e);
		}
	}

	public void run(){
		String inmsg, outmsg;
		
		try {
			inmsg = in.readLine();

			// 1. Έχω αφήσει ίδιο τον κώδικα στο MultithreadedEchoServerTCP (δηλαδή ServerThread sthread = new ServerThread(dataSocket);)
			// 2. Έχω ονομάσει τα server protocol ανάλογα με το excercise: Ex1 και Ex2
			// 3. και έχω προσθέσει τις 3 παρακάτω γραμμές ώστε να επιλέγω όποιο Server Protocol θέλω να χρησιμοποιήσω.
			// (για να μην έχω τα ίδια 3 αρχεία με μόνο μία γραμμή διφορετική)

			//ServerProtocol app = new ServerProtocol();
			//ServerProtocol_Ex1 app = new ServerProtocol_Ex1();
			ServerProtocol_Ex2 app = new ServerProtocol_Ex2();

			outmsg = app.processRequest(inmsg);
			while(!outmsg.equals(EXIT)) {
				out.println(outmsg);
				inmsg = in.readLine();
				outmsg = app.processRequest(inmsg);
			}		

			dataSocket.close();
			System.out.println("Data socket closed");

		} catch (IOException e)	{		
			System.out.println("I/O Error " + e);
		}
	}	
}
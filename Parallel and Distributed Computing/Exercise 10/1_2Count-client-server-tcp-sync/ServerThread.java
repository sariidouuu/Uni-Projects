import java.io.*;
import java.net.*;

class ServerThread extends Thread{
	private Socket dataSocket;
	private InputStream is;
	private BufferedReader in;
	private OutputStream os;
	private PrintWriter out;
	//private Count count; Δεν το χρειαζόμαστε διότι δεν απαιτείται μοιραζόμενη κατάσταση

	public ServerThread(Socket socket){
		dataSocket = socket;
		try {
			is = dataSocket.getInputStream(); 
			in = new BufferedReader(new InputStreamReader(is));
			os = dataSocket.getOutputStream();
			out = new PrintWriter(os, true);
			//count = c;
		}
		catch (IOException e)	{		
			System.out.println("I/O Error " + e);
		}
	}
	
	public void run(){
		String inmsg, outmsg;
		
		try {
			inmsg = in.readLine();
			ServerProtocol app = new ServerProtocol();
			//outmsg = app.processRequest(inmsg); 
			
			while (inmsg != null) {
				outmsg = app.processRequest(inmsg);
                if (outmsg.equals("EXIT")) {
					break; // Τερματισμός αν n == -1 
				}
                out.println(outmsg); 
                inmsg = in.readLine();	
			}	
			dataSocket.close();	

		} catch (IOException e)	{		
			System.out.println("I/O Error " + e);
		}
	}	
}
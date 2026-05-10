import java.net.*;
import java.io.*;

public class ClientProtocol {

	public String prepareRequest(String n) throws IOException {
		return n;
		//String theOutput = "INC";
		//return theOutput;
	}
        
	public String prepareExit() throws IOException {
		String theOutput = "EXIT";
		return theOutput;
	}

	public void processReply(String theInput) throws IOException {
		System.out.println("Reply: " + theInput);
	}
}

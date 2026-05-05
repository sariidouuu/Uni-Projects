import java.net.*;
import java.io.*;

public class ClientProtocol_Ex2{

	BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
	
	// message types 
	// requestMessage (string): op <space> a <space> b 
	// exitMessage (string): !  
	// replyMessage (string): R <space> r 
	// errorMessage (string): E <space> err_code

	public String prepareRequest() throws IOException {
		System.out.println("\n--- Calculator Menu ---");
        System.out.println("Operations: +, -, *, /");
		System.out.println("Type '!' to exit");
        System.out.print("Enter operation: ");
        String op = user.readLine();

		// exitMessage (string): ! 
		if (op.equals("!")) return "CLOSE";

		System.out.print("Enter first number (a): ");
        String a = user.readLine();
        
        System.out.print("Enter second number (b): ");
        String b = user.readLine();

        // requestMessage (string): op <space> a <space> b 
        return op + " " + a + " " + b;
	}

	public void processReply(String theInput) throws IOException {
		// replyMessage (string): R <space> r 
		// errorMessage (string): E <space> err_code

        if (theInput.startsWith("R")) {
            System.out.println("Result: " + theInput.substring(2));
        } else if (theInput.startsWith("E")) {
            System.out.println("Error code: " + theInput.substring(2));
        } else {
            System.out.println("Server reply: " + theInput);
        }
	}
}
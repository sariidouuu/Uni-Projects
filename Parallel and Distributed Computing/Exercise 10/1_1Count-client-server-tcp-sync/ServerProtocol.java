import java.net.*;
import java.io.*;

public class ServerProtocol {
	private Count counter;

	// Διαγράφουμε τον constructor
	/*public ServerProtocol (Count c) {
		counter = c;
    }*/

	public String processRequest(String theInput) {

		if(theInput.equals("-1")) return "EXIT";

		try{
			long numSteps = Long.parseLong(theInput);
			double sum = 0.0;
			double step = 1.0 / (double)numSteps;
        	/* do computation */
			for (long i=0; i < numSteps; ++i) {
				double x = ((double)i+0.5)*step;
				sum += 4.0/(1.0+x*x);
			}
        	double pi = sum * step;
			return ("Computed pi: " + pi) ;
		} catch(NumberFormatException e) {
			return "Wrong number" ;
		}
	}
}
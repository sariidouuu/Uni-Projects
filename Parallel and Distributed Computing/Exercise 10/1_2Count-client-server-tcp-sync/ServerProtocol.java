import java.net.*;
import java.io.*;

public class ServerProtocol {
	private Count counter;

	// Διαγράφουμε τον constructor
	/*public ServerProtocol (Count c) {
		counter = c;
    }*/

	public String processRequest(String theInput) {
		if (theInput.equals("-1")) return "EXIT";

		try {
			long n = Long.parseLong(theInput);

			// Έλεγχος αν υπάρχει ήδη το αποτέλεσμα 
			synchronized(MultithreadedCountServerTCP.resultsCache) {
				if (MultithreadedCountServerTCP.resultsCache.containsKey(n)) {
					double cachedPi = MultithreadedCountServerTCP.resultsCache.get(n);
					return "Found in cache. Pi = " + cachedPi; // Επιστροφή χωρίς υπολογισμό 
				}
			}

			// Αν ΔΕΝ υπάρχει υπολογίζουμε το π
			double sum = 0.0;
			double step = 1.0 / (double)n;
			for (long i=0; i < n; ++i) {
				double x = ((double)i+0.5)*step;
				sum += 4.0/(1.0+x*x);
			}
			double pi = sum * step;

			// Αποθηκεύουμε το νέο αποτέλεσμα στη δομή 
			synchronized(MultithreadedCountServerTCP.resultsCache) {
				MultithreadedCountServerTCP.resultsCache.put(n, pi);
			}
			return "Computed pi: " + pi;
		} catch (NumberFormatException e) {
			return "Wrong number";
		}
	}
}
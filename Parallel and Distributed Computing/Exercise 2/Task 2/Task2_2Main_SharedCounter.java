//2. Task1_1_Main_SharedCounter with LOCKS

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Task2_2Main_SharedCounter {
	
    public static void main(String[] args) {
		int end = 1000;
		int[] array = new int[end];
		int numThreads = 4;

		CounterThread threads[] = new CounterThread[numThreads];
		// Δημιουργώ τη lock στην Main ώστε αν την περάσω ως όρισμα στην CounterThread
		// Περνάω αναφορά, ώστε κάθε νήμα να λαμβάνει μια αναφορά στο κλείδωμα που πρέπει να χρησιμοποιήσει.

		// Η lock που στέλνεις ως όρισμα είναι ΕΝΑ και ΜΟΝΑΔΙΚΟ αντικείμενο (instance) το οποίο μοιράζονται όλα τα νήματα.
		// Το περνάω ως όρισμα αναφοράς, και όχι όρισμα τιμής
		Lock lock = new ReentrantLock();
		
		for (int i = 0; i < numThreads; i++) {
			
			threads[i] = new CounterThread(end, array, lock);
			threads[i].start();
		}
	
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {}
		} 
		check_array (end, array, numThreads);
    }

    static void check_array (int end, int[] array, int numThreads)  {
		int i, errors = 0;
		System.out.println ("Checking...");

        for (i = 0; i < end; i++) {
			if (array[i] != numThreads*i) {
				errors++;
				System.out.printf("%d: %d should be %d\n", i, array[i], numThreads*i);
			}         
        }
        System.out.println (errors+" errors.");
    }

    static class CounterThread extends Thread {
		int end;
		int[] array;
		Lock lock;

        public CounterThread(int end, int[] array, Lock lock) {
			this.end = end;
			this.array = array;
			this.lock=lock;
        }

        public void run() {
            for (int i = 0; i < end; i++) {
				lock.lock();
					try{
					for (int j = 0; j < i; j++)
						array[i]++;	
					} finally {
						lock.unlock();
					}		
            } 
		}    
    }
}
// Στην αρχή είχα το lock.lock() μέσα στο εσωτερικό loop. 
// Ο κώδικας ήταν μεν απόλυτα ασφαλής (δεν υπήρχε data race), αλλά καθυστερούσε υπερβολικά λόγω του τεράστιου overhead.
// Η διαδικασία να ζητάς και να αφήνεις το lock εκατομμύρια φορές είναι πολύ 'βαριά' για τον επεξεργαστή. 
// Βγάζοντας το lock στο εξωτερικό loop, το νήμα παίρνει το κλείδωμα μία φορά, κάνει όλες τις προσθέσεις και το αφήνει, 
// κερδίζοντας τεράστια ταχύτητα χωρίς να θυσιάζεται η ασφάλεια.

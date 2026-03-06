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
// Όμως το lock/unlock γινόταν τόσο γρήγορα και που η JVM ή ο επεξεργαστής δεν προλάβαιναν να συγχρονίσουν σωστά τις τιμές μεταξύ των διαφορετικών πυρήνων (cores),
// Πριν: Για κάθε μία αύξηση (++), το νήμα έπρεπε να ζητήσει το κλείδωμα, να το πάρει, να αλλάξει τη μνήμη και να το αφήσει.
// Τώρα: Το νήμα παίρνει το κλείδωμα μία φορά, κάνει όλες τις προσθέσεις που χρειάζονται για το συγκεκριμένο i (δηλαδή j φορές) και μετά το αφήνει.
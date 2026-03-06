//2. Task1_1_Main_SharedCounter_While.java with LOCKS
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Task2_2Main_SharedCounterWhile{
    public static void main(String[] args) {

        int end = 10000;
        //int counter = 0;
        int[] array = new int[end];
        int numThreads = 4;

        CounterThread threads[] = new CounterThread[numThreads];
        //Lock lock = new ReentrantLock();
        SharedCounter counter = new SharedCounter();
	
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new CounterThread(end, array, counter);
			threads[i].start();
		}
	
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {}
		} 
        check_array(end, array);
    }
    static void check_array (int end, int[] array)  {
		int i, errors = 0;

		System.out.println ("Checking...");

        for (i = 0; i < end; i++) {
			if (array[i] != 1) {
				errors++;
				System.out.printf("%d: %d should be 1\n", i, array[i]);
			}         
		}
        System.out.println (errors+" errors.");
	}

// Αν αφήσω το counter μέσα στην κλάση CounterThread τότε:
// Όταν θα περάσω το counter στον constructor της CounterThread, δεν περνώ μια κοινή μεταβλητή, αλλά ένα αντίγραφο της τιμής της.
// Στη main, το counter είναι 0. Αλλά όταν δημιουργώ τα 4 threads, το καθένα παίρνει τη δική του τοπική μεταβλητή this.counter=0.
// Ακόμα και αν χρησιμοποιώ lock, κάθε thread αυξάνει το δικό του counter.
// Λύση: Δημιουργώ μια κλάση SharedData η οποία θα περιέχει μόνο το int counter και την κοινή Lock.

    static class SharedCounter {

        static Lock lock = new ReentrantLock();
        // Αν έβαζα την Lock μέσα στην CounterThread, τότε το κάθε thread θα είχε το δικό του lock (=> Data Race).
        int count;

        public SharedCounter() {
            this.count = 0;
        }

        public int get(){
            return count;
        }

        public void inc(){
            count++;
        }
    }

    static class CounterThread extends Thread {
        int end;
        int[] array;
        SharedCounter counter;

        public CounterThread(int end, int[] array, SharedCounter counter) {
            this.end = end;
            this.array = array;
            this.counter=counter;
        }

        public void run() {
            while (true) {
                SharedCounter.lock.lock();
                try{
                    if (counter.get() >= end) break;
                    array[counter.get()]++;
                    counter.inc();		
                } finally {
                    SharedCounter.lock.unlock();
                }    
            } 
            // Το if πρέπει να είναι μέσα στο κρίσιμο τμήμα που προστατεύει το lock για να ελεέγξει τη συνθήκη και να εκτελέσει την πράξη
            // Αν δεν το βάζαμε: Πραάδειγμα: Έστω counter=10 & thread a μπαίνει στο κρίσιμο τμήμα, μπαίνει στο Lock. Δεν προλαβαίνει να αυξήσει τον counter. 
            // Ταυτόχρονα thread b διαβάζει counter=10, περιμένει το thread a να αυξήσει το counter & να ξεκλειδώσει, αλλά το thread b έχει ήδη διαβάσει counter=10. 
		}            	
    }
}
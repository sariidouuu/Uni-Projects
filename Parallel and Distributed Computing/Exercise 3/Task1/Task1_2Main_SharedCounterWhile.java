//2. Task2_2_Main_SharedCounter_While.java with LOCKS from exercise 2

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Task1_2Main_SharedCounterWhile{
    public static void main(String[] args) {

        int end = 10000;
        int[] array = new int[end];
        int numThreads = 4;

        CounterThread threads[] = new CounterThread[numThreads];
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
    static class SharedCounter {
        //static Lock lock = new ReentrantLock();
        //static Object lockObj = new Object(); 
        // ΔΕ μπορώ να δημιουργήσω static lock object διότι θα αφορούσε την κλάση και όχι το αντικείμενο
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
                // Κλειδώνω το ίδιο το αντικείμενο counter, που διαμοιράζονται τα νήματα
                // Διασφαλίζω ότι τα νήματα χρησιμοποιούν το ίδιο ακριβώς κλειδί για να προστατεύσουν τα δεδομένα που αυτό περιέχει
                synchronized(counter){
                    if (counter.get() >= end) break;
                    array[counter.get()]++;
                    counter.inc();		
                }  
            }  
		}            	
    }
}
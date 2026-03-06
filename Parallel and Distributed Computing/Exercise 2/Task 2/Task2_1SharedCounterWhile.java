import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Task2_1SharedCounterWhile { 
    static int end = 10000;
    static int counter = 0;
    static int[] array = new int[end];
    static int numThreads = 4;

	static Lock lock = new ReentrantLock();

    public static void main(String[] args) {

        CounterThread threads[] = new CounterThread[numThreads];
	
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new CounterThread();
			threads[i].start();
		}
	
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {}
		} 
        check_array ();
    }
    static void check_array ()  {
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


    static class CounterThread extends Thread {
        public CounterThread() {
        }

        public void run() {
            while (true) {
				if (counter >= end) break;       // SSSSSSSSOOOOOOOOSSSSSSSS
				// Αν το Lock είναι εκτός του if τυπώνει τα εξής στο terminal.. ArrayIndexOutOfBoundsException
				// Πρέπει ο έλεγχος της μεταβλητής counter να γίνεται μέσα στο synchronized block, 
				// ώστε κανένα άλλο νήμα να μην μπορεί να την αλλάξει ανάμεσα στον έλεγχο και τη χρήση της.

				lock.lock();
				try{
					array[counter]++;
					counter++;	
				}finally{
					lock.unlock();
				}		
            } 
		}            	
    }
}


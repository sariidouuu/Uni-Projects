//2. Task2_1_Main_SharedCounter with LOCKS from exercise 2

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Task1_2Main_SharedCounter {
	
    public static void main(String[] args) {
		int end = 1000;
		int[] array = new int[end];
		int numThreads = 4;

		CounterThread threads[] = new CounterThread[numThreads];
		Lock lock = new ReentrantLock();

		Object lockObj = new Object(); // Ένα κοινό lock object για όλα τα νήματα
		
		for (int i = 0; i < numThreads; i++) {
			// Στέλνω σαν όρισμα στη CounterThread το lockObj
			threads[i] = new CounterThread(end, array, lock, lockObj);
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
		Object lock;


        public CounterThread(int end, int[] array, Lock lock, Object lockObj) {
			this.end = end;
			this.array = array;
			this.lock=lockObj;
        }

        public void run() {
            for (int i = 0; i < end; i++) {
				synchronized(lock){
					for (int j = 0; j < i; j++)
						array[i]++;	
					}		
            } 
		}    
    }
}
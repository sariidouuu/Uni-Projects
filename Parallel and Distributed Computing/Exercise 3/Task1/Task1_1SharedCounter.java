
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Task1_1SharedCounter {
    static int end = 1000;
    static int[] array = new int[end];
    static int numThreads = 4;

	static Lock lock = new ReentrantLock();

	static Object lockObj = new Object(); // Ένα κοινό lock object για όλα τα νήματα

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
			if (array[i] != numThreads*i) {
				errors++;
				System.out.printf("%d: %d should be %d\n", i, array[i], numThreads*i);
			}         
        }
        System.out.println (errors+" errors.");
    }

    static class CounterThread extends Thread {
        public CounterThread() {
        }

        public void run() {
            for (int i = 0; i < end; i++) {
				for (int j = 0; j < i; j++){
					synchronized (lockObj) { array[i]++; }   		
				} 
			}    
		}
	}
}

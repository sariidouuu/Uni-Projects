//2. Task2_2_Args_SharedCounter.java with LOCKS from exercise 2

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Task1_3Args_SharedCounter {

    public static void main(String[] args) {

		SharedData sharedData = new SharedData();

		int numThreads = 4;
		CounterThread threads[] = new CounterThread[numThreads];
		

		for (int i = 0; i < numThreads; i++) {
			threads[i] = new CounterThread(sharedData);
			threads[i].start();
		}
	
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {}
		} 
		check_array(sharedData, numThreads);
    }

	static void check_array(SharedData sharedData, int numThreads)  {
		int i, errors = 0;

		System.out.println ("Checking...");

		for (i = 0; i < sharedData.end; i++) {
			if (sharedData.array[i] != numThreads*i) {
				errors++;
				System.out.printf("%d: %d should be %d\n", i, sharedData.array[i], numThreads*i);
			}         
		}
		System.out.println (errors+" errors.");
	}
}

class SharedData {
	int end;
	int[] array;
	//Lock lock = new ReentrantLock();

	// Δημιορυργώ ένα lock object στην sharedData ώστε να είναι κοινό, για κάθε Instance του κοινού αντικειμένου SharedData που έχει δημιουργηθεί στη main
	Object lockObj = new Object();

	public SharedData() {
		this.end = 1000;
		this.array = new int[end];
	}

	public void arrayInc(int i) {
		synchronized(lockObj){ // Μπορώ και synchronized(this), δηλαδή και πάλι πάνω στο ίδιο το αντικείμενο
			array[i]++;
		}
	}
	//Επίσης μπορώ και synchronized public void arrayInc(int i)

	public int array(int i) {
		return array[i];
	}
}

class CounterThread extends Thread {
	private int end;
	private SharedData sharedData; 

    public CounterThread(SharedData sharedData) {
		this.end = sharedData.end;
		this.sharedData = sharedData;
    }

    public void run() {
        for (int i = 0; i < end; i++) {
			for (int j = 0; j < i; j++)
				sharedData.arrayInc(i);		
        } 
	}    
}


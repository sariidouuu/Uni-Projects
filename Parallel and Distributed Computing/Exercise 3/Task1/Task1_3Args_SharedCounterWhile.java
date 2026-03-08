//2. Task2_2_Main_SharedCounterWhile.java with LOCKS from exercise 2

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Task1_3Args_SharedCounterWhile{

    public static void main(String[] args) {

        int numThreads = 4;
        CounterThread threads[] = new CounterThread[numThreads];

        //Δημιορυγούμε αντικείμενο SharedData
		SharedData sharedData = new SharedData();

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
        check_array(sharedData);
    }

    static void check_array (SharedData sharedData)  {
		int i, errors = 0;

		System.out.println ("Checking...");

        for (i = 0; i < sharedData.end; i++) {
			if (sharedData.array[i] != 1) {
				errors++;
				System.out.printf("%d: %d should be 1\n", i, sharedData.array[i]);
			}         
		}
        System.out.println (errors+" errors.");
	}
}

class SharedData {
	int end;
	int[] array;
    int counter;
	//Lock lock = new ReentrantLock();
	Object lockObj = new Object();
	// Σε αντίθεση με την Task1_2Main_SharedCounterWhile, εδώ μπορώ να δημιουργήσω αντικείμενο μέσα στην κλάση sharedData διότι αυτό δεν θα είναι static
	// Το lock object εδώ αφορά το συγκεκριμένο instance της κλάσης
	
	// Η διαφορά είναι πως εδώ έχω τις κλάσεις SharedData και CounterThread εκτός της Task1_3Args_SharedCounterWhile, 
	// ενώ στο Task1_2 While όχι, οι κλάσεις εκει SharedData και CounterThread ήταν static και χρειαζόμουν static μεταβλητή lock object 

	public SharedData() {
		this.end = 1000;
		this.array = new int[end];
        this.counter = 0;
	}

	public void arrayInc() {
		array[counter]++;
	}

    public void counterInc(){
		counter++;
    }

	public int array(int i) {
		return array[i];
	}

    public int get() {
        return counter;
    }

    public int end() {
        return end;
    }
}

class CounterThread extends Thread {
    SharedData sharedData;

    public CounterThread(SharedData sharedData) {
        this.sharedData = sharedData;
    }

    public void run() {
        while (true) {
			synchronized(sharedData.lockObj){
				if (sharedData.get() >= sharedData.end()) break;
				sharedData.arrayInc();
				sharedData.counterInc();		
			}
        } 
	}            	
}

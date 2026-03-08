//2. Task2_3Args_SharedCounter.java from lab 2

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class Task2_2_fromLab2 {
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
			if (sharedData.array.get(i) != numThreads*i) {
				errors++;
				System.out.printf("%d: %d should be %d\n", i, sharedData.array.get(i), numThreads*i);
			}         
		}
		System.out.println (errors+" errors.");
	}
}

class SharedData {
	int end;
	//int[] array;
	//Lock lock = new ReentrantLock();
	//List<Integer> list;

	// Επιχείρησα να χρησιμοποιήσω την CopyOnWriteArrayList αλλά μου πετούσε errors, 
	// επειδή η πράξη array[i]++ θα έπρεπε να γραφτεί list.set(i, list.get(i) + 1); κάτι το οποίο είανι 2 πράξεις ( get & set) και όχι 1.
	// Οπότε χρησιμοποίησα μια κλάση όπου το κάθε στοιχείο είναι atomic εξαρχής
	AtomicIntegerArray array; 

	// Atomic: Σημαίνει ότι η πράξη incrementAndGet(i) εκτελείται ολόκληρη σαν μία αδιαίρετη πράξη στο hardware level, 
	// χωρίς κανένα thread να μπορεί να "μπει στη μέση".
	
	public SharedData() {
		this.end = 1000;
		//this.array = new int[end];
		//this.list = new CopyOnWriteArrayList<>();
		this.array = new AtomicIntegerArray(end);
	}

	public void arrayInc(int i) {
		array.incrementAndGet(i);
	}

	public int array(int i) {
		return array.get(i);
		// Χρειάζομαι .get() διότι ο array δεν είναι απλός πίνακας με στοιχεία, είναι αντικείμενο
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


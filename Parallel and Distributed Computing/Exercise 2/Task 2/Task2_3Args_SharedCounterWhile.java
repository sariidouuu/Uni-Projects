//2. Task1_2_Main_SharedCounterWhile.java with LOCKS
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Task2_3Args_SharedCounterWhile{

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
	Lock lock = new ReentrantLock();

	public SharedData() {
		this.end = 1000;
		this.array = new int[end];
        this.counter = 0;
	}

	public void arrayInc() {
		//lock.lock();
		//try{ array[counter]++; } finally { lock.unlock(); }	
		array[counter]++;
	}

    public void counterInc(){
        //lock.lock();
		//try{ counter++;  } finally { lock.unlock(); }	
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
			sharedData.lock.lock();
			// Με την ίδια λογική του Task2_2Main_SharedCounterWhile η if πρέπει να μπέι μέσα στο Lock.
			try{
				if (sharedData.get() >= sharedData.end()) break;
				sharedData.arrayInc();
				sharedData.counterInc();		
			} finally { sharedData.lock.unlock(); }
        } 
	}            	
}

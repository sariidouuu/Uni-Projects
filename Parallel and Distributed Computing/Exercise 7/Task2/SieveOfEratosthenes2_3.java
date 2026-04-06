import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SieveOfEratosthenes2_3{
	// διαμοιραζόμενη μεταβλητή μετρητή εργασιών - δείχνει ποιο είναι το επόμενο στοιχείο του πίνακα προς επεξεργασία
	static int tasksAssigned = 1; //ξεκινά από 1, ώστε το πρώτο ++ δίνει 2   
	static Lock lock = new ReentrantLock();
	public static void main(String[] args){  
		
		int size = 0;
		int numThreads = 0;

		if (args.length != 2) {
			System.out.println("Usage: java SieveOfEratosthenes <size> <number of threads> ");
			System.exit(1);
		}

		try {
			size = Integer.parseInt(args[0]);
			numThreads = Integer.parseInt(args[1]);
		}
		catch (NumberFormatException nfe) {
			System.out.println("Integer argument expected");
			System.exit(1);
		}
		if (size <= 0) {
			System.out.println("size should be positive integer");
			System.exit(1);
		}
		if(numThreads <= 0) numThreads = Runtime.getRuntime().availableProcessors();

		boolean[] prime = new boolean[size+1];
		for(int i = 2; i <= size; i++)
			prime[i] = true; 		

		int limit = (int)Math.sqrt(size) + 1;

		// get current time 
		long start = System.currentTimeMillis();

		Thread[] threads = new Thread[numThreads];

		// Δημιουργία κ εκκίνηση νημάτων εργαζομένων
		for(int i=0; i < threads.length; i++){
			threads[i] = new Thread(new Worker(prime, size, limit));
			threads[i].start();
		}
		// Αναμονή τερματισμού νημάτων εργαζομένων
		for(int i = 0; i < threads.length; i++) {
            try {
				threads[i].join();
			}
			catch (InterruptedException e) {
				System.err.println("this should not happen");
			}
        }

        // get current time and calculate elapsed time
		long elapsedTimeMillis = System.currentTimeMillis()-start;

		int count = 0;
		for(int i = 2; i <= size; i++) 
			if (prime[i] == true) {
				//System.out.println(i); 
				count++;
			}

		System.out.println("number of primes "+count); 
		System.out.println("time in ms = "+ elapsedTimeMillis);
	}

	// Κρίσιμο τμήμα: ατομική λήψη επόμενης εργασίας
    static int getTask(int limit) {
        lock.lock();
        try {
            if (++tasksAssigned <= limit) // Έχω ++tasksAssigned οπότε από 1 γίνεται 2
                return tasksAssigned;
            else
                return -1;
        } finally {
            lock.unlock();
        }
    }

	// Μέσα στην κλάση ώστε να βλέπει τη getTask()
    static class Worker implements Runnable {
        private boolean[] prime;
        private int size;
        private int limit;

        Worker(boolean[] prime, int size, int limit) {
            this.prime = prime;
            this.size = size;
            this.limit = limit;
        }

        public void run() {
            int task;
            while ((task = SieveOfEratosthenes2_3.getTask(limit)) >= 0) {
                if (prime[task]) {
                    for(int i = task * task; i <= size; i += task) //ίδιος τύπος με πδφ 7 διαφ 37 i+P
                        prime[(int) i] = false;
                }
            }
        }
	}
}
//Threads : 4
// Size          |  Time in ms
// 10.000        |  1
// 100.000       |  5
// 1.000.000     |  21
// 10.000.000    |  28
// 100.000.000   |  671
// 1.000.000.000 |  9967

//ΣΥΝΟΛΙΚΑ:
// SIZE          |  SERIAL    |  STATIC   |  CYCLIC    |  DYNAMIC
// 10.000        |  0 		 |  1		 |  3 	      |  1
// 100.000       |  0		 |	5		 | 	3 		  |  5
// 1.000.000     |  8		 |	18		 | 	10		  |  21
// 10.000.000    |  31		 |	18		 | 	24 		  |  28
// 100.000.000   |  999		 |	1015	 | 	883		  |  671
// 1.000.000.000 |  12.455	 |	10379	 | 	10972	  |  9967
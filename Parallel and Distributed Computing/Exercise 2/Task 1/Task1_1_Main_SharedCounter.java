//1.1 Mε χρήση ορισμάτων του main().

public class Task1_1_Main_SharedCounter {

    /*static int end = 1000;
    static int[] array = new int[end];
    static int numThreads = 4;*/

    public static void main(String[] args) {
		//Προσθέτουμε τις μεταβλητές στη main: 
		int end = 1000;
		int[] array = new int[end];
		int numThreads = 4;

		CounterThread threads[] = new CounterThread[numThreads];
		
		for (int i = 0; i < numThreads; i++) {
			// Οι μεταβλητές μας πλέον δεν είναι static και καθολικές, αλλά τοπικές τις main.
		    // Χρειάζεται να τις περάσουμε ως ορίσματα στην μέθοδο CounterThread για να έχει πρόσβαση σε αυτές.
			threads[i] = new CounterThread(end, array);
			threads[i].start();
		}
	
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {}
		} 
		// Οι μεταβλητές μας πλέον δεν είναι static και καθολικές, αλλά τοπικές τις main.
		// Χρειάζεται να τις περάσουμε ως ορίσματα στην μέθοδο check_array για να έχει πρόσβαση σε αυτές.
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
		// Επειδή η CounterThread χρειάζεται Constructor, δηλώνουμε τις μεταβλητές που χρειαζόμαστε και τις δίνουμε τις τιμές των ορισμάτων
		int end;
		int[] array;

        public CounterThread(int end, int[] array) {
			this.end = end;
			this.array = array;
        }

        public void run() {
            for (int i = 0; i < end; i++) {
				for (int j = 0; j < i; j++)
					array[i]++;		
            } 
		}    
    }
}

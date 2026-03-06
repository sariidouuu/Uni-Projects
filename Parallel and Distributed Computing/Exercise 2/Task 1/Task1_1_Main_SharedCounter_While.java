//1.1 Mε χρήση ορισμάτων του main().

public class Task1_1_Main_SharedCounter_While{
    /*static int end = 10000;
    static int counter = 0;
    static int[] array = new int[end];
    static int numThreads = 4;*/

    public static void main(String[] args) {

        int end = 10000;
        int counter = 0;
        int[] array = new int[end];
        int numThreads = 4;

        CounterThread threads[] = new CounterThread[numThreads];
	
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


    static class CounterThread extends Thread {
        int end;
        int[] array;
        int counter;

        public CounterThread(int end, int[] array, int counter) {
            this.end = end;
            this.array = array;
            this.counter = counter;
        }

        public void run() {
            while (true) {
				if (counter >= end) break;
                array[counter]++;
				counter++;		
            } 
		}            	
    }
}
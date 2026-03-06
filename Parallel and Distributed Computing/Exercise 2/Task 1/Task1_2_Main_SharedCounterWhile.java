//1.2 Mε χρήση μοιραζόμενης δομής/αντικειμένου.

public class Task1_2_Main_SharedCounterWhile{
    /*static int end = 10000;
    static int counter = 0;
    static int[] array = new int[end];
    static int numThreads = 4;*/

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
			if (sharedData.get() >= sharedData.end()) break;
            sharedData.arrayInc();
			sharedData.counterInc();		
        } 
	}            	
}

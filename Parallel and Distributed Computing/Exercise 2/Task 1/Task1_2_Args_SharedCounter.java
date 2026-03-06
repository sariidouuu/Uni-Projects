//1.2 Mε χρήση μοιραζόμενης δομής/αντικειμένου.

public class Task1_2_Args_SharedCounter {
    /*static int end = 1000;
    static int[] array = new int[end];
    static int numThreads = 4;*/

    public static void main(String[] args) {

		//Δημιορυγούμε αντικείμενο SharedData
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
	
	//Κρατάμε την μέθοδο check_array μέσα στην κλάση Task1_2_Args_SharedCounterArrayGlobal ώστε να μπορούμε να την καλέσουμε απευθείας
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

// Προσθέτουμε μια κλάση sahredData η οποία περιέχει τις κοινές μεταβλητές
// Δημιουργούμε τον Constructor 
// Προσθέτουμε μια μέθοδο arrayInc η οποία θα διαχερίζεται την εντολή array[i]++; του CounterThread run
class SharedData {
	int end;
	int[] array;

	public SharedData() {
		this.end = 1000;
		this.array = new int[end];
	}

	public void arrayInc(int i) {
		array[i]++;
	}

	public int array(int i) {
		return array[i];
	}
}

class CounterThread extends Thread {
	private int end;
	private SharedData sharedData; 

	// Προσθέτουμε constructor αφού δεν έχουμε καθολικές μεταβλητές
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


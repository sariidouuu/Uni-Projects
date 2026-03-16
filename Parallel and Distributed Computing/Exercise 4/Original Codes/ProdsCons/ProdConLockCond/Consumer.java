public class Consumer extends Thread {
	private Buffer buff;
	private int scale;

	// Constructor
	public Consumer(Buffer b, int s) {
		this.buff = b;
		this.scale = s;
	}

	// Producer runs for reps times with random(scale) intervals
	public void run() {
		int value;
		while (true) { //Όσο έχει να διαβασει είναι True
			try {
				sleep((int)(Math.random()*scale));
			} catch (InterruptedException e) { }
			value = buff.get();
		} //Αντιπροσωπεύει τον χρόνο που χρειάζεται ο καταναλωτής για να «επεξεργαστεί» ή 
		// να «καταναλώσει» το στοιχείο που μόλις πήρε από τον buffer 
	}
}

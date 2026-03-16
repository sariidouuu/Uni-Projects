public class Producer extends Thread {
	private Buffer buff;
	private int reps;
	private int scale;

	// Constructor
	public Producer(Buffer b, int r, int s) {
		this.buff = b;
		this.reps = r;
		this.scale = s;
	}

	// Producer runs for reps times with random(scale) intervals
	public void run() {
		for(int i = 0; i < reps; i++) {
			buff.put(i);
			try {
				sleep((int)(Math.random()*scale));
			} catch (InterruptedException e) { }
		} //Προκαλεί μια τυχαία καθυστέρηση στην εκτέλεση του νήματος, 
		// προσομοιώνοντας τον χρόνο που χρειάζεται ένας παραγωγός για να «ετοιμάσει» το επόμενο προϊόν του.
	}
}

public class Buffer{

	private int contents;
	//Μπορούμε να διαγράψουμε τελείως τα boolean bufferEmpty και BufferFull και να ελέγχουμε τα if και while μόνο με το counter
	//private boolean bufferEmpty = true;
	//private boolean bufferFull = false;
	private int counter = 0;

	// Constructor
	public Buffer() {
		contents = 0;
	}

	// Put an item into buffer
	public synchronized void put(int data){
		while (counter == 1) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		contents = data;
		counter++;
		System.out.println("Item " + data + " added " + ". Count = " + counter);
		System.out.println("The buffer is full");

		notifyAll();
	}

	// Get an item from bufffer
	public synchronized int get(){
		while (counter==0) {
			try { wait(); }
			catch (InterruptedException e) {}
		}

		int data = contents;
		counter--;
		System.out.println("Item " + data + " removed " + ". Count = " + counter);	
		System.out.println("The buffer is empty");

		notifyAll();
		return data;
	}
}
import java.util.concurrent.Semaphore;

public class Buffer{

	private int contents;
	private int counter = 0;
	private Semaphore mutex = new Semaphore(1);
	private Semaphore bufferFull = new Semaphore(0); //itemToGet(0) διότι ξεκινάμε με άδειο buffer
	private Semaphore bufferEmpty = new Semaphore(1); //itemsToPut

	// Constructor
	public Buffer() {
		contents = 0;
	}

	// Put an item into buffer
	public void put(int data) {
		try {
			bufferEmpty.acquire(); //P(itemsToPut);
		} catch (InterruptedException e) { }
		try {
			mutex.acquire(); //P(bufferMutex);
		} catch (InterruptedException e) { }
		contents = data;
		counter++;
		System.out.println("Prod " + Thread.currentThread().getName() + " No "+ data + " Count = " + counter);
		
		System.out.println("The buffer is full");
		
		mutex.release();  //V(bufferMutex);
		bufferFull.release(); //V(itemsToGet);
	}

	// Get an item from bufffer
	public int get() {
		int data = 0;
		try {
			bufferFull.acquire(); //P(itemsToGet);
		} catch (InterruptedException e) { }
		try {
			mutex.acquire(); //P(bufferMutex);
		} catch (InterruptedException e) { }

		data = contents;
		System.out.println("  Cons " + Thread.currentThread().getName() + " No "+ data + " Count = " + (counter-1));
		counter--;	
		//Δεν χρειάζεται if (counter == 0) διότι ο buffer έχει ένα slot
		System.out.println("The buffer is empty");	
		
		mutex.release();  //V(bufferMutex);		
		bufferEmpty.release();  //V(itemsToPut);
		return data;
	}
}
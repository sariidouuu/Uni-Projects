import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Buffer{
	//private int[] contents; private int size; private int front, back; Δεν χρειάζονται
	private int contents;
	private int counter = 0;
	private Lock lock = new ReentrantLock();
	private Condition bufferFull = lock.newCondition();
	private Condition bufferEmpty = lock.newCondition();

	// Constructor
	public Buffer() {
		contents = 0;
	}

	// Put an item into buffer
	public void put(int data) {
		lock.lock();
			try {
				while (counter == 1) {
					System.out.println("The buffer is full");
					try {
						bufferFull.await();
					} catch (InterruptedException e) { }
				}
				contents = data;
				counter++;
				System.out.println("Prod " + Thread.currentThread().getName() + " No "+ data + " Count = " + counter);
				//buffer was empty
				bufferEmpty.signalAll();
				// Δεν χρειάζεται να γράψω if (counter == 1) bufferEmpty.signalAll(); αφού με το ένα data που πρόσθεσα ο buffer γέμισε
		} finally {
			lock.unlock();
		}
	}

	// Get an item from bufffer
	public int get() {
		int data = 0;

		lock.lock();
		try {
			while (counter == 0) {
				System.out.println("The buffer is empty");
				try {
					bufferEmpty.await();
				} catch (InterruptedException e) { }
			}
			data = contents;
			System.out.println("  Cons " + Thread.currentThread().getName() + " No "+ data + " Count = " + (counter-1));
			counter--;
			//buffer was full
			//Αντίστοιχα δεν χρειάζομαι ούτε εδώ την if (counter == 0) bufferFull.signalAll();
			bufferFull.signalAll();
		} finally {
			lock.unlock() ;
		}
		return data;
	}
}
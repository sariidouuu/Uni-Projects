import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Buffer{
	private int[] contents;
	private int size;
	private int front, back;
	private int counter = 0;
	private Lock lock = new ReentrantLock();
	private Condition bufferFull = lock.newCondition();
	private Condition bufferEmpty = lock.newCondition();

	// Constructor
	public Buffer(int s) {
		this.size = s;
		contents = new int[size];
		for (int i=0; i<size; i++){
			contents[i] = 0;
			this.front = 0;
			this.back = size - 1;
		}	
	}

	// Put an item into buffer
	public void put(int data) {
		lock.lock();
			try {
				while (counter == size) {
					System.out.println("The buffer is full");
					try {
						bufferFull.await();
					} catch (InterruptedException e) { }
				}
				back = (back + 1) % size;
				contents[back] = data;
				counter++;
				System.out.println("Prod " + Thread.currentThread().getName() + " No "+ data + " Loc " + back + " Count = " + counter);
				//buffer was empty
				if (counter == 1) bufferEmpty.signalAll();
				//Αν ο buffer ήταν άδειος, υπάρχει μεγάλη πιθανότητα κάποιοι καταναλωτές να έχουν σταματήσει την εκτέλεσή τους 
				//και να περιμένουν στην ουρά bufferEmpty.await(). Οπότε τους ξυπνάει.
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
			data = contents[front];
			System.out.println("  Cons " + Thread.currentThread().getName() + " No "+ data + " Loc " + front + " Count = " + (counter-1));
			front = (front + 1) % size;
			counter--;
			//buffer was full
			if (counter == size-1) bufferFull.signalAll();
			//Αν ο Buffer ήταν γεμάτος οι παραγωγοί (αν υπήρχαν ίσως να) είχαν σταματήσει την εκτέλεσή τους, 
			//Τώρα αφού έχω αφαιρέσει ένα στοιχείο, οι παραγωγοί μπορούν να γράψουν.
			//Οπότε ξυπνάω την bufferFull.
		} finally {
			lock.unlock() ;
		}
		return data;
	}
}
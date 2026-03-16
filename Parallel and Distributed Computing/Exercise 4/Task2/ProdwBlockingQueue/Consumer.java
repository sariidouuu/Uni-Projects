import java.util.concurrent.BlockingQueue;

public class Consumer extends Thread {
	private int scale;
	private BlockingQueue<Message> q;

	// Constructor
	public Consumer(BlockingQueue<Message> q, int s) {
		this.scale = s;
		this.q = q;
	}

	// Producer runs for reps times with random(scale) intervals
	public void run() {
		int value;
		while (true) { 
			try {
				// Η take() περιμένει αυτόματα αν η ουρά είναι άδεια
				Message m = q.take();
				System.out.println("Consumed: " + m.getMsg() + " | consumed by thread: " + Thread.currentThread().getName() );

				sleep((int)(Math.random()*scale));
			} catch (InterruptedException e) { }
		}
	}
}

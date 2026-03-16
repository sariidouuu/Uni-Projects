import java.util.concurrent.BlockingQueue;

public class Producer extends Thread {
	private int reps;
	private int scale;
	private BlockingQueue<Message> q;

	// Constructor
	public Producer(BlockingQueue<Message> q , int r, int s) {
		this.reps = r;
		this.scale = s;
		this.q = q;
	}

	// Producer runs for reps times with random(scale) intervals
	public void run() {
		for(int i = 0; i < reps; i++) {
			try {
				// Η put() της BlockingQueue περιμένει αυτόματα αν η ουρά είναι γεμάτη
				Message m = new Message("Item: "+ i + " produced by thread: " + Thread.currentThread().getName());
				System.out.println(m.getMsg());
				q.put(m);

				// Αν τα βάλω με αυτή τη σειρά:
				// Message m = new Message("Item: "+ i + " produced by thread: " + Thread.currentThread().getName());
				// q.put(m);
				// System.out.println(m.getMsg());
				
				//τότε υπάρχει το ενδεχόμενο να εκτελεστεί ως εξής:
				//Producer: q.put(item6) <- item6 μπαίνει στην ουρά
				//Consumer: q.take() <- παίρνει αμέσως το item6
				//Consumer: println("Consumed item6") <- τυπώνει πρώτος
				//Producer: println("item6") <- τυπώνει μετά
				
				sleep((int)(Math.random()*scale));
			} catch (InterruptedException e) { }
		}
	}
}
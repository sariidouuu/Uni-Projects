import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer{
	public static void main(String[] args){
		//Διαγράφω σχεδόν τα πάντα
		int bufferSize = 10;
		int noIterations = 20;  
		int producerDelay = 100;
		int consumerDelay = 1;
		// Με consumer delay = 1 το πρόγραμμα φαίνεται εκτελείται σειριακά, δηλαδή:
		// item 0 produced, imer 0 consumed, item 1 produced, imer 1 consumed κλπ..
		// Με consumer delay = 500 ο producer προλαβαίναι να γράψει περισσότερες θέσεις

		//Δημιουργία ουράς
		BlockingQueue<Message> queue = new ArrayBlockingQueue<>(bufferSize);

		Producer prod;
		Consumer cons;

		prod = new Producer(queue, noIterations, producerDelay);
		prod.start();
		cons = new Consumer(queue, consumerDelay);
		cons.start();
	}

}
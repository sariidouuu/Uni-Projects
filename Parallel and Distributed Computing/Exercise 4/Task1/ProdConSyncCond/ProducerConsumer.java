public class ProducerConsumer{
	public static void main(String[] args){
		int noIterations = 20;
		int producerDelay = 100;
		int consumerDelay = 1;

		Producer prod;
		Consumer cons;

		// Bounded Buffer
		Buffer buff = new Buffer();
		
		// Producer threads
		prod = new Producer(buff, noIterations, producerDelay);
		prod.start();

		// Consumer threads
		cons = new Consumer(buff, consumerDelay);
		cons.start();
	}
}
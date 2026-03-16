public class ProducerConsumer{
	public static void main(String[] args){
		int noIterations = 20;
		int producerDelay = 1;
		int consumerDelay = 100;
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
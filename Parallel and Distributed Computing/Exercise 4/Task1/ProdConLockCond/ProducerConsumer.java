public class ProducerConsumer{
	public static void main(String[] args){
		//int bufferSize = 5; Το διαγράφω καθώς έχω single slot buffer
		int noIterations = 20;  
		int producerDelay = 100;
		int consumerDelay = 1;

		// Bounded Buffer
		Buffer buff = new Buffer(); //Και δημιουργώ εναν buffer

		//Διαγράφω τους πίνακες και τα for loops, αφού ζητάμε για ΕΝΑΝ producer και ΕΝΑΝ consumer
		Producer prod;
		Consumer cons;

		prod = new Producer(buff, noIterations, producerDelay);
		prod.start();
		cons = new Consumer(buff, consumerDelay);
		cons.start();
	}
}
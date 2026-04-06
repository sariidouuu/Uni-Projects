public class SieveOfEratosthenes2_2 {
    public static void main(String[] args){  
		
		int size = 0;
		int numThreads = 0;

		if (args.length != 2) {
			System.out.println("Usage: java SieveOfEratosthenes <size> <number of threads> ");
			System.exit(1);
		}

		try {
			size = Integer.parseInt(args[0]);
			numThreads = Integer.parseInt(args[1]);
		}
		catch (NumberFormatException nfe) {
			System.out.println("Integer argument expected");
			System.exit(1);
		}
		if (size <= 0) {
			System.out.println("size should be positive integer");
			System.exit(1);
		}
		if(numThreads <= 0) numThreads = Runtime.getRuntime().availableProcessors();

		boolean[] prime = new boolean[size+1];

		for(int i = 2; i <= size; i++)
			prime[i] = true; 		

		// get current time 
		long start = System.currentTimeMillis();

		CyclicWorker[] threads = new CyclicWorker[numThreads];

        int limit = (int)Math.sqrt(size) + 1;

		for(int i=0; i<numThreads; i++){
			// startIndex = i το id του thread
			// step = numThreads το βήμα του κάθε thread
			
			threads[i] = new CyclicWorker(i, numThreads, size, limit, prime);
			threads[i].start();
		}
		
		for(int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // get current time and calculate elapsed time
		long elapsedTimeMillis = System.currentTimeMillis()-start;

		int count = 0;
		for(int i = 2; i <= size; i++) 
			if (prime[i] == true) {
				//System.out.println(i); 
				count++;
			}

		System.out.println("number of primes "+count); 
		System.out.println("time in ms = "+ elapsedTimeMillis);
	}
}

class CyclicWorker extends Thread{
	private int id;
    private int numThreads;
    private int size;
    private int limit;
    private boolean[] prime;

	CyclicWorker(int i, int numThreads, int size, int limit, boolean[] prime){
		this.id = i;
        this.numThreads = numThreads;
		this.size = size;
        this.limit = limit;
        this.prime = prime;
	}

	public void run(){
        // Για p από 2 συν το id του για να βρω την αρχή του κάθε νήματος (από που θα ξεκινήσει)
        // και προχωράω με βήμα +numThreads για κυκλικότητα
		for(int p = 2 + id; p <= limit; p += numThreads){
			if(prime[p]){
				for(int i = p*p; i <= size; i +=p){ //φτάνω μέχρι το τέλος
					prime[i] = false; 
				}
			}
		}
	}
}

//Threads : 4
// Size          |  Time in ms
// 10.000        |  3
// 100.000       |  3
// 1.000.000     |  10
// 10.000.000    |  24
// 100.000.000   |  883
// 1.000.000.000 |  10972
public class SieveOfEratosthenes2_1{
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

		Worker[] threads = new Worker[numThreads];

		int range = size / numThreads;
        int limit = (int)Math.sqrt(size) + 1;

		for(int i=0; i<numThreads; i++){
			int startIndex = 2 + i * range; //ίδιος τύπος με πδφ 5 διαφ 69 Στατική Κατανομή, απλά εδώ ξεκινάω από το 2
			int endIndex = startIndex + range;
			if(i == numThreads-1) endIndex=size;
			
			threads[i] = new Worker(startIndex, endIndex, limit, prime);
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

class Worker extends Thread{
	private int start;
    private int end;
    private int limit;
    private boolean[] prime;

	Worker(int startIndex, int endIndex, int limit, boolean[] prime){
		this.start = startIndex;
        this.end = endIndex;
		this.limit = limit;
        this.prime = prime;
	}

	public void run(){
		//Ελέγχω όλους τους πρώτους μέχρι το limit
		for(int p = 2; p <= limit; p++){
			if(prime[p]){
				//Το κάθε νήμα διαγράφει μόνο στο δικό του κομμάτι
				for(int i = p*p; i < end; i +=p){
					//Βάζω την if για να βεβαιωθώ ότι το νήμα θα ξεκινήσει να διαγράφει από τη δικιά ΤΟΥ αρχή.
					if(i>=start) prime[i] = false; 
				}
			}
		}
	}
}

//Threads : 4
// Size          |  Time in ms  |  Primes
// 10.000        |  1           |  1.229
// 100.000       |  5           |  9.592
// 1.000.000     |  18          |  78.498
// 10.000.000    |  18          |  664.579
// 100.000.000   |  1015        |  5.761.455
// 1.000.000.000 |  10379       |  50.847.534

//Serial Program:
// Size          |  Time in ms
// 10.000        |  0
// 100.000       |  0
// 1.000.000     |  8
// 10.000.000    |  31
// 100.000.000   |  999
// 1.000.000.000 |  12.455

// Για μικρό όγκο έχουν παρόμοια απόδοση
// Για περισσότερο όγκο, η στατική κατανομή έχει καλύτερα αποτελέσματα
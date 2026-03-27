/* Vector Addition a = b + c  */

// time in ms = 12 for size = 1000000
class VectorAdd_5_12{
    public static void main(String args[]){
        int size = 1000000;

        int numThreads = 0;

        if (args.length != 1) {
            System.out.println("Usage: java ThreadParSqrt <number of threads>");
            System.exit(1);
        }

        try {
            numThreads = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException nfe) {
            System.out.println("Integer argument expected");
            System.exit(1);
        }
        if (numThreads == 0) numThreads = Runtime.getRuntime().availableProcessors();

        double[] a = new double[size];
        double[] b = new double[size];
        double[] c = new double[size];

        for(int i = 0; i < size; i++) {
            a[i] = 0.0;
            b[i] = 1.0;
            c[i] = 0.5;
        }

        // get current time 
        long start = System.currentTimeMillis();
        // create threads
        VectorThread threads[] = new VectorThread[numThreads];

        for(int i = 0; i < numThreads; i++) {
            threads[i] = new VectorThread(i, numThreads, a, b, c, size);
            threads[i].start();
        }
        for(int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {}
        }
        // get current time and calculate elapsed time
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        System.out.println("time in ms = "+ elapsedTimeMillis);
    }
}

class VectorThread extends Thread{
    private double [] a,b,c;
	private int start;
	private int stop;
    private int size;

    public VectorThread(int myId, int numThreads, double[] a, double[] b, double[] c, int size){
		this.a = a;
        this.b = b;
        this.c = c;
        this.size = size;
		start = myId * (size / numThreads);
		stop = start + (size / numThreads);
		if (myId == (numThreads - 1)) stop = size;
	}

    public void run(){
		for(int i = start; i < stop; i++)  //Κάθε νήμα έχει τη γραμμή του
                a[i] = b[i] + c[i];
	}
}

        /* for debugging 
        for(int i = 0; i < size; i++) 
            System.out.println(a[i]); */

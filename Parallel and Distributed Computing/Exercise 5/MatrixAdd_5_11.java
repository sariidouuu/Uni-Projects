/* Matrix Addition A = B + C  */

// time in ms = 18 for size = 1000

class MatrixAdd_5_11 {
    public static void main(String args[]) {
        int size = 1000;

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
            
        double[][] a = new double[size][size];
        double[][] b = new double[size][size];
        double[][] c = new double[size][size];

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                a[i][j] = 0.1;
                b[i][j] = 0.3;
                c[i][j] = 0.5;
            }

        // get current time 
        long start = System.currentTimeMillis();
        // create threads
        MatrixThread threads[] = new MatrixThread[numThreads];

        for(int i = 0; i < numThreads; i++) {
            threads[i] = new MatrixThread(i, numThreads, a, b, c, size);
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


class MatrixThread extends Thread{
    private double [][] a,b,c;
	private int myStart;
	private int myStop;
    private int size;

    public MatrixThread(int myId, int numThreads, double[][] a, double[][] b, double[][] c, int size){
		this.a = a;
        this.b = b;
        this.c = c;
        this.size = size;
		myStart = myId * (size / numThreads);
		myStop = myStart + (size / numThreads);
		if (myId == (numThreads - 1)) myStop = size;
	}

    public void run(){
		for(int i = myStart; i < myStop; i++)  //Κάθε νήμα έχει τη γραμμή του
			for (int j = 0; j < size; j++) //Ολόκληρες στήλες
                a[i][j] = b[i][j] + c[i][j];
	}
}


    /*
     * for debugging
     * for(int i = 0; i < size; i++) {
     * for(int j = 0; j < size; j++)
     * System.out.print(a[i][j]+" ");
     * System.out.println();
     * }
     */

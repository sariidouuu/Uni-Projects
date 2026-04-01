//ΛΑΘΟΣ ΛΥΣΗ ME ΑΜΟΙΒΑΙΟ ΑΠΟΚΛΕΙΣΜΟ & ΜΕ ΚΟΙΝΟΧΡΗΣΤΗ SUM

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Ex1_3NumIntSeq {
    public static Lock lock = new ReentrantLock();
    public static void main(String[] args) {

        long numSteps = 1000000;
        int numThreads = 0;

        // parse command line
        if (args.length != 2) {
            System.out.println("arguments:  <number_of_steps> <number_of_threads>");
            System.exit(1);
        }
        try {
            numSteps = Long.parseLong(args[0]);
            numThreads = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Arguments must be integers (long for steps, int for threads)");
            System.exit(1);
        }
        
        if (numThreads == 0) numThreads = Runtime.getRuntime().availableProcessors();

        /* start timing */
        long startTime = System.currentTimeMillis();
        
        double[] tsum = new double[numThreads];
        double step = 1.0 / (double)numSteps;

        NumIntThread[] threads = new NumIntThread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new NumIntThread(i, numThreads, tsum, numSteps, step);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {}
        }

        double sum = 0.0;
        /* do computation */
        for (int i=0; i < numThreads; i++) {
            sum += tsum[i];
        }
        double pi = sum * step;

        /* end timing and print result */
        long endTime = System.currentTimeMillis();
        System.out.printf("parallel program results with %d steps and %d threads\n", numSteps, numThreads);
        System.out.printf("computed pi = %22.20f\n" , pi);
        System.out.printf("difference between estimated pi and Math.PI = %22.20f\n", Math.abs(pi - Math.PI));
        System.out.printf("time to compute = %f seconds\n", (double) (endTime - startTime) / 1000);
    }
}

class NumIntThread extends Thread {
    private double[] sums;
    private double mySum;
    private int myId;
    private long myStart;
    private long myStop;
    private double step;

    public NumIntThread(int id, int numThreads, double[] tsum, long numSteps, double step) {
        this.sums = tsum;
        this.myId = id;
        this.step = step;
        this.mySum = 0.0;
        // Διαμερισμός του εύρους numSteps στα διαθέσιμα νήματα [cite: 6]
        this.myStart = myId * (numSteps / numThreads);
        this.myStop = (myId == numThreads - 1) ? numSteps : myStart + (numSteps / numThreads);
    }

    public void run() {
        for (long i = myStart; i < myStop; i++) {
            double x = ((double)i + 0.5) * step;
            Ex1_3NumIntSeq.lock.lock();
			try {
                sums[myId] = 4.0 / (1.0 + x * x);
            } finally {
				Ex1_3NumIntSeq.lock.unlock();
			}    

        }
    }
}

//FOR PARALLEL with 4 Threads:
// Steps         | Time
// 1.000.000     | 0.037000 seconds
// 10.000.000    | 0.208000 seconds
// 100.000.000   | 1.943000 seconds
// 1.000.000.000 | 25.45600 seconds

// Παρατηρώ τεράστιο overhead για τα 1δισ. steps
// Ο χρόνος αυξάνεται σχεδόν ακριβώς δέκα φορές κάθε φορά που δεκαπλασιάζεις τα βήματα. 
// Αυτό δείχνει ότι το πρόγραμμα συμπεριφέρεται "σειριακά" παρόλο που χρησιμοποιώ 4 νήματα.
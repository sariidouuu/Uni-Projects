//ΣΩΣΤΗ ΛΥΣΗ ΧΩΡΙΣ ΑΜΟΙΒΑΙΟ ΑΠΟΚΛΕΙΣΜΟ & ΜΕ ΚΟΙΝΟΧΡΗΣΤΗ SUM & ΤΟΠΙΚΗ ΜΕΤΑΒΛΗΤΗ ΜΕΣΑ ΣΤΗΝ TASK

public class Ex1_1NumIntSeq {

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
            // Ο τύπος από τον ακολουθιακό κώδικα 
            double x = ((double)i + 0.5) * step;
            mySum += 4.0 / (1.0 + x * x);
        }
        // Αποθήκευση στο κοινόχρηστο array χωρίς lock 
        sums[myId] = mySum;
    }
}

// FOR SEQUENT:
// Steps         | Time
// 1.000.000     | 0.005000 seconds
// 10.000.000    | 0.022000 seconds
// 100.000.000   | 0.194000 seconds
// 1.000.000.000 | 2.251000 seconds

//FOR PARALLEL with 4 Threads:
// Steps         | Time
// 1.000.000     | 0.012000 seconds
// 10.000.000    | 0.016000 seconds
// 100.000.000   | 0.070000 seconds
// 1.000.000.000 | 0.519000 seconds

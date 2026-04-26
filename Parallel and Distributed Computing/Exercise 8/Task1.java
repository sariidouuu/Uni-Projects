// Χρησιμοποίησα τη λογική της SimpleRecursiveHalf.java αφού με αυτή κερδίζεις 
// πόρους και αποδοτικότητα (καλύτερη επεξεργαστική ισχύς)

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Task1 {

    public static void main(String[] args) {

        long numSteps = 10000;
        int limit = 1000;

        if (args.length != 2) {
            System.out.println("Arguments:  <number_of_steps>  <limit>");
            System.exit(1);
        }

        try {
            numSteps = Long.parseLong(args[0]);  // Ο αριθμός των βημάτων
            limit    = Integer.parseInt(args[1]); // Το όριο αποκοπής
        } catch (NumberFormatException nfe) {
            System.out.println("Arguments must be integers");
            System.exit(1);
        }

        double step = 1.0 / (double) numSteps;

        // Δημιουργία ForkJoinPool με τον αριθμό των διαθέσιμων επεξεργαστών
        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

        // start timing
        long startTime = System.currentTimeMillis();

        // Δημιουργία και εκτέλεση της κεντρικής εργασίας
        PiTask task = new PiTask(0, numSteps, limit, step);
        double sum = pool.invoke(task); // invoke() = fork + join

        double pi = sum * step;

        // end timing
        long endTime = System.currentTimeMillis() - startTime;

        System.out.printf("ForkJoin results with %d steps, limit=%d%n", numSteps, limit);
        System.out.printf("computed pi = %22.20f%n", pi);
        System.out.printf("difference  = %22.20f%n", Math.abs(pi - Math.PI));
        System.out.printf("time        = %d ms%n", endTime);
        System.out.printf("parallelism = %d threads%n", pool.getParallelism());

        pool.shutdown();
    }
}

// RecursiveTask<Double> αντί για Runnable — επιστρέφει αποτέλεσμα απευθείας
class PiTask extends RecursiveTask<Double> {

    private final long   myFrom;
    private final long   myTo;
    private final int    myLimit;
    private final double myStep;

    public PiTask(long from, long to, int limit, double step) {
        this.myFrom  = from;
        this.myTo    = to;
        this.myLimit = limit;
        this.myStep  = step;
    }

    @Override
    protected Double compute() {
        long workload = myTo - myFrom;

        // Βασική περίπτωση: υπολογισμός απευθείας
        if (workload <= myLimit) {
            double localSum = 0.0;
            for (long i = myFrom; i < myTo; i++) {
                double x = (i + 0.5) * myStep;
                localSum += 4.0 / (1.0 + x * x);
            }
            return localSum;
        }else{
            // Αναδρομική περίπτωση: διαίρει και βασίλευε
            long mid = myFrom + workload / 2;

            PiTask taskL = new PiTask(myFrom, mid,   myLimit, myStep);
            PiTask taskR = new PiTask(mid,    myTo,  myLimit, myStep);

            // Fork το αριστερό — εκτελείται ασύγχρονα από το ForkJoinPool
            taskL.fork();

            // Εκτελούμε το ένα task στο τρέχον thread (half-threads τεχνική)
            double rightResult = taskR.compute();

            // Join
            double leftResult = taskL.join();
            
            return leftResult + rightResult;
        }
    }
}

// ΣΤΑΤΙΚΗ ΚΑΤΑΝΟΜΗ
//                με 4 threads  με 8 threads
// Steps         | Time in ms   |  Time in ms
// 1.000.000     | 12           |  21
// 10.000.000    | 16           |  18
// 100.000.000   | 70           |  52
// 1.000.000.000 | 519          |  373

//ΔΙΑΡΕΙ & ΒΑΣΙΛΕΥΕ ΜΕ FORK JOIN & 
// 12 available THREADS & Limit: 1.000
// Steps         | Time in ms
// 1.000.000     | 36           
// 10.000.000    | 52
// 100.000.000   | 181
// 1.000.000.000 | 405  

// 12 available & Limit: 10.000
// Steps         | Time 
// 1.000.000     | 31             
// 10.000.000    | 83 
// 100.000.000   | 110
// 1.000.000.000 | 442

// 12 available & Limit: 50.000
// Steps         | Time 
// 1.000.000     | 29             
// 10.000.000    | 56 
// 100.000.000   | 75
// 1.000.000.000 | 406

// Στη λύση με Fork Join, με Limit 50.000 φαίνεται να έχουμε το μικρότερο overhead συνολικά. 
// Η στατική κατανομή ωστόσο φαίνεται να έχει καλύτερα αποτελέσματα για τον υπολογισμό του Pi
/*
* Χωρίς την δομή δεδομένων, τα νήματα θα δημιουργούνταν μεμονωμένα μέσα στη for, για παράδειγμα ως Thread t = new myThread(I, numThread);,
* και δηλαδή δεν θα χρησιμοποιούσαμε τον πίνακα threads[]. Έτσι δεν θα μπορούσαμε να καλέσουμε την join() μέσα σε (επόμενο) 
* for loop για όλα τα νήματα (πίνακα threads). Αυτό θα είχε ως αποτέλεσμα η main να συνεχίσει την εκτέλεση του παρακάτω κώδικά της, 
* δηλαδή για τα συγκεκριμένα αρχεία να τερματίσει. 
*
* Θα μπορούσαμε να έχουμε join() για κάθε νήμα μεμονωμένα μέσα στο πρώτο for loop (όπου δημιουργούμε τα νήματα χωρίς δομή δεδομένων). 
* Με αυτόν τον τρόπο όμως θα μετατρέπαμε το πρόγραμμα σε σειριακό, δηλαδή θα είχαμε τη δημιουργία του thread 0 (create and start thread 0), 
* και τις εκτυπώσεις Hello from thread 0 out of 20, ακολουθούμενη από Thread 0 exists. 
* Αφού τελειώσει η εκτέλεση του νήματος 0, το πρόγραμμα θα δημιουργήσει το νήμα 1 κλπ... 
 */
public class Task1 {

    public static void main(String[] args) {
        int numThreads = 20;
        Thread thread;

        /* create and start threads */
        for (int i = 0; i < numThreads; ++i) {
            System.out.println("In main: create and start thread " + i);
            thread = new MyThread(i, numThreads);
            thread.start();

            //Βάζουμε join() για κάθε νήμα μεμονωμένα.
            try {
                thread.join();
            }catch (InterruptedException e) {
                System.err.println("this should not happen");
            }
        }
        System.out.println("In main: threads all done");
    }
}

/* class containing code for each thread to execute */
class MyThread extends Thread {

    /* instance variables */
    private int myID;
    private int totalThreads;

    /* constructor */
    public MyThread(int myID, int totalThreads) {
        this.myID = myID;
        this.totalThreads = totalThreads;
    }

    /* thread code */
    public void run() {
        System.out.println("Hello from thread " + myID + " out of " + totalThreads);
        System.out.println("Thread " + myID + " exits");
        
    } 

}


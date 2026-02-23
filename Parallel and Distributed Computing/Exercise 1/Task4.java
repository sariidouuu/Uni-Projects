/*
* Τα νήματα εκτυπώνουν ταυτόχρονα και είναι ανεξάρτητα μεταξύ τους. Συνεπώς τα αποτελέσματα μπερδεύονται μεταξύ τους. 
* Eμφανίζονται γινόμενα ενός νήματος πριν τελειώσει ένα προηγούμενο νήμα.
*
* Ωστόσο, παρατηρούμε πως παρόλο που τα αποτελέσματα των νημάτων είναι μπερδεμένα μεταξύ τους, 
* τα αποτελέσματα του κάθε νήματος εμφανίζονται σε σειρά. Δηλαδή, πρώτα θα τυπωθεί 1 * 3 = 3, μετά 2 * 3 = 6, 3 * 3 = 9, κλπ... 
*
* Αν απομονώσουμε τις εκτυπώσεις ενός νήματος, θα δούμε ότι εκτυπώνονται τα πρώτα 20 πολλαπλάσια του 1, μετά τα πρώτα 20 πολλαπλάσια του 2 κλπ...
* Για να υλοποιηθεί αυτή η ακολουθιακή εκτέλεση των νημάτων, θα πρέπει να χρησιμοποιηθεί η μέθοδος join() μέσα στο ίδιο for loop που δημιουργούνται τα νήματα.
*/
public class Task4 {

    public static void main(String[] args) {

        /* allocate array of thread objecst */
        int numThreads = 10;
        Thread[] threads = new Thread[numThreads];

        /* create and start threads */
        for (int i = 0; i < numThreads; ++i) {
            threads[i] = new MyThread(i);
            threads[i].start();
            
        }
        /* wait for threads to finish */
        for (int i = 0; i < numThreads; ++i) {
            try {
                threads[i].join();
            }
            catch (InterruptedException e) {
                System.err.println("this should not happen");
            }
        }

        System.out.println("In main: threads all done");
    }
}

/* class containing code for each thread to execute */
class MyThread extends Thread {
    private int myID;

    /* constructor */
    public MyThread(int myID) {
        this.myID = myID;
    }

    /* thread code */
    public void run() {
        for(int i=1; i<=20; i++) {
            System.out.println(i + "*" + (myID+1) + "=" + i*(myID+1));
        }
    } 
}


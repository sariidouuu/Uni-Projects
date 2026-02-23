/*
 * Δημιουργούνται δύο κλάσεις (TypeA και TypeB) που κληρονομούν την κλάση PolymorphicThread.
 * Δημιουργούμε δύο πίνακες, τον aThreads και τον bThreads, που περιέχουν numThreads=10 αντικείμενα τύπου TypeA και TypeB αντίστοιχα.
 * Τα νήματα εκτελούνται παράλληλα και εμφανίζουν τον αριθμό του thread, τον τύπο του και τον συνολικό αριθμό των threads (για τα TypeB).
 * Δεν εξασφαλίζεται η σειρά εκτέλεσης των threads, δεν γνωρίζουμε με ποιά σειρά θα τελειώσουν.
*/
public class Task3 {
    public static void main(String[] args) {

        int numThreads = 10;
        PolymorphicThread[] aThreads = new TypeA[numThreads];
        PolymorphicThread[] bThreads = new TypeB[numThreads];

        for(int i = 0; i<numThreads; i++){
            System.out.println("In main: create and start thread " + i + "type A");
            aThreads[i] = new TypeA(i);
            aThreads[i].start();

            System.out.println("In main: create and start thread " + i + "type B");
            bThreads[i] = new TypeB(i,numThreads);
            bThreads[i].start();
        }

        for (int i = 0; i < numThreads; ++i) {
            try {
                aThreads[i].join();
                bThreads[i].join();
            }
            catch (InterruptedException e) {
                System.err.println("this should not happen");
            }
        }
        System.out.println("In main: threads are done");
    }
}

abstract class PolymorphicThread extends Thread{
    protected int myID;

    public PolymorphicThread(int myID) {
        this.myID = myID;
    }
    public abstract void run();
}

class TypeA extends PolymorphicThread {
    //constructor
    public TypeA(int myID) {
        super(myID); //Κληρονομικότητα: καλώ τον constructor της κλάσης PolymorphicThread για να αρχικοποιήσω το myID.
    }
    @Override
    public void run() {
        System.out.println("Hello from thread " + myID + ". My type is A!");
        System.out.println("Thread " + myID + " exits");
    }
}

class TypeB extends PolymorphicThread {
    private int total;
    
    //constructor
    public TypeB(int myID, int totalThreads) {
        super(myID);
        total = totalThreads;
    }
    @Override
    public void run() {
        System.out.println("Hello from thread " + myID + " out of " + total + ". My type is B!");
        System.out.println("Thread " + myID + " exits");
    }
}
/*
 * Δημιουργούνται δύο κλάσεις (TypeA και TypeB) που κληρονομούν την κλάση PolymorphicThread.
 * Η κλάση TypeA έχει μία παράμετρο, την myID και η run() της εμφανίζει τον αριθμό του thread (myID) και τον τύπο της (A).
 * Η κλάση TypeB έχει δύο παραμέτρους, την myID και totalThreads και η run() της εμφανίζει τον αριθμό του thread (myID), 
 * τον τύπο της (B) και τον συνολικό αριθμό των threads.
*/
public class Task2 {
    public static void main(String[] args) {
        System.out.println("In main: create and start two threads");

        PolymorphicThread aThread = new TypeA(0);
        aThread.start();

        PolymorphicThread bThread = new TypeB(1, 2);
        bThread.start();

        try {
            aThread.join();
            bThread.join();
        }catch (InterruptedException e){
                System.err.println("this should not happen");
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
    //Προσθέτουμε μία παράμετρο για τον συνολικό αριθμό των threads. 
    //Έτσι διαφοροποιούμε τον constructor της TypeB από αυτόν της TypeA, καθώς η TypeA δεν έχει αυτή την παράμετρο.
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
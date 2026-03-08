import java.util.concurrent.TimeUnit;

class Philosopher extends Thread {
    private int identity;
    private Fork left;
    private Fork right;
    private int scale;
    private int next;

    // Για τον έλεγχο
    private boolean gotLeft, gotRight;

    Philosopher(int id, int n, int s, Fork l, Fork r) {
        identity = id; next = n; scale = s; left = l; right = r; 
    }

    public void run() {
        // Με while(true) ο κώδικας έτρεχε επ αορίστον (οι Philosophers τύπωνε ότι έφαγαν)
        // Για να επιβεβαιώσω ότι θα φάνε όλοι οι philosphers από μία φορά απλά άλλαξα τη while σε (!hasEaten) για πιο ξεκάθαρα prints
        boolean hasEaten = false;

        while (!hasEaten) {
            //thinking
            System.out.println(" Philosopher "+ identity + " is thinking");
            delay(scale); 

            //hungry
            System.out.println(" Philosopher "+ identity+ " is trying to get fork " + identity);

            gotRight = right.get(1000, TimeUnit.MILLISECONDS); //100 miliseconds
            if(!gotRight){
                System.out.println(" Philosopher " + identity + " could not get fork " + identity + ", retrying...");
                continue;
            }
            // Αν δεν πήρε το δεξί πιρούνι το Thread λόγω της Thread.currentThread().interrupt(); διακόπτεται
            // TΟ συγκεκριμένο thread που απέτυχε, επιστρέφει στην αρχή της while, σκέφτεται και ξανά προσπαθεί 
            // Αν πήρε το δεξί πιρούνι:
            System.out.println(" Philosopher " + identity + " got fork " + identity + " and is trying to get fork " + next);

            gotLeft = left.get(1000, TimeUnit.MILLISECONDS);
            if (!gotLeft) {
                // Δεν πήρε το αριστερό, άρα πρέπει να αφήσει το δεξί και να ξαναπροσπαθήσει
                System.out.println(" Philosopher " + identity + " could not get fork " + next + ", releasing fork " + identity);
                right.put();
                continue; // Ίδια λογική, επιστρέφει το thread σρην αρχή της while
            }

            //eating
            System.out.println(" Philosopher "+ identity + " is eating");
            hasEaten = true;
            
            System.out.println(" Philosopher "+ identity + " is releasing left fork " + next);
            //delay(scale);
            left.put();
            System.out.println(" Philosopher "+ identity + " is releasing fork " + identity);
            //delay(scale);
            right.put();
        }    
    }

    public void delay(int scale) {
        try {
            sleep((int)(Math.random()*scale));
        } catch (InterruptedException e) { }
    }
}
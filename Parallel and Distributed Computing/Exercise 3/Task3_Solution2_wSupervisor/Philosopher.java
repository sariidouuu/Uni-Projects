class Philosopher extends Thread {
    private int identity;
    private Fork left;
    private Fork right;
    private int scale;
    private int next;
    private Supervisor s;

    Philosopher(int id, int sl, Fork l, Fork r, Supervisor s) {
        identity = id; 
        //next = n; 
        scale = sl; 
        left = l; 
        right = r; 
        this.s = s;
    }

    public void run() {
        // Για να επιβεβαιώσω ότι θα φάνε όλοι οι philosphers από μία φορά απλά άλλαξα τη while σε (!hasEaten) για πιο ξεκάθαρα prints
        boolean hasEaten = false;
        while (!hasEaten) {

            //thinking
            System.out.println(" Philosopher "+ identity + " is thinking");
            delay(scale);

            // Request permission from the moderator
            System.out.println("Philosopher " + identity + " requests permission to eat");
            s.getPermission(identity, left, right);

            // Eating
            System.out.println("Philosopher " + identity + " is eating");
            delay(scale);
            hasEaten = true;

            // Release permission
            System.out.println("Philosopher " + identity + " is releasing both forks");
            s.releasePermission(identity, left, right);
        }    
    }

    public void delay(int scale) {
        try {
            sleep((int)(Math.random()*scale));
        } catch (InterruptedException e) { }
    }
}
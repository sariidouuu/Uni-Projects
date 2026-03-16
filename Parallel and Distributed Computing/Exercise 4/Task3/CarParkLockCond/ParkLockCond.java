import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ParkLockCond{

	private int capacity;
	private int spaces;
	private int waitscale;
	private int inscale;

    private Lock lock = new ReentrantLock();
	private Condition parkFull = lock.newCondition();
	private Condition parkEmpty = lock.newCondition();
    
    public ParkLockCond(int c) {
        capacity = c;
        spaces = capacity;
        waitscale = 10;
        inscale = 5;
    }

	public void arrive () {
		//Car arrival with radom delay
        try {
            Thread.sleep((int)(Math.random()*waitscale));
        } catch (InterruptedException e) { }
        System.out.println(Thread.currentThread().getName() + " has arrived");

        lock.lock();
			try {
                while(spaces == 0){
                    System.out.println("The parking is full");
                    try {
						parkFull.await();
					} catch (InterruptedException e) { }
                }
                
                //Car entering
                System.out.println(Thread.currentThread().getName()+" parking");
                //Decrement capacity
                spaces--;
                System.out.println("free "+ spaces);

                // Αν ήταν άδειο το parking (δηλαδή spaces == capacity, όλες οι θέσεις ήταν άδειες)
                // Πρέπει να ξυπνήσουμε την parkEmpty ότι μπορεί να τρέξει
                //Αφού προσθέσαμε ένα αυτοκίνητο: spaces = capacity-1
                if (spaces == capacity-1) parkEmpty.signalAll();
        }finally {
            lock.unlock();
        }
	}
        
    public void depart () {
        lock.lock();
		try {
			while (spaces == capacity){
                System.out.println("The parking is empty");
                try {
					parkEmpty.await();
				} catch (InterruptedException e) { }
            }
            //Car departure
            System.out.println(Thread.currentThread().getName()+" departure");
            //Increment capacity
            spaces++;
            System.out.println("free "+ spaces);

            if(spaces == 1) parkFull.signalAll();
        }finally {
			lock.unlock();
		}
    }            

    public void park() {    
        try {
                Thread.sleep((int)(Math.random()*inscale));
            } catch (InterruptedException e) { }
    }
}

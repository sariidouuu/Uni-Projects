import java.util.concurrent.Semaphore;

public class ParkSem{

	private int capacity;
	private int spaces;
	private int waitscale;
	private int inscale;

    private Semaphore mutex = new Semaphore(1);
	private Semaphore parkFull = new Semaphore(0); //itemToGet(0) διότι ξεκινάμε με άδειο buffer
	private Semaphore parkEmpty; 
    
    public ParkSem(int c) {
        capacity = c;
        spaces = capacity;
        waitscale = 10;
        inscale = 5;

        this.parkEmpty = new Semaphore(capacity);
    }

	void arrive () {
        //Car arrival with radom delay
		try {
            Thread.sleep((int)(Math.random()*waitscale));
		} catch (InterruptedException e) { }
		System.out.println(Thread.currentThread().getName()+" arrival");
        
        try {
			parkEmpty.acquire(); //P(itemsToPut);
		} catch (InterruptedException e) { }
		try {
			mutex.acquire(); //P(bufferMutex);
		} catch (InterruptedException e) { }

        //Car entering
		System.out.println(Thread.currentThread().getName()+" parking");
		//Decrement capacity
		spaces--;
		System.out.println("free "+ spaces);
		
		mutex.release();  //V(bufferMutex);
		parkFull.release(); //V(itemsToGet);
	}
        
    void depart () {
        try {
			parkFull.acquire(); //P(itemsToGet);
		} catch (InterruptedException e) { }
		try {
			mutex.acquire(); //P(bufferMutex);
		} catch (InterruptedException e) { }
        
        //Car departure
        System.out.println(Thread.currentThread().getName()+" departure");
		spaces++;	
        System.out.println("free "+ spaces);

		mutex.release();  //V(bufferMutex);		
		parkEmpty.release();  //V(itemsToPut);
    }            

    void park() {    
        try {
                Thread.sleep((int)(Math.random()*inscale));
            } catch (InterruptedException e) { }
    }
}
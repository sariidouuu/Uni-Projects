
public class ParkMon{

	private int capacity;
	private int spaces;
	private int waitscale;
	private int inscale;

    private boolean parkEmpty = true;
	private boolean parkFull = false;
    
    public ParkMon(int c) {
        capacity = c;
        spaces = capacity;
        waitscale = 10;
        inscale = 5;
    }

	public synchronized void arrive () {
		//Car arrival with radom delay
		try {
        Thread.sleep((int)(Math.random()*waitscale));
		} catch (InterruptedException e) { }
		System.out.println(Thread.currentThread().getName()+" arrival");
		
        while (parkFull) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
        
        //Car entering
		System.out.println(Thread.currentThread().getName() + " parking");
		//Decrement capacity
		spaces--;
		System.out.println("free "+ spaces);

        parkEmpty = false;
        if(spaces == 0){
            parkFull = true;
            System.out.println("The park is full");
        }
        //park was empty
        if(spaces == capacity -1) notifyAll();
	}
        
    public synchronized void depart () {

        while (parkEmpty) {
			try { wait(); } catch (InterruptedException e) {}
		}

        //Car departure
        System.out.println(Thread.currentThread().getName() + " departure");
        //Increment capacity
        spaces++;
        System.out.println("free "+ spaces);

        if (spaces == capacity){
			parkEmpty = true;
			System.out.println("The park is empty");
		}

        //park was full
		if (spaces == 1) notifyAll();
    }           

    public synchronized void park() {    
        try {
                Thread.sleep((int)(Math.random()*inscale));
            } catch (InterruptedException e) { }
    }
}
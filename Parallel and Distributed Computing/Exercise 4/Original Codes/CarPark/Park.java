
public class Park{

	private int capacity;
	private int spaces;
	private int waitscale;
	private int inscale;
    
    public Park(int c) {
       capacity = c;
       spaces = capacity;
       waitscale = 10;
       inscale = 5;
    }
       
	void arrive () {
		//Car arrival with radom delay
		try {
		   Thread.sleep((int)(Math.random()*waitscale));
		} catch (InterruptedException e) { }
		System.out.println(Thread.currentThread().getName()+" arrival");
		//Car entering
		System.out.println(Thread.currentThread().getName()+"     parking");
		//Decrement capacity
		spaces--;
		System.out.println("free "+ spaces);
	}
        
    void depart () {
        //Car departure
        System.out.println(Thread.currentThread().getName()+"         departure");
        //Increment capacity
        spaces++;
        System.out.println("free "+ spaces);
    }            
           
    void park() {    
        try {
                Thread.sleep((int)(Math.random()*inscale));
            } catch (InterruptedException e) { }
    }
}

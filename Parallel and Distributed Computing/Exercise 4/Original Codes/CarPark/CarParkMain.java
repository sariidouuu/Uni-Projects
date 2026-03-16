public class CarParkMain
{
	public static void main(String[] args) {
	    int capacity = 4;
		Park x = new Park(capacity);
		int cars = 20;
		Car[] p = new Car[cars];

		for (int i=0; i<cars; i++) {
			p[i] = new Car(x);
			p[i].start(); 
		}
		for (int i=0; i<cars; i++) 
			try { 
				 p[i].join(); 
			} catch (InterruptedException e) { }
    }
}

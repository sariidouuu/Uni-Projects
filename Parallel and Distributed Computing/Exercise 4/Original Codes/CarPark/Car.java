class Car extends Thread
{
	private Park a_park;

	
	public Car (Park a) {
		a_park = a;
	}

	public void run() {
		for (int i = 0; i < 100; i++) {
			 a_park.arrive();
			 a_park.park();
			 a_park.depart();
		}        
	}

}

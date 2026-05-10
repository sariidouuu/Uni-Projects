public class Count {
	private static int anum;
	
	public Count () {
		anum = 0;
	}

	public synchronized void increment() {
	//public void increment() {
		int temp;
    	//System.out.println(this.print());
		temp = anum;
		temp = temp + 1;
		anum = temp;
		//System.out.println(this.print());
    }
	
    public synchronized String print () {
    //public String print () {
		return String.valueOf(anum);
    }	    
}

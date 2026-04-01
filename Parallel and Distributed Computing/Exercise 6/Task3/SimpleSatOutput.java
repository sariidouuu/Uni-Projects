import java.lang.Math;
import java.util.LinkedList;

import java.util.concurrent.ConcurrentLinkedQueue;
//Αντικατάσταση της LinkedList με την ConcurrentLinkedQueue για thread-safe αποθήκευση

class SimpleSatOutput {
	
	public static void main(String[] args) {  
        int size = 0;

        if (args.length != 2) {
            System.out.println("Usage: java SimpleSat <vector size> <number of threads>");
            System.exit(1);
        }

        try {
            size = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException nfe) {
            System.out.println("Integer argument expected");
            System.exit(1);
        }
		if (size <= 0) {
			System.out.println("size should be positive integer");
			System.exit(1);
        }
        int iterations = (int) Math.pow(2, size);
        
        int numThreads = Integer.parseInt(args[1]); // Είναι το <number of threads> που δίνει στην είσοδο ο χρήστης
        if (numThreads == 0) numThreads = Runtime.getRuntime().availableProcessors();

        LinkedList<String>[] lists = new LinkedList[numThreads];
        for(int i=0; i<numThreads; i++){
            lists[i] = new LinkedList<>();
        }

        //Χρησιμοποιούμε ConcurrentLinkedQueue για thread-safe αποθήκευση
        //Δεν το χρειάζομαι, το διαγράφω
        //ConcurrentLinkedQueue<String> output = new ConcurrentLinkedQueue<>();
        
        // Χρειάζομαι όμως μια Linked list για την αναγωγή:
        LinkedList<String> finalOutput = new LinkedList<>();

        CircuitThread[] threads = new CircuitThread[numThreads];
        
        long startTime = System.currentTimeMillis();

        int block = iterations / numThreads;
        int start = 0;
        int end = 0;
        for (int i = 0; i < numThreads; i++) {
            start = i * block;
            end = start + block;
            if (i == (numThreads - 1))
                end = iterations;
            //threads[i] = new CircuitThread(start, end, size, output);
            threads[i] = new CircuitThread(start, end, size, lists[i]);
            // Starting threads
            threads[i].start();
        }

        // Join threads
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
                finalOutput.addAll(lists[i]);
            } catch (InterruptedException e) {}
        }

        long elapsedTimeMillis = System.currentTimeMillis()-startTime;
        
        System.out.println(finalOutput); 
        
        System.out.println ("All done\n");
        System.out.println("time in ms = "+ elapsedTimeMillis);
        
    } 
}

class CircuitThread extends Thread{
    private int start, end;
    private int size;
    //private ConcurrentLinkedQueue<String> output;
    private LinkedList<String> myList;

    public CircuitThread(int start, int end, int size, LinkedList<String> myList) {
        this.start = start;
        this.end = end;
        this.size = size;
        this.myList = myList;
    }

    public void run() {
        for (int i = start; i < end; i++)
            check_circuit(i, size, myList);
    }

    static void check_circuit (int z, int size, LinkedList<String> myListOutput) {
        
		boolean[] v = new boolean[size];  /* Each element is a bit of z */
    
		for (int i = size-1; i >= 0; i--) 
			v[i] = (z & (1 << i)) != 0;
    
        boolean value = 
            (  v[0]  ||  v[1]  )
        && ( !v[1]  || !v[3]  )
        && (  v[2]  ||  v[3]  )
        && ( !v[3]  || !v[4]  )
        && (  v[4]  || !v[5]  )
        && (  v[5]  || !v[6]  )
        && (  v[5]  ||  v[6]  )
        && (  v[6]  || !v[15] )
        && (  v[7]  || !v[8]  )
        && ( !v[7]  || !v[13] )
        && (  v[8]  ||  v[9]  )
        && (  v[8]  || !v[9]  )
        && ( !v[9]  || !v[10] )
        && (  v[9]  ||  v[11] )
        && (  v[10] ||  v[11] )
        && (  v[12] ||  v[13] )
        && (  v[13] || !v[14] )
        && (  v[14] ||  v[15] )
        && (  v[14] ||  v[16] )
        && (  v[17] ||  v[1]  )
        && (  v[18] || !v[0]  )
        && (  v[19] ||  v[1]  )
        && (  v[19] || !v[18] )
        && ( !v[19] || !v[9]  )
        && (  v[0]  ||  v[17] )
        && ( !v[1]  ||  v[20] )
        && ( !v[21] ||  v[20] )
        && ( !v[22] ||  v[20] )
        && ( !v[21] || !v[20] )
        && (  v[22] || !v[20] );
        
        
        if (value) {
			saveResult(v, size, z, myListOutput);
		}	
    }
    
    static void saveResult (boolean[] v, int size, int z, LinkedList<String> myListOutput) {
		
		String result = null;
		result = String.valueOf(z);

		for (int i=0; i< size; i++)
			if (v[i]) result += " 1";
			else result += " 0";
		
		//Just print result	for debugging
		//System.out.println(result);
		//Save result
		myListOutput.add("\n"+result);
	}
}

/*
RESULTS TABLE
Vector Size | 4 threads
------------------------
23          | 96ms
24          | 149ms
25          | 274ms
26          | 587ms
*/
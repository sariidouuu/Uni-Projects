import java.lang.Math;
import java.util.LinkedList;

class SimpleSatOutput {
	
	public static void main(String[] args) {  
        
        int size = 0;
        if (args.length != 1) {
            System.out.println("Usage: java SimpleSat <vector size>");
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
        
        // Saves Results but occupies large space
        LinkedList<String> output = new LinkedList<String>();
        
        long start = System.currentTimeMillis();
                
        for (int i = 0; i < iterations; i++)
            check_circuit (i, size, output);
           
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        
        System.out.println(output); 
        
        System.out.println ("All done\n");
        System.out.println("time in ms = "+ elapsedTimeMillis);
        
    }
        
    static void check_circuit (int z, int size, LinkedList<String> output) {
        
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
			saveResult(v, size, z, output);
		}	
    }
    
    static void saveResult (boolean[] v, int size, int z, LinkedList<String> output) {
		
		String result = null;
		result = String.valueOf(z);

		for (int i=0; i< size; i++)
			if (v[i]) result += " 1";
			else result += " 0";
		
		//Just print result	for debugging
		//System.out.println(result);
		//Save result
		output.add("\n"+result);
	}	
    
}

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class WordCount1_1 {
    public static void main(String args[]) throws FileNotFoundException, IOException {
    
        if (args.length != 2) {
			System.out.println("WordCount  <file name> <number of threads>");
			System.exit(1);
        }
        
        String fileString = new String(Files.readAllBytes(Paths.get(args[0])));//, StandardCharsets.UTF_8);
        String[] words = fileString.split("[ \n\t\r.,;:!?(){}]");    
        
        int numOfThreads = Integer.parseInt(args[1]);
        if(numOfThreads <= 0) {
            numOfThreads = Runtime.getRuntime().availableProcessors();
        }
        
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        // get current time
		long start = System.currentTimeMillis();

        int blockSize = words.length / numOfThreads;
        Worker[] workers = new Worker[numOfThreads];

        for (int i = 0; i < numOfThreads; i++) {
            int startIdx = i * blockSize;
            int endIdx = startIdx + blockSize;
            if (i == numOfThreads - 1) endIdx = words.length;

            workers[i] = new Worker(words, startIdx, endIdx, map);
            workers[i].start();
        }

        for (int i = 0; i < numOfThreads; i++) {
            try {
                workers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        TreeMap<String, Integer> sortedmap = new TreeMap<String, Integer>(map);
        //HashMap<String, Integer> map = new HashMap<String, Integer>();

        for (String name: sortedmap.keySet()) {
			String key = name.toString();
			String value = sortedmap.get(name).toString();
            System.out.println(key + "\t " + value);
        }

        // get current time and calculate elapsed time
		long elapsedTimeMillis = System.currentTimeMillis()-start;
		System.out.println("time in ms = "+ elapsedTimeMillis);
    }
}

class Worker extends Thread {
    private String words[];
    private int start;
    private int end;
    private ConcurrentHashMap<String, Integer> map;

    public Worker(String[] words, int start, int end, ConcurrentHashMap<String, Integer> map) {
        this.words = words;
        this.start = start;
        this.end = end;
        this.map = map;
    }

    public void run() {
        for (int counter = start; counter < end; counter++) {
            String key = words[counter].toLowerCase();
            if (key.length() > 0) {
                if (map.get(key) == null) {
                    map.put(key, 1);
                }
                else {
                    int value = map.get(key).intValue();
                    value++;
                    map.put(key, value);
                }
            }
        }
    }
}
// Αρχική λύση: 
// bible.txt : 733ms
// bible-fourtimes.txt : 1323ms

// Parallel
// Threads  |  file: bible.txt  | file: bible-fourtimes.txt
// 2        |       694ms       |        894ms
// 4        |       669ms       |        759ms
// 8        |       613ms       |        719ms

// Παρατηρούμε πως στην σειριακή λύση, ο τετραπλασιασμός του αρχείου διπλασιάζει τον χρόνο εκτέλεσης, ενώ
// στην παράλληλη λύση, δεν έχει τόσο μεγάλη διαφορά, ο τετραπλασιασμός δεν έχει σοβαρό αντίκτυπο.
// Ο παράλληλος κώδικας διαχειρίζεται πολύ πιο αποτελεσματικά τον μεγαλύτερο όγκο δεδομένων.

// (για το bible.txt) Παρατηρούμε ότι με 2 threads ο χρόνος είναι ελάχιστα καλύτερος (694ms) από τη σειριακή λύση (733ms). 
// Αυτό συμβαίνει επειδή το κόστος δημιουργίας και διαχείρισης των νημάτων (thread overhead) 
// είναι μεγαλύτερο από το όφελος της παράλληλης επεξεργασίας σε τόσο μικρό όγκο δεδομένων.
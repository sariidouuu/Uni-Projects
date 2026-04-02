import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.*;

public class WordCountStreams1_2 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("bible.txt");
        BufferedReader in = new BufferedReader(new InputStreamReader(fileInputStream));

        //HashMap<Object, Integer> mapper = new HashMap<>();
        ConcurrentHashMap<String, Integer> mapper = new ConcurrentHashMap<>();

        // get current time
        long start = System.currentTimeMillis();

        in.lines()
            .parallel()
            .filter(str -> str.length() > 10)
            .flatMap(str -> Arrays.stream(str.split("\\s+"))) // split με regex για κενά
            .filter(word -> !word.isEmpty())
            .forEach(word -> {
                // Χρήση της merge για thread-safe ενημέρωση της τιμής
                mapper.merge(word, 1, Integer::sum);
        });

        // get current time and calculate elapsed time
        long elapsedTimeMillis = System.currentTimeMillis() - start;

        //Για να δω με πόσα νήματα τρέχει:
        System.out.println("Parallelism level: " + java.util.concurrent.ForkJoinPool.commonPool().getParallelism());

        // Τυπώνει:
        // Parallelism level: 11
        // time in ms = 224

        in.close();
        fileInputStream.close();

        System.out.println("time in ms = " + elapsedTimeMillis);

        /*for(String key : mapper.keySet()) {
            System.out.println(key + ":" + mapper.get(key));
        }*/
    }
}

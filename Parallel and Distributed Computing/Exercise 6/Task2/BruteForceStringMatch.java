package Task2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*; 

public class BruteForceStringMatch {
    public static void main(String args[]) throws IOException {
        int numThreads = 0;

        if (args.length != 3) {
			System.out.println("BruteForceStringMatch  <file name> <pattern> <numThreads>");
			System.exit(1);
        }

        String fileString = new String(Files.readAllBytes(Paths.get(args[0])));//, StandardCharsets.UTF_8);
        char[] text = new char[fileString.length()]; 
        int n = fileString.length();
        for (int i = 0; i < n; i++) { 
            text[i] = fileString.charAt(i); 
        } 

        String patternString = new String(args[1]);                                                
        char[] pattern = new char[patternString.length()]; 
        int m = patternString.length(); 
        for (int i = 0; i < m; i++) { 
            pattern[i] = patternString.charAt(i); 
        }

        try {
            numThreads = Integer.parseInt(args[2]);
            if (numThreads == 0)
                numThreads = Runtime.getRuntime().availableProcessors();
        } catch (NumberFormatException e) {
            System.out.println("Invalid number of threads: " + args[2]);
            System.exit(1);
        }

        //Το n είναι το μήκος του κειμένου και το m το μήκος του pattern.
        //Το matchLength αντιπροσωπεύει τη μέγιστη θέση από την οποία μπορεί να ξεκινάει το pattern μέσα στο κείμενο. 
        // Αν το pattern ξεκινήσει μετά από αυτό το σημείο, δεν θα "χωράει" να ολοκληρωθεί μέχρι το τέλος του κειμένου.
        int matchLength = n - m;
        //Δημιουργείται ένας πίνακας χαρακτήρων (char[]) που έχει τόσες θέσεις όσες και οι πιθανές θέσεις έναρξης του pattern μέσα στο κείμενο
        char[] match = new char[matchLength+1]; 
        for (int i = 0; i <= matchLength; i++) { 
            match[i] = '0'; // γεμίζει όλο τον πίνακα με τον χαρακτήρα '0' = false
        }
        
        // get current time
		long start = System.currentTimeMillis();

        StringMatchThread[] threads = new StringMatchThread[numThreads];

        for(int i = 0 ; i < numThreads; i++) {
            threads[i] = new StringMatchThread(i, numThreads, matchLength, m, text, pattern, match);
            threads[i].start();
        }
        int matchCount = 0;
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
                matchCount += threads[i].getMyMatchCount();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }

        // get current time and calculate elapsed time
		long elapsedTimeMillis = System.currentTimeMillis()-start;
		System.out.println("time in ms = "+ elapsedTimeMillis);
		
		//print results
        for (int i = 0; i <= matchLength; i++) { 
            if (match[i] == '1') System.out.print(i+" ");
        }
        System.out.println();
        System.out.println("Total matches " + matchCount); 
    }
}


class StringMatchThread extends Thread {
    private int myId, numThreads, m;
    private long myStart, myStop, matchLength;
    private char[] text, pattern, match;
    private int myMatchCount = 0;

    public StringMatchThread(int id, int numThreads, long matchLength, int m, char[] text, char[] pattern, char[] match) {
        this.myId = id;
        this.numThreads = numThreads;
        this.matchLength = matchLength;
        this.m = m;
        this.text = text;
        this.pattern = pattern;
        this.match = match;
        
        this.myStart = myId * (matchLength / numThreads);
        this.myStop = myStart + (matchLength / numThreads);
        if (myId == numThreads - 1) 
            this.myStop = matchLength; // Το τελευταίο νήμα πηγαίνει μέχρι το τέλος
    }

    public void run() {
        for (long j = myStart; j <= myStop; j++) {
            int i;
            // Η συνθήκη σύγκρισης που συζητήσαμε
            for (i = 0; i < m && pattern[i] == text[(int)(i + j)]; i++);
            
            if (i >= m) {
                match[(int)j] = '1';
                myMatchCount++;
            }
        }
    }
	//i < m: Η σύγκριση συνεχίζεται όσο δεν έχουμε φτάσει στο τέλος του μοτίβου (όπου m είναι το μήκος του pattern).
    //pattern[i] == text[(int)(i + j)]: Συγκρίνουμε τον χαρακτήρα στη θέση i του μοτίβου με τον αντίστοιχο χαρακτήρα στο κείμενο. Εφόσον ξεκινήσαμε από τη θέση j του κειμένου, ο χαρακτήρας που εξετάζουμε στο κείμενο βρίσκεται στη θέση j + i.
    //i++: Αν οι χαρακτήρες ΤΑΙΡΙΑΖΟΥΝ, το i αυξάνεται κατά 1 και πάμε στον επόμενο χαρακτήρα.

    public int getMyMatchCount() {
        return myMatchCount;
    }
}
// Για java BruteForceStringMatch.java E.coli “tacccagattatcgccatcaacgg”  1 (σειριακά) τύπωσε:
// time in ms = 21
// 7311 (<- θέση index μέσα στο κείμενο (στο αρχείο dna του E.coli))
// Total matches 1

// Για java BruteForceStringMatch.java E.coli “tacccagattatcgccatcaacgg” 4 (parallel) τύπωσε: 
// time in ms = 18

// Για java BruteForceStringMatch.java sixteen-times.txt "tacccagattatcgccatcaacgg" 1 τύπωσε:
// time in ms = 345
// 7311 4646003 9284695 13923387 18562079 23200771 27839463 32478155 37116847 41755539 46394231 51032923 55671615 60310307 64948999 69587691 
// Total matches 16

// Για java BruteForceStringMatch.java sixteen-times.txt "tacccagattatcgccatcaacgg" 4 τύπωσε:
// time in ms = 118
// 7311 4646003 9284695 13923387 18562079 23200771 27839463 32478155 37116847 41755539 46394231 51032923 55671615 60310307 64948999 69587691 
// Total matches 16

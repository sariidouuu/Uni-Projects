// Java program for Merge Sort

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class Task2 {
    // Merges two subarrays of arr[].
    // First subarray is arr[l..m]
    // Second subarray is arr[m+1..r]

    // Driver code
    public static void main(String args[]) {
        //int arr[] = { 12, 11, 13, 5, 6, 7 };
        // Το σβληνιυμε διότι θα δώσουμε δικό μας size στον πίνακα

        int size = 0;
        int limit = 1000;

        if (args.length != 2) {
            System.err.println("Arguments:  <number_of_arry_size>  <limit>");
            System.exit(1);
        }
        try {
            size = Integer.parseInt(args[0]); 
            limit = Integer.parseInt(args[1]); // Το όριο αποκοπής
        } catch (NumberFormatException nfe) {
            System.out.println("Arguments must be integers");
            System.exit(1);
        }

        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = (int) (Math.random() * size + 1);
        }

        //System.out.println("Given array is");
        //printArray(arr);

        long startTime = System.currentTimeMillis();

        ForkJoinPool pool = new ForkJoinPool(4); // ή 8 νήματα, το αλλάζω
        MergeTask task = new MergeTask(arr, 0, arr.length - 1, limit);
        pool.invoke(task);
        pool.shutdown();

        long endTime = System.currentTimeMillis() - startTime;

        System.out.println("The array is sorted");
        System.out.printf("Time taken is %d ms\n", endTime);
    }

    // A utility function to print array of size n
    static void printArray(int arr[]) {
        int n = arr.length;
        for (int i = 0; i < n; ++i)
            System.out.print(arr[i] + " ");
        System.out.println();
    }
}

class MergeTask extends RecursiveTask<Void> {
    private int[] arr;
    private int left; //from
    private int right; //to
    private int limit; // κατώφλι αποκοπής

    public MergeTask(int[] arr, int left, int right, int limit) {
        this.arr = arr;
        this.left = left;
        this.right = right;
        this.limit = limit;
    }

    @Override
    protected Void compute() { // Την αλλάζω σε void αφού δεν επιστρέφω κάτι
        long workload = right - left; //to-from
        if (workload <= limit) {
            // Αν το workload είναι μικρότερο από το limit τότε μπορώ να τρέξω τη sort:
            sort(arr, left, right);
        }else{
            // Αλλιώς σπάμε  τις εργασίες σε υποεργασίες
            int mid = left + (right - left) / 2; //size=right-lefft
            
            MergeTask leftTask = new MergeTask(arr, left, mid, limit);
            MergeTask rightTask = new MergeTask(arr, mid + 1, right, limit);
            
            leftTask.fork(); // υο ένα το στέλνω σε άλλο νήμα
            rightTask.compute(); // Το ίδο νήμα υπολογίζει εδώ το ένα task
            leftTask.join(); // Join

            merge(arr, left, mid, right);
        }
        return null;
    }

    void merge(int arr[], int l, int m, int r) {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        // Create temp arrays
        int L[] = new int[n1];
        int R[] = new int[n2];

        // Copy data to temp arrays
        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];

        // Merge the temp arrays
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarray
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        // Copy remaining elements of L[] if any
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        // Copy remaining elements of R[] if any
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

        // Main function that sorts arr[l..r] using merge()
    void sort(int arr[], int l, int r) {
        if (l < r) {
            // Find the middle point
            int m = l + (r - l) / 2;

            // Sort first and second halves
            sort(arr, l, m);
            sort(arr, m + 1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }
}

// Για 4 νήματα:
// Με limit  2.500:      Με limit  10.000:
// Size       | ms       Size       | ms
// 100.000    | 9        100.000    | 3
// 500.000    | 25       500.000    | 18
// 1.000.000  | 69       1.000.000  | 24

// Για 8 νήματα:
// Με limit  2.500:      Με limit  10.000:
// Size       | ms       Size       | ms
// 100.000    | 8        100.000    | 4
// 500.000    | 18       500.000    | 17
// 1.000.000  | 25       1.000.000  | 27

// Συμπεράσματα:
// 1. Με 4 threads: Με μεγαλύτερο limit (κατώφλι) έχω λιγότερο Overhead. Αφού δημιουργούνται λιγότερα αντικείμενα MergeTask και καλώ λιγότερες φορές την fork.
// 2. Με 8 threads: το μέγεθος του limit δεν έχει κάποια επίπτωση.
// 3. Παρατηρώ πως για το size 1.000.000 η αύξηση των νημάτων ρίχνει τον χρόνο από 69ms σε 25ms. 
// Αυτό συμβαίνει διότι οι εργασίες μπορούν να σταλθούν σε περισσότερα νήματα.

// Άρα είναι σημαντικό το πως θα συνδυάσουμε τα: limit & threads για κάθε size, για καλύερη απόδοση. 
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
#include <stdbool.h>

int main(int argc, char *argv[])
{
    int size = 0;
    
    // Check if the correct number of arguments is provided
    if (argc != 2) {
        printf("Usage: %s <size>\n", argv[0]);
        return 1;
    }
    
    // Convert argument to integer
    char *endptr;
    size = strtol(argv[1], &endptr, 10);
    
    // Check if the conversion was successful
    if (*endptr != '\0') {
        printf("Integer argument expected\n");
        return 1;
    }
    
    // Check if size is positive
    if (size <= 0) {
        printf("size should be positive integer\n");
        return 1;
    }
    
    // Allocate memory for the prime array
    bool *prime = (bool*)malloc((size + 1) * sizeof(bool));
    if (prime == NULL) {
        printf("Memory allocation failed\n");
        return 1;
    }
    
    // Initialize all entries as true (potentially prime)
    for (int i = 2; i <= size; i++) {
        prime[i] = true;
    }
    
    // Set 0 and 1 as not prime
    if (size >= 0) prime[0] = false;
    if (size >= 1) prime[1] = false;
    
    // Get current time
    clock_t start = clock();
    
    // Apply Sieve of Eratosthenes algorithm
    int limit = (int)sqrt(size) + 1;
    for (int p = 2; p <= limit; p++) {
        // If prime[p] is true, then it is a prime
        if (prime[p]) {
            // Update all multiples of p
            for (int i = p * p; i <= size; i += p) {
                prime[i] = false;
            }
        }
    }
    
    // Calculate elapsed time
    clock_t end = clock();
    double elapsed_time_ms = ((double)(end - start) * 1000.0) / CLOCKS_PER_SEC;
    
    // Count primes
    int count = 0;
    for (int i = 2; i <= size; i++) {
        if (prime[i]) {
            //printf("%d\n", i); // Uncomment to print all primes
            count++;
        }
    }
    
    printf("number of primes %d\n", count);
    printf("time in ms = %.2f\n", elapsed_time_ms);
    
    // Free allocated memory
    free(prime);
    
    return 0;
}

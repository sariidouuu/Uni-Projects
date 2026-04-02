#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

// Structure to represent a word and its count
typedef struct WordNode {
    char* word;
    int count;
    struct WordNode* left;
    struct WordNode* right;
} WordNode;

// Function to create a new word node
WordNode* createNode(const char* word) {
    WordNode* newNode = (WordNode*)malloc(sizeof(WordNode));
    if (newNode == NULL) {
        fprintf(stderr, "Memory allocation failed\n");
        exit(1);
    }
    
    newNode->word = strdup(word);
    if (newNode->word == NULL) {
        fprintf(stderr, "Memory allocation failed\n");
        free(newNode);
        exit(1);
    }
    
    newNode->count = 1;
    newNode->left = NULL;
    newNode->right = NULL;
    return newNode;
}

// Function to insert or update a word in the BST
WordNode* insert(WordNode* root, const char* word) {
    if (root == NULL) {
        return createNode(word);
    }
    
    int cmp = strcasecmp(word, root->word);
    
    if (cmp < 0) {
        root->left = insert(root->left, word);
    } else if (cmp > 0) {
        root->right = insert(root->right, word);
    } else {
        // Word already exists, increment count
        root->count++;
    }
    
    return root;
}

// Function to read the entire file into a string
char* readFile(const char* filename) {
    FILE* file = fopen(filename, "r");
    if (file == NULL) {
        fprintf(stderr, "Error opening file %s\n", filename);
        exit(1);
    }
    
    // Find the file size
    fseek(file, 0, SEEK_END);
    long fileSize = ftell(file);
    fseek(file, 0, SEEK_SET);
    
    // Allocate memory for the entire file content plus null terminator
    char* buffer = (char*)malloc(fileSize + 1);
    if (buffer == NULL) {
        fprintf(stderr, "Memory allocation failed\n");
        fclose(file);
        exit(1);
    }
    
    // Read the file into the buffer
    size_t bytesRead = fread(buffer, 1, fileSize, file);
    buffer[bytesRead] = '\0';  // Null terminate the string
    
    fclose(file);
    return buffer;
}

// Function for in-order traversal and printing the results
void printInOrder(WordNode* root) {
    if (root != NULL) {
        printInOrder(root->left);
        printf("%s\t %d\n", root->word, root->count);
        printInOrder(root->right);
    }
}

// Function to free the BST
void freeTree(WordNode* root) {
    if (root != NULL) {
        freeTree(root->left);
        freeTree(root->right);
        free(root->word);
        free(root);
    }
}

// Function to check if a character is a delimiter
int isDelimiter(char c) {
    return (c == ' ' || c == '\n' || c == '\t' || c == '\r' ||
            c == '.' || c == ',' || c == ';' || c == ':' ||
            c == '!' || c == '?' || c == '(' || c == ')' ||
            c == '{' || c == '}');
}

int main(int argc, char* argv[]) {
    if (argc != 2) {
        printf("Usage: %s <file name>\n", argv[0]);
        return 1;
    }
    
    char* fileContent = readFile(argv[1]);
    WordNode* root = NULL;
    
    // Tokenize the file content and build the BST
    char* token = strtok(fileContent, " \n\t\r.,;:!?(){}");
    while (token != NULL) {
        // Convert token to lowercase
        char* p = token;
        while (*p) {
            *p = tolower((unsigned char)*p);
            p++;
        }
        
        // Only insert non-empty tokens
        if (strlen(token) > 0) {
            root = insert(root, token);
        }
        
        token = strtok(NULL, " \n\t\r.,;:!?(){}");
    }
    
    // Print the word counts in alphabetical order
    printInOrder(root);
    
    // Clean up
    freeTree(root);
    free(fileContent);
    
    return 0;
}

package bfs;

import java.util.*;
import java.io.*;

// Adjacency list -> https://www.youtube.com/watch?v=ee6zIj4J3-Y 

public class SocialNetworkGraph {
    private int numAccounts; // Number of accounts in the social network
    private int numFriendships; // Number of connections (edges) in the social network
    private ArrayList<LinkedList<Integer>> adjacencyList; // Create adjacency list of integers (Account numbers from txt file)

    /*
     * Constructor
     * 
     * @param filename - file name of chosen data set (.txt file)
     * 
     */ 
    public SocialNetworkGraph(String filename) throws FileNotFoundException {
        loadFromFile(filename);
    }

    /*
     * Read all file contents and convert them to a graph using Adjacency List
     * 
     * @param filename - file name of chosen data set (.txt file)
     * 
     */ 
    private void loadFromFile(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        
        // Read first line: n and e
        numAccounts = scanner.nextInt(); // n (Number of accounts)
        numFriendships = scanner.nextInt(); // e (Number of connections)
        
        // Initialize adjacency list and linked lists
        adjacencyList = new ArrayList<>(numAccounts);
        for (int i = 0; i < numAccounts; i++) {
            adjacencyList.add(new LinkedList<>());
        }
        
        // Read friendship
        for (int i = 0; i < numFriendships; i++) {
            int a = scanner.nextInt(); // Account
            int b = scanner.nextInt(); // Friend of the account
            
            // Validate account IDs
            if (a < 0 || a >= numAccounts || b < 0 || b >= numAccounts) {
                scanner.close();
                throw new IllegalArgumentException("Invalid account ID in friendship: " + a + " " + b);
            }
            
            // Add bidirectional friendships (Place accounts in each other's friends list)
            if (!adjacencyList.get(a).contains(b)) { // Add node b to node a linked list if it hasn't yet
                adjacencyList.get(a).add(b);
            }
            if (!adjacencyList.get(b).contains(a)) { // Add node a to node b linked list if it hasn't yet
                adjacencyList.get(b).add(a);
            }
        }
        
        scanner.close();
    }
    
    // Getters
    public int getNumAccounts() {
        return numAccounts;
    }
    
    public int getNumFriendships() {
        return numFriendships;
    }
    
    public ArrayList<LinkedList<Integer>> getAdjacencyList() {
        return adjacencyList;
    }
    
    // Returns a single linked list of a chosen node/account
    public List<Integer> getFriends(int accountId) {
        if (accountId < 0 || accountId >= numAccounts) {
            return Collections.emptyList();
        }
        return new ArrayList<>(adjacencyList.get(accountId));
    }
}

package bfs;

import java.util.*;
import java.io.*;

public class SocialNetworkGraph {
    private int numAccounts;
    private int numFriendships;
    private List<List<Integer>> adjacencyList;
    
    public SocialNetworkGraph(String filename) throws FileNotFoundException {
        loadFromFile(filename);
    }
    
    private void loadFromFile(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        
        // Read first line: n and e
        numAccounts = scanner.nextInt(); // n
        numFriendships = scanner.nextInt(); // e
        
        // Initialize adjacency list
        adjacencyList = new ArrayList<>(numAccounts);
        for (int i = 0; i < numAccounts; i++) {
            adjacencyList.add(new ArrayList<>());
        }
        
        // Read friendships
        for (int i = 0; i < numFriendships; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            
            // Validate account IDs
            if (a < 0 || a >= numAccounts || b < 0 || b >= numAccounts) {
                scanner.close();
                throw new IllegalArgumentException("Invalid account ID in friendship: " + a + " " + b);
            }
            
            // Add bidirectional friendship (avoid duplicates)
            if (!adjacencyList.get(a).contains(b)) {
                adjacencyList.get(a).add(b);
            }
            if (!adjacencyList.get(b).contains(a)) {
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
    
    public List<List<Integer>> getAdjacencyList() {
        return adjacencyList;
    }
    
    public List<Integer> getFriends(int accountId) {
        if (accountId < 0 || accountId >= numAccounts) {
            return Collections.emptyList();
        }
        return new ArrayList<>(adjacencyList.get(accountId));
    }
}

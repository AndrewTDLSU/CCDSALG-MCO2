/*
 * CCDSALG MCO2: Reconstructing friends list using graphs and connections through BFS algorithm
 * 
 * Authors:
 *   - Charles Stephen Cordez
 *   - Nyan Jezreel Espineli
 *   - Ian Gabriel Ilagan
 *   - Andrew Tagalag
 * 
 * Description:
 *   This program takes in a data set .txt file representing a social network 
 *   containing the ff. (Number of accounts, Number of connections, and the
 *   list of connections with each number representing an account ID). This
 *   program aims to replicate social media websites (e.g. Facebook) by 
 *   implementing algorithms to find the friends list of a specified account
 *   ID and also the social connection between 2 given account IDs.
 * 
 * Date: November 2025
 */

package bfs;

import java.util.*;
import bfs.SocialNetworkGraph;
//import java.io.*;

// BFS -> https://www.geeksforgeeks.org/dsa/breadth-first-search-or-bfs-for-a-graph/ 

public class Main {

    /*
     * Returns friend ID list of person by printing the linked list of the account
     * node in the graph
     * 
     * @param sc - scanner
     * 
     * @param - the social network graph
     * 
     * @return if no account exists
     * 
     */
    public static void getFriendList(Scanner sc, SocialNetworkGraph graph) {
        System.out.print("Enter ID of person: ");
        int personId = sc.nextInt(); // Input the account's ID

        // Determines if the account exists or not (doesn't exist if negative number or
        // is more than the number of accounts)
        if (personId < 0 || personId >= graph.getNumAccounts()) {
            System.out.println("Error: Person ID " + personId + " does not exist!");
            return; // Return to main menu if not found
        }

        List<Integer> friends = graph.getFriends(personId); // Return friends list from graph
        System.out.println("\nPerson " + personId + " has " + friends.size() + " friends!");
        System.out.print("List of friends: ");

        // Print friends list in the format shown in sample
        for (int i = 0; i < friends.size(); i++) {
            System.out.print(friends.get(i));
            if (i < friends.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    /*
     * Find if there is a connection between the 2 IDs through BFS algorithm
     * 
     * @param sc - scanner for input
     * 
     * @param graph - the social network
     * 
     * @return if either account doesn't exists
     * 
     */
    public static void getConnection(Scanner sc, SocialNetworkGraph graph) {

        // Account number inputs
        System.out.print("Enter ID of first person: ");
        int person1 = sc.nextInt();
        System.out.print("Enter ID of second person: ");
        int person2 = sc.nextInt();

        // Validate if accounts exist
        if (person1 < 0 || person1 >= graph.getNumAccounts()) {
            System.out.println("Error: Person ID " + person1 + " does not exist!");
            return;
        }
        if (person2 < 0 || person2 >= graph.getNumAccounts()) {
            System.out.println("Error: Person ID " + person2 + " does not exist!");
            return;
        }
        // If same person entered
        if (person1 == person2) {
            System.out.println("Same person entered!");
            return;
        }

        // Find connection using BFS
        List<Integer> path = findConnectionBFS(person1, person2, graph.getAdjacencyList());

        if (path.isEmpty()) { // No connection found
            System.out.println("Cannot find a connection between " + person1 + " and " + person2);
        } else { // Connection found
            System.out.println("There is a connection from " + person1 + " to " + person2 + "!");
            printConnectionPath(path);
        }
    }

    /*
     * BFS for finding path between two nodes (Specified account and target account)
     * 
     * @param start - person 1 ID
     * 
     * @param target - person 2 ID
     * 
     * @param adj - social network graph adjacency list
     * 
     * @return List of account IDs from person 1 to person 2, or empty list if there
     * isn't a connection between the two IDs
     * 
     */
    public static List<Integer> findConnectionBFS(int start, int target, ArrayList<LinkedList<Integer>> adj) {
        int V = adj.size();
        boolean[] visited = new boolean[V]; // Checks if node is visited
        int[] parent = new int[V]; // To track the correct path from source to destination node (important for
                                   // printing who's friends with who)
        Arrays.fill(parent, -1);

        Queue<Integer> q = new LinkedList<>();
        visited[start] = true; // Starting node is already marked true
        parent[start] = -1; // Start node has no parent
        q.add(start);

        while (!q.isEmpty()) {
            int curr = q.poll(); // Current node from queue

            // If we found the target, reconstruct and return the path
            if (curr == target) {
                return reconstructPath(parent, start, target);
            }

            // Visit all unvisited neighbors of current node
            for (int x : adj.get(curr)) {
                if (!visited[x]) {
                    visited[x] = true; // Mark node as visited
                    parent[x] = curr; // Track which node we came from
                    q.add(x);
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    /*
     * Build the path from the source node to the destination node
     * 
     * @param parent - array of nodes within the correct path between start and
     * target
     * 
     * @param start - person 1 ID
     * 
     * @param target - person 2 ID
     * 
     * @return List of account ID's from person 1 to person 2
     * 
     */
    public static List<Integer> reconstructPath(int[] parent, int start, int target) {
        List<Integer> path = new ArrayList<>();

        // Backtrack from target to start
        int current = target;
        while (current != -1) {
            path.add(current);
            current = parent[current];
        }

        // Reverse to get path from start to target
        Collections.reverse(path);
        return path;
    }

    /*
     * Print the friend connection
     * 
     * @param path - reconstructed path array between person 1 and person 2 IDs
     * 
     */
    public static void printConnectionPath(List<Integer> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            System.out.println(path.get(i) + " is friends with " + path.get(i + 1));
        }
    }

    /*
     * Helper method to load graph from file
     * 
     * @param sc - scanner
     * 
     * @return the graph if found successfully, null if not found
     * 
     */
    public static SocialNetworkGraph loadGraph(Scanner sc) {
        System.out.print("Input file name: ");
        String filename = sc.nextLine().trim() + ".txt";

        // Use try-catch to avoid prematurely terminating the program
        try {
            SocialNetworkGraph graph = new SocialNetworkGraph(filename); // Convert text file contents to a graph
            System.out.println("Graph loaded!");
            return graph;
        } catch (Exception e) { // File not found
            System.out.println("Error loading file: " + e.getMessage());
            System.out.println("Please try again.");
            return null;
        }
    }

    /*
     * Main
     * 
     * @param args - main method
     * 
     */
    public static void main(String[] args) throws Exception {

        boolean running = true; // Make sure program doesn't terminate until user chooses to exit
        SocialNetworkGraph graph = null; // Data set is converted to a graph object
        boolean fileChosen = false; // Indicator if a txt file is found
        Scanner sc = new Scanner(System.in); // Scanner

        while (running) {

            while (!fileChosen) {
                if (graph == null) {
                    graph = loadGraph(sc); // Input data set txt file
                    if (graph != null) {
                        fileChosen = true; // Exit the loop when graph is loaded
                    }
                }
            }

            // Present menu if file and graph is loaded
            while (fileChosen && graph != null) {
                // Main menu
                System.out.println("\nMAIN MENU");
                System.out.println("[1] Get friend list");
                System.out.println("[2] Get connection");
                System.out.println("[3] Exit");

                System.out.print("\nEnter your choice: ");
                int choice = sc.nextInt(); // User's choice

                switch (choice) {
                    case 1: // Return the friend list
                        getFriendList(sc, graph);
                        break;
                    case 2: // Return the connections
                        getConnection(sc, graph);
                        break;
                    case 3: // Terminate the program
                        fileChosen = false;
                        running = false;
                        System.out.println("\nGoodbye!");
                        break;
                    default: // Tell user to choose again if invalid choice
                        System.out.println("Invalid choice! Try again");
                }
            }
        }
        sc.close();
    }
}
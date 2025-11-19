package bfs;

import java.util.*;
import java.io.*;

// BFS -> https://www.geeksforgeeks.org/dsa/breadth-first-search-or-bfs-for-a-graph/
// DFS -> https://www.geeksforgeeks.org/dsa/depth-first-search-or-dfs-for-a-graph/ 

public class Main {

    // Returns friend ID list of person
    public static void getFriendList(Scanner sc, SocialNetworkGraph graph) {
        System.out.print("Enter ID of person: ");
        int personId = sc.nextInt();
        
        if (personId < 0 || personId >= graph.getNumAccounts()) {
            System.out.println("Error: Person ID " + personId + " does not exist!");
            return;
        }
        
        List<Integer> friends = graph.getFriends(personId);
        System.out.println("\nPerson " + personId + " has " + friends.size() + " friends!");
        System.out.print("List of friends: ");
        
        // Print friends in the format shown in sample
        for (int i = 0; i < friends.size(); i++) {
            System.out.print(friends.get(i));
            if (i < friends.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    // Find if there is a connection between the 2 IDs
    public static void getConnection(Scanner sc, SocialNetworkGraph graph) {
        System.out.print("Enter ID of first person: ");
        int person1 = sc.nextInt();
        System.out.print("Enter ID of second person: ");
        int person2 = sc.nextInt();
        
        // Validate inputs
        if (person1 < 0 || person1 >= graph.getNumAccounts()) {
            System.out.println("Error: Person ID " + person1 + " does not exist!");
            return;
        }
        if (person2 < 0 || person2 >= graph.getNumAccounts()) {
            System.out.println("Error: Person ID " + person2 + " does not exist!");
            return;
        }
        if (person1 == person2) {
            System.out.println("Same person entered!");
            return;
        }
        
        // Find connection using BFS
        List<Integer> path = findConnectionBFS(person1, person2, graph);
        
        if (path.isEmpty()) {
            System.out.println("Cannot find a connection between " + person1 + " and " + person2);
        } else {
            System.out.println("There is a connection from " + person1 + " to " + person2 + "!");
            printConnectionPath(path);
        }
    }

    // BFS
    public static List<Integer> findConnectionBFS(int start, int target, SocialNetworkGraph graph) {
        int n = graph.getNumAccounts();
        boolean[] visited = new boolean[n];
        int[] parent = new int[n]; // To reconstruct path
        Arrays.fill(parent, -1);
        
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        visited[start] = true;
        parent[start] = -1;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            // If we found the target
            if (current == target) {
                return reconstructPath(parent, start, target);
            }
            
            // Visit all friends
            for (int friend : graph.getFriends(current)) {
                if (!visited[friend]) {
                    visited[friend] = true;
                    parent[friend] = current;
                    queue.offer(friend);
                }
            }
        }
        
        return Collections.emptyList(); // No path found
    }

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

    public static void printConnectionPath(List<Integer> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            System.out.println(path.get(i) + " is friends with " + path.get(i + 1));
        }
    }

    // Helper method to load graph from file
    public static SocialNetworkGraph loadGraph(Scanner sc) {
        System.out.print("Input file path: ");
        String filename = sc.nextLine();

        try {
            SocialNetworkGraph graph = new SocialNetworkGraph(filename); // Convert text file contents to a graph
            System.out.println("Graph loaded!");
            return graph;
        } catch(Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
            System.out.println("Please try again.");
            return null;
        }
    }

    // Main
    public static void main(String[] args) throws Exception {

        boolean running = true;
        Scanner sc = new Scanner(System.in);
        SocialNetworkGraph graph = null;

        while(running) {

            // Load graph if not already loaded
            if (graph == null) {
                graph = loadGraph(sc);
                if (graph == null) {
                    continue; // Continue to next iteration if loading failed
                }
            }

            boolean fileChosen = true;
            while(fileChosen && graph != null){
                System.out.println("\nMAIN MENU");
                System.out.println("[1]Get friend list");
                System.out.println("[2]Get connection");
                System.out.println("[3]Exit");

                System.out.print("\nEnter your choice: ");
                int choice = sc.nextInt();

                switch(choice) {
                    case 1:
                        getFriendList(sc, graph);
                        break;
                    case 2:
                        getConnection(sc, graph);
                        break;
                    case 3:
                        fileChosen = false;
                        running = false;
                        System.out.println("\nGoodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice! Try again");
                }
            }                       
        }
        sc.close();
    }
}
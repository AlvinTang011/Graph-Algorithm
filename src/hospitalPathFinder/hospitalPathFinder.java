package hospitalPathFinder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;

import Interface.App;

public class hospitalPathFinder {

    private static String output = "";

    // No of vertices
    private int v, h;

    // Adjacency list for storing which vertices are connected
    private ArrayList<ArrayList<Integer>> adj;

    // List of hospitals
    private Hashtable<Integer, Integer> hpl;
    
    // List of already visited hospitals
    private ArrayList<Integer> avh;
    
    // Track the number of nearest hospitals
    private int numOfHospitals;
    
    // List of nodes that are unable to find nearest hospitals
    // no nearest hospital array
    private Hashtable<Integer, Integer> nnh;

    // ID and index for the List of nodes in the graph
    private Hashtable<Integer, Integer> id;
    private Hashtable<Integer, Integer> key;

    // Constructor
    public hospitalPathFinder(String file1, String file2) {
        //initialize global variables to 0 or empty;
    	this.v = 0;
        this.h = 0;
        this.adj = new ArrayList<ArrayList<Integer>>();
        this.hpl = new Hashtable<Integer, Integer>();
        this.id = new Hashtable<Integer, Integer>();
        this.key = new Hashtable<Integer, Integer>();
        this.nnh = new Hashtable<Integer, Integer>();
        
        
    	// File1 extraction
        try {
            File graphFile = new File(file1);
            Scanner sc = new Scanner(graphFile);
            String data = "";
            while (sc.hasNextLine()) {
                data = sc.nextLine();
                // Retrieve number of nodes
                if (data.startsWith("# Nodes:")) {
                    String numOfNodes = data.substring(9);
                    numOfNodes = numOfNodes.substring(0, numOfNodes.indexOf(" "));
                    v = Integer.parseInt(numOfNodes);
                    adj = new ArrayList<ArrayList<Integer>>(v); // list size = no. of vertices
                    for (int j = 0; j < v; ++j) {
                        adj.add(new ArrayList<Integer>()); // each vertex has its own list
                    }
                }
                // addEdge and hashTable storing of vertices ID and index
                if (Character.isDigit(data.charAt(0))) {
                    int val = Integer.parseInt(data.substring(0, data.indexOf("\t")));
                    // Insert first non-duplicate node into nodes list from the line
                    if (!key.containsKey(val)) {
                    	id.put(id.size(), val);
                        key.put(val, key.size());
                    }
                    int val2 = Integer.parseInt(data.substring(data.indexOf("\t") + 1));
                    // Insert second non-duplicate node into nodes list from the line
                    if (!key.containsKey(val2)) {
                    	id.put(id.size(), val2);
                        key.put(val2, key.size());
                    }
                    // Insert bi-directional edge into adjacency list
                    adj.get(key.get(val)).add(key.get(val2));
                    adj.get(key.get(val2)).add(key.get(val));
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error");
            e.printStackTrace();
        }

        // File2 extraction
        try {
            File graphFile = new File(file2);
            Scanner sc = new Scanner(graphFile);
            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                if (!data.startsWith("#")) {
                    hpl.put(hpl.size(), Integer.parseInt(data));
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }

    // function to print the shortest distance and path
    // between source vertex and destination vertex
    // k is iterator
    private void printShortestDistance(int s, int k) {
    	
    	// Get key of current source node
        s = key.get(s);
        
        // Make a new empty list of already visited hospitals
        avh = new ArrayList<Integer>();
        
        // Restart number of already visited hospitals to 0
        numOfHospitals = 0;

        // predecessor[i] array stores predecessor of
        // i and distance array stores distance of i
        // from s
        int pred[] = new int[v];
        int dist[] = new int[v];

        // Repeat K times for Top-K nearest hospital
        for (int x = 0; x < k; x++) {
            
        	// Perform modified BFS to find nearest hospital
        	int dest = BFS(s, pred, dist, numOfHospitals);
            
        	// if return value is -1, no nearest hospital accessible for the current node
            if (dest == -1) {
            	output += "Hospital unreachable for node " + id.get(s) + "\n";
            	// For clairty, output includes telling user how many nearby hospitals
            	// for that specific nodes it previously had visited before dest = -1
            	output += "No further hospitals after " + (numOfHospitals) + " hospital(s)" + "\n";
            	// Extra line for cleaner output
            	output += "\n";
            	return;
            } else {
            	// get the nearest hospital node
            	dest = key.get(dest);
                // LinkedList to store path
                LinkedList<Integer> path = new LinkedList<Integer>();
                int crawl = dest;
                path.add(crawl);
                while (pred[crawl] != -1) {
                	path.add(pred[crawl]);
                    crawl = pred[crawl];
                }

                // Print nearest hospital node
                output += "Hospital found at " + id.get(dest);

                // Print distance
                output += "\n" + "Shortest path length is " + dist[dest] + "\n";

                // Print path
                output += "Path is ";
                for (int i = path.size() - 1; i >= 0; i--) {
                    output += id.get(path.get(i)) + " ";
                    if (i > 0) {
                        output += "> ";
                    }
                }
                
                // Print next line
                output += "\n";
            }
            
            // Adds current nearest hospital into array
            avh.add(id.get(dest));
            // Increment number of hospitals by 1
            numOfHospitals++; 
        }
        
        // An additional line for next source node for ease of viewing
        output += "\n";
    }
    
    // a modified version of BFS that stores predecessor
    // of each vertex in array pred
    // and its distance from source in array dist
    private int BFS(int src, int pred[], int dist[], int numOfHospitals) {
        
    	// initialize dest value to -1
    	int dest = -1;
    	
    	// temporary array list to store nodes for nnh array
        ArrayList<Integer> temp = new ArrayList<Integer>();
    	
        // a queue to maintain queue of vertices whose
        // adjacency list is to be scanned as per normal
        // modified BFS algorithm using LinkedList of Integer type
        LinkedList<Integer> queue = new LinkedList<Integer>();

        // boolean array visited[] which stores the
        // information whether ith vertex is reached
        // at least once in the Breadth first search
        boolean visited[] = new boolean[v];

        // initially all vertices are unvisited
        // so v[i] for all i is false
        // and as no path is yet constructed
        // dist[i] for all i set to infinity
        for (int i = 0; i < v; i++) {
            visited[i] = false;
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }
        
        // now source is first to be visited and
        // distance from source to itself should be 0
        visited[src] = true;
        dist[src] = 0;
        queue.add(src);
        
        // checks if the node belongs to the path with no nearest hospital
        if (nnh.contains(id.get(src))) {
        	// if it is, take that node out from this list as it has been
        	// checked off, thus unnecessary to keep the node
        	nnh.remove(id.get(src));
        	return dest;
        }
        
        // checks if source node is a hospital node
        if (hpl.contains(id.get(src))) {
            
        	// if avh array is empty, it means no hospital node has
        	// been visted, thus it is the first nearest hospital node
        	// Returns the id of the node as the nearest hospital node
        	if (avh.isEmpty()) {
        		dest = id.get(src);
                return dest;
        	}
        	// ignores current node to find the next nearest hospital
        }
        
        // Perform modified BFS Algorithm
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int i = 0; i < adj.get(u).size(); i++) {
                // Checks if neighbour node has not been visited
            	if (visited[adj.get(u).get(i)] == false) {
            		
            		// Set neighbour node as visited
                    visited[adj.get(u).get(i)] = true;
                    
                    // Increase distance from source node by 1
                    dist[adj.get(u).get(i)] = dist[u] + 1;
                    
                    // Stores into predecessor array
                    pred[adj.get(u).get(i)] = u;
                    
                    // Add previous node into queue to track the path
                    queue.add(adj.get(u).get(i));
                    
                    // Stores the path of the nodes for the array nnh
                    temp.add(id.get(adj.get(u).get(i)));
                    
                    // Checks if current node is a hospital node
                    if (hpl.contains(id.get(adj.get(u).get(i)))) {
                    	                    	
                    	// If avh array is not empty, it means this is not the first
                    	// BFS cycle, thus perform a check to ensure that it is not 
                    	// the previous nearest hospital node
                    	if (!avh.isEmpty()) {
                    		// If current node is the same as previous nearest hospital
                    		// Pass and continue finding the next nearest hospital node
                    		if (avh.contains(id.get(adj.get(u).get(i)))) ;
                    		// If not, returns current node id as next nearest hospital
                    		else {
                    			dest = id.get(adj.get(u).get(i));
                    			return dest;
                    		}
                    	}
                    	
                    	// Since array is empty, this is the first BFS cycle
                    	// Just return current node id as nearest hospital
                    	else {
                    		dest = id.get(adj.get(u).get(i));
                    		return dest;
                    	}
                    }
                                                            
                }
            }
        }
        
        // Only checks during the first iteration of the modified BFS
        // So that in the event that after Top-N nearest hospital node,
        // If there is no N+1 nearest hospital node, the path will not
        // be added into the no nearest hospital nodes array.
        if (numOfHospitals == 0) {
	        //add list of nodes that do not have nearest hospital into array
	        for (int i = 0; i<temp.size(); i++) {
	        	if (!nnh.contains(temp.get(i))) {
	        		nnh.put(nnh.size(), temp.get(i));
	        	}	
	        }
        }
        // If no nearest hospital found, return dest value as -1
        return dest;
    }

    private void fileOutput() {
    	// write output to file
        try (FileWriter fstream = new FileWriter("output.txt", false);
            BufferedWriter bw = new BufferedWriter(fstream)) {
            bw.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Driver Program
    public static String main(App a) {
        int source;
        // preprocessing of data
        hospitalPathFinder pU = new hospitalPathFinder(a.getGraphDirectory(), a.getHospDirectory());
        
        //Print shortest distance for all nodes
        for (int i = 0; i < pU.id.size(); i++) {
        	source = pU.id.get(i);
        	pU.printShortestDistance(source, a.getkValue());
        	pU.fileOutput();
        }
        // Return output fo all nodes' shortest path
        return output;
    }

}
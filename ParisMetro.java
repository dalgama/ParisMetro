import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

import net.datastructures.AdjacencyMapGraph;
//import net.datastructures.Dijkstra;
import net.datastructures.Edge;
import net.datastructures.Graph;
import net.datastructures.GraphAlgorithms;
import net.datastructures.Map;
import net.datastructures.Vertex;

import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

import java.util.Set;
import java.util.HashSet;

import java.util.List;

public class ParisMetro {

	private int numOfVertices;

	private int numOfEdges;

	private String[] stationsInOrder;

	private Graph<Integer, Integer> sGraph;

	private Hashtable<Integer, Vertex<Integer>> vertices;

	private static final Integer WALKING_CONSTANT = new Integer(90);
	
	/** Identify all the stations belonging to the 
	same line as station N1. */
	public ParisMetro(Integer N1) throws Exception, IOException { 

		// System.out.println("Constructor 1\n");
		
		FileReader file = new FileReader("metro.txt");
		sGraph = new AdjacencyMapGraph<Integer, Integer>(true);
		readMetro(file);
		printAll(N1, -1, -1);
	}

	/** Find the shortest path between stations N1 & N2.
	Print all stations of the path in order.
	Print the total travel time. */
	public ParisMetro(Integer N1, Integer N2) throws Exception, IOException {

		// System.out.println("Constructor 2\n");

		FileReader file = new FileReader("metro.txt");
		sGraph = new AdjacencyMapGraph<Integer, Integer>(true);
		readMetro(file);
		printAll(N1, N2, -1);
	}

	/** Find the shortest path between stations N1 & N2
	knowing that the line on which station N3 is situated is
	not functioning. (Station N3 is an endpoint of its line.) 
	Print all stations of the path in order.
	Print the total travel time. */
	public ParisMetro(Integer N1, Integer N2, Integer N3) throws Exception, IOException {

		// System.out.println("Constructor 3\n");
		
		FileReader file = new FileReader("metro.txt");
		sGraph = new AdjacencyMapGraph<Integer, Integer>(true);
		readMetro(file);
		
		printAll(N1, N2, N3);
	}

	public void readMetro(FileReader filename) {

		try {

			vertices = new Hashtable<Integer, Vertex<Integer>>();

	        BufferedReader metroFile = new BufferedReader(filename);

	        int lineNumber = 0;

	        boolean space = false;

	        boolean sdw = false;

	        String line = metroFile.readLine();

	        String currentStation = "";

	        while (line != null) {

	            if (lineNumber == 0) {

	            	StringTokenizer tmpTok = new StringTokenizer(line);

	            	String bugCharString = tmpTok.nextToken();

	            	int ch=0;

	            	
	            	if (bugCharString.length() == 4) { // ...this takes care of an invisible "\uFEFF" character if present.
	            		System.out.println(bugCharString.length());
		            		while(line.charAt(ch)!= '3'){
		            			ch++;
		            		}
	            		//ch = 1;
	            	}
	            	else { // bugCharString.length() == 3 ...this means there is no "\uFEFF" character in front of "376".
	            		System.out.println(bugCharString.length());
	            		//ch = 0;
	            	}
	            	

	            	String strNumOfVertices = "";
	            	String strNumOfEdges = "";

	            	while (ch < line.length()) {

	            		if (line.charAt(ch) == ' ') {
	            			space = true;
	            			ch++;
	            		}

	            		if (space) {
	            			strNumOfEdges = strNumOfEdges + line.charAt(ch);
	            		}
	            		else {
	            			strNumOfVertices = strNumOfVertices + line.charAt(ch);	
	            		}

	            		ch++;
	            	}

	            	space = false;
					numOfVertices = Integer.parseInt(strNumOfVertices);
					numOfEdges = Integer.parseInt(strNumOfEdges);

					System.out.println(numOfVertices);
					System.out.println(numOfEdges);

					stationsInOrder = new String[numOfVertices];
	            }
	            else {

		        	StringTokenizer sTokenizer = new StringTokenizer(line);

		        	String token = sTokenizer.nextToken();

		        	if (token.equals("$")) {
		        		
		        		sdw = true;
		        		
		        		line = metroFile.readLine();
		        		sTokenizer = new StringTokenizer(line);
		        		token = sTokenizer.nextToken();
		        	}

		        	if (!sdw) {

		        		for (int ch = 0; ch < line.length(); ch++) {
		            		
		            		if ((line.charAt(ch) == ' ') && !space) {
		            			space = true;
		            			ch++;
		            		}

		            		if (space) {
		            			currentStation = currentStation + line.charAt(ch);
		            		}
		            	}

		            	space = false;
		            	stationsInOrder[lineNumber-1] = currentStation;
		            	currentStation = "";
		        	}
		        	else { // (sdw) // [  SOURCE  DESTINATION  WEIGHT  ]

		        		Integer source = new Integer(token);

		        		token = sTokenizer.nextToken();
		        		Integer dest = new Integer(token);  

		        		token = sTokenizer.nextToken();
		        		Integer weight = new Integer(token);
		        		
		        		if (weight == -1) {
		        			weight = WALKING_CONSTANT;
		        		}
		        		
		        		Vertex<Integer> sv = vertices.get(source);

		        		if (sv == null) {
		        			sv = sGraph.insertVertex(source);
		        			vertices.put(source, sv);
		        		}

		        		Vertex<Integer> dv = vertices.get(dest);

		        		if (dv == null) {
		        			dv = sGraph.insertVertex(dest);
							vertices.put(dest, dv);
		        		}

		        		if (sGraph.getEdge(sv, dv) == null) {
		        			Edge<Integer> e = sGraph.insertEdge(sv, dv, weight);
						}
		        	}	
		        }

	            line = metroFile.readLine();
	            lineNumber++;
	        }
	    }
	    catch (FileNotFoundException e) {
	    	e.printStackTrace();	
		}
		catch (IOException e) {
	    	e.printStackTrace();	
		}
	}

	/**
	 * Helper routine to get a Vertex (Position) from a string naming the vertex
	 * Modified by Thais Bardini on November 19th, 2017 (tbard069@uottawa.ca) 
	 */
	public Vertex<Integer> getVertex(Integer vert) throws Exception {
		for (Vertex<Integer> v : sGraph.vertices()) {
			if (v.getElement().equals(vert)) {
				return v;
			}
		}
		throw new Exception("Vertex not in graph: " + vert);
	}


	/**
	 * Print the shortest distances
	 * Modified by Thais Bardini on November 19th, 2017 (tbard069@uottawa.ca)
	 * @throws Exception 
	 */
	public void printAll(Integer start, Integer fin, Integer broke) throws Exception {
		
		if (fin == -1 && broke == -1) {

			System.out.println("Printing all stations on the same line as station " + start + ":");

			Vertex<Integer> vSource = getVertex(start);
			GraphAlgorithms dj = new GraphAlgorithms();
			dj.DFSComplete(sGraph, vSource, stationsInOrder);

			System.out.println();		
		}
		else if (broke == -1) { 

			System.out.println("Printing shortest path from station " + start + " to station " + fin + ":");

			Vertex<Integer> vSource = getVertex(start);
			Vertex<Integer> vGoal = getVertex(fin);

			GraphAlgorithms dj = new GraphAlgorithms();
			Map<Vertex<Integer>, Integer> result = dj.shortestPathLengths(sGraph, vSource, vGoal);

			List<Integer> interV = dj.verticesInShortestPath(sGraph, vSource, vGoal);

			for (Integer i : interV) {
				System.out.print(i + " ");
			}
			
			System.out.println("\n\nTotal travel time: " + result.get(vGoal) + " seconds.");
			System.out.println("Total travel time is around: " + Math.round((float)result.get(vGoal)/60) + " minutes.\n");
		}
		else {

			System.out.println("Printing shortest path from station " + start + " to station " + fin + " when the line containing station " + broke + " is closed:");
			
			// Vertex<Integer> vBroke = getVertex(broke);

			// for (Edge<Integer> e : sGraph.outgoingEdges(vBroke)) {
				
			// 	if (e.getElement() != 90) {
			// 		Vertex<Integer> vb = sGraph.opposite(vBroke, e);

			// 		if (vb != null) {
			// 			if (sGraph.inDegree(vb) <= 2 && sGraph.inDegree(vBroke) <= 2) {
			// 				sGraph.removeVertex(vBroke);
			// 				vBroke = vb;
			// 			}
			// 			else {
			// 				vBroke = vb;
			// 			}	
			// 		}
			// 	}
			// }

			Vertex<Integer> vSource = getVertex(start);
			Vertex<Integer> vGoal = getVertex(fin);

			GraphAlgorithms dj = new GraphAlgorithms();
			Map<Vertex<Integer>, Integer> result = dj.shortestPathLengths(sGraph, vSource, vGoal);

			List<Integer> interV = dj.verticesInShortestPath(sGraph, vSource, vGoal);

			for (Integer i : interV) {
				System.out.print(i + " ");
			}
			
			System.out.println("\n\nTotal travel time: " + result.get(vGoal) + " seconds.");
			System.out.println("Total travel time is around: " + Math.round((float)result.get(vGoal)/60) + " minutes.\n");
		}
	}


	public static void main(String[] args) {

		if (args.length == 1) {

			System.out.print("\nOne INPUT: ");

			System.out.println("N1 = " + args[0] + "\n");

			Integer N1 = new Integer(args[0]);

			try {
				ParisMetro pM = new ParisMetro(N1);
			}
			catch (Exception except) {
				System.err.println(except);
				except.printStackTrace();
			}
		} 
		else if (args.length == 2) {

			System.out.print("\nTwo INPUTS: ");

			System.out.println("N1 = " + args[0] + "  N2 = " + args[1] + "\n");

			Integer N1 = new Integer(args[0]);
			Integer N2 = new Integer(args[1]);

			try {
				ParisMetro pM = new ParisMetro(N1, N2);
			}
			catch (Exception except) {
				System.err.println(except);
				except.printStackTrace();
			}
		}
		else if (args.length == 3) {

			System.out.print("\nThree INPUTS: ");

			System.out.println("N1 = " + args[0] + "  N2 = " + args[1] + "  N3 = " + args[2] + "\n");

			Integer N1 = new Integer(args[0]);
			Integer N2 = new Integer(args[1]);
			Integer N3 = new Integer(args[2]);

			try {
				ParisMetro pM = new ParisMetro(N1, N2, N3);
			}
			catch (Exception except) {
				System.err.println(except);
				except.printStackTrace();
			}
		}
	}
}
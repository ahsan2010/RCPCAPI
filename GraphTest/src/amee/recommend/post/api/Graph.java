package graph.scc;

import java.util.Vector;

public class Graph {

	private Vector<Node> vertices; 
	private int size; 

	public Graph(int size) {
		this.size = size;
		vertices = new Vector<Node>();
	}

	public int getSize(){
		return this.size;
	}
	public Vector<Node> getVertices(){
		return this.vertices;
	}
	
	
	public class Node {
		int name;
		Vector<Neighbour> neighbourList;		
		int cost;
		int discTime;  // Numerator
		int lowTime;  // Denominator
		boolean visited;
		Node(int name, int cost) {
			this.name = name;
			this.cost = cost;
			neighbourList = new Vector<Neighbour>();
		}
		
	}

	public class Neighbour {
		int index;
		int weight;

		public Neighbour(int index, int weight) {
			this.index = index;
			this.weight = weight;
		}
	}
	

	public Vector<Neighbour> getNeighbours(Node n){
		return vertices.elementAt(n.name).neighbourList;
	}

	public void addNode(int name, int cost) {
		vertices.add(new Node(name, cost));
	}

	public void addEdge(int sourceName, int destinationName, int weight) {
		Node source = vertices.get(sourceName);
		source.neighbourList.add(new Neighbour(destinationName, weight));
	}

	public void printState() {
		for (int i = 0; i < vertices.size(); i++) {
			Node u = vertices.get(i);
			Vector<Neighbour> temp = u.neighbourList;
			
			for(Neighbour nbour : temp){
				Node v = vertices.get(nbour.index);
				System.out.println("name:" + u.name + ",cost:" + u.cost + " is connected with " + "name:" + v.name
						+ ",cost:" + v.cost + " with edge-weight as " + nbour.weight);
				
			}
		
		}
	}
	
	public static void main( String arg[]){
		Graph g = new Graph(3);
		g.addNode(1,1);
		g.addNode(2,1);
		g.addNode(3,1);
		
		g.addEdge(1, 2, 2);
		g.addEdge(2,3, 3);
		g.addEdge(3, 1, 4);
		
		g.printState();
		
	}
	
	
}
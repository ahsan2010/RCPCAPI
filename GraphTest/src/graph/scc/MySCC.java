package graph.scc;

import java.util.Stack;
import java.util.Vector;

import cc.mallet.pipe.SourceLocation2TokenSequence;
import graph.scc.Graph.Neighbour;
import graph.scc.Graph.Node;

public class MySCC {
	
	public static int index = 0;
	public Stack<Integer> stack;
    public static Vector<Vector<Integer>> set;
   
    
	public MySCC(){
		stack = new Stack<Integer>();
		set = new Vector<Vector<Integer>>();
	}
	
	public void calculateSCC(Graph g){
        Vector<Node> nodes = g.getVertices();
        for(int i = 0; i < nodes.size(); i++) {
            Node node = nodes.elementAt(i);
             
            if(!node.visited)
                strongConnect(g, node);
        }
	}
	
	public void strongConnect(Graph g , Node node){
		
		node.discTime = index;
        node.lowTime = index;
        index++;
        //System.out.println("Name;" );
        stack.push(node.name);
		node.visited = true;
	
        Vector<Neighbour> neighborList = g.getNeighbours(node);
        
        for(Neighbour neighbour: neighborList){        	
        	Node nv = g.getVertices().elementAt(neighbour.index);
        	if(!nv.visited){        		
                strongConnect(g, nv);
                node.lowTime = Math.min(node.lowTime, nv.lowTime);

        	}else if(stack.contains(nv.name)){
                node.lowTime = Math.min(node.lowTime,nv.lowTime);
        	}
        }
        if(node.lowTime == node.discTime) {
            Vector<Integer> subset = new Vector<Integer>();
            int neighborIndex = -1;
             
            while(node.name != neighborIndex) {
            	neighborIndex = stack.pop();
                subset.add(neighborIndex);
            }             
            set.add(subset);
        }
        
        
	}
	
	public void printSCC(){
		for(int i = 0 ; i < set.size() ; i ++ ){
			System.out.println("SCC: " + i);
			for(int j : set.elementAt(i)){
				System.out.println(j);
			}
			System.out.println("---------------------");
		}
	}
	
	
	public Vector<Integer> getMaxSCC(){
		Vector<Integer> maxScc = null;
		int maxi = -1;
		for(Vector<Integer> v : set){
			if(v.size() > maxi){
				maxi = v.size();
				maxScc = v;
			}
		}
		
		return maxScc;
	}
	
	public static void main ( String arg[] ){
		MySCC s = new MySCC();
		Graph g = new Graph(3);
		g.addNode(0,1);
		g.addNode(1,1);
		g.addNode(2,1);
		g.addNode(3,1);
		g.addNode(4,1);
		g.addNode(5,1);
		g.addNode(6,1);
		g.addNode(7,1);
	
		g.addEdge(1, 0, 1);
		g.addEdge(0, 2, 1);
		g.addEdge(2, 6, 1);
		g.addEdge(6,1, 1);
		g.addEdge(2, 3, 1);
		g.addEdge(3, 4, 1);
		g.addEdge(3, 5, 1);
		g.addEdge(5, 3, 1);
		g.addEdge(5, 6, 1);
		g.addEdge(7, 5, 1);
		
		g.printState();
		s.calculateSCC(g);
		s.printSCC();
		
	}
	
	
}

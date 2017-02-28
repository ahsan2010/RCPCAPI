package graph.scc;

import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.GabowStrongConnectivityInspector;
import org.jgrapht.alg.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedSubgraph;

class MyGraph {

	DirectedGraph<String, DefaultEdge> g = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
	public int vertices;
	
	
	public void addVertex(String name) {
		g.addVertex(name);
	}

	public void addEdge(String v1, String v2) {
		g.addEdge(v1, v2);
	}

	public DirectedGraph<String, DefaultEdge> getGraph() {
		return g;
	}

	public List<Set<String>> getStronlyComponent() {
		//StrongConnectivityAlgorithm<String, DefaultEdge> scAlg = new GabowStrongConnectivityInspector<>(g);
		StrongConnectivityAlgorithm<String, DefaultEdge> scAlg = new KosarajuStrongConnectivityInspector<>(g);
		List<DirectedSubgraph<String, DefaultEdge>> stronglyConnectedSubgraphs = scAlg.stronglyConnectedSubgraphs();

		return scAlg.stronglyConnectedSets();

	}

	public static void main(String arg[]) {
		MyGraph graph = new MyGraph();
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.addVertex("v4");

		graph.addEdge("v1", "v2");
		graph.addEdge("v2", "v3");
		graph.addEdge("v3", "v1");
		graph.addEdge("v2", "v4");

		List<Set<String>> comp = graph.getStronlyComponent();
		for (int i = 0; i < comp.size(); i++) {
			for (String s : comp.get(i)) {
				System.out.print(s + " ");
			}
			System.out.println(" ");
		}

	}
}

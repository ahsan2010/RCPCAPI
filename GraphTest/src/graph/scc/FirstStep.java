package graph.scc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.derby.tools.sysinfo;

public class FirstStep {

	ArrayList<String> expertUsers = new ArrayList<String>();
	
	Map<String,ArrayList<String>> userPosts = new HashMap<String,ArrayList<String>>();
	Map<String,SOPost> posts;
	UserMap pMap;
	MyGraph graph = new MyGraph();
	Graph g;
	public FirstStep(UserMap pMap){
		this.posts = pMap.getPosts();
		this.pMap = pMap;
	}
	
	public ArrayList<String> getUserList(){		
		return this.expertUsers;		
	}
	
	public void generateMyGraph(){
		int total = 0 ;
		for(String s : pMap.users){
			if(s == null) continue;
			//System.out.println(s);
			//System.out.println(++total +" " + s);
			graph.addVertex(s);
		}
		
		System.out.println("Vertex Adding Complete: " + graph.g.vertexSet().size());
		int edge= 0;
		for(Map.Entry<String,ArrayList<String>> u : pMap.userAnswers.entrySet()){
			String answererId = u.getKey();
			if(answererId == null) continue;
			for(String pid : u.getValue()){
				String questionerId = pMap.posts.get(pid).getUserId();
				if(questionerId == null)continue;
				//System.out.println(questionerId +" " + answererId);
				graph.addEdge(questionerId,answererId);
				System.out.println("Edge: " + (++edge));
			}
		}
		System.out.println("Graph Creation Complete");
		System.out.println("Total Vertex: " + graph.g.vertexSet().size());
		System.out.println("Total Edges: " + graph.g.edgeSet().size());
	}
	
	public void generateGraph(){
		
		// Add Nodes
		for(int i = 0 ; i < pMap.totalUser + 1 ; i ++ )
			g.addNode(i,1);
		
		// Add Edges
		for(Map.Entry<String,ArrayList<String>> u : pMap.userAnswers.entrySet()){
				int answererId = pMap.userToNum.get(u.getKey());
				for(String pid : u.getValue()){
					int questionerId = pMap.userToNum.get(pMap.posts.get(pid).getUserId());
					g.addEdge(answererId,questionerId, 1);
				}
		}
		
		//g.printState();
		
		
	}
	
	/*public void selectExpertUsers(){
		MySCC scc = new MySCC();
		scc.calculateSCC(g);
		
		for(Integer id : scc.getMaxSCC()){
			expertUsers.add(pMap.numToUser.get(id));
		}
		
		System.out.println("Total: " + pMap.users.size());
		System.out.println("BowTie: " + expertUsers.size() );
	}*/
	
	public void selectExpertUsers(){
		
		List<Set<String>> comp = graph.getStronlyComponent(); 
		Set <String> maxiComp = null;
		int maxi = -1;
		for(int i = 0 ; i < comp.size() ; i ++ ){
			if(comp.get(i).size()>maxi){
				maxi = comp.get(i).size();
				maxiComp = comp.get(i);
			}
		}
		
		for(String id : maxiComp){
			expertUsers.add(id);
		}
		
		System.out.println("Total: " + pMap.users.size());
		System.out.println("BowTie: " + expertUsers.size() );
	}
	
	public void printExpertUsers(){
		System.out.println("Total Expert Users: " + expertUsers.size());
		for(String id : expertUsers){
			System.out.println(id);
		}
	}
	
	public void showExpertUsers(){
		System.out.println("Expert Users");
		System.out.println("----------------------------");
		for(String uid : expertUsers){
			System.out.println(uid);
		}
		
	}
	
	public ArrayList<String> getExpertUsers(){
		return this.expertUsers;
	}
	
	
}

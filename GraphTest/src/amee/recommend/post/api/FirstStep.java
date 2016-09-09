package graph.scc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.derby.tools.sysinfo;

public class FirstStep {

	ArrayList<String> expertUsers = new ArrayList<String>();
	
	Map<String,ArrayList<String>> userPosts = new HashMap<String,ArrayList<String>>();
	Map<String,SOPost> posts;
	UserMap pMap;
	Graph g;
	
	public FirstStep(UserMap pMap){
		this.posts = pMap.getPosts();
		this.pMap = pMap;
	}
	
	public ArrayList<String> getUserList(){		
		return this.expertUsers;		
	}
	
	public void generateGraph(){
		g = new Graph(pMap.totalUser + 1);
		// Add Nodes
		for(int i = 0 ; i < pMap.totalUser + 1 ; i ++ )
			g.addNode(i,1);
		
		// Add Edges
		for(Map.Entry<String,ArrayList<String>> u : pMap.userAnswers.entrySet()){
				int answererId = pMap.userToNum.get(u.getKey());
				for(String pid : u.getValue()){
					int questionerId = pMap.userToNum.get(pMap.posts.get(pid).getUserId());
					g.addEdge(questionerId, answererId, 1);
				}
		}
		
		//g.printState();
		
		
	}
	
	public void selectExpertUsers(){
		MySCC scc = new MySCC();
		scc.calculateSCC(g);
		
		for(Integer id : scc.getMaxSCC()){
			expertUsers.add(pMap.numToUser.get(id));
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

package graph.scc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

	Map<String,SOPost> posts;

	public void RecommndPostApiIssues(){
		
		UserMap umap = new UserMap("android");
		umap.readPost();
		umap.mineUser();
		umap.mapPost();
		System.out.println("User Read Complete");
		FirstStep step1 = new FirstStep(umap);
		step1.generateMyGraph();
		step1.selectExpertUsers();
		//step1.showExpertUsers();
		System.out.println("Total Users: " + step1.getExpertUsers().size());
		
		
		SecondStep step2 = new SecondStep(step1,umap);
		step2.findExpertPosts();
		step2.dimensionReduction();
		
		ThirdStep step3 = new ThirdStep(step1, step2, umap);
		step3.quesitonSelection();
		//step3.printSortedQuestion();
		step3.doControlChart();
		//step3.printSelectedQuesiton();
		//umap.showuserToNum();
		ForthStep step4 = new ForthStep(step1, step2, step3, umap);
		step4.calculateScore();
		step4.showTopQuestions();
		
		//compareResult(step4);
		System.out.println(umap.maxiAnswers +" " + umap.maxiComments +" " + umap.maxiFav);
		
	}
	
	public void compareResult(ForthStep s){
		
		ArrayList<String> postIds = Preprocessing.readIssueId("neo4j");
		Set<String> ids = new HashSet<String>(postIds);
		ArrayList<String> selectQuestions = s.getSelectedQuestions();
		double total = 0;
		System.out.println("----------------------------------");
		for(String st : selectQuestions){
			String temp = st.trim();
			if(ids.contains(temp)){
				total ++ ;
				System.out.println(temp);
			}
		}
		
		System.out.println("Total Selected: " + ids.size());
		System.out.println("Total Find: " + total);
		
	}
	
	public static void main (String arg[]){
		new Main().RecommndPostApiIssues();
	}
	
}

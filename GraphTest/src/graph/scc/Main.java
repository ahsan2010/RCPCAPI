package graph.scc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

	Map<String,SOPost> posts;

	public void RecommndPostApiIssues(){
		
		UserMap umap = new UserMap("neo4j");
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
		
		System.out.println("Finish Step2");
		
		ThirdStep step3 = new ThirdStep(step1, step2, umap);
		step3.quesitonSelection();
		//step3.printSortedQuestion();
		step3.doControlChart();
		//step3.printSelectedQuesiton();
		//umap.showuserToNum();
		
		System.out.println("Finish Step3");
		
		ForthStep step4 = new ForthStep(step1, step2, step3, umap);
		step4.calculateScore();
		System.out.println("Finish Step4");
		
		step4.showTopQuestions();
		step4.getAccuray();
		//compareResult(step4);
		
		System.out.println(umap.maxiAnswers +" " + umap.maxiComments +" " + umap.maxiFav);
		System.out.println("Complete.");
	}
	
	public void compareResult(ForthStep s){
		
		Set<String> postIds = Preprocessing.readIssueId("neo4j");
		ArrayList<String> selectQuestions = s.getSelectedQuestions();
		double total = 0;
		System.out.println("----------------------------------");
		for(String st : selectQuestions){
			String temp = st.trim();
			if(postIds.contains(temp)){
				total ++ ;
				System.out.println(temp);
			}
		}
		
		System.out.println("Total Selected: " + postIds.size());
		System.out.println("Total Find: " + total);
		
	}
	
	public static void main (String arg[]){
		new Main().RecommndPostApiIssues();
	}
	
}

package graph.scc;

import java.util.HashMap;
import java.util.Map;

public class Main {

	Map<String,SOPost> posts;

	public void RecommndPostApiIssues(){
		
		UserMap umap = new UserMap("android");
		umap.readPost();
		umap.mineUser();
		umap.mapPost();
		FirstStep step1 = new FirstStep(umap);
		step1.generateGraph();
		step1.selectExpertUsers();
		//step1.showExpertUsers();
		
		SecondStep step2 = new SecondStep(step1,umap);
		step2.findExpertPosts();
		step2.dimensionReduction();
		
		ThirdStep step3 = new ThirdStep(step1, step2, umap);
		step3.quesitonSelection();
		//step3.printSortedQuestion();
		step3.doControlChart();
		//umap.showuserToNum();
		
		
	}
	
	public static void main (String arg[]){
		new Main().RecommndPostApiIssues();
	}
	
}

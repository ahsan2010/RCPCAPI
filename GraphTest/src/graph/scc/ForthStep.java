package graph.scc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ForthStep {

	ThirdStep step3;
	SecondStep step2;
	FirstStep step1;
	UserMap umap;
	
	
	Map<String,Double> userResoRate = new HashMap<String,Double>();
	Map<String,Double> postScore = new HashMap<String,Double>();
	//Map<String,Double> ps = new HashMap<String,Double>();
	//Map<String,Double> ms = new HashMap<String,Double>();
	double maxPs = 0;
	double maxMs = 0;
	
	public ForthStep(FirstStep step1, SecondStep step2, ThirdStep step3, UserMap umap){
		this.step1 = step1;
		this.step2 = step2;
		this.step3 = step3;
		this.umap = umap;
	}
	
	public int countAcceptAnswers(String uid){
		int acc = 0;
		if(umap.userAnswers.containsKey(uid)){
			ArrayList<String> uanswer = umap.answerIdByUser.get(uid);
			for(String aid : uanswer){
				if(umap.posts.containsKey(aid)){
					
					String parentId = umap.posts.get(aid).parentPostId;
					
					//System.out.println(aid + " " + parentId);
					if(umap.posts.containsKey(parentId)){
						if(!umap.posts.get(parentId).getAccAnswerId().trim().equals(aid)){
							acc++;
						}
					}
				}
			}
		}
		
		
		return acc;
	}
	
	public void resolutionRate(){
		for(String uid : step1.expertUsers){
			int acc = countAcceptAnswers(uid);
			
			userResoRate.put(uid, (double)acc/umap.answerIdByUser.get(uid).size());
			//System.out.println(uid + " " + userResoRate.get(uid) +" " + acc +" " + umap.answerIdByUser.get(uid).size());
			
		}
	}
	
	public void calculateScore(){
		
		resolutionRate();
		
		for(String id : step3.selectedQuestion){
			String uid = umap.posts.get(id).getUserId();
			double ps = calculatePostScore(id);
			double ms = 0;
			if(umap.postAnswerers.containsKey(uid)){
				
				for(String users : umap.postAnswerers.get(uid)){
					if(userResoRate.containsKey(users)){
						ms += userResoRate.get(users);
					}
				}
			}
			double score = ps + ms;
		//	System.out.println(score);
			postScore.put(id,(ps+ms));
			
		}
		
		
		Collections.sort(step3.selectedQuestion, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				
				if(postScore.get(o1) > postScore.get(o2)){
					return -1;
				}
				else if(postScore.get(o2) > postScore.get(o1)){
					return 1;
				}
				return 0;
			 
			}
			
		});
		
		
	}
	public double calculatePostScore(String id){
		double pscore = 0;
		
		if(umap.posts.containsKey(id)){
			
			SOPost p  = umap.posts.get(id);
			
			pscore = p.getAnswersCount()/umap.maxiAnswers + p.getCommentsCount()/umap.maxiComments + p.getFavoriteCount()/umap.maxiFav;
			
		}
		
		
		return pscore;
		
	}
	public void showTopQuestions(){
		
		int size = step3.selectedQuestion.size() < 200 ? step3.selectedQuestion.size() : 200;
		for(int i = 0 ; i < size ; i ++ ){
			System.out.println(step3.selectedQuestion.get(i) +" " + postScore.get(step3.selectedQuestion.get(i)));
		}
		
	}
	
	public ArrayList<String> getSelectedQuestions(){
		return step3.selectedQuestion;
	}
	
	
	
}

package graph.scc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ForthStep {

	ThirdStep step3;
	SecondStep step2;
	FirstStep step1;
	UserMap umap;
	
	double maxiUpVotes = -1;	
	double maxiDownVotes = -1;
	
	Map<String,Votes> votes = null;
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
	
	public void calculateMaxiVotes(Map<String,Votes> votes){
		
		for(Map.Entry<String, Votes> v : votes.entrySet()){
			Votes tempVotes = v.getValue();
			if(maxiUpVotes < tempVotes.getUpVote()){
				maxiUpVotes = tempVotes.getUpVote();
			}
			else if(maxiDownVotes < tempVotes.getDownVote()){
				maxiDownVotes = tempVotes.getDownVote();
			}
		}
		
	}
	
	public void calculateScore(){
		
		resolutionRate();
		
		votes = Preprocessing.loadVotesData();
		
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
			
			double upVotes = 0.0;
			double downVotes = 0.0;
			
			if(votes.containsKey(p.getPostId())){
				upVotes = votes.get(p.getPostId()).getUpVote();
				downVotes = votes.get(p.getPostId()).getDownVote();
			}
			
			pscore = 15*upVotes/maxiUpVotes + 125*downVotes/maxiDownVotes + p.getAnswersCount()/umap.maxiAnswers + 50*p.getCommentsCount()/umap.maxiComments + p.getFavoriteCount()/umap.maxiFav;
			
		}
		
		
		return pscore;
		
	}
	public void showTopQuestions(){
		
		int size = step3.selectedQuestion.size() < 200 ? step3.selectedQuestion.size() : 200;
		for(int i = 0 ; i < size ; i ++ ){
			System.out.println(step3.selectedQuestion.get(i) +" " + postScore.get(step3.selectedQuestion.get(i)));
		}
		
	}
	
	public void getAccuray(){
		double  totalSelected = 0;
		Set<String> issueIds = Preprocessing.readIssueId("neo4j");
		int size = step3.selectedQuestion.size() < 200 ? step3.selectedQuestion.size() : 200;
		for(int i = 0 ; i < size ; i ++ ){
			String postId = step3.selectedQuestion.get(i).trim();
			if(issueIds.contains(postId)){
				totalSelected ++ ;
				System.out.println(postId);
			}
		}
		System.out.println("---------------------------------");
		System.out.println("Accuracy : " + totalSelected/200 +" " + totalSelected);
	}
	
	public ArrayList<String> getSelectedQuestions(){
		return step3.selectedQuestion;
	}
	
	
	
}

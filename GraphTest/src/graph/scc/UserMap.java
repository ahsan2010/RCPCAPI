package graph.scc;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.crypto.Data;

public class UserMap {

	public Map<String,Integer> userToNum;
	public Map<Integer,String> numToUser;
	public Map<String,SOPost>posts;
	ArrayList<String> users = new ArrayList<String>();
	
	Map<String,ArrayList<String>> postAnswerers = new HashMap<String,ArrayList<String>>();
	Map<String,ArrayList<String>> userAnswers = new HashMap<String,ArrayList<String>>();
	Map<String,ArrayList<String>> userQuestions = new HashMap<String,ArrayList<String>>();
	Map<String,ArrayList<String>> answerIdByUser = new HashMap<String,ArrayList<String>>();
	public int totalUser;
	String tag;
	
	double maxiAnswers;
	double maxiView;
	double maxiFav;
	double maxiComments;
	
	
	public UserMap(String tag){
		userToNum = new HashMap<String,Integer>();
		numToUser = new HashMap<Integer,String>();
		totalUser = 0;
		this.tag = tag;
	}
	public Map<String,SOPost> getPosts(){
		return this.posts;
	}
	
	public void mineUser(){
		
		for(Map.Entry<String, SOPost> p : posts.entrySet() ){
			SOPost post = p.getValue();
			users.add(p.getValue().getUserId());
			if(!post.isQuestion){
				
				if(!postAnswerers.containsKey(post.getParentPostId())){
					ArrayList<String> uid = new ArrayList<String>();
					
					if(posts.containsKey(post.getParentPostId())){
						
						uid.add(post.getUserId());
						postAnswerers.put(post.getParentPostId(),uid);
						
					}
				}else{
					postAnswerers.get(post.getParentPostId()).add(post.getUserId());
				}
				
				if(!answerIdByUser.containsKey(post.getUserId())){
					ArrayList<String> aid = new ArrayList<String>();
					aid.add(post.getPostId());
					answerIdByUser.put(post.getUserId(), aid);
				}else{
					answerIdByUser.get(post.getUserId()).add(post.getPostId());
				}
				
				if(!userAnswers.containsKey(post.getUserId())){
					ArrayList<String> ansQuesId = new ArrayList<String>();
					ansQuesId.add(post.getParentPostId());
					userAnswers.put(post.getUserId(), ansQuesId);
				}else{
					userAnswers.get(post.getUserId()).add(post.getParentPostId());
				}
			}else{
				
				if(maxiFav < post.getFavoriteCount()){
					maxiFav = post.getFavoriteCount();
				}
				
				if(maxiAnswers < post.getAnswersCount()){
					maxiAnswers = post.getAnswersCount();
				}
				if(maxiComments < post.getCommentsCount()){
					maxiComments = post.getCommentsCount();
				}				
				
				if(!userQuestions.containsKey(post.getUserId())){
					ArrayList<String> askQuesId = new ArrayList<String>();
					askQuesId.add(post.getPostId());
					userQuestions.put(post.getUserId(), askQuesId);
				}else{
					userQuestions.get(post.getUserId()).add(post.getPostId());
				}
			}
		}
		
	}
	
	public void readPost() {

		long start = System.currentTimeMillis();

		File f = new File(Properties.post_ser_file);
		if (!(f.exists() && !f.isDirectory())) {
			try {
				System.err.println("File Not Found.. Generating...");
				new Parser().ParseXml(tag);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			FileInputStream fileIn = null;
			fileIn = new FileInputStream(Properties.post_ser_file);

			System.out.println("FileReading...");

			ObjectInputStream in = new ObjectInputStream(fileIn);
			posts = (Map<String, SOPost>) in.readObject();

			long end = System.currentTimeMillis();
			System.out.println("[Loading Posts data takes: " + (end - start) + " ms]");
			System.out.println();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void mapPost(){
		int id=0;
		for(Map.Entry<String, SOPost> p : posts.entrySet()){
			SOPost post = p.getValue();
			
			if(!userToNum.containsKey(post.getUserId())){
				userToNum.put(post.getUserId(), id);
				numToUser.put(id,post.getUserId());
				id = id + 1;
			}
	
		}
		totalUser = id;  // Total Number of Posts;
	
	}
	
	public void showuserToNum(){
		System.out.println("Map Info Post to Num Total " + totalUser );
		System.err.println("-----------------------------");
		for(Map.Entry<String, Integer> m : userToNum.entrySet()){
			System.out.println(m.getKey() + " --> " + m.getValue());
		}
		System.out.println("Total " + totalUser);
	}
	
	public void shownumToUser(){
		System.out.println("Map Info Number to Post Total " + totalUser);
		System.err.println("-----------------------------");
		for(Map.Entry<Integer, String> m : numToUser.entrySet()){
			System.out.println(m.getKey() + " --> " + m.getValue());
		}
		System.out.println("Total " + totalUser);

	}

}

package graph.scc;

import java.io.Serializable;

public class Votes implements Serializable{

	public String id;
	public String postId;
	public int voteTypeId;
	public String userId;
	public String creationDate;
	public int bountyAmount;
	public int upVote;
	public int downVote;
	
	
	
	public int getUpVote() {
		return upVote;
	}
	public void setUpVote(int upVote) {
		this.upVote = upVote;
	}
	public int getDownVote() {
		return downVote;
	}
	public void setDownVote(int downVote) {
		this.downVote = downVote;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		if(id != null ) this.id = id;
		else this.id = " ";
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		if(postId != null)
			this.postId = postId;
		else this.postId = " ";
	}
	public int getVoteTypeId() {
		return voteTypeId;
	}
	public void setVoteTypeId(String voteTypeId) {
		if(voteTypeId != null){
			this.voteTypeId = Integer.parseInt(voteTypeId);
		}
		else this.voteTypeId = -1;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		if(userId != null){
			this.userId = userId;
		}
		else {
			this.userId = null;
		}
		
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public int getBountyAmount() {
		return bountyAmount;
	}
	public void setBountyAmount(String bountyAmount) {
		if(bountyAmount != null){
			this.bountyAmount = Integer.parseInt(bountyAmount);
		}
		else this.bountyAmount = -1;
	}
	
	public void increaseUpVote(){
		this.upVote ++;
	}
	public void increaseDownVote(){
		this.downVote ++;
	}
	
}

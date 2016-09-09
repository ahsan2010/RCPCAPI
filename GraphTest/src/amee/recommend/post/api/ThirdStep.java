package graph.scc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ThirdStep {

	FirstStep step1;
	SecondStep step2;
	UserMap umap;
	
	ArrayList<String> selectedQuestion = new ArrayList<String>();
	ArrayList<String> tempQues = new ArrayList<String>();
	ArrayList<TempQuestion> quesSort = new ArrayList<TempQuestion>();
	
	
	public ThirdStep(FirstStep step1, SecondStep step2, UserMap umap){
		this.step1 = step1;
		this.step2 = step2;
		this.umap = umap;
	}
	
	public void quesitonSelection(){
		
		final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-mm-dd HH:mm:ss");
		
		
		for(String id : step2.questionLdaFilter){
			
			if(umap.posts.get(id).getAccAnswerId().compareToIgnoreCase("-1")!= 0){
				
				String accId = umap.posts.get(id).getAccAnswerId();
				if(umap.posts.containsKey(accId)){
					String accTime = umap.posts.get(accId).getCreationDate();
					accTime = accTime.replace("T"," ");
					int i1 = accTime.lastIndexOf(".");
					if(i1 > 0 ){
						accTime = accTime.substring(0, i1);
					}
					TempQuestion t = new TempQuestion(id,accId,accTime);
					quesSort.add(t);
				}
				
				
			}
			
			
			
		}
		System.out.println("Total ACC Questions: " + quesSort.size());
		Collections.sort(quesSort, new Comparator<TempQuestion>() {

			@Override
			public int compare(TempQuestion o1, TempQuestion o2) {
				
				DateTime d1 = dtf.parseDateTime(o1.accDate);
				DateTime d2 = dtf.parseDateTime(o2.accDate);
				
				
				return d1.compareTo(d2);
			}
			
		});
		
		
	}
	
	
	public void doControlChart(){
		
		
		int ucl = (int)Math.round(0.9 * quesSort.size());
		int lcl = (int)Math.round(0.1 * quesSort.size());
		int cl = (int) Math.round(0.5 * quesSort.size());
		
		System.out.println("LCL : " + lcl);
		System.out.println("CL : " + cl);
		System.out.println("UCL : " + ucl);
		
		for(int i = lcl ; i <= ucl ; i ++ ){
			selectedQuestion.add(quesSort.get(i).questionId);
		}
		
		System.out.println("Total Quseiton After CC : " + selectedQuestion.size());
		
	}
	
	
	public void printSortedQuestion(){
		for(TempQuestion t : quesSort){
			System.out.println("ID: " + t.questionId +" "+t.accDate);
		}
	}
	
	
	
}

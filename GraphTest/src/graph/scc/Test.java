package graph.scc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Test {

	public static void dateTest(){
		String d1 = "2008-12-01 16:08:52";
		String d2 = "2008-08-01 16:08:52.333";
		
		String s= d2.substring(0,d2.lastIndexOf("."));
		System.out.println(s);
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime jodatime = dtf.parseDateTime(d1);
		DateTime jodatime2 = dtf.parseDateTime("2009-12-09 16:08:52");
		DateTime jodatime3 = dtf.parseDateTime("2009-08-01 19:08:52");

		System.out.println(jodatime.toString());
		
		
		ArrayList<DateTime> tm = new ArrayList<DateTime>();
		tm.add(jodatime3);
		tm.add(jodatime2);
		tm.add(jodatime);
		
		
		Collections.sort(tm,new Comparator<DateTime>() {

			@Override
			public int compare(DateTime o1, DateTime o2) {
				return o1.compareTo(o2);
			}
			
		});
		
		DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MM/dd/yyyy");
		
		for(DateTime d : tm){
			System.out.println(dtfOut.print(d));
		}
		
		
	}
	public static void main (String arg[]){
		
		
		try {
			//Parser p = new Parser();
			//p.ParseVoteXML();
			
		Map<String,Votes> votes = Preprocessing.loadVotesData();
		
		for(Map.Entry<String,Votes> v : votes.entrySet()){
			System.out.println(v.getValue().getPostId()+" "+v.getValue().getUpVote() +" " + v.getValue().getDownVote());
		}
		
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}
	
}

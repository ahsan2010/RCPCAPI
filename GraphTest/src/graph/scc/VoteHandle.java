package graph.scc;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.opencsv.CSVWriter;

public class VoteHandle extends DefaultHandler {

	private XMLReader xmlReader;
	boolean triggerEnd = true;
	boolean trigger = true;
	private int total = 0;
	private String lang;
	public int totalVote = 0;

	Map<String, Votes> votes = new HashMap<String, Votes>();

	CSVWriter writer;

	public VoteHandle(XMLReader xmlReader) throws Exception {

		this.xmlReader = xmlReader;
	}

	public void ReadUsers(String sName, String qName, Attributes atts, boolean pf) throws SAXException {
		try {

			if (qName.equalsIgnoreCase("row")) {

				String postId = atts.getValue("PostId");

				if (postId != null) {

					if (votes.containsKey(postId)) {
						Votes v = votes.get(postId);
						String voteTypeId = atts.getValue("VoteTypeId");
						if (voteTypeId.trim().equals("2")) {
							v.increaseUpVote();
						} else if (voteTypeId.trim().equals("3")) {
							v.increaseDownVote();
						}

					} else {
						Votes v = new Votes();
						v.setId(atts.getValue("Id"));
						v.setPostId(atts.getValue("PostId"));
						v.setVoteTypeId(atts.getValue("VoteTypeId"));
						v.setCreationDate(atts.getValue("CreationDate"));
						v.setBountyAmount(atts.getValue("BountyAmount"));

						votes.put(v.getPostId(), v);

					}
				}else{
					System.out.println("Null Post ID " + (++total));
				}
				if(totalVote % 1000 == 0 )
					System.out.println("Vote.. " + atts.getValue("CreationDate") +" " + (++totalVote));

			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Override
	public void endDocument() throws SAXException {
		writeObject();
		super.endDocument();
		System.exit(1);

	}

	public void writeObject() {
		try {
			FileOutputStream fileOut = new FileOutputStream(Properties.votes_ser_file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			System.out.println("Finish Votes Parsing.");
			System.out.println("Total Votes: " + votes.size());
			System.out.println("Do not Close.. Saving the data......");
			out.writeObject(votes);
			out.close();
			fileOut.close();
			System.out.println("Saving Complete.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

		ReadUsers(localName, qName, atts, false);
		// ReadIssuePost(localName, qName, atts, false);
	}

}

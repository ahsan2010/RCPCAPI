package graph.scc;

import com.csvreader.CsvReader;

//import MyNewException;

import com.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import org.tartarus.snowball.SnowballStemmer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

//import com.mysql.jdbc.PreparedStatement;

public class PostHandle extends DefaultHandler {

	private XMLReader xmlReader;
	boolean triggerEnd = true;
	boolean trigger = true;

	private String lang;

	Map<String, Integer> stopList = new HashMap<String, Integer>();
	Map<String, Integer> tagFreq = new HashMap<String, Integer>();
	// Set<String> answers = new HashSet<String>();
	// Set<String> questions = new HashSet<String>();
	Map<String, SOPost> posts = new HashMap<String, SOPost>();
	// posts tempPost = new posts();
	private static final Set<String> DISALLOWED_HTML_TAGS = new HashSet<String>(
			Arrays.asList(HTMLElementName.PRE, HTMLElementName.CODE, HTMLElementName.A, HTMLElementName.LINK));

	private static final String CodeTag = HTMLElementName.PRE;

	CSVWriter writer;

	public PostHandle(XMLReader xmlReader, String lang) throws Exception {

		this.lang = lang;
		this.xmlReader = xmlReader;

		// writer = new CSVWriter(new FileWriter(PathInfo.pathData));
		// writer.writeNext(new String[] { "id", "acceptedanswerId", "title",
		// "body", "tags", "creationDate" });

	}

	private void emit(String s) throws SAXException {

	}

	public void processElement(String sName, String qName, Attributes attrs) throws SAXException {
		{
			triggerEnd = true;
			String eName = sName; // element name
			if ("".equals(eName))
				eName = qName; // not namespace-aware
			emit("<" + eName);
			if (attrs != null) {
				System.out.println(attrs.getLength());
				for (int i = 0; i < attrs.getLength(); i++) {
					String aName = attrs.getLocalName(i); // Attr name
					if ("".equals(aName))
						aName = attrs.getQName(i);
					emit(" ");
					emit(aName + "=\"" + attrs.getValue(i) + "\"");
				}
			}
			emit(">");
		}

	}

	public Set<String> getTagList(String tags, String postId) {
		Set<String> tg = new HashSet<String>();
		StringTokenizer st = new StringTokenizer(tags, " < > -");
		while (st.hasMoreTokens()) {

			String token = st.nextToken().toLowerCase();

			if (token.length() > 0) {
				tg.add(token);
			}

		}
		return tg;
	}

	public String getTag(String tempTag) {

		if (tempTag.trim() == null)
			return null;
		String allTag = tempTag.trim();
		String tags = "";
		String temp = "";
		for (int i = 0; i < allTag.length(); i++) {
			if (allTag.charAt(i) == '<') {
				temp = "";
			} else if (allTag.charAt(i) == '>') {
				tags += temp + " ";
			} else
				temp += allTag.charAt(i);
		}

		return tags;

	}

	boolean checkJava(String att, String key) {

		StringTokenizer st = new StringTokenizer(att, ">");

		while (st.hasMoreTokens()) {
			String tok = st.nextToken();
			if (tok.trim().equalsIgnoreCase("<" + key)) {
				return true;
			}
		}

		return false;
	}

	boolean isCodeExisit(Source source) {
		boolean result = false;

		OutputDocument outputDocument = new OutputDocument(source);
		List<Element> elements = source.getAllElements();

		for (Element element : elements) {
			// System.out.println("Element Name " + element.getName());
			if (CodeTag.equalsIgnoreCase(element.getName())) {
				// System.out.println(element.getName());
				result = true;
				break;
			}
		}

		return result;
	}

	public void ReadPost(String sName, String qName, Attributes atts, boolean pf) throws SAXException {
		try {

			int tId = 0;

			if (qName.equalsIgnoreCase("row")) {

				int year = Integer.parseInt(atts.getValue("CreationDate").substring(0, 4));

				int postTypeId = Integer.parseInt(atts.getValue("PostTypeId"));
				String id = atts.getValue("Id");
				System.out.println(year);

				if (postTypeId == 1 && checkJava(atts.getValue("Tags"), lang)) {
					
					SOPost p = new SOPost();
					p.setPostId(id);
					p.setAccAnswerId(atts.getValue("AcceptedAnswerId"));
					p.setCommentsCount(atts.getValue("CommentCount"));
					p.setFavoriteCount(atts.getValue("FavoriteCount"));
					p.setAnswersCount(atts.getValue("AnswerCount"));
					p.setTitle(atts.getValue("Title"));
					p.setCreationDate(atts.getValue("CreationDate"));
					p.setBody(atts.getValue("Body"));
					p.setTags(atts.getValue("Tags"));
					p.setUserId(atts.getValue("OwnerUserId"));
					p.isQuestion = true;
					posts.put(p.getPostId(), p);
					// questions.add(p.getPostId());
				}
				if (postTypeId == 2) {

					if (posts.containsKey(atts.getValue("ParentId"))) {
						if (checkJava(posts.get(atts.getValue("ParentId")).getTags(), lang)) {

							SOPost p = new SOPost();
							p.setPostId(id);
							p.setParentPostId(atts.getValue("ParentId"));
							p.setCommentsCount(atts.getValue("CommentCount"));
							p.setCreationDate(atts.getValue("CreationDate"));
							p.setBody(atts.getValue("Body"));
							p.isQuestion = false;
							p.setUserId(atts.getValue("OwnerUserId"));
							posts.put(p.getPostId(), p);
							// answers.add(p.getPostId());
						}

					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void ReadTag(String sName, String qName, Attributes atts, boolean pf) throws SAXException {
		try {

			if (qName.equalsIgnoreCase("row")) {

				int year = Integer.parseInt(atts.getValue("CreationDate").substring(0, 4));
				// System.out.println(year);

				int postTypeId = Integer.parseInt(atts.getValue("PostTypeId"));
				String id = atts.getValue("Id");
				System.out.println(year);

				/*
				 * if(year > 2008){ endDocument(); System.exit(0); }
				 */

				if (postTypeId == 1) {

					String tag = atts.getValue("Tags");
					Set<String> data = getTagList(tag, id);

					for (String st : data) {
						if (tagFreq.containsKey(st)) {
							tagFreq.put(st, tagFreq.get(st) + 1);
						} else {
							tagFreq.put(st, 1);
						}

					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public boolean isTagHere(String tags, String key) {
		Set<String> tg = new HashSet<String>();
		StringTokenizer st = new StringTokenizer(tags, " < > ");
		while (st.hasMoreTokens()) {

			String token = st.nextToken().toLowerCase();
			tg.add(token);

		}
		if (tg.contains(key)) {
			return true;
		}
		return false;
	}

	@Override
	public void endDocument() throws SAXException {
		// writeTagInfo();
		// writePostInfo();
		writeObject();
		// writePostInfo();
		System.out.println("Complete Post Reading and Writing..");
		super.endDocument();
		System.exit(1);

	}

	public void writeObject() {
		try {
			FileOutputStream fileOut = new FileOutputStream(Properties.post_ser_file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);

			out.writeObject(posts);
			out.close();
			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writePostInfo() {
		try {
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeTagInfo() {
		try {

			CSVWriter csv = new CSVWriter(new FileWriter("/home/amee/Documents/Serialized_File/tagAnalysis_Corpus"));

			for (Map.Entry<String, Integer> m : tagFreq.entrySet()) {
				String id = m.getKey();
				int freq = m.getValue();

				csv.writeNext(new String[] { id, Integer.toString(freq) });

			}

			csv.close();

		} catch (Exception ex) {

			ex.printStackTrace();
		}
	}

	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

		// ReadAnswer(localName, qName, atts);
		// ReadTag(localName,qName,atts,false);

		ReadPost(localName, qName, atts, false); // true -> create the
													// questioner List
		// readAns(localName,qName, atts); // false -> gather the question..
		// ReadPostForAnswer(localName,qName, atts,false);

	}

	public void endElement(String uri, String localName, String qName) throws SAXException {

	}

	public void characters(char[] ch, int start, int length) throws SAXException {

	}

}

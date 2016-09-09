/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph.scc;

import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 *
 * @author root
 */
public class Parser {
    
    
    
    public void ParseXml (String tag) throws Exception {			
				
		        SAXParserFactory spf = SAXParserFactory.newInstance();
		        SAXParser sp = spf.newSAXParser();
		        XMLReader xr = sp.getXMLReader();		       
		        xr.setContentHandler(new PostHandle(xr,tag));		      
		        try {
		        	xr.parse(Properties.msr_post_file);
		           } catch (SAXException e) {
		            e.printStackTrace();
		        }   
		      
	}
}

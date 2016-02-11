package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {
    private IndexSearcher searcher = null;
    private QueryParser parser = null;
    static final String OLD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    static final String NEW_FORMAT = "MMM-dd-yy HH:mm:ss";
    
    /** Creates a new instance of SearchEngine */
    public AuctionSearch() {
    	try {
	        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene"))));
	        parser = new QueryParser("content", new StandardAnalyzer());
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    }

    public Document getDocument(int docId) {
    	try {
        	return searcher.doc(docId);
    	} catch (Exception e) {
			System.out.println(e);
    	}
    	return null;
    }

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, int numResultsToReturn) {
		TopDocs topDocs = null;
		ScoreDoc[] hits;
		try {
			Query q = parser.parse(query);  
			if (numResultsToSkip > 0) {
				topDocs = searcher.search(q, numResultsToSkip);
				hits = topDocs.scoreDocs;
				topDocs = searcher.searchAfter(hits[numResultsToSkip], q, numResultsToReturn);
			} else {
				topDocs = searcher.search(q, numResultsToReturn);
			}
		} catch (Exception e) {
			System.out.println(e);
		}


		// obtain the ScoreDoc (= documentID, relevanceScore) array from topDocs
		hits = topDocs.scoreDocs;
		// retrieve each matching document from the ScoreDoc arry
		SearchResult[] results = new SearchResult[hits.length];
		for (int i = 0; i < hits.length; i++) {
		    Document doc = getDocument(hits[i].doc);
		    String itemId = doc.get("id");
		    String itemName = doc.get("name");
		    SearchResult item = new SearchResult(itemId, itemName);
		    results[i] = item;
		}
		return results;
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		int[] spatialResults = DbManager.spatialItems(region);
		SearchResult[] baseResults = basicSearch(query, 0, Integer.MAX_VALUE);
		System.out.println("base result" + baseResults.length);
		System.out.println("spatial result" + spatialResults.length);
		int tail = 0;
		for(int i = 0; i < baseResults.length; i++){
			boolean foundMatch = false;
			for(int j = 0; j < spatialResults.length; j++){
				if(Integer.parseInt(baseResults[i].getItemId()) == spatialResults[j]){
					foundMatch = true;
					break;
				}
			}
			if(foundMatch){
				baseResults[tail++] = baseResults[i];
			}
		}
		int endIndex = numResultsToSkip + numResultsToReturn;
		System.out.println("tail size " + tail);
		if (tail < numResultsToSkip + numResultsToReturn)
			endIndex = tail;
		return Arrays.copyOfRange(baseResults, numResultsToSkip, endIndex);
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		// retrieve Item Info
		Item item = DbManager.getItem(Integer.parseInt(itemId));
		StringBuilder xml = new StringBuilder();
		if (item != null) {
			xml.append("<Item ItemID=\"" + itemId + "\">");
			xml.append("<Name>" + escapeXMLChars(item.name) + "</Name>");


			// retrieve categories categories!
			ArrayList<String> categories = DbManager.getCategories(itemId);
			for (String category : categories) {
				xml.append("<Category>" + escapeXMLChars(category) + "</Category>");
			}

			xml.append("<Currently>$" + item.currently + "</Currently>");
			if (item.buyPrice > 0) {
				xml.append("<Buy_Price>$" + item.buyPrice + "</Buy_Price>");
			}

			xml.append("<First_Bid>$" + item.firstBid + "</First_Bid>");
			xml.append("<Number_of_Bids>" + Integer.toString(item.numBids) + "</Number_of_Bids>");

			// get bids
			if (item.numBids > 0) {
				xml.append("<Bids>");
				for (Bid bid : item.bids) {
					xml.append("<Bid>");
					xml.append("<Bidder Rating=\"" + bid.rating + "\" UserID=\"" + bid.userId + "\">");
					xml.append("<Location>" + escapeXMLChars(bid.location) +"</Location>");
					xml.append("<Country>" + escapeXMLChars(bid.country) +"</Country>");
					xml.append("</Bidder>");
					xml.append("<Time>" + bid.time + "</Time>");
					xml.append("<Amount>$" + bid.amount + "</Amount>");
					xml.append("</Bid>");
				}
				xml.append("</Bids>");
			} else {
				xml.append("<Bids/>");
			}


			xml.append("<Location Latitude=\"" + item.latitude + "\" Longitude=\"" + item.longitude + "\">" + escapeXMLChars(item.location) + "</Location>");
			xml.append("<Country>" + item.country + "</Country>");
			xml.append("<Started>" + formatDate(item.started) + "</Started>");
			xml.append("<Ends>" + formatDate(item.ends) +"</Ends>");
			xml.append("<Seller Rating=\"" + item.sellerRating + "\" UserID=\"" + item.sellerId + "\"/>");
			xml.append("<Description>" + escapeXMLChars(item.description) +"</Description>");

			xml.append("</Item>");
		}

		return xml.toString();
	}

	static String formatDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(OLD_FORMAT);
        try {
            Date date = dateFormat.parse(dateString);
            dateFormat.applyPattern(NEW_FORMAT);
            dateString = dateFormat.format(date); 
        } catch (Exception e) {
                
            }
        return dateString;
    }

   static String escapeXMLChars(String input) {
      if (input == null) {
         return null;
      }
      
      StringBuilder output = new StringBuilder();
      for (int i = 0; i < input.length(); i++) {
      	 char ch = input.charAt(i);
         switch (ch) {
         case '<':
            output.append("&lt;");
            break;
         case '>':
            output.append("&gt;");
            break;
         case '&':
            output.append("&amp;");
            break;
         case '\"':
            output.append("&quot;");
            break;
         case '\'':
            output.append("&apos;");
            break;
         default:
            output.append(ch);
            break;
         }
      }
      return output.toString();
   }
	
	public String echo(String message) {
		return message;
	}

}

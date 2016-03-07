/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

class MyParser {
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };



    static final String OLD_FORMAT = "MMM-dd-yy HH:mm:ss";
    static final String NEW_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }

    /**
    * Change datestring from OLD_FORMAT TO NEW_FORMAT
    *
    * 
    */
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

    static Item processDoc(Document doc) {
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */ 
        Item rItem = new Item();
        Element item = (Element) doc.getElementsByTagName("Item").item(0);
        String itemID = item.getAttribute("ItemID");
        rItem.itemId = Integer.parseInt(itemID);
        rItem.name = getElementTextByTagNameNR(item,"Name");
        //get Category+
        Element[] categoryArray = getElementsByTagNameNR(item, "Category");
        for ( Element category : categoryArray){
            String categoryText = getElementText(category);
            rItem.categories.add(categoryText);
        }
        rItem.currently = getElementTextByTagNameNR(item, "Currently");
        rItem.buyPrice = getElementTextByTagNameNR(item, "Buy_Price");
        rItem.firstBid = getElementTextByTagNameNR(item, "First_Bid");
        rItem.numBids = getElementTextByTagNameNR(item, "Number_of_Bids");

        Element bids = getElementByTagNameNR(item, "Bids");
        Element [] bidsArray = getElementsByTagNameNR(bids, "Bid");
        for ( Element e : bidsArray){
                Bid bid = new Bid();

                Element bidder = getElementByTagNameNR(e, "Bidder");
                bid.userId = bidder.getAttribute("UserID");
                bid.rating = bidder.getAttribute("Rating");
                bid.location = getElementTextByTagNameNR(bidder, "Location");
                bid.country = getElementTextByTagNameNR(bidder, "Country");
                bid.time = getElementTextByTagNameNR(e, "Time");
                bid.amount = getElementTextByTagNameNR(e, "Amount");

                rItem.bids.add(bid);
            }
        Collections.sort(rItem.bids, new Comparator<Bid>() {
            @Override
            public int compare(Bid b1, Bid b2) {
                try {
                    String date1 = formatDate(b1.time);
                    String date2 = formatDate(b2.time);
                    return date2.compareTo(date1);
                } catch (Exception e) {
                    System.err.println("compare date error");
                    return -1;
                }
            }
        });

        //get Location
        Element location = getElementByTagNameNR(item, "Location");
        rItem.latitude = location.getAttribute("Latitude");
        rItem.longitude = location.getAttribute("Longitude");
        rItem.location = getElementText(location);

        rItem.country = getElementTextByTagNameNR(item, "Country");
        rItem.started = getElementTextByTagNameNR(item, "Started");
        rItem.ends = getElementTextByTagNameNR(item, "Ends");

        Element seller = getElementByTagNameNR(item, "Seller");
        rItem.sellerId = seller.getAttribute("UserID");
        rItem.sellerRating = seller.getAttribute("Rating");

        rItem.description = getElementTextByTagNameNR(item, "Description");     
        return rItem;   
    }
}

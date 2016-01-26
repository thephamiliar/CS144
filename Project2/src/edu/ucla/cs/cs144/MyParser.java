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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
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
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
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

    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        PrintWriter itemFile = null;
        PrintWriter categoryFile = null;
        PrintWriter bidFile = null;
        PrintWriter sellerFile = null;
        PrintWriter bidderFile = null;
        
        try {
            doc = builder.parse(xmlFile);
            itemFile = new PrintWriter(new FileWriter("items.csv", true));
            categoryFile = new PrintWriter(new FileWriter("categories.csv", true));
            bidFile = new PrintWriter(new FileWriter("bids.csv", true));
            sellerFile = new PrintWriter(new FileWriter("sellers.csv", true));
            bidderFile = new PrintWriter(new FileWriter("bidders.csv", true));
               
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        //System.out.println("Successfully parsed - " + xmlFile);
        
        Element items = doc.getDocumentElement();

        //System.out.println("Retrieved root : " + items.getTagName());

        Element[] itemArray = getElementsByTagNameNR(items, "Item");

        int i = 0;
        for ( Element item : itemArray){
            StringBuilder itemLine = new StringBuilder(200);

            String itemID = item.getAttribute("ItemID");
            itemLine.append(itemID + columnSeparator + getElementTextByTagNameNR(item, "Name") + columnSeparator);
            //System.out.println("Line string : " + itemLine);
            //get Category+
            Element[] categoryArray = getElementsByTagNameNR(item, "Category");
            for ( Element category : categoryArray){
                StringBuilder categoryLine = new StringBuilder(200);
                String categoryText = getElementText(category);
                categoryLine.append(itemID + columnSeparator + categoryText);
                categoryFile.println(categoryLine.toString() );
                categoryFile.flush();
            }
            //get Currently
            itemLine.append(strip(getElementTextByTagNameNR(item, "Currently")) + columnSeparator);
            //get Buy_Price
            itemLine.append(strip(getElementTextByTagNameNR(item, "Buy_Price")) + columnSeparator);
            //get First_Bid
            itemLine.append(strip(getElementTextByTagNameNR(item, "First_Bid")) + columnSeparator);
            //get Number_of_Bids
            itemLine.append(getElementTextByTagNameNR(item, "Number_of_Bids") + columnSeparator);
            //get Bids (bid s inside)
            Element bids = getElementByTagNameNR(item, "Bids");
            Element [] bidsArray = getElementsByTagNameNR(bids, "Bid");
            for ( Element bid : bidsArray){
                StringBuilder bidLine = new StringBuilder(200);
                StringBuilder bidderLine = new StringBuilder(200);

                //bidder element
                Element bidder = getElementByTagNameNR(bid, "Bidder");
                String bidderUserID = bidder.getAttribute("UserID");
                String bidderRating = bidder.getAttribute("Rating");
                String bidderLocation = getElementTextByTagNameNR(bidder, "Location");
                String bidderCountry = getElementTextByTagNameNR(bidder, "Country");
                bidderLine.append(bidderUserID + columnSeparator);
                bidderLine.append(bidderRating + columnSeparator);
                bidderLine.append(bidderLocation + columnSeparator);
                bidderLine.append(bidderCountry);

                bidderFile.println(bidderLine.toString() );
                bidderFile.flush();

                //bid element
                bidLine.append(bidderUserID + columnSeparator+ itemID + columnSeparator);
                String bidTime = getElementTextByTagNameNR(bid, "Time");
                String bidAmount = getElementTextByTagNameNR(bid, "Amount");
                bidLine.append(bidTime + columnSeparator + strip(bidAmount));
                bidFile.println(bidLine.toString() );
                bidFile.flush();
            }
            //get Location
            Element location = getElementByTagNameNR(item, "Location");
            String locationLatitude = location.getAttribute("Latitude");
            String locationLongitude = location.getAttribute("Longitude");
            String locationName = getElementText(location);
            itemLine.append(locationName + columnSeparator + locationLatitude + columnSeparator + locationLongitude + columnSeparator);

            //get country
            String country = getElementTextByTagNameNR(item, "Country");
            itemLine.append(country + columnSeparator);

            //get Started
            String started = getElementTextByTagNameNR(item, "Started");
            itemLine.append(formatDate(started) + columnSeparator);

            //get Ends
            String ends = getElementTextByTagNameNR(item, "Ends");
           
            itemLine.append(formatDate(ends) + columnSeparator);

            //get Seller
            Element seller = getElementByTagNameNR(item, "Seller");
            String sellerUserID = seller.getAttribute("UserID");
            String sellerRating = seller.getAttribute("Rating");
            StringBuilder sellerLine = new StringBuilder(200);
            //System.out.println("Seller got info : rating : " + sellerRating);
            sellerLine.append(sellerUserID + columnSeparator + sellerRating);
            itemLine.append(sellerUserID + columnSeparator);
            sellerFile.println(sellerLine.toString());
            sellerFile.flush();

            //get Description
            String description = getElementTextByTagNameNR(item, "Description");
            itemLine.append(description);

            itemFile.println(itemLine.toString());
            itemFile.flush();
        }

        System.out.println("Item size : " + itemArray.length );
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        

        
        
        /**************************************************************/
        
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}

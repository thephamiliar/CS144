package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }

    private IndexWriter indexWriter = null;
    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File("/var/lib/lucene"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
    }

    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
   }

   public void indexItem(Item item) throws IOException {
        IndexWriter writer = getIndexWriter(false);
        Document doc = new Document();
        doc.add(new StringField("id", Integer.toString(item.itemId), Field.Store.YES));
        doc.add(new StringField("name", item.name, Field.Store.YES));
        doc.add(new StringField("categories", item.categories, Field.Store.YES));
        doc.add(new StringField("description", item.description, Field.Store.YES));
        String fullSearchableText = item.itemId + " " + item.name + " " + item.categories + " " + item.description;
        doc.add(new TextField("content", fullSearchableText, Field.Store.NO));
        writer.addDocument(doc);
    } 
 
    public void rebuildIndexes() {
    	/*
    	 * Add your code here to retrieve Items using the connection
    	 * and add corresponding entries to your Lucene inverted indexes.
             *
             * You will have to use JDBC API to retrieve MySQL data from Java.
             * Read our tutorial on JDBC if you do not know how to use JDBC.
             *
             * You will also have to use Lucene IndexWriter and Document
             * classes to create an index and populate it with Items data.
             * Read our tutorial on Lucene as well if you don't know how.
             *
             * As part of this development, you may want to add 
             * new methods and create additional Java classes. 
             * If you create new classes, make sure that
             * the classes become part of "edu.ucla.cs.cs144" package
             * and place your class source files at src/edu/ucla/cs/cs144/.
    	 * 
    	 */
        try {
            // Erase existing index
            getIndexWriter(true);
            // Index all Accommodation entries
            Item[] items = DbManager.getItems();
            System.out.println(items.length);
            for(Item item : items) {
                System.out.println(item.categories);
                indexItem(item);              
            }
            // Don't forget to close the index writer when done
            closeIndexWriter();
        }
        catch (Exception e){
            System.out.println("error");
        }
    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
        DbManager.getItems();
    }   
}

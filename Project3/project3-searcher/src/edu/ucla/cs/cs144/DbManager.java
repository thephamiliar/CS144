package edu.ucla.cs.cs144;

import java.sql.*;

import java.util.*;

public class DbManager {
        static private String databaseURL = "jdbc:mysql://localhost:3306/";
        static private String dbname = "CS144";
        static private String username = "cs144";
        static private String password = "";
	
	/**
	 * Opens a database connection
	 * @param dbName The database name
	 * @param readOnly True if the connection should be opened read-only
	 * @return An open java.sql.Connection
	 * @throws SQLException
	 */
	public static Connection getConnection(boolean readOnly)
	throws SQLException {        
            Connection conn = DriverManager.getConnection(
                databaseURL + dbname, username, password);
            conn.setReadOnly(readOnly);        
            return conn;
        }
	
	private DbManager() {}


	public static int[] spatialItems(SearchRegion sr){

		try {
			Connection conn = getConnection(true);
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM SpatialTable WHERE Latitude > ? AND Latitude < ? AND Longitude > ? AND Longitude < ?");
			stmt.setDouble(1,sr.getLx());
			stmt.setDouble(2,sr.getRx());
			stmt.setDouble(3,sr.getLy());
			stmt.setDouble(4,sr.getRy());
			ResultSet rs = stmt.executeQuery();
			List<Integer> itemIds = new ArrayList<>();
			while(rs.next()){ 
				int itemId = rs.getInt("ItemID");
				itemIds.add(itemId);
			}
			System.out.println("Total valid outputs (Spatial Search) : " + itemIds.size());
			conn.close();

			int[] intArray = new int[itemIds.size()];
			int count = 0;
			for (Integer i : itemIds){
				intArray[count++] = i.intValue();
			}
     		return intArray;
     	}
     	catch (Exception e){
     		System.out.println("Spatial index error" + e);
     	}
     	return null;
	}


	public static Item getItem(int itemId) {
		Item item = null;
		try {
			Connection conn = getConnection(true);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Items WHERE ItemID=" + itemId);
			if (rs.next()){ 
				String description = rs.getString("Description");
				String name = rs.getString("Name");

				PreparedStatement categoryStatement = conn.prepareStatement( "SELECT * FROM Categories WHERE ItemID = ?");
				categoryStatement.setInt(1, itemId);
				ResultSet crs = categoryStatement.executeQuery();
				StringBuilder categories = new StringBuilder();
				while(crs.next() ){
					categories.append(" " + crs.getString("Category"));
				}
				item = new Item(itemId, name, categories.toString(), description);

				item.currently = rs.getFloat("Currently");
				item.buyPrice = rs.getFloat("Buy_Price");
				item.firstBid = rs.getFloat("First_Bid");
				item.numBids = rs.getInt("Number_Of_Bids");

				PreparedStatement bidsStatement = conn.prepareStatement( "SELECT * FROM Bids WHERE ItemID = ?");
				bidsStatement.setInt(1, itemId);
				ResultSet brs = bidsStatement.executeQuery();
				List<Bid> bids = new ArrayList<Bid>();
				while (brs.next()) {
					Bid bid = new Bid();
					bid.time = brs.getDate("Time").toString();
					bid.amount = brs.getFloat("Amount");

					PreparedStatement userStatement = conn.prepareStatement( "SELECT * FROM Users WHERE UserID = ?");
					userStatement.setString(1, brs.getString("BidderID"));
					ResultSet urs = userStatement.executeQuery();
					if (urs.next()) {
						bid.rating = urs.getInt("BidderRating");
						bid.userId = urs.getString("UserID");
						bid.location = urs.getString("Location");
						bid.country = urs.getString("Country");
					}

					bids.add(bid);
				}

				item.bids = bids;
				item.location = rs.getString("LocationName");
				item.latitude = rs.getFloat("Latitude");
				item.longitude = rs.getFloat("Longitude");
				item.started = rs.getDate("Started").toString();
				item.ends = rs.getDate("Ends").toString();
				item.sellerId = rs.getString("SellerID");

				PreparedStatement sellerStatement = conn.prepareStatement( "SELECT * FROM Users WHERE UserID = ?");
				sellerStatement.setString(1, item.sellerId);
				ResultSet srs = sellerStatement.executeQuery();
				if (srs.next()) {
					item.sellerRating = srs.getInt("SellerRating");
				}
			}
			conn.close();
		}
		catch (Exception e){
			System.out.println("Error encountered");
		}
		return item;
	}

	public static ArrayList<String> getCategories(String itemId){
		ArrayList<String> categories = new ArrayList<String>();
		try {
			Connection conn = getConnection(true);
			Statement stmt = conn.createStatement();
			ResultSet crs = stmt.executeQuery("SELECT * FROM Categories WHERE ItemID=" + itemId);
			while (crs.next()){ 
				categories.add(crs.getString("Category"));
			}
			conn.close();
		}
		catch (Exception e){
			System.out.println("Error encountered");
		}
		return categories;
	}
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}

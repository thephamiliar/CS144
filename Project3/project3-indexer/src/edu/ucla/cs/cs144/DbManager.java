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
	public static Connection getConnection(boolean readOnly) throws SQLException {        
        Connection conn = DriverManager.getConnection(databaseURL + dbname, username, password);
        conn.setReadOnly(readOnly);        
        return conn;
    }

	private DbManager() {}

	public static Item[] getItems(){
		try {
			Connection conn = getConnection(true);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Items");
			List<Item> items = new ArrayList<Item>();
			while(rs.next()){ 
				int itemId = rs.getInt("ItemID");
				String description = rs.getString("Description");
				String name = rs.getString("Name");

				PreparedStatement categoryStatement = conn.prepareStatement( "SELECT * FROM Categories WHERE ItemID = ?");
				categoryStatement.setInt(1, itemId);
				ResultSet crs = categoryStatement.executeQuery();
				StringBuilder categories = new StringBuilder();
				while(crs.next() ){
					categories.append(" " + crs.getString("Category"));
				}
				Item item = new Item(itemId, name, categories.toString(), description);
				items.add(item);
			}
			conn.close();
			return items.toArray(new Item[items.size()]);
		}
		catch (Exception e){
			System.out.println("Error encountered");
		}
		return null;
	}
	//1          2
	//
	//3          4
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}

package edu.ucla.cs.cs144;

import java.util.*;

public class Item {

	public int itemId;
	public String name;
	public String categories;
	public float currently;
	public float buyPrice;
	public float firstBid;
	public int numBids;
	public ArrayList<Bid> bids;
	public String location;
	public float latitude;
	public float longitude;
	public String country;
	public String started;
	public String ends;
	public int sellerRating;
	public String sellerId;
	public String description;

	public Item(int itemId, String name, String categories, String description){
		this.itemId = itemId;
		this.name = name;
		this.categories = categories;
		this.description = description;
	}
}
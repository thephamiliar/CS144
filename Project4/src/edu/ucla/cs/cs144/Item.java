package edu.ucla.cs.cs144;

import java.util.*;

public class Item {

	public int itemId;
	public String name;
	public ArrayList<String> categories;
	public String currently;
	public String buyPrice;
	public String firstBid;
	public String numBids;
	public ArrayList<Bid> bids;
	public String location;
	public String latitude;
	public String longitude;
	public String country;
	public String started;
	public String ends;
	public String sellerRating;
	public String sellerId;
	public String description;

	public Item(){
		this.categories = new ArrayList<>();
		this.bids = new ArrayList<>();
	}
}
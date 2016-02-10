package edu.ucla.cs.cs144;

import java.util.*;

public class Item{

	public int itemId;
	public String name;
	public List<String> categories;
	public String description;

	public Item(int itemId, String name, String description){
		this.name = name;
		categories = new ArrayList<String>();
		this.description = description;
	}
}
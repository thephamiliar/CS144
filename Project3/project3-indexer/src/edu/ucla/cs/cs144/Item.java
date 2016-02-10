package edu.ucla.cs.cs144;

import java.util.*;

public class Item{

	public int itemId;
	public String name;
	public String categories;
	public String description;

	public Item(int itemId, String name, String categories, String description){
		this.itemId = itemId;
		this.name = name;
		this.categories = categories;
		this.description = description;
	}
}
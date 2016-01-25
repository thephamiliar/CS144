Part B: Design your relational schema
1. 	Items
		(ItemID*, Name, Currently, Buy_Price, First_Bid, Number_Of_Bids, LocationID^, Country, Started, Ends, SellerID^, Description)
	Category
		(ItemID^*, Category*)
	Bids
		(BidderID^*, ItemID^*, Time*, Amount)
	Sellers
		(SellerID*, Rating)
	Bidders
		(BidderID*, Rating, Location, Country)
	Location
		(LocationID*, Name, Latitude, Longitude)

	*primary keys
	^foreign keys

2.	Items.ItemID -> Name, Currently, Buy_Price, First_Bid, Number_Of_Bids, LocationID^, Country, Started, Ends, SellerID, Description
	Category.ItemID -> Category
	Bids.BidderID, Bids.ItemID, Bids.Time -> Amount
	Sellers.SellerID -> Rating
	Bidders.BidderID -> Rating, Location, Country
	Location.ID -> Location.Name, Latitude, Longitude

3. Yes
4. Yes
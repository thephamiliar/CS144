CREATE TABLE Users(
	UserID VARCHAR(50),
	BidderRating INTEGER DEFAULT NULL,
	SellerRating INTEGER DEFAULT NULL,
	Location VARCHAR(80),
	Country VARCHAR(80),
	PRIMARY KEY (UserID)
);

CREATE TABLE Items(
	ItemID INTEGER,
	Name VARCHAR(80),
	Currently DECIMAL(6,2),
	Buy_Price DECIMAL(6,2),
	First_Bid DECIMAL(6,2), 
	Number_Of_Bids INTEGER,
	LocationName VARCHAR(200),
	Latitude DECIMAL(9,6),
	Longitude DECIMAL(9,6),
	Country VARCHAR(80),
	Started DATETIME,
	Ends DATETIME,
    SellerID VARCHAR(50) NOT NULL,
	Description VARCHAR(4000),
	PRIMARY KEY (ItemID),
	FOREIGN KEY (SellerID) REFERENCES Users(UserID)
);

CREATE TABLE Bids(
	BidderID VARCHAR(80),
	ItemID INTEGER,
	Time DATETIME,
	Amount DECIMAL(6,2),
	FOREIGN KEY (ItemID) REFERENCES Items(ItemID),
	FOREIGN KEY (BidderID) REFERENCES Users(UserID)
);


CREATE TABLE Categories(
	ItemID INTEGER,
	Category VARCHAR(80),
	FOREIGN KEY Categories(ItemID) REFERENCES Items(ItemID)
);

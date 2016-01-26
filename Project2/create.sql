CREATE TABLE Sellers(
	SellersID VARCHAR(50),
	Rating INTEGER,
	PRIMARY KEY (SellersID)
);

CREATE TABLE Bidders(
	BiddersID VARCHAR(50),
	Rating INTEGER,
	Location VARCHAR(80),
	Country VARCHAR(80),
	PRIMARY KEY (BiddersID)
);

CREATE TABLE Items(
	ItemsID INTEGER,
	Name VARCHAR(80),
	Currently DECIMAL(6,2),
	Buy_Price DECIMAL(6,2),
	First_Bid DECIMAL(6,2), 
	Number_Of_Bids INTEGER,
	LocationName VARCHAR(200),
	Latitude DECIMAL(8,6),
	Longitude DECIMAL(8,6),
	Country VARCHAR(80),
	Started DATETIME,
	Ends DATETIME,
    SellerID VARCHAR(50) NOT NULL,
	Description VARCHAR(4000),
	PRIMARY KEY (ItemsID),
	FOREIGN KEY (SellerID) REFERENCES Sellers(SellersID)
);

CREATE TABLE Bids(
	BidderID VARCHAR(80),
	ItemID INTEGER,
	Time DATETIME,
	Amount DECIMAL(6,2),
	FOREIGN KEY (ItemID) REFERENCES Items(ItemsID),
	FOREIGN KEY (BidderID) REFERENCES Bidders(BiddersID)
);


CREATE TABLE Categories(
	ItemID INTEGER,
	Category VARCHAR(80),
	FOREIGN KEY Categories(ItemID) REFERENCES Items(ItemsID)
);

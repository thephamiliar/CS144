CREATE TABLE Sellers(
	UserID VARCHAR(50),
	Rating INTEGER,
	PRIMARY KEY (UserID)
);

CREATE TABLE Bidders(
	UserID VARCHAR(50),
	Rating INTEGER,
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
	Latitude DECIMAL(8,6),
	Longitude DECIMAL(8,6),
	Country VARCHAR(80),
	Started DATETIME,
	Ends DATETIME,
    UserID VARCHAR(50) NOT NULL,
	Description VARCHAR(4000),
	PRIMARY KEY (ItemID),
	FOREIGN KEY (UserID) REFERENCES Sellers(UserID)
);

CREATE TABLE Bids(
	UserID VARCHAR(80),
	ItemID INTEGER,
	Time DATETIME,
	Amount DECIMAL(6,2),
	FOREIGN KEY (ItemID) REFERENCES Items(ItemID),
	FOREIGN KEY (UserID) REFERENCES Bidders(UserID)
);


CREATE TABLE Categories(
	ItemID INTEGER,
	Category VARCHAR(80),
	FOREIGN KEY Categories(ItemID) REFERENCES Items(ItemID)
);

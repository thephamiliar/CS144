CREATE TABLE SpatialTable(
	ItemID INTEGER,
	Latitude DECIMAL(8,6),
	Longitude DECIMAL(8,6)
) ENGINE=MyISAM;
INSERT INTO SpatialTable(ItemID, Latitude, Longitude) SELECT ItemID,Latitude,Longitude from Items;
ALTER TABLE SpatialTable ADD Coordinates Point NOT NULL;
UPDATE  SpatialTable SET Coordinates = Point(Latitude, Longitude);
CREATE SPATIAL INDEX sp_index ON SpatialTable(Coordinates);
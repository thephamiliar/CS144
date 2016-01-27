SELECT COUNT(*)
FROM (SELECT DISTINCT (T.UserID)
	FROM ((SELECT UserID FROM Sellers) UNION (SELECT UserID FROM Bidders)) AS T
) AS T2;

SELECT COUNT(*)
FROM Items
WHERE BINARY LocationName="New York";

SELECT COUNT(*)
FROM ( SELECT * FROM Categories
	GROUP BY ItemID
	HAVING COUNT(ItemID) = 4
	) AS T;

SELECT ItemID
FROM Items
WHERE Currently = (SELECT Max(Currently) 
				FROM Items
				WHERE Started < '2001-12-20 00:00:01'
				AND Ends > '2001-12-20 00:00:01'
    			AND Number_of_Bids > 0)
AND Started < '2001-12-20 00:00:01'
AND Ends > '2001-12-20 00:00:01'
AND Number_of_Bids > 0;

SELECT COUNT(*)
FROM Sellers
WHERE Rating > 1000;

SELECT COUNT(*)
FROM Sellers
INNER JOIN Bidders ON Sellers.UserID = Bidders.UserID;

SELECT COUNT(*)
FROM (SELECT DISTINCT Category
	FROM Categories
	WHERE ItemID IN 
		(SELECT ItemID
		FROM Bids
		WHERE Amount > 100)
	) as T2;

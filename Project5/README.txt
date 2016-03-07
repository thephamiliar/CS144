README
1. Encrypted when credit card information is being passed: (4)->(5) & (5)->(6) 
2. Ensure buy_price data integrity by creating a session in the ItemServlet and passing the buy_price attribute through the session id. This makes sure that the user cannot access and manipulate data in the application layer.
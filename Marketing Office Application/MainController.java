package application;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;


public class MainController {
	@FXML
	private Button run;
	@FXML
	private  Button quit;
	@FXML
	private ListView<String> brandDollar;
	@FXML
	private ListView<String> dealerDollar;
	@FXML
	private ListView<String> brandUnit;
	@FXML
	private ListView<String> convertMonth;
	@FXML
	private ListView<String> inventory;
	@FXML
	private ListView<String> yearTrend;
	@FXML
	private ListView<String> gender;
	@FXML
	private ListView<String> incomeRange;
	@FXML
	private ListView<String> totalSoldprice;
	
	
	
	public void processQuit(ActionEvent e) {
		//quit button function to close application.
			System.exit(0);
		}	
	
public void processRun(ActionEvent e) {
	//Run button to populate application from queries
		ObservableList<String> items = FXCollections.observableArrayList();
		ObservableList<String> dealerItems = FXCollections.observableArrayList();
		ObservableList<String> unitItems = FXCollections.observableArrayList();
		ObservableList<String> monthItems = FXCollections.observableArrayList();
		ObservableList<String> inventoryItems = FXCollections.observableArrayList();
		ObservableList<String> yearItems = FXCollections.observableArrayList();
		ObservableList<String> genderItems = FXCollections.observableArrayList();
		ObservableList<String> rangeItems = FXCollections.observableArrayList();
		ObservableList<String> saleItems = FXCollections.observableArrayList();
	
		try {
	  	    Class.forName("oracle.jdbc.driver.OracleDriver"); 
	        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@citdb.nku.edu:1521:csc450", "BUNDYJ2", "csc821" ); 
	        Statement pStmt = conn.createStatement();
	        Statement dStmt = conn.createStatement();
	        Statement uStmt = conn.createStatement();
	        Statement mStmt = conn.createStatement();
	        Statement iStmt = conn.createStatement();
	        Statement tStmt = conn.createStatement();
	        
			// assign values to parameters
	        
	        ResultSet rset = pStmt.executeQuery("with top_brand as\r\n" + 
	        		"(Select Brandname, sum(soldprice) as Dollar_Amount \r\n" + 
	        		"from brand B, Model M, Sale S, Vehicle V\r\n" + 
	        		"where B.BID = M.BID and M.MID = V.MID and V.VIN = S.VIN\r\n" + 
	        		"and SoldDate between '01-JAN-2018' and '31-DEC-2018' \r\n" + 
	        		"group by brandname)\r\n" + 
	        		"select brandname as Top_Brand\r\n" + 
	        		"from top_brand\r\n" + 
	        		"where dollar_amount = (select max(dollar_amount) from top_brand)");
	        
	        ResultSet dealerSet = dStmt.executeQuery("with top_dealer as\r\n" + 
	        		"(Select dealername, sum(soldprice) as Dollar_Amount   \r\n" + 
	        		"from Dealer D, Model M, Sale S, Vehicle V\r\n" + 
	        		"where D.dID = S.dID\r\n" + 
	        		"and SoldDate between '01-JAN-2018' and '31-DEC-2018'\r\n" + 
	        		"group by dealername)\r\n" + 
	        		"select dealername as Top_Dealer\r\n" + 
	        		"from top_dealer\r\n" + 
	        		"where dollar_amount = (select max(dollar_amount) from top_dealer)");
	        
	        ResultSet unitSet = uStmt.executeQuery("With unit_sales as\r\n" + 
	        		"(Select B.brandname, count(brandname) as Units_Sold\r\n" + 
	        		"From brand B, model M, Vehicle V, Sale S\r\n" + 
	        		"Where B.BID = M.BID and M.MID = V.MID and V.VIN = S.VIN\r\n" + 
	        		"And SoldDate between '01-JAN-2018' and '31-DEC-2018'\r\n" + 
	        		"group by brandname)\r\n" + 
	        		"select brandname as Top_unitSales\r\n" + 
	        		"from unit_sales\r\n" + 
	        		"where units_sold = (select max(units_sold) from unit_sales)");
	        
	        ResultSet monthSet = mStmt.executeQuery("With convertible_month as\r\n" + 
	        		"(select extract( month from SoldDate) as Month, count(extract( month from SoldDate)) as Convertibles_Sold \r\n" + 
	        		"from Sale S, Model M, Vehicle V\r\n" + 
	        		"where M.MID = V.MID and V.VIN = S.VIN and\r\n" + 
	        		"bodystyle = 'Convertible' \r\n" + 
	        		"group by extract( month from SoldDate))\r\n" + 
	        		"select month as Convertible_Month\r\n" + 
	        		"from convertible_month\r\n" + 
	        		"where convertibles_sold = (select max(convertibles_sold) from convertible_month)");
	        
	        ResultSet inventorySet = iStmt.executeQuery("With max_diff as \r\n" + 
	        		"(select dealername, avg((solddate - inventorydate)) as difference from dealer D, sale S, Vehicle V\r\n" + 
	        		"where D.dID = S.dID and V.VIN = S.VIN\r\n" + 
	        		"group by dealername)\r\n" + 
	        		"select dealername as Longest_Inventory\r\n" + 
	        		"from max_diff\r\n" + 
	        		"where difference = (select max(difference) from max_diff)");
	        
	        
	       ResultSet tableSet = tStmt.executeQuery("with IncomeRange as(\r\n" + 
	       		"Select \r\n" + 
	       		"    Case \r\n" + 
	       		"        when annualIncome between 10000 and 400000 then '10000 and 400000'\r\n" + 
	       		"        when annualIncome between 400001 and 800000 then '400001 and 800000'\r\n" + 
	       		"    End as Range\r\n" + 
	       		"from Customer) \r\n" + 
	       		"Select extract( year from SoldDate) as Year_Trend, c.gender as Gender, i.range as Income_Range, sum(s.soldPrice) as Total_SoldPrice\r\n" + 
	       		"From brand b, model m, vehicle v, customer c, sale s, IncomeRange i\r\n" + 
	       		"Where m.mid = v.mid and b.bid = m.bid and v.vin = s.vin and solddate between to_date('01-JAN-2016') and to_date('31-DEC-2018')\r\n" + 
	       		"Group by extract( year from SoldDate), c.gender, i.range\r\n" + 
	       		"Order by Year_Trend ASC");
	       
	       			        
	        dealerDollar.setItems(dealerItems);
	        brandDollar.setItems(items);
	        brandUnit.setItems(unitItems);
	        convertMonth.setItems(monthItems);
	        inventory.setItems(inventoryItems);
	        yearTrend.setItems(yearItems);
	        gender.setItems(genderItems);
	        incomeRange.setItems(rangeItems);
	        totalSoldprice.setItems(saleItems);
	       
	        	     
	        while (rset.next()) 	        	
	        	items.add(rset.getString("Top_Brand"));	        
	        while(dealerSet.next()) 
	        	dealerItems.add(dealerSet.getString("Top_Dealer"));
			while(unitSet.next())
				unitItems.add(unitSet.getString("Top_unitSales"));
			while(monthSet.next())
				monthItems.add(monthSet.getString("Convertible_Month"));
			while(inventorySet.next())
				inventoryItems.add(inventorySet.getString("Longest_Inventory"));
			while(tableSet.next()) {
				yearItems.add(tableSet.getString("Year_Trend"));
				genderItems.add(tableSet.getString("Gender"));
				rangeItems.add(tableSet.getString("Income_Range"));
				saleItems.add(tableSet.getString("Total_SoldPrice"));
			}
		        pStmt.close();
		        dStmt.close();
		        mStmt.close();
		        iStmt.close();
		        tStmt.close();
		        
		        conn.close();
		}   
		     catch (SQLException sqle) {
		        System.out.println("SQLException : " + sqle);
		   }
		   catch (ClassNotFoundException s) {
		   System.out.println("ClassNotFoundException : " + s);

		   }
	}		
}	
	 
		




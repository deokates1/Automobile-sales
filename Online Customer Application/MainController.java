package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class MainController 
{
	@FXML
	private Button showResult;
	@FXML
	private Button clear;
	@FXML
	private Button quit;
	@FXML
	private TextField brand;
	@FXML
	private TextField model;
	@FXML
	private TextField bodyStyle;
	@FXML
	private TextField transmission;
	@FXML
	private TextField engine;
	@FXML
	private TextField  notification1;
	@FXML
	private TextField  notification2;
	@FXML
	private TextField  notification3;
	
	
	public void processQuit(ActionEvent e) {
	//quit button function to close application.
	
		System.exit(0);
		
	}
	
	public void processClear(ActionEvent e) {
		//Clear button function to clear all text fields
		
			brand.clear();
			model.clear();
			bodyStyle.clear();
			transmission.clear();
			engine.clear();
			notification1.clear();
			notification2.clear();
			notification3.clear();
			
	}
	
	public void processShowResult() 
	{
		//Shows which dealer has the desired car based off of user input. 
		if (brand.getText().isEmpty() == false && model.getText().isEmpty() == false && bodyStyle.getText().isEmpty() == false 
		&& transmission.getText().isEmpty() == false && engine.getText().isEmpty() == false) 
		{
			try 
			{
		  	    Class.forName("oracle.jdbc.driver.OracleDriver"); 
		        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@citdb.nku.edu:1521:csc450", "BUNDYJ2", "csc821" ); 

		        String brand1 = brand.getText();
		        String model1 = model.getText();
		        String bs = bodyStyle.getText();
		        String tran = transmission.getText();
		        String eng = engine.getText();
		        
		        PreparedStatement pStmt = conn.prepareStatement("SELECT distinct(dealername), dealerAddress, tagprice" + 
		        		" from dealer d, vehicle v, model m, brand b"
		        		+ " where d.did = v.did and v.mid = m.mid and m.bid = b.bid"
		        			+ " and b.brandName =?  and m.modelName =? and m.bodyStyle =? and v.engine =? and v.transmission =?");
		        		
		        pStmt.setString(1, brand1);
		        pStmt.setString(2, model1);
		        pStmt.setString(3, bs);
		        pStmt.setString(4, eng);
		        pStmt.setString(5, tran);
		        
		        ResultSet rst = pStmt.executeQuery();
		        
		        ResultSetMetaData rsmd = rst.getMetaData();
		        int columnsNumber = rsmd.getColumnCount();
		        while (rst.next()) 
		        {
		            for (int i = 1; i <= columnsNumber; i++) 
		            {
		                if (i > 1) System.out.print(",  ");
		                String columnValue = rst.getString(i);
		                if(i == 1)
		                	 notification1.setText(columnValue);
		                if(i == 2)
		                	 notification2.setText(columnValue);
		                if(i == 3)
		                	 notification3.setText(columnValue);
		             
		            }
		            System.out.println("");
		        }
		        
		    
		        
		        pStmt.close();
		        conn.close();
			}
		    catch (SQLException | ClassNotFoundException sqle) 
			{
		        System.out.println("SQLException : " + sqle);
			}
		}
	}
}
	

package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.*; 

import javax.swing.JOptionPane;

public class MainController 
{
	@FXML
	private Button update;
	@FXML
	private  Button clear;
	@FXML
	private  Button quit;
	@FXML
	private TextField vin;
	@FXML
	private TextField mid;
	@FXML
	private TextField did;
	@FXML
	private TextField color;
	@FXML
	private TextField engine;
	@FXML
	private TextField transmission;
	@FXML
	private TextField tagprice;
	@FXML
	private TextField isSold; 
	@FXML
	private TextField inventorydate;
	@FXML
	private TextField manufacturedate;
	
	
	public void processQuit(ActionEvent e) {
	//quit button function to close application.
		System.exit(0);
	}
	
	public void processClear(ActionEvent e) {
		//Clear button function to clear all text fields
			vin.clear();
			mid.clear();
			did.clear();
			color.clear();
			engine.clear();
			transmission.clear();
			tagprice.clear();
			isSold.clear();
			inventorydate.clear();
			manufacturedate.clear();
	}
	
public void processUpdate(ActionEvent e) {
		//Connect to Database and Update Vehicle Table
		if (vin.getText().isEmpty() == false && mid.getText().isEmpty() == false && did.getText().isEmpty() == false && color.getText().isEmpty() == false
				&& engine.getText().isEmpty() == false && transmission.getText().isEmpty() == false  && tagprice.getText().isEmpty() == false   && isSold.getText().isEmpty() == false
				&& inventorydate.getText().isEmpty() == false  && manufacturedate.getText().isEmpty() == false ) {
			try {
		  	    Class.forName("oracle.jdbc.driver.OracleDriver"); 
		        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@citdb.nku.edu:1521:csc450", "BUNDYJ2", "csc821" ); 

				PreparedStatement pStmt = conn.prepareStatement("insert into vehicle(VIN, mID, dID, color, engine, transmission, tagPrice, isSold, inventoryDate, manufactureDate) values(?,?,?,?,?,?,?,?,?,?)");
				// assign values to parameters
				pStmt.setString(1, vin.getText());	
				pStmt.setString(2, mid.getText()); 
				pStmt.setString(3, did.getText());   	
				pStmt.setString(4, color.getText());
				pStmt.setString(5, engine.getText());
				pStmt.setString(6, transmission.getText());
				pStmt.setString(7, tagprice.getText());
				pStmt.setString(8, isSold.getText());
				pStmt.setString(9, inventorydate.getText());
				pStmt.setString(10, manufacturedate.getText());
				///*
				  try { 
						pStmt.executeUpdate();
					}
			        catch (SQLException sqle) { 
			            System.out.println("Could not insert tuple. " + sqle);
			        }

			        pStmt.close();
			        conn.close();
			     }
			     catch (SQLException sqle) {
			        System.out.println("SQLException : " + sqle);
			   }
			   catch (ClassNotFoundException s) {
			   System.out.println("ClassNotFoundException : " + s);

			   }
				JOptionPane.showMessageDialog(null, "Vehicle has been added to Inventory");
		
		} 
			else {
			JOptionPane.showMessageDialog(null, "Error: Not all fields have values");
		}
		
	}
}


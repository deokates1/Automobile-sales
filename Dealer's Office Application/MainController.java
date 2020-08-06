package application;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


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
	private TextField did;
	@FXML
	private TextField cid;
	@FXML
	private TextField custName;
	@FXML
	private TextField custAdd;
	@FXML
	private TextField phone;
	@FXML
	private TextField gender;
	@FXML
	private TextField annualIncome;
	@FXML
	private TextField soldPrice;
	@FXML
	private TextField soldDate;
	@FXML
	private Label notification;
	
	public void processQuit(ActionEvent e) {
	//quit button function to close application.
	
		System.exit(0);
		
	}
	
	public void processClear(ActionEvent e) {
		//Clear button function to clear all text fields
		
			vin.clear();
			did.clear();
			cid.clear();
			custName.clear();
			custAdd.clear();
			phone.clear();
			gender.clear();
			annualIncome.clear();
			soldPrice.clear();
			soldDate.clear();
	}
	
	public void processUpdate(ActionEvent e) {
		//Connect to Database and Update Customer and Sale tables
		if (vin.getText().isEmpty() == false && did.getText().isEmpty() == false && cid.getText().isEmpty() == false && custName.getText().isEmpty() == false
				&& custAdd.getText().isEmpty() == false && phone.getText().isEmpty() == false  && gender.getText().isEmpty() == false   && annualIncome.getText().isEmpty() == false
				&& soldPrice.getText().isEmpty() == false  && soldDate.getText().isEmpty() == false ) {
			try {
		  	    Class.forName("oracle.jdbc.driver.OracleDriver"); 
		        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@citdb.nku.edu:1521:csc450", "DEOKATES1", "csc745" ); 
		        PreparedStatement pStmt0 = conn.prepareStatement("update Vehicle set isSold = 'Yes' where vin = ?");
				PreparedStatement pStmt = conn.prepareStatement("insert into sale(VIN, did, cid, soldPrice, soldDate) values(?,?,?,?,?)");
				PreparedStatement pStmt1 = conn.prepareStatement("insert into customer(cid, customerName, customerAddress, phone, gender, annualIncome) values(?,?,?,?,?,?)");
				// update isSold to Yes
				pStmt0.setString(1,vin.getText());
				// assign values to sale parameters
				pStmt.setString(1, vin.getText());	
				pStmt.setString(2, did.getText()); 
				pStmt.setString(3, cid.getText());   	
				pStmt.setString(4, soldPrice.getText());
				pStmt.setString(5, soldDate.getText());
				
				// assign values to customer parameters
				pStmt1.setString(1, cid.getText());
				pStmt1.setString(2, custName.getText());
				pStmt1.setString(3, custAdd.getText());
				pStmt1.setString(4, phone.getText());
				pStmt1.setString(5, gender.getText());
				pStmt1.setString(6, annualIncome.getText());
				
				///*
				  try { 
						pStmt1.executeUpdate();
						pStmt.executeUpdate();
						pStmt0.executeUpdate();
					
					}
			        catch (SQLException sqle) { 
			            System.out.println("Could not insert tuple. " + sqle);
			        }

			        pStmt.close();
			        pStmt1.close();
			        pStmt0.close();
			        conn.commit(); 
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

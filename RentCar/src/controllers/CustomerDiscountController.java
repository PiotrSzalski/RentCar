package controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import controllers.CustomerServiceController.Customer;
import data_base.CustomerDB;
import data_base.EmployeeDB;
import data_base.LoggedUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;

public class CustomerDiscountController {

	@FXML
	private Button back;
	@FXML
	private TextArea discount_list;
	
	private MainPaneController mainController;
	private LoggedUser loggedUser;
	
	public void load_discount() {
		ResultSet res;
		String querty = "select discount_description,value from discount join customer_discount on "
				+ "discount.id_discount=customer_discount.id_discount where id_customer="+loggedUser.getUserID()+";";
		try {
			res = CustomerDB.stmt.executeQuery(querty);
			while(res.next()) {
				discount_list.appendText(res.getString("discount_description"));
				discount_list.appendText(". Wartoœæ rabatu: ");
				discount_list.appendText(res.getString("value")+" z³.\n");
			}
		} catch (SQLException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Nie uda³o siê odczytac rabatów.");
			alert.showAndWait();
		}
		
	}
	
	@FXML
	public void back() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/Customer.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainController.loadScreen(pane);
		CustomerController customerController = loader.getController();
		customerController.setMainController(mainController);
		customerController.setLoggedUser(loggedUser);
		customerController.setWelcome();
	}
	
	public void setMainController(MainPaneController mainController) {
		this.mainController = mainController;
	}
	public void setLoggedUser(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}
}

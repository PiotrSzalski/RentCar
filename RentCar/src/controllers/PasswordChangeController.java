package controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import data_base.CustomerDB;
import data_base.EmployeeDB;
import data_base.LoggedUser;
import data_base.ManagerDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;

public class PasswordChangeController {
	
	@FXML
	private PasswordField old_password;
	@FXML
	private PasswordField new_password;
	@FXML
	private PasswordField repeated_password;
	@FXML
	private Button back;
	@FXML
	private Button change_password;
	
	private LoggedUser loggedUser;
	private MainPaneController mainController;
	
	@FXML
	public void back() {
		FXMLLoader loader = new FXMLLoader();
		if(loggedUser.getUserType() == 1) {
			loader.setLocation(this.getClass().getResource("/fxml/Manager.fxml"));
		} else if(loggedUser.getUserType() == 2) {
			loader.setLocation(this.getClass().getResource("/fxml/Employee.fxml"));
		} else if(loggedUser.getUserType() == 3) {
			loader.setLocation(this.getClass().getResource("/fxml/Customer.fxml"));
		} 
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainController.loadScreen(pane);
		if(loggedUser.getUserType() == 1) {
			ManagerController managerController = loader.getController();
			managerController.setMainController(mainController);
			managerController.setLoggedUser(loggedUser);
			managerController.setWelcome();
		} else if(loggedUser.getUserType() == 2) {
			EmployeeController employeeController = loader.getController();
			employeeController.setMainController(mainController);
			employeeController.setLoggedUser(loggedUser);
			employeeController.setWelcome();
		} else if(loggedUser.getUserType() == 3) {
			CustomerController customerController = loader.getController();
			customerController.setMainController(mainController);
			customerController.setLoggedUser(loggedUser);
			customerController.setWelcome();
		} 
	}
	
	@FXML
	public void change_password() {
		if(new_password.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Nie wprowadzono nowego has³a");
			alert.showAndWait();
		} else if(new_password.getText().equals(repeated_password.getText())) {
			ResultSet res = null;
			String querty = "";
			if(loggedUser.getUserType() == 1) {
				querty = "Select password from manager where id_manager="+loggedUser.getUserID()+";";
			} else if(loggedUser.getUserType() == 2) {
				querty = "Select password from employee where id_employee="+loggedUser.getUserID()+";";
			} else if(loggedUser.getUserType() == 3) {
				querty = "Select password from customer where id_customer="+loggedUser.getUserID()+";";
			} 
			String password = "";
			try {
				if(loggedUser.getUserType() == 1) {
					res = ManagerDB.stmt.executeQuery(querty);
				} else if(loggedUser.getUserType() == 2) {
					res = EmployeeDB.stmt.executeQuery(querty);
				} else if (loggedUser.getUserType() == 3) {
					res = CustomerDB.stmt.executeQuery(querty);
				}
				while(res.next()) {
					password = res.getString("password");
				}
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("B³¹d");
				alert.setHeaderText("Nie uda³o siê zmienic has³a");
				alert.showAndWait();
				e.printStackTrace();
			}
			if(password.equals(old_password.getText())) {
				if(loggedUser.getUserType() == 1) {
					querty = "update manager set password='"+new_password.getText()+"' where id_manager="+loggedUser.getUserID()+";";
				} else if(loggedUser.getUserType() == 2) {
					querty = "update employee set password='"+new_password.getText()+"' where id_employee="+loggedUser.getUserID()+";";
				} else if(loggedUser.getUserType() == 3) {
					querty = "update customer set password='"+new_password.getText()+"' where id_customer="+loggedUser.getUserID()+";";
				} 
				try {
					if(loggedUser.getUserType() == 1) {
						ManagerDB.stmt.executeUpdate(querty);
					} else if(loggedUser.getUserType() == 2) {
						EmployeeDB.stmt.executeUpdate(querty);
					} else if (loggedUser.getUserType() == 3) {
						CustomerDB.stmt.executeUpdate(querty);
					}
				} catch (SQLException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("B³¹d");
					alert.setHeaderText("Nie uda³o siê zmienic has³a");
					alert.showAndWait();
					e.printStackTrace();
					back();
				}
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Zmiana has³a");
				alert.setHeaderText("Pomyœlnie zmieniono has³o");
				alert.showAndWait();
				back();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("B³¹d");
				alert.setHeaderText("Stare has³o jest niepoprawne");
				alert.showAndWait();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Nowe i powtórzone has³o nie s¹ identyczne");
			alert.showAndWait();
		}
	}

	public void setMainController(MainPaneController mainController) {
		this.mainController = mainController;
	}
	
	public void setLoggedUser(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}
}

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
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class LoginController {

	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private Button back;
	@FXML
	private Button login;
	@FXML
	private Text error;
	
	private MainPaneController mainController;
	private int userNumber;
	
	@FXML
	public void login() {
		if (userNumber == 1) {
			ManagerDB.connect();
			ResultSet res;
			String querty = "Select id_manager,first_name,last_name from manager where username like '"+username.getText()+"' and password like '"+password.getText()+"';";
			String id = "";
			String last_name = "";
			String first_name = "";
			try {
				res = ManagerDB.stmt.executeQuery(querty);
				while(res.next()) {
					id = res.getString("id_manager");
					first_name = res.getString("first_name");
					last_name = res.getString("last_name");
				}
			} catch (SQLException e) {
				id = "";
				e.printStackTrace();
			}
			if( id.equals("")) {
				error.setVisible(true);
			} else {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(this.getClass().getResource("/fxml/Manager.fxml"));
				Pane pane = null;
				try {
					pane = loader.load();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mainController.loadScreen(pane);
				ManagerController managerController = loader.getController();
				managerController.setMainController(mainController);
				LoggedUser loggedUser = new LoggedUser(Integer.parseInt(id),first_name,last_name,1);
				managerController.setLoggedUser(loggedUser);
				managerController.setWelcome();
			}
		} else if (userNumber == 2) {
			EmployeeDB.connect();
			ResultSet res;
			String querty = "Select id_employee,first_name,last_name from employee where username like '"+username.getText()+"' and password like '"+password.getText()+"';";
			String id = "";
			String last_name = "";
			String first_name = "";
			try {
				res = EmployeeDB.stmt.executeQuery(querty);
				while(res.next()) {
					id = res.getString("id_employee");
					first_name = res.getString("first_name");
					last_name = res.getString("last_name");
				}
			} catch (SQLException e) {
				id = "";
				e.printStackTrace();
			}
			if( id.equals("")) {
				error.setVisible(true);
			} else {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(this.getClass().getResource("/fxml/Employee.fxml"));
				Pane pane = null;
				try {
					pane = loader.load();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mainController.loadScreen(pane);
				EmployeeController employeeController = loader.getController();
				employeeController.setMainController(mainController);
				LoggedUser loggedUser = new LoggedUser(Integer.parseInt(id),first_name,last_name,2);
				employeeController.setLoggedUser(loggedUser);
				employeeController.setWelcome();
			}
		}
		else if (userNumber == 3) {
			CustomerDB.connect();
			ResultSet res;
			String querty = "Select id_customer,first_name,last_name from customer where username like '"+username.getText()+"' and password like '"+password.getText()+"';";
			String id = "";
			String last_name = "";
			String first_name = "";
			try {
				res = CustomerDB.stmt.executeQuery(querty);
				while(res.next()) {
					id = res.getString("id_customer");
					first_name = res.getString("first_name");
					last_name = res.getString("last_name");
				}
			} catch (SQLException e) {
				id = "";
				e.printStackTrace();
			}
			if( id.equals("")) {
				error.setVisible(true);
			} else {
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
				LoggedUser loggedUser = new LoggedUser(Integer.parseInt(id),first_name,last_name,3);
				customerController.setLoggedUser(loggedUser);
				customerController.setWelcome();
			}
		}
	}
	
	public void back() {
		if(ManagerDB.stmt != null && ManagerDB.conn != null) {
			try {
				ManagerDB.stmt.close();
				ManagerDB.conn.close();
				} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(EmployeeDB.stmt != null && EmployeeDB.conn != null) {
			try {
				EmployeeDB.conn.close();
				EmployeeDB.stmt.close();
				} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(CustomerDB.stmt != null && CustomerDB.conn != null) {
			try {
				CustomerDB.stmt.close();
				CustomerDB.conn.close();
				} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		mainController.loadMenuScreen();
	}
	
	public void setMainController(MainPaneController mainController) {
		this.mainController = mainController;
	}
	public void setUser(int UserNumber) {
		this.userNumber = UserNumber;
	}
}

package controllers;

import java.io.IOException;

import data_base.LoggedUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class EmployeeController {

	@FXML
	private Text welcome;
	@FXML
	private Button cars;
	@FXML
	private Button history;
	@FXML
	private Button rental;
	@FXML
	private Button customer;
	@FXML
	private Button password;
	@FXML
	private Button logout;
	
	private MainPaneController mainController;
	private LoggedUser loggedUser;
	
	@FXML
	public void cars_button() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/CarEmployee.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainController.loadScreen(pane);
		CarEmployeeController carEmployeeController = loader.getController();
		carEmployeeController.setMainController(mainController);
		carEmployeeController.setLoggedUser(loggedUser);
	}
	@FXML
	public void history_button() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/HistoryEmployee.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainController.loadScreen(pane);
		HistoryEmployeeController historyEmployeeController = loader.getController();
		historyEmployeeController.setMainController(mainController);
		historyEmployeeController.setLoggedUser(loggedUser);
	}
	@FXML
	public void rental_button() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/RentalService.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainController.loadScreen(pane);
		RentalServiceController rentalServiceController = loader.getController();
		rentalServiceController.setMainController(mainController);
		rentalServiceController.setLoggedUser(loggedUser);
	}
	@FXML
	public void customer_button() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/CustomerService.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainController.loadScreen(pane);
		CustomerServiceController customerServiceController = loader.getController();
		customerServiceController.setMainController(mainController);
		customerServiceController.setLoggedUser(loggedUser);
	}
	@FXML
	public void password_button() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/PasswordChange.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainController.loadScreen(pane);
		PasswordChangeController passwordChange = loader.getController();
		passwordChange.setMainController(mainController);
		passwordChange.setLoggedUser(loggedUser);
	}
	@FXML
	public void logout_button() {
		this.mainController.loadMenuScreen();
	}
	
	public void setMainController(MainPaneController mainController) {
		this.mainController = mainController;
	}
	public void setWelcome() {
		this.welcome.setText("Witaj! "+loggedUser.getFirstName()+" "+loggedUser.getLastName());
	}
	
	public void setLoggedUser(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}

}

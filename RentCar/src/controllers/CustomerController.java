package controllers;

import java.io.IOException;

import data_base.LoggedUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class CustomerController {

	@FXML
	private Text welcome;
	@FXML
	private Button cars;
	@FXML
	private Button history;
	@FXML
	private Button discount;
	@FXML
	private Button password;
	@FXML
	private Button logout;
	
	private MainPaneController mainController;
	private LoggedUser loggedUser;
	@FXML
	public void cars_button() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/CustomerCar.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainController.loadScreen(pane);
		CustomerCarController customerCarController = loader.getController();
		customerCarController.setMainController(mainController);
		customerCarController.setLoggedUser(loggedUser);
	}
	@FXML
	public void history_button() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/RentalHistory.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainController.loadScreen(pane);
		RentalHistoryController rentalHistoryController = loader.getController();
		rentalHistoryController.setMainController(mainController);
		rentalHistoryController.setLoggedUser(loggedUser);
		rentalHistoryController.load_history();
	}
	@FXML
	public void discount_button() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/CustomerDiscount.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainController.loadScreen(pane);
		CustomerDiscountController customerDiscountController = loader.getController();
		customerDiscountController.setMainController(mainController);
		customerDiscountController.setLoggedUser(loggedUser);
		customerDiscountController.load_discount();
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
		mainController.loadMenuScreen();
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

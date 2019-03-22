package controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class UserChoiceContoller {
	
	@FXML
	private Button manager;
	@FXML
	private Button customer;
	@FXML
	private Button employee;

	private MainPaneController mainController;
	
	@FXML
	public void manager_login() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/Login.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainController.loadScreen(pane);
		LoginController loginController = loader.getController();
		loginController.setMainController(mainController);
		loginController.setUser(1);
	}
	
	@FXML
	public void customer_login() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/Login.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainController.loadScreen(pane);
		LoginController loginController = loader.getController();
		loginController.setMainController(mainController);
		loginController.setUser(3);
	}
	
	@FXML
	public void employee_login() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/Login.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainController.loadScreen(pane);
		LoginController loginController = loader.getController();
		loginController.setMainController(mainController);
		loginController.setUser(2);
	}

	public void setMainController(MainPaneController mainPaneController) {
		this.mainController = mainPaneController;
	}
}
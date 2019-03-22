package controllers;

import java.io.IOException;
import java.sql.SQLException;
import data_base.LoggedUser;
import data_base.ManagerDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ManagerController {

	@FXML
	private Text welcome;
	@FXML
	private Button cars;
	@FXML
	private Button employee;
	@FXML
	private Button password;
	@FXML
	private Button logout;
	@FXML
	private Button backup;
	@FXML
	private Button restore;
	
	private MainPaneController mainController;
	private LoggedUser loggedUser;
	
	@FXML
	public void cars_button() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/CarService.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainController.loadScreen(pane);
		CarServiceController carServiceController = loader.getController();
		carServiceController.setMainController(mainController);
		carServiceController.setLoggedUser(loggedUser);
	}
	@FXML
	public void employee_button() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/EmployeeService.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainController.loadScreen(pane);
		EmployeeServiceController employeeServiceController = loader.getController();
		employeeServiceController.setMainController(mainController);
		employeeServiceController.setLoggedUser(loggedUser);
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
		try {
			ManagerDB.conn.close();
			ManagerDB.stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		mainController.loadMenuScreen();
	}
	@FXML
	public void backup() {
		String executeCmd = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump -u root -proot_password -B rentcar -r C:\\Users\\Admin\\Desktop\\baza.sql";
		Runtime runtime = Runtime.getRuntime();
		try {
			Process p = runtime.exec(executeCmd);
			if(p.waitFor() == 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Backup");
				alert.setHeaderText("Pomyœlnie utworzono kopiê zapasow¹.");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
    			alert.setTitle("B³¹d");
    			alert.setHeaderText("Nie mo¿na utworzyc kopii zapasowej");
    			alert.showAndWait();
			}
		} catch (IOException | InterruptedException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Nie mo¿na utworzyc kopii zapasowej");
			alert.showAndWait();
			e.printStackTrace();
		}
	}
	@FXML
	public void restore() {
		String[] restoreCmd = new String[]{"mysql ", "--user=" + "root", "--password=" + "root_password", "-e", "source " + "C:\\Users\\Admin\\Desktop\\baza.sql"};
		Process runtimeProcess;
		try {
			runtimeProcess = Runtime.getRuntime().exec(restoreCmd);
			int processComplete = runtimeProcess.waitFor();
			if (processComplete == 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Restore");
				alert.setHeaderText("Pomyœlnie wczytano kopiê zapasow¹.");
				alert.showAndWait();
            } else {
            	Alert alert = new Alert(AlertType.ERROR);
    			alert.setTitle("B³¹d");
    			alert.setHeaderText("Nie mo¿na wczytac kopii zapasowej");
    			alert.showAndWait();
            }
		} catch (IOException | InterruptedException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Nie mo¿na wczytac kopii zapasowej");
			alert.showAndWait();
			e.printStackTrace();
		} 
	}
	public void setWelcome() {
		welcome.setText("Witaj, "+loggedUser.getFirstName()+" "+loggedUser.getLastName());
	}
	
	public void setLoggedUser(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}
	
	public void setMainController(MainPaneController mainController) {
		this.mainController = mainController;
	}
}

package controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainPaneController {
	
	@FXML
	private StackPane stackPane; 
	
	@FXML
	public void initialize() {
		loadMenuScreen();
	}
	
	public void loadMenuScreen() {
		stackPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/UserChoice.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		UserChoiceContoller userChoiceController = loader.getController();
		userChoiceController.setMainController(this);
		stackPane.getChildren().add(pane);
	}
	
	public void loadScreen(Pane pane) {
		stackPane.getChildren().clear();
		stackPane.getChildren().add(pane);
	}
}

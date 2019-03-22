package application;
	
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/MainPane.fxml"));
		try {
			StackPane sp = loader.load();
			Scene scene = new Scene(sp);
			primaryStage.setScene(scene);
			primaryStage.setTitle("RentCar");
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

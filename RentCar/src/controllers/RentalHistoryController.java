package controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import data_base.CustomerDB;
import data_base.LoggedUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class RentalHistoryController {

	@FXML
	private Button back;
	@FXML
	private Pane show_pane;
	
	private TableView<History> table;
	private ObservableList<History> histories;
	private MainPaneController mainController;
	private LoggedUser loggedUser;
	
	@FXML
	public void initialize() {
		table = new TableView<History>();
        table.setPrefWidth(550);
        table.setPrefHeight(220);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        show_pane.getChildren().add(table);
        histories = FXCollections.observableArrayList();
        TableColumn<History, String> begin = new TableColumn<>("Pocz¹tek");
        begin.setCellValueFactory(new PropertyValueFactory<>("begin"));
        TableColumn<History, String> end = new TableColumn<>("Koniec");
        end.setCellValueFactory(new PropertyValueFactory<>("end"));
        TableColumn<History, String> car = new TableColumn<>("Samochód");
        car.setCellValueFactory(new PropertyValueFactory<>("car"));
        TableColumn<History, Integer> cost = new TableColumn<>("Koszt");
        cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        table.getColumns().addAll(begin, end, car, cost);
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
	
	public void load_history() {
		ResultSet res;
		String querty = "select begin,end,concat(brand,' ',model) as car,cost from rental join car on "
				+ "car.id_car=rental.id_car where id_customer="+loggedUser.getUserID()+";";
		try {
			res = CustomerDB.stmt.executeQuery(querty);
			while(res.next()) {
				History h = new History();
				h.setBegin(res.getString("begin"));
				h.setCar(res.getString("car"));
				h.setEnd(res.getString("end"));
				h.setCost(res.getInt("cost"));
				histories.add(h);
			}
		} catch (SQLException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Nie uda³o siê odczytac historii.");
			alert.showAndWait();
			e.printStackTrace();
		}
		table.setItems(histories);
	}
	public void setMainController(MainPaneController mainController) {
		this.mainController = mainController;
	}
	public void setLoggedUser(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}
	
	public class History {
		private String begin;
		private String end;
		private String car;
		private int cost;
		
		public String getBegin() {
			return begin;
		}
		public void setBegin(String begin) {
			this.begin = begin;
		}
		public String getEnd() {
			return end;
		}
		public void setEnd(String end) {
			this.end = end;
		}
		public String getCar() {
			return car;
		}
		public void setCar(String car) {
			this.car = car;
		}
		public int getCost() {
			return cost;
		}
		public void setCost(int cost) {
			this.cost = cost;
		}
	}
}

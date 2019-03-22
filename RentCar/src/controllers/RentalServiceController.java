package controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import controllers.CustomerCarController.Car;
import controllers.RentalHistoryController.History;
import data_base.CustomerDB;
import data_base.EmployeeDB;
import data_base.LoggedUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class RentalServiceController {

	@FXML
	private Button back;
	@FXML
	private TextField id_customer_begin;
	@FXML
	private TextField id_car_begin;
	@FXML
	private TextField id_customer_end;
	@FXML
	private Button begin;
	@FXML
	private Button end;
	@FXML
	private Pane show_pane;
	@FXML
	private ComboBox damaged;
	
	private LoggedUser loggedUser;
	private MainPaneController mainController;
	private TableView<Rental> table;
	private ObservableList<Rental> rentals;
	
	@FXML
	public void initialize() {
		ObservableList<String> option = FXCollections.observableArrayList("TAK","NIE");
		damaged.setItems(option);
		table = new TableView<Rental>();
        table.setPrefWidth(320);
        table.setPrefHeight(160);
        show_pane.getChildren().add(table);
        rentals = FXCollections.observableArrayList();
		TableColumn<Rental, Integer> id_rental = new TableColumn<>("ID");
		id_rental.setCellValueFactory(new PropertyValueFactory<>("id_rental"));
		id_rental.setPrefWidth(30);
        TableColumn<Rental, String> begin = new TableColumn<>("Pocz¹tek");
        begin.setCellValueFactory(new PropertyValueFactory<>("begin"));
        begin.setPrefWidth(100);
        TableColumn<Rental, Integer> id_customer = new TableColumn<>("ID Klienta");
        id_customer.setCellValueFactory(new PropertyValueFactory<>("id_customer"));
        id_customer.setPrefWidth(45);
        TableColumn<Rental, String> name = new TableColumn<>("Klient");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setPrefWidth(100);
        TableColumn<Rental, Integer> id_car = new TableColumn<>("ID Samochodu");
        id_car.setCellValueFactory(new PropertyValueFactory<>("id_car"));
        id_car.setPrefWidth(45);
		table.getColumns().addAll(id_rental,begin,id_customer,name,id_car);
		load_rental();
	}
	
	private void load_rental() {
		rentals.clear();
		ResultSet res;
		String querty = "select id_rental,begin,rental.id_car,rental.id_customer,concat(first_name,' ',last_name) as customer "
				+ "from rental join customer on customer.id_customer=rental.id_customer where end is null;";
		try {
			res = EmployeeDB.stmt.executeQuery(querty);
			while(res.next()) {
				Rental r = new Rental();
				r.setId_rental(res.getInt("id_rental"));
				r.setBegin(res.getString("begin"));
				r.setId_customer(res.getInt("id_customer"));
				r.setName(res.getString("customer"));
				r.setId_car(res.getInt("id_car"));
				rentals.add(r);
			}
		} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("B³¹d");
				alert.setHeaderText("Nie uda³o sie odczytac wypo¿yczeñ.");
				alert.showAndWait();
				e.printStackTrace();
		}
		table.setItems(rentals);
	}

	@FXML
	public void back() {
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
		employeeController.setLoggedUser(loggedUser);
		employeeController.setWelcome();
	}
	
	@FXML
	public void end() {
		if(id_customer_end.getText().equals("") || damaged.getValue() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Nie wprowadzono wszystkich danych.");
			alert.showAndWait();
		} else {
			ResultSet res;
			String querty ="call end_rental("+id_customer_end.getText()+",'"+damaged.getValue().toString()+"');";
			try {
				res = EmployeeDB.stmt.executeQuery(querty);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Wypo¿yczenie");
				querty ="select cost from rental where id_rental="+id_customer_end.getText()+";";
				res = EmployeeDB.stmt.executeQuery(querty);
				String cost = "";
				while(res.next()) {
					cost = res.getString("cost");
				}
				alert.setHeaderText("Pomyœlnie zakoñczono wypo¿yczenie auta.\nKoszt wypo¿yczenia wyniós³ "+cost+" z³.");
				alert.showAndWait();
				id_customer_end.setText("");
				load_rental();
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("B³¹d");
				alert.setHeaderText("Nie ma wypo¿yczenia o takim id, lub zosta³o ono zakoñczone.");
				alert.showAndWait();
				e.printStackTrace();
			}
		}
		load_rental();
	}
	
	@FXML
	public void begin() {
		if(id_customer_begin.getText().equals("") || id_car_begin.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Nie wprowadzono wszystkich danych.");
			alert.showAndWait();
		} else {
			String querty ="call begin_rental("+id_customer_begin.getText()+","+id_car_begin.getText()+");";
			try {
				EmployeeDB.stmt.executeQuery(querty);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Wypo¿yczenie");
				alert.setHeaderText("Pomyœlnie wypo¿yczono auto.");
				alert.showAndWait();
				load_rental();
				id_customer_begin.setText("");
				id_car_begin.setText("");
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("B³¹d");
				alert.setHeaderText("Nie uda³o siê wypo¿yczyc auta.");
				alert.showAndWait();
				e.printStackTrace();
			}
		}
	}
	
	public void setLoggedUser(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}
	
	public void setMainController(MainPaneController mainController) {
		this.mainController = mainController;
	}
	
	public class Rental {
		private String begin;
		private int id_customer;
		private int id_rental;
		private int id_car;
		private String name;
		
		public String getBegin() {
			return begin;
		}
		public void setBegin(String begin) {
			this.begin = begin;
		}
		public int getId_customer() {
			return id_customer;
		}
		public void setId_customer(int id_customer) {
			this.id_customer = id_customer;
		}
		public int getId_rental() {
			return id_rental;
		}
		public void setId_rental(int id_rental) {
			this.id_rental = id_rental;
		}
		public int getId_car() {
			return id_car;
		}
		public void setId_car(int id_car) {
			this.id_car = id_car;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
}

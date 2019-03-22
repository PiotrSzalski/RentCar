package controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import data_base.EmployeeDB;
import data_base.LoggedUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class CarEmployeeController {

	@FXML
	private Button back;
	@FXML
	private ComboBox brandBox;
	@FXML
	private TextField number_of_seats;
	@FXML
	private TextField min_cost;
	@FXML
	private TextField max_cost;
	@FXML
	private TextField min_power;
	@FXML
	private TextField max_power;
	@FXML
	private ComboBox typeBox;
	@FXML
	private Button filter;
	@FXML
	private Pane show_pane;
	
	private MainPaneController mainController;
	private LoggedUser loggedUser;
	private ObservableList<String> typeList;
	private ObservableList<String> brandList;
	private TableView<Car> table;
	private ObservableList<Car> cars;
	
	@FXML
	public void initialize() {
		typeList = FXCollections.observableArrayList();
		typeList.add("");
		typeBox.setValue("");
		brandList = FXCollections.observableArrayList();
		brandList.add("");
		brandBox.setValue("");
		table = new TableView<Car>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefWidth(600);
        table.setPrefHeight(220);
        show_pane.getChildren().add(table);
        cars = FXCollections.observableArrayList();
        TableColumn<Car, String> id_car = new TableColumn<>("ID");
        id_car.setCellValueFactory(new PropertyValueFactory<>("id_car"));
        TableColumn<Car, String> brand = new TableColumn<>("Marka");
        brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        TableColumn<Car, String> model = new TableColumn<>("Model");
        model.setCellValueFactory(new PropertyValueFactory<>("model"));
        TableColumn<Car, String> type = new TableColumn<>("Typ");
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Car, String> number_of_seats = new TableColumn<>("Liczba miejsc");
        number_of_seats.setCellValueFactory(new PropertyValueFactory<>("number_of_seats"));
        TableColumn<Car, Integer> deposit = new TableColumn<>("Kaucja");
        deposit.setCellValueFactory(new PropertyValueFactory<>("deposit"));
        TableColumn<Car, Integer> price_per_night = new TableColumn<>("Cena za dob�");
        price_per_night.setCellValueFactory(new PropertyValueFactory<>("price_per_night"));
        TableColumn<Car, String> status = new TableColumn<>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<Car, String> next_inspection = new TableColumn<>("N. przegl�d");
        next_inspection.setCellValueFactory(new PropertyValueFactory<>("next_inspection"));
        TableColumn<Car, String> damaged = new TableColumn<>("Uszkodzony");
        damaged.setCellValueFactory(new PropertyValueFactory<>("damaged"));
        TableColumn<Car, String> power = new TableColumn<>("Moc");
        power.setCellValueFactory(new PropertyValueFactory<>("power"));
        table.getColumns().addAll(id_car,brand,model,type,power,number_of_seats,deposit,price_per_night,damaged,next_inspection,status);
        ResultSet res;
		String querty = "select id_car,brand,model,car_type,number_of_seats,price_per_night,deposit,power,status,next_inspection,damaged from car join tariff on "
				+ "car.tariff=tariff.id_tariff where status not like 'Nie dost�pny';";
		try {
			res = EmployeeDB.stmt.executeQuery(querty);
			while(res.next()) {
				Car c = new Car();
				c.setId_car(res.getInt("id_car"));
				c.setBrand(res.getString("brand"));
				if(!brandList.contains(c.getBrand())) {
					brandList.add(c.getBrand());
				}
				c.setModel(res.getString("model"));
				c.setType(res.getString("car_type"));
				if(!typeList.contains(c.getType())) {
					typeList.add(c.getType());
				}
				c.setNumber_of_seats(res.getInt("number_of_seats"));
				c.setDeposit(res.getInt("deposit"));
				c.setPrice_per_night(res.getInt("price_per_night"));
				c.setPower(res.getInt("power"));
				if(res.getInt("damaged") == 1) {
					c.setDamaged("TAK");
				} else {
					c.setDamaged("NIE");
				}
				c.setNext_inspection(res.getString("next_inspection"));
				c.setStatus(res.getString("status"));
				cars.add(c);
			}
		} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("B��d");
				alert.setHeaderText("Nie uda�o sie odczytac samochod�w.");
				alert.showAndWait();
				e.printStackTrace();
		}
		typeBox.setItems(typeList);
		brandBox.setItems(brandList);
		table.setItems(cars);
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
	public void filter() {
		cars.clear();
		StringBuilder querty = new StringBuilder();
		querty.append("select id_car,brand,model,car_type,number_of_seats,price_per_night,deposit,power,status,next_inspection,damaged from car join tariff on "
				+ "car.tariff=tariff.id_tariff where");
		if(!brandBox.getValue().toString().equals("")) {
			querty.append(" brand like '"+brandBox.getValue().toString()+"' and");
		}
		if(!typeBox.getValue().toString().equals("")) {
			querty.append(" car_type like '"+typeBox.getValue().toString()+"' and");
		}
		if(!number_of_seats.getText().equals("")) {
			querty.append(" number_of_seats="+number_of_seats.getText()+" and");
		}
		if(!min_cost.getText().equals("")) {
			querty.append(" price_per_night>="+min_cost.getText()+" and");
		}
		if(!max_cost.getText().equals("")) {
			querty.append(" price_per_night<="+max_cost.getText()+" and");
		}
		if(!min_power.getText().equals("")) {
			querty.append(" power>="+min_power.getText()+" and");
		}
		if(!max_power.getText().equals("")) {
			querty.append(" power<="+max_power.getText()+" and");
		}
		querty.append(" status not like 'Nie dost�pny';");
		ResultSet res;
		try {
			res = EmployeeDB.stmt.executeQuery(querty.toString());
			while(res.next()) {
				Car c = new Car();
				c.setId_car(res.getInt("id_car"));
				c.setBrand(res.getString("brand"));
				c.setModel(res.getString("model"));
				c.setType(res.getString("car_type"));
				c.setNumber_of_seats(res.getInt("number_of_seats"));
				c.setDeposit(res.getInt("deposit"));
				c.setPrice_per_night(res.getInt("price_per_night"));
				c.setPower(res.getInt("power"));
				if(res.getInt("damaged") == 1) {
					c.setDamaged("TAK");
				} else {
					c.setDamaged("NIE");
				}
				c.setNext_inspection(res.getString("next_inspection"));
				c.setStatus(res.getString("status"));
				cars.add(c);
			}
		} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("B��d");
				alert.setHeaderText("B��dne dane.");
				alert.showAndWait();
				e.printStackTrace();
		}
		table.setItems(cars);
	}
	
	public void setMainController(MainPaneController mainController) {
		this.mainController = mainController;
	}
	public void setLoggedUser(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}
	
	public class Car {
		private int id_car;
		private String brand;
		private String model;
		private String type;
		private int number_of_seats;
		private int deposit;
		private int price_per_night;
		private int power;
		private String status;
		private String next_inspection;
		private String damaged;
		
		public String getBrand() {
			return brand;
		}
		public void setBrand(String brand) {
			this.brand = brand;
		}
		public String getModel() {
			return model;
		}
		public void setModel(String model) {
			this.model = model;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public int getNumber_of_seats() {
			return number_of_seats;
		}
		public void setNumber_of_seats(int number_of_seats) {
			this.number_of_seats = number_of_seats;
		}
		public int getDeposit() {
			return deposit;
		}
		public void setDeposit(int deposit) {
			this.deposit = deposit;
		}
		public int getPrice_per_night() {
			return price_per_night;
		}
		public void setPrice_per_night(int price_per_night) {
			this.price_per_night = price_per_night;
		}
		public int getPower() {
			return power;
		}
		public void setPower(int power) {
			this.power = power;
		}
		public int getId_car() {
			return id_car;
		}
		public void setId_car(int id_car) {
			this.id_car = id_car;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getNext_inspection() {
			return next_inspection;
		}
		public void setNext_inspection(String next_inspection) {
			this.next_inspection = next_inspection;
		}
		public String getDamaged() {
			return damaged;
		}
		public void setDamaged(String damaged) {
			this.damaged = damaged;
		}	
	}
}


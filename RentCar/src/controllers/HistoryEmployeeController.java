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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class HistoryEmployeeController {
	
	@FXML
	private Button back;
	@FXML
	private Button filter;
	@FXML
	private DatePicker begin_after;
	@FXML
	private DatePicker end_before;
	@FXML
	private ComboBox customer;
	@FXML
	private ComboBox car;
	@FXML
	private Pane show_pane;
	
	private LoggedUser loggedUser;
	private MainPaneController mainController;
	private TableView<History> table;
	private ObservableList<History> histories;
	private ObservableList<String> customerList;
	private ObservableList<String> carList;
	
	@FXML
	public void initialize() {
		customerList = FXCollections.observableArrayList();
		customerList.add("");
		customer.setValue("");
		carList = FXCollections.observableArrayList();
		carList.add("");
		car.setValue("");
		table = new TableView<History>();
        table.setPrefWidth(600);
        table.setPrefHeight(210);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        show_pane.getChildren().add(table);
        histories = FXCollections.observableArrayList();
        TableColumn<History, Integer> rental_id = new TableColumn<>("ID");
        rental_id.setCellValueFactory(new PropertyValueFactory<>("id_rental"));
        TableColumn<History, Integer> customer_id = new TableColumn<>("Klient");
        customer_id.setCellValueFactory(new PropertyValueFactory<>("id_customer"));
        TableColumn<History, Integer> car_id = new TableColumn<>("Auto");
        car_id.setCellValueFactory(new PropertyValueFactory<>("id_car"));
        TableColumn<History, String> begin = new TableColumn<>("Pocz¹tek");
        begin.setCellValueFactory(new PropertyValueFactory<>("begin"));
        TableColumn<History, String> end = new TableColumn<>("Koniec");
        end.setCellValueFactory(new PropertyValueFactory<>("end"));
        TableColumn<History, String> damaged = new TableColumn<>("Uszkodzony");
        damaged.setCellValueFactory(new PropertyValueFactory<>("damaged"));
        TableColumn<History, Integer> cost = new TableColumn<>("Koszt");
        cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        table.getColumns().addAll(rental_id,customer_id,car_id,begin,end,damaged,cost);
        ResultSet res;
		String querty = "select id_rental,id_customer,id_car,begin,end,damaged,cost from rental;";
		try {
			res = EmployeeDB.stmt.executeQuery(querty);
			while(res.next()) {
				History h = new History();
				h.setId_rental(res.getInt("id_rental"));
				h.setId_customer(res.getInt("id_customer"));
				if(!customerList.contains(Integer.toString(h.getId_customer()))) {
					customerList.add(Integer.toString(h.getId_customer()));
				}
				h.setId_car(res.getInt("id_car"));
				if(!carList.contains(Integer.toString(h.getId_car()))) {
					carList.add(Integer.toString(h.getId_car()));
				}
				h.setBegin(res.getString("begin"));
				h.setEnd(res.getString("end"));
				int dam = res.getInt("damaged");
				if(dam == 1) {
					h.setDamaged("TAK");
				} else {
					h.setDamaged("NIE");
				}
				h.setCost(res.getInt("cost"));
				histories.add(h);
			}
		} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("B³¹d");
				alert.setHeaderText("Nie uda³o sie odczytac samochodów.");
				alert.showAndWait();
				e.printStackTrace();
		}
		customer.setItems(customerList);
		car.setItems(carList);
		table.setItems(histories);
	}
	
	@FXML
	public void filter() {
		histories.clear();
		ResultSet res;
		StringBuilder sb = new StringBuilder();
		sb.append("select id_rental,id_customer,id_car,begin,end,damaged,cost from rental where");
		if(!customer.getValue().toString().equals("")) {
			sb.append(" id_customer="+customer.getValue().toString()+" and");
		}
		if(!car.getValue().toString().equals("")) {
			sb.append(" id_car="+car.getValue().toString()+" and");
		}
		if(begin_after.getValue() != null) {
			sb.append(" begin>='"+begin_after.getValue().toString()+"' and");
			begin_after.setValue(null);
		}
		if(end_before.getValue() != null) {
			sb.append(" end<='"+end_before.getValue().toString()+"' and");
			end_before.setValue(null);
		}
		sb.append(" 1=1;");
		try {	
			res = EmployeeDB.stmt.executeQuery(sb.toString());
			while(res.next()) {
				History h = new History();
				h.setId_rental(res.getInt("id_rental"));
				h.setId_customer(res.getInt("id_customer"));
				h.setId_car(res.getInt("id_car"));
				h.setBegin(res.getString("begin"));
				h.setEnd(res.getString("end"));
				int dam = res.getInt("damaged");
				if(dam == 1) {
					h.setDamaged("TAK");
				} else {
					h.setDamaged("NIE");
				}
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
	
	public void setMainController(MainPaneController mainController) {
		this.mainController = mainController;
	}
	
	public void setLoggedUser(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}
	
	public class History {
		private int id_rental;
		private int id_customer;
		private int id_car;
		private String begin;
		private String end;
		private String damaged;
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
		public int getCost() {
			return cost;
		}
		public void setCost(int cost) {
			this.cost = cost;
		}
		public int getId_rental() {
			return id_rental;
		}
		public void setId_rental(int id_rental) {
			this.id_rental = id_rental;
		}
		public int getId_customer() {
			return id_customer;
		}
		public void setId_customer(int id_customer) {
			this.id_customer = id_customer;
		}
		public int getId_car() {
			return id_car;
		}
		public void setId_car(int id_car) {
			this.id_car = id_car;
		}
		public String getDamaged() {
			return damaged;
		}
		public void setDamaged(String damaged) {
			this.damaged = damaged;
		}
	}
}

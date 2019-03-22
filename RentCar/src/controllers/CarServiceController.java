package controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import data_base.LoggedUser;
import data_base.ManagerDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class CarServiceController {
	
	@FXML
	private Button previous;
	@FXML
	private Button next;
	@FXML
	private Button back;
	@FXML
	private Text text;
	@FXML
	private Pane add_pane;
	@FXML
	private TextField registration_number;
	@FXML
	private TextField brand;
	@FXML
	private TextField model;
	@FXML
	private TextField number_of_seats;
	@FXML
	private TextField deposit;
	@FXML
	private TextField price_per_night;
	@FXML
	private ComboBox type;
	@FXML
	private DatePicker next_inspection;
	@FXML
	private Button add;
	@FXML
	private TextField power;
	@FXML
	private Pane repair_pane;
	@FXML
	private TextArea damaged_list;
	@FXML
	private TextArea inspection_list;
	@FXML
	private TextField id_repaired;
	@FXML
	private TextField id_inspected;
	@FXML
	private DatePicker date_next;
	@FXML
	private Button enter;
	@FXML
	private Pane edit_pane;
	@FXML
	private TextField edit_id;
	@FXML
	private TextField edit_deposit;
	@FXML
	private TextField edit_price;
	@FXML
	private Button edit;
	@FXML
	private Pane delete_pane;
	@FXML
	private TextField delete_id;
	@FXML
	private Button delete_button;
	@FXML
	private Pane show_pane;
	@FXML
	private RadioButton old_cars;
	
	private MainPaneController mainController;
	private LoggedUser loggedUser;
	private ObservableList<String> typeList;
	private int state;
	private TableView<Car> table;
	private ObservableList<Car> cars;
	
	@FXML
	public void initialize() {
		state = 1;
		typeList = FXCollections.observableArrayList("Kombi","Coupe","Sedan","Limuzna","SUV","Hatchback","Kabriolet");
		type.setItems(typeList);
		next_inspection.setValue(LocalDate.now());
		date_next.setValue(LocalDate.now());
		setLayout();
		table = new TableView<Car>();
        table.setPrefWidth(600);
        table.setPrefHeight(220);
        show_pane.getChildren().add(table);
        cars = FXCollections.observableArrayList();
        TableColumn<Car, Integer> id_car = new TableColumn<>("ID");
        id_car.setCellValueFactory(new PropertyValueFactory<>("id_car"));
        TableColumn<Car, String> registration_number = new TableColumn<>("Nr rejestracyjny");
        registration_number.setCellValueFactory(new PropertyValueFactory<>("registration_number"));
        TableColumn<Car, String> brand = new TableColumn<>("Marka");
        brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        TableColumn<Car, String> model = new TableColumn<>("Model");
        model.setCellValueFactory(new PropertyValueFactory<>("model"));
        TableColumn<Car, String> car_type = new TableColumn<>("Typ");
        car_type.setCellValueFactory(new PropertyValueFactory<>("car_type"));
        TableColumn<Car, Integer> deposit = new TableColumn<>("Kaucja");
        deposit.setCellValueFactory(new PropertyValueFactory<>("deposit"));
        TableColumn<Car, Integer> price_per_night = new TableColumn<>("Cena za dobê");
        price_per_night.setCellValueFactory(new PropertyValueFactory<>("price_per_night"));
        TableColumn<Car, String> damaged = new TableColumn<>("Uszkodzony");
        damaged.setCellValueFactory(new PropertyValueFactory<>("damaged"));
        TableColumn<Car, String> next_inspection = new TableColumn<>("Nastêpny przegl¹d");
        next_inspection.setCellValueFactory(new PropertyValueFactory<>("next_inspection"));
        TableColumn<Car, String> status = new TableColumn<>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        table.getColumns().addAll(id_car,registration_number,brand,model,car_type,deposit,price_per_night,damaged,next_inspection,status);
		load_cars();
		load_inspection();
		load_damaged();
	}
	
	@FXML
	public void go_previous() {
		state--;
		if(state == 0) {
			state = 5;
		}
		setLayout();
	}
	
	@FXML
	public void go_next() {
		state++;
		if(state == 6) {
			state = 1;
		}
		setLayout();
	}
	
	@FXML
	public void back() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/Manager.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainController.loadScreen(pane);
		ManagerController managerController = loader.getController();
		managerController.setMainController(mainController);
		managerController.setLoggedUser(loggedUser);
		managerController.setWelcome();
	}
	
	@FXML
	public void enter_repair() {
		if(id_repaired.getText().equals("") && id_inspected.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Nie wprowadzono id.");
			alert.showAndWait();
		} else {
			if(!id_repaired.getText().equals("")) {
				String querty = "update car set damaged=0 where id_car="+id_repaired.getText()+";";
				try {
					ManagerDB.stmt.executeUpdate(querty);
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Naprawa");
					alert.setHeaderText("Pomyœlnie wprowadzono informacje o naprawie auta.");
					alert.showAndWait();
					damaged_list.setText("");;
					load_damaged();
					id_repaired.setText("");
					load_cars();
				} catch (SQLException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("B³¹d");
					alert.setHeaderText("Nie uda³o sie wprowadzic inforamcji o naprawie auta.");
					alert.showAndWait();
					e.printStackTrace();
				}
			}
			if(!id_inspected.getText().equals("")) {
				if(date_next.getValue().isBefore(LocalDate.now())) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("B³¹d");
					alert.setHeaderText("Wprowadzono niepoprawn¹ datê.");
					alert.showAndWait();
				} else {
					String querty = "update car set next_inspection='"+date_next.getValue()+"' where id_car="+id_inspected.getText()+";";
					try {
						ManagerDB.stmt.executeUpdate(querty);
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Nastêpny przegl¹d");
						alert.setHeaderText("Pomyœlnie zmieniono datê nastêpnego przegl¹du dla pojazdu o id "+id_inspected.getText()+" na dzieñ "+date_next.getValue()+".");
						alert.showAndWait();
						inspection_list.setText("");;
						load_inspection();
						id_inspected.setText("");
						date_next.setValue(LocalDate.now());
						load_cars();
					} catch (SQLException e) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("B³¹d");
						alert.setHeaderText("Nie uda³o sie zmienic daty nastêpnego przegl¹du.");
						alert.showAndWait();
						e.printStackTrace();
					}
				}
			} 
		}
	}
	
	@FXML
	public void delete_car() {
		if(delete_id.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Nie wprowadzono id.");
			alert.showAndWait();
		} else {
			String querty = "update car set status='Nie dostêpny',damaged=null,next_inspection=null where id_car="+delete_id.getText()+";";
			try {
				ManagerDB.stmt.executeUpdate(querty);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Usuniêcie samochodu");
				alert.setHeaderText("Pomyœlnie usuniêto samochód.");
				alert.showAndWait();
				delete_id.setText("");
				load_cars();
				load_inspection();
				load_damaged();
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("B³¹d");
				alert.setHeaderText("Samochód jest aktualnie wypo¿yczony lub nie ma go ju¿ w wypo¿yczalni.");
				alert.showAndWait();
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	public void edit_tariff() {
		if(edit_id.getText().equals("") || edit_deposit.getText().equals("") || edit_price.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Nie wprowadzono wszystkich danych.");
			alert.showAndWait();
		} else {
			ResultSet res;
			String id = "";
			String querty = "select id_tariff from tariff where deposit="+edit_deposit.getText()+" and price_per_night="+edit_price.getText()+";";
			try {
				res = ManagerDB.stmt.executeQuery(querty);
				while(res.next()) {
					id = res.getString("id_tariff");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(id.equals("")) {
				querty = "insert into tariff (deposit,price_per_night) values ("+edit_deposit.getText()+","+edit_price.getText()+");";
				try {
					ManagerDB.stmt.executeUpdate(querty);
					querty = "select id_tariff from tariff where deposit="+edit_deposit.getText()+" and price_per_night="+edit_price.getText()+";";
					res = ManagerDB.stmt.executeQuery(querty);
					while(res.next()) {
							id = res.getString("id_tariff");
					}
				} catch (SQLException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("B³¹d");
					alert.setHeaderText("Nie uda³o sie dodac takiej taryfy.");
					alert.showAndWait();
					e.printStackTrace();
				}
			} else {
				querty = "update car set tariff="+id+" where id_car="+edit_id.getText()+";";
				try {
					ManagerDB.stmt.executeUpdate(querty);
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Zmiana taryfy");
					alert.setHeaderText("Pomyœlnie zmieniono taryfê.");
					alert.showAndWait();
					edit_id.setText("");
					edit_deposit.setText("");
					edit_price.setText("");
					load_cars();
				} catch (SQLException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("B³¹d");
					alert.setHeaderText("Samochód jest wypo¿yczony lub nie ma go ju¿ w wypo¿yczalni.");
					alert.showAndWait();
					e.printStackTrace();
				}
			}
			
		}
	}
	@FXML
	public void add_car() {
		if(registration_number.getText().equals("") || brand.getText().equals("") || model.getText().equals("") || 
				number_of_seats.getText().equals("") || number_of_seats.getText().equals("") || price_per_night.getText().equals("") ||
				type.getValue() == null || power.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Wprowadzono niepoprawne dane.");
			alert.showAndWait();
		} else {
			ResultSet res;
			String id = "";
			String querty = "select id_tariff from tariff where deposit="+deposit.getText()+" and price_per_night="+price_per_night.getText()+";";
			try {
				res = ManagerDB.stmt.executeQuery(querty);
				while(res.next()) {
					id = res.getString("id_tariff");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(id.equals("")) {
				querty = "insert into tariff (deposit,price_per_night) values ("+deposit.getText()+","+price_per_night.getText()+");";
				try {
					ManagerDB.stmt.executeUpdate(querty);
					querty = "select id_tariff from tariff where deposit="+deposit.getText()+" and price_per_night="+price_per_night.getText()+";";
					res = ManagerDB.stmt.executeQuery(querty);
					while(res.next()) {
							id = res.getString("id_tariff");
					}
				} catch (SQLException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("B³¹d");
					alert.setHeaderText("Nie uda³o sie dodac takiej taryfy.");
					alert.showAndWait();
					e.printStackTrace();
				}
			}
			querty = "insert into car (registration_number,brand,model,car_type,number_of_seats,tariff,damaged,next_inspection,power,status) "
					+ "values ('"+registration_number.getText()+"','"+brand.getText()+"','"+model.getText()+"','"+type.getValue().toString()+
					"',"+number_of_seats.getText()+","+id+",0,'"+next_inspection.getValue()+"',"+power.getText()+",1);";
			try {
				ManagerDB.stmt.executeUpdate(querty);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Dodanie samochodu");
				alert.setHeaderText("Pomyœlnie dodano samochód.");
				alert.showAndWait();
				registration_number.setText("");
				brand.setText("");
				model.setText("");
				number_of_seats.setText("");
				next_inspection.setValue(LocalDate.now());
				deposit.setText("");
				price_per_night.setText("");
				power.setText("");
				load_cars();
				load_inspection();
				load_damaged();
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("B³¹d");
				alert.setHeaderText("Nie uda³o sie dodac samochodu.");
				alert.showAndWait();
				e.printStackTrace();
			}
			
		}
	}
	
	private void load_cars() {
		cars.clear();
		ResultSet res;
		String querty;
		if(!old_cars.isSelected()) {
			 querty = "select id_car,registration_number,brand,model,car_type,deposit,price_per_night,damaged,next_inspection,status "
			 		+ "from car join tariff on car.tariff=tariff.id_tariff where status not like 'Nie dostêpny';";
			 try {
				res = ManagerDB.stmt.executeQuery(querty);
				while(res.next()) {
					Car c = new Car();
					c.setId_car(res.getInt("id_car"));
					c.setBrand(res.getString("brand"));
					c.setRegistration_number(res.getString("registration_number"));
					c.setModel(res.getString("model"));
					c.setCar_type(res.getString("car_type"));
					if(res.getInt("damaged") == 0) {
						c.setDamaged("NIE");
					} else {
						c.setDamaged("TAK");
					}
					c.setDeposit(res.getInt("deposit"));
					c.setPrice_per_night(res.getInt("price_per_night"));
					c.setNext_inspection(res.getString("next_inspection"));
					c.setStatus(res.getString("status"));
					cars.add(c);
				}
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("B³¹d");
				alert.setHeaderText("Nie uda³o sie odczytac samochodów.");
				alert.showAndWait();
				e.printStackTrace();
			}
		} else {
			 querty = "select id_car,registration_number,brand,model,car_type,deposit,price_per_night,damaged,next_inspection,status "
				 		+ "from car join tariff on car.tariff=tariff.id_tariff;";
				 try {
					res = ManagerDB.stmt.executeQuery(querty);
					while(res.next()) {
						Car c = new Car();
						c.setId_car(res.getInt("id_car"));
						c.setBrand(res.getString("brand"));
						c.setRegistration_number(res.getString("registration_number"));
						c.setModel(res.getString("model"));
						c.setCar_type(res.getString("car_type"));
						if(res.getInt("damaged") == 0) {
							c.setDamaged("NIE");
						} else {
							c.setDamaged("TAK");
						}
						c.setDeposit(res.getInt("deposit"));
						c.setPrice_per_night(res.getInt("price_per_night"));
						c.setNext_inspection(res.getString("next_inspection"));
						c.setStatus(res.getString("status"));
						if(c.getStatus().equals("Nie dostêpny")) {
							c.setDamaged("-");
						}
						cars.add(c);
					}
				} catch (SQLException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("B³¹d");
					alert.setHeaderText("Nie uda³o sie odczytac samochodów.");
					alert.showAndWait();
					e.printStackTrace();
				}
		}
		table.setItems(cars);
	}
	
	@FXML
	public void old_car() {
		load_cars();
	}
	
	private void setLayout() {
		if(state == 1) {
			text.setText("Wyœwietl samochochody");
			show_pane.setVisible(true);
			add_pane.setVisible(false);
			edit_pane.setVisible(false);
			delete_pane.setVisible(false);
			repair_pane.setVisible(false);
			enter.setVisible(false);
			old_cars.setVisible(true);
		} else if (state == 2) {
			text.setText("Dodaj samochochód");
			show_pane.setVisible(false);
			add_pane.setVisible(true);
			edit_pane.setVisible(false);
			delete_pane.setVisible(false);
			repair_pane.setVisible(false);
			enter.setVisible(false);
			old_cars.setVisible(false);
		} else if (state == 3) {
			text.setText("Zmieñ taryfê");
			show_pane.setVisible(false);
			add_pane.setVisible(false);
			edit_pane.setVisible(true);
			delete_pane.setVisible(false);
			repair_pane.setVisible(false);
			enter.setVisible(false);
			old_cars.setVisible(false);
		} else if (state == 4) {
			text.setText("Usuñ samochochód");
			show_pane.setVisible(false);
			add_pane.setVisible(false);
			edit_pane.setVisible(false);
			delete_pane.setVisible(true);
			repair_pane.setVisible(false);
			enter.setVisible(false);
			old_cars.setVisible(false);
		} else if (state == 5) {
			text.setText("Naprawa samochodów");
			show_pane.setVisible(false);
			add_pane.setVisible(false);
			edit_pane.setVisible(false);
			delete_pane.setVisible(false);
			repair_pane.setVisible(true);
			enter.setVisible(true);
			old_cars.setVisible(false);
		}
	}
	
	private void load_damaged() {
		damaged_list.setText("");
		ResultSet res;
		String querty = "select id_car,registration_number,brand,model from car where damaged=1;";
		try {
			res = ManagerDB.stmt.executeQuery(querty);
			while(res.next()) {
				damaged_list.appendText(res.getString("id_car")+"     ");
				damaged_list.appendText(res.getString("registration_number")+"     ");
				damaged_list.appendText(res.getString("brand")+" ");
				damaged_list.appendText(res.getString("model")+"\n");
			}
		} catch (SQLException e) {
			damaged_list.setText("Nie mo¿na za³adowac");
		}
	}
	
	private void load_inspection() {
		inspection_list.setText("");
		ResultSet res;
		String querty = "select id_car,registration_number,brand,model,next_inspection from car where datediff(date_add(curdate(),interval 14 day),next_inspection)>0;";
		try {
			res = ManagerDB.stmt.executeQuery(querty);
			while(res.next()) {
				inspection_list.appendText(res.getString("id_car")+"     ");
				inspection_list.appendText(res.getString("registration_number")+"     ");
				inspection_list.appendText(res.getString("brand")+" ");
				inspection_list.appendText(res.getString("model")+"     ");
				inspection_list.appendText(res.getString("next_inspection")+"\n");
			}
		} catch (SQLException e) {
			inspection_list.setText("Nie mo¿na za³adowac");
		}
	}
	public void setMainController(MainPaneController mainController) {
		this.mainController = mainController;
	}
	
	public void setLoggedUser(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}
	
	public class Car {
		private int id_car;
		private String registration_number;
		private String brand;
		private String model;
		private String car_type;
		private int deposit;
		private int price_per_night;
		private String damaged;
		private String next_inspection;
		private String status;
		
		public int getId_car() {
			return id_car;
		}
		public void setId_car(int id_car) {
			this.id_car = id_car;
		}
		public String getRegistration_number() {
			return registration_number;
		}
		public void setRegistration_number(String registration_number) {
			this.registration_number = registration_number;
		}
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
		public String getCar_type() {
			return car_type;
		}
		public void setCar_type(String car_type) {
			this.car_type = car_type;
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
		public String getDamaged() {
			return damaged;
		}
		public void setDamaged(String damaged) {
			this.damaged = damaged;
		}
		public String getNext_inspection() {
			return next_inspection;
		}
		public void setNext_inspection(String next_inspection) {
			this.next_inspection = next_inspection;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
	}
}

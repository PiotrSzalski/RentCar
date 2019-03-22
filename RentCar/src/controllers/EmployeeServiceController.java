package controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

import data_base.LoggedUser;
import data_base.ManagerDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class EmployeeServiceController {

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
	private TextField username;
	@FXML
	private TextField first_name;
	@FXML
	private TextField last_name;
	@FXML
	private TextField pesel;
	@FXML
	private TextField phone_number;
	@FXML
	private TextField address;
	@FXML
	private Button add_button;
	@FXML
	private Pane show_pane;
	@FXML
	private Pane delete_pane;
	@FXML
	private TextField delete_id;
	@FXML
	private Button delete;
	@FXML
	private Pane edit_pane;
	@FXML
	private TextField edit_id;
	@FXML
	private TextField edit_first_name;
	@FXML
	private TextField edit_last_name;
	@FXML
	private TextField edit_pesel;
	@FXML
	private TextField edit_phone_number;
	@FXML
	private TextField edit_address;
	@FXML
	private TextField edit_username;
	@FXML
	private Button edit;
	
	private TableView<Employee> table;
	private ObservableList<Employee> workers;
	private LoggedUser loggedUser;
	private MainPaneController mainController;
	private int state;
	
	@FXML
	public void initialize() {
		state = 1;
		setLayout();
		table = new TableView<Employee>();
        table.setPrefWidth(500);
        table.setPrefHeight(220);
        show_pane.getChildren().add(table);
        workers = FXCollections.observableArrayList();
        TableColumn<Employee, Integer> id_employee = new TableColumn<>("ID");
        id_employee.setCellValueFactory(new PropertyValueFactory<>("Id"));
        id_employee.setPrefWidth(20);
        TableColumn<Employee, String> first_name = new TableColumn<>("Imiê");
        first_name.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        first_name.setPrefWidth(80);
        TableColumn<Employee, String> last_name = new TableColumn<>("Nazwisko");
        last_name.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        last_name.setPrefWidth(80);
        TableColumn<Employee, String> phone_number = new TableColumn<>("Nr telefonu");
        phone_number.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        phone_number.setPrefWidth(100);
        TableColumn<Employee, String> pesel = new TableColumn<>("PESEL");
        pesel.setCellValueFactory(new PropertyValueFactory<>("pesel"));
        pesel.setPrefWidth(100);
        TableColumn<Employee, String> address = new TableColumn<>("Adres");
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        address.setPrefWidth(155);
        table.getColumns().addAll(id_employee, first_name, last_name, phone_number, pesel, address);
		load_workers();
	}
	
	@FXML
	public void go_previous() {
		state--;
		if(state == 0) {
			state = 4;
		}
		setLayout();
	}
	
	@FXML
	public void go_next() {
		state++;
		if(state == 5) {
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
	public void delete_employee() {
		if(delete_id.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Nie wprowadzono ID");
			alert.showAndWait();
		} else {
			String querty = "delete from employee where id_employee="+delete_id.getText()+";";
			try {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Potwierdzenie");
				alert.setHeaderText("Czy na pewno chcesz usun¹c pracownika o id "+delete_id.getText()+"?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					int res = ManagerDB.stmt.executeUpdate(querty);
					if(res == 1) {
						alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Usnuniecie pracownika");
						alert.setHeaderText("Pomyœlnie usuniêto pracownika");
						alert.showAndWait();
					} else {
						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Usnuniecie pracownika");
						alert.setHeaderText("Nie ma pracownika o podanym id");
						alert.showAndWait();
					}
					delete_id.setText("");
					workers.clear();
					load_workers();
				} else {
					delete_id.setText("");
				}
				
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("B³¹d");
				alert.setHeaderText("Nie mo¿na usun¹c pracownika o podanym id");
				alert.showAndWait();
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	public void edit_button() {
		if(edit_id.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Nie podano id");
			alert.showAndWait();
		} else if (edit_first_name.getText().equals("") && edit_last_name.getText().equals("") && edit_username.getText().equals("") && edit_pesel.getText().equals("") && edit_address.getText().equals("") && edit_phone_number.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Nie podano ¿adnych danych");
			alert.showAndWait();
		} else {
			String querty = "";
			StringBuilder header = new StringBuilder();
			StringBuilder header2 = new StringBuilder();
			int rows = 0;
			header.append("Edytowano:\n");
			header2.append("Nie uda³o siê edytowac:\n");
			if(!(edit_first_name.getText().equals(""))) {
				querty = "update employee set first_name='"+edit_first_name.getText()+"' where id_employee="+edit_id.getText()+";";
				try {
					rows += ManagerDB.stmt.executeUpdate(querty);
					header.append("-imie\n");
				} catch (SQLException e) {
					header2.append("-imie\n");
					e.printStackTrace();
				}
			} 
			if (!(edit_last_name.getText().equals(""))) {
				querty = "update employee set last_name='"+edit_last_name.getText()+"' where id_employee="+edit_id.getText()+";";
				try {
					rows += ManagerDB.stmt.executeUpdate(querty);
					header.append("-nazwisko\n");
				} catch (SQLException e) {
					header2.append("-nazwisko\n");
					e.printStackTrace();
				}
			}
			if (!(edit_username.getText().equals(""))) {
				querty = "update employee set username='"+edit_username.getText()+"' where id_employee="+edit_id.getText()+";";
				try {
					rows += ManagerDB.stmt.executeUpdate(querty);
					header.append("-nazwa u¿ytkownika\n");
				} catch (SQLIntegrityConstraintViolationException e) {
					rows++;
					header2.append("-nazwa u¿ytkownika\n");
				} catch (SQLException e) {
					header2.append("-nazwa urzytkownika\n");
					e.printStackTrace();
				}
			}
			if (!(edit_pesel.getText().equals(""))) {
				querty = "update employee set pesel='"+edit_pesel.getText()+"' where id_employee="+edit_id.getText()+";";
				try {
					rows += ManagerDB.stmt.executeUpdate(querty);
					header.append("-pesel\n");
				} catch (SQLException e) {
					header2.append("-pesel\n");
					e.printStackTrace();
				}
			}
			if (!(edit_phone_number.getText().equals(""))) {
				querty = "update employee set phone_number='"+edit_phone_number.getText()+"' where id_employee="+edit_id.getText()+";";
				try {
					rows += ManagerDB.stmt.executeUpdate(querty);
					header.append("-numer telefonu\n");
				} catch (SQLException e) {
					header2.append("-numer telefonu\\n");
					e.printStackTrace();
				}
			}
			if (!(edit_address.getText().equals(""))) {
				querty = "update employee set address='"+edit_address.getText()+"' where id_employee="+edit_id.getText()+";";
				try {
					rows += ManagerDB.stmt.executeUpdate(querty);
					header.append("-adres\n");
				} catch (SQLException e) {
					header2.append("-adres\n");
					e.printStackTrace();
				}
			}
			if(rows == 0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("B³¹d");
				alert.setHeaderText("Nie ma pracownika o podanym id, lub podano b³êdne dane");
				alert.showAndWait();
			} else {
				StringBuilder result = new StringBuilder();
				if(!(header.toString().equals("Edytowano:\n"))) {
					result.append(header.toString());
					result.append("\n");
				}
				if (!(header2.toString().equals("Nie uda³o siê edytowac:\n"))) {
					result.append(header2.toString());
				}
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Edycja pracownika");
				alert.setHeaderText(result.toString());
				alert.showAndWait();
			}
			edit_id.setText("");
			edit_first_name.setText("");
			edit_last_name.setText("");
			edit_pesel.setText("");
			edit_phone_number.setText("");
			edit_address.setText("");
			edit_username.setText("");
			workers.clear();
			load_workers();
		}
	}
	
	@FXML
	public void add_employee() {
		if(username.getText().equals("") || first_name.getText().equals("") || last_name.getText().equals("") || pesel.getText().equals("") ||
				address.getText().equals("") || phone_number.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Nie wprowadzono wszystkich danych");
			alert.showAndWait();
		}
		else {
			String querty = "insert into employee (username,password,first_name,last_name,phone_number,pesel,address) values ('"+
					username.getText()+"','','"+first_name.getText()+"','"+last_name.getText()+"','"+ phone_number.getText()+"','"
					+pesel.getText()+"','"+address.getText()+"');";
			try {
				ManagerDB.stmt.executeUpdate(querty);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Dodanie pracownika");
				alert.setHeaderText("Pomyœlnie dodano pracownika");
				alert.showAndWait();
				username.setText("");
				first_name.setText("");
				last_name.setText("");
				phone_number.setText("");
				pesel.setText("");
				address.setText("");
				workers.clear();
				load_workers();
			} catch (SQLIntegrityConstraintViolationException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("B³¹d");
				alert.setHeaderText("Pracownik o podanej nazwie ju¿ istnieje");
				alert.showAndWait();
				e.printStackTrace();
			} catch (SQLException e2) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("B³¹d");
				alert.setHeaderText("Podano b³êdne dane.");
				alert.showAndWait();
				e2.printStackTrace();
			}
		}
	}
	
	public void load_workers() {
		ResultSet res;
		String querty = "Select id_employee,first_name,last_name,phone_number,pesel,address from employee;";
		try {
			res = ManagerDB.stmt.executeQuery(querty);
			while(res.next()) {
				Employee e = new Employee();
				e.setId(Integer.parseInt(res.getString("id_employee")));
				e.setFirst_name(res.getString("first_name"));
				e.setLast_name(res.getString("last_name"));
				e.setPhone_number(res.getString("phone_number"));
				e.setPesel(res.getString("pesel"));
				e.setAddress(res.getString("address"));
				workers.add(e);
			}
		} catch (SQLException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Nie uda³o siê odczytac pracowników");
			alert.showAndWait();
			e.printStackTrace();
		}
		table.setItems(workers);
	}
	
	public void setLayout() {
		if(state == 1) {
			text.setText("Wyœwietl pracowników");
			add_pane.setVisible(false);
			show_pane.setVisible(true);
			delete_pane.setVisible(false);
			edit_pane.setVisible(false);
		} else if(state == 2) {
			text.setText("Dodaj pracownika");
			add_pane.setVisible(true);
			show_pane.setVisible(false);
			delete_pane.setVisible(false);
			edit_pane.setVisible(false);
		} else if(state == 3) {
			text.setText("Edytuj pracownika");
			add_pane.setVisible(false);
			show_pane.setVisible(false);
			delete_pane.setVisible(false);
			edit_pane.setVisible(true);
		} else if(state == 4) {
			text.setText("Usuñ pracownika");
			add_pane.setVisible(false);
			show_pane.setVisible(false);
			delete_pane.setVisible(true);
			edit_pane.setVisible(false);
		}
	}
	
	public void setLoggedUser(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}
	
	public void setMainController(MainPaneController mainController) {
		this.mainController = mainController;
	}
	
	public class Employee {
		
		private int id_employee;
		private String first_name;
		private String last_name;
		private String phone_number;
		private String pesel;
		private String address;
		
		public int getId() {
			return id_employee;
		}
		public void setId(int id) {
			this.id_employee = id;
		}
		public String getFirst_name() {
			return first_name;
		}
		public void setFirst_name(String first_name) {
			this.first_name = first_name;
		}
		public String getLast_name() {
			return last_name;
		}
		public void setLast_name(String last_name) {
			this.last_name = last_name;
		}
		public String getPhone_number() {
			return phone_number;
		}
		public void setPhone_number(String phone_number) {
			this.phone_number = phone_number;
		}
		public String getPesel() {
			return pesel;
		}
		public void setPesel(String pesel) {
			this.pesel = pesel;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
	}
}

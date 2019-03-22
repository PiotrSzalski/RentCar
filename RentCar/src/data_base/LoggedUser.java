package data_base;

public class LoggedUser {

	private int userID;
	private String first_name;
	private String last_name;
	private int userType;
	
	public LoggedUser(int userId, String first_name, String last_name, int userType) {
		this.userID = userId;
		this.first_name = first_name;
		this.last_name = last_name;
		this.userType = userType;
	}

	public int getUserID() {
		return userID;
	}
	
	public String getFirstName() {
		return first_name;
	}
	
	public String getLastName() {
		return last_name;
	}
	
	public int getUserType() {
		return userType;
	}
}

package model;

/*blueprint for user class*/

public abstract class User implements Quotable {
	private String password;
	private String userName;
	private String firstName;
	private String surname;
	private Workspace workspace;
	private String quote;

	private String imageString;
	
	public User(String password, String userName, String firstName, String surname) {
		this.password = password;
		this.userName = userName;
		this.firstName = firstName;
		this.surname = surname;
	}

	public String getUserName() {
		return this.userName;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getSurname() {
		return this.surname;
	}
	
	public String getFullName() {
		return this.firstName + " "+ this.surname;
	}
	
	public void setUserName(String username) {
		this.userName = username;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
		
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getPassword() {
		return this.password;
	}

	public  Workspace getWorkspace() {
		return this.workspace;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public String getImageString() {
		return imageString;
	}

	public void setImageString(String imageString) {
		this.imageString = imageString;
	}

}

package application;

import java.util.ArrayList;
import java.util.List;

import model.Authenticator;
import model.DefaultUser;
import model.User;

//approach of singleton - user holder class
public class UserHolder {
	

	public static List<User> listOfUsers = new ArrayList<User>();

	Authenticator auth = new Authenticator(listOfUsers);
	
	private DefaultUser dUser;
	
	private final static UserHolder UserInstance = new UserHolder();
	
	public static UserHolder getUserInstance() {
		return UserInstance;
	}
	
	public void setUser(DefaultUser user) {
		this.dUser = user;
	}
	
	public DefaultUser getUser() {
		return this.dUser;
	}
	
	public Authenticator getAuth() {
		return this.auth;
	}
	
	public List<User> getList() {
		return this.listOfUsers;
	}

}

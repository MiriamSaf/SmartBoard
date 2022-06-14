package model;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.text.Text;
import model.User;

/*class deals with authenication of user
 * stores a list of all users which is used to
 * sign in users */
public class Authenticator {

    public List<User> listOfUsers = new ArrayList<User>();

    public Authenticator(List<User> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }
    
    public void setListUsers(List<User> listOfUsers) {
    	this.listOfUsers = listOfUsers;
    }

    //adds a new user
    public void addUser(User user) {
        this.listOfUsers.add(user);
    }
    
    public List<User> getUsers() {
    	return this.listOfUsers;
    }
    
    //checks if users password matched with chosen pwd
    public boolean validateChosenPassword(String usernm, String password) {
    	try {
			for (int i=0; i< listOfUsers.size(); i++) {
				if((usernm.equals(listOfUsers.get(i).getUserName())) && (password.equals(listOfUsers.get(i).getPassword()))){
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        // Password is incorrect
        throw new SecurityException();
    }
    
    public boolean checkDuplicateUserName(String username, List<User> listOfUsers2) {
		for (int i = 0; i < listOfUsers2.size(); i++) {
			if (listOfUsers2.get(i).getUserName().equals(username)) {
				return true;
			}
		}
		return false;
	}
    

}

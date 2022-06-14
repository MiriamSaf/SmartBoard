package controller;

import database.DataBaseMaster;

import java.io.IOException;

import java.util.ArrayList;

import application.UserHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.DefaultUser;
import model.User;
import model.Authenticator;
import model.Board;
import model.Workspace;

/*deals with logging in the user to the main application*/
public class LoginController {

	public static ArrayList<Board> listOfBoards = new ArrayList<Board>();

	// through the masterDB we can access the database
	DataBaseMaster masterDB = new DataBaseMaster();

	UserHolder holder = UserHolder.getUserInstance();
	@FXML
	private Text hiddenText;

	@FXML
	private Text password;

	@FXML
	private PasswordField passwordInput;

	@FXML
	private Hyperlink register;

	@FXML
	private Button signIn;

	@FXML
	private Text userName;

	@FXML
	private TextField usernameInput;

	private Stage stage;
	private Scene scene;
	private Parent root;

	// go back to register sign up page
	@FXML
	void registerUserBack(ActionEvent event) throws IOException {

		// load
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SignUp.fxml"));
		root = loader.load();

		// get the instance of signup controller
		SignUpController signUpcontroller = loader.getController();

		// get current scene and replace the current scene with login
		stage = (Stage) register.getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.setTitle("Sign Up");
		stage.show();
	}

	/* signs in the user if the password and username matched stored user */
	@FXML
	void signInUser(ActionEvent event) throws IOException {

		String usernm = usernameInput.getText();
		String passwd = passwordInput.getText();
		String fullname;

		if (usernm.isEmpty()) {
			hiddenText.setText("Username field must not be left empty. Please try again");
		}
		if (passwd.isEmpty()) {
			hiddenText.setText("Password field must not be left empty. Please try again");
		}

		// if password and username matches table of users - show that it is working and
		// log in user

		// create a dummy user to store and compare all users passwords against the
		// current one
		// do not save it in our users as that would expose the database
		UserHolder user = new UserHolder();
		Authenticator auth = user.getAuth();

		masterDB.selectAllUsers(user);

		for (int i = 0; i < auth.getUsers().size(); i++) {
			String username = auth.getUsers().get(i).getUserName();
			String password = auth.getUsers().get(i).getPassword();

			if (passwd.equals(password) && usernm.equals(username)) {
				if (holder.getUser() == null) {

					// if theres a saved img string path
					if (auth.getUsers().get(i).getImageString() != null) {
						DefaultUser deUser = new DefaultUser(passwd, usernm, auth.getUsers().get(i).getFirstName(),
								auth.getUsers().get(i).getSurname(), auth.getUsers().get(i).getImageString());
						holder.setUser(deUser);
						holder.getUser().setImageString(auth.getUsers().get(i).getImageString());

					} else {
						DefaultUser deUser = new DefaultUser(passwd, usernm, auth.getUsers().get(i).getFirstName(),
								auth.getUsers().get(i).getSurname(), "/default.jpg");
						holder.setUser(deUser);
						// set as the default img as they have no img saved
						holder.getUser().setImageString("/default.jpg");

					}

					Workspace defaultWorkspace;
					defaultWorkspace = new Workspace(listOfBoards);
					holder.getUser().setWorkspace(defaultWorkspace);
					loadLists(holder);

				}
				
				

				// quote should be different each time
				String quote = holder.getUser().randomQuote();

				holder.getUser().setQuote(quote);

				fullname = holder.getUser().getFirstName() + " " + holder.getUser().getSurname();

				// load
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProjectBoard.fxml"));
				root = loader.load();

				// get the instance of signup controller
				ProjectBoardController controller = loader.getController();
				// display full name and pass it into controller
				controller.displayName(fullname);
				controller.displayImg();
				controller.setVisibleElements();
				controller.setRandomQuote(quote);
				controller.showTabsAfterSignIn();
				controller.addColumn();

				try {
					stage = (Stage) signIn.getScene().getWindow();
					stage.setScene(new Scene(root));
					stage.setTitle("SmartBoard");
					stage.show();

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				hiddenText.setText("password/and or username are invalid. Please try again");
			}

		}

	}

	// load the lists from the database - accessed using facade to DB
	public void loadLists(UserHolder user) {
		// find the users saved boards from the DB
		masterDB.selectAllBoards(user);
		masterDB.selectAllColumns(user);
		masterDB.selectAllTTasks(user);

		user.getUser().getWorkspace().setLoadedLists(true);
	}

}

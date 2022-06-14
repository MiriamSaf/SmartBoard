package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import application.UserHolder;
import database.DataBaseMaster;
import database.UserDataBaseManager;
import javafx.application.Platform;
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
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Authenticator;
import model.DefaultUser;
import model.User;

/*deals with signing up the user and creating an account*/
public class SignUpController {
	private static FileInputStream fileInputStream = null;

	DataBaseMaster masterDB = new DataBaseMaster();

	UserHolder holder = UserHolder.getUserInstance();

	public FileInputStream fInput;

	UserDataBaseManager db = new UserDataBaseManager();
	public static List<User> listOfUsers = new ArrayList<User>();

	Authenticator auth = new Authenticator(listOfUsers);

	public String imgPath;
	@FXML
	private TextField firstNameInput;

	@FXML
	private ImageView image;

	@FXML
	private TextField lastNameInput;

	@FXML
	private Text password;

	@FXML
	private PasswordField passwordInput;

	@FXML
	private Hyperlink signInHyperlink;

	@FXML
	private Hyperlink profileSelect;

	@FXML
	private Button signUpBtn;

	@FXML
	private Text signUpInfo;

	@FXML
	private Text surname;

	@FXML
	private TextField userNameInput;

	@FXML
	private Text username;

	@FXML
	private Image imgNotViewable;

	private Stage stage;
	private Scene scene;
	private Parent root;

	// close sign up btn
	@FXML
	void closeActionSignUp(ActionEvent event) {
		Platform.exit();
	}
	
	//check lengths of input
	public boolean checkLength(String fieldName, int length, String text) {
		if(text.length() > length) {
			signUpInfo.setText("Error: "+fieldName+" must not be greater than "+length+" characters");
			return true;
		}
		return false;
	}

	// sign up button takes to next page if all fields filled in
	@FXML
	void nextActionSignUp(ActionEvent event) throws IOException {
		String usernm = userNameInput.getText();
		String firstname = firstNameInput.getText();
		String surnm = lastNameInput.getText();
		String passwd = passwordInput.getText();

		if (usernm.isEmpty()) {
			signUpInfo.setText("Error. Please do not leave username input empty");
		}
		if (firstname.isEmpty()) {
			signUpInfo.setText("Error. Please do not leave firstname input empty");
		}
		if (surnm.isEmpty()) {
			signUpInfo.setText("Error. Please do not leave surname input empty");
		}
		if (passwd.isEmpty()) {
			signUpInfo.setText("Error. Please do not leave password input empty");
		}

		if(!checkLength("username", 15, usernm ) && !checkLength("firstname", 15, firstname) && !checkLength("surname", 20, surnm) && !checkLength("password", 30, passwd)) {
		if (!usernm.isEmpty() && !firstname.isEmpty() && !surnm.isEmpty() && !passwd.isEmpty()) {

			signUpInfo.setText("Signed up successfully");
			// load
			FXMLLoader loaderSignUp = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
			root = loaderSignUp.load();

			// get the instance of login controller
			LoginController logincontroller = loaderSignUp.getController();

			// user name must be unique
			// of no one else has same username
			UserHolder user = new UserHolder();
			Authenticator auth = user.getAuth();
			//save all the users to dummy user just for checking 
			masterDB.selectAllUsers(user);
			boolean duplicateUser = false;
			
			if(auth.checkDuplicateUserName(usernm, auth.getUsers())) {
				signUpInfo.setText("Error. There is already a user with that username");
				duplicateUser = true;
			}

			if (!duplicateUser) {
				if (image.getImage() == null) {
					//System.out.println("Image is null");

					//ImageView card = new ImageView("/default.jpg");
					UserHolder holder = UserHolder.getUserInstance();
					DefaultUser deUser = new DefaultUser(passwd, usernm, firstname, surnm, "/default.jpg");

					holder.setUser(deUser);

					auth.addUser(deUser);
					db.insertRowIntoChosenTable( usernm, firstname, surnm, passwd, "/default.jpg");

					db.selectUserDBQuery(holder);

				} else {
					// Image Assigned with the image path
					UserHolder holder = UserHolder.getUserInstance();
					DefaultUser deUser = new DefaultUser(passwd, usernm, firstname, surnm, imgPath);
					holder.setUser(deUser);
					//set the image string to the user holder so can be seen on sign up
					holder.getUser().setImageString(imgPath);


					db.insertRowIntoChosenTable(usernm, firstname, surnm, passwd, imgPath);
					db.selectUserDBQuery(holder);

					//add this user to the list
					auth.addUser(deUser);

				}
			

			// get current scene and replace the current scene with login
			stage = (Stage) signUpBtn.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setTitle("Log In");
			stage.show();
			
			}
		} else {
			signUpInfo.setText("Error. Please do not leave any input empty");
		}
		}
	}

	// upload a picture and save the path of the picture
	@FXML
	void uploadPicture(ActionEvent event) {

		FileChooser filechooser = new FileChooser();

		// set extension filter
		ExtensionFilter extFiler = new ExtensionFilter("*.png", "*.jpg", "*.jpeg");

		filechooser.getExtensionFilters().add(extFiler);

		// show file open dialog
		File fileSelect = filechooser.showOpenDialog(stage);

		// the following code to grab the path of the image adapted from
		// https://stackoverflow.com/questions/46828724/javafx-filechooser-save-image-to-resources-and-save-path-to-database
		if (fileSelect != null) {
			try {
				String imageSavedPath = fileSelect.toURI().toURL().toString();
				imgPath = imageSavedPath;
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		// show the image chosen on screen
		InputStream fileInpStream;
		try {
			// show the
			fileInpStream = new FileInputStream(fileSelect);
			image.setImage(new Image(fileInpStream));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//take to login screen if have a
	@FXML
	void AccountSignInExisting(ActionEvent event) throws IOException {

		FXMLLoader loaderSignUp = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
		root = loaderSignUp.load();
		LoginController logincontroller = loaderSignUp.getController();
		// get current scene and replace the current scene with login
		stage = (Stage) signUpBtn.getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.setTitle("Log In");
		stage.show();
	}
}

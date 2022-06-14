package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.tree.DefaultTreeCellEditor.DefaultTextField;

import application.UserHolder;
import database.DataBaseMaster;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/*profile controller - deals with uploading a new image and updating firstname and surname
 * of the user */
public class ProfileController {

	DataBaseMaster masterDB = new DataBaseMaster();

	@FXML
	private Button Cancel;

	@FXML
	private TextField firstNameInp;

	@FXML
	private Text profileClick;

	@FXML
	private ImageView profileImage;

	@FXML
	private Image imgNotSeenPassedThrough;

	@FXML
	private Image imgNotViewable;

	@FXML
	private Text profileMsg;

	@FXML
	private Button saveChangesBtn;

	@FXML
	private TextField surameInp;

	@FXML
	private Text userTextShow;

	private Stage stage;
	private Scene scene;
	private Parent root;

	public boolean chosenImg;

	// displays username on screen of profile
	public void displayUser(String user) {
		userTextShow.setText(user);

	}

	// cancel button - send to main board
	@FXML
	void cancelBtn(ActionEvent event) throws IOException {
		show("/views/ProjectBoard.fxml", Cancel, "Smart Board");
	}

	// select new image for profile
	@FXML
	void imageSelect(ActionEvent event) {

		UserHolder holder = UserHolder.getUserInstance();
		FileChooser filechooser = new FileChooser();

		// set extension filter
		ExtensionFilter extFiler = new ExtensionFilter("*.png", "*.jpg", "*.jpeg");

		filechooser.getExtensionFilters().add(extFiler);

		// show file open dialog
		File fileSelect = filechooser.showOpenDialog(stage);
		InputStream fileInpStream;

		if (fileSelect != null) {
			try {
				String imageSavedPath = fileSelect.toURI().toURL().toString();
				Image imagePath = new Image(imageSavedPath, 500, 500, true, true, true);
				// Set image to the path that we grabbed
				imgNotViewable = imagePath;
				// imageSavedString = imageSavedPath;
				holder.getUser().setImageString(imageSavedPath);
				masterDB.updateUserImage(holder.getUser().getUserName(), imageSavedPath);
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		try {
			fileInpStream = new FileInputStream(fileSelect);
			profileImage.setImage(new Image(fileInpStream));

			// UserHolder holder = UserHolder.getUserInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}

		chosenImg = true;

	}

	// save profile changes and show back to main board
	@FXML
	void saveBtnProf(ActionEvent event) throws IOException {

		UserHolder holder = UserHolder.getUserInstance();

		String firstNameUpdate = firstNameInp.getText();
		String surnameUpdate = surameInp.getText();

		// only go through if inputs not empty
		if (!checkNotEmpty("firstname", firstNameUpdate) && !checkNotEmpty("surname", surnameUpdate)) {
			if (!checkLength("firstname", 15, firstNameUpdate) && !checkLength("surname", 20, surnameUpdate)) {
				// change the current firstname and surname
				holder.getUser().setFirstName(firstNameUpdate);
				holder.getUser().setSurname(surnameUpdate);

				masterDB.updateSurnameAndFirstname(holder.getUser().getUserName(), firstNameUpdate, surnameUpdate);

				// then show project board if inputs changed
				show("/views/ProjectBoard.fxml", saveChangesBtn, "Smart Board");
			}
		}

	}

	// show method to show back to main board
	public void show(String fxml, Button btn, String title) throws IOException {

		UserHolder holder = UserHolder.getUserInstance();
		String quote = holder.getUser().getQuote();
		String fullname = holder.getUser().getFirstName() + " " + holder.getUser().getSurname();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
		root = loader.load();

		// holder.getUser().setImage(imgNotViewable);
		// get the instance of controller and set the elements as they were
		// previously
		ProjectBoardController controller = loader.getController();
		controller.displayName(fullname);
		controller.setRandomQuote(quote);
		controller.showTabs();
		controller.addColumn();
		controller.displayImg();

		// get current scene and replace the current scene with login
		stage = (Stage) btn.getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.setTitle(title);
		stage.show();
	}

	// set txt and imgs when come on screen
	public void setTextImgFields() {
		UserHolder holder = UserHolder.getUserInstance();

		Image img = new Image(holder.getUser().getImageString());
		profileImage.setImage(img);
		firstNameInp.setText(holder.getUser().getFirstName());
		surameInp.setText(holder.getUser().getSurname());
	}

	// check length
	public boolean checkLength(String fieldName, int length, String text) {
		if (text.length() > length) {
			profileMsg.setText("Error: " + fieldName + " must not be greater than " + length + " characters");
			return true;
		}
		return false;
	}

	// check not empty inputs
	public boolean checkNotEmpty(String fieldName, String text) {
		if (text.isEmpty()) {
			profileMsg.setText("Error: " + fieldName + " must not be left empty");
			return true;
		}
		return false;
	}

}

package controller;

import java.io.IOException;
import java.util.ArrayList;

import application.UserHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Board;

//rename a column
public class RenameColumnController {
	@FXML
	private Button cancelBtn;

	@FXML
	private TextField columnName;

	@FXML
	private Text hiddenText;

	@FXML
	private Text infoText;

	@FXML
	private Button okayBtn;

	private String currColumnName;
	private int currTab;

	private Stage stage;
	private Scene scene;
	private Parent root;


	//change a columns name
	@FXML
	void ChangeColName(ActionEvent event) throws IOException {
		UserHolder holder = UserHolder.getUserInstance();

		ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();

		String newName = columnName.getText();
		boolean duplicate = false;

		checkIfEmpty(newName);
		if (!checkLength(newName)) {
			if (!checkNotEmpty(newName)) {
				// loop through boards
				for (int i = 0; i < boards.size(); i++) {

					for (int j = 0; j < boards.get(i).getColumn().size(); j++) {
						// check no column with this name exist before add a new one with same name
						if (boards.get(i).getColumn().get(j).getColumnName().equals(newName)) {
							infoText.setText("That name already exists for a column on this board. Try another name");
							duplicate = true;
							return;
						}
					}

					// get current tab that board is on to remove list from
					if (i == currTab) {
						// loop through current columns
						for (int j = 0; j < boards.get(i).getColumn().size(); j++) {
							// it is a matching column to the one user chose
							if (boards.get(i).getColumn().get(j).getColumnName().equals(currColumnName)) {
								// set the new name for the column
								boards.get(i).getColumn().get(j).setColumnName(newName);
							}
						}
					}
				}

				// call project board controller if no duplicate name named for col
				if (!duplicate) {
					callBackToMain(okayBtn);
				}
			}
		}
	}
	

	//check length of the input text
	public boolean checkLength(String text) {
		if (text.length() > 20) {
			infoText.setText("Error: column name must be less than 20 characters");
			return true;
		}
		return false;
	}

	//check is not empty input
	public boolean checkNotEmpty(String text) {
		if (text.isEmpty()) {
			infoText.setText("Error: column name must not be left blank");
			return true;
		}
		return false;
	}

	private void checkIfEmpty(String newName) {
		if (newName.isEmpty()) {
			hiddenText.setText("You must enter a new name for the column or press cancel");
		}
	}

	//cancel - go back to main
	@FXML
	void cancelBtn(ActionEvent event) throws IOException {
		callBackToMain(cancelBtn);
	}

	//get curr col and set it 
	public void getCurrColumn(String name) {
		hiddenText.setText("Current column name is: " + name);
		currColumnName = name;

	}

	//get curr tab
	public void getCurrTab(int selectedIndex) {
		currTab = selectedIndex;

	}

	//call back to main board
	public void callBackToMain(Button btn) throws IOException {

		UserHolder holder = UserHolder.getUserInstance();
		String quote = holder.getUser().getQuote();
		String fullname = holder.getUser().getFirstName() + " " + holder.getUser().getSurname();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProjectBoard.fxml"));
		root = loader.load();

		// get the instance of controller and reset the elements as they were previously
		ProjectBoardController controller = loader.getController();
		// set the controller functions
		controller.displayImg();
		controller.showTabs();
		controller.addColumn();
		controller.displayName(fullname);
		controller.setRandomQuote(quote);

		stage = (Stage) btn.getScene().getWindow();
		stage.setScene(new Scene(root));
	}
}

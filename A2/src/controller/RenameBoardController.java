package controller;

import java.io.IOException;
import java.util.ArrayList;

import application.UserHolder;
import database.DataBaseMaster;
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

//rename a board
public class RenameBoardController {

	DataBaseMaster masterDB = new DataBaseMaster();

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

	private int currentTab;
	private String currentTabName;

	private Stage stage;
	private Scene scene;
	private Parent root;

	// check length of input
	public boolean checkLength(String text) {
		if (text.length() > 20) {
			hiddenText.setText("Error: board name must be less than 20 characters");
			return true;
		}
		return false;
	}

	// changes the column name to new input and sets in DB
	@FXML
	void ChangeColName(ActionEvent event) throws IOException {
		UserHolder holder = UserHolder.getUserInstance();
		ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();

		holder.getUser().getWorkspace().getSelectedTabName();
		String boardName = columnName.getText();

		boolean lengthCheck = checkLength(boardName);
		boolean duplicate = false;
		if (!lengthCheck) {
			checkIfEmpty(boardName);
			if (!boardName.isEmpty()) {
				// loop through boards
				for (int i = 0; i < boards.size(); i++) {
					// get current tab that board is on to remove list from
					if (i == currentTab) {
						for (int j = 0; j < boards.size(); j++) {
							//check if there is a board already with this name
							if (boardName.equals(boards.get(j).getBoardName())) {
								duplicate = true;
								hiddenText.setText(
										"Error: you cannot name the board the same name as another board. Please retry");
							}
						}
						String userName = holder.getUser().getUserName();
						String oldBoardName = boards.get(i).getBoardName();

						boards.get(i).setBoardName(boardName);

						masterDB.editBoard(userName, oldBoardName, boardName);
					}
				}
				if(!duplicate) {
				callBackToMain(okayBtn);
				}
			}
		}
	}

	//check if empty
	private void checkIfEmpty(String colName) {
		if (colName.isEmpty()) {
			hiddenText.setText("You must enter a new name for the board or press cancel");
		}

	}

	//cancel back to main
	@FXML
	void cancelBtn(ActionEvent event) throws IOException {
		callBackToMain(cancelBtn);
	}

	public void getCurrTab(int indexTab) {
		currentTab = indexTab;

	}

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

	//get curr name
	public void getCurrNameForDef(String currTabName) {
		currentTabName = currTabName;
		infoText.setText("Current board name is: "+currentTabName);

	}
}

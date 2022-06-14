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
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Board;
import model.Column;
import model.Task;
import model.Utilities;

//create a new column in column list
public class NewColumnController {

	DataBaseMaster masterDB = new DataBaseMaster();
	Utilities utils = new Utilities();

	@FXML
	private TextField columnName;

	@FXML
	private Text infoText;

	@FXML
	private Button okayBtn;

	@FXML
	private Button cancelBtn;
	private Stage stage;
	private Scene scene;
	private Parent root;
	private int currTab;

	// create a new column and add to the DB and the list
	@FXML
	void CreateColumnBtn(ActionEvent event) throws IOException {

		UserHolder holder = UserHolder.getUserInstance();
		String columnTitle = columnName.getText();
		boolean duplicate = false;
		if (!checkNotEmpty(columnTitle) && !checkLength(columnTitle)) {

			ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();
			ArrayList<Task> listTask = new ArrayList<Task>();
			Column column = new Column(columnTitle, listTask);

			// add column name to the user holders boards
			for (int i = 0; i < boards.size(); i++) {

				// loop through columns
				for (int j = 0; j < boards.get(i).getColumn().size(); j++) {
					// check no boards with this name exist before add a new one with same name
					if (boards.get(i).getColumn().get(j).getColumnName().equals(columnTitle)) {
						infoText.setText("That name already exists for a column on this board. Try another name");
						duplicate = true;
						return;
					}
				}

				// get current tab that board is on to add the new column to
				if (i == currTab) {
					int sizeCol = boards.get(i).getColumn().size();
					// add the column to the board
					boards.get(i).addColumnToBoard(column);
					// add col to DB
					String userName = holder.getUser().getUserName();
					// add col to DB
					masterDB.addColumn(userName, i, columnTitle, sizeCol);
				}
			}

			if (!duplicate) {
				callBackToMain(okayBtn);
			}
		}

	}

	// check length of string
	public boolean checkLength(String text) {
		if (text.length() > 20) {
			infoText.setText("Error: column name must be less than 20 characters");
			return true;
		}
		return false;
	}

	// check string not empty
	public boolean checkNotEmpty(String text) {
		if (text.isEmpty()) {
			infoText.setText("Error: column name must not be left blank");
			return true;
		}
		return false;
	}

	// cancel new column creation
	@FXML
	void cancelBtn(ActionEvent event) throws IOException {
		callBackToMain(cancelBtn);
	}

	public void getCurrTab(int i) {
		currTab = i;
	}

	// call back to main controller
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

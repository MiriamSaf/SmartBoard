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
import javafx.stage.Stage;
import model.Board;

//column delete controller
public class DeleteColumnController {

	DataBaseMaster masterDB = new DataBaseMaster();

	@FXML
	private Button cancelBtn;

	@FXML
	private TextField columnName;

	@FXML
	private Button deleteBtn;

	private String currCul;
	private int selectedTab;

	private Stage stage;
	private Scene scene;
	private Parent root;

	// send back to main and do nothing with the column
	@FXML
	void cancelBtn(ActionEvent event) throws IOException {

		// call project board controller
		callBackToMain(cancelBtn);

	}

	// delete the column
	@FXML
	void deleteColumn(ActionEvent event) throws IOException {

		UserHolder holder = UserHolder.getUserInstance();

		ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();

		// loop through boards
		for (int i = 0; i < boards.size(); i++) {
			// get current tab that board is on to remove list from
			if (i == selectedTab) {
				// loop through current columns
				for (int j = 0; j < boards.get(i).getColumn().size(); j++) {
					// get the column name
					String currColName = boards.get(i).getColumn().get(j).getColumnName();
					// it is a matching column to the one user chose
					if (boards.get(i).getColumn().get(j).getColumnName().equals(currCul)) {
						// delete from list
						boards.get(i).deleteChosen(currCul);
						// delete col from DB
						String userName = holder.getUser().getUserName();

						// delete col from curr board with this username
						masterDB.deleteColumn(userName, i, currColName);
						masterDB.deleteTaskKnowCol(userName, i, j);
					}
				}
			}
		}
		// call project board controller
		callBackToMain(deleteBtn);

	}

	// get current column name from prev controller
	public void getCurrColumn(String name) {
		columnName.setText(name);
		currCul = name;

	}

	//get curr tab and set it in controller
	public void getCurrTab(int selectedIndex) {
		selectedTab = selectedIndex;

	}

	// call back to main controller - proj board
	public void callBackToMain(Button deleteBtn2) throws IOException {

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

		stage = (Stage) deleteBtn2.getScene().getWindow();
		stage.setScene(new Scene(root));
	}

}

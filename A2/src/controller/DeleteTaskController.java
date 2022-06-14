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

//deletes the task chosen
public class DeleteTaskController {

	DataBaseMaster masterDB = new DataBaseMaster();
	@FXML
	private Button cancelBtn;

	@FXML
	private Button deleteBtn;

	@FXML
	private TextField taskName;

	private Stage stage;
	private Scene scene;
	private Parent root;
	private String taskDelName;
	private String colName;
	private int currTab;

	// cancel goes back to main
	@FXML
	void cancelBtn(ActionEvent event) throws IOException {
		callBackToMain(cancelBtn);
	}

	// deletes a task
	@FXML
	void deleteTask(ActionEvent event) throws IOException {

		UserHolder holder = UserHolder.getUserInstance();

		ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();

		// loop through boards
		for (int i = 0; i < boards.size(); i++) {
			// get current tab that board is on to remove list from
			if (i == currTab) {
				// loop through current columns
				for (int j = 0; j < boards.get(i).getColumn().size(); j++) {
					// it is a matching column to the one user chose
					if (boards.get(i).getColumn().get(j).getColumnName().equals(colName)) {
						// delete the chosen task from the matching board -> column
						boards.get(i).getColumn().get(j).deleteChosen(taskDelName);

						String userName = holder.getUser().getUserName();
						masterDB.deleteTask(userName, i, j, taskDelName);
					}
				}
			}
		}
		// call project board controller
		callBackToMain(deleteBtn);
	}

	// call back to proj board
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

	// get curr tab
	public void getCurrTab(int selectedIndex) {
		currTab = selectedIndex;

	}

	// get curr task name
	public void getCurrTaskName(String name) {
		taskDelName = name;
		taskName.setText(name);
	}

	// get curr column
	public void getCurrColumn(String name) {
		colName = name;

	}
}

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

//edit task controller
public class EditTaskController {

	DataBaseMaster masterDB = new DataBaseMaster();

	@FXML
	private Button cancelBtn;

	@FXML
	private Text hiddenText;

	@FXML
	private Text infoText;

	@FXML
	private Button okayBtn;

	@FXML
	private TextField taskDescript;

	@FXML
	private TextField taskName;

	private Stage stage;
	private Scene scene;
	private Parent root;

	private String taskChosenName;
	private String colName;
	private int currTab;

	// change task name and description - edit
	@FXML
	void ChangeTaskDetail(ActionEvent event) throws IOException {
		String taskDescribe = taskDescript.getText();
		String taskNameField = taskName.getText();

		if (!checkNotDuplicateTaskName(taskNameField)) {
			if (!taskDescribe.isEmpty() && !taskNameField.isEmpty()) {

				// check task length and description match database constraints
				if (!(checkLength(taskNameField, 20, "task name"))
						&& !(checkLength(taskDescribe, 100, "task description"))) {

					UserHolder holder = UserHolder.getUserInstance();

					ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();

					// loop through boards
					for (int i = 0; i < boards.size(); i++) {
						// get current tab that board is on to remove list from
						if (i == currTab) {
							// loop through current columns\
							for (int j = 0; j < boards.get(i).getColumn().size(); j++) {
								// it is a matching column to the one user chose
								if (boards.get(i).getColumn().get(j).getColumnName().equals(colName)) {
									// loop through task list until find match
									for (int v = 0; v < boards.get(i).getColumn().get(j).getTasks().size(); v++) {
										// there is a matching task so can edit it
										String nameTask = boards.get(i).getColumn().get(j).getTasks().get(v)
												.getTaskName();
										// if the task matches then set its new name and description
										if (nameTask.equals(taskChosenName)) {
											String oldTaskName = boards.get(i).getColumn().get(j).getTasks().get(v)
													.getTaskName();

											boards.get(i).getColumn().get(j).getTasks().get(v)
													.setTaskName(taskNameField);
											boards.get(i).getColumn().get(j).getTasks().get(v)
													.setTaskDescrip(taskDescribe);

											String userName = holder.getUser().getUserName();
											// edit current task in DB save
											masterDB.editTask(oldTaskName, taskNameField, taskDescribe, userName, i, j);
										}
									}
								}
							}
						}
					}
					// call project board controller
					callBackToMain(okayBtn);

				}
			} else {
				hiddenText.setText("Error: Please do not leave input blank");
			}
		}
	}

	// cancel = go back to main
	@FXML
	void cancelBtn(ActionEvent event) throws IOException {
		callBackToMain(cancelBtn);
	}

	// check its not a duplicate task name that it is being changed to
	public boolean checkNotDuplicateTaskName(String newTaskName) {

		UserHolder holder = UserHolder.getUserInstance();

		ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();
		// loop through boards
		for (int i = 0; i < boards.size(); i++) {
			if (i == currTab) {
				// loop through columns
				for (int y = 0; y < boards.get(i).getColumn().size(); y++) {
					// loop through tasks
					for (int v = 0; v < boards.get(i).getColumn().get(y).getTasks().size(); v++) {
						// find name of task and see if matches chosen new name
						String nameTask = boards.get(i).getColumn().get(y).getTasks().get(v).getTaskName();
						if(newTaskName.equals(taskChosenName)) {
							//as it can be its own name
							return false;
						}
						if (nameTask.equals(newTaskName)) {
							hiddenText.setText(
									"Error: you cannot name a task the same name as another task in this column");
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	// call back to proj board
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

	// check length of string
	public boolean checkLength(String text, int length, String type) {
		if (text.length() > length) {
			hiddenText.setText("Error: task " + type + " must be less than " + length + " characters");
			return true;
		}
		return false;
	}

	// get curr tab
	public void getCurrTab(int selectedIndex) {
		currTab = selectedIndex;

	}

	
	// get curr task name
	public void getCurrTaskName(String name) {
		taskChosenName = name;
		taskName.setText(name);
	}

	// get curr col
	public void getCurrColumn(String name) {
		colName = name;

	}

	// set task description on screen
	public void setTaskDescrip(String descript) {
		taskDescript.setText(descript);
	}
}

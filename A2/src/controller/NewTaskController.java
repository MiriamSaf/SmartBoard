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
import model.Column;
import model.Task;

//new task creation 
public class NewTaskController {

	DataBaseMaster masterDB = new DataBaseMaster();

	@FXML
	private Button cancelBtn;

	@FXML
	private Text hiddenText;

	@FXML
	private Button okayBtn;

	@FXML
	private TextField taskDescripInp;

	@FXML
	private TextField taskNameInp;

	private String columnName;
	private int currTab;

	private Stage stage;
	private Scene scene;
	private Parent root;

	// create a new task
	@FXML
	void CreateTaskBtn(ActionEvent event) throws IOException {

		UserHolder holder = UserHolder.getUserInstance();

		String taskName = taskNameInp.getText();
		String taskDescript = taskDescripInp.getText();

		checkNoEmpty(taskName, taskDescript);

		ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();
		Task task = new Task(taskName, taskDescript);

		if (!checkNotDuplicateTaskName(taskName)) {
			if (!taskName.isEmpty() && !taskDescript.isEmpty()) {
				// check task length and description match database constraints
				if (!(checkLength(taskName, 20, "task name"))
						&& !(checkLength(taskDescript, 100, "task description"))) {

					String columnNameSaved;
					// loop through boards
					for (int i = 0; i < boards.size(); i++) {
						// get current tab that board is on to add the new
						if (i == currTab) {
							// loop through current columns
							for (int j = 0; j < boards.get(i).getColumn().size(); j++) {
								// if column matches with current column name
								columnNameSaved = boards.get(i).getColumn().get(j).getColumnName();
								// the column matches with current column
								if (columnNameSaved.equals(columnName)) {

									boards.get(i).getColumn().get(j).addTaskToBoard(task);
									// add task to DB
									String userName = holder.getUser().getUserName();
									int boardNum = i;
									masterDB.addTask(userName, boardNum, j, taskName, taskDescript);
								}
							}
						}
					}

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

					stage = (Stage) okayBtn.getScene().getWindow();
					stage.setScene(new Scene(root));
				}
			}
		}

	}

	// cancel new task and go back to main controller
	@FXML
	void cancelTaskBtn(ActionEvent event) throws IOException {

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

		stage = (Stage) okayBtn.getScene().getWindow();
		stage.setScene(new Scene(root));

	}

	// check not duplicate task name being created
	public boolean checkNotDuplicateTaskName(String newTaskName) {

		UserHolder holder = UserHolder.getUserInstance();

		ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();

		for (int i = 0; i < boards.size(); i++) {
			if (i == currTab) {
				for (int y = 0; y < boards.get(i).getColumn().size(); y++) {

					for (int v = 0; v < boards.get(i).getColumn().get(y).getTasks().size(); v++) {
						// find name of task and see if matches chosen new name
						String nameTask = boards.get(i).getColumn().get(y).getTasks().get(v).getTaskName();
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

	// check length
	public boolean checkLength(String text, int length, String type) {
		if (text.length() > length) {
			hiddenText.setText("Error: task " + type + " must be less than " + length + " characters");
			return true;
		}
		return false;
	}

	// check not empty
	public void checkNoEmpty(String taskName, String taskDescript) {
		if (taskName.isEmpty()) {
			hiddenText.setText("Error: task name input field cannot be left blank");
		}
		if (taskDescript.isEmpty()) {
			hiddenText.setText("Error: task description field cannot be left blank");
		}
	}

	// get the current tab from previous controller
	public void getCurrTab(int selectedIndex) {
		currTab = selectedIndex;

	}

	// get current column name from prev controller
	public void getCurrColumn(String name) {
		columnName = name;

	}
}

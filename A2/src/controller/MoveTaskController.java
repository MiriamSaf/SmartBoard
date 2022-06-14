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
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Board;
import model.Task;

//moves the task to the left or the right column
public class MoveTaskController {

	DataBaseMaster masterDB = new DataBaseMaster();

	@FXML
	private Button cancelBtn;

	@FXML
	private Button leftCol;

	@FXML
	private Button rightCol;

	@FXML
	private Text hiddenText;

	private Stage stage;
	private Scene scene;
	private Parent root;

	private String taskChosenName;
	private String colName;
	private int currTab;

	//cancel btn - back to main
	@FXML
	void cancel(ActionEvent event) {
		try {
			callBackToMain(cancelBtn);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	//move task to the left
	@FXML
	void moveLeft(ActionEvent event) {
		UserHolder holder = UserHolder.getUserInstance();

		ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();

		Task chosenTask = null;
		int chosenCol = 0;
		boolean moveTask = false;
		// loop through boards
		for (int i = 0; i < boards.size(); i++) {
			// get current tab that board is on to remove task from
			if (i == currTab) {
				// loop through current columns
				for (int j = 0; j < boards.get(i).getColumn().size(); j++) {
					// it is a matching column to the one user chose
					if (boards.get(i).getColumn().get(j).getColumnName().equals(colName)) {
						// loop through task list until find match
						for (int v = 0; v < boards.get(i).getColumn().get(j).getTasks().size(); v++) {
							// there is a matching task so can get it
							String nameTask = boards.get(i).getColumn().get(j).getTasks().get(v).getTaskName();
							// if the task matches then set remove it 
							if (nameTask.equals(taskChosenName)) {
								if (j - 1 >= 0) {
									//get the column -1
									chosenCol = j - 1;
									//save the chosen task
									chosenTask = boards.get(i).getColumn().get(j).getOneTask(v);
									//remove the task 
									boards.get(i).getColumn().get(j).getTasks().remove(v);
									
									masterDB.editTaskColumn(nameTask, holder.getUser().getUserName(), i, j, chosenCol);
									moveTask = true;
								} else {
									hiddenText.setText("Unable to move task to the left. There is no column there");
								}
							}
						}
					}
				}
			}
		}
		// move the chosen task over a column
		if (moveTask == true) {
			for (int i = 0; i < boards.size(); i++) {
				// get current tab that board is on to remove list from
				// System.out.println("its true to move task " + chosenCol);
				if (i == currTab) {
					// loop through current columns
					for (int j = 0; j < boards.get(i).getColumn().size(); j++) {
						if (j == chosenCol) {
							//add the task to the column on the left
							boards.get(i).getColumn().get(j).getTasks().add(chosenTask);
							try {
								//call back to main board
								callBackToMain(leftCol);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				} else {
					hiddenText.setText("Unable to move task to the left. Please create a column first");
				}

			}
		}
	}

	//move task right
	@FXML
	void moveRight(ActionEvent event) {
		moveTask();
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

	//get curr tab
	public void getCurrTab(int selectedIndex) {
		currTab = selectedIndex;

	}

	//set the curr col
	public void getCurrColumn(String name) {
		colName = name;

	}

	//set the curr task name
	public void getCurrTaskName(String name) {
		taskChosenName = name;
	}

	//move task to the right
	public void moveTask() {
		UserHolder holder = UserHolder.getUserInstance();
		ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();

		Task chosenTask = null;
		int column = 0;
		boolean moveTask = false;
		// loop through boards
		for (int i = 0; i < boards.size(); i++) {
			// get current tab that board is on to remove list from
			if (i == currTab) {
				// loop through current columns\
				int colSize = boards.get(i).getColumn().size();
				for (int j = 0; j < boards.get(i).getColumn().size(); j++) {
					// it is a matching column to the one user chose
					if (boards.get(i).getColumn().get(j).getColumnName().equals(colName)) {
						// loop through task list until find match
						for (int v = 0; v < boards.get(i).getColumn().get(j).getTasks().size(); v++) {
							// there is a matching task 
							String nameTask = boards.get(i).getColumn().get(j).getTasks().get(v).getTaskName();
						
							//System.out.println("The curent col size is " + colSize);

							if (nameTask.equals(taskChosenName)) {
								// if there is a column to the right
								if (j + 1 < colSize) {
									column = j + 1;
									//grab the current task and save it 
									chosenTask = boards.get(i).getColumn().get(j).getOneTask(v);
									//remove the current task
									boards.get(i).getColumn().get(j).getTasks().remove(v);
									

									masterDB.editTaskColumn(nameTask, holder.getUser().getUserName(), i, j, column);
									moveTask = true;
								} else {
									hiddenText
											.setText("Unable to move task to the right. Please create a column first");
								}
							}
						}
					}
				}
			}
		}
		// move the chosen task over a column
		if (column > 0) {
			// if move task is true
			if (moveTask == true) {
				for (int i = 0; i < boards.size(); i++) {
					// get current tab that board is on 
					//System.out.println("its true to move task " + column);
					if (i == currTab) {
						// loop through current columns
						for (int j = 0; j < boards.get(i).getColumn().size(); j++) {
							if (j == column) {
								// add chosen task to the column that is to the right
								boards.get(i).getColumn().get(j).getTasks().add(chosenTask);
								try {
									// go back to board
									callBackToMain(leftCol);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}

			}
		} else {
			hiddenText.setText("Unable to move task to the right. Please create a column first");
		}
	}

}

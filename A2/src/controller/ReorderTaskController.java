package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import application.UserHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Board;
import model.Task;

//reorder the list of tasks
public class ReorderTaskController {

    @FXML
    private Button cancelBtn;

    @FXML
    private Button descrip;

    @FXML
    private Text hiddenText;

    @FXML
    private Button reorderName;

    private Stage stage;
    private Scene scene;
    private Parent root;
    

    private String colName;
    private int currTab;

    //reorder current tasks by description 
    @FXML
    void ReorderByDescript(ActionEvent event) {

	UserHolder holder = UserHolder.getUserInstance();
        
    	ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();
    	
		//loop through boards
		for (int i = 0; i < boards.size(); i++) {
			// get current tab that board is on to remove list from
			if (i == currTab) {
				//loop through current columns
				for(int j = 0; j<boards.get(i).getColumn().size(); j++) {
					//it is a matching column to the one user chose
					if(boards.get(i).getColumn().get(j).getColumnName().equals(colName)){
						//get current task list
						 ArrayList<Task> task = boards.get(i).getColumn().get(j).getTasks();
						//sort the tasks by name 
						task.sort(Comparator.comparing(Task::getTaskDescrip));
						//set the tasks after they are sorted 
						boards.get(i).getColumn().get(j).setTasks(task);
					}
				}
			}
		}
		try {
			callBackToMain(descrip);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }

    //reorder current tasks by name and show back to main
    @FXML
    void ReorderByName(ActionEvent event) {
    	UserHolder holder = UserHolder.getUserInstance();
    	ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();
    	
		//loop through boards
		for (int i = 0; i < boards.size(); i++) {
			// get current tab that board is on to remove list from
			if (i == currTab) {
				//loop through current columns
				for(int j = 0; j<boards.get(i).getColumn().size(); j++) {
					//it is a matching column to the one user chose
					if(boards.get(i).getColumn().get(j).getColumnName().equals(colName)){
						//get current task list
						 ArrayList<Task> task = boards.get(i).getColumn().get(j).getTasks();
						//sort the tasks by name 
						task.sort(Comparator.comparing(Task::getTaskName));
						//set the tasks after they are sorted 
						boards.get(i).getColumn().get(j).setTasks(task);
						
					}
				}
			}
		}
		
		try {
			callBackToMain(reorderName);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    

    //cancel go back to board
    @FXML
    void cancel(ActionEvent event) {
    	try {
			callBackToMain(cancelBtn);
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	//get curr tab
	public void getCurrTab(int selectedIndex) {
		currTab = selectedIndex;	
	}
    
	//get curr column
	public void getCurrColumn(String name) {
		colName = name;
		
	}
}

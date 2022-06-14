package controller;

import javafx.fxml.FXML;

import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Board;
import model.Column;
import model.Task;
import model.Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ModuleLayer.Controller;
import java.util.ArrayList;

import application.UserHolder;
import database.DataBaseMaster;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

/*main application screen - the project board*/
public class ProjectBoardController {


	Utilities utils = new Utilities();
	DataBaseMaster masterDB = new DataBaseMaster();

	public static ObservableList<Tab> tabList;

	public static ArrayList<Task> listOfTasks = new ArrayList<Task>();

	@FXML
	private TextField newProjectText;

	@FXML
	private MenuItem newProject;

	@FXML
	private SplitMenuButton AddTaskBtn;

	@FXML
	private Tab BoardNameOne;

	@FXML
	private Button addTextInpBoard;

	@FXML
	private Tab BoardNameTwo;

	@FXML
	private Button EditTask;

	@FXML
	private Button LogoutBtn;

	@FXML
	private MenuItem RenameBtn;

	@FXML
	private MenuItem addColumn;

	@FXML
	private SplitMenuButton addTask;

	@FXML
	private MenuItem addTaskBtn;

	@FXML
	private Pane columnContainer;

	@FXML
	private Pane columnContainer2;

	@FXML
	private Pane columnContainer3;

	@FXML
	private Button columnName;

	@FXML
	private MenuItem defaultSetter;

	@FXML
	private MenuItem deleteProjBtn;

	@FXML
	private Button deleteTask;

	@FXML
	private Button editTask;

	@FXML
	private ImageView imageDisplayBoard;

	@FXML
	private Button profileBtn;

	@FXML
	private SplitMenuButton projectDropdownBtn;

	@FXML
	private Text quoteDisplay;

	@FXML
	private Text taskDescripText;

	@FXML
	private Text taskDescript;

	@FXML
	private SplitPane taskHolder;

	@FXML
	private Text taskName;

	@FXML
	private TabPane tabPane;

	@FXML
	private Text taskNameTxt;

	@FXML
	private Text infoHidden;

	@FXML
	private MenuItem unsetBtn;

	@FXML
	private Text userNameDisplay;

	@FXML
	private SplitMenuButton workspaceDropBtn;

	private Stage stage;
	private Scene scene;
	private Parent root;

	// rename current board
	@FXML
	void RenameCurrProjectBox(ActionEvent event) throws IOException {
	
		boolean checkTabExists = utils.checkNoTabs(tabPane);
		if(checkTabExists) {
			infoHidden
			.setText("Error: You cannot rename a board if you have no boards. Please add one first");
			return;
			
		}

		int indexTab = tabPane.getSelectionModel().getSelectedIndex();
		String currTabName = tabPane.getSelectionModel().getSelectedItem().getText();

		if (currTabName.contains("default")) {
			infoHidden.setText("Error: you must unset the tab as default before you change its name");
		}

		else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/RenameProjectBoard.fxml"));
			root = loader.load();

			// get the instance of controller
			RenameBoardController controller = loader.getController();
			controller.getCurrTab(indexTab);
			controller.getCurrNameForDef(currTabName);

			// get current scene and replace the current scene with login
			stage = (Stage) profileBtn.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setTitle("Rename Board");
			stage.show();
		}
	}

	// default index - set to current focused tab
	@FXML
	void SetBoardAsDefault(ActionEvent event) {

		boolean checkTabExists = utils.checkNoTabs(tabPane);
		if(checkTabExists) {
			infoHidden.setText("Error: You cannot set a board as default if you have no boards. Please add one first");
			return;
		}

		UserHolder holder = UserHolder.getUserInstance();
		// see if there is a current board set
		boolean tabChosen = holder.getUser().getWorkspace().isDefaultBoard();
		ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();
		// if there is no current default board then they can set the current as default
		if (tabChosen != true) {

			String selectedTab = tabPane.getSelectionModel().getSelectedItem().getText();

			int indexTab = tabPane.getSelectionModel().getSelectedIndex();
			holder.getUser().getWorkspace().setTabIndex(indexTab);
			holder.getUser().getWorkspace().setSelectedTabName(selectedTab);
			// set default board property to true
			holder.getUser().getWorkspace().setDefaultBoard(true);
			if (boards != null) {
				for (int i = 0; i < boards.size(); i++) {
					String name = boards.get(i).getBoardName();
					if (name.equals(selectedTab)) {
						holder.getUser().getWorkspace().getBoards().get(i).setDefault();;
						tabPane.getSelectionModel().getSelectedItem().setText(name + " - default");
						masterDB.setAndUnsetDefault(name, holder.getUser().getUserName(), 1);
					}

				}
			}
		} else {
			infoHidden.setText("Error: you must unset the current default tab before you set a new tab as default");
		}

	}

	// reset the default to 0 - first one and unset the current tab and remove
	// default text
	@FXML
	void UnsetDefaultBoard(ActionEvent event) {

		boolean checkTabExists = utils.checkNoTabs(tabPane);
		if(checkTabExists) {
			infoHidden
					.setText("Error: You cannot unset a board as default if you have no boards. Please add one first");
			return;
		}

		UserHolder holder = UserHolder.getUserInstance();
		String selectedTab = tabPane.getSelectionModel().getSelectedItem().getText();

		holder.getUser().getWorkspace().setTabIndex(0);

		ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();
		boolean foundTab = false;
		if (boards != null) {
			for (int i = 0; i < boards.size(); i++) {

				String name = boards.get(i).getBoardName() + " - default";
				// if current tab matches saved tabs then unset it
				if (name.equals(selectedTab)) {
					// set to original name
					tabPane.getSelectionModel().getSelectedItem().setText(boards.get(i).getBoardName());
					// set the hidden text to show nothing
					foundTab = true;
					// the tab was unset so set default board to false as there are no default
					// boards anymore

					holder.getUser().getWorkspace().getBoards().get(i).unsetDefault();

					holder.getUser().getWorkspace().setSelectedTabName(null);

					masterDB.setAndUnsetDefault(boards.get(i).getBoardName(), holder.getUser().getUserName(), 0);
					infoHidden.setText(null);
				}

			}
		}

		// if tab was not found (ie they are not clicking on default tab - inform user
		if (foundTab != true) {
			// System.out.println("no tab found to unset");
			infoHidden.setText("You cannot unset a tab if it is not set as default");
		}

		holder.getUser().getWorkspace().setSelectedTabName("");
	}

	// add a column/tab to board
	@FXML
	void addColumnAction(ActionEvent event) throws IOException {

		UserHolder holder = UserHolder.getUserInstance();
		// get current tab
		int currTab = tabPane.getSelectionModel().getSelectedIndex();

		ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();
		boolean foundTab = false;
		if (boards != null) {
			for (int i = 0; i < boards.size(); i++) {
				if (currTab == i) {
					foundTab = true;
					// load
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/NewColumn.fxml"));
					root = loader.load();

					// get the instance of controller
					NewColumnController controller = loader.getController();
					controller.getCurrTab(tabPane.getSelectionModel().getSelectedIndex());

					// get current scene and replace the current scene with new column
					stage = (Stage) profileBtn.getScene().getWindow();
					stage.setScene(new Scene(root));
					stage.setTitle("New Column");
					stage.show();

				}
			}
		}
		if (foundTab != true) {
			infoHidden.setText("Error: please add a board before you create a column");
		}

	}

	// deletes the active tab/board
	@FXML
	void deleteSelectedProject(ActionEvent event) {
		boolean checkTabExists = utils.checkNoTabs(tabPane);
		if(checkTabExists) {
			infoHidden
					.setText("Error: You cannot delete a project if you have no boards");
			return;
		}
		// get the current tab
		Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
		// delete current tab
		tabPane.getTabs().remove(selectedTab);
		System.out.println("selected tab is "+selectedTab);

		UserHolder holder = UserHolder.getUserInstance();
		// delete the selected workspace
		for (int i = 0; i < holder.getUser().getWorkspace().getBoards().size(); i++) {
			int tabCurr = tabPane.getSelectionModel().getSelectedIndex() +1;
				if( tabCurr == i) {
					//also delete the columns and tasks if the board is deleted
					masterDB.deleteColKnowBoard(holder.getUser().getUserName(), i);
					masterDB.deleteTaskKnowBoard(holder.getUser().getUserName(), i);

			}
		}
		//then delete it from the current list as well using the name
		holder.getUser().getWorkspace().deleteChosen(selectedTab.getText());
		String currTabText = selectedTab.getText().replace(" - default", "");
		masterDB.deleteBoard(holder.getUser().getUserName(), currTabText);

		

	}

	// sets the tabs in the tab pane and adds in new one
	public void initialize(String name) {
		tabPane.setVisible(true);
		Tab tab = new Tab();
		tab.setText(name);
		tabPane.getTabs().add(new Tab(name));
	}
	
	// show the tabs after sign in - show the default first- each board in the list becomes a tab to show
	public void showTabsAfterSignIn() {

		tabPane.setVisible(true);
		UserHolder holder = UserHolder.getUserInstance();
		if(holder.getUser().getWorkspace() != null) {
		ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();

		if (boards != null) {
			for (int i = 0; i < boards.size(); i++) {
				String name = boards.get(i).getBoardName();
				// if default board - show default wording to signify its default
				if (holder.getUser().getWorkspace().getBoards().get(i).getDefault() == true) {
					//&& holder.getUser().getWorkspace().getBoards().get(i).getBoardName().equals(name)
					tabPane.getTabs().add(new Tab(boards.get(i).getBoardName() + " - default"));
					//show it first on sign in
					tabPane.getSelectionModel().select(i);

				}
				// not default board - just show regular
				else {
					tabPane.getTabs().add(new Tab(boards.get(i).getBoardName()));
				}

			}
			if (!boards.isEmpty()) {
				int indexTab = holder.getUser().getWorkspace().getTabIndex();
				try {
					//tabPane.getSelectionModel().select(indexTab);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	}

	// show the tabs - each board in the list becomes a tab to show
	public void showTabs() {

		tabPane.setVisible(true);
		UserHolder holder = UserHolder.getUserInstance();
		if(holder.getUser().getWorkspace() != null) {
		ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();

		if (boards != null) {
			for (int i = 0; i < boards.size(); i++) {
				String name = boards.get(i).getBoardName();
				// if default board - show default wording to signify its default
				if (holder.getUser().getWorkspace().getBoards().get(i).getDefault() == true) {
					//show as default but dont select it as that is only set on sign in 
					tabPane.getTabs().add(new Tab(boards.get(i).getBoardName() + " - default"));
					

				}
		
				// not default board - just show regular
				tabPane.getTabs().add(new Tab(boards.get(i).getBoardName()));
				

			}
			if (!boards.isEmpty()) {
				int indexTab = holder.getUser().getWorkspace().getTabIndex();
				try {
					//tabPane.getSelectionModel().select(indexTab);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	}

	// check no duplicate board name
	public boolean checkDuplicateTabName(String name) {

		UserHolder holder = UserHolder.getUserInstance();
		boolean checkDuplicate = utils.checkDuplicateTabName(holder, name);
		return checkDuplicate;
	}

	// add tab to board
	public void addTab(String tabTitle) {
		String text = newProjectText.getText();

		if (!checkDuplicateTabName(text)) {
			UserHolder holder = UserHolder.getUserInstance();
			// got to open up new project popup here so can ask user for name
			tabPane.setVisible(true);
			ArrayList<Column> column = new ArrayList<Column>();

			int numBoardsForName = holder.getUser().getWorkspace().getBoards().size();
			String boardName = text;

			for (int j = 0; j < numBoardsForName; j++) {
				boardName = tabTitle;
			}

			int numBoardsCurr = holder.getUser().getWorkspace().getBoards().size();
			holder.getUser().getWorkspace().addBoardToList(new Board(boardName, column));
			// add a new board in the database for this user
			masterDB.addNewBoard(holder.getUser().getUserName(), text, 0, numBoardsForName);

			String tabName = null;
			try {
				for (int i = 0; i < numBoardsCurr; i++) {

					tabName = holder.getUser().getWorkspace().getBoards().get(i).getBoardName();

				}
				initialize(text);
			} catch (Exception e) {
				System.out.println("Error: could not add board " + e);
			}
		} else {
			infoHidden.setText("You cannot name a board the same name as another board");
		}

	}

	// check a strings length
	public boolean checkLength(String text) {
		if (text.length() > 20) {
			infoHidden.setText("Error: board name must be less than 20 characters");
			return true;
		}
		return false;
	}

	// add tab to board and perform checks
	@FXML
	void addTabInBoard(ActionEvent event) {
		String text = newProjectText.getText();

		boolean length = checkLength(text);

		if (!length) {
			if (!checkDuplicateTabName(text)) {
				if (!text.isEmpty()) {
					tabPane.setVisible(true);
					// call add tab method
					addTab(text);
					newProjectText.clear();
					infoHidden.setText(null);

				} else {
					infoHidden.setText("Error: to create a board name, the input field cannot be empty");
				}
			} else {
				infoHidden.setText("Error: board names must be unique. Please try again");
			}
		}
	}

	// logoutuser and shows the sign in login screen
	@FXML
	void logoutUser(ActionEvent event) throws IOException {

		tabPane.getTabs().clear();
		UserHolder holder = UserHolder.getUserInstance();

		// delete current user instance and clear their boards
		holder.getUser().getWorkspace().setClearBoards();
		holder.getUser().getWorkspace().setBoards(null);
		holder.getUser().setWorkspace(null);
		holder.setUser(null);
		holder = null;

		// load
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
		root = loader.load();

		// get the instance of login controller
		LoginController logincontroller = loader.getController();

		// get current scene and replace the current scene with login
		stage = (Stage) LogoutBtn.getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.setTitle("Login");
		stage.show();
	}

	// shows user profile
	@FXML
	void profileUser(ActionEvent event) throws IOException {

		UserHolder holder = UserHolder.getUserInstance();
		// System.out.println("profile clicked");
		String user = holder.getUser().getUserName();

		// load
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Profile.fxml"));
		root = loader.load();

		// get the instance of controller
		ProfileController controller = loader.getController();
		controller.displayUser(user);
		controller.setTextImgFields();

		// get current scene and replace the current scene with login
		stage = (Stage) profileBtn.getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.setTitle("Profile");
		stage.show();
	}

	// displays name onto screen
	public void displayName(String name) {
		userNameDisplay.setText(name);
	}

	// displays img onto screen top dashboard
	public void displayImg() {
		UserHolder holder = UserHolder.getUserInstance();

		try {
			Image imgDefault = new Image("/default.jpg");

			Image img = new Image(holder.getUser().getImageString());
			if (img != null) {
				imageDisplayBoard.setImage(img);
			}
			if (holder.getUser().getImageString() == null) {
				imageDisplayBoard.setImage(imgDefault);
			}

		} catch (Exception e) {
			System.out.println(e);

		}
	}

	// set elements visibility on entering app
	public void setVisibleElements() {
		tabPane.setVisible(false);
	}

	// sets the random quote for the session
	public void setRandomQuote(String quote) {
		quoteDisplay.setText(quote);
	}

	// populates the columns on screen in their parent tabs
	// shows scroll bar if too much content to fit on screen
	public void addColumn() {

		UserHolder holder = UserHolder.getUserInstance();
		if(holder.getUser().getWorkspace() != null) {
		ArrayList<Board> boards = holder.getUser().getWorkspace().getBoards();

		// tab looping through

		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			// have to check if matches with holder in file
			for (int k = 0; k < boards.size(); k++) {
				// board matches with tab
				if (i == k) {
					// loop through that tabs columns
					HBox hbox = new HBox(); // spacing = 8

					ScrollPane sp = new ScrollPane();

					ArrayList currColumn = boards.get(k).getColumn();
					// loop through the columns
					for (int s = 0; s < currColumn.size(); s++) {
						try {

							hbox.setSpacing(5);
							Text textShow = new Text();

							// button to show options for column/list
							SplitMenuButton split = new SplitMenuButton();
							split.setText("Add Task");

							MenuItem delete = new MenuItem("Delete Column");
							MenuItem rename = new MenuItem("Rename Column");
							MenuItem addTask = new MenuItem("Add Task");

							split.getItems().addAll(delete, rename, addTask);
							split.setBackground((new Background(new BackgroundFill(Color.WHITE, null, null))));

							// get the column name
							String name = boards.get(k).getColumn().get(s).getColumnName();
							// set column name to stored val
							textShow.setText(name);
							// add task set on action to call controller
							addTask.setOnAction(e -> {
								try {
									// show new task controller
									showNewTask(name);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							});
							// delete curr column
							delete.setOnAction(e -> {
								try {
									showDeleteConfirm(name);
								} catch (IOException e1) {
									e1.printStackTrace();
								}

							});
							// rename curr column
							rename.setOnAction(e -> {
								try {
									showRenameColumn(name);
								} catch (IOException e2) {
									e2.printStackTrace();
								}
							});

							// check for tasks inside the column with another for loop
							String taskDescript;
							String taskName;
							VBox vbox2 = new VBox();
							Text textForTaskName = new Text();
							Text textForTaskDescrip = new Text();

							VBox vboxTask = new VBox();
							// loop through tasks
							for (int h = 0; h < boards.get(k).getColumn().get(s).getTasks().size(); h++) {
								// if the columns with this task match with current column
								if (boards.get(k).getColumn().get(s).getColumnName().equals(name)) {
									VBox boxForTask = new VBox(20);
									Text filler = new Text("");

									Button btn = new Button();
									Text txtTaskName = new Text();
									// VBox vboxInTaskLoop = new VBox();
									taskDescript = boards.get(k).getColumn().get(s).getTasks().get(h).getTaskDescrip();
									taskName = boards.get(k).getColumn().get(s).getTasks().get(h).getTaskName();

									textForTaskName.setText(taskName);
									textForTaskDescrip.setText(taskDescript);

									btn.setText(taskName + " " + taskDescript);
									txtTaskName.setText(taskName);

									SplitMenuButton splitTaskBtn = new SplitMenuButton();
									splitTaskBtn.setText("Task");
									splitTaskBtn.setBackground(
											(new Background(new BackgroundFill(Color.ANTIQUEWHITE, null, null))));

									MenuItem deleteTask = new MenuItem("Delete Task");
									MenuItem editTask = new MenuItem("Edit Task");
									MenuItem moveTask = new MenuItem("Move Task");
									MenuItem reorderTask = new MenuItem("Reorder Tasks");

									buttonActions(deleteTask, editTask, moveTask, reorderTask, name, taskName,
											taskDescript);

									splitTaskBtn.getItems().addAll(deleteTask, editTask, moveTask, reorderTask);
									boxForTask.getChildren().addAll(txtTaskName, splitTaskBtn);
									boxForTask
											.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
									boxForTask.setPadding(new Insets(12, 12, 12, 12));
									// add the hbox into the vertical box for all tasks
									vboxTask.getChildren().addAll(boxForTask, filler);
									vboxTask.setPadding(new Insets(1, 1, 1, 1));
								}
							}

							VBox verticalTask = new VBox();

							verticalTask.getChildren().addAll(vboxTask);

							BorderPane border = new BorderPane();
							// set borders padding
							border.setPadding(new Insets(10, 5, 10, 5));

							Pane paneForCol = new Pane();
							paneForCol.setMinSize(200, 200);
							HBox hboxInner = new HBox(100);
							// set hboxinners css properties
							hboxInner.setPadding(new Insets(15, 12, 15, 12));
							hboxInner.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, null, null)));
							// add column text and drop down to the hbox for the column
							hboxInner.getChildren().addAll(textShow, split);
							paneForCol.getChildren().addAll(vbox2);
							border.setCenter(vboxTask);

							// vbox css padding
							vboxTask.setPadding(new Insets(10, 5, 10, 5));

							border.setTop(hboxInner);

							hbox.getChildren().addAll(border);

						} catch (Exception e) {
							System.out.println(e);
						}
						// set the hbox inside the scrollbar
						sp.setContent(hbox);
						// scroll bar should show only if content needs it and is not viewable
						sp.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
						sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
					}

					// set the scroll bar in the tabPane with all of its column names and tasks
					tabPane.getTabs().get(i).setContent(sp);
				}

			}

		}
		}

	}

	// actions for task buttons
	void buttonActions(MenuItem deleteTask2, MenuItem editTask2, MenuItem moveTask, MenuItem reorderTask,
			String colName, String taskName2, String descript) {
		deleteTask2.setOnAction(e -> {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DeleteTaskConfirm.fxml"));
			try {
				root = loader.load();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			// get the instance of controller
			DeleteTaskController controller = loader.getController();
			controller.getCurrTab(tabPane.getSelectionModel().getSelectedIndex());
			controller.getCurrColumn(colName);
			controller.getCurrTaskName(taskName2);

			// get current scene and replace the current scene with login
			showStage("Delete Task");
		});
		editTask2.setOnAction(e -> {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditTask.fxml"));
			try {
				root = loader.load();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			// get the instance of controller
			EditTaskController controller = loader.getController();
			controller.getCurrTab(tabPane.getSelectionModel().getSelectedIndex());
			controller.getCurrColumn(colName);
			controller.getCurrTaskName(taskName2);
			controller.setTaskDescrip(descript);

			// get current scene and replace the current scene with login
			showStage("Edit Task");
		});
		moveTask.setOnAction(e -> {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MoveTask.fxml"));
			try {
				root = loader.load();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			// get the instance of controller
			MoveTaskController controller = loader.getController();
			controller.getCurrTab(tabPane.getSelectionModel().getSelectedIndex());
			controller.getCurrColumn(colName);
			controller.getCurrTaskName(taskName2);

			// get current scene and replace the current scene with login
			showStage("Move Task");
		});
		reorderTask.setOnAction(e -> {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ReorderTask.fxml"));
			try {
				root = loader.load();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			// get the instance of controller
			ReorderTaskController controller = loader.getController();
			controller.getCurrTab(tabPane.getSelectionModel().getSelectedIndex());
			controller.getCurrColumn(colName);

			// get current scene and replace the current scene with login
			showStage("Reorder Task");
		});

	}

	// show stage easily
	void showStage(String stageTitle) {
		// get current scene and replace the current scene with stage
		stage = (Stage) profileBtn.getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.setTitle(stageTitle);
		stage.show();
	}

	// shows new task controller - called from show new task button
	@FXML
	void showNewTask(String name) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/NewTask.fxml"));
		root = loader.load();

		// get the instance of controller
		NewTaskController controller = loader.getController();
		controller.getCurrTab(tabPane.getSelectionModel().getSelectedIndex());
		controller.getCurrColumn(name);

		// get current scene and replace the current scene with login
		showStage("New Task");
	}

	// shows new delete column controller - called from show new task button
	@FXML
	void showDeleteConfirm(String name) throws IOException {
		// System.out.println(" name: "+name);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DeleteColumnConfirm.fxml"));
		root = loader.load();

		// get the instance of controller
		DeleteColumnController controller = loader.getController();
		controller.getCurrColumn(name);

		controller.getCurrTab(tabPane.getSelectionModel().getSelectedIndex());

		// get current scene and replace the current scene with login
		showStage("Confirm Delete Column");
	}

	// shows new task controller - called from show new task button
	@FXML
	void showRenameColumn(String name) throws IOException {
		// System.out.println(" name: "+name);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/RenameColumn.fxml"));
		root = loader.load();

		// get the instance of controller
		RenameColumnController controller = loader.getController();
		controller.getCurrColumn(name);

		controller.getCurrTab(tabPane.getSelectionModel().getSelectedIndex());

		// get current scene and replace the current scene with login
		showStage("Rename Column");
	}

}

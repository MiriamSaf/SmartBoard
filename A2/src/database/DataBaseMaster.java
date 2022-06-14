package database;

import application.UserHolder;

//facade to outside classes 
//master of all the database controls so that it is not accessible from all the other classes
public class DataBaseMaster {

	BoardDataBaseManager boardManager = new BoardDataBaseManager();
	UserDataBaseManager userDataBaseManager = new UserDataBaseManager();
	ColumnDataBaseManager columnManager = new ColumnDataBaseManager();
	TaskDataBaseManager taskManager = new TaskDataBaseManager();

	// database creation
	// -------------------------------------
	public void createUserDB() {
		userDataBaseManager.tryConnect();
	}

	public void createBoardDB() {
		boardManager.tryCreateBoardDB();
	}

	public void createColDB() {
		columnManager.tryCreateColumnDB();
	}

	public void createTaskDB() {
		taskManager.tryCreateTaskDB();
	}

	// user section
	// -------------------------------------
	public void selectAllUsers(UserHolder user) {
		userDataBaseManager.selectUserDBQuery(user);
	}

	public void updateUserImage(String uname, String imgpath) {
		userDataBaseManager.updateImageUsernameTable(uname, imgpath);
	}

	public void updateSurnameAndFirstname(String uname, String firstname, String surname) {
		userDataBaseManager.updateUserSurnameFirstNameTable(surname, firstname, uname);
	}

	// board section
	// -------------------------------------
	public void selectAllBoards(UserHolder user) {
		boardManager.selectBoardDBQuery(user);
	}

	public void addNewBoard(String username, String boardname, int defBoard, int boardNum) {
		// a new board is never default to start so is set to 0
		boardManager.insertRowIntoBoardTable(username, boardname, 0, boardNum);
	}

	public void editBoard(String username, String oldname, String newName) {
		boardManager.updateBoardName(oldname, newName, username);
	}

	public void setAndUnsetDefault(String boardName, String username, int defInt) {
		boardManager.updateSetDefaultBoard(boardName, username, defInt);
	}

	public void deleteBoard(String username, String boardname) {
		boardManager.deleteRowFromBoardTable(username, boardname);
	}

	// column section
	// -------------------------------------
	public void selectAllColumns(UserHolder user) {
		columnManager.selectColDBQuery(user);
	}

	public void deleteColumn(String username, int boardNum, String colname) {
		columnManager.deleteRowFromColTable(username, boardNum, colname);
	}

	public void deleteColKnowBoard(String username, int boardNum) {
		columnManager.deleteRowFromColKnowBoard(username, boardNum);
	}

	public void addColumn(String username, int boardnum, String colname, int colNum) {
		columnManager.insertRowIntoColTable(username, boardnum, colname, colNum);
	}

	public void editColumn(String oldCol, String newCol, String username, int boardNum) {
		columnManager.updateColName(oldCol, newCol, username, boardNum);
	}

	// task section
	// -------------------------------------
	public void selectAllTTasks(UserHolder user) {
		taskManager.selectColDBTaskQuery(user);
	}

	public void addTask(String username, int boardnum, int colnum, String taskname, String taskdesc) {
		taskManager.insertRowIntoTaskTable(username, boardnum, colnum, taskname, taskdesc);
	}

	public void deleteTask(String username, int boardnum, int colnum, String taskname) {
		taskManager.deleteRowFromTaskTable(username, boardnum, colnum, taskname);
	}

	public void editTask(String oldTaskName, String taskName, String desc, String username, int boardnum, int colnum) {
		taskManager.updateTaskNameAndDesc(oldTaskName, taskName, username, boardnum, colnum, desc);
	}

	public void editTaskColumn(String taskName, String userName, int boardNum, int colNumOld, int colNumNew) {
		taskManager.updateTaskColNum(taskName, userName, boardNum, colNumOld, colNumNew);
	}

	public void deleteTaskKnowCol(String username, int boardnum, int colnum) {
		taskManager.deleteRowFromTaskKnowCol(username, boardnum, colnum);
	}

	public void deleteTaskKnowBoard(String username, int boardnum) {
		taskManager.deleteRowFromTaskKnowBoard(username, boardnum);
	}

}

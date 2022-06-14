package model;

import java.util.ArrayList;

/*each user has one workspace with many boards*/
public class Workspace implements Deleteable {

	private ArrayList<Board> boards; // arraylist of boards of the user
	private int tabIndex;
	private String selectedTabName;
	private boolean defaultBoard;
	private boolean loadedLists;

	// set up workspace to have list of boards
	public Workspace(ArrayList<Board> boards) {
		this.setBoards(boards);
	}

	public void setClearBoards() {
		this.boards.clear();
	}
	//add board to list
	public void addBoardToList(Board addingBoard) {
		if (addingBoard != null) {
			this.boards.add(addingBoard);
		}
	}

	public ArrayList<Board> getBoards() {
		return boards;
	}

	public void setBoards(ArrayList<Board> boards) {
		this.boards = boards;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

	public String getSelectedTabName() {
		return selectedTabName;
	}

	public void setSelectedTabName(String selectedTabName) {
		this.selectedTabName = selectedTabName;
	}

	public boolean isDefaultBoard() {
		return defaultBoard;
	}

	public void setDefaultBoard(boolean defaultBoard) {
		this.defaultBoard = defaultBoard;
	}

	public boolean getLoadedLists() {
		return loadedLists;

	}

	public void setLoadedLists(boolean loadedLists) {
		this.loadedLists = loadedLists;
	}

	// remove a board method - overide deletable so can get board name
	@Override
	public void deleteChosen(String removeBoard) {
		for (int i = 0; i < this.boards.size(); i++) {
			if (this.boards.get(i).getBoardName().equals(removeBoard)) {
				// if this is the one to remove then remove it
				this.boards.remove(i);
			}
		}
	}

	public void printBoards() {
		System.out.println("printing boards: ");
		for (int i = 0; i < this.boards.size(); i++) {
			System.out.println(this.boards.get(i).getBoardName());
		}
	}

}

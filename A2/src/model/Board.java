package model;

import java.util.ArrayList;

/*board that holds board name and list of columns in the board*/
public class Board implements Deleteable {
	private String boardName;
	public ArrayList<Column> column; // columns in each board - has a list of columns
	public boolean isDefault;

	public Board(String boardName, ArrayList<Column> column) {
		this.setBoardName(boardName);
		this.setColumn(column);
	}
	

	public ArrayList<Column> getColumn() {
		return column;
	}

	public void setColumn(ArrayList<Column> column) {
		this.column = column;
	}

	public String getBoardName() {
		return boardName;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}
	
	public void unsetDefault() {
		this.isDefault = false;
	}

	public void setDefault() {
		this.isDefault = true;
	}

	public boolean getDefault() {
		return this.isDefault;
	}
	
	//add a column to the board
	public void addColumnToBoard(Column addingColumn) {
		if (addingColumn != null) {
			column.add(addingColumn);
		}

	}

	// delete a column
	@Override
	public void deleteChosen(String remove) {
		for (int i = 0; i < this.column.size(); i++) {
			if (this.column.get(i).getColumnName().equals(remove)) {
				// if this is the one to remove then remove it
				this.column.remove(i);
			}
		}
	}

	//prints column for testing purposes
	public void printColumnNameInBoard() {
		for (int i = 0; i < this.column.size(); i++) {
			System.out.println(this.column.get(i).getColumnName());
		}
	}

}

package model;

import application.UserHolder;
import javafx.scene.control.TabPane;

//basic utilities used for project
public class Utilities {

	// check if there are no tabs in screen
	public boolean checkNoTabs(TabPane tabPane) {
		if (tabPane != null) {
			if (tabPane.getTabs().size() == 0) {
				return true;
			}
			return false;
		}
		return false;
	}

	// checks if duplicate tab being added
	public boolean checkDuplicateTabName(UserHolder holder, String name) {

		if (holder != null) {
			for (int i = 0; i < holder.getUser().getWorkspace().getBoards().size(); i++) {
				if (holder.getUser().getWorkspace().getBoards().get(i).getBoardName().equals(name)) {
					return true;
				}
			}
			return false;
		}
		return false;
	}
}

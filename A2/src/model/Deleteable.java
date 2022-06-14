package model;

import java.util.ArrayList;

/*interface that allows for deleting board/column/tasks*/
public interface Deleteable {
	public static ArrayList list = new ArrayList();
	
	default <T> void deleteChosen( String remove) {
		
		for( int i = 0; i < list.size(); i++) {
			if(list.get(i).equals(remove)) {
				list.remove(i);
			}
		}
		
	}
}

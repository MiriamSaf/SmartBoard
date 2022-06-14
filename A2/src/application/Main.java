package application;


import database.DataBaseMaster;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

/*starts up the application*/
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//facade master DB
			DataBaseMaster masterDB = new DataBaseMaster();
			//connect to database on start and set up user , board,  col and task databases if they do not exist yet
			masterDB.createUserDB();
			masterDB.createBoardDB();
			masterDB.createColDB();
			masterDB.createTaskDB();
			
			AnchorPane anch = (AnchorPane)FXMLLoader.load(getClass().getResource("/views/SignUp.fxml"));
			Scene scene = new Scene(anch,350,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

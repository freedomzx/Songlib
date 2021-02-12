package songlib.app;

/**
*
* @authors Brian Albert and Elijah Ongoco
*/

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import songlib.view.Controller;

public class SongLib extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		//get fxml from songlib.view.songlib.fxml
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/songlib/view/songlib.fxml"));
		Pane root = loader.load();
		
		//initialize list of songs
		Controller listController = loader.getController();
		listController.initializeListView();
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Songlib");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
}
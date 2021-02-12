package songlib.view;

/**
*
* @authors Brian Albert and Elijah Ongoco
*/

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {
	@FXML Button addbtn;
	@FXML Button editbtn;
	@FXML Button deletebtn;
	@FXML TextField nametext;
	@FXML TextField artisttext;
	@FXML TextField yeartext;
	@FXML TextField albumtext;
	
	public void test(ActionEvent e) {
		Button b = (Button)e.getSource();
		
		if(b == addbtn) {
			nametext.setText("Working");
		}
		else {
			artisttext.setText("asdjnasdahs");
		}
	}
	
	public void add(ActionEvent e) {
		Song toAdd = new Song(nametext.getText(), artisttext.getText(), albumtext.getText(), yeartext.getText());
		
		System.out.println(toAdd.getName() + " " + toAdd.getArtist() + " " + 
				toAdd.getAlbum() + " " + toAdd.getYear());
		
		
	}
	
}

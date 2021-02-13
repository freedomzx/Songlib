package songlib.view;

/**
*
* @authors Brian Albert and Elijah Ongoco
*/

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller {
	@FXML Button addbtn;
	@FXML Button editbtn;
	@FXML Button deletebtn;
	@FXML TextField nametext;
	@FXML TextField artisttext;
	@FXML TextField albumtext;
	@FXML TextField yeartext;
	@FXML ListView<String> songlist;
	
	private ObservableList<String> obsList;
	static List<Song> listOfSongs = new ArrayList<Song>();
	static List<String> songNames = new ArrayList<String>();
	Stage primaryStage;
	
	public void initializeListView(Stage mainStage) throws IOException {
		//put code here to read from file and fill in the 2 arraylists
		primaryStage = mainStage;
		BufferedReader bsvReader = new BufferedReader(new FileReader("src\\list.txt"));
		String row;
		while((row = bsvReader.readLine()) != null) {
			String[] songData = row.split("\\|", -1); //-1 lets us see if album/year is empty
			Song newSong = new Song(songData[0], songData[1], songData[2], songData[3]);
			listOfSongs.add(newSong);
			String songStr = songData[0] + " | " + songData[1];
			if(!songData[2].isEmpty()) songStr += " | " + songData[2];
			if(!songData[3].isEmpty()) songStr += " | " + songData[3];
			songNames.add(songStr);
		}
		bsvReader.close();
		Song.bubbleSort(listOfSongs);
		Collections.sort(songNames, String.CASE_INSENSITIVE_ORDER);
		
		//update the listview
		obsList = FXCollections.observableArrayList(songNames);
		songlist.setItems(obsList);
		//select first song if it exists
		if(songNames.size() > 0) {
			songlist.getSelectionModel().select(0);
		}
	}
	
	public static void showAlert(Stage mainStage, String message, String header) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(mainStage);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.showAndWait();
	}

	
	public void add(ActionEvent e) throws IOException {
		Song toAdd = new Song(nametext.getText(), artisttext.getText(), albumtext.getText(), yeartext.getText());
		
//		System.out.println(toAdd.getName() + " " + toAdd.getArtist() + " " + 
//				toAdd.getAlbum() + " " + toAdd.getYear());
		
		//only name and artist are required, check if missing either of them
		if(toAdd.getName().isEmpty() || toAdd.getArtist().isEmpty()) {
			//System.out.println("ERROR: Required fields not filled in.");
			showAlert(primaryStage, "Name/Artist not filled in", "Field error");
			return;
		}
		
		//validate characters in string
		boolean noBar = true;
		if(toAdd.getName().indexOf('|') != -1 ||
				toAdd.getArtist().indexOf('|') != -1) {
			noBar = false;
		}
		if(!toAdd.getAlbum().isEmpty()) {
			if(toAdd.getAlbum().indexOf('|') != -1) {
				noBar = false;
			}
		}
		if(!noBar) {
			showAlert(primaryStage, "\"|\" is not a valid character", "Character error");
			return;
		}
		
		if(!toAdd.getYear().isEmpty()) {
			if(!toAdd.getYear().matches("-?(0|[1-9]\\d*)")) {
				showAlert(primaryStage, "Invalid year input", "Year error");
				return;
			}
			int yr = Integer.parseInt(toAdd.getYear());
			if(yr < 0) {
				showAlert(primaryStage, "Negative year not allowed", "Year error");
				return;
			}
		}
		
		//add to obslist, have to check if that song is there already
		for(Song song : listOfSongs) {
			if(toAdd.getName().toLowerCase().equals(song.getName().toLowerCase()) && 
					toAdd.getArtist().toLowerCase().equals(song.getArtist().toLowerCase())) {
				showAlert(primaryStage, "Song by artist already exists", "Song add error");
				return;
			}
		}
		listOfSongs.add(toAdd);
		String songStr = "";
		songStr += toAdd.getName() + " | " + toAdd.getArtist();
		if(!toAdd.getAlbum().isEmpty()) songStr += " | " + toAdd.getAlbum();
		if(!toAdd.getYear().isEmpty()) songStr += " | " + toAdd.getYear();
		songNames.add(songStr);
		Song.bubbleSort(listOfSongs);
		Collections.sort(songNames, String.CASE_INSENSITIVE_ORDER);
		obsList = FXCollections.observableArrayList(songNames);
		songlist.setItems(obsList);
		
		//select newly added item
		int index = -1;
		for(int i = 0; i < songNames.size(); i++) {
			if(songNames.get(i).equals(songStr)) {
				index = i;
				break;
			}
		}
		songlist.getSelectionModel().select(index);
		
		//need to add it to the file now for persistance
		FileWriter bsvWriter = new FileWriter("src\\list.txt", true);

		List<String> songDataList = new ArrayList<String>();
		songDataList.add(toAdd.getName());
		songDataList.add(toAdd.getArtist());
		if(!toAdd.getAlbum().isEmpty()) songDataList.add(toAdd.getAlbum());
		else {
			songDataList.add("");
		}
		if(!toAdd.getYear().isEmpty()) songDataList.add(toAdd.getYear());
		else {
			songDataList.add("");
		}
		
		bsvWriter.append(String.join("|", songDataList));
		bsvWriter.append("\n");
		bsvWriter.flush();
		bsvWriter.close();
		
		System.out.println("DEBUG: Add success");
	}
	
	
	public void edit(ActionEvent e) {
		String str = songlist.getSelectionModel().getSelectedItem();
		System.out.println(str);
	}
	
	public void delete(ActionEvent e) {
		
	}
	
	
	
}

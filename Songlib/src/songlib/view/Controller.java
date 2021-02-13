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
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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
	
	public void initializeListView() {
		//put code here to read from file and fill in the 2 arraylists
		
		//update
		obsList = FXCollections.observableArrayList(songNames);
		songlist.setItems(obsList);
	}
	
	public void add(ActionEvent e) throws IOException {
		Song toAdd = new Song(nametext.getText(), artisttext.getText(), albumtext.getText(), yeartext.getText());
		
//		System.out.println(toAdd.getName() + " " + toAdd.getArtist() + " " + 
//				toAdd.getAlbum() + " " + toAdd.getYear());
		
		//only name and artist are required, check if missing either of them
		if(toAdd.getName().isEmpty() || toAdd.getArtist().isEmpty()) {
			System.out.println("ERROR: Required fields not filled in.");
			return;
		}
		
		//validate characters in string
		if(toAdd.getName().indexOf('|') != -1 ||
				toAdd.getArtist().indexOf('|') != -1) {
			System.out.println("ERROR: \"|\" not a valid character.");
			return;
		}
		if(!toAdd.getAlbum().isEmpty()) {
			if(toAdd.getAlbum().indexOf('|') != -1) {
				System.out.println("ERROR: \"|\" not a valid character.");
				return;
			}
		}
		if(!toAdd.getYear().isEmpty()) {
			if(!toAdd.getYear().matches("-?(0|[1-9]\\d*)")) {
				System.out.println("ERROR: " + toAdd.getYear() + " is not a valid year.");
				return;
			}
			int yr = Integer.parseInt(toAdd.getYear());
			if(yr < 0) {
				System.out.println("ERROR: Invalid year number.");
				return;
			}
		}
		
		//add to obslist, have to check if that song is there already
		for(Song song : listOfSongs) {
			if(toAdd.getName().equals(song.getName()) && toAdd.getArtist().equals(song.getArtist())) {
				System.out.println("ERROR: Song already exists.");
				return;
			}
		}
		listOfSongs.add(toAdd);
		//TODO case insensitivity
		String songStr = "";
		songStr += toAdd.getName() + " | " + toAdd.getArtist();
		if(!toAdd.getAlbum().isEmpty()) songStr += " | " + toAdd.getAlbum();
		if(!toAdd.getYear().isEmpty()) songStr += " | " + toAdd.getYear();
		songNames.add(songStr);
		Song.bubbleSort(listOfSongs);
		Collections.sort(songNames, String.CASE_INSENSITIVE_ORDER);
		obsList = FXCollections.observableArrayList(songNames);
		songlist.setItems(obsList);
		
		//need to add it to the file now for persistance
		FileWriter csvWriter = new FileWriter("src\\list.txt", true);
//		csvWriter.append("Name");
//		csvWriter.append(",");
//		csvWriter.append("Artist");
//		if(!toAdd.getAlbum().isEmpty()) {
//			csvWriter.append(",");
//			csvWriter.append("Album");
//		}
//		if(!toAdd.getYear().isEmpty()) {
//			csvWriter.append(",");
//			csvWriter.append("Year");
//		}
		List<String> songDataList = new ArrayList<String>();
		songDataList.add(toAdd.getName());
		songDataList.add(toAdd.getArtist());
		if(!toAdd.getAlbum().isEmpty()) songDataList.add(toAdd.getAlbum());
		if(!toAdd.getYear().isEmpty()) songDataList.add(toAdd.getYear());
		
		csvWriter.append(String.join("|", songDataList));
		csvWriter.append("\n");
		csvWriter.flush();
		csvWriter.close();
		
		System.out.println("DEBUG: Add success");
	}
	
	
	public void edit(ActionEvent e) {
		
	}
	
	public void delete(ActionEvent e) {
		
	}
	
	
	
}

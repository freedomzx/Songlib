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
import javafx.scene.control.Label;
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
	@FXML Label nameLabel;
	@FXML Label artistLabel;
	@FXML Label albumLabel;
	@FXML Label yearLabel;
	
	private ObservableList<String> obsList;
	static List<Song> listOfSongs = new ArrayList<Song>();
	static List<String> songNames = new ArrayList<String>();
	Stage primaryStage;
	
	public void initializeListView(Stage mainStage) throws IOException {
		// Read in file and fill arrays
		primaryStage = mainStage;
		BufferedReader bsvReader = new BufferedReader(new FileReader("src\\list.txt"));
		String row;
		while((row = bsvReader.readLine()) != null) {
			String[] songData = row.split("\\|", -1); //-1 lets us see if album/year is empty
			Song newSong = new Song(songData[0], songData[1], songData[2], songData[3]);
			listOfSongs.add(newSong);
			String songStr = songData[0] + " | " + songData[1];
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
			nameLabel.setText(listOfSongs.get(0).getName());
			artistLabel.setText(listOfSongs.get(0).getArtist());
			albumLabel.setText(listOfSongs.get(0).getAlbum());
			yearLabel.setText(listOfSongs.get(0).getYear());
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
		String addName = nametext.getText().trim();
		String addArtist = artisttext.getText().trim();
		String addAlbum = albumtext.getText().trim();
		String addYear = yeartext.getText().trim();
		
		Song toAdd = new Song(addName, addArtist, addAlbum, addYear);
		
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
				showAlert(primaryStage, "Duplicate entry", "Song add error");
				return;
			}
		}
		
		// Add new song to list of songs
		listOfSongs.add(toAdd);
		Song.bubbleSort(listOfSongs);
		
		// Write list to csv file
		String toWrite = "";
		String listString;
		songNames.clear();
		for(Song song : listOfSongs) {
			toWrite += song.getName() + "|" + song.getArtist() + "|" + song.getAlbum()
					+ "|" + song.getYear() + "\n";
			
			listString = "";
			listString += song.getName() + " | " + song.getArtist();
			songNames.add(listString);
		}
		new FileWriter("src\\list.txt", false).close();
		FileWriter bsvWriter = new FileWriter("src\\list.txt", true);
		bsvWriter.append(toWrite);
		bsvWriter.flush();
		bsvWriter.close();
		
		// Get index of selected song
		int index = 0;
		int count = 0;
		for(Song song : listOfSongs) {
			if (song.getName().equalsIgnoreCase(addName) && song.getArtist().equalsIgnoreCase(addArtist)
					&& song.getAlbum().equalsIgnoreCase(addAlbum) && song.getYear().equalsIgnoreCase(addYear)) {
				index = count;
			}
			count++;
		}
	
		//update the listview
		obsList = FXCollections.observableArrayList(songNames);
		songlist.setItems(obsList);
		songlist.getSelectionModel().select(index);
		
		// Update details
		nameLabel.setText(listOfSongs.get(index).getName());
		artistLabel.setText(listOfSongs.get(index).getArtist());
		albumLabel.setText(listOfSongs.get(index).getAlbum());
		yearLabel.setText(listOfSongs.get(index).getYear());
		
		// Clear Text Field
		nametext.clear();
		artisttext.clear();
		albumtext.clear();
		yeartext.clear();
		
		//System.out.println("DEBUG: Add success");
	}
	
	
	public void edit(ActionEvent e) throws IOException {	
		
		// Get index of selected song
		int index = songlist.getSelectionModel().getSelectedIndex();
		if(index == -1) {
			showAlert(primaryStage, "No song selected", "Edit error");
			return;
		}
		
		// Get variables from form and remove leading and trailing spaces
		String editName = nametext.getText().trim();
		String editArtist = artisttext.getText().trim();
		String editAlbum = albumtext.getText().trim();
		String editYear = yeartext.getText().trim();
		
		// Get variables from the selected song
		String name = listOfSongs.get(index).getName();
		String artist = listOfSongs.get(index).getArtist();
		String album = listOfSongs.get(index).getAlbum();
		String year = listOfSongs.get(index).getYear();
		
		// Check to see if any edits were made
		if(editName.isEmpty() && editArtist.isEmpty() && editAlbum.isEmpty() && editYear.isEmpty()) {
			//System.out.println("ERROR: Required fields not filled in.");
			showAlert(primaryStage, "No edits made", "Field error");
			return;
		}
		
		
		// Validate characters in string
		boolean noBar = true;
		if(nametext.getText().indexOf('|') != -1 ||
				artisttext.getText().indexOf('|') != -1) {
			noBar = false;
		}
		if(!albumtext.getText().isEmpty()) {
			if(albumtext.getText().indexOf('|') != -1) {
				noBar = false;
			}
		}
		if(!noBar) {
			showAlert(primaryStage, "\"|\" is not a valid character", "Character error");
			return;
		}
		if(!yeartext.getText().isEmpty()) {
			if(!yeartext.getText().matches("-?(0|[1-9]\\d*)")) {
				showAlert(primaryStage, "Invalid year input", "Year error");
				return;
			}
			int yr = Integer.parseInt(yeartext.getText());
			if(yr < 0) {
				showAlert(primaryStage, "Negative year not allowed", "Year error");
				return;
			}
		}
		
		// Check to see if song with edit already exists
		if (!editName.isEmpty() && editArtist.isEmpty()) { // Case 1: editing name but not artist
			for(Song song : listOfSongs) {
				if(editName.toLowerCase().equals(song.getName().toLowerCase()) && 
						name.equals(song.getArtist().toLowerCase())) {
					showAlert(primaryStage, "Edit makes this song a duplicate", "Song edit error");
					return;
				}
			}
		} else if (!editName.isEmpty() && !editArtist.isEmpty()) { // Case 2: editing name and artist
			for(Song song : listOfSongs) {
				if(editName.toLowerCase().equals(song.getName().toLowerCase()) && 
						editArtist.toLowerCase().equals(song.getArtist().toLowerCase()) && song != listOfSongs.get(index) ) {
					showAlert(primaryStage, "Edit makes this song a duplicate", "Song edit error");
					return;
				}
			}
		} else if (editName.isEmpty() && !editArtist.isEmpty()) { // Case 3: editing artist but not name
			for(Song song : listOfSongs) {
				if(name.toLowerCase().equals(song.getName().toLowerCase()) && 
						editArtist.toLowerCase().equals(song.getArtist().toLowerCase()) && song != listOfSongs.get(index) ) {
					showAlert(primaryStage, "Edit makes this song a duplicate", "Song edit error");
					return;
				}
			}
		}
		
		// Make changes to selected song
		if (!editName.isEmpty()) {
			listOfSongs.get(index).setName(editName);
		}
		if (!editArtist.isEmpty()) {
			listOfSongs.get(index).setArtist(editArtist);
		}
		if (!editAlbum.isEmpty()) {
			listOfSongs.get(index).setAlbum(editAlbum);
		} 
		if (!editYear.isEmpty()) {
			listOfSongs.get(index).setYear(editYear);
		}
		Song.bubbleSort(listOfSongs);
		
		String toWrite = "";
		String listString;
		songNames.clear();
		for(Song song : listOfSongs) {
			toWrite += song.getName() + "|" + song.getArtist() + "|" + song.getAlbum()
			+ "|" + song.getYear() + "\n";
			
			listString = "";
			listString += song.getName() + " | " + song.getArtist();
			songNames.add(listString);
		}
		Collections.sort(songNames, String.CASE_INSENSITIVE_ORDER);
		new FileWriter("src\\list.txt", false).close();
		FileWriter bsvWriter = new FileWriter("src\\list.txt", true);
		bsvWriter.append(toWrite);
		bsvWriter.flush();
		bsvWriter.close();
		
	
		//update the listview
		obsList = FXCollections.observableArrayList(songNames);
		songlist.setItems(obsList);
		
		// Get the index of new song
		if (!editName.isEmpty() && editArtist.isEmpty()) { // Case 1: editing name but not artist
			for(int i = 0; i < listOfSongs.size(); i++) {
				if(editName.equalsIgnoreCase(listOfSongs.get(i).getName()) && 
						artist.equalsIgnoreCase(listOfSongs.get(i).getArtist())) {
					index = i;
					break;
				}
			}
		} else if (!editName.isEmpty() && !editArtist.isEmpty()) { // Case 2: editing name and artist
			for(int i = 0; i < listOfSongs.size(); i++) {
				if(editName.equalsIgnoreCase(listOfSongs.get(i).getName()) && 
						editArtist.equalsIgnoreCase(listOfSongs.get(i).getArtist())) {
					index = i;
					break;
				}
			}
		} else if (editName.isEmpty() && !editArtist.isEmpty()) { // Case 3: editing artist but not name
			for(int i = 0; i < listOfSongs.size(); i++) {
				if(name.equalsIgnoreCase(listOfSongs.get(i).getName()) && 
						editArtist.equalsIgnoreCase(listOfSongs.get(i).getArtist())) {
					System.out.println("Index = " + i);
					index = i;
					break;
				}
			}
		}
		songlist.getSelectionModel().select(index);
		
		// Update details
		nameLabel.setText(listOfSongs.get(index).getName());
		artistLabel.setText(listOfSongs.get(index).getArtist());
		albumLabel.setText(listOfSongs.get(index).getAlbum());
		yearLabel.setText(listOfSongs.get(index).getYear());
		
		// Clear Text Field
		nametext.clear();
		artisttext.clear();
		albumtext.clear();
		yeartext.clear();
	}
	
	public void delete(ActionEvent e) throws IOException {
		// Get index of selected song
		int index = songlist.getSelectionModel().getSelectedIndex();
		if(index == -1) {
			showAlert(primaryStage, "No song selected", "Delete error");
			return;
		}
		
		// Remove selected song
		listOfSongs.remove(index);
		
		// Write list to csv file
		String toWrite = "";
		String listString;
		songNames.clear();
		for(Song song : listOfSongs) {
			toWrite += song.getName() + "|" + song.getArtist() + "|" + song.getAlbum()
					+ "|" + song.getYear() + "\n";
			
			listString = "";
			listString += song.getName() + " | " + song.getArtist();
			songNames.add(listString);
		}
		new FileWriter("src\\list.txt", false).close();
		FileWriter bsvWriter = new FileWriter("src\\list.txt", true);
		bsvWriter.append(toWrite);
		bsvWriter.flush();
		bsvWriter.close();
		
		if (listOfSongs.size() <= index) {
			index--;
		}
	
		// Update the listview
		obsList = FXCollections.observableArrayList(songNames);
		songlist.setItems(obsList);
		songlist.getSelectionModel().select(index);
				
		// If list is now empty show no details
		if(obsList.size() == 0) {
			nameLabel.setText("");
			artistLabel.setText("");
			albumLabel.setText("");
			yearLabel.setText("");
		} else { 
			// Update details
			nameLabel.setText(listOfSongs.get(index).getName());
			artistLabel.setText(listOfSongs.get(index).getArtist());
			albumLabel.setText(listOfSongs.get(index).getAlbum());
			yearLabel.setText(listOfSongs.get(index).getYear());
		}
	}
	
	// Update the details of the song when the list is changed
	public void updateDetails(){
		// Get index of selected song
		int index = songlist.getSelectionModel().getSelectedIndex();
		//if there's nothing in the list to select return to not have exception
		if(index < 0) return;
		
		// Update details
		nameLabel.setText(listOfSongs.get(index).getName());
		artistLabel.setText(listOfSongs.get(index).getArtist());
		albumLabel.setText(listOfSongs.get(index).getAlbum());
		yearLabel.setText(listOfSongs.get(index).getYear());
	}
	
	//method for debugging, just prints songs
	private static void printSongs() {
		for(Song song : listOfSongs) {
			System.out.println(song.toString());
		}
	}
	
}

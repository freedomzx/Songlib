package songlib.view;

import java.util.Collections;
import java.util.List;

/**
*
* @authors Brian Albert and Elijah Ongoco
*/
public class Song {
	private String name;
	private String artist;
	private String album;
	private String year;
	
	// Constructor
	public Song(String initialName, String initialArtist, String initialAlbum, String initialYear) {
		this.name = initialName;
		this.artist = initialArtist;
		this.album = initialAlbum;
		this.year = initialYear;
	}
	
	// SetName method accepts an argument which is stored in the name field
	public void setName(String newName){
		this.name = newName;
	}
	
	// SetName method accepts an argument which is stored in the name field
	public void setArtist(String newArtist){
		this.artist = newArtist;
	}
	
	// setAlbum method accepts an argument which is stored in the album field
	public void setAlbum(String newAlbum){
		this.album = newAlbum;
	}
	
	// setYear method accepts an argument which is stored in the year field
	public void setYear(String newYear){
		this.year = newYear;
	}
	
	// Returns the value stored in the name field
	public String getName(){
		return this.name;
	}
	
	// Returns the value stored in the name field
	public String getArtist(){
		return this.artist;
	}
	
	// Returns the value stored in the name field
	public String getAlbum(){
		return this.album;
	}
	
	// Returns the value stored in the name field
	public String getYear(){
		return this.year;
	}
	
	// Takes in a list of songs and sorts the list alphabetically by the song's name
	public static void bubbleSort(List<Song> toSort) 
    { 
		int n = toSort.size(); 
    	for (int i = 0; i < n-1; i++) {
        		for (int j = 0; j < n-i-1; j++) {
        			if ((toSort.get(j).getName().toLowerCase().compareTo(toSort.get(j+1).getName().toLowerCase())) > 0) 
					{ 
						// swap j and j+1
						Collections.swap(toSort, j,j+1);
					} else if ((toSort.get(j).getName().toLowerCase().compareTo(toSort.get(j+1).getName().toLowerCase())) == 0) {
						if ((toSort.get(j).getArtist().toLowerCase().compareTo(toSort.get(j+1).getArtist().toLowerCase())) > 0) 
						{ 
							// swap j and j+1
							Collections.swap(toSort, j,j+1);
						}
					}
        		}
    	}
    } 
	
	public String toString() {
		return this.getName() + " | " + this.getArtist() + " | " + this.getAlbum() + " | " + this.getYear();
	}
}

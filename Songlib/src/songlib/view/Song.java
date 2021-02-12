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
		name = initialName;
		artist = initialArtist;
		album = initialAlbum;
		year = initialYear;
	}
	
	// SetName method accepts an argument which is stored in the name field
	public void setName(String newName){
        	name = newName;
    	}
	
	// SetName method accepts an argument which is stored in the name field
	public void setArtist(String newArtist){
        	artist = newArtist;
    	}
	
	// setAlbum method accepts an argument which is stored in the album field
	public void setAlbum(String newAlbum){
        	artist = newAlbum;
    	}
	
	// setYear method accepts an argument which is stored in the year field
	public void setYear(String newYear){
        	year = newYear;
    	}
	
	// Returns the value stored in the name field
	public String getName(){
		return name;
	}
	
	// Returns the value stored in the name field
	public String getArtist(){
		return artist;
	}
	
	// Returns the value stored in the name field
	public String getAlbum(){
		return album;
	}
	
	// Returns the value stored in the name field
	public String getYear(){
		return year;
	}
	
	// Takes in a list of songs and sorts the list alphabetically by the song's name
	public void bubbleSort(List<Song> toSort) 
    	{ 
		int n = toSort.size(); 
        	for (int i = 0; i < n-1; i++) 
            		for (int j = 0; j < n-i-1; j++) 
				if ((toSort.get(j).getName().toLowerCase().compareTo(toSort.get(j+1).getName().toLowerCase())) > 0) 
				{ 
					// swap j and j+1
					Collections.swap(toSort, j,j+1);
				} 
    } 
}
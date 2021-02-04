package songlib;

/**
*
* @authors Brian Albert and Elijah Ongoco
*/
public class Song {
	private String name;
	private String artist;
	
	// Constructor
	public Song(String initialName, String initialArtist) {
		name = initialName;
		artist = initialArtist;
	}
	
	// SetName method accepts an argument which is stored in the name field
	public void setName(String newName){
        name = newName;
    }
	
	// SetName method accepts an argument which is stored in the name field
	public void setArtist(String newArtist){
        artist = newArtist;
    }
	
	// Returns the value stored in the name field
	public String getName(){
		return name;
	}
	
	// Returns the value stored in the name field
	public String getArtist(){
		return artist;
	}
}
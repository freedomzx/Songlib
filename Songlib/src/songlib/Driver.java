package songlib;
import java.util.*;

public class Driver {
	
	public static void main(String[] args) {
		// Initialize Variables
		String name = "God's Plan";
		String artist = "Drake";
		String album = "";
		String year = "";
		
		Song newSong = new Song(name, artist, album, year);
		List<Song> songList = new ArrayList<Song>(); 
		songList.add(newSong);
		
		boolean quit = false;
		Scanner myObj = new Scanner(System.in);  // Create a Scanner object
		while(quit != true) {
			System.out.println("New name: ");
			name = myObj.nextLine();  // Read user input
			
			System.out.println("New artist: ");
			artist = myObj.nextLine();  // Read user input
			
			System.out.println("New album: ");
			album = myObj.nextLine();  // Read user input
			
			System.out.println("New year: ");
			year = myObj.nextLine();  // Read user input
			
			System.out.println("Enter q to quit or anything else to continue: ");
			if (myObj.nextLine().compareTo("q") == 0) {
				quit = true;
			}
			
			Song tempSong = new Song(name, artist, album, year);
						
			songList.add(tempSong);
		}
			
		newSong.bubbleSort(songList);
		
		
		for (int i = 0; i < songList.size(); i++) {
			System.out.println("Song " + (i+1) + ":");
			System.out.println("name: " + songList.get(i).getName());
			System.out.println("artist: " + songList.get(i).getArtist());
			System.out.println("album: " + songList.get(i).getAlbum());
			System.out.println("year: " + songList.get(i).getYear());
		}
		
	}
}

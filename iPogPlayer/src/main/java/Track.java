import java.io.Serializable;

public class Track implements Serializable {
    private static final long serialVersionUID = 1L;
    String title;
    String artist;
    String spotifyUrl;
    String filePath;


    public Track(String title, String artist, String spotifyUrl) {
        this.title = title;
        this.artist = artist;
        this.spotifyUrl = spotifyUrl;
        this.filePath = "";
    }
}

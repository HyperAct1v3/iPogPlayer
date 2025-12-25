import java.io.Serializable;

public class Playlist implements Serializable {
    public String name;
    public Track[] tracks;
    public int count;

    public Playlist(String name) {
        this.name = name;
        this.tracks = new Track[5];
        this.count = 0;
    }
    public void addTrack(Track track) {
        if (count >= tracks.length) {
            expandPlaylist();
        }
        tracks[count] = track;
        count++;
    }

    private void expandPlaylist() {
        Track[] bigger = new Track[tracks.length * 2];
        for (int i = 0; i < tracks.length; i++) {
            bigger[i] = tracks[i];
        }
        tracks = bigger;
    }
    public void removeTrack(int index) {
        if (index<0|| index >= count)
            return;
        for (int i = index; i < count; i++) {
            tracks[i] = tracks[i+1];
        }
        tracks[count-1] = null;
        count--;
    }
}

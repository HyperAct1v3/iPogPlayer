import java.io.Serializable;

public class Library implements Serializable {
   public  Track[] allSongs =  new Track[10];
   public int songCount;

    private Playlist[] playlists = new Playlist[5];
    private int playlistCount = 0;

    public void addSong(Track track) {
        if (songCount >= allSongs.length) {
            expandSongArray();
        }
        allSongs[songCount] = track;
        songCount++;
    }

    public void removeSong(int index) {
        if (index >= 0 && index < songCount) {
            for (int i = index; i < songCount; i++) {
                allSongs[i] = allSongs[i + 1];
            }
            allSongs[songCount - 1] = null;
            songCount--;
        }

    }

    public void makePlaylist(String name) {
        if (playlistCount >= playlists.length) {
            expandPlaylistArray();
        }
        playlists[playlistCount] = new Playlist(name);
        playlistCount++;
    }
    public Track[] getSongs() {
        return allSongs;
    }
    public int getPlaylistCount() {
        return playlistCount;
    }
    public Playlist[] getPlaylists() {
        return playlists;
    }
    public int getSongCount() {
        return songCount;
    }

    private void expandSongArray() {
        Track[] bigger = new Track[allSongs.length * 2];
        for (int i = 0; i < songCount; i++) {
            bigger[i] = allSongs[i];
        }
        allSongs = bigger;
    }
    private void expandPlaylistArray() {
        Playlist[] bigger =  new Playlist[playlists.length * 2];
        for (int i = 0; i < playlistCount; i++) {
            bigger[i] = playlists[i];
        }
        playlists = bigger;
    }
    public void removePlaylist(int index) {
        if (index >= 0 || index >= playlistCount)
            return;
        for (int i = index; i < playlistCount - 1; i++) {
            playlists[i] = playlists[i + 1];
        }
        playlists[playlistCount - 1] = null;
        playlistCount--;
    }
}

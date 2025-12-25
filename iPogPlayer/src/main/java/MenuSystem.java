import java.util.Scanner;

public class MenuSystem {
    private Library myLibrary;
    private Scanner scanner = new Scanner(System.in);
    private boolean running = true;
    public static boolean autoplay = false;
    private MediaEngine engine = new MediaEngine();
    private SpotmateDownloader downloader = new SpotmateDownloader();
    private Search searchEngine = new Search();

    public void run() {
        myLibrary = DataManager.loadData();

        if (myLibrary == null) {
            myLibrary = new Library();

        }
        while (running) {
            printMainMenu();
            if (scanner.hasNextLine()) {
                String choice = scanner.nextLine();
                handleInput(choice);
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n==========================================");
        System.out.println("                   iPog Menu");
        System.out.println("==========================================");
        System.out.println(" [1] View/Play Library");
        System.out.println(" [2] Playlists");
        System.out.println(" [3] Search & Download New Song");
        System.out.println(" [4] Delete Song");
        System.out.println(" [X] Exit");
        System.out.print(" >> ");
    }

    private void handleInput(String choice) {
        switch (choice.trim().toLowerCase()) {
            case "1":
                viewAndPlay(myLibrary.getSongs(), myLibrary.getSongCount());
                break;
            case "2":
                handlePlaylists();
                break;
            case "3":
                searchAndDownload();
                break;
            case "4":
                deleteSong();
                break;
            case "x":
                running = false;
                DataManager.saveLibrary(myLibrary);
                System.out.println("Library Saved");
                System.exit(69420);
                break;
            default:
                System.out.println("Invalid choice, please try again.");
        }
    }

    private void searchAndDownload() {
        try {
            System.out.println("--- Starting Search ---");

            Track selectedTrack = searchEngine.search(scanner);
            if (selectedTrack == null) {
                System.out.println("Search Stopped");
                return;
            }
            System.out.println("Selected: " + selectedTrack.title);
            System.out.println("URL: " + selectedTrack.spotifyUrl);
            System.out.println("Converting to MP3...");

            String mp3Url = downloader.getMp3Link(selectedTrack.spotifyUrl);

            System.out.println("Link Generated: " + mp3Url);

            if (mp3Url == null || mp3Url.isEmpty() || mp3Url.isBlank()) {
                System.out.println("Error: Could not find download link." +
                        " Input could be ( and probably is ) blank. ");
                return;
            }
            String safeName = sanitize(selectedTrack.artist + " - " + selectedTrack.title);
            System.out.println("Downloading...");
            java.io.File savedFile = downloader.downloadMp3(mp3Url, "music", safeName);

            selectedTrack.filePath = savedFile.getAbsolutePath();
            myLibrary.addSong(selectedTrack);
            System.out.println("Download Complete! " + selectedTrack.title + " has been added to your library.");

            DataManager.saveLibrary(myLibrary);
            System.out.println("Saved...");

        } catch (Exception e) {
            System.out.println("Something bad happened: " + e.getMessage() + " :( ");
            e.printStackTrace();
        }
    }

    private void viewAndPlay(Track[] tracks, int count) {
        if (count == 0) {
            System.out.println("Nothing to play. Add some songs first!");
            return;
        }
        System.out.print("--- Songs ---\n");
        for (int i = 0; i < count; i++) {
            System.out.printf("[%d] %s - %s\n", i + 1, tracks[i].title, tracks[i].artist);
        }
        System.out.println("---------------------------");
        System.out.println("Autoplay is: " + (autoplay ? "ON" : "OFF") + " (Type 't' while playing to toggle)");
        System.out.print("Play # (0 to go back): ");
        try {
            String input = scanner.nextLine();
            int index = Integer.parseInt(input) - 1;
            if (index == -1)
                return;
            if (index >= 0 && index < count) {

                Track track = tracks[index];

                java.io.File check = new java.io.File(track.filePath);
                if (check.exists()) {
                    boolean finishedNaturally = engine.playTrackWithUI(track, track.filePath, scanner);

                    if (finishedNaturally && autoplay) {
                        System.out.println("-AutoPlaying next song-");
                        index++;
                    }
                } else {
                    System.out.println("Error: File does not exist for:  " + track.title);
                }
            }
        } catch (Exception e) {
        }
    }

    private void handlePlaylists() {
        boolean inMenu = true;
        while (inMenu) {
            Playlist[] pl = myLibrary.getPlaylists();
            int count = myLibrary.getPlaylistCount();

            System.out.println("\n--- Playlists ---");
            System.out.println("(N) Create New Playlist");

            for (int i = 0; i < count; i++) {
                System.out.println("[" + (i + 1) + "] " + pl[i].name + " (" + pl[i].count + " songs)");
            }
            System.out.println("(B) Back to Main Menu");
            System.out.print("Select >> ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("n")) {
                System.out.print("Enter name: ");
                myLibrary.makePlaylist(scanner.nextLine());
            } else if (input.equalsIgnoreCase("b")) {
                inMenu = false;
            } else {
                try {
                    int index = Integer.parseInt(input) - 1;
                    if (index >= 0 && index < count) {
                        managePlaylist(pl[index], index); // Pass index so we can delete it if needed
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input.");
                }
            }
        }
    }

    private void managePlaylist(Playlist p, int playlistIndex) {
        boolean inPlaylist = true;
        while (inPlaylist) {
            System.out.println("\n=== " + p.name + " ===");
            if (p.count == 0) {
                System.out.println("(Empty)");
            } else {
                for (int i = 0; i < p.count; i++) { // GEMINI text v
                    System.out.printf("[%d] %s - %s\n", i + 1, p.tracks[i].title, p.tracks[i].artist);
                }
            } //GEMINI v
            System.out.println("---------------------------");
            System.out.println("[P] Play Specific Song");
            System.out.println("[A] Add Song");
            System.out.println("[D] Delete Song");
            System.out.println("---------------------------");
            System.out.println("[ALL] Play All (Sequential)");
            System.out.println("[SHUFFLE] Shuffle Play");
            System.out.println("---------------------------");
            System.out.println("[REN] Rename Playlist");
            System.out.println("[DEL] Delete Entire Playlist");
            System.out.println("[B] Back");
            System.out.print(">> ");
            //GEMINI ^
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "P":
                    System.out.print("Song #: ");
                    try {
                        int idx = Integer.parseInt(scanner.nextLine()) - 1;
                        if (idx >= 0 && idx < p.count) {
                            engine.playTrackWithUI(p.tracks[idx], p.tracks[idx].filePath, scanner);
                        }
                    } catch (Exception e) {
                    }
                    break;

                case "A":
                    addSongToPlaylist(p);
                    break;

                case "D":
                    System.out.print("Delete Song #: ");
                    try {
                        int idx = Integer.parseInt(scanner.nextLine()) - 1;
                        p.removeTrack(idx);
                        System.out.println("Song removed.");
                    } catch (Exception e) {
                    }
                    break;

                case "ALL":
                    System.out.println("Playing all songs...");
                    for (int i = 0; i < p.count; i++) {
                        System.out.println("Now Playing: " + p.tracks[i].title);
                        engine.playTrackWithUI(p.tracks[i], p.tracks[i].filePath, scanner);
                    }
                    break;

                case "SHUFFLE":
                    playShuffle(p);
                    break;

                case "REN":
                    System.out.print("New Name: ");
                    p.name = scanner.nextLine();
                    break;

                case "DEL":
                    System.out.print("Are you sure? (y/n): ");
                    if (scanner.nextLine().equalsIgnoreCase("y")) {
                        myLibrary.removePlaylist(playlistIndex);
                        inPlaylist = false;
                        System.out.println("Playlist Deleted.");
                    }
                    break;

                case "B":
                    inPlaylist = false;
                    break;
            }
        }
    }

    private void playShuffle(Playlist p) {
        if (p.count <= 0) return;

        Track[] temp = new Track[p.count];
        for (int i = 0; i < p.count; i++) {
            temp[i] = p.tracks[i];
        }

        for (int i = p.count - 1; i > 0; i--) {
            int index = (int) (Math.random() * (i + 1));

            Track swap = temp[index];
            temp[index] = temp[i];
            temp[i] = swap;
        }

        System.out.println("--- Shuffled ---");
        for (Track t : temp) {
            System.out.println("Playing: " + t.title);
            engine.playTrackWithUI(t, t.filePath,scanner);
        }
    }

    private void addSongToPlaylist(Playlist p) {
        Track[] libSongs = myLibrary.getSongs();
        int libCount = myLibrary.getSongCount();

        if (libCount == 0) {
            System.out.println("Nothing to play. Add some songs first!");
            return;
        }
        for (int i = 0; i < libCount; i++) {
            System.out.printf("[%d] %s\n", i + 1, libSongs[i].title);
        }
        System.out.print("Enter Song #: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index >= 0 && index < libCount) {
                p.addTrack(libSongs[index]);
                System.out.println("Added!");
            }
        } catch (Exception e) {
            System.out.println("Invalid selection.");
        }
    }

    private void deleteSong() {
        printAllSongs();
        myLibrary.getSongCount();
        System.out.print("Enter # to delete: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            myLibrary.removeSong(index);
            System.out.println("Song Deleted!");
        } catch (Exception e) {
        }
    }

    private void printAllSongs() {
        Track[] tracks = myLibrary.getSongs();
        int count = myLibrary.getSongCount();
        System.out.println("--- All Songs ---");
        for (int i = 0; i < count; i++) {
            System.out.printf("[%d] %s\n", i + 1, tracks[i].title + " - " + tracks[i].artist);
        }
        System.out.println("----------------------------------");

    }

    private String sanitize(String input) {
        return input.replaceAll("[^a-zA-Z0-9 . -]", "_");
    }
}

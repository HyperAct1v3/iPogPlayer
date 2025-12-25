
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class MediaEngine {
    private boolean isPaused = false;
    private MediaPlayer player;


    public MediaEngine() {
        new JFXPanel();
    }

    public void play(String path) {
        try {
            if (player != null) {
                player.stop();
                player.dispose();
            }
            Media media = new Media(new File(path).toURI().toString());
            player = new MediaPlayer(media);


            player.setOnReady(() -> {
                player.play();
                isPaused = false;
            });

        } catch (Exception e) {
            System.out.println("Error playing song: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void pause() {
        if (player != null) {
            player.pause();
            isPaused = true;
        }
    }

    public void stop() {
        if (player != null) {
            player.stop();
        }
    }

    public void resume() {
        if (player != null && isPaused) {
            player.play();
            isPaused = false;
        }
    }

    public boolean playTrackWithUI(Track track, String filePath, Scanner scanner) {
        AtomicBoolean isRunning = new AtomicBoolean(true);
        AtomicBoolean songFinished = new AtomicBoolean(false);
        System.out.println("starting play logic");
        play(filePath);


        if (player != null) {
            player.setOnEndOfMedia(() -> {
                songFinished.set(true);
                isRunning.set(false);
            });
        }

        Thread uiThread = getThread(track, isRunning);
        uiThread.start();

        while (isRunning.get()) {
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine().trim().toLowerCase();

                switch (input) {
                    case "p":
                        pause();
                        break;
                    case "r":
                        resume();
                        break;
                    case "t":
                    case "autoplay":
                        MenuSystem.autoplay = !MenuSystem.autoplay;
                        System.out.println("Autoplay is now set to " + (MenuSystem.autoplay ? "ON" : "OFF"));
                        break;
                    case "s":
                    case "b":
                        stop();
                        isRunning.set(false);
                        break;
                }
            }
        }
        stop();
        try {
            uiThread.join(500);
        } catch (InterruptedException _) {
        }
        return songFinished.get();
    }

    @NotNull
    private Thread getThread(Track track, AtomicBoolean isRunning) {
        Thread uiThread = new Thread(() -> {
            long tick = 0;
            while (isRunning.get()) {
                if (player == null || player.getStatus() == MediaPlayer.Status.STOPPED) {
                    isRunning.set(false);
                    break;
                }
                double current = player.getCurrentTime().toSeconds();
                double total = player.getTotalDuration().toSeconds();
                if (total <= 0) total = 1;

                NowPlayingScreen.show(
                        track.title,
                        track.artist,
                        current,
                        total,
                        isPaused,
                        tick
                );
                tick++;

                if (current >= total - 0.5) {
                    isRunning.set(false);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }

            }
        });
        return uiThread;
    }
}

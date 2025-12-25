

public class NowPlayingScreen {

    public static void show(String title, String artist, double progress, double duration, boolean isPaused, long tick) {

        clearScreen();

        String timeStr = formatTime(progress) + " / " + formatTime(duration);

        int barWidth = 20;
        int filled = (int)((progress / duration) * barWidth);
        filled = Math.max(0, Math.min(filled, barWidth));

        String bar = "[" + "█".repeat(filled) + " ".repeat(barWidth - filled) + "]";

        String scrollTitle = getScrollText(title, tick);
        String scrollArtist = getScrollText(artist, tick);


        System.out.println("==========================================");
        System.out.println("         iPog Music Player");
        System.out.println("==========================================\n");

        System.out.println("+----------------------------------------+");
        System.out.println("|               NOW PLAYING              |");
        System.out.println("+----------------------------------------+");
        System.out.printf("| 🎵  Song:   %-26s |\n", scrollTitle);
        System.out.printf("| 🎤  Artist: %-26s |\n", scrollArtist);
        System.out.printf("| ⏱️  Time:   %-26s |\n", timeStr);
        System.out.println("|                                        |");
        System.out.printf("|   %-37s|\n", bar);
        System.out.println("|                                        |");
        System.out.println("+----------------------------------------+");
        String statusText = isPaused ? "[R]esume" : "[P]ause ";
        System.out.println("|      <<     " + statusText + "     >>       |");
        System.out.println("+----------------------------------------+");
    }

    private static String formatTime(double seconds) {
        int m = (int)(seconds / 60);
        int s = (int)(seconds % 60);
        return String.format("%02d:%02d", m, s);
    }
    private static void clearScreen() {
        System.out.print("\n".repeat(30));
       }

       private static String getScrollText(String text, long tick ) {
        if (text.length() <= 26) {
            return String.format("%-" + 26 + "s", text);
        }
        String gap = "   ";
        String fullText = text + gap;

        int start = (int) (tick % fullText.length());

        String loopedText = fullText + fullText;
        return loopedText.substring(start, start + 26);
       }


    }

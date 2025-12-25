import com.google.gson.JsonObject;

import java.util.Scanner;

public class Search {
    private final SpotifyScraper scraper = new SpotifyScraper();
    private final int pageSize = 10;

    public Track search(Scanner scanner) throws Exception {
        System.out.println("Please enter your search term: ");
        String query = scanner.nextLine();
        JsonObject result = scraper.search(query);
        Track[] tracks = scraper.buildTrackArray(result);

        Track selected = chooseTrack(tracks,10, scanner);
        return selected;
    }

    public void printPage(Track[] results, int page, int pageSize) {

        int total = results.length;
        int totalPages = (int) Math.ceil((double) results.length / pageSize);

        int start = page * pageSize;
        int end = Math.min(start + pageSize, total);

        System.out.println("\n--- Page " + (page + 1) + " of " + totalPages + " ---");

        for (int i = start; i < end; i++) {
            Track t = results[i];
            int displayNum = i - start + 1; // 1–10

            System.out.println(displayNum + ". " + t.artist + " - " + t.title);
        }

        System.out.println("\n[n] Next page   [p] Previous page   [q] Quit");
        System.out.println("[1-" + (end - start) + "] Choose a song");
        System.out.print("Enter choice: ");
    }

    public Track chooseTrack(Track[] results, int pageSize, Scanner scanner) {
        Scanner sc = new Scanner(System.in);

        int total = results.length;
        int totalPages = (int) Math.ceil((double) results.length / pageSize);

        int page = 0;

        while (true) {
            printPage(results, page, pageSize);
            String input = sc.nextLine().trim();


            if (input.equalsIgnoreCase("n")) {
                if (page < totalPages - 1) {
                    page++;
                } else {
                    System.out.println("There are no more pages.");
                }
            } else if (input.equalsIgnoreCase("p")) {
                if (page > 0) {
                    page--;
                } else {
                    System.out.println("You're on the first page.");
                }
            } else if (input.equalsIgnoreCase("q")) {
                return null;
            } else {
                try {
                    int num = Integer.parseInt(input);

                    int start = page * pageSize;
                    int end = Math.min(start + pageSize, total);
                    int pageCount = end - start;

                    if (num >= 1 && num <= pageCount) {
                        int globalIndex = start + (num - 1);
                        return results[globalIndex];
                    } else {
                        System.out.println("Invalid number for this page.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                }
            }
        }
    }
}



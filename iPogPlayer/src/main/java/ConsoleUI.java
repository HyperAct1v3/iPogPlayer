public class ConsoleUI {

    public static void clear() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    public static void header(String title) {
        System.out.println("====================================");
        System.out.println("         iPog Music Player");
        System.out.println("====================================");
        System.out.println(" " + title);
        System.out.println("------------------------------------\n");
    }


    public static void waitForEnter() {
        System.out.println("\nPress ENTER to continue...");
        new java.util.Scanner(System.in).nextLine();
    }
}

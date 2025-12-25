import java.io.*;

public class DataManager {
private static final String FILE_NAME = "library.ser";

    public static void saveLibrary(Library lib) {
        try {
            FileOutputStream fileOut = new FileOutputStream(FILE_NAME);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(lib);

            out.close();
            fileOut.close();
            System.out.println("Saved...");

        } catch (IOException i) {
            System.out.println("Error while saving library: " + i.getMessage());
            i.printStackTrace();
        }
    }
    public static Library loadData() {
        Library lib = null;
        try {
            FileInputStream fileIn = new FileInputStream("library.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);

            lib = (Library) in.readObject();

            in.close();
            fileIn.close();
            System.out.println("Library loaded successfully...");

        } catch (FileNotFoundException e) {
            System.out.println("No save file found. Creating a new library. ");
            lib = new Library();
        } catch (IOException| ClassNotFoundException e) {
            e.printStackTrace();

            lib = new Library();
        }
        return lib;
    }


}

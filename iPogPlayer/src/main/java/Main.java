
public class Main {
    static MenuSystem menu =  new MenuSystem();


public static void main(String[] args) {
    try {
        menu.run();
    } catch (Exception e) {
        System.out.println("SOMETHING REALLY BAD HAPPENED!! :( " + e.getMessage());
        e.printStackTrace();
    }

   }

}

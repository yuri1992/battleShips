package game.runners;

import java.util.Scanner;

public class ConsoleUtils {
    private static final Scanner in = new Scanner(System.in);

    /*
        Waiting function, will wait till will revice a valid input
     */
    public static int getIntergerInput() {
        int input = 0;
        if (in.hasNextLine()) {

        }

        return input;
    }

    /*
        Waiting function, will wait till will revice a valid input
     */
    public static int getIntegerByRange() {
        return 0;
    }

    /*
        Waiting function, will wait till will revice a valid input
     */
    public static String getString() {
        return in.nextLine();
    }

    public static String getStringByLength(int length) {
        return in.nextLine();
    }
}

package game.runners;

import java.util.Scanner;

public class ConsoleUtils {
    private static final Scanner in = new Scanner(System.in);

    public static boolean isValidInt(String stringType) {
        boolean isValid;
        try {
            Integer.parseInt(stringType);
            isValid = true;
        } catch (NumberFormatException e) {
            isValid = false;
        }

        return isValid;
    }

    /*
        Waiting function, will wait till will revice a valid input
     */
    public static int getIntegerInput() {
        String input = getString();
        return isValidInt(input) ? Integer.parseInt(input) : -1;
    }

    /*
        Waiting function, will wait till will revice a valid input
     */
    public static int getIntegerByRange(int start, int end) {
        int n = getIntegerInput();
        while (n < start || n > end) {
            System.out.println("Input value is not in the range of " + start + " to " + end);
            n = getIntegerInput();
        }
        return n;
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

package banking;

import java.sql.ResultSet;
import java.util.Scanner;

public class Main {

    static String pathToDB = "cards.s3db"; //default

    public static void main(String[] args) {
        Session session = new Session();
        Scanner scan = new Scanner(System.in);
        processArgs(args);
        Database.dbInit(pathToDB);
        Database.printAllCards();
        while (session.isRunning) {
            mainMenu();
            String in = scan.nextLine();
            switch (parseInput(in)) {
                case 1:
                    session.newAccount();
                    break;
                case 2:
                    //Database.printAllCards();
                    Account loggedIn = session.login();
                    if (loggedIn != null) {
                        session.loggedIn(loggedIn);
                    }
                    break;
                case 0:
                    session.end();
                    break;
                default:
                    // do nothing
            }
        }

    }

    static void processArgs(String[] args) {
        for (int ii = 0; ii < args.length; ii += 2) {
            switch (args[ii]) {
                case "-fileName":
                    pathToDB = args[ii + 1];
                    break;
                default:
                    //do nothing;
            }
        }
    }

    static void mainMenu() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    static int parseInput(String input) {
        return Integer.parseInt(input);
    }
}

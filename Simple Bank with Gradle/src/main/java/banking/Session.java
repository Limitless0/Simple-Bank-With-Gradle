package banking;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class Session {
    Random rand = new SecureRandom();
    int issuerID = 400000;
    int checksum;
    int[] acc = new int[9];
    int[] PIN = new int[4];
    boolean isRunning;

    Session () {
        this.isRunning = true;
    }

    private void setAcc() {
        for (int ii = 0; ii < acc.length; ii++) {
            acc[ii] = rand.nextInt(10);
        }
    }

    private void setPIN() {

        for (int ii = 0; ii < PIN.length; ii++) {
            PIN[ii] = rand.nextInt(10);
        }
    }

    void newAccount () {
        setAcc();
        setPIN();
        StringBuilder cardNum = new StringBuilder();
        cardNum.append(issuerID);
        for (int num : acc) {
            cardNum.append(num);
        }
        checksum = luhnAlgo(cardNum.toString());
        cardNum.append(checksum);

        StringBuilder sPIN = new StringBuilder();
        for (int num : PIN) {
            sPIN.append(num);
        }

        Database.newAcc(cardNum.toString(), sPIN.toString());

        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(cardNum.toString());
        System.out.println("Your card PIN:");
        System.out.println(sPIN.toString());
    }

    private int luhnAlgo(String cardNum) {
        int[] numbers = new int[15];
        int lastDigit;
        int sum = 0;
        for (int ii = 0; ii < cardNum.length(); ii++) {
            numbers[ii] = Integer.parseInt(String.valueOf(cardNum.charAt(ii)));
            if ((ii + 1) % 2 == 1) {
                numbers[ii] = 2 * numbers[ii];
            }
            if (numbers[ii] > 9) {
                numbers[ii] = numbers[ii] - 9;
            }
            sum += numbers[ii];
        }
        lastDigit = (10 - (sum % 10) % 10);
        if (lastDigit == 10) {
            lastDigit = 0;
        }
        return lastDigit;
    }

    Account login () {
        System.out.println("Enter your card number:");
        Scanner scan = new Scanner(System.in);
        String cardnum = scan.nextLine();
        System.out.println("Enter your PIN:");
        String sPIN = scan.nextLine();
        Account result = Database.getCard(cardnum, sPIN);
        if (result == null) {
            System.out.println("Wrong card number or PIN!");
        }
        return result;
    }

    void end () {
        System.out.println("Bye!");
        this.isRunning = false;
    }

    public void loggedIn(Account account) {
        boolean loggedIn = true;

        System.out.println("You have successfully logged in!");


        while (loggedIn) {
            System.out.println("1. Balance");
            System.out.println("2. Close Account");
            System.out.println("3. Log out");
            System.out.println("0. Exit");
            Scanner scan = new Scanner(System.in);
            int input = Main.parseInput(scan.nextLine());
            switch (input) {
                case 1:
                    System.out.println("Balance: " + account.bal);
                    break;
                case 2:
                    Database.deleteAcc(account.cardNum);
                    System.out.println("The account has been closed!");
                    loggedIn = false;
                    break;
                case 3:
                    System.out.println("You have successfully logged out!");
                    loggedIn = false;
                    break;
                case 0:
                    end();
                    loggedIn = false;
            }
        }
    }
}


package banking;

public class Account {
    int ID;
    String cardNum;
    String PIN;
    int bal;
    Account(int ID, String cardNum, String pin, int bal) {
        this.ID = ID;
        this.cardNum = cardNum;
        this.PIN = pin;
        this.bal = bal;
    }
}

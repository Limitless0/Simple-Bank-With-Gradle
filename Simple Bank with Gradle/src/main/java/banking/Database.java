package banking;

import org.sqlite.SQLiteDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    static SQLiteDataSource dataSource = new SQLiteDataSource();

    static void dbInit(String path) {
        String url = "jdbc:sqlite:" + path;
        dataSource.setUrl(url);
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "number TEXT," +
                        "pin TEXT," +
                        "balance INTEGER DEFAULT 0)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    static void deleteAcc(String cardNum){
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery("DELETE FROM card " +
                        "WHERE number = '" + cardNum + "'");
                System.out.println("The account has been closed!");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    static Account getCard(String cardnum, String pin) {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try {
                    ResultSet cards = statement.executeQuery("SELECT * FROM card " +
                        "WHERE number = '" + cardnum + "'" +
                        "AND pin = '" + pin + "'");
                    if (cards.next()) {
                        return new Account(cards.getInt("id"),
                                cards.getString("number"),
                                cards.getString("pin"),
                                cards.getInt("balance"));
                    } else {
                        cards.close();
                        return null;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static void printAllCards() {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet cards = statement.executeQuery("SELECT * FROM card")) {
                    while (cards.next()) {
                        // Retrieve column values
                        int id = cards.getInt("id");
                        String num = cards.getString("number");
                        String pin = cards.getString("pin");
                        int bal = cards.getInt("balance");

                        System.out.printf("ID %d", id);
                        System.out.printf("\tNum: %s", num);
                        System.out.printf("\tPin: %s", pin);
                        System.out.printf("\tBal %d\n", bal);
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    static void newAcc (String card, String pin) {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(String.format("INSERT INTO card(number,pin)" +
                        "Values('%s','%s')", card, pin));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

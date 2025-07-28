package Model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import model.DatabaseConnection;

public class DatabaseManager {

    public static void initializeDatabase() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connected to the database.");
                createTables(connection);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createTables(Connection connection) {
        String createMembersTable = "CREATE TABLE IF NOT EXISTS members (" 
            + "memberID VARCHAR(255) PRIMARY KEY, "
            + "name VARCHAR(255) NOT NULL, "
            + "contactNumber VARCHAR(255) NOT NULL, "
            + "cardNumber VARCHAR(255), "
            + "expirationDate VARCHAR(255))";

        String createBooksTable = "CREATE TABLE IF NOT EXISTS books ("
            + "bookID VARCHAR(255) PRIMARY KEY, "
            + "title VARCHAR(255) NOT NULL, "
            + "author VARCHAR(255) NOT NULL, "
            + "yearPublished INT)";

        String createAdminUsersTable = "CREATE TABLE IF NOT EXISTS admin_users ("
            + "username VARCHAR(255) PRIMARY KEY, "
            + "password VARCHAR(255) NOT NULL)";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createMembersTable);
            statement.execute(createBooksTable);
            statement.execute(createAdminUsersTable);
            System.out.println("Tables created successfully.");

            // Insert default admin user
            String insertAdminUser = "INSERT IGNORE INTO admin_users (username, password) "
                + "VALUES ('admin', 'admin123')";
            statement.execute(insertAdminUser);
            System.out.println("Default admin user inserted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
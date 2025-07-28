package Main;

import Controller.BookController;
import Model.DatabaseManager;
import View.LoginView;
import Controller.LoginController;
import View.BookView;
import model.DatabaseConnection;

public class Main {

    public static void main(String[] args) { // Initialize the database DatabaseManager.initializeDatabase(); // Launch the login view

        System.out.println("Initializing the Library Management System...");
        
        DatabaseConnection.initializeDatabase(); // Initialize tables
        
        // Launch the application (e.g., Login View)
        LoginView loginView = new LoginView();
        new LoginController(loginView);
        loginView.setVisible(true);

//        BookView bookView = new BookView();
//        BookController controller = new BookController(bookView);
//        bookView.setVisible(true);
    }
}

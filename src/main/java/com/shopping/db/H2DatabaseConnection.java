package com.shopping.db;

import org.h2.tools.RunScript;
import org.h2.tools.Server;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class H2DatabaseConnection {
    private static final Logger logger = Logger.getLogger(H2DatabaseConnection.class.getName());

    static {
        try {
            initializeDatabase(getConnectionToDatabase());
        } catch (FileNotFoundException exception) {
            logger.log(Level.SEVERE, "Could not find the .sql file", exception);
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "SQL error", exception);
        }
    }

    static Server server;

    public static Connection getConnectionToDatabase() {
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:h2:mem:shoppingDb", "", "");

        } catch (Exception exception) {
            logger.log(Level.SEVERE, "Could not set up connection", exception);
        }
        logger.info("Connection set up completed");
        return connection;
    }

    /* Loads and run the initialize.sql file **/
    public static void initializeDatabase(Connection conn) throws FileNotFoundException, SQLException {
        InputStream resource = H2DatabaseConnection.class.getClassLoader().getResourceAsStream("initialize.sql");
        RunScript.execute(conn, new InputStreamReader(resource));
    }

}

import database.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Statement;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inizializza il DB all'avvio e popola i dati di default!
        DatabaseManager.getInstance();
        inizializzaDatiDiDefault();

        // Carica la schermata grafica (ho lasciato il tuo percorso corretto)
        Parent root = FXMLLoader.load(getClass().getResource("/resources/Login.fxml"));

        primaryStage.setTitle("Segreteria Studenti - Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Metodo per inserire i dati "di fabbrica" al primo avvio
    private void inizializzaDatiDiDefault() {
        Connection conn = DatabaseManager.getInstance().getConnection();
        try (Statement stmt = conn.createStatement()) {
            // Inserisce la segreteria (ID: 1, Password: admin123)
            stmt.execute("INSERT OR IGNORE INTO Segreteria (ID, Nome, Password) VALUES (1, 'Segreteria Centrale', 'admin123')");

            // Inseriamo anche un Docente e un Corso per testare dopo le altre schermate
            stmt.execute("INSERT OR IGNORE INTO Docente (CF, Password, Nome, Cognome, ID) VALUES ('DOC123', 'passDoc', 'Mario', 'Verdi', 1)");
            stmt.execute("INSERT OR IGNORE INTO Corso (Nome, CFU, Anno) VALUES ('Programmazione 3', 9, 2026)");
        } catch (Exception e) {
            System.err.println("Errore inserimento dati di default: " + e.getMessage());
        }
    }
}
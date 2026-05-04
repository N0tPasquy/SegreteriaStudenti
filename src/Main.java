import database.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inizializza il DB all'avvio e popola i dati di default!
        DatabaseManager.getInstance();
        inizializzaDatiDiDefault();

        // Carica la schermata grafica (ho lasciato il tuo percorso corretto)
        Parent root = FXMLLoader.load(getClass().getResource("/resources/DashboardSegreteria.fxml"));

        primaryStage.setTitle("Dashboard Segreteria");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Metodo per inserire i dati "di fabbrica" al primo avvio
    private void inizializzaDatiDiDefault() throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        try (Statement stmt = conn.createStatement()) {
            // Aggiungo la segreteria
            stmt.execute("INSERT OR IGNORE INTO Segreteria (ID, Nome, Password) VALUES (1, 'Segreteria Centrale', 'admin123')");

            // Inserisco un docente e 3 Corsi come test
            stmt.execute("INSERT OR IGNORE INTO Docente (CF, Password, Nome, Cognome, ID) VALUES ('GLLLGU03T02A512D', '1234', 'Luigi', 'Galio', 1)");

            stmt.execute("INSERT OR IGNORE INTO Corso (Nome, CFU, Anno) VALUES ('Programmazione 3', 9, 2026)");
            stmt.execute("INSERT OR IGNORE INTO Corso (Nome, CFU, Anno) VALUES ('Basi di Dati', 6, 2026)");
            stmt.execute("INSERT OR IGNORE INTO Corso (Nome, CFU, Anno) VALUES ('Ingegneria del Software', 9, 2026)");

            stmt.execute("INSERT OR IGNORE INTO Studente (Matricola, Password, Nome, Cognome, Residenza, DataNascita, TassePagate, ID) " +
                    "VALUES ('1', 'pass123', 'Nazzaro', 'Tessitore', 'Frignano', '2003-09-14', 1, 1)");

            // Aggiungo degli Appelli, servono per collegare l'esito
            stmt.execute("INSERT OR IGNORE INTO Appello (NomeCorso, Data) VALUES ('Programmazione 3', '2026-05-10')");
            stmt.execute("INSERT OR IGNORE INTO Appello (NomeCorso, Data) VALUES ('Basi di Dati', '2026-06-15')");
            stmt.execute("INSERT OR IGNORE INTO Appello (NomeCorso, Data) VALUES ('Ingegneria del Software', '2026-07-20')");

            // Creo 3 Esiti in attesa di verbalizzazione per la matricola 1
            stmt.execute("INSERT OR IGNORE INTO Esito (ID, Voto, Lode, Stato, Tipo, NomeCorso, Data, Matricola) " +
                    "VALUES (1, 28, 0, 'Accettato', 'Orale', 'Programmazione 3', '2026-05-10', '1')");

            stmt.execute("INSERT OR IGNORE INTO Esito (ID, Voto, Lode, Stato, Tipo, NomeCorso, Data, Matricola) " +
                    "VALUES (2, 30, 1, 'Accettato', 'Scritto', 'Basi di Dati', '2026-06-15', '1')");

            stmt.execute("INSERT OR IGNORE INTO Esito (ID, Voto, Lode, Stato, Tipo, NomeCorso, Data, Matricola) " +
                    "VALUES (3, 25, 0, 'Accettato', 'Progetto', 'Ingegneria del Software', '2026-07-20', '1')");

            System.out.println("Dati di default generati correttamente.");
        } catch (Exception e) {
            System.err.println("Errore inserimento dati di default: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
}
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
        Parent root = FXMLLoader.load(getClass().getResource("/resources/Login.fxml"));

        primaryStage.setTitle("Login");
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
            // --- Segreteria ---
            stmt.execute("INSERT OR IGNORE INTO Segreteria (ID, Nome, Password) VALUES (1, 'Segreteria Centrale', 'admin123')");

            // --- Docenti (10) ---
            stmt.execute("INSERT OR IGNORE INTO Docente (CF, Password, Nome, Cognome, ID) VALUES ('GLLLGU03T02A512D', '1234', 'Luigi', 'Galio', 1)");
            stmt.execute("INSERT OR IGNORE INTO Docente (CF, Password, Nome, Cognome, ID) VALUES ('RSSMRA80A01H501U', 'pass1', 'Maria', 'Rossi', 1)");
            stmt.execute("INSERT OR IGNORE INTO Docente (CF, Password, Nome, Cognome, ID) VALUES ('BNCLRD85B02F205V', 'pass2', 'Leonardo', 'Bianchi', 1)");
            stmt.execute("INSERT OR IGNORE INTO Docente (CF, Password, Nome, Cognome, ID) VALUES ('VRDGNN90C03L736W', 'pass3', 'Giovanni', 'Verdi', 1)");
            stmt.execute("INSERT OR IGNORE INTO Docente (CF, Password, Nome, Cognome, ID) VALUES ('NGRFNC88D04M128X', 'pass4', 'Francesco', 'Neri', 1)");
            stmt.execute("INSERT OR IGNORE INTO Docente (CF, Password, Nome, Cognome, ID) VALUES ('GLLSNT92E05E456Y', 'pass5', 'Santo', 'Gallo', 1)");
            stmt.execute("INSERT OR IGNORE INTO Docente (CF, Password, Nome, Cognome, ID) VALUES ('PLMCHR95F06G789Z', 'pass6', 'Chiara', 'Palumbo', 1)");
            stmt.execute("INSERT OR IGNORE INTO Docente (CF, Password, Nome, Cognome, ID) VALUES ('CNTMRC87G07H012A', 'pass7', 'Marco', 'Conti', 1)");
            stmt.execute("INSERT OR IGNORE INTO Docente (CF, Password, Nome, Cognome, ID) VALUES ('FNTGPP91H08I345B', 'pass8', 'Giuseppe', 'Fontana', 1)");
            stmt.execute("INSERT OR IGNORE INTO Docente (CF, Password, Nome, Cognome, ID) VALUES ('LMBRBT89I09J678C', 'pass9', 'Roberto', 'Lombardi', 1)");

            // --- Corsi (10) ---
            stmt.execute("INSERT OR IGNORE INTO Corso (Nome, CFU, Anno) VALUES ('Programmazione 3', 9, 2026)");
            stmt.execute("INSERT OR IGNORE INTO Corso (Nome, CFU, Anno) VALUES ('Basi di Dati', 6, 2026)");
            stmt.execute("INSERT OR IGNORE INTO Corso (Nome, CFU, Anno) VALUES ('Ingegneria del Software', 9, 2026)");
            stmt.execute("INSERT OR IGNORE INTO Corso (Nome, CFU, Anno) VALUES ('Analisi Matematica 2', 9, 2026)");
            stmt.execute("INSERT OR IGNORE INTO Corso (Nome, CFU, Anno) VALUES ('Fisica Generale', 6, 2026)");
            stmt.execute("INSERT OR IGNORE INTO Corso (Nome, CFU, Anno) VALUES ('Algoritmi e Strutture Dati', 9, 2026)");
            stmt.execute("INSERT OR IGNORE INTO Corso (Nome, CFU, Anno) VALUES ('Reti di Calcolatori', 6, 2026)");
            stmt.execute("INSERT OR IGNORE INTO Corso (Nome, CFU, Anno) VALUES ('Sistemi Operativi', 9, 2026)");
            stmt.execute("INSERT OR IGNORE INTO Corso (Nome, CFU, Anno) VALUES ('Programmazione 1', 6, 2026)");
            stmt.execute("INSERT OR IGNORE INTO Corso (Nome, CFU, Anno) VALUES ('Calcolo Numerico', 9, 2026)");

            // --- Studenti (10) ---
            stmt.execute("INSERT OR IGNORE INTO Studente (Matricola, Password, Nome, Cognome, Residenza, DataNascita, TassePagate, ID) VALUES ('MAT001', 'pass123', 'Nazzaro', 'Tessitore', 'Frignano', '2003-09-14', 1, 1)");
            stmt.execute("INSERT OR IGNORE INTO Studente (Matricola, Password, Nome, Cognome, Residenza, DataNascita, TassePagate, ID) VALUES ('MAT002', 'stud02', 'Alice', 'Verdi', 'Caserta', '2002-03-10', 1, 2)");
            stmt.execute("INSERT OR IGNORE INTO Studente (Matricola, Password, Nome, Cognome, Residenza, DataNascita, TassePagate, ID) VALUES ('MAT003', 'stud03', 'Marco', 'Bianchi', 'Napoli', '2001-07-22', 1, 3)");
            stmt.execute("INSERT OR IGNORE INTO Studente (Matricola, Password, Nome, Cognome, Residenza, DataNascita, TassePagate, ID) VALUES ('MAT004', 'stud04', 'Sofia', 'Russo', 'Aversa', '2003-11-05', 0, 4)");
            stmt.execute("INSERT OR IGNORE INTO Studente (Matricola, Password, Nome, Cognome, Residenza, DataNascita, TassePagate, ID) VALUES ('MAT005', 'stud05', 'Luca', 'Ferrari', 'Santa Maria Capua Vetere', '2000-01-30', 1, 5)");
            stmt.execute("INSERT OR IGNORE INTO Studente (Matricola, Password, Nome, Cognome, Residenza, DataNascita, TassePagate, ID) VALUES ('MAT006', 'stud06', 'Giulia', 'Romano', 'Maddaloni', '2002-05-18', 1, 6)");
            stmt.execute("INSERT OR IGNORE INTO Studente (Matricola, Password, Nome, Cognome, Residenza, DataNascita, TassePagate, ID) VALUES ('MAT007', 'stud07', 'Andrea', 'Gallo', 'Marcianise', '2001-09-02', 0, 7)");
            stmt.execute("INSERT OR IGNORE INTO Studente (Matricola, Password, Nome, Cognome, Residenza, DataNascita, TassePagate, ID) VALUES ('MAT008', 'stud08', 'Elena', 'Costa', 'San Nicolò', '2003-12-11', 1, 8)");
            stmt.execute("INSERT OR IGNORE INTO Studente (Matricola, Password, Nome, Cognome, Residenza, DataNascita, TassePagate, ID) VALUES ('MAT009', 'stud09', 'Davide', 'Barbieri', 'Capodrise', '2000-06-25', 1, 9)");
            stmt.execute("INSERT OR IGNORE INTO Studente (Matricola, Password, Nome, Cognome, Residenza, DataNascita, TassePagate, ID) VALUES ('MAT010', 'stud10', 'Alice', 'Verdi', 'Recale', '2002-08-14', 1, 10)");

            // --- Appelli (10+) ---
            stmt.execute("INSERT OR IGNORE INTO Appello (NomeCorso, Data) VALUES ('Programmazione 3', '2026-05-10')");
            stmt.execute("INSERT OR IGNORE INTO Appello (NomeCorso, Data) VALUES ('Programmazione 3', '2026-06-20')");
            stmt.execute("INSERT OR IGNORE INTO Appello (NomeCorso, Data) VALUES ('Basi di Dati', '2026-06-15')");
            stmt.execute("INSERT OR IGNORE INTO Appello (NomeCorso, Data) VALUES ('Ingegneria del Software', '2026-07-20')");
            stmt.execute("INSERT OR IGNORE INTO Appello (NomeCorso, Data) VALUES ('Analisi Matematica 2', '2026-06-01')");
            stmt.execute("INSERT OR IGNORE INTO Appello (NomeCorso, Data) VALUES ('Fisica Generale', '2026-06-08')");
            stmt.execute("INSERT OR IGNORE INTO Appello (NomeCorso, Data) VALUES ('Algoritmi e Strutture Dati', '2026-06-22')");
            stmt.execute("INSERT OR IGNORE INTO Appello (NomeCorso, Data) VALUES ('Reti di Calcolatori', '2026-06-10')");
            stmt.execute("INSERT OR IGNORE INTO Appello (NomeCorso, Data) VALUES ('Sistemi Operativi', '2026-05-25')");
            stmt.execute("INSERT OR IGNORE INTO Appello (NomeCorso, Data) VALUES ('Programmazione 1', '2026-06-18')");

            // --- Esiti ---
            stmt.execute("INSERT OR IGNORE INTO Esito (ID, Voto, Lode, Stato, Tipo, NomeCorso, Data, Matricola) VALUES (1, 28, 0, 'In Attesa', 'Orale', 'Programmazione 3', '2026-05-10', 'MAT001')");
            stmt.execute("INSERT OR IGNORE INTO Esito (ID, Voto, Lode, Stato, Tipo, NomeCorso, Data, Matricola) VALUES (2, 30, 1, 'In Attesa', 'Scritto', 'Basi di Dati', '2026-06-15', 'MAT001')");
            stmt.execute("INSERT OR IGNORE INTO Esito (ID, Voto, Lode, Stato, Tipo, NomeCorso, Data, Matricola) VALUES (3, 0, 0, 'Assente', 'Orale', 'Ingegneria del Software', '2026-07-20', 'MAT001')");
            stmt.execute("INSERT OR IGNORE INTO Esito (ID, Voto, Lode, Stato, Tipo, NomeCorso, Data, Matricola) VALUES (4, 25, 0, 'Accettato', 'Orale', 'Ingegneria del Software', '2026-07-20', 'MAT001')");
            stmt.execute("INSERT OR IGNORE INTO Esito (ID, Voto, Lode, Stato, Tipo, NomeCorso, Data, Matricola) VALUES (5, 22, 0, 'In Attesa', 'Scritto', 'Programmazione 3', '2026-06-20', 'MAT002')");

            // --- Tiene (Assegnazione Corsi ai Docenti) ---
            stmt.execute("INSERT OR IGNORE INTO Tiene (CFDocente, NomeCorso) VALUES ('GLLLGU03T02A512D', 'Programmazione 3')");
            stmt.execute("INSERT OR IGNORE INTO Tiene (CFDocente, NomeCorso) VALUES ('RSSMRA80A01H501U', 'Basi di Dati')");
            stmt.execute("INSERT OR IGNORE INTO Tiene (CFDocente, NomeCorso) VALUES ('BNCLRD85B02F205V', 'Ingegneria del Software')");
            stmt.execute("INSERT OR IGNORE INTO Tiene (CFDocente, NomeCorso) VALUES ('VRDGNN90C03L736W', 'Analisi Matematica 2')");
            stmt.execute("INSERT OR IGNORE INTO Tiene (CFDocente, NomeCorso) VALUES ('NGRFNC88D04M128X', 'Fisica Generale')");

            // --- DeveSeguire (Piani di Studi degli Studenti) ---
            // MAT001 (Nazzaro)
            stmt.execute("INSERT OR IGNORE INTO DeveSeguire (MatricolaStudente, NomeCorso) VALUES ('MAT001', 'Programmazione 3')");
            stmt.execute("INSERT OR IGNORE INTO DeveSeguire (MatricolaStudente, NomeCorso) VALUES ('MAT001', 'Basi di Dati')");
            stmt.execute("INSERT OR IGNORE INTO DeveSeguire (MatricolaStudente, NomeCorso) VALUES ('MAT001', 'Ingegneria del Software')");
            // MAT002 (Alice)
            stmt.execute("INSERT OR IGNORE INTO DeveSeguire (MatricolaStudente, NomeCorso) VALUES ('MAT002', 'Programmazione 3')");
            stmt.execute("INSERT OR IGNORE INTO DeveSeguire (MatricolaStudente, NomeCorso) VALUES ('MAT002', 'Analisi Matematica 2')");
            // MAT003 (Marco)
            stmt.execute("INSERT OR IGNORE INTO DeveSeguire (MatricolaStudente, NomeCorso) VALUES ('MAT003', 'Fisica Generale')");
            stmt.execute("INSERT OR IGNORE INTO DeveSeguire (MatricolaStudente, NomeCorso) VALUES ('MAT003', 'Algoritmi e Strutture Dati')");

            // --- SiPrenota (Prenotazioni agli Appelli) ---
            // Creiamo alcune prenotazioni per far funzionare la "Ricerca Studenti Prenotati" lato Docente
            stmt.execute("INSERT OR IGNORE INTO SiPrenota (MatricolaStudente, DataAppello, NomeCorso) VALUES ('MAT001', '2026-05-10', 'Programmazione 3')");
            stmt.execute("INSERT OR IGNORE INTO SiPrenota (MatricolaStudente, DataAppello, NomeCorso) VALUES ('MAT001', '2026-06-15', 'Basi di Dati')");
            stmt.execute("INSERT OR IGNORE INTO SiPrenota (MatricolaStudente, DataAppello, NomeCorso) VALUES ('MAT002', '2026-06-20', 'Programmazione 3')");
            stmt.execute("INSERT OR IGNORE INTO SiPrenota (MatricolaStudente, DataAppello, NomeCorso) VALUES ('MAT002', '2026-06-01', 'Analisi Matematica 2')");
            stmt.execute("INSERT OR IGNORE INTO SiPrenota (MatricolaStudente, DataAppello, NomeCorso) VALUES ('MAT003', '2026-06-08', 'Fisica Generale')");

            System.out.println("Dati di default generati correttamente.");
        } catch (Exception e) {
            System.err.println("Errore inserimento dati di default: " + e.getMessage());
        } finally {
            conn.close();
        }
    }
}
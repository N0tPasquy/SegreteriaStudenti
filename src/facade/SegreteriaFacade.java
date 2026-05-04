package facade;

import database.DatabaseManager;
import strategy.SearchStrategy;

import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SegreteriaFacade {

    // Inseriamo un nuovo studente
    public void iscriviStudente(String Matricola, String Password, String Nome, String Cognome, String Residenza, Date DataNascita) throws SQLException {
        String sql = "INSERT INTO Studente (Matricola, Password, Nome, Cognome, Residenza, DataNascita) VALUES (?, ?, ?, ?, ?, ?)";

        // Prendiamo la connessione FUORI dal try-with-resources
        Connection conn = DatabaseManager.getInstance().getConnection();

        // Mettiamo solo lo statement dentro le parentesi!
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, Matricola);
            stmt.setString(2, Password);
            stmt.setString(3, Nome);
            stmt.setString(4, Cognome);
            stmt.setString(5, Residenza);
            stmt.setDate(6, DataNascita);

            stmt.executeUpdate();

            // Lascio un log per assicurarmi che vada tutto correttamente durante l'inserimento
            System.out.println("Successo: Studente " + Nome + " " + Cognome + " iscritto correttamente!");
        } catch (SQLException e) {
            //System.err.println("Errore durante l'iscrizione: La matricola potrebbe già esistere."); Prima stampavo l'errore sulla console, ora passo l'errore alla GUI
            throw e;
        } finally {
            conn.close();
        }
    }

    // Ricercare uno studente usando lo Strategy Pattern
    public String visualizzaStudente(SearchStrategy strategia, String inputDiRicerca) throws SQLException {
        System.out.println("Ricerca in corso...");
        System.out.println(inputDiRicerca);
        return strategia.cerca(inputDiRicerca);
    }

    // Aggiungi un corso da seguire ad uno studente
    public void aggiungiCorso(String Matricola, String NomeCorso) throws SQLException {
        // Query di controllo per verificare che il corso che sto associando ad uno studente esista
        String sqlCheck = "SELECT Nome FROM Corso WHERE Nome = ?";
        String sql = "INSERT INTO DeveSeguire (MatricolaStudente, NomeCorso) VALUES (?, ?)";

        try(Connection conn = DatabaseManager.getInstance().getConnection()){
            // Uso un blocco try per eseguire la query che cerca se il corso esiste
            try (PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)){
                stmtCheck.setString(1, NomeCorso);

                // Eseguo la query dentro un if perche' .next ritorna falso se non c'e' nulla
                if(!stmtCheck.executeQuery().next()){
                    throw new SQLException("Il corso '" + NomeCorso + "' non esiste nel sistema.");
                }
            }

            // Se la query di prima non ha fatto scattare l'eccezione del corso non esistente procedo con l'inserimento del corso al piano di studi
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, Matricola);
                stmt.setString(2, NomeCorso);

                // Se questo Corso e' gia' presente nel piano di studi scatta l'eccezione per violazione di primary key
                stmt.executeUpdate();
            }
        } catch (SQLException e){
            throw e;
        }
    }

    // Rimuovo un corso dal piano di studi di uno studente
    public void eliminaCorso(String Matricola, String NomeCorso) throws SQLException {
        String sql = "DELETE FROM DeveSeguire WHERE MatricolaStudente = ? AND NomeCorso = ?";

        try(Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, Matricola);
            stmt.setString(2, NomeCorso);

            // Salvo il numero di righe modificate nel DB
            int righeModificate = stmt.executeUpdate();

            // Se le righe modificare sono 0, non e' successo nulla, quindi lo studente non aveva il corso assegnato
            if(righeModificate == 0){
                throw new SQLException("Il corso non era presente nel piano di studi!");
            }
        } catch (SQLException e) {
            throw e;
        }
    }
}

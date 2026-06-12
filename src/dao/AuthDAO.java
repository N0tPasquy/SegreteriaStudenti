package dao;

import database.DatabaseManager;
import model.CredenzialiDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object per l'autenticazione.
 * Cerca un utente in tutte le tabelle (Studente, Docente, Segreteria) per username.
 */
public class AuthDAO {

    /**
     * Trova un utente nel database a partire dall'username (matricola/CF/ID).
     * @param Username identificativo dell'utente
     * @return CredenzialiDTO con ruolo e password, oppure null se non trovato
     * @throws SQLException se si verifica un errore DB
     */
    public CredenzialiDTO trovaUtente(String Username) throws SQLException {

        try (Connection conn = DatabaseManager.getInstance().getConnection()) {
            // Studente
            try (PreparedStatement stmt = conn.prepareStatement("SELECT Password FROM Studente WHERE Matricola = ?")) {
                stmt.setString(1, Username);
                ResultSet rs = stmt.executeQuery();
                // Se la query è andata allora esiste un utente con quell'Username e mi riprendo la password dal DB per confrontarla con quella inserita
                if (rs.next()) return new CredenzialiDTO(Username, rs.getString("Password"), "STUDENTE");
            }

            // Docente
            try (PreparedStatement stmt = conn.prepareStatement("SELECT Password FROM Docente WHERE CF= ?")) {
                stmt.setString(1, Username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) return new CredenzialiDTO(Username, rs.getString("Password"), "DOCENTE");
            }

            // Segreteria
            try (PreparedStatement stmt = conn.prepareStatement("SELECT Password FROM Segreteria WHERE ID = ?")) {
                stmt.setString(1, Username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) return new CredenzialiDTO(Username, rs.getString("Password"), "SEGRETERIA");
            }
        }

        // Se non lo trova da nessuna parteritorna null
        return null;
    }
}

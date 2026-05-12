package dao;

import database.DatabaseManager;
import model.CredenzialiDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Data Access Object
public class AuthDAO {
    public CredenzialiDTO trovaUtente(String Username) throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();

        try{
            // Studente
            try(PreparedStatement stmt = conn.prepareStatement("SELECT Password FROM Studente WHERE Matricola = ?")){
                stmt.setString(1, Username);
                ResultSet rs = stmt.executeQuery();
                // Se la query è andata allora esiste un utente con quell'Username e mi riprendo la password dal DB per confrontarla con quella inserita
                if(rs.next()) return new CredenzialiDTO(Username, rs.getString("Password"), "STUDENTE");
            }

            // Docente
            try(PreparedStatement stmt = conn.prepareStatement("SELECT Password FROM Docente WHERE CF= ?")) {
                stmt.setString(1, Username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) return new CredenzialiDTO(Username, rs.getString("Password"), "DOCENTE");
            }

            // Segreteria
            try(PreparedStatement stmt = conn.prepareStatement("SELECT Password FROM Segreteria WHERE ID = ?")) {
                stmt.setString(1, Username);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()) return new CredenzialiDTO(Username, rs.getString("Password"),"SEGRETERIA");
            }
        } finally {
            conn.close();
        }

        // Se non lo trova da nessuna parte, chiudiamo la connessione e ritorna null
        return null;
    }
}

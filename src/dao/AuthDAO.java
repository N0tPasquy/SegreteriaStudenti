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
            // Cerchiamo se è uno Studente (Usa la matricola)
            try(PreparedStatement stmt = conn.prepareStatement("SELECT Password FROM Studente WHERE Matricola = ?")){
                stmt.setString(1, Username);
                ResultSet rs = stmt.executeQuery();
                // Se la query è andata allora esiste un utente con quell'Username e mi riprendo la password dal DB per confrontarla con quella inserita
                if(rs.next()) return new CredenzialiDTO(Username, rs.getString("Password"), "STUDENTE");
            }catch (SQLException e) {
                e.printStackTrace();
            }

            // Cerchiamo se è un Docente (usa il CF)
            try(PreparedStatement stmt = conn.prepareStatement("SELECT Password FROM Docente WHERE CF= ?")) {
                stmt.setString(1, Username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) return new CredenzialiDTO(Username, rs.getString("Password"), "DOCENTE");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Cerchiamo se è la segreteria (usa l' ID)
            try(PreparedStatement stmt = conn.prepareStatement("SELECT Password FROM Segreteria WHERE ID = ?")) {
                stmt.setString(1, Username);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()) return new CredenzialiDTO(Username, rs.getString("Password"),"SEGRETERIA");
            }catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
                throw new RuntimeException(e);
        } finally {
            conn.close();
        }

        // Se non lo trova da nessuna parte, ritorna null
        return null;
    }
}

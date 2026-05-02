package strategy;

import database.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchByMatricola implements SearchStrategy {
    @Override
    public String cerca(String Matricola) throws SQLException {
        String sql = "SELECT * FROM Studente WHERE Matricola = ?";
        return eseguiRicerca(sql, Matricola, null);
    }

    // Metodo helper interno per non duplicare il codice Java DataBase Connectivity
    protected String eseguiRicerca(String sql, String param1, String param2) throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, param1);
            if (param2 != null) stmt.setString(2, param2);

            ResultSet rs = stmt.executeQuery();

            // Per restituire tutti gli studenti trovati "costruisco" la stringa da ritornare
            StringBuilder risultati = new StringBuilder();

            while (rs.next()) {
                risultati.append("Trovato ").append(rs.getString("Nome")).append(" ").append(rs.getString("Cognome")).append(" (Matricola: ").append(rs.getString("Matricola")).append(")\n");
            }
            if (risultati.isEmpty()) return "Nessun studente trovato con questi criteri.";

            // Se è andato tutto okay ritorno la stringa costruita da piu' righe (se ci sono studenti con stesso nome e congome)
            return risultati.toString();    // .toString() perché' risultati è di tipo StringBuilder

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
        return "Nessuno studente trovato";
    }
}

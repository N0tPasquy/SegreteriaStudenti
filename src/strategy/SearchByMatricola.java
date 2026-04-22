package strategy;

import database.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SearchByMatricola implements SearchStrategy {
    @Override
    public void cerca(String Matricola){
        String sql = "SELECT * FROM Studente WHERE Matricola = ?";
        eseguiRicerca(sql, Matricola, null);
    }

    // Metodo helper interno per non duplicare il codice Java DataBase Connectivity
    protected void eseguiRicerca(String sql, String param1, String param2) {
        Connection conn = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, param1);
            if (param2 != null) stmt.setString(2, param2);

            ResultSet rs = stmt.executeQuery();
            boolean trovato = false;
            while (rs.next()) {
                trovato = true;
                System.out.println("Trovato: " + rs.getString("Nome") + " " + rs.getString("Cognome") +
                        " (Matricola: " + rs.getString("Matricola") + ")");
            }
            if (!trovato) System.out.println("Nessun studente trovato con questi criteri.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

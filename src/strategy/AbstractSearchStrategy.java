package strategy;

import database.DatabaseManager;
import model.StudenteDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * Classe che fornisce il metodo template "eseguiRicerca" che le sottoclassi concrete riutilizzano,
 * variando solo la query SQL e i parametri.
 */
public abstract class AbstractSearchStrategy implements SearchStrategy {
    /*
     * Il metodo accetta un numero variabile di parametri String (String...) in modo da
     * supportare sia la ricerca per matricola (1 parametro) sia per nome+cognome (2 parametri).
     */
    protected List<StudenteDTO> eseguiRicerca(String sql, String... Parametri) throws SQLException {
        List<StudenteDTO> Risultati = new ArrayList<>();
        Connection conn = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < Parametri.length; i++) {
                stmt.setString(i + 1, Parametri[i]);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Risultati.add(new StudenteDTO(
                        rs.getString("Nome"),
                        rs.getString("Cognome"),
                        rs.getString("Matricola"),
                        rs.getString("DataNascita"),
                        rs.getString("Residenza"),
                        rs.getInt("TassePagate") == 1,   // SQLite salva boolean come 0/1
                        rs.getString("PianoStudi"),
                        rs.getString("VotiVerbalizzati")
                ));
            }
        } finally {
            conn.close();
        }

        return Risultati;
    }
}

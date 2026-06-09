package strategy;

import database.DatabaseManager;
import model.StudenteDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe astratta che implementa il metodo template per l'esecuzione di una ricerca.
 * Le sottoclassi devono fornire la query SQL e i parametri.
 */
public abstract class AbstractSearchStrategy implements SearchStrategy {
    /**
     * Metodo template che esegue una query SQL con i parametri forniti.
     * @param sql query SQL con placeholder ?
     * @param Parametri valori da sostituire nei placeholder
     * @return lista di DTO degli studenti trovati
     * @throws SQLException se la query fallisce
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

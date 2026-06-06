package strategy;

import model.StudenteDTO;

import java.sql.SQLException;
import java.util.List;

/*
 * Strategia concreta: ricerca uno studente per matricola.
 * La query effettua un JOIN con DeveSeguire/Corso per il piano di studi e con la tabella Esito per i voti verbalizzati,
 * SQLite aggrega le righe multiple in una stringa usando GROUP_CONCAT(DISTINCT ...)
 */
public class SearchByMatricola extends AbstractSearchStrategy {
    @Override
    public List<StudenteDTO> cerca(String Matricola) throws SQLException {
        String sql =
                "SELECT S.Nome, S.Cognome, S.Matricola, S.DataNascita, S.Residenza, S.TassePagate, " +
                        "GROUP_CONCAT(DISTINCT D.NomeCorso) AS PianoStudi, " +
                        "GROUP_CONCAT(DISTINCT E.NomeCorso || ': ' || E.Voto) AS VotiVerbalizzati " +
                        "FROM Studente S " +
                        "LEFT JOIN DeveSeguire D ON S.Matricola = D.MatricolaStudente " +
                        "LEFT JOIN Esito E ON S.Matricola = E.Matricola AND E.Stato = 'Verbalizzato' " +
                        "WHERE S.Matricola = ? " +
                        "GROUP BY S.Matricola, S.Nome, S.Cognome, S.DataNascita, S.Residenza, S.TassePagate";

        return eseguiRicerca(sql, Matricola);
    }
}

package strategy;

import model.StudenteDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Strategia concreta: ricerca uno o più studenti per nome e cognome.
 * L'input atteso è "Nome Cognome" separati da spazio. La ricerca è case-sensitive: prestare attenzione alle iniziali maiuscole.
 * La query effettua un JOIN con DeveSeguire/Corso ed Esito per restituire il piano di studi e i voti verbalizzati di ogni studente trovato.
 * SQLite aggrega le righe multiple in una stringa usando GROUP_CONCAT(DISTINCT ...)
 */
public class SearchByName extends AbstractSearchStrategy {
    @Override
    public List<StudenteDTO> cerca(String input) throws SQLException {
        // .split("\\s+", 2): usa lo spazio come separatore, "+" gestisce spazi multipli
        String[] parti = input.trim().split("\\s+", 2);

        if (parti.length < 2) {
            throw new IllegalArgumentException("Inserire Nome e Cognome separati da spazio.");
        }

        String sql =
                "SELECT S.Nome, S.Cognome, S.Matricola, S.DataNascita, S.Residenza, S.TassePagate, " +
                        "GROUP_CONCAT(DISTINCT D.NomeCorso) AS PianoStudi, " +
                        "GROUP_CONCAT(DISTINCT E.NomeCorso || ': ' || E.Voto) AS VotiVerbalizzati " +
                        "FROM Studente S " +
                        "LEFT JOIN DeveSeguire D ON S.Matricola = D.MatricolaStudente " +
                        "LEFT JOIN Esito E ON S.Matricola = E.Matricola AND E.Stato = 'Verbalizzato' " +
                        "WHERE S.Nome = ? AND S.Cognome = ? " +
                        "GROUP BY S.Matricola, S.Nome, S.Cognome, S.DataNascita, S.Residenza, S.TassePagate";

        return eseguiRicerca(sql, parti[0], parti[1]);
    }
}

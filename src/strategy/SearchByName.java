package strategy;

import model.StudenteDTO;

import java.sql.SQLException;
import java.util.List;

public class SearchByName extends AbstractSearchStrategy {

    // Al momento la funzione di ricerca per Nome e Congome è case sensitive,
    // bisogna prestare attenzione quando si inserisce il nome in input
    @Override
    public List<StudenteDTO> cerca(String input) throws SQLException{
        // .split("\\s+", 2) indica di usare come separatore il carattere spazio, "+" indica di considerare anche piu' spazi
        String[] parti = input.trim().split("\\s+", 2);

        if(parti.length < 2){
            throw new IllegalArgumentException("Inserire Nome e Cognome separati da spazio.");
        }

        String sql = "SELECT Nome, Cognome, Matricola FROM Studente WHERE Nome = ? AND Cognome = ?";
        return eseguiRicerca(sql, parti[0], parti[1]);
    }
}

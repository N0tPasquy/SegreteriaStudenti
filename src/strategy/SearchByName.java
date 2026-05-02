package strategy;

import java.sql.SQLException;

public class SearchByName extends SearchByMatricola {

    // Al momento la funzione di ricerca per Nome e Congome è case sensitive, bisogna prestare attenzione quando si inserisce il nome in input
    @Override
    public String cerca(String nomeCognome) throws SQLException {
        // Sepriamo nome e cognome (l' input deve essere del tipo "Mario Rossi")
        String[] parti = nomeCognome.split(" ", 2);
        if (parti.length < 2){
            return "Inserire sia nome che cognome separati da sapzio.";
        }

        String sql = "SELECT * FROM Studente WHERE Nome = ? AND Cognome = ?";
        return eseguiRicerca(sql, parti[0], parti[1]);
    }
}

package strategy;

import model.StudenteDTO;

import java.sql.SQLException;
import java.util.List;

public class SearchByMatricola extends AbstractSearchStrategy {
    @Override
    public List<StudenteDTO> cerca(String Matricola) throws SQLException{
        String sql = "SELECT Nome, Cognome, Matricola FROM Studente WHERE Matricola = ?";
        return eseguiRicerca(sql, Matricola);
    }
}

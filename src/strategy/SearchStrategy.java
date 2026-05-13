package strategy;

import model.StudenteDTO;
import java.sql.SQLException;
import java.util.List;

public interface SearchStrategy{
    // Ritorna una lista di DTO, in questo caso un lista di studenti
    List<StudenteDTO> cerca (String input) throws SQLException;
}

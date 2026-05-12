package strategy;

import java.sql.SQLException;

public interface SearchStrategy{
    // Ritorna String per stampare a video, ma in futuro potresti fargli ritornare un DTO
    String cerca (String input) throws SQLException;
}

package strategy;

import database.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface SearchStrategy{
    // Ritorna void per stampare a video, ma in futuro potresti fargli ritornare un DTO
    void cerca (String input);
}

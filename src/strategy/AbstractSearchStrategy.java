package strategy;

import database.DatabaseManager;
import model.StudenteDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSearchStrategy implements SearchStrategy {
    // String... Parametri permette al metodo di accettare N dati in input a patto che siano sempre stringhe
    protected List<StudenteDTO> eseguiRicerca(String sql, String... Parametri) throws  SQLException{
        List<StudenteDTO> Risultati = new ArrayList<>();
        Connection conn = DatabaseManager.getInstance().getConnection();

        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            for(int i = 0; i < Parametri.length; i++){
                stmt.setString(i+1, Parametri[i]);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Risultati.add(new StudenteDTO(rs.getString("Nome"), rs.getString("Cognome"), rs.getString("Matricola")));
            }
        } finally {
            conn.close();
        }

        return Risultati;
    }
}

package auth;

import dao.AuthDAO;
import model.CredenzialiDTO;

import java.sql.SQLException;

/**
 * Handler che verifica l'esistenza dell'utente nel database.
 */
public class UserExistsHandler extends LoginHandler {
    private final AuthDAO authDAO;

    public UserExistsHandler(AuthDAO authDAO){
        this.authDAO = authDAO;
    }

    @Override
    public CredenzialiDTO handle(String Username, String PasswordInput, CredenzialiDTO Utente) throws SQLException {
        CredenzialiDTO UtenteTrovato = authDAO.trovaUtente(Username);

        if(UtenteTrovato == null){
            System.out.println("Erorre: Utente non trovato nel DB.");
            return null;
        }

        if(nextHandler != null){
            return nextHandler.handle(Username, PasswordInput, UtenteTrovato);
        }

        return UtenteTrovato;
    }
}

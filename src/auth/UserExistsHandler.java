package auth;

import dao.AuthDAO;
import model.CredenzialiDTO;

import java.sql.SQLException;

public class UserExistsHandler extends LoginHandler {
    private AuthDAO authDAO;
    private CredenzialiDTO utenteTrovato; // Salviamo l'utente per passarlo allo step successivo

    public UserExistsHandler(AuthDAO authDAO){
        this.authDAO = authDAO;
    }

    @Override
    public boolean handle(String Username, String PasswordInput) throws SQLException {
        utenteTrovato = authDAO.trovaUtente(Username);

        if(utenteTrovato == null){
            System.out.println("Errore: Utente non trovato nel db.");
            return false; // Interrompe la catena
        }

        // Passiamo al prossimo controllo
        return super.handle(Username, PasswordInput);
    }

    public CredenzialiDTO getUtenteTrovato(){
        return utenteTrovato;
    }
}

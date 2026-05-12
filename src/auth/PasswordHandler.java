package auth;

import model.CredenzialiDTO;

import java.sql.SQLException;

public class PasswordHandler extends LoginHandler{
    @Override
    public CredenzialiDTO handle(String Username, String PasswordInput, CredenzialiDTO Utente) throws SQLException{
        if(Utente == null){
            return null;
        }

        if(!Utente.getPasswordDB().equals(PasswordInput)){
            System.out.println("Errore: Password errata.");
            return null;
        }

        System.out.println("Login effettuato con successo come: " + Utente.getRuolo());

        if(nextHandler != null){
            return nextHandler.handle(Username, PasswordInput, Utente);
        }

        return Utente;
    }
}

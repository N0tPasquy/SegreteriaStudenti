package auth;

import java.sql.SQLException;
import model.CredenzialiDTO;

public abstract class LoginHandler {
    protected LoginHandler nextHandler;

    // Metodo per concatenare gli handler
    public LoginHandler setNext(LoginHandler nextHandler){
        this.nextHandler = nextHandler;
        return nextHandler; // Ritorna il next per permettere il concatenamento
    }

    // Ogni Handler riceve un utente gia' validato dallo step precedente
    public abstract CredenzialiDTO handle(String Username, String PasswordInput, CredenzialiDTO utente) throws SQLException;
}

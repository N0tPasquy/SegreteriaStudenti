package auth;

import java.sql.SQLException;

public class LoginHandler {
    private LoginHandler nextHandler;

    // Metodo per concatenare gli handler
    public LoginHandler setNext(LoginHandler nextHandler){
        this.nextHandler = nextHandler;
        return nextHandler; // Ritorna il next per permettere il concatenamento
    }

    // Il metodo che ogni handler implementera'
    public boolean handle(String Username, String PasswordInput) throws SQLException {
        if(nextHandler != null){
            return nextHandler.handle(Username, PasswordInput);
        }
        return true;    // Se arriviamo alla fine della catena senza errori, il login ha successo
    }
}

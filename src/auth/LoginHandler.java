package auth;

import java.sql.SQLException;
import model.CredenzialiDTO;

/**
 * Classe astratta per la gestione della catena di responsabilità del login.
 */
public abstract class LoginHandler {
    protected LoginHandler nextHandler;

    /**
     * Imposta il successivo handler nella catena.
     * @param nextHandler handler successivo
     * @return l'handler appena impostato (per concatenamento)
     */
    public LoginHandler setNext(LoginHandler nextHandler){
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    /**
     * Gestisce la richiesta di login.
     * @param Username username inserito
     * @param PasswordInput password in chiaro inserita
     * @param utente utente già parzialmente validato (può essere null)
     * @return CredenzialiDTO se autenticato, altrimenti null
     * @throws SQLException se errore DB
     */
    public abstract CredenzialiDTO handle(String Username, String PasswordInput, CredenzialiDTO utente) throws SQLException;
}

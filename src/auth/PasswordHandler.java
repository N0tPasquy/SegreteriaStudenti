package auth;

import model.CredenzialiDTO;

public class PasswordHandler extends LoginHandler{
    private UserExistsHandler prevHandler; // Ci serve per recuperare l'utente trovato nello step prima

    public PasswordHandler(UserExistsHandler prevHandler){
        this.prevHandler = prevHandler;
    }

    @Override
    public boolean handle(String username, String passwordInput) {
        CredenzialiDTO utente = prevHandler.getUtenteTrovato();

        // Confronto base (per l'esame si potrebbe fare con l'hashing, ma partiamo semplici)
        if (!utente.getPasswordDB().equals(passwordInput)) {
            System.out.println("Errore: Password errata.");
            return false; // Interrompe la catena
        }

        System.out.println("Login effettuato con successo come: " + utente.getRuolo());
        // Se volessimo, qui c'è lo spazio per il "RoleHandler" per reindirizzare la GUI
        // Qui andra il codice per reindirizzare alla GUI corretta in base al ruolo

        return super.handle(username, passwordInput);
    }
}

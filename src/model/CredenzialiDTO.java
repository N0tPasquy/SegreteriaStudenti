package model;

/**
 * Data Transfer Object per le credenziali di login.
 */
public class CredenzialiDTO {
    private String Username;
    private String PasswordDB;
    private String Ruolo;   // "SEGRETERIA", "DOCENTE", "STUDENTE"

    /**
     * Costruttore.
     * @param Username username (matricola/CF/ID)
     * @param PasswordDB password in chiaro nel DB
     * @param Ruolo ruolo dell'utente
     */
    public CredenzialiDTO(String Username, String PasswordDB, String Ruolo) {
        this.Username =  Username;
        this.PasswordDB = PasswordDB;
        this.Ruolo = Ruolo;
    }

    public String getUsername(){ return Username; }
    public String getPasswordDB(){ return PasswordDB; }
    public String getRuolo(){ return Ruolo; }
}

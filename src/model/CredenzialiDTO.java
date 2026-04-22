package model;

//Data Transfer Object
public class CredenzialiDTO {
    private String Username;
    private String PasswordDB;
    private String Ruolo;   // "SEGRETERIA", "DOCENTE", "STUDENTE"

    public CredenzialiDTO(String Username, String PasswordDB, String Ruolo) {
        this.Username =  Username;
        this.PasswordDB = PasswordDB;
        this.Ruolo = Ruolo;
    }

    public String getUsername(){ return Username; }
    public String getPasswordDB(){ return PasswordDB; }
    public String getRuolo(){ return Ruolo; }
}

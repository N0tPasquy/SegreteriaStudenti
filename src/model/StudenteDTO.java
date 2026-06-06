package model;

/*
 * Data Transfer Object per il trasferimento delle informazioni complete di uno studente.
 * Contiene tutti i campi dello studente: dati anagrafici, piano di studi, voti verbalizzati e stato delle tasse.
 */
public class StudenteDTO {
    private final String Nome;
    private final String Cognome;
    private final String Matricola;
    private final String DataNascita;
    private final String Residenza;
    private final boolean TassePagate;
    /* Elenco dei corsi del piano di studi. */
    private final String PianoStudi;
    /* Voti verbalizzati nel formato "Corso:Voto" separati da virgola. */
    private final String VotiVerbalizzati;


    public StudenteDTO(String nome, String cognome, String matricola,
                       String dataNascita, String residenza, boolean tassePagate,
                       String pianoStudi, String votiVerbalizzati) {
        Nome             = nome;
        Cognome          = cognome;
        Matricola        = matricola;
        DataNascita      = dataNascita;
        Residenza        = residenza;
        TassePagate      = tassePagate;
        PianoStudi       = (pianoStudi != null) ? pianoStudi : "Nessun corso";
        VotiVerbalizzati = (votiVerbalizzati != null) ? votiVerbalizzati : "Nessun voto verbalizzato";
    }

    public String getNome()             { return Nome; }
    public String getCognome()          { return Cognome; }
    public String getMatricola()        { return Matricola; }
    public String getDataNascita()      { return DataNascita; }
    public String getResidenza()        { return Residenza; }
    public boolean isTassePagate()      { return TassePagate; }
    public String getPianoStudi()       { return PianoStudi; }
    public String getVotiVerbalizzati() { return VotiVerbalizzati; }
}

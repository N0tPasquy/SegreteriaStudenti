package model;

// Uso il pattern builder per simulare una sessione dopo il login
public class UtenteSessione {
    // Attributi che non cambieranno durante la sessione
    private final String IDUtente; // IDUtente varia a seconda di chi accede, Matricola per studenti, CF per i docenti e l'ID per la segreteria
    private final String Nome;
    private final String Cognome;
    private final String Ruolo;

    private UtenteSessione(Builder builder){
        this.IDUtente = builder.IDUtente;
        this.Nome = builder.Nome;
        this.Cognome = builder.Cognome;
        this.Ruolo = builder.Ruolo;
    }

    // Metodi get utili per la GUI
    public String getIDUtente() { return IDUtente; }
    public String getNome() { return Nome; }
    public String getCognome() { return Cognome; }
    public String getRuolo() { return Ruolo; }

    // Metodo che ritorna i dettagli della sessione
    public String getDettagliSessione(){
        return  "Sessione Attiva -> [" + Ruolo + "] " + Nome + " " + Cognome + " (ID: " + IDUtente + ")";
    }

    /* Il builder si trova all'interno della classe che costruisce in modod a avere un incapsulamento totale
     * Nessuna classe puo' fare new UtenteSessione()...
     * L'unico autorizzato e' proprio il Builder interno.
     */
    public static class Builder{
        private final String IDUtente;
        private final String Ruolo;
        private String Nome = "Sconosciuto";
        private String Cognome = "Sconosciuto";

        public Builder(String IDUtente, String Ruolo){
            this.IDUtente = IDUtente;
            this.Ruolo = Ruolo;
        }

        public Builder conNome(String Nome){
            this.Nome = Nome;
            return this;
        }

        public Builder conCognome(String Cognome){
            this.Cognome = Cognome;
            return this;
        }

        public UtenteSessione build(){
            return new UtenteSessione(this);
        }
    }
}

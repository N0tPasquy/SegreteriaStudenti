package state;

import eccezioni.AzioneNonPermessaException;

public class Esito {
    private int ID;
    private int Voto;
    private String Matricola;
    private StatoEsito statoCorrente;

    public Esito(int ID, int Voto, String Matricola){
        this.ID = ID;
        this.Voto = Voto;
        this.Matricola = Matricola;
        // Di base quando si crea un nuovo esito, questo parte da "In attesa"
        this.statoCorrente = new StatoInAttesa();
    }

    // Metodo per cambiare stato dinamicamente
    public void setStato(StatoEsito nuovoStato){
        this.statoCorrente = nuovoStato;
    }

    // Metodi che delegano il lavoro alla classe dello stato corrente
    public void accetta() throws AzioneNonPermessaException{
        statoCorrente.accetta(this);
    }

    public void rifiuta() throws AzioneNonPermessaException{
        statoCorrente.rifiuta(this);
    }

    public void verbalizza() throws AzioneNonPermessaException{
        statoCorrente.verbalizza(this);
    }

    public StatoEsito getStatoCorrente(){ return statoCorrente; }
    public String getNomeStato() { return statoCorrente.getNomeStato(); }
    public int getVoto() { return Voto; }
    public String getMatricola() { return Matricola; }
}

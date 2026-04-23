package state;

public class StatoRifiutato implements StatoEsito {
    @Override
    public void accetta(Esito esito){
        System.out.println("Avviso: Non puoi accettare un voto che hai gia' rifiutato in precedenza.");
    }

    /*  TO-DO!
        Per quando si implementa la parte GIU implementare una seconda verifica per il rifiuto
     */
    @Override
    public void rifiuta(Esito esito){
        System.out.println("Voto rifiutato.");
    }

    @Override
    public void verbalizza(Esito esito){
        System.out.println("Errore: Non puoi verbalizzare un voto rifiutato.");
    }

    @Override
    public String getNomeStato() {
        return "Rifiutato";
    }
}

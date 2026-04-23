package state;

public class StatoVerbalizzato implements StatoEsito {
    @Override
    public void accetta(Esito esito){
        System.out.println("Errore: Il voto e' gia' stato verbalizzato e non puo' essere piu' modificato.");
    }

    @Override
    public void rifiuta(Esito esito){
        System.out.println("Errore: Impossibile rifiutare un voto gia' presente nel libretto elettronico.");
    }

    @Override
    public void verbalizza(Esito esito){
        System.out.println("Avviso: Questo voto risulta gia' verbalizzato ufficialmente,");
    }

    @Override
    public String getNomeStato(){
        return "Verbalizzato";
    }
}

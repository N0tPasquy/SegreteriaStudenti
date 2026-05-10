package state;

public class StatoVerbalizzato implements StatoEsito {
    @Override
    public void accetta(Esito esito){
        throw new IllegalStateException("Operazione negata: Il voto e' gia' stato verbalizzato e non puo' essere piu' modificato.");
    }

    @Override
    public void rifiuta(Esito esito){
        throw new IllegalStateException("Operazione negata: Impossibile rifiutare un voto gia' presente nel libretto elettronico.");
    }

    @Override
    public void verbalizza(Esito esito){
        throw new IllegalStateException("Operazione negata: Questo voto risulta gia' verbalizzato ufficialmente,");
    }

    @Override
    public String getNomeStato(){
        return "Verbalizzato";
    }
}

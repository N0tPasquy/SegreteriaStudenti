package state;

public class StatoAccettato implements StatoEsito {
    @Override
    public void accetta(Esito esito){
        throw new IllegalStateException("Operazione negata: Il voto e' gia' stato accettato in precedenza.");
    }

    @Override
    public void rifiuta(Esito esito){
        throw new IllegalStateException("Operazione negata: Non puoi piu' rifiutare il voto che hai gia' accettato.");
    }

    @Override
    public void verbalizza(Esito esito){
        System.out.println("Voto verbalizzato correttamente in segreteria");
        esito.setStato(new StatoVerbalizzato());
    }

    @Override
    public String getNomeStato(){
        return "Accettato";
    }
}

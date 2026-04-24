package state;

import eccezioni.AzioneNonPermessaException;

public class StatoVerbalizzato implements StatoEsito {
    @Override
    public void accetta(Esito esito) throws AzioneNonPermessaException{
        throw new AzioneNonPermessaException("Operazione negata: Il voto e' gia' stato verbalizzato e non puo' essere piu' modificato.");
    }

    @Override
    public void rifiuta(Esito esito)throws AzioneNonPermessaException{
        throw new AzioneNonPermessaException("Operazione negata: Impossibile rifiutare un voto gia' presente nel libretto elettronico.");
    }

    @Override
    public void verbalizza(Esito esito) throws AzioneNonPermessaException{
        throw new AzioneNonPermessaException("Operazione negata: Questo voto risulta gia' verbalizzato ufficialmente,");
    }

    @Override
    public String getNomeStato(){
        return "Verbalizzato";
    }
}

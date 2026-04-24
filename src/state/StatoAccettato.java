package state;

import eccezioni.AzioneNonPermessaException;

public class StatoAccettato implements StatoEsito {
    @Override
    public void accetta(Esito esito)throws AzioneNonPermessaException{
        throw new AzioneNonPermessaException("Operazione negata: Il voto e' gia' stato accettato in precedenza.");
    }

    @Override
    public void rifiuta(Esito esito) throws AzioneNonPermessaException{
        throw new AzioneNonPermessaException("Operazione negata: Non puoi piu' rifiutare il voto che hai gia' accettato.");
    }

    /*  Quasi sicuramente questa parte e' da cambiare,
        Lo studente accetta e deve essere la segreteria a verbalizzarlo
     */
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

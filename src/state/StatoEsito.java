package state;

import eccezioni.AzioneNonPermessaException;

public interface StatoEsito {
    void accetta(Esito esito) throws AzioneNonPermessaException;
    void rifiuta(Esito esito) throws AzioneNonPermessaException;
    void verbalizza(Esito esito) throws AzioneNonPermessaException;
    String getNomeStato();
}

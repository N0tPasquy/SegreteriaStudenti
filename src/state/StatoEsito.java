package state;

public interface StatoEsito {
    void accetta(Esito esito);
    void rifiuta(Esito esito);
    void verbalizza(Esito esito);
    String getNomeStato();
}

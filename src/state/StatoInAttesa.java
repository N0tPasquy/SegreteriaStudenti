package state;

public class StatoInAttesa implements StatoEsito {
    @Override
    public void accetta(Esito esito){
        System.out.println("Voto di " + esito.getVoto() + " accettato dallo studente.");
        esito.setStato(new StatoAccettato()); // Transizione di stato
    }

    @Override
    public void rifiuta(Esito esito){
        System.out.println("Voto di " + esito.getVoto() + "rifiutato dallo studente.");
        esito.setStato(new StatoRifiutato()); // Transizione di stato
    }

    @Override
    public void verbalizza(Esito esito){
        System.out.println("Errore: Impossibile verbalizzare. Lo studente deve prima accettare il voto.");
    }

    @Override
    public String getNomeStato(){
        return "In Attesa";
    }
}

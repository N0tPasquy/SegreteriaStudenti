package eccezioni;

// Estendo la classe Exception in modo da obbligare a chi la richiama di usare un blocco try-catch
public class AzioneNonPermessaException extends Exception {
    public AzioneNonPermessaException(String messaggio){
        super(messaggio);
    }
}

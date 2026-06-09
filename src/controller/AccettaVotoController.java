package controller;

import facade.StudenteFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;

/**
 * Controller per la finestra modale di accettazione/rifiuto voto.
 */
public class AccettaVotoController {
    @FXML private TextField NomeCorso;
    @FXML private  Label ErrorArea;

    private StudenteFacade studenteFacade;
    private String MatricolaLoggata;

    @FXML
    public void initialize(){
        studenteFacade = new StudenteFacade();
        ErrorArea.setText("");
    }

    /**
     * Riceve la matricola dello studente loggato.
     * @param Matricola matricola
     */
    public void initData(String Matricola){
        MatricolaLoggata = Matricola;
    }

    @FXML
    public void Accetta(ActionEvent event){
        Scelta(true, event);
    }

    @FXML
    public void Rifiuta(ActionEvent event){
        Scelta(false, event);
    }

    /**
     * Metodo unico che gestisce sia accetta che rifiuta del voto
     * @param scelta scelta ricavata dai pulsanti
     * @param event evento del pulsante
     */
    private void Scelta(boolean scelta, ActionEvent event){
        String Corso = NomeCorso.getText();

        if(Corso == null || Corso.trim().isEmpty()){
            mostraErrore("Inserisci il nome del corso!");
            NomeCorso.clear();
            return;
        }

        try{
            // Richiama il facade
            String Risultato = studenteFacade.gestisciVoto(MatricolaLoggata, Corso, scelta);

            // Se tutto va bene mostriamo la scritta verde di successo
            mostraSuccesso(Risultato);
            NomeCorso.clear();
        }catch (SQLException e){
            mostraErrore("Errore SQL:\n" + e.getMessage());
        }
    }

    // Metodi di supporto
    private void mostraErrore(String Messaggio){
        ErrorArea.setStyle("-fx-text-fill: red;");
        ErrorArea.setText(Messaggio);
    }

    private void mostraSuccesso(String Messaggio){
        ErrorArea.setStyle("-fx-text-fill: green;");
        ErrorArea.setText(Messaggio);
    }
}

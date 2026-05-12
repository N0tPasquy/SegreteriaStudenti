package controller;

import facade.DocenteFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;

public class CreaAppelloController {
    @FXML private TextField NomeCorso;
    @FXML private DatePicker DataAppello;
    @FXML Label ErrorCode;

    private DocenteFacade docenteFacade;

    @FXML
    public void initialize(){
        docenteFacade = new DocenteFacade();
        ErrorCode.setText("");
    }

    @FXML
    public void creaAppello(ActionEvent event){
        String Corso = NomeCorso.getText();
        LocalDate Data = DataAppello.getValue();

        // Controllo che tutti i campi siano compilati
        if(Corso.trim().isEmpty() || Data == null){
            mostraErrore("Compila tutti i campi");
            return;
        }

        // Elimino l'errore se l'utente ha inserito tutto
        ErrorCode.setText("");

        try {
            // Richiamo il facade che inserisce l'appello nel DB
            docenteFacade.creaAppello(Corso, Data.toString());
            mostraSuccesso("Appello aggiunto correttamente\nin data " + Data + ".");
        } catch (SQLException e){
            mostraErrore("Errore Creazione:\n" + e.getMessage());
        }
    }

    // Metodi per far comparire i messaggi di errore e successo colorati
    private void mostraErrore(String Messaggio){
        ErrorCode.setStyle("-fx-text-fill: red;");
        ErrorCode.setText(Messaggio);
    }

    private void mostraSuccesso(String Messaggio){
        ErrorCode.setStyle("-fx-text-fill: green;");
        ErrorCode.setText(Messaggio);
    }
}

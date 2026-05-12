package controller;

import facade.SegreteriaFacade;

import java.sql.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDate;

// Lo studente avra' una password di default che al primo login dovrà cambiare
public class InserisciStudenteController {
    @FXML private TextField NewMatricola;
    @FXML private TextField NewNome;
    @FXML private TextField NewCognome;
    @FXML private TextField NewResidenza;
    @FXML private DatePicker NewDataNascita;
    @FXML private Label ErrorArea;

    private SegreteriaFacade segreteriaFacade;

    @FXML
    public void initialize(){
        segreteriaFacade = new SegreteriaFacade();

        ErrorArea.setText("");
    }

    @FXML
    public void iscriviStudente(ActionEvent event){
        String Matricola = NewMatricola.getText();
        String Nome = NewNome.getText();
        String Cognome = NewCognome.getText();
        String Residenza = NewResidenza.getText();

        // Traduco la data di nascita da javaFX in data java
        LocalDate DataNascita = NewDataNascita.getValue();

        // Controllo che i campi non siano vuoti
        if(Matricola == null|| Nome == null || Cognome == null|| Residenza == null || DataNascita == null){
            mostraErrore("Compila tutti i campi prima di procedere!");
            return;
        }

        // Dopo il controllo traduco la data java in data sql
        Date dataNascita = Date.valueOf(DataNascita);

        try {
            // Richiamo il facade per l'inserimento nel DB, con la password di default
            segreteriaFacade.iscriviStudente(Matricola, "Cambiami123", Nome, Cognome, Residenza, dataNascita);

            // Visualizzo il messaggio di successo
            mostraSuccesso("Studente iscritto correttamente!");
        } catch (Exception e){
            e.printStackTrace();
            mostraErrore("Impossibile iscrivere lo studente. Verifica che la matricola non esista già.");
        }
    }

    // Metodi di supporto per non ripetere sempre le stesse 2 righe
    private void mostraErrore(String Messaggio){
        ErrorArea.setStyle("-fx-text-fill: red;");
        ErrorArea.setText(Messaggio);
    }

    private void mostraSuccesso(String Messaggio){
        ErrorArea.setStyle("-fx-text-fill: green;");
        ErrorArea.setText(Messaggio);
    }
}

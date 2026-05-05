package controller;

import facade.DocenteFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
            ErrorCode.setStyle("-fx-text-fill: red;");
            ErrorCode.setText("Compila tutti i campi");
            return;
        }

        // Elimino l'errore se l'utente ha inserito tutto
        ErrorCode.setText("");

        try {
            // Richiamo il facade che inserisce l'appello nel DB
            docenteFacade.creaAppello(Corso, Data.toString());
            ErrorCode.setStyle("-fx-text-fill: green;");
            ErrorCode.setText("Appello aggiunto correttamente\nin data " + Data + ".");
        } catch (SQLException e){
            ErrorCode.setStyle("-fx-text-fill: red;");
            ErrorCode.setText("Errore Creazione:\n" + e.getMessage());
        }
    }
}

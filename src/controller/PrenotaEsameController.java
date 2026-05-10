package controller;

import facade.StudenteFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;

public class PrenotaEsameController {
    @FXML private TextField NomeCorso;
    @FXML private DatePicker DataEsame;
    @FXML private Label ErrorArea;

    private StudenteFacade studenteFacade;
    private String MatricolaLoggata;

    @FXML
    public void initialize(){
        studenteFacade = new StudenteFacade();
        ErrorArea.setText("");
    }

    public void initData(String Matricola) {
        MatricolaLoggata = Matricola;
    }

    @FXML
    public void Prenotati(ActionEvent event){
        String Corso = NomeCorso.getText();
        LocalDate Data = DataEsame.getValue();

        // Controllo che i campi siano okay
        if(Corso == null || Data == null){
            mostraErrore("Compila tutti i campi");
            return;
        }

        try {
            // Chiamo il facade per gestire la prenotazione
            studenteFacade.prenotaAppello(MatricolaLoggata, Corso, Data.toString());
            mostraSuccesso("Ti sei prenotato correttamente all'appello di\n" + Corso + " per il giorno " + Data + ".");
        } catch (SQLException e){
            mostraErrore("Errore nel db:\n" + e.getMessage());
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

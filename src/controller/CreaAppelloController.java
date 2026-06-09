package controller;

import facade.DocenteFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Controller per la finestra modale di creazione di un nuovo appello.
 */
public class CreaAppelloController {
    @FXML private TextField NomeCorso;
    @FXML private DatePicker DataAppello;
    @FXML Label ErrorCode;

    private DocenteFacade docenteFacade;
    private String CFLoggato;

    @FXML
    public void initialize(){
        docenteFacade = new DocenteFacade();
        ErrorCode.setText("");
    }

    /**
     * Riceve il CF del docente loggato.
     * @param CF codice fiscale
     */
    public void initData(String CF){
        this.CFLoggato = CF;
    }

    @FXML
    public void creaAppello(ActionEvent event){
        String Corso = NomeCorso.getText();
        LocalDate Data = DataAppello.getValue();

        if(Corso.trim().isEmpty() || Data == null){
            mostraErrore("Compila tutti i campi");
            return;
        }

        // Elimino l'errore se l'utente ha inserito tutto
        ErrorCode.setText("");

        try {
            // Richiamo il facade che inserisce l'appello nel DB
            docenteFacade.creaAppello(CFLoggato, Corso, Data.toString());
            mostraSuccesso("Appello aggiunto correttamente\nin data " + Data + ".");
        } catch (SQLException e){
            mostraErrore("Errore Creazione:\n" + e.getMessage());
        }
    }

    private void mostraErrore(String Messaggio){
        ErrorCode.setStyle("-fx-text-fill: red;");
        ErrorCode.setText(Messaggio);
    }

    private void mostraSuccesso(String Messaggio){
        ErrorCode.setStyle("-fx-text-fill: green;");
        ErrorCode.setText(Messaggio);
    }
}

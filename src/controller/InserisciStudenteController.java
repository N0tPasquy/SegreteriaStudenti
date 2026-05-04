package controller;

import facade.SegreteriaFacade;

import java.sql.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

// Lo studente avra' una password di default che al primo login dovrà cambiare
public class InserisciStudenteController {
    @FXML private TextField NewMatricola;
    @FXML private TextField NewNome;
    @FXML private TextField NewCognome;
    @FXML private TextField NewResidenza;
    @FXML private DatePicker NewDataNascita;

    private SegreteriaFacade segreteriaFacade;

    @FXML
    public void initialize(){
        segreteriaFacade = new SegreteriaFacade();
    }

    @FXML
    public void iscriviStudente(ActionEvent event){
        String Matricola = NewMatricola.getText();
        String Nome = NewNome.getText();
        String Cognome = NewCognome.getText();
        String Residenza = NewResidenza.getText();
        LocalDate DataNascita = NewDataNascita.getValue();
        java.sql.Date dataNascita = java.sql.Date.valueOf(DataNascita);

        // Controllo che nessun campo sia vuoto
        if(Matricola.trim().isEmpty() || Nome.trim().isEmpty() || Cognome.trim().isEmpty() || Residenza.trim().isEmpty()){
            MostraAllert(Alert.AlertType.WARNING, "Attenzione", "Compila tutti i campi prima di procedere!");
            return;
        }

        try {
            // Richiamo il facade per l'inserimento nel DB, con la password di default
            segreteriaFacade.iscriviStudente(Matricola, "Cambiami123", Nome, Cognome, Residenza, dataNascita);

            // Visualizzo il messaggio di successo
            MostraAllert(Alert.AlertType.INFORMATION, "Sucesso", "Studente iscritto correttamente!");

            // Chiudo tutto automaticamente
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e){
            e.printStackTrace();
            MostraAllert(Alert.AlertType.ERROR, "Errore Database", "Impossibile iscrivere lo studente. Verifica che la matricola non esista già.");
        }
    }

    // Metodo che crea un popup con javaFX
    private void MostraAllert(Alert.AlertType tipo, String Titolo, String Messaggio){
        Alert alert = new Alert(tipo);
        alert.setTitle(Titolo);
        alert.setHeaderText(null);
        alert.setContentText(Messaggio);
        alert.showAndWait();
    }
}

package controller;

import facade.SegreteriaFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.lang.invoke.SwitchPoint;
import java.sql.SQLException;

public class CambiaPianoStudiController {
    @FXML private TextField EditMatricola;
    @FXML private TextField EditCorso;
    @FXML private Button AggiungiCorso;
    @FXML private Button EliminaCorso;
    @FXML private Label ErroreCorsoInesistente;

    private SegreteriaFacade segreteriaFacade;

    @FXML
    public void initialize(){
        segreteriaFacade = new SegreteriaFacade();

        // Imposto di default a vuoto la Lable degli errori
        ErroreCorsoInesistente.setText("");
    }

    @FXML
    public void AggiungiCorso(ActionEvent event){
        String Matricola = EditMatricola.getText();
        String Corso = EditCorso.getText();

        // Controllo che i campi non siano vuoti
        if(!validaCampi(Matricola, Corso)){
            return;
        }

        try{
            segreteriaFacade.aggiungiCorso(Matricola, Corso);

            mostraAllert(Alert.AlertType.INFORMATION, "Successo", "Il corso '" + Corso + "' e' stato aggiunto al piano di studi di " + Matricola + ".");
            chiudiFinestra(event);
        } catch (SQLException e){
            mostraAllert(Alert.AlertType.ERROR, "Errore di Inserimento", e.getMessage())    ;
        }
    }

    @FXML
    public void EliminaCorso(ActionEvent event){
        String Matricola = EditMatricola.getText();
        String Corso = EditCorso.getText();

        if(!validaCampi(Matricola, Corso)){
            return;
        }

        try{
            segreteriaFacade.eliminaCorso(Matricola, Corso);

            mostraAllert(Alert.AlertType.INFORMATION, "Successo", "Il corso '" + Corso + "' e' stato rimosso dal piano di studi di " + Matricola + ".");
            chiudiFinestra(event);
        } catch (SQLException e){
            mostraAllert(Alert.AlertType.ERROR, "Errore di Rimozione", e.getMessage());
        }
    }

    // Qui ci sono i metodi di supporto, validazione dei campi, mostra l'errore nella lable nascosta e chiude il pop-up recuperando lo Stage dal bottone premuto

    private boolean validaCampi(String Matricola, String Corso){
        if(Matricola.trim().isEmpty() || Corso.trim().isEmpty()){
            ErroreCorsoInesistente.setStyle("-fx-text-fill: red;");
            ErroreCorsoInesistente.setText("Compila entrambi i campi");
            return false;
        }

        ErroreCorsoInesistente.setText(""); // Pulisce l'errore se va tutto bene
        return true;
    }

    private void mostraAllert(Alert.AlertType Tipo, String Titolo, String Messaggio){
        Alert alert = new Alert(Tipo);
        alert.setTitle(Titolo);
        alert.setHeaderText(null);
        alert.setContentText(Messaggio);
        alert.showAndWait();
    }

    private void chiudiFinestra(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}

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
    @FXML private Label ErrorArea;

    private SegreteriaFacade segreteriaFacade;

    @FXML
    public void initialize(){
        segreteriaFacade = new SegreteriaFacade();

        // Imposto di default a vuoto la Lable degli errori
        ErrorArea.setText("");
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
            mostraSuccesso("Il corso '" + Corso + "'\ne' stato aggiunto al piano di studi di\n" + Matricola + ".");
        } catch (SQLException e){
            mostraErrore("Errore di Inserimento\n" + e.getMessage());
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
            mostraSuccesso("Il corso '" + Corso + "'\ne' stato rimosso dal piano di studi di\n" + Matricola + ".");
        } catch (SQLException e){
            mostraErrore("Errore di rimozione.\n" + e.getMessage());
        }
    }

    // Qui ci sono i metodi di supporto, validazione dei campi, mostra l'errore nella lable nascosta e chiude il pop-up recuperando lo Stage dal bottone premuto
    private boolean validaCampi(String Matricola, String Corso){
        if(Matricola.trim().isEmpty() || Corso.trim().isEmpty()){
            mostraErrore("Compila entrambi i campi");
            return false;
        }

        ErrorArea.setText(""); // Pulisce l'errore se va tutto bene
        return true;
    }

    private void mostraErrore(String Messaggio){
        ErrorArea.setStyle("-fx-text-fill: red;");
        ErrorArea.setText(Messaggio);
    }

    private void mostraSuccesso(String Messaggio){
        ErrorArea.setStyle("-fx-text-fill: green;");
        ErrorArea.setText(Messaggio);
    }
}

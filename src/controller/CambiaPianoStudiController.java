package controller;

import facade.SegreteriaFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;

/**
 * Controller per la finestra modale di modifica del piano di studi (aggiunta/rimozione corsi).
 */
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

    /**
     * Metodo che aggiunge un corso al piano di studi di uno studente
     */
    @FXML
    public void AggiungiCorso(ActionEvent event){
        String Matricola = EditMatricola.getText();
        String Corso = EditCorso.getText();

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

    /**
     * Metodo che elimina un corso dal piano di studi di uno studente
     */
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

    /**
     * Metodo di supporto per controllare i campi inseriti
     * @param Matricola matricola a cio si vuole modificare il piano di studi
     * @param Corso corso da aggiungere o eliminare
     * @return false se non sono compilati entrambi i campi, altrimenti true
     */
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

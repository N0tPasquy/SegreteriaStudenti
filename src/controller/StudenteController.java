package controller;

import facade.StudenteFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class StudenteController {
    @FXML private TextArea AreaRisultati;

    private StudenteFacade studenteFacade;
    private String MatricolaLoggata;

    @FXML
    public void initialize(){
        studenteFacade = new StudenteFacade();
    }

    public void initData(String Matricola){
            this.MatricolaLoggata = Matricola;
    }

    @FXML
    public void vediPianoStudi(ActionEvent event){
        try{
            String piano = studenteFacade.vediPianoStudi(MatricolaLoggata);
            AreaRisultati.setText("-- PIANO DI STUDI --\n\n" + piano);
        } catch (SQLException e){
            AreaRisultati.setText("Errore nella ricerca:\n" + e.getMessage());
        }
    }

    @FXML
    public void vediVotiAttesa(ActionEvent event){
        try{
            String voti = studenteFacade.visualizzaVotiAttesa(MatricolaLoggata);
            AreaRisultati.setText("-- VOTI IN ATTESA DI ESSERE ACCETTATI --\n\n" + voti);
        } catch (SQLException e){
            AreaRisultati.setText("Errore nella ricerca:\n" + e.getMessage());
        }
    }

    @FXML
    public void VediAppelli(ActionEvent event){
        try{
            // Richiamo il metodo nel facade
            String appelli = studenteFacade.visualizzaAppelli(MatricolaLoggata);
            AreaRisultati.setText("-- APPELLI DISPONIBILI --\n\n" + appelli);
        }catch (SQLException e){
            AreaRisultati.setText("Errore durante il caricamento degli appelli:\n" + e.getMessage());
        }
    }

    // Menu a tendina
    @FXML
    public void apriPrenotaEsame(ActionEvent event){
        apriModale("/resources/ModalePrenotaEsame.fxml", "Prenota Appello");
    }

    @FXML
    public void apriAccettaVoto(ActionEvent event){
        apriModale("/resources/ModaleAccettaVoto.fxml", "Gestisci Esiti");
    }

    // Metodo di supporto per aprire i modali passando anche la matricola loggata
    private void apriModale(String Percorso, String Titolo){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Percorso));
            Parent root = loader.load();

            // Passiamo la matricola al modale
            Object controller = loader.getController();
            if (controller instanceof PrenotaEsameController) {
                ((PrenotaEsameController) controller).initData(MatricolaLoggata);
            } else if (controller instanceof AccettaVotoController) {
                ((AccettaVotoController) controller).initData(MatricolaLoggata);
            }

            Stage dialogState = new Stage();
            dialogState.setTitle(Titolo);
            dialogState.setScene(new Scene(root));
            dialogState.initModality(Modality.APPLICATION_MODAL);
            dialogState.setResizable(false);
            dialogState.showAndWait();
        } catch (IOException e){
            AreaRisultati.setText("Errore GUI:\n" + e.getMessage());
        }
    }

}

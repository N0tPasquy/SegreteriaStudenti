package controller;

import facade.DocenteFacade;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class DocenteController {
    @FXML private TextField NomeCorso;
    @FXML private DatePicker DataAppello;
    @FXML private TextArea AreaPrenotati;

    private DocenteFacade docenteFacade;

    @FXML
    public void initialize(){
        docenteFacade = new DocenteFacade();
    }

    @FXML
    public void cercaPrenotati(ActionEvent event){
        String Corso = NomeCorso.getText();
        LocalDate Data = DataAppello.getValue();

        // Controllo che il docente abbia compilato tutto
        if (Corso.trim().isEmpty() || Data == null){
            AreaPrenotati.setText("Completa tutti i campi prima di cercare un appello,");
            return;
        }

        try {
            // Richiamo il facade per ottenere la lista dei prenotati in base all'imput
            String Risultati = docenteFacade.visualizzaPrenotati(Corso, Data.toString());
            AreaPrenotati.setText(Risultati);
        } catch (Exception e) {
            AreaPrenotati.setText("Errore Database, impossibile recuperare le prenotazioni.\n" +  e.getMessage());
        }
    }

    @FXML
    public void apriCreaAppello(ActionEvent event){
        apriModale("/resources/ModaleCreaAppello.fxml", "Crea Nuovo Appello");
    }

    @FXML
    public void apriAssegnaVoto(ActionEvent event){
        apriModale("/resources/ModaleAssegnaVoto.fxml", "Assegna Voto Studente");
    }

    // Metodo apriModale come abbiamo fatto per la segreteria,
    // forse conviene scriverlo 1 volta sola e richiamarlo in entrambi i casi
    private void apriModale(String Percorso, String Titolo){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Percorso));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(Titolo);
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
        } catch (IOException e){
            AreaPrenotati.setText("Errore GUI, impossibile caricare la schermata: " + e.getMessage());
        }
    }
}

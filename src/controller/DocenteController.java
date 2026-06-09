package controller;

import facade.DocenteFacade;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Controller della dashboard del docente.
 */
public class DocenteController {
    @FXML private TextField NomeCorso;
    @FXML private DatePicker DataAppello;
    @FXML private TextArea AreaPrenotati;

    private DocenteFacade docenteFacade;
    private String CFLoggato;

    @FXML
    public void initialize(){
        docenteFacade = new DocenteFacade();
    }

    /**
     * Riceve il CF del docente loggato.
     * @param CF codice fiscale
     */
    public void initData(String CF){
        this.CFLoggato = CF;
    }

    /**
     * Metodo che mostra tutti gli studenti prenotati in base al nome di un esame e alla data in cui si tiene l'appello
     */
    @FXML
    public void cercaPrenotati(ActionEvent event){
        String Corso = NomeCorso.getText();
        LocalDate Data = DataAppello.getValue();

        if (Corso.trim().isEmpty() || Data == null){
            mostraErrore("Completa tutti i campi prima di cercare un appello.");
            return;
        }

        try {
            // Richiamo il facade per ottenere la lista dei prenotati in base all'imput
            String Risultati = docenteFacade.visualizzaPrenotati(CFLoggato, Corso, Data.toString());
            AreaPrenotati.setStyle("-fx-text-fill: black;"); // resetto il colore del testo se prima c'era un errore
            AreaPrenotati.setText(Risultati);
        } catch (Exception e) {
            mostraErrore("Errore Database, impossibile recuperare le prenotazioni.\n" +  e.getMessage());
        }
    }

    @FXML
    public void apriCreaAppello(ActionEvent event){
        apriModale("/resources/ModaleCreaAppello.fxml", "Crea Nuovo Appello");
    }

    @FXML
    public void apriAssegnaVoto(ActionEvent event){
        apriModale("/resources/ModaleInserisciVoto.fxml", "Assegna Voto Studente");
    }

    // Metodo apriModale come abbiamo fatto per la segreteria,
    private void apriModale(String Percorso, String Titolo){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Percorso));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof CreaAppelloController) {
                ((CreaAppelloController) controller).initData(CFLoggato);
            } else if (controller instanceof InserisciVotoController) {
                ((InserisciVotoController) controller).initData(CFLoggato);
            }

            Stage dialogStage = new Stage();
            dialogStage.setTitle(Titolo);
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
        } catch (IOException e){
            mostraErrore("Errore GUI, impossibile caricare la schermata:\n" + e.getMessage());
        }
    }

    // Metodo di support per mostrare gli errori in rosso
    private void mostraErrore(String Messaggio){
        AreaPrenotati.setStyle("-fx-text-fill: red;");
        AreaPrenotati.setText(Messaggio);
    }
}

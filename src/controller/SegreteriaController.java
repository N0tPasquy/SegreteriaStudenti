package controller;

import facade.SegreteriaFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;
import strategy.SearchByMatricola;
import strategy.SearchByName;

import javax.imageio.IIOException;
import java.io.IOException;
import java.sql.SQLException;

public class SegreteriaController {
    @FXML private RadioButton radioMatricola;
    @FXML private RadioButton radioNomeCognome;
    @FXML private TextField campoRicerca;
    @FXML private TextArea areaRisultati;

    private ToggleGroup gruppoRicerca;
    private SegreteriaFacade segreteriaFacade;

    // Metodo chiamato automaticamente da JavaFX all'avvio della schermata
    @FXML
    public void initialize(){
        segreteriaFacade = new SegreteriaFacade();

        // Raggruppo i due pulsanti in modo che solo 1 alla volta possa essere attivo
        gruppoRicerca = new ToggleGroup();
        radioMatricola.setToggleGroup(gruppoRicerca);
        radioNomeCognome.setToggleGroup(gruppoRicerca);

        // Setto la ricerca della matricola come predefinita
        radioMatricola.setSelected(true);
    }

    // Metodo richiamato quando si clicca il pulsante ricerca
    @FXML
    public void cercaStudente(ActionEvent event) throws SQLException {
        String parametro = campoRicerca.getText();

        // Controllo che non sia vuoto l'area di testo
        if(parametro == null || parametro.trim().isEmpty()){
            areaRisultati.setText("Errore: Inserisci un parametro di ricerca valido.");
            return;
        }

        String risultato = "";

        // Richiamo il facade in base al pulsante selezionato dall'utente
        if(radioMatricola.isSelected()){
            risultato = segreteriaFacade.visualizzaStudente(new SearchByMatricola(), parametro);
        } else if (radioNomeCognome.isSelected()) {
            risultato = segreteriaFacade.visualizzaStudente(new SearchByName(), parametro);
        }

        areaRisultati.setText(risultato);
    }

    // Metodo di support che apre i modali in base all'opzione scelta nel menu' a tendina
    @FXML
    private void apriModale(String percorsoFxml, String titolo){
        try{
            // Carico il file FXML del modale
            FXMLLoader loader = new FXMLLoader(getClass().getResource(percorsoFxml));
            Parent root = loader.load();

            // Creo una nuova finestra
            Stage dialogStage = new Stage();
            dialogStage.setTitle(titolo);
            dialogStage.setScene(new Scene(root));

            // Blocco l'interazione con la finestra principale fino a quando non chiudo il modale appena aperto
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            // Impedisco di ridimensionare il modale
            dialogStage.setResizable(false);

            // Mostro il modale e aspetta che viene chiuso
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            areaRisultati.setText("Errore nell'apertura della finestra: " + titolo);
        }
    }

    @FXML
    public void apriInserisciStudente(ActionEvent event){
        // Codice per aprire modale inserisci studente
        apriModale("/resources/ModaleInserisciStudente.fxml", "Inserimento Nuovo Studente");
    }

    @FXML
    public void apriVerbalizzaVoto(ActionEvent event){
        // Codice per aprire modale veralizza voto di un esame
        apriModale("/resources/ModaleVerbalizzaVoto.fxml", "Verbalizzazione Voto");
    }

    @FXML
    public void apriCambiaPianoStudi(ActionEvent event){
        // Codice per cambiare piano di studi di uno studente
        apriModale("/resources/ModaleCambiaPianoStudi.fxml", "Aggiungi Corso al Piano di Studi");
    }
}

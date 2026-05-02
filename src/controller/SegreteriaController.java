package controller;

import facade.SegreteriaFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import strategy.SearchByMatricola;
import strategy.SearchByName;

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

    @FXML
    public void apriInserisciStudente(ActionEvent event){
        // Codice per aprire modale inserisci studente
    }

    @FXML
    public void apriVerbalizzaVoto(ActionEvent event){
        // Codice per aprire modale veralizza voto di un esame
    }

    @FXML
    public void apriCambiaPianoStudi(ActionEvent event){
        // Codice per cambiare piano di studi di uno studente
    }
}

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
import model.StudenteDTO;
import strategy.SearchByMatricola;
import strategy.SearchByName;
import strategy.SearchStrategy;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller della dashboard della segreteria.
 */
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
        radioMatricola.setSelected(true);
    }

    /**
     * Cerco lo studente tenendo conto del radio button, che sceglie la strategia
     * @throws SQLException se la query fallisce
     */
    @FXML
    public void cercaStudente(ActionEvent event) throws SQLException {
        String parametro = campoRicerca.getText();

        if(parametro == null || parametro.trim().isEmpty()){
            areaRisultati.setText("Errore: Inserisci un parametro di ricerca valido.\n  Usa Nome Cognome, con le iniziali in maiuscolo");
            return;
        }

        SearchStrategy strategia;
        if (radioMatricola.isSelected()) {
            strategia = new SearchByMatricola();
        } else {
            strategia = new SearchByName();
        }

        // Chiamo il metodo nel facade che cerca gli studenti
        try {
            List<StudenteDTO> Risultati = segreteriaFacade.visualizzaStudente(strategia, parametro);
            areaRisultati.setText(formattaRisultati(Risultati));
        } catch (IllegalArgumentException e){
            areaRisultati.setText(e.getMessage());
        } catch (SQLException e){
            areaRisultati.setText(e.getMessage());
        }
    }

    /**
     * Metodo di supporto per formattare i risultati della lista StudneteDTO
     * @param risultati Lista di StudenteDTO
     * @return stringa formattata con tutti gli studenti trovati
     */
    private String formattaRisultati(List<StudenteDTO> risultati){
        if(risultati.isEmpty()){
            return "Nessuno studente trovato con questi criteri.";
        }

        StringBuilder sb = new StringBuilder();
        for (StudenteDTO studente : risultati) {
            sb.append("══════════════════════════════════════\n");
            sb.append("  Nome:        ").append(studente.getNome()).append(" ").append(studente.getCognome()).append("\n");
            sb.append("  Matricola:   ").append(studente.getMatricola()).append("\n");
            sb.append("  Data nasc.:  ").append(studente.getDataNascita()).append("\n");
            sb.append("  Residenza:   ").append(studente.getResidenza()).append("\n");
            sb.append("  Tasse:       ").append(studente.isTassePagate() ? "✔ Pagate" : "✘ Non pagate").append("\n");
            sb.append("  Piano studi: ").append(studente.getPianoStudi()).append("\n");
            sb.append("  Voti verb.:  ").append(studente.getVotiVerbalizzati()).append("\n");
        }
        sb.append("══════════════════════════════════════\n");

        return sb.toString();
    }

    // Metodi di support che apre i modali in base all'opzione scelta nel menu' a tendina
    @FXML
    private void apriModale(String percorsoFxml, String titolo){
        try{
            // Carico il file FXML del modale
            FXMLLoader loader = new FXMLLoader(getClass().getResource(percorsoFxml));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(titolo);
            dialogStage.setScene(new Scene(root));

            // Blocco l'interazione con la finestra principale fino a quando non chiudo il modale appena aperto
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            areaRisultati.setText("Errore nell'apertura della finestra: " + titolo);
        }
    }

    @FXML
    public void apriInserisciStudente(ActionEvent event){
        apriModale("/resources/ModaleInserisciStudente.fxml", "Inserimento Nuovo Studente");
    }

    @FXML
    public void apriVerbalizzaVoto(ActionEvent event){
        apriModale("/resources/ModaleVerbalizzaVoto.fxml", "Verbalizzazione Voto");
    }

    @FXML
    public void apriCambiaPianoStudi(ActionEvent event){
        apriModale("/resources/ModaleCambiaPianoStudi.fxml", "Aggiungi Corso al Piano di Studi");
    }
}

package controller;

import facade.SegreteriaFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class VerbalizzaVotoController {
    @FXML private TextField VerbalizzaMatricola;
    @FXML private TextArea AreaVoti;

    private SegreteriaFacade segreteriaFacade;

    @FXML
    public void initialize(){
        segreteriaFacade = new SegreteriaFacade();
    }

    // Funzione collegata al bottone "Cerca Voti" che mostra i risultati nell'area di testo
    @FXML
    public void cercaVotiMatricola(ActionEvent event){
        String Matricola = VerbalizzaMatricola.getText();

        if(Matricola == null || Matricola.trim().isEmpty()){
            mostraAllert(Alert.AlertType.WARNING, "Attenzione", "Inserisci una matricola prima di cercare.");
        }

        // Richiamo il metodo da SegreteriaFacade per mostrare la stringa dei risultati
        try {
            String Risultati = segreteriaFacade.cercaVotiAccettati(Matricola);
            AreaVoti.setText(Risultati);
        } catch (SQLException e){
            mostraAllert(Alert.AlertType.ERROR, "Errore DB", e.getMessage());
        }
    }

    // Funzione collegata al bottone "Verbalizza Tutti"
    @FXML
    public void VerbalizzaVoti(ActionEvent event){
        String Matricola = VerbalizzaMatricola.getText();

        // Evito chiamate al DB se non c'e' nulla da verbalizzare
        String testoArea = AreaVoti.getText();
        if (testoArea.isEmpty() || testoArea.contains("Nessun voto")){
            mostraAllert(Alert.AlertType.WARNING, "Attenzione", "Nessun voto da verbalizzare per " + Matricola + ".");
        }

        // Se ci sono voti procedo con la chiamata al metodo in SegreteriaFacade che li verbalizza
        try {
                segreteriaFacade.verbalizzaTutti(Matricola);
                mostraAllert(Alert.AlertType.INFORMATION, "Successo", "Tutti i voti che erano in attesa sono stati verbalizzati con successo.");

                // Chiudo automaticamente la finestra dopo che ho verbalizzato
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (SQLException e){
            mostraAllert(Alert.AlertType.ERROR, "Errore di verbalizzazione", e.getMessage());
        }
    }

    // Metodo che crea i pop-up
    private void mostraAllert(Alert.AlertType Tipo, String Titolo, String Messaggio){
        Alert alert = new Alert(Tipo);
        alert.setTitle(Titolo);
        alert.setHeaderText(null);
        alert.setContentText(Messaggio);
        alert.showAndWait();
    }
}

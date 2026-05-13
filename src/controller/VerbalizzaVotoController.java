package controller;

import facade.SegreteriaFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
            mostraErrore("Inserisci una matricola prima di cercare.");
            return;
        }

        // Richiamo il metodo da SegreteriaFacade per mostrare la stringa dei risultati
        try {
            String Risultati = segreteriaFacade.cercaVotiAccettati(Matricola);
            AreaVoti.setText(Risultati);
        } catch (SQLException e){
            mostraErrore("Errore DB:\n" + e.getMessage());
        }
    }

    // Funzione collegata al bottone "Verbalizza Tutti"
    @FXML
    public void VerbalizzaVoti(ActionEvent event){
        String Matricola = VerbalizzaMatricola.getText();

        // Evito chiamate al DB se non c'e' nulla da verbalizzare
        String testoArea = AreaVoti.getText();
        if (testoArea.isEmpty() || testoArea.contains("Nessun voto")){
            mostraErrore("Nessun voto da verbalizzare per " + Matricola + ".");
            return;
        }

        // Se ci sono voti procedo con la chiamata al metodo in SegreteriaFacade che li verbalizza
        try {
                segreteriaFacade.verbalizzaTutti(Matricola);
                mostraSuccesso("Tutti i voti che erano in attesa sono stati verbalizzati con successo.");
        } catch (SQLException e){
            mostraErrore("Errore di verbalizzazione:\n" + e.getMessage());
        }
    }

    // Metodi di supporto
    private void mostraErrore(String Messaggio){
        AreaVoti.setStyle("-fx-text-fill: red;");
        AreaVoti.setText(Messaggio);
    }

    private void mostraSuccesso(String Messaggio){
        AreaVoti.setStyle("-fx-text-fill: green;");
        AreaVoti.setText(Messaggio);
    }
}

package controller;

import facade.DocenteFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;

public class InserisciVotoController {
    @FXML private TextField Matricola;
    @FXML private TextField NomeCorso;
    @FXML private TextField Voto;
    @FXML private DatePicker DataEsame;
    @FXML private CheckBox Assente;
    @FXML private CheckBox Lode;
    @FXML private Label ErrorArea;

    private DocenteFacade docenteFacade;
    private String CFLoggato;

    @FXML
    public void initialize(){
        docenteFacade = new DocenteFacade();
        ErrorArea.setText("");
    }

    public void initData(String CF){
        this.CFLoggato = CF;
    }

    @FXML
    public void InviaVoto(ActionEvent event){
        String matricola = Matricola.getText();
        String corso = NomeCorso.getText();
        String voto = Voto.getText();
        LocalDate data = DataEsame.getValue();
        boolean assente = Assente.isSelected();
        boolean lode = Lode.isSelected();

        if(matricola == null || corso == null || data == null){
            mostraErrore("Compila tutti i campi.");
            return;
        }

        int votoFinale = 0;

        // Controllo se "Assente" sia spuntato
        if (!assente){
            // Se "Assente" non è spuntata allora deve esserci un voto
            if(voto == null){
                mostraErrore("Inserisci un voto.");
                return;
            }

            // Se tutto è okay procedo con i controlli sul voto
            try {
                votoFinale = Integer.parseInt(voto); // Converto il voto da String a int

                if(votoFinale < 18 || votoFinale > 30){
                    mostraErrore("Il voto deve essere tra 18 e 30.");
                    return;
                }

                // Se c'e' la lode il voto deve essere 30
                if(lode && votoFinale != 30){
                    mostraErrore("Il voto deve essere 30 per avere la lode.");
                    return;
                }
            } catch (NumberFormatException e){
                mostraErrore("Il voto deve essere un numero intero.");
                return;
            }
        }

        // Se è tutto okay pulisco l'area di errore e salvo i dati nel DB richimando il facade
        ErrorArea.setText("");

        try {
            docenteFacade.inserisciVoto(CFLoggato, matricola, corso, data.toString(), votoFinale, assente, lode);

            // Se lo studente risulta assente, anche se il docente inserisce per sbaglio un voto, il backend lo capisce
            // e ignora del tutto il voto, inserendo null come valore.
            if(assente){
                String Messaggio = "Studente registrato come assente,\nvoto ingorato.";
                mostraSuccesso(Messaggio);
            }else {
                String Messaggio = "Voto inserito correttamente.";
                mostraSuccesso(Messaggio);
            }
        }catch (SQLException e){
            mostraErrore("Errore inserimento, riprova.");
        }
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

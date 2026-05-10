package controller;

import auth.PasswordHandler;
import auth.UserExistsHandler;
import dao.AuthDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    // Con questi "collego" le funzioni della GUI con quelle della logica implementata fino ad ora
    @FXML private TextField UsernameField;
    @FXML private PasswordField PasswordField;
    @FXML private Label ErrorLabel;

    // Metodo che verra' richiamato quando si clicca il pulsante Accedi
    @FXML
    public void gestisciLogin(ActionEvent event) throws SQLException {
        String Username = UsernameField.getText();
        String Password = PasswordField.getText();

        // Richiamo la logica "backend" con il design pattern Handler
        AuthDAO authDAO = new AuthDAO();
        UserExistsHandler ceckUser = new UserExistsHandler(authDAO);
        PasswordHandler ceckPassword = new PasswordHandler(ceckUser);
        ceckUser.setNext(ceckPassword);

        boolean logiSuccess = ceckUser.handle(Username, Password);

        if(logiSuccess){
            // Ritrovo il ruolo in base all'Username
            String Ruolo = ruolo(Username);

            // Usa il ruolo dal CredenzialiDTO nel login, non la lunghezza dell’username.

            // In base al ruolo rimando al file fxml corretto
            String fileFxml = "";
            switch (Ruolo){
                case "SEGRETERIA": fileFxml = "/resources/DashboardSegreteria.fxml"; break;
                case "DOCENTE": fileFxml = "/resources/DashboardDocente.fxml"; break;
                case "STUDENTE": fileFxml = "/resources/DashboardStudente.fxml"; break;
            }
            cambiaFinestra(event, fileFxml, Username);

        } else{
            ErrorLabel.setStyle("-fx-text-fill: red;");
            ErrorLabel.setText("Credenziali errate o utente non trovato.");
        }

    }

    private String ruolo(String Username){
        if(Username.equals("1")) return "SEGRETERIA";
        if(Username.length() == 16) return "DOCENTE";
        return "STUDENTE";
    }

    private void cambiaFinestra(ActionEvent event, String fileFxml, String identificativo){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fileFxml));
            Parent root = loader.load();

            // INIEZIONE DEI DATI: Capiamo quale controller è stato caricato e passiamo il dato
            Object controller = loader.getController();
            if (controller instanceof StudenteController) {
                ((StudenteController) controller).initData(identificativo);
            }
            // (Se in futuro servirà l'ID alla Segreteria o al Docente, aggiungerai gli 'else if' qui)

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
            ErrorLabel.setText("Errore nel caricamento dell'interfaccia.");
        }
    }
}

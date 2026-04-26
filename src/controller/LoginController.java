package controller;

import auth.PasswordHandler;
import auth.UserExistsHandler;
import dao.AuthDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    // Con questi "collego" le funzioni della GUI con quelle della logica implementata fino ad ora
    @FXML private TextField UsernameField;
    @FXML private PasswordField PasswordField;
    @FXML private Label ErrorLabel;

    // Metodo che verra' richiamato quando si clicca il pulsante Accedi
    @FXML
    public void gestisciLogin(ActionEvent event){
        String Username = UsernameField.getText();
        String Password = PasswordField.getText();

        // Richiamo la logica "backend" che usa i diversi design patterns
        AuthDAO authDAO = new AuthDAO();
        UserExistsHandler ceckUser = new UserExistsHandler(authDAO);
        PasswordHandler ceckPassword = new PasswordHandler(ceckUser);
        ceckUser.setNext(ceckPassword);

        boolean logiSuccess = ceckUser.handle(Username, Password);

        if(logiSuccess){
            ErrorLabel.setStyle("-fx-text-fill: green;");
            ErrorLabel.setText("Login effettuato! (Apertura dashboard in corso...)");

            // TODO: Qua chiameremo il Builder per la sessione e cambieremo schermata
        } else{
            ErrorLabel.setStyle("-fx-text-fill: red;");
            ErrorLabel.setText("Credenziali errate o utente non trovato.");
        }

    }
}

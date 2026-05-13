package controller;

import auth.PasswordHandler;
import auth.UserExistsHandler;
import dao.AuthDAO;
import model.CredenzialiDTO;

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

    @FXML
    public void gestisciLogin(ActionEvent event) throws SQLException {
        String Username = UsernameField.getText();
        String Password = PasswordField.getText();

        AuthDAO authDAO = new AuthDAO();

        UserExistsHandler checkUser = new UserExistsHandler(authDAO);
        PasswordHandler checkPassword = new PasswordHandler();

        checkUser.setNext(checkPassword);

        CredenzialiDTO UtenteAutenticato = checkUser.handle(Username, Password, null);

        if(UtenteAutenticato != null){
            String Ruolo = UtenteAutenticato.getRuolo();
            String FileFxml = "";

            switch(Ruolo){
                case "SEGRETERIA":
                    FileFxml = "/resources/DashboardSegreteria.fxml";
                    break;
                case "DOCENTE":
                    FileFxml = "/resources/DashboardDocente.fxml";
                    break;
                case "STUDENTE":
                    FileFxml = "/resources/DashboardStudente.fxml";
                    break;
                default:
                    ErrorLabel.setStyle("-fx-text-fill: red;");
                    ErrorLabel.setText("Ruolo non riconosciuto");
                    return;
            }

            cambiaFinestra(event, FileFxml, Username, UtenteAutenticato.getRuolo());
        } else {
            ErrorLabel.setStyle("-fx-text-fill: red;");
            ErrorLabel.setText("Credenziali errate o utente non trovato.");
        }
    }

    private void cambiaFinestra(ActionEvent event, String fileFxml, String identificativo, String Ruolo){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fileFxml));
            Parent root = loader.load();

            // Se e' uno studente a fare il login, passo anche la matricola al controller
            Object controller = loader.getController();
            if (controller instanceof StudenteController) {
                ((StudenteController) controller).initData(identificativo);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("DASHBOARD " + Ruolo);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
            ErrorLabel.setText("Errore nel caricamento dell'interfaccia.");
        }
    }
}

package facade;

import database.DatabaseManager;
import strategy.SearchStrategy;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SegreteriaFacade {

    // Inseriamo un nuovo studente
    public void iscriviStudente(String Matricola, String Password, String Nome, String Cognome, String Residenza) {
        String sql = "INSERT INTO Studente (Matricola, Password, Nome, Cognome, Residenza) VALUES (?, ?, ?, ?, ?)";

        // Prendiamo la connessione FUORI dal try-with-resources
        Connection conn = DatabaseManager.getInstance().getConnection();

        // Mettiamo solo lo statement dentro le parentesi!
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, Matricola);
            stmt.setString(2, Password);
            stmt.setString(3, Nome);
            stmt.setString(4, Cognome);
            stmt.setString(5, Residenza);

            stmt.executeUpdate();
            System.out.println("Successo: Studente " + Nome + " " + Cognome + " iscritto correttamente!");
        } catch (Exception e) {
            System.err.println("Errore durante l'iscrizione: La matricola potrebbe già esistere.");
        }
    }

    // Ricercare uno studente usando lo Strategy Pattern
    public void visualizzaStudente(SearchStrategy strategia, String inputDiRicerca){
        System.out.println("Ricerca in corso...");
        strategia.cerca(inputDiRicerca);
    }

    // Cambia piano di studi ad uno studente
    public void cambiaPianoStudi(String Matricola, String NomeCorso) {
        String sql = "INSERT INTO Deve_seguire (Matricola, NomeCorso) VALUES (?, ?)";
        Connection conn = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, Matricola);
            stmt.setString(2, NomeCorso);
            stmt.executeUpdate();
            System.out.println("Piano di studi aggiornato: Aggiunto " + NomeCorso + " allo studente " + Matricola);
        } catch (Exception e) {
            System.err.println("Errore aggiornamento piano studi (il corso esiste?).");
        }
    }
}

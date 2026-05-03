package facade;

import database.DatabaseManager;
import strategy.SearchStrategy;

import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SegreteriaFacade {

    // Inseriamo un nuovo studente
    public void iscriviStudente(String Matricola, String Password, String Nome, String Cognome, String Residenza, Date DataNascita) throws SQLException {
        String sql = "INSERT INTO Studente (Matricola, Password, Nome, Cognome, Residenza, DataNascita) VALUES (?, ?, ?, ?, ?, ?)";

        // Prendiamo la connessione FUORI dal try-with-resources
        Connection conn = DatabaseManager.getInstance().getConnection();

        // Mettiamo solo lo statement dentro le parentesi!
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, Matricola);
            stmt.setString(2, Password);
            stmt.setString(3, Nome);
            stmt.setString(4, Cognome);
            stmt.setString(5, Residenza);
            stmt.setDate(6, DataNascita);

            stmt.executeUpdate();
            System.out.println("Successo: Studente " + Nome + " " + Cognome + " iscritto correttamente!");
        } catch (Exception e) {
            System.err.println("Errore durante l'iscrizione: La matricola potrebbe già esistere.");
        } finally {
            conn.close();
        }
    }

    // Ricercare uno studente usando lo Strategy Pattern
    public String visualizzaStudente(SearchStrategy strategia, String inputDiRicerca) throws SQLException {
        System.out.println("Ricerca in corso...");
        System.out.println(inputDiRicerca);
        return strategia.cerca(inputDiRicerca);
    }

    // Cambia piano di studi a uno studente
    public void cambiaPianoStudi(String Matricola, String NomeCorso) throws SQLException {
        String sql = "INSERT INTO DeveSeguire (MatricolaStudente, NomeCorso) VALUES (?, ?)";
        Connection conn = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, Matricola);
            stmt.setString(2, NomeCorso);
            stmt.executeUpdate();
            System.out.println("Piano di studi aggiornato: Aggiunto " + NomeCorso + " allo studente " + Matricola);
        } catch (Exception e) {
            System.err.println("Errore aggiornamento piano studi (il corso esiste?).");
        } finally {
            conn.close();
        }
    }
}

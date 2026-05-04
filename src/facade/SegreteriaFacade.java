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

            // Lascio un log per assicurarmi che vada tutto correttamente durante l'inserimento
            System.out.println("Successo: Studente " + Nome + " " + Cognome + " iscritto correttamente!");
        } catch (SQLException e) {
            //System.err.println("Errore durante l'iscrizione: La matricola potrebbe già esistere."); Prima stampavo l'errore sulla console, ora passo l'errore alla GUI
            throw e;
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

    // Aggiungi un corso da seguire ad uno studente
    public void aggiungiCorso(String Matricola, String NomeCorso) throws SQLException {
        String sql = "INSERT INTO DeveSeguire (MatricolaStudente, NomeCorso) VALUES (?, ?)";

        // Dichiarando la connessione nelle parentesi del blocco try, quest'ultima viene chiusa in automatico non appena il blocco try finisce/lancia un eccezione
        try(Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, Matricola);
            stmt.setString(2, NomeCorso);

            // Va in errore se l'abbinamento esiste gia' oppure se uno dei due campi non esiste nelle tabelle a cui fanno riferimento le Foreign Key
            stmt.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

    // Rimuovo un corso dal piano di studi di uno studente
    public void eliminaCorso(String Matricola, String NomeCorso) throws SQLException {
        String sql = "DELETE FROM DeveSeguire WHERE MatricolaStudente = ? AND NomeCorso = ?";

        try(Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, Matricola);
            stmt.setString(2, NomeCorso);

            // Salvo il numero di righe modificate nel DB
            int righeModificate = stmt.executeUpdate();

            // Se le righe modificare sono 0, non e' successo nulla, quindi lo studente non aveva il corso assegnato
            if(righeModificate == 0){
                throw new SQLException("Il corso non era presente nel piano di studi!");
            }
        } catch (SQLException e) {
            throw e;
        }
    }
}

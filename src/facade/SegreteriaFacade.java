package facade;

import database.DatabaseManager;
import strategy.SearchStrategy;
import model.StudenteDTO;

import java.sql.*;
import java.util.List;

/**
 * Facade per le operazioni della segreteria (iscrizione studenti, gestione piano studi, verbalizzazione).
 */

public class SegreteriaFacade {

    /**
     * Iscrive un nuovo studente nel sistema.
     * @param Matricola matricola dello studente
     * @param Password password in chiaro
     * @param Nome nome
     * @param Cognome cognome
     * @param Residenza residenza
     * @param DataNascita data di nascita (java.sql.Date)
     * @throws SQLException se la matricola esiste già o errore DB
     */
    public void iscriviStudente(String Matricola, String Password, String Nome, String Cognome, String Residenza, Date DataNascita) throws SQLException {
        String sql = "INSERT INTO Studente (Matricola, Password, Nome, Cognome, Residenza, DataNascita) VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, Matricola);
            stmt.setString(2, Password);
            stmt.setString(3, Nome);
            stmt.setString(4, Cognome);
            stmt.setString(5, Residenza);
            stmt.setDate(6, DataNascita);

            stmt.executeUpdate();

            System.out.println("Successo: Studente " + Nome + " " + Cognome + " iscritto correttamente!");
        } catch (SQLException e) {
            throw e;
        } finally {
            conn.close();
        }
    }

    /**
     * Cerca studenti usando la strategia fornita (per matricola o per nome/cognome).
     * @param strategia strategia di ricerca concreta
     * @param input parametro di ricerca
     * @return lista di DTO degli studenti trovati
     * @throws SQLException se la query fallisce
     */
    public List<StudenteDTO> visualizzaStudente(SearchStrategy strategia, String input) throws SQLException{
        return strategia.cerca(input);
    }

    /**
     * Aggiunge un corso al piano di studi dello studente.
     * @param Matricola matricola dello studente
     * @param NomeCorso nome del corso da aggiungere
     * @throws SQLException se il corso non esiste o è già presente nel piano
     */
    public void aggiungiCorso(String Matricola, String NomeCorso) throws SQLException {

        String sqlCheck = "SELECT Nome FROM Corso WHERE Nome = ?";
        String sql = "INSERT INTO DeveSeguire (MatricolaStudente, NomeCorso) VALUES (?, ?)";

        try(Connection conn = DatabaseManager.getInstance().getConnection()){
            try (PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)){
                stmtCheck.setString(1, NomeCorso);

                // Eseguo la query dentro un if perche' .next ritorna falso se non c'e' nulla
                if(!stmtCheck.executeQuery().next()){
                    throw new SQLException("Il corso '" + NomeCorso + "'\nnon esiste nel sistema.");
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, Matricola);
                stmt.setString(2, NomeCorso);

                // Se questo Corso è gia' presente nel piano di studi scatta l'eccezione per violazione di primary key
                stmt.executeUpdate();
            }
        } catch (SQLException e){
            throw e;
        }
    }

    /**
     * Rimuove un corso dal piano di studi dello studente.
     * @param Matricola matricola dello studente
     * @param NomeCorso nome del corso da rimuovere
     * @throws SQLException se il corso non era presente nel piano
     */
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
                throw new SQLException("Il corso non era presente\nnel piano di studi!");
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Cerca i voti già accettati (in attesa di verbalizzazione) per uno studente.
     * @param Matricola matricola dello studente
     * @return stringa con l'elenco dei voti accettati
     * @throws SQLException se la query fallisce
     */
    public String cercaVotiAccettati(String Matricola) throws SQLException{
        // Uso StringBuilder in moda da concatenare ogni occorrenza della query in un unica stringa
        StringBuilder risultati = new StringBuilder();
        String sql = "SELECT NomeCorso, Voto FROM Esito WHERE Matricola = ? AND Stato = 'Accettato'";

        try(Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, Matricola);
            ResultSet rs = stmt.executeQuery();

            // In questo ciclo "costruisco" la stringa finale che verrà mostrata nella TextArea
            while (rs.next()){
                risultati.append("- Esame: ").append(rs.getString("NomeCorso")).append(" | Voto: ").append(rs.getString("Voto")).append("\n");
            }
        }

        if (risultati.length() == 0){
            return "Nessun voto in attesa di verbalizzazione";
        }

        return risultati.toString();
    }

    /**
     * Verbalizza (cambia stato da 'Accettato' a 'Verbalizzato') tutti i voti accettati dello studente.
     * @param Matricola matricola dello studente
     * @throws SQLException se non ci sono voti da verbalizzare
     */
    public void verbalizzaTutti(String Matricola) throws SQLException{
        String sql = "UPDATE Esito SET Stato = 'Verbalizzato' WHERE Matricola = ? AND Stato = 'Accettato'";

        try(Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, Matricola);

            int righeAggiornate = stmt.executeUpdate();

            if(righeAggiornate == 0){
                throw new SQLException("Nessun voto da verbalizzare trovato per questa matricola");
            }
        }
    }

}

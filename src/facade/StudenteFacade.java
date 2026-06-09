package facade;

import database.DatabaseManager;

import java.sql.*;

/**
        * Facade per le operazioni dello studente (piano studi, appelli, prenotazione, gestione voti).
        */

public class StudenteFacade {

    /**
     * Restituisce il piano di studi formattato dello studente.
     * @param Matricola matricola dello studente
     * @return stringa con l'elenco dei corsi
     * @throws SQLException se si verifica un errore DB
     */
    public String vediPianoStudi(String Matricola) throws SQLException {
        String sql = "SELECT C.Nome, C.CFU, C.Anno FROM DeveSeguire D JOIN Corso C ON D.NomeCorso = C.Nome WHERE D.MatricolaStudente = ?";
        Connection conn = DatabaseManager.getInstance().getConnection();
        StringBuilder Risultato = new StringBuilder();

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, Matricola);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Risultato.append("- ").append(rs.getString("Nome")).append(" (").append(rs.getInt("CFU")).append(" CFU, Anno ").append(rs.getInt("Anno")).append(")\n");
            }

            if(Risultato.length() > 0){
                return Risultato.toString();
            } else {
                return "Nessun corso presente nel piano di studi.";
            }
        } catch (SQLException e) {
            return "Errore caricamento piano di studi: " + e.getMessage();
        } finally {
            conn.close();
        }
    }

    /**
     * Mostra gli appelli dei corsi presenti nel piano di studi dello studente.
     * @param Matricola matricola dello studente
     * @return stringa con gli appelli disponibili
     * @throws SQLException se si verifica un errore DB
     */
    public String visualizzaAppelli(String Matricola) throws SQLException {
        String sql = "SELECT A.NomeCorso, A.Data FROM Appello A " +
                "JOIN DeveSeguire D ON A.NomeCorso = D.NomeCorso " +
                "WHERE D.MatricolaStudente = ?";

        Connection conn = DatabaseManager.getInstance().getConnection();
        StringBuilder Risultato = new StringBuilder();

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, Matricola);
            ResultSet rs = stmt.executeQuery();

            boolean trovato = false;
            while (rs.next()) {
                trovato = true;
                Risultato.append("- Corso: ").append(rs.getString("NomeCorso"))
                        .append(" | Data Appello: ").append(rs.getString("Data")).append("\n");
            }

            if (!trovato) {
                return "Al momento non ci sono appelli\nprogrammati per i corsi del tuo piano di studi.";
            }

            return Risultato.toString();

        } finally {
            conn.close();
        }
    }

    /**
     * Restituisce i voti in attesa di accettazione per lo studente.
     * @param Matricola matricola dello studente
     * @return stringa con i voti in attesa
     * @throws SQLException se si verifica un errore DB
     */
    public String visualizzaVotiAttesa(String Matricola)throws SQLException{
        String sql = "SELECT NomeCorso, Voto, Lode FROM Esito WHERE Matricola = ? AND Stato = 'In Attesa'";
        StringBuilder Risultato = new StringBuilder();

        Connection conn = DatabaseManager.getInstance().getConnection();

        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, Matricola);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                String Lode = "";
                if(rs.getInt("Lode") == 1){ // Se c'e' lode allora la aggiungo alla stringa
                    Lode = " e lode";
                }
                Risultato.append("- Esame: ").append(rs.getString("NomeCorso")).append(" | Voto: ").append(rs.getInt("Voto")).append(Lode).append(".\n");
            }
            return Risultato.toString();
        } catch (SQLException e){
            return "Non ci sono voti in attesa di essere accettati.";
        } finally {
            conn.close();
        }
    }

    /**
     * Prenota lo studente a un appello (se il corso è nel suo piano di studi e l'appello esiste).
     * @param Matricola matricola dello studente
     * @param NomeCorso nome del corso
     * @param DataAppello data dell'appello (formato yyyy-mm-dd)
     * @throws SQLException se il corso non è nel piano, l'appello non esiste o errore DB
     */
    public void prenotaAppello(String Matricola, String NomeCorso, String DataAppello) throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();

        try {
            // Controllo se il corso  e' nel piano di studi dello studente
            String sqlPiano = "SELECT NomeCorso FROM DeveSeguire WHERE MatricolaStudente = ? AND NomeCorso = ?";
            PreparedStatement stmtPiano = conn.prepareStatement(sqlPiano);
            stmtPiano.setString(1, Matricola);
            stmtPiano.setString(2, NomeCorso);
            if(!stmtPiano.executeQuery().next()){
                throw new SQLException("Errore: Il corso non e' presente nel tuo piano di studi.");
            }
            stmtPiano.close();

            // Controllo se esiste un appello di quel corso in quella data
            String sqlAppello = "SELECT NomeCorso FROM Appello WHERE NomeCorso = ? AND Data = ?";
            PreparedStatement stmtAppello = conn.prepareStatement(sqlAppello);
            stmtAppello.setString(1, NomeCorso);
            stmtAppello.setString(2, DataAppello);
            if(!stmtAppello.executeQuery().next()){
                throw new SQLException("Errore: Non esiste alcun appello nella data selezionata.");
            }
            stmtAppello.close();

            // Se entrambi i controlli sono andati a buon fine allora inserico la prenotazione nel DB
            String sql = "INSERT INTO SiPrenota (MatricolaStudente, NomeCorso, DataAppello) VALUES (?, ?, ? )";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, Matricola);
            stmt.setString(2, NomeCorso);
            stmt.setString(3, DataAppello);
            stmt.execute();
            stmt.close();
        } finally {
            conn.close();
        }
    }

    /**
     * Accetta o rifiuta un voto in attesa.
     * @param matricola matricola dello studente
     * @param nomeCorso nome del corso
     * @param accettaVoto true = accetta, false = rifiuta
     * @return messaggio di conferma
     * @throws SQLException se il voto non esiste o errore DB
     */
    public String gestisciVoto(String matricola, String nomeCorso, boolean accettaVoto) throws SQLException {
        String sqlSelect = "SELECT ID, Stato FROM Esito WHERE NomeCorso = ? AND Matricola = ?";
        String nuovoStato = "";
        if (accettaVoto) {
            nuovoStato = "Accettato";
        } else {
            nuovoStato = "Rifiutato";
        }

        Connection conn = DatabaseManager.getInstance().getConnection();

        try {
            PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect);
            stmtSelect.setString(1, nomeCorso);
            stmtSelect.setString(2, matricola);

            ResultSet rs = stmtSelect.executeQuery();

            if (!rs.next()) {
                throw new SQLException("Nessun voto trovato per il corso di " + nomeCorso);
            }

            int idEsito = rs.getInt("ID");
            String statoAttuale = rs.getString("Stato");

            if (!"In Attesa".equals(statoAttuale)) {
                return "Voto gia' accettato!\nIn attesa della verbalizzazione.";
            }

            String sqlUpdate = "UPDATE Esito SET Stato = ? WHERE ID = ?";
            PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
            stmtUpdate.setString(1, nuovoStato);
            stmtUpdate.setInt(2, idEsito);
            stmtUpdate.executeUpdate();
            stmtUpdate.close();

            String esito;
            if (accettaVoto) {
                esito = "accettato";
            } else {
                esito = "rifiutato";
            }

            return "Voto di " + nomeCorso + "\n" + esito + " con successo!";

        } finally {
            conn.close();
        }
    }
}

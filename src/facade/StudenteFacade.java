package facade;

import database.DatabaseManager;
import state.*;

import java.sql.*;

public class StudenteFacade {

    // Visualizza il piano di studi
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

    // Visualizza gli appelli disponibili
    public String visualizzaAppelli(String Matricola) throws SQLException {
        // Mostriamo solo gli appelli dei corsi presenti nel piano di studi dello studente!
        String sql = "SELECT A.NomeCorso, A.Data FROM Appello A " +
                "JOIN DeveSeguire D ON A.NomeCorso = D.NomeCorso " +
                "WHERE D.MatricolaStudente = ?";

        Connection conn = database.DatabaseManager.getInstance().getConnection();
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

    // Metodo per trovare i voti in attesa di essere accettati
    public String visualizzaVotiAttesa(String Matricola)throws SQLException{
        String sql = "SELECT NomeCorso, Voto, Lode FROM Esito WHERE Matricola = ? AND Stato = 'In Attesa'";
        StringBuilder Risultato = new StringBuilder();

        Connection conn = DatabaseManager.getInstance().getConnection();

        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, Matricola);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                String Lode = "";
                if(rs.getInt("Lode") == 1){
                    Lode = " e lode";
                }
                Risultato.append("- Esame: ").append(rs.getString("NomeCorso")).append(" | Voto: ").append(rs.getInt("Voto")).append(Lode).append(".\n");
            }
            return Risultato.toString();
        } catch (SQLException e){
            return "Non ci sono voti in attesa di accettazione.";
        } finally {
            conn.close();
        }
    }

    // Funzione che permette di prenotarsi ad un appello
    public void prenotaAppello(String Matricola, String NomeCorso, String DataAppello) throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();

        try {
            // Controllo se il corso e' nel piano di studi dello studente
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

            // Se entrambi i controlli sono andati a buon fine allora inserico la prenotazione
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

    // Metodo per accettare o rifiutare il voto di un esame, integrato con lo state pattern
    public String gestisciVoto(String Matricola, String NomeCorso, boolean accettaVoto) throws SQLException{
        // Usiamo Matricola e NomeCorso per trovare la riga esatta!
        String sqlSelect = "SELECT ID, Voto, Stato FROM Esito WHERE NomeCorso = ? AND Matricola = ? AND Stato = 'In Attesa'";
        Connection conn = database.DatabaseManager.getInstance().getConnection();

        try {
            PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect);
            stmtSelect.setString(1, NomeCorso);
            stmtSelect.setString(2, Matricola);

            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                int idEsito = rs.getInt("ID");
                int Voto = rs.getInt("Voto");
                String statoDB = rs.getString("Stato");

                Esito esito = new Esito(idEsito, Voto, Matricola);

                switch (statoDB) {
                    case "In Attesa": esito.setStato(new state.StatoInAttesa()); break;
                    case "Accettato": esito.setStato(new state.StatoAccettato()); break;
                    case "Rifiutato": esito.setStato(new state.StatoRifiutato()); break;
                    case "Verbalizzato": esito.setStato(new state.StatoVerbalizzato()); break;
                }

                String statoPrima = esito.getNomeStato();

                // Esegue l'azione dello State Pattern. Se fallisce, lancia l'eccezione al Controller!
                if (accettaVoto) {
                    esito.accetta();
                } else {
                    esito.rifiuta();
                }

                if (!statoPrima.equals(esito.getNomeStato())) {
                    String sqlUpdate = "UPDATE Esito SET Stato = ? WHERE ID = ?";
                    PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);

                    stmtUpdate.setString(1, esito.getNomeStato());
                    stmtUpdate.setInt(2, idEsito);
                    stmtUpdate.executeUpdate();
                    stmtUpdate.close();

                    return "Voto di " + NomeCorso + "\n" + (accettaVoto ? "accettato" : "rifiutato") + " con successo!";
                }

                return "Nessuna modifica effettuata.";

            } else {
                throw new SQLException("Nessun voto in attesa trovato\nper il corso di " + NomeCorso);
            }

        } catch (SQLException e) {
            throw e;
        }catch (IllegalStateException e) {
            // Intercetta il blocco dello State Pattern
            return e.getMessage();
        } finally {
            conn.close();
        }
    }
}

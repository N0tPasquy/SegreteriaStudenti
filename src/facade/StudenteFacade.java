package facade;

import database.DatabaseManager;
import state.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudenteFacade {
    // Funzione che permette di prenotarsi ad un appello
    public void prenotaAppello(String Matricola, String NomeCorso, String DataAppello){
        String sql = "INSERT INTO SiPrenota (MatricolaStudente, NomeCorso, DataAppello) VALUES (?, ?, ?)";
        Connection conn = DatabaseManager.getInstance().getConnection();

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, Matricola);
            stmt.setString(2, NomeCorso);
            stmt.setString(3, DataAppello);

            stmt.executeUpdate();
            System.out.println("Studente " + Matricola + " prenotato con successo all'appello di " + NomeCorso + " del " + DataAppello);
        } catch (SQLException e) {
            System.err.println("Errore dutante la prenotazione (sei gia' prenotato?): " + e.getMessage());
        }
    }

    // Visualizza il piano di studi
    public void vediPianoStudi(String Matricola){
        String sql = "SELECT C.Nome, C.CFU, C.Anno FROM DeveSeguire D JOIN Corso C ON D.NomeCorso = C.Nome WHERE D.MatricolaStudente = ?";
        Connection conn = DatabaseManager.getInstance().getConnection();

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, Matricola);
            ResultSet rs = stmt.executeQuery();

            System.out.println("--- Piano di Studi per la matricola " + Matricola + " ---");
            boolean trovato = false;
            while (rs.next()) {
                trovato = true;
                System.out.println("- " + rs.getString("Nome") + " (" + rs.getInt("CFU") + " CFU, Anno " + rs.getInt("Anno") + ")");
            }

            if(!trovato){
                System.out.println("Nessun corso presente nel piano di studi.");
            }
        } catch (SQLException e) {
            System.err.println("Errore caricamento piano di studi: " + e.getMessage());
        }
    }

    // Metodo per accettare o rifiutare il voto di un esame, integrato con lo state pattern
    public void gestisciVoto(int IDEsito, String Matricola, boolean accettaVoto) {
        // Recupero l'esito dal db
        String sqlSelect = "SELECT Voto, Stato FROM Esito WHERE ID = ? AND Matricola = ?";
        Connection conn = DatabaseManager.getInstance().getConnection();

        try(PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {
            stmtSelect.setInt(1, IDEsito);
            stmtSelect.setString(2, Matricola);

            ResultSet rs = stmtSelect.executeQuery();

            if(rs.next()){
                int Voto = rs.getInt("Voto");
                String statoDB = rs.getString("Stato");

                // Ricostruisco l'oggetto Esito
                Esito esito = new Esito(IDEsito, Voto, Matricola);

                // Mappo la stringa dal DB sull'oggetto Stato corretto
                switch (statoDB){
                    case "In Attesa": esito.setStato(new StatoInAttesa()); break;
                    case "Accettato": esito.setStato(new StatoAccettato()); break;
                    case "Rifiutato": esito.setStato(new StatoRifiutato()); break;
                    case "Verbalizzato": esito.setStato(new StatoVerbalizzato()); break;
                }

                // Provo ad eseguire l'azione richiesta
                String statoPrima = esito.getNomeStato(); // Salvo lo stato iniziale
                if(accettaVoto){
                    esito.accetta();
                } else{
                    esito.rifiuta();
                }

                // Se l'azione di prima ha modificato lo stato dell'oggetto aggiorno il db
                if(!statoPrima.equals(esito.getNomeStato())){
                    String sqlUpdate = "UPDATE Esito SET Stato = ? WHERE ID = ?";

                    try(PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                        stmtUpdate.setString(1, esito.getNomeStato());
                        stmtUpdate.setInt(2, IDEsito);
                        stmtUpdate.executeUpdate();
                        System.out.println("Database aggiornato: nuovo stato dell'esito -> " + esito.getNomeStato());
                    }
                }
            } else{
                System.out.println("Errore: Esito non trovato o non appartenente a questa matricola.");
            }
        } catch (SQLException e){
            System.err.println("Errore DB in gestisciVoto: " + e.getMessage());
        }
    }
}

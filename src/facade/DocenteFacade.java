package facade;

import database.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DocenteFacade {
    // Inserisce un nuovo appello nel db
    public void creaAppello(String NomeCorso, String Data){
        String sql = "INSERT INTO Appello (NomeCorso, Data) VALUES (?, ?)";
        Connection conn = DatabaseManager.getInstance().getConnection();

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, NomeCorso);
            stmt.setString(2, Data);

            stmt.executeUpdate();
            System.out.println("Appello creato con successo per il corso di " + NomeCorso + " in data " + Data);
        } catch (SQLException e) {
            System.out.println("Errore durante la creazione dell'appello (esiste gia'?): " + e.getMessage());
        }
    }

    // Visualizza la lista degli studenti prenotati tramite una JOIN
    public void visualizzaPrenotati(String NomeCorso, String DataAppello){
        String sql = "SELECT S.Matricola, S.Nome, S.Cognome " +
                "FROM Studente S " +
                "JOIN SiPrenota P ON S.Matricola = P.MatricolaStudente " +
                "WHERE P.NomeCorso = ? AND P.DataAppello = ?";

        Connection conn = DatabaseManager.getInstance().getConnection();

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, NomeCorso);
            stmt.setString(2, DataAppello);

            ResultSet rs = stmt.executeQuery();
            boolean ciSonoPrenotati = false;

            System.out.println("--- Studenti prenotati all'appello di " + NomeCorso + " del " + DataAppello + " ---");
            while (rs.next()){
                ciSonoPrenotati = true;
                System.out.println("- " + rs.getString("Nome") + " " + rs.getString("Cognome") + " (Matricola: " + rs.getString("Matricola") + ")");
            }
            if(!ciSonoPrenotati){
                System.out.println("Nessuno studente prenotato a questo appello.");
            }
        } catch (SQLException e) {
            System.out.println("Errore durante il recupero delle prenotazioni: " + e.getMessage());
        }
    }

    public void inserisciVoto(String Matricola, String NomeCorso, String DataAppello, int Voto, boolean assente){
        String sql = "INSERT INTO Esito (Voto, Stato, Tipo, Matricola, NomeCorso, Data) VALUES (?, ?, 'Scritto', ?, ?, ?)";
        Connection conn = DatabaseManager.getInstance().getConnection();

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            if(assente){
                stmt.setNull(1, java.sql.Types.INTEGER);
                stmt.setString(2, "Assente"); // Chiudiamo subito il ciclo se e' assente
            } else {
                stmt.setInt(1, Voto);
                stmt.setString(2, "In Attesa"); // Il triggher per lo State Pattern lato Studente
            }

            stmt.setString(3, Matricola);
            stmt.setString(4, NomeCorso);
            stmt.setString(5, DataAppello);

            stmt.executeUpdate();

            if(assente){
                System.out.println("Studente " + Matricola + " registrato come Assente.");
            } else {
                System.out.println("Voto di " + Voto + " registrato per lo studente " + Matricola + ". In attesa di essere accettato.");
            }
        } catch(SQLException e) {
            System.out.println("Errore dutante l'inserimento del voto: " + e.getMessage());
        }
    }
}

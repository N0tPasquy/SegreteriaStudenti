package facade;

import database.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DocenteFacade {
    // Inserisce un nuovo appello nel db
    public void creaAppello(String NomeCorso, String Data) throws SQLException {
        String sqlCheck = "SELECT Nome FROM Corso WHERE Nome = ?";
        String sql = "INSERT INTO Appello (NomeCorso, Data) VALUES (?, ?)";
        Connection conn = DatabaseManager.getInstance().getConnection();

        // Controllo che il corso esista nel DB
        try (PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)) {
            stmtCheck.setString(1, NomeCorso);
            if(!stmtCheck.executeQuery().next()){
                throw new SQLException("Impossibile creare l'appello: il corso\n'" + NomeCorso + "' non esiste nel sistema.");
            }
        }

        // Se il corso esiste procedu con la creazione dell'appello
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, NomeCorso);
            stmt.setString(2, Data);

            stmt.executeUpdate();
            //System.out.println("Appello creato con successo per il corso di " + NomeCorso + " in data " + Data);
        } catch (SQLException e) {
            throw e;
        } finally {
            conn.close();
        }
    }

    // Visualizza la lista degli studenti prenotati tramite una JOIN
    public String visualizzaPrenotati(String NomeCorso, String DataAppello) throws SQLException {
        String sql = "SELECT S.Matricola, S.Nome, S.Cognome " +
                "FROM Studente S " +
                "JOIN SiPrenota P ON S.Matricola = P.MatricolaStudente " +
                "WHERE P.NomeCorso = ? AND P.DataAppello = ?";

        StringBuilder Risultati = new StringBuilder();

        Connection conn = DatabaseManager.getInstance().getConnection();

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, NomeCorso);
            stmt.setString(2, DataAppello);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                Risultati.append("- ").append(rs.getString("Nome")).append(" ").append(rs.getString("Cognome")).append(" (Matricola: ").append(rs.getString("Matricola")).append(")\n");
            }

        } catch (SQLException e) {
           throw e;
        } finally {
            conn.close();
        }

        if (Risultati.length() == 0){
            return "Nessuno studente prenotato all'appello di " + NomeCorso + " del " + DataAppello + ".";
        }

        return Risultati.toString();
    }

    public void inserisciVoto(String Matricola, String NomeCorso, String DataAppello, int Voto, boolean assente, boolean Lode) throws SQLException {
        String sql = "INSERT INTO Esito (Voto, Lode, Stato, Tipo, Matricola, NomeCorso, Data) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseManager.getInstance().getConnection();

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            if(assente){
                stmt.setNull(1, java.sql.Types.INTEGER); // Voto null se assente
                stmt.setInt(2, 0);  // Lode false se assente
                stmt.setString(3, "Assente"); // Chiudiamo subito il ciclo se è assente
            } else {
                stmt.setInt(1, Voto);
                stmt.setInt(2, Lode ? 1 : 0); // Se Lode e' true al DB passo 1, altrimenti passo 0
                stmt.setString(3, "In Attesa"); // Il triggher per lo State Pattern lato Studente
            }

            stmt.setString(4, "Orale"); // Per semplicita' il tipo di esame sara' sempre orale.
            stmt.setString(5, Matricola);
            stmt.setString(6, NomeCorso);
            stmt.setString(7, DataAppello);

            stmt.executeUpdate();

        } catch(SQLException e) {
            throw e;
        } finally {
            conn.close();
        }
    }
}

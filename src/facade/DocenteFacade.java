package facade;

import database.DatabaseManager;
import java.sql.*;

public class DocenteFacade {

    public void creaAppello(String CFDocente, String NomeCorso, String Data) throws SQLException {

        // Controllo che il docente appartenga al corso tramite un metodo apposito
        if(!cekcCorso(CFDocente, NomeCorso)){
            throw new SQLException("Impossibile creare l'appello:\nNon appartieni al corso '" + NomeCorso + "'.");
        }

        // Query per l'inserimento dell'appello
        String sqlInsert = "INSERT INTO Appello (NomeCorso, Data) VALUES (?, ?)";
        Connection conn = DatabaseManager.getInstance().getConnection();

        // Procedo con la creazione dell'appello tramite statement
        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
            stmtInsert.setString(1, NomeCorso);
            stmtInsert.setString(2, Data);

            stmtInsert.executeUpdate();}
        catch (SQLException e) {
            throw e;
        } finally {
            conn.close();
        }
    }

    // Visualizza la lista degli studenti prenotati ad un appello
    public String visualizzaPrenotati(String CFDocente, String NomeCorso, String DataAppello) throws SQLException {

        if(!cekcCorso(CFDocente, NomeCorso)){
            throw new SQLException("Impossibile creare l'appello:\nNon appartieni al corso '" + NomeCorso + "'.");
        }

        String sql = "SELECT S.Matricola, S.Nome, S.Cognome " +
                "FROM Studente S " +
                "JOIN SiPrenota P ON S.Matricola = P.MatricolaStudente " +
                "WHERE P.NomeCorso = ? AND P.DataAppello = ?";

        StringBuilder Risultati = new StringBuilder();
        Connection conn = DatabaseManager.getInstance().getConnection();

        // Cerco i prenotati tramite statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, NomeCorso);
            stmt.setString(2, DataAppello);

            try (ResultSet rs = stmt.executeQuery()) {
                // Fin quando ci sono risultati costruisco la stringa
                while (rs.next()) {
                    Risultati.append("- ").append(rs.getString("Nome")).append(" ").append(rs.getString("Cognome")).append(" (Matricola: ").append(rs.getString("Matricola")).append(")\n");
                }
            }
        }
        catch (SQLException e) {
            throw e;
        } finally {
            conn.close();
        }

        // Controllo che la stringa Risultati non sia vuota
        if (Risultati.length() == 0) {
            return "Nessuno studente prenotato all'appello di:\n" + NomeCorso + "\ndel " + DataAppello + ".";
        }

        return Risultati.toString();
    }

    public void inserisciVoto(String CFDocente, String Matricola, String NomeCorso, String DataAppello, int Voto, boolean assente, boolean Lode) throws SQLException {

        if(!cekcCorso(CFDocente, NomeCorso)){
            throw new SQLException("Impossibile creare l'appello:\nNon appartieni al corso '" + NomeCorso + "'.");
        }

        String sql = "INSERT INTO Esito (Voto, Lode, Stato, Tipo, Matricola, NomeCorso, Data) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseManager.getInstance().getConnection();

        // Procedo all'inserimento dell'esito tenendo conto se sia assente o meno
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (assente) {
                stmt.setNull(1, java.sql.Types.INTEGER); // Voto null se assente
                stmt.setInt(2, 0);  // Lode false se assente
                stmt.setString(3, "Assente");
            } else {
                stmt.setInt(1, Voto);
                stmt.setInt(2, Lode ? 1 : 0);
                stmt.setString(3, "In Attesa");
            }

            // Resto dei dati invariato in entrambi i casi
            stmt.setString(4, "Orale");
            stmt.setString(5, Matricola);
            stmt.setString(6, NomeCorso);
            stmt.setString(7, DataAppello);

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        } finally {
            conn.close();
        }
    }

    private boolean cekcCorso(String CFDocente, String NomeCorso) throws SQLException {
        String sqlCheck = "SELECT 1 FROM Tiene WHERE CFDocente = ? AND NomeCorso = ?";
        Connection conn = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)) {
            stmtCheck.setString(1, CFDocente);
            stmtCheck.setString(2, NomeCorso);

            // Se non ci sono risultati significa che il docente non appartiene al corso, quindi ritorno falso
            if (!stmtCheck.executeQuery().next()) {
                return false;
            }

        } catch (SQLException e){
            throw e;
        } finally {
            conn.close();
        }

        // Se non entra nell' if allora ci sono risultati il docente appartiene al corso, quindi ritorno true
        return true;
    }
}

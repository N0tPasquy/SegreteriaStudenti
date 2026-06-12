package facade;

import database.DatabaseManager;
import java.sql.*;

/**
 * Facade per le operazioni del docente (creazione appelli, inserimento voti, visualizzazione prenotati).
 */

public class DocenteFacade {

    /**
     * Crea un nuovo appello per un corso di cui il docente è titolare.
     * @param CFDocente codice fiscale del docente
     * @param NomeCorso nome del corso
     * @param Data data dell'appello (yyyy-mm-dd)
     * @throws SQLException se il docente non tiene il corso o errore DB
     */

    public void creaAppello(String CFDocente, String NomeCorso, String Data) throws SQLException {

        if(!ceckCorso(CFDocente, NomeCorso)){
            throw new SQLException("Impossibile creare l'appello:\nNon appartieni al corso '" + NomeCorso + "'.");
        }

        // Query per l'inserimento dell'appello
        String sqlInsert = "INSERT INTO Appello (NomeCorso, Data) VALUES (?, ?)";
        Connection conn = DatabaseManager.getInstance().getConnection();

        // Creazione dell'appello tramite statement
        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
            stmtInsert.setString(1, NomeCorso);
            stmtInsert.setString(2, Data);

            stmtInsert.executeUpdate();
        } finally {
            conn.close();
        }
    }

    /**
     * Restituisce l'elenco degli studenti prenotati a un dato appello.
     * @param CFDocente codice fiscale del docente
     * @param NomeCorso nome del corso
     * @param DataAppello data dell'appello
     * @return stringa formattata con l'elenco degli studenti
     * @throws SQLException se il docente non tiene il corso o errore DB
     */
    public String visualizzaPrenotati(String CFDocente, String NomeCorso, String DataAppello) throws SQLException {

        if(!ceckCorso(CFDocente, NomeCorso)){
            throw new SQLException("Impossibile visualizzare i prenotati:\nnon risulti essere il titolare del corso selezionato.");
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
                    Risultati.append("- ").append(rs.getString("Nome")).append(" ").append(rs.getString("Cognome"))
                            .append(" (Matricola: ").append(rs.getString("Matricola")).append(")\n");
                }
            }
        } finally {
            conn.close();
        }

        if (Risultati.isEmpty()) {
            return "Nessuno studente prenotato all'appello di:\n" + NomeCorso + "\ndel " + DataAppello + ".";
        }

        return Risultati.toString();
    }

    /**
     * Inserisce un voto (o registra un'assenza) per uno studente a un determinato appello.
     * @param CFDocente codice fiscale del docente
     * @param Matricola matricola dello studente
     * @param NomeCorso nome del corso
     * @param DataAppello data dell'appello
     * @param Voto voto numerico (ignorato se assente = true)
     * @param assente true se lo studente è assente
     * @param Lode true se viene concessa la lode (solo con voto 30)
     * @throws SQLException se il docente non tiene il corso o errore DB
     */
    public void inserisciVoto(String CFDocente, String Matricola, String NomeCorso, String DataAppello, int Voto, boolean assente, boolean Lode) throws SQLException {

        if(!ceckCorso(CFDocente, NomeCorso)){
            throw new SQLException("Impossibile inserire il voto:\nNon appartieni al corso '" + NomeCorso + "'.");
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
        } finally {
            conn.close();
        }
    }

    private boolean ceckCorso(String CFDocente, String NomeCorso) throws SQLException {
        String sqlCheck = "SELECT 1 FROM Tiene WHERE CFDocente = ? AND NomeCorso = ?";
        Connection conn = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)) {
            stmtCheck.setString(1, CFDocente);
            stmtCheck.setString(2, NomeCorso);

            // Se non ci sono risultati significa che il docente non appartiene al corso, quindi ritorno falso
            if (!stmtCheck.executeQuery().next()) {
                return false;
            }

        } finally {
            conn.close();
        }

        // Se non entra nell' if allora ci sono risultati, il docente appartiene al corso, quindi ritorno true
        return true;
    }
}

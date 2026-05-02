package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static DatabaseManager instance;
    private static final String URL = "jdbc:sqlite:segreteria.db";

    private DatabaseManager(){
        creaTabelle();
    }

    public static DatabaseManager getInstance(){
        if(instance == null){
            instance = new DatabaseManager();
        }
        return instance;
    }

    // Genera una nuova connessione ogni volta che viene chiamato!
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    private void creaTabelle() {
        String sqlSegreteria = "CREATE TABLE IF NOT EXISTS Segreteria (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Nome VARCHAR(50) NOT NULL, " +
                "Password VARCHAR(255) NOT NULL)";

        String sqlDocente = "CREATE TABLE IF NOT EXISTS Docente (" +
                "CF VARCHAR(16) PRIMARY KEY, " +
                "Password VARCHAR(255) NOT NULL, " +
                "Nome VARCHAR(50) NOT NULL, " +
                "Cognome VARCHAR(50) NOT NULL, " +
                "ID INTEGER NOT NULL," +
                "FOREIGN KEY (ID) REFERENCES Segreteria(ID));";

        String sqlCorso = "CREATE TABLE IF NOT EXISTS Corso (" +
                "Nome VARCHAR(255) PRIMARY KEY, " +
                "CFU INTEGER NOT NULL, " +
                "Anno INTEGER NOT NULL);";

        String sqlStudente = "CREATE TABLE IF NOT EXISTS Studente (" +
                "Matricola VARCHAR(10) PRIMARY KEY, " +
                "Password VARCHAR(255) NOT NULL, " +
                "Nome VARCHAR(50) NOT NULL, " +
                "Cognome VARCHAR(50) NOT NULL, " +
                "Residenza VARCHAR(50) NOT NULL, " +
                "DataNascita DATE, " +
                "TassePagate BOOLEAN DEFAULT 1, " +
                "ID INTEGER, " +
                "FOREIGN KEY (ID) REFERENCES Segreteria(ID));";

        String sqlAppello = "CREATE TABLE IF NOT EXISTS Appello (" +
                "NomeCorso VARCHAR(255), " +
                "Data DATE, " +
                "FOREIGN KEY (NomeCorso) REFERENCES Corso(Nome), " +
                "PRIMARY KEY (NomeCorso, Data));";

        String sqlEsito = "CREATE TABLE IF NOT EXISTS Esito (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Voto INTEGER, " +
                "Stato VARCHAR(30) NOT NULL, " +
                "Tipo VARCHAR(30) NOT NULL DEFAULT 'Scritto', " +
                "NomeCorso VARCHAR(255), " +
                "Data DATE, " +
                "Matricola VARCHAR(10), " +
                "FOREIGN KEY (NomeCorso) REFERENCES Appello(NomeCorso), " +
                "FOREIGN KEY (Data) REFERENCES Appello(Data), " +
                "FOREIGN KEY (Matricola) REFERENCES Studente(Matricola));";

        String sqlTiene = "CREATE TABLE IF NOT EXISTS Tiene (" +
                "CFDocente VARCHAR(16), " +
                "NomeCorso VARCHAR(255), " +
                "FOREIGN KEY (CFDocente) REFERENCES Docente(CF), " +
                "FOREIGN KEY (NomeCorso) REFERENCES Corso(Nome), " +
                "PRIMARY KEY (CFDocente, NomeCorso));";

        String sqlDeveSeguire = "CREATE TABLE IF NOT EXISTS DeveSeguire(" +
                "MatricolaStudente VARCHAR(10), " +
                "NomeCorso VARCHAR(255), " +
                "FOREIGN KEY (MatricolaStudente) REFERENCES Studente(Matricola), " +
                "FOREIGN KEY (NomeCorso) REFERENCES Corso(Nome), " +
                "PRIMARY KEY (MatricolaStudente, NomeCorso));";

        String sqlSiPrenota = "CREATE TABLE IF NOT EXISTS SiPrenota(" +
                "MatricolaStudente VARCHAR(10), " +
                "DataAppello DATE, " +
                "NomeCorso VARCHAR(255), " +
                "FOREIGN KEY (MatricolaStudente) REFERENCES Studente(Matricola), " +
                "FOREIGN KEY (DataAppello) REFERENCES Appello(Data), " +
                "FOREIGN KEY (NomeCorso) REFERENCES Appello(NomeCorso), " +
                "PRIMARY KEY (MatricolaStudente, DataAppello, NomeCorso));";

        try(Connection conn = getConnection();
            Statement stmt = conn.createStatement()){
                stmt.execute(sqlSegreteria);
                stmt.execute(sqlDocente);
                stmt.execute(sqlCorso);
                stmt.execute(sqlStudente);
                stmt.execute(sqlEsito);
                stmt.execute(sqlAppello);
                stmt.execute(sqlTiene);
                stmt.execute(sqlDeveSeguire);
                stmt.execute(sqlSiPrenota);

                System.out.println("Tabelle del database inizializzate con successo.");
        } catch (SQLException e) {
            System.err.println("Errore durante la creazione delle tabelle: " + e.getMessage());
        }
    }
}

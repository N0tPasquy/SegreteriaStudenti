import auth.PasswordHandler;
import auth.UserExistsHandler;
import dao.AuthDAO;
import database.DatabaseManager;
import facade.DocenteFacade;
import facade.SegreteriaFacade;
import facade.StudenteFacade;
import model.UtenteSessione;
import strategy.SearchByMatricola;
import strategy.SearchByName;

import java.sql.Connection;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== INIZIALIZZAZIONE SISTEMA ===");
        // 1. Inizializzo il DB (Singleton) e preparo i dati essenziali
        DatabaseManager.getInstance();
        preparaDatiDiBase();

        // 2. Istanzio i Facade
        SegreteriaFacade segreteria = new SegreteriaFacade();
        DocenteFacade docente = new DocenteFacade();
        StudenteFacade studenteFacade = new StudenteFacade();

        System.out.println("\n=== FASE 1: LOGIN E BUILDER ===");
        AuthDAO authDAO = new AuthDAO();
        UserExistsHandler checkUser = new UserExistsHandler(authDAO);
        PasswordHandler checkPassword = new PasswordHandler(checkUser);
        checkUser.setNext(checkPassword);

        // Simuliamo il login della Segreteria
        boolean loginSuccess = checkUser.handle("1", "admin123");
        if (loginSuccess) {
            // Usiamo il BUILDER per creare la sessione
            UtenteSessione sessione = new UtenteSessione.Builder("ADMIN_SEG", "SEGRETERIA")
                    .conNome("Segreteria Studenti")
                    .conCognome("Bianchi")
                    .build();
            System.out.println(sessione.getDettagliSessione());
        }

        System.out.println("\n=== FASE 2: OPERAZIONI SEGRETERIA (Facade + Strategy) ===");
        // La segreteria iscrive un nuovo studente
        segreteria.iscriviStudente("MATR999", "pass123", "Luca", "Neri", "Milano");
        // Gli assegna il piano di studi
        segreteria.cambiaPianoStudi("MATR999", "Programmazione 3");

        // Cerca lo studente con lo Strategy Pattern
        System.out.print("Ricerca per Nome: ");
        segreteria.visualizzaStudente(new SearchByName(), "Luca Neri");
        System.out.print("Ricerca per Matricola: ");
        segreteria.visualizzaStudente(new SearchByMatricola(), "MATR999");

        System.out.println("\n=== FASE 3: OPERAZIONI DOCENTE E STUDENTE ===");
        // Il docente crea l'appello
        docente.creaAppello("Programmazione 3", "2026-06-20");

        // Lo studente consulta il piano di studi e si prenota
        studenteFacade.vediPianoStudi("MATR999");
        studenteFacade.prenotaAppello("MATR999", "Programmazione 3", "2026-06-20");

        // Il docente visualizza chi si è prenotato e inserisce il voto
        docente.visualizzaPrenotati("Programmazione 3", "2026-06-20");
        docente.inserisciVoto("MATR999", "Programmazione 3", "2026-06-20", 28, false);

        System.out.println("\n=== FASE 4: STATE PATTERN ED ECCEZIONI PERSONALIZZATE ===");
        // Lo studente vede il voto "In Attesa" (essendo il primo e unico voto, l'ID nel DB sarà 1)
        System.out.println("-> Lo studente tenta di ACCETTARE il voto...");
        studenteFacade.gestisciVoto(1, "MATR999", true); // true = accetta

        System.out.println("\n-> Lo studente cambia idea e tenta di RIFIUTARE il voto appena accettato...");
        // Qui lo State Pattern bloccherà l'azione lanciando l'AzioneNonPermessaException!
        studenteFacade.gestisciVoto(1, "MATR999", false); // false = rifiuta

        System.out.println("\n=== SIMULAZIONE COMPLETATA CON SUCCESSO ===");
    }

    // Metodo helper per inserire nel DB un corso e gli utenti amministrativi di base per il test
    private static void preparaDatiDiBase() {
        Connection conn = DatabaseManager.getInstance().getConnection();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT OR IGNORE INTO Segreteria (ID, Nome, Password) VALUES (1, 'Segreteria Centrale', 'admin123')");
            stmt.execute("INSERT OR IGNORE INTO Docente (CF, Password, Nome, Cognome, ID) VALUES ('DOC123', 'passDoc', 'Mario', 'Verdi', 1)");
            stmt.execute("INSERT OR IGNORE INTO Corso (Nome, CFU, Anno) VALUES ('Programmazione 3', 9, 2026)");
        } catch (Exception e) {
            System.err.println("Errore preparazione dati base: " + e.getMessage());
        }
    }
}
import database.DatabaseManager;
import facade.DocenteFacade;
import facade.SegreteriaFacade;
import facade.StudenteFacade;

import java.sql.Connection;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        // 0. Inizializza il DB vuoto
        DatabaseManager.getInstance();
        preparaDatiDiBase(); // Inserisce un corso e uno studente di prova

        DocenteFacade docente = new DocenteFacade();
        StudenteFacade studente = new StudenteFacade();

        System.out.println("\n=== INIZIO SIMULAZIONE ===");

        // 1. Il Docente crea un appello
        System.out.println("\n--- 1. DOCENTE CREA APPELLO ---");
        docente.creaAppello("Programmazione 3", "2026-06-20");

        // 2. Lo Studente si prenota
        System.out.println("\n--- 2. STUDENTE SI PRENOTA ---");
        studente.prenotaAppello("MATR123", "Programmazione 3", "2026-06-20");

        // 3. Il Docente visualizza i prenotati e inserisce il voto
        System.out.println("\n--- 3. DOCENTE REGISTRA VOTO ---");
        docente.visualizzaPrenotati("Programmazione 3", "2026-06-20");
        docente.inserisciVoto("MATR123", "Programmazione 3", "2026-06-20", 30, false);

        // 4. Lo Studente vede il voto e lo accetta (l'ID sarà 1 perché il DB è appena nato)
        System.out.println("\n--- 4. STUDENTE GESTISCE VOTO ---");
        studente.gestisciVoto(1, "MATR123", true);

        // 5. Test di blocco dello State Pattern
        System.out.println("\n--- 5. TEST STATE PATTERN (Prova a rifiutare) ---");
        studente.gestisciVoto(1, "MATR123", false);
    }

    // Metodo helper per inserire i dati strettamente necessari per evitare errori di Foreign Key
    // Metodo helper corretto!
    private static void preparaDatiDiBase() {
        // 1. Prendiamo la connessione FUORI dal try
        Connection conn = DatabaseManager.getInstance().getConnection();

        // 2. Mettiamo solo lo Statement dentro le parentesi
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT OR IGNORE INTO Corso (Nome, CFU, Anno) VALUES ('Programmazione 3', 9, 2026)");
            stmt.execute("INSERT OR IGNORE INTO Studente (Matricola, Password, Nome, Cognome, Residenza) VALUES ('MATR123', 'pass', 'Mario', 'Rossi', 'Napoli')");
        } catch (Exception e) {
            System.err.println("Errore preparazione dati base: " + e.getMessage());
        }
    }
}
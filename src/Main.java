import database.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Avvio del sistema Segreteria Studenti...");

        // Chiamando getInstance(), il costruttore privato verrà eseguito,
        // verrà creato il file .db e verranno generate le tabelle.
        DatabaseManager db = DatabaseManager.getInstance();

        if (db.getConnection() != null) {
            System.out.println("Connessione al database stabilita correttamente!");
        } else {
            System.out.println("Errore: impossibile stabilire la connessione.");
        }
    }
}
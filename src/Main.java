import facade.SegreteriaFacade;
import strategy.SearchByMatricola;
import strategy.SearchByName;

public class Main {
    public static void main(String[] args) {
        // Istanziamo il nostro Facade
        SegreteriaFacade segreteria = new SegreteriaFacade();

        System.out.println("--- 1. TEST ISCRIZIONE ---");
        segreteria.iscriviStudente("MATR999", "pass123", "Giulia", "Verdi", "Roma");

        System.out.println("\n--- 2. TEST RICERCA (Strategy) ---");
        // Ricerca con la strategia Matricola
        System.out.println("> Cerco per Matricola:");
        segreteria.visualizzaStudente(new SearchByMatricola(), "MATR999");

        // Ricerca con la strategia Nome e Cognome
        System.out.println("> Cerco per Nome/Cognome:");
        segreteria.visualizzaStudente(new SearchByName(), "Mario Rossi"); // Questo l'avevi inserito prima!

        System.out.println("\n--- 3. TEST PIANO DI STUDI ---");
        // (Nota: Per questo test, dovrebbe prima esistere il corso "Informatica" nella tabella Corso)
        // segreteria.cambiaPianoStudi("MATR999", "Informatica");
    }
}
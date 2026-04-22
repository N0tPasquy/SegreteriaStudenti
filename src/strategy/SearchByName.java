package strategy;

public class SearchByName extends SearchByMatricola {
    @Override
    public void cerca(String nomeCognome){
        // Sepriamo nome e cognome (l' input deve essere del tipo "Mario Rossi")
        String[] parti = nomeCognome.split(" ", 2);
        if (parti.length < 2){
            System.out.println("Inserire sia nome che cognome separati da sapzio.");
            return;
        }

        String sql = "SELECT * FROM Studente WHERE Nome = ? AND Cognome = ?";
        eseguiRicerca(sql, parti[0], parti[1]);
    }
}

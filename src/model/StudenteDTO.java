package model;

public class StudenteDTO {
    private final String Nome;
    private final String Cognome;
    private final String Matricola;

    public StudenteDTO(String nome, String cognome, String matricola){
        Nome = nome;
        Cognome = cognome;
        Matricola = matricola;
    }

    public String getNome(){
        return Nome;
    }

    public String getCognome(){
        return Cognome;
    }

    public String getMatricola(){
        return Matricola;
    }
}

package model;

public class OperatoreSanitario {
    private String CF;
    private Ruoli tipo;
    private String username;

    public String getCF() {
        return CF;
    }

    public void setCF(String CF) {
        this.CF = CF;
    }

    public Ruoli getTipo() {
        return tipo;
    }

    public void setTipo(Ruoli tipo) {
        this.tipo = tipo;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

package Model;

import Controller.Ruoli;

public class OperatoreSanitario {
    private String CF;
    private Ruoli tipo;

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
}

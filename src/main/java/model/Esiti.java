package model;

public enum Esiti {
    POSITIVO("Positivo"),
    GUARITO("Guarito"),
    DECESSO("Decesso"),
    RICOVERATO("Ricoverato"),
    TERAPIA_INTENSIVA("Terapia intensiva");

    private final String esito;

    private Esiti(String esito) {
        this.esito = esito;
    }

    public String toString() {
        return this.esito;
    }
}

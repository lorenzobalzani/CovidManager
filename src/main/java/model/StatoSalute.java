package model;

public class StatoSalute {
    String dataInizio;
    String dataFine;
    Esiti esito;

    public String getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(String dataInizio) {
        this.dataInizio = dataInizio;
    }

    public String getDataFine() {
        return dataFine;
    }

    public void setDataFine(String dataFine) {
        this.dataFine = dataFine;
    }

    public Esiti getEsito() {
        return esito;
    }

    public void setEsito(Esiti esito) {
        this.esito = esito;
    }
}

package model;

public class Cittadino {
    private String CF;
    private String nome;
    private String cognome;
    private String dataDiNascita;
    private String genere;
    private String comuneResidenza;
    private String telefono;
    private StatoSalute statoSalute;

    public String getCF() {
        return CF;
    }

    public void setCF(String CF) {
        this.CF = CF;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getDataDiNascita() {
        return dataDiNascita;
    }

    public void setDataDiNascita(String dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public String getComuneResidenza() {
        return comuneResidenza;
    }

    public void setComuneResidenza(String comuneResidenza) {
        this.comuneResidenza = comuneResidenza;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public StatoSalute getStatoSalute() {
        return statoSalute;
    }

    public void setStatoSalute(StatoSalute statoSalute) {
        this.statoSalute = statoSalute;
    }

    @Override
    public String toString() {
        return nome + " " + cognome;
    }
}

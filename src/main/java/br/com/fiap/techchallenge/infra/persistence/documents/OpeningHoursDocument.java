package br.com.fiap.techchallenge.infra.persistence.documents;

public class OpeningHoursDocument {
    private String opens;
    private String closes;

    public OpeningHoursDocument() {}

    public OpeningHoursDocument(String opens, String closes) {
        this.opens = opens;
        this.closes = closes;
    }

    public String getOpens() {
        return opens;
    }

    public void setOpens(String opens) {
        this.opens = opens;
    }

    public String getCloses() {
        return closes;
    }

    public void setCloses(String closes) {
        this.closes = closes;
    }
}

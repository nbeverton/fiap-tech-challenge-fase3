package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.entity;

public class OpeningHoursEntity {
    private String opens;
    private String closes;

    public OpeningHoursEntity() {}

    public OpeningHoursEntity(String opens, String closes) {
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


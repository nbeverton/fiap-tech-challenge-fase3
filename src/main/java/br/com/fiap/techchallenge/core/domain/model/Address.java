package br.com.fiap.techchallenge.core.domain.model;

public class Address {

    private final String id;
    private String postalCode;
    private String streetName;
    private int streetNumber;
    private String additionalInfo;
    private String neighborhood;
    private String city;
    private String stateProvince;
    private String country;


    public Address(
            String id,
            String postalCode,
            String streetName,
            int streetNumber,
            String additionalInfo,
            String neighborhood,
            String city,
            String stateProvince,
            String country) {

        this.id = id;
        this.postalCode = postalCode;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.additionalInfo = additionalInfo;
        this.neighborhood = neighborhood;
        this.city = city;
        this.stateProvince = stateProvince;
        this.country = country;
    }


    public Address(String postalCode,
                   String streetName,
                   int streetNumber,
                   String additionalInfo,
                   String neighborhood,
                   String city,
                   String stateProvince,
                   String country) {

        this.id = null;
        this.postalCode = postalCode;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.additionalInfo = additionalInfo;
        this.neighborhood = neighborhood;
        this.city = city;
        this.stateProvince = stateProvince;
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getCity() {
        return city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public String getCountry() {
        return country;
    }


    public void updatePostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void updateStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void updateStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void updateAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public void updateNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void updateCity(String city) {
        this.city = city;
    }

    public void updateStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public void updateCountry(String country) {
        this.country = country;
    }
}
